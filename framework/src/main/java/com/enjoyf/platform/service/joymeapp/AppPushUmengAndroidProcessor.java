package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebApiHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.event.system.AppPushEvent;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.util.umeng.android.AndroidFilecast;
import com.enjoyf.platform.util.umeng.android.AndroidGroupcast;
import com.enjoyf.platform.util.umeng.android.AndroidUnicast;
import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-9-2
 * Time: 上午9:42
 * To change this template use File | Settings | File Templates.
 */
public class AppPushUmengAndroidProcessor extends AbstractAppPushProcessor implements AppPushProcessor {
    private static final Logger logger = LoggerFactory.getLogger(AppPushUmengAndroidProcessor.class);

    @Override
    public void sendPushMessage(AppPushEvent appPushEvent) {
        String toolsAppId = "";//pushMessage.getAppKey() + "A";
        if (appPushEvent.getPlatform().getCode() == 1) {
            toolsAppId = getAppKey(appPushEvent.getAppKey()) + "A";
        }

        String umengAppKey = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getUmengAppKey(toolsAppId);
        String umengMasterSecret = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getUmengMasterSecret(toolsAppId);
        if (StringUtil.isEmpty(umengAppKey) || StringUtil.isEmpty(umengMasterSecret)) {
            logger.info("AppPushUmengAndroidProcessor sendPushMessage hotdeploy null");
            return;
        }
        String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000));
        String validationToken = DigestUtils.md5Hex(umengAppKey.toLowerCase() + umengMasterSecret.toLowerCase() + timestamp);
        try {
            if (!StringUtil.isEmpty(appPushEvent.getDevices())) {
                Set<String> deviceSet = new HashSet<String>();
                String[] deviceArr = appPushEvent.getDevices().split(" ");
                for (String device : deviceArr) {
                    if (!StringUtil.isEmpty(device)) {
                        deviceSet.add(device);
                    }
                }
                if (!CollectionUtil.isEmpty(deviceSet)) {
                    logger.info("---------------------------------------" + deviceSet.toString() + "-----------------------------------");
                    if (deviceSet.size() == 1) {
                        String device = "";
                        for (String token : deviceSet) {
                            if (!StringUtil.isEmpty(token)) {
                                device = token;
                            }
                        }
                        if (!StringUtil.isEmpty(device)) {
                            AndroidUnicast unicast = new AndroidUnicast();
                            unicast.setPredefinedKeyValue("appkey", umengAppKey);
                            unicast.setPredefinedKeyValue("timestamp", timestamp);
                            unicast.setPredefinedKeyValue("validation_token", validationToken);

                            //上传多个device tokens,用 '\n' 隔开
                            unicast.setPredefinedKeyValue("device_tokens", device);

                            unicast.setPredefinedKeyValue("ticker", appPushEvent.getSubject());
                            unicast.setPredefinedKeyValue("title", appPushEvent.getSubject());
                            unicast.setPredefinedKeyValue("text", appPushEvent.getContext());
                            unicast.setPredefinedKeyValue("after_open", "go_custom");
                            unicast.setPredefinedKeyValue("display_type", "notification");
                            unicast.setPredefinedKeyValue("description", appPushEvent.getSubject());
                            unicast.setPredefinedKeyValue("play_vibrate", "false");
                            unicast.setPredefinedKeyValue("play_lights", "false");
                            if (StringUtil.isEmpty(appPushEvent.getSound())) {
                                unicast.setPredefinedKeyValue("play_sound", "false");
                            } else {
                                unicast.setPredefinedKeyValue("play_sound", "true");
                            }

                            //"true"生产环境，"false"测试环境
                            int umengPushChannel = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getUmengProductionMode();
                            if (umengPushChannel == 2) {
                                unicast.setPredefinedKeyValue("production_mode", "false");
                            } else if (umengPushChannel == 1) {
                                unicast.setPredefinedKeyValue("production_mode", "true");
                            }

                            //用户自定义内容, "d","p"为友盟保留字段，key不可以是"d","p"
                            String checkStr = "";
                            if (appPushEvent.getAppKey().equals("0VsYSLLsN8CrbBSMUOlLNx")) {
                                //
                            } else {
                                AppPushCustom custom = new AppPushCustom();
                                custom.setJt(String.valueOf(appPushEvent.getJt()));
                                custom.setJi(appPushEvent.getJi());
                                unicast.setPredefinedKeyValue("custom", JsonBinder.buildNormalBinder().toJson(custom));
                                checkStr += String.valueOf(JsonBinder.buildNormalBinder().toJson(custom));
                                //                    filecast.setExtraField("jt", String.valueOf(pushMessage.getOptions().getList().get(0).getType()));
                                //                    filecast.setExtraField("ji", pushMessage.getOptions().getList().get(0).getInfo());
                            }

                            unicast.checkPayload(checkStr);
                            logger.info("AppPushUmengAndroidProcessor unicast:" + JsonBinder.buildNormalBinder().toJson(unicast));
                            boolean bool = unicast.send();
                            if (bool) {
                                UpdateExpress updateExpress = new UpdateExpress();
                                updateExpress.set(PushMessageField.SEND_STATUS, ActStatus.ACTED.getCode());
                                MessageServiceSngl.get().modifyPushMessage(updateExpress, new QueryExpress()
                                        .add(QueryCriterions.eq(PushMessageField.PUSHMSGID, appPushEvent.getPushMsgId())), appPushEvent.getAppKey());
                            } else {
                                UpdateExpress updateExpress = new UpdateExpress();
                                updateExpress.set(PushMessageField.SEND_STATUS, ActStatus.ERROR.getCode());
                                MessageServiceSngl.get().modifyPushMessage(updateExpress, new QueryExpress()
                                        .add(QueryCriterions.eq(PushMessageField.PUSHMSGID, appPushEvent.getPushMsgId())), appPushEvent.getAppKey());
                            }
                        }
                    } else {
                        String uploadContents = "";
                        for (String token : deviceSet) {
                            uploadContents += token + "\n";
                        }

                        AndroidFilecast filecast = new AndroidFilecast();
                        filecast.setPredefinedKeyValue("appkey", umengAppKey);
                        filecast.setPredefinedKeyValue("timestamp", timestamp);
                        filecast.setPredefinedKeyValue("validation_token", validationToken);

                        //上传多个device tokens,用 '\n' 隔开
                        filecast.uploadContents(uploadContents);

                        filecast.setPredefinedKeyValue("ticker", appPushEvent.getSubject());
                        filecast.setPredefinedKeyValue("title", appPushEvent.getSubject());
                        filecast.setPredefinedKeyValue("text", appPushEvent.getContext());
                        filecast.setPredefinedKeyValue("after_open", "go_custom");
                        filecast.setPredefinedKeyValue("display_type", "notification");
                        filecast.setPredefinedKeyValue("description", appPushEvent.getSubject());
                        filecast.setPredefinedKeyValue("play_vibrate", "false");
                        filecast.setPredefinedKeyValue("play_lights", "false");
                        if (StringUtil.isEmpty(appPushEvent.getSound())) {
                            filecast.setPredefinedKeyValue("play_sound", "false");
                        } else {
                            filecast.setPredefinedKeyValue("play_sound", "true");
                        }

                        //"true"生产环境，"false"测试环境
                        int umengPushChannel = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getUmengProductionMode();
                        if (umengPushChannel == 2) {
                            filecast.setPredefinedKeyValue("production_mode", "false");
                        } else if (umengPushChannel == 1) {
                            filecast.setPredefinedKeyValue("production_mode", "true");
                        }

                        //用户自定义内容, "d","p"为友盟保留字段，key不可以是"d","p"
                        String checkStr = "";

                        if (appPushEvent.getAppKey().equals("0VsYSLLsN8CrbBSMUOlLNx")) {
                            //
                        } else {
                            AppPushCustom custom = new AppPushCustom();
                            custom.setJt(String.valueOf(appPushEvent.getJt()));
                            custom.setJi(appPushEvent.getJi());
                            filecast.setPredefinedKeyValue("custom", JsonBinder.buildNormalBinder().toJson(custom));
                            checkStr += String.valueOf(JsonBinder.buildNormalBinder().toJson(custom));
                            //                    filecast.setExtraField("jt", String.valueOf(pushMessage.getOptions().getList().get(0).getType()));
                            //                    filecast.setExtraField("ji", pushMessage.getOptions().getList().get(0).getInfo());
                        }

                        filecast.checkPayload(checkStr);
                        logger.info("AppPushUmengAndroidProcessor fileCast:" + filecast.toString());
                        boolean bool = filecast.send();
                        if (bool) {
                            UpdateExpress updateExpress = new UpdateExpress();
                            updateExpress.set(PushMessageField.SEND_STATUS, ActStatus.ACTED.getCode());
                            MessageServiceSngl.get().modifyPushMessage(updateExpress, new QueryExpress()
                                    .add(QueryCriterions.eq(PushMessageField.PUSHMSGID, appPushEvent.getPushMsgId())), appPushEvent.getAppKey());
                        } else {
                            UpdateExpress updateExpress = new UpdateExpress();
                            updateExpress.set(PushMessageField.SEND_STATUS, ActStatus.ERROR.getCode());
                            MessageServiceSngl.get().modifyPushMessage(updateExpress, new QueryExpress()
                                    .add(QueryCriterions.eq(PushMessageField.PUSHMSGID, appPushEvent.getPushMsgId())), appPushEvent.getAppKey());
                        }
                    }
                }
            } else {
                AndroidGroupcast groupcast = new AndroidGroupcast();
                groupcast.setPredefinedKeyValue("appkey", umengAppKey);
                groupcast.setPredefinedKeyValue("timestamp", timestamp);
                groupcast.setPredefinedKeyValue("validation_token", validationToken);
                //filter
                if (!StringUtil.isEmpty(appPushEvent.getVersion()) || !StringUtil.isEmpty(appPushEvent.getChannel()) || !StringUtil.isEmpty(appPushEvent.getTags())) {
                    JSONObject filterJson = new JSONObject();
                    JSONObject whereJson = new JSONObject();

                    JSONArray tagArray = new JSONArray();

                    if (!StringUtil.isEmpty(appPushEvent.getVersion())) {
                        JSONObject versionTag = new JSONObject();
                        JSONArray versionTagArray = new JSONArray();

                        String[] versionList = appPushEvent.getVersion().split("\\|");
                        for (String ver : versionList) {
                            if (!StringUtil.isEmpty(ver)) {
                                JSONObject version = new JSONObject();
                                version.put("app_version", ver);
                                versionTagArray.put(version);
                            }
                        }
                        versionTag.put("or", versionTagArray);
                        tagArray.put(versionTag);
                    }
                    if (!StringUtil.isEmpty(appPushEvent.getChannel())) {
                        JSONObject channelTag = new JSONObject();
                        JSONArray channelTagArray = new JSONArray();

                        String[] channelList = appPushEvent.getChannel().split("\\|");
                        for (String chan : channelList) {
                            if (!StringUtil.isEmpty(chan)) {
                                JSONObject channel = new JSONObject();
                                channel.put("channel", chan);
                                channelTagArray.put(channel);
                            }
                        }
                        channelTag.put("or", channelTagArray);
                        tagArray.put(channelTag);
                    }
                    if (!StringUtil.isEmpty(appPushEvent.getTags())) {
                        JSONObject tagFilter = new JSONObject();
                        JSONArray tagFilterArray = new JSONArray();

                        String[] tagList = appPushEvent.getTags().split("\\|");
                        for (String tag : tagList) {
                            JSONObject tagItem = new JSONObject();
                            tagItem.put("tag", tag);
                            tagFilterArray.put(tagItem);
                        }
                        tagFilter.put("or", tagFilterArray);
                        tagArray.put(tagFilter);
                    }
                    whereJson.put("and", tagArray);
                    filterJson.put("where", whereJson);

                    groupcast.setPredefinedKeyValue("filter", filterJson);
                    logger.info("AppPushUmengAndroidProcessor filter:" + filterJson.toString());
                }
                if (appPushEvent.getSendDate() != null) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    groupcast.setPredefinedKeyValue("start_time", df.format(appPushEvent.getSendDate()));
                }
                groupcast.setPredefinedKeyValue("ticker", appPushEvent.getSubject());
                groupcast.setPredefinedKeyValue("title", appPushEvent.getSubject());
                groupcast.setPredefinedKeyValue("text", appPushEvent.getContext());
                groupcast.setPredefinedKeyValue("after_open", "go_custom");
                groupcast.setPredefinedKeyValue("display_type", "notification");
                groupcast.setPredefinedKeyValue("description", appPushEvent.getSubject());
                groupcast.setPredefinedKeyValue("play_vibrate", "false");
                groupcast.setPredefinedKeyValue("play_lights", "false");
                if (StringUtil.isEmpty(appPushEvent.getSound())) {
                    groupcast.setPredefinedKeyValue("play_sound", "false");
                } else {
                    groupcast.setPredefinedKeyValue("play_sound", "true");
                }

                //"true"生产环境，"false"测试环境
                int umengPushChannel = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getUmengProductionMode();
                if (umengPushChannel == 2) {
                    groupcast.setPredefinedKeyValue("production_mode", "false");
                } else if (umengPushChannel == 1) {
                    groupcast.setPredefinedKeyValue("production_mode", "true");
                }

                //用户自定义内容, "d","p"为友盟保留字段，key不可以是"d","p"
                String checkStr = "";

                if (appPushEvent.getAppKey().equals("0VsYSLLsN8CrbBSMUOlLNx")) {
                    //
                } else {
                    AppPushCustom custom = new AppPushCustom();
                    custom.setJt(String.valueOf(appPushEvent.getJt()));
                    custom.setJi(appPushEvent.getJi());
                    groupcast.setPredefinedKeyValue("custom", JsonBinder.buildNormalBinder().toJson(custom));
                    checkStr += String.valueOf(JsonBinder.buildNormalBinder().toJson(custom));
                    //                    filecast.setExtraField("jt", String.valueOf(pushMessage.getOptions().getList().get(0).getType()));
                    //                    filecast.setExtraField("ji", pushMessage.getOptions().getList().get(0).getInfo());
                }

                groupcast.checkPayload(checkStr);
                logger.info("AppPushUmengAndroidProcessor groupCast:" + groupcast.toString());
                boolean bool = groupcast.send();
                if (bool) {
                    UpdateExpress updateExpress = new UpdateExpress();
                    updateExpress.set(PushMessageField.SEND_STATUS, ActStatus.ACTED.getCode());
                    MessageServiceSngl.get().modifyPushMessage(updateExpress, new QueryExpress()
                            .add(QueryCriterions.eq(PushMessageField.PUSHMSGID, appPushEvent.getPushMsgId())), appPushEvent.getAppKey());
                } else {
                    UpdateExpress updateExpress = new UpdateExpress();
                    updateExpress.set(PushMessageField.SEND_STATUS, ActStatus.ERROR.getCode());
                    MessageServiceSngl.get().modifyPushMessage(updateExpress, new QueryExpress()
                            .add(QueryCriterions.eq(PushMessageField.PUSHMSGID, appPushEvent.getPushMsgId())), appPushEvent.getAppKey());
                }
            }
        } catch (Exception e) {
            GAlerter.lab("AppPushUmengAndroidProcessor sendPushMessage exception.e", e);
        }
    }

    public static void main(String[] args) {
        try {
            String umengAppKey = "55003e8efd98c5f8a5000081";
            String umengMasterSecret = "5qwdnujylpvzfg6ykxtrdzm6hcq5hd0x";
            String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000));
            String validationToken = DigestUtils.md5Hex(umengAppKey.toLowerCase() + umengMasterSecret.toLowerCase() + timestamp);

            AndroidUnicast unicast = new AndroidUnicast();
            unicast.setPredefinedKeyValue("appkey", umengAppKey);
            unicast.setPredefinedKeyValue("timestamp", timestamp);
            unicast.setPredefinedKeyValue("validation_token", validationToken);

            //上传多个device tokens,用 '\n' 隔开
            //An5GhLvSKaBW6h-jv55HgRuQ0Y5i4g93mlkZY2DAc4AU
            //AizNqvlETstNmKqI7DFHUQl1srhFmA5hmYCkbLYPTux0
            unicast.setPredefinedKeyValue("device_tokens", "An5GhLvSKaBW6h-jv55HgRuQ0Y5i4g93mlkZY2DAc4AU");

            unicast.setPredefinedKeyValue("ticker", "ticker");
            unicast.setPredefinedKeyValue("title", "title");
            unicast.setPredefinedKeyValue("text", "text");
            unicast.setPredefinedKeyValue("after_open", "go_custom");
            unicast.setPredefinedKeyValue("display_type", "notification");
            unicast.setPredefinedKeyValue("description", "description");
            unicast.setPredefinedKeyValue("play_vibrate", "false");
            unicast.setPredefinedKeyValue("play_lights", "false");
            unicast.setPredefinedKeyValue("play_sound", "true");

            //"true"生产环境，"false"测试环境
            unicast.setPredefinedKeyValue("production_mode", "false");
            unicast.send();
        } catch (Exception e) {

        }
    }
}
