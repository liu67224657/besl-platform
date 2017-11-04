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
import com.enjoyf.platform.util.umeng.ios.IOSFilecast;
import com.enjoyf.platform.util.umeng.ios.IOSGroupcast;
import com.enjoyf.platform.util.umeng.ios.IOSUnicast;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-9-2
 * Time: 上午9:42
 * To change this template use File | Settings | File Templates.
 */
public class AppPushUmengIosProcessor extends AbstractAppPushProcessor implements AppPushProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AppPushUmengIosProcessor.class);

    @Override
    public void sendPushMessage(AppPushEvent appPushEvent) {
        String toolsAppId = "";// pushMessage.getAppKey() + "I";
        if (appPushEvent.getEnterpriseType().equals(AppEnterpriserType.DEFAULT)) {
            toolsAppId = getAppKey(appPushEvent.getAppKey()) + "I";
        } else if (appPushEvent.getEnterpriseType().equals(AppEnterpriserType.ENTERPRISE)) {
            toolsAppId = getAppKey(appPushEvent.getAppKey()) + "E";
        }

        String umengAppKey = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getUmengAppKey(toolsAppId);
        String umengMasterSecret = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getUmengMasterSecret(toolsAppId);
        GAlerter.lan("-----------------AppPushUmengIosProcessor umeng push umengAppKey:" + umengAppKey + ",umengMasterSecret=" + umengMasterSecret + ",toolsAppId=" + toolsAppId);
        if (StringUtil.isEmpty(umengAppKey) || StringUtil.isEmpty(umengMasterSecret)) {
            logger.info("-----------------AppPushUmengIosProcessor umeng push msgid:" + appPushEvent.getPushMsgId() + " hotdeploy null---------------------");
            GAlerter.lab("-----------------AppPushUmengIosProcessor umeng push msgid:" + appPushEvent.getPushMsgId() + " hotdeploy null---------------------");
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
                            IOSUnicast unicast = new IOSUnicast();
                            unicast.setPredefinedKeyValue("appkey", umengAppKey);
                            unicast.setPredefinedKeyValue("timestamp", timestamp);
                            unicast.setPredefinedKeyValue("validation_token", validationToken);
                            unicast.setPredefinedKeyValue("device_tokens", device);
                            unicast.setPredefinedKeyValue("alert", appPushEvent.getContext());
                            unicast.setPredefinedKeyValue("badge", appPushEvent.getBadge());
                            unicast.setPredefinedKeyValue("sound", appPushEvent.getSound());
                            unicast.setPredefinedKeyValue("description", appPushEvent.getContext());
                            //"true"生产环境，"false"测试环境
                            int umengPushChannel = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getUmengProductionMode();
                            if (umengPushChannel == 2) {
                                unicast.setPredefinedKeyValue("production_mode", "false");
                            } else if (umengPushChannel == 1) {
                                unicast.setPredefinedKeyValue("production_mode", "true");
                            }

                            //用户自定义内容, "d","p"为友盟保留字段，key不可以是"d","p"
                            String checkStr = "" + appPushEvent.getSound() + appPushEvent.getBadge();
                            int len = 0;

                            if (appPushEvent.getAppKey().equals("0VsYSLLsN8CrbBSMUOlLNx")) {
                                unicast.setPredefinedKeyValue("msgtype", appPushEvent.getJt());
                                checkStr += ("msgtype" + appPushEvent.getJt());
                                len += 6;
                                unicast.setPredefinedKeyValue("info", appPushEvent.getJi());
                                checkStr += ("info" + appPushEvent.getJi());
                                len += 6;
                            } else if (appPushEvent.getAppKey().equals("17yfn24TFexGybOF0PqjdY")) {
                                unicast.setPredefinedKeyValue("jt", appPushEvent.getJt());
                                checkStr += ("jt" + appPushEvent.getJt());
                                len += 6;
                                unicast.setPredefinedKeyValue("ji", appPushEvent.getJi());
                                checkStr += ("ji" + appPushEvent.getJi());
                                len += 6;
                            } else {
                                unicast.setPredefinedKeyValue("jt", appPushEvent.getJt());
                                checkStr += ("jt" + appPushEvent.getJt());
                                len += 6;
                                unicast.setPredefinedKeyValue("ji", appPushEvent.getJi());
                                checkStr += ("ji" + appPushEvent.getJi());
                                len += 6;
                            }

                            unicast.checkPayload(checkStr, len);
                            logger.info("AbstractAppPushProcessor unicast:" + unicast.toString());
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
                        IOSFilecast filecast = new IOSFilecast();
                        filecast.setPredefinedKeyValue("appkey", umengAppKey);
                        filecast.setPredefinedKeyValue("timestamp", timestamp);
                        filecast.setPredefinedKeyValue("validation_token", validationToken);

                        //上传多个device tokens,用 '\n' 隔开
                        filecast.uploadContents(uploadContents);

                        filecast.setPredefinedKeyValue("alert", appPushEvent.getContext());
                        filecast.setPredefinedKeyValue("badge", appPushEvent.getBadge());
                        filecast.setPredefinedKeyValue("sound", appPushEvent.getSound());
                        filecast.setPredefinedKeyValue("description", appPushEvent.getContext());
                        //"true"生产环境，"false"测试环境
                        int umengPushChannel = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getUmengProductionMode();
                        if (umengPushChannel == 2) {
                            filecast.setPredefinedKeyValue("production_mode", "false");
                        } else if (umengPushChannel == 1) {
                            filecast.setPredefinedKeyValue("production_mode", "true");
                        }

                        //用户自定义内容, "d","p"为友盟保留字段，key不可以是"d","p"
                        String checkStr = "" + appPushEvent.getSound() + appPushEvent.getBadge();
                        int len = 0;

                        if (appPushEvent.getAppKey().equals("0VsYSLLsN8CrbBSMUOlLNx")) {
                            filecast.setPredefinedKeyValue("msgtype", appPushEvent.getJt());
                            checkStr += ("msgtype" + appPushEvent.getJi());
                            len += 6;
                            filecast.setPredefinedKeyValue("info", appPushEvent.getJi());
                            checkStr += ("info" + appPushEvent.getJi());
                            len += 6;
                        } else {
                            filecast.setPredefinedKeyValue("jt", appPushEvent.getJt());
                            checkStr += ("jt" + appPushEvent.getJt());
                            len += 6;
                            filecast.setPredefinedKeyValue("ji", appPushEvent.getJi());
                            checkStr += ("ji" + appPushEvent.getJi());
                            len += 6;
                        }


                        filecast.checkPayload(checkStr, len);
                        logger.info("AbstractAppPushProcessor filecast:" + filecast.toString());
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
                IOSGroupcast groupcast = new IOSGroupcast();
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
                            JSONObject channel = new JSONObject();
                            channel.put("channel", chan);
                            channelTagArray.put(channel);
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
                    logger.info("-----------------AppPushUmengIosProcessor filter:" + filterJson.toString() + "--------------------------------------------");
                }
                if (appPushEvent.getSendDate() != null) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    groupcast.setPredefinedKeyValue("start_time", df.format(appPushEvent.getSendDate()));
                }

                groupcast.setPredefinedKeyValue("alert", appPushEvent.getContext());
                groupcast.setPredefinedKeyValue("badge", appPushEvent.getBadge());
                groupcast.setPredefinedKeyValue("sound", appPushEvent.getSound());
                groupcast.setPredefinedKeyValue("description", appPushEvent.getContext());
                //"true"生产环境，"false"测试环境
                int umengPushChannel = HotdeployConfigFactory.get().getConfig(WebApiHotdeployConfig.class).getUmengProductionMode();
                if (umengPushChannel == 2) {
                    groupcast.setPredefinedKeyValue("production_mode", "false");
                } else if (umengPushChannel == 1) {
                    groupcast.setPredefinedKeyValue("production_mode", "true");
                }

                //用户自定义内容, "d","p"为友盟保留字段，key不可以是"d","p"
                String checkStr = "" + appPushEvent.getSound() + appPushEvent.getBadge();
                int len = 0;
                if (appPushEvent.getAppKey().equals("0VsYSLLsN8CrbBSMUOlLNx")) {
                    groupcast.setPredefinedKeyValue("msgtype", appPushEvent.getJt());
                    checkStr += ("msgtype" + appPushEvent.getJt());
                    len += 6;
                    groupcast.setPredefinedKeyValue("info", appPushEvent.getJi());
                    checkStr += ("info" + appPushEvent.getJi());
                    len += 6;
                } else {
                    groupcast.setPredefinedKeyValue("jt", appPushEvent.getJt());
                    checkStr += ("jt" + appPushEvent.getJt());
                    len += 6;
                    groupcast.setPredefinedKeyValue("ji", appPushEvent.getJi());
                    checkStr += ("ji" + appPushEvent.getJi());
                    len += 6;
                }

                groupcast.checkPayload(checkStr, len);
                logger.info("AppPushIosProcessor groupcast:" + groupcast.toString());
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
            GAlerter.lab("AppPushUmengIosProcessor sendPushMessage exception.e", e);
        }
    }

    public static void main(String[] args) {
        try {
            String umengAppKey = "546aec60fd98c51020005413";
            String umengMasterSecret = "fiu35duoy24k2vhivxczxyisfs8x0y6h";
            String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000));
            String validationToken = DigestUtils.md5Hex(umengAppKey.toLowerCase() + umengMasterSecret.toLowerCase() + timestamp);

            IOSGroupcast groupcast = new IOSGroupcast();
            groupcast.setPredefinedKeyValue("appkey", umengAppKey);
            groupcast.setPredefinedKeyValue("timestamp", timestamp);
            groupcast.setPredefinedKeyValue("validation_token", validationToken);
                                    /*
                                     *  Construct the filter condition:
                            		 *  "where":
                            		 *	{
                                	 *		"and":
                                	 *		[
                                  	 *			{"tag":"test"},
                                  	 *			{"tag":"Test"}
                                	 *		]
                            		 *	}
                            		 */
            JSONObject filterJson = new JSONObject();
            JSONObject whereJson = new JSONObject();

            JSONArray tagArray = new JSONArray();

            JSONObject versionTag = new JSONObject();
            JSONArray versionTagArray = new JSONArray();

            JSONObject version = new JSONObject();
            version.put("app_version", "1.4.0");
            JSONObject version2 = new JSONObject();
            version2.put("app_version", "1.4.1");
            JSONObject version3 = new JSONObject();
            version3.put("app_version", "1.4.2");
            JSONObject version4 = new JSONObject();
            version4.put("app_version", "1.4.3");
            versionTagArray.put(version);
            versionTagArray.put(version2);
            versionTagArray.put(version3);
            versionTagArray.put(version4);
            versionTag.put("or", versionTagArray);

//            JSONObject channelTag = new JSONObject();
//            JSONArray channelTagArray = new JSONArray();
//            JSONObject channel = new JSONObject();
//            channel.put("channel", "c360");
//            channelTagArray.put(channel);
//            channelTag.put("or", channelTagArray);

            tagArray.put(versionTag);
//            tagArray.put(channelTag);

            whereJson.put("and", tagArray);
            filterJson.put("where", whereJson);
            System.out.println(filterJson.toString());

            groupcast.setPredefinedKeyValue("filter", filterJson);

            groupcast.setPredefinedKeyValue("alert", "15条需要牢记的僵尸末世生存法则。");
            groupcast.setPredefinedKeyValue("badge", 0);
            groupcast.setPredefinedKeyValue("sound", "");

            groupcast.setPredefinedKeyValue("description", "1.29");
//            groupcast.setPredefinedKeyValue("start_time", "2015-01-07 11:00:00");
            //"true"生产环境，"false"测试环境
            groupcast.setPredefinedKeyValue("production_mode", "false");

            groupcast.setPredefinedKeyValue("jt", "0");
            groupcast.setPredefinedKeyValue("ji", "");
            //用户自定义内容, "d","p"为友盟保留字段，key不可以是"d","p"
            groupcast.send();


//            String url = "http://msg.umeng.com/api/tag/add";
//            HttpPost post = new HttpPost(url);
//            post.setHeader("User-Agent", "Mozilla/5.0");
//
//            String umengAppKey = "54898c6ffd98c53e080008d3";
//            String umengMasterSecret = "mmjxaa1x1v7yjhjcyc1m3nqhextcvwv4";
//            String timestamp = Integer.toString((int) (System.currentTimeMillis() / 1000));
//            String validationToken = DigestUtils.md5Hex(umengAppKey.toLowerCase() + umengMasterSecret.toLowerCase() + timestamp);
//
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("appkey", umengAppKey);
//            jsonObject.put("timestamp", timestamp);
//            jsonObject.put("validation_token", validationToken);
//            jsonObject.put("device_tokens", "ea879feb6f25f538b54bb8fe5bbf5e400487b2c59a0371a2eddc681a75612a9d");
//            jsonObject.put("tag", "game00000");
////            jsonObject.put("production_mode", "false");
//
//            StringEntity se = new StringEntity(jsonObject.toString(), "UTF-8");
//            System.out.println(jsonObject.toString());
//            post.setEntity(se);
//            // Send the post request and get the response
//            HttpClient client = new DefaultHttpClient();
//            HttpResponse response = client.execute(post);
//            int status = response.getStatusLine().getStatusCode();
//            System.out.println(status);
//            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//            StringBuffer result = new StringBuffer();
//            String line = "";
//            while ((line = rd.readLine()) != null) {
//                result.append(line);
//            }
//            System.out.println(result.toString());
        } catch (Exception e) {

        }
    }
}
