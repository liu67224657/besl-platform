package com.enjoyf.webapps.joyme.webpage.controller.joymeapp;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.message.MessageServiceSngl;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.HttpClientManager;
import com.enjoyf.platform.util.http.HttpParameter;
import com.enjoyf.platform.util.http.HttpResult;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.webapps.common.ResultListMsg;
import com.enjoyf.platform.webapps.common.ResultObjectMsg;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-20
 * Time: 下午1:45
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/joymeapp/update")
public class JoymeAppUpdateController extends JoymeAppBaseController {

    @ResponseBody
    @RequestMapping(value = "/getversion")
    public String reportInstall(HttpServletRequest request,
                                @RequestParam(value = "appkey", required = false) String appkey,
                                @RequestParam(value = "platform", required = false) String platformStr,
                                @RequestParam(value = "version", required = false) String versionStr,
                                @RequestParam(value = "channelid", required = false) String channel,
                                @RequestParam(value = "clientid", required = false) String clientid) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rs", ResultObjectMsg.CODE_E);
        jsonObject.put("msg", "");
        jsonObject.put("result", new JSONObject());
        try {
            if (StringUtil.isEmpty(appkey)) {
                jsonObject.put("rs", ResultObjectMsg.CODE_E);
                jsonObject.put("msg", "param.appkey.is.empty");
                jsonObject.put("result", new JSONObject());
                return jsonObject.toString();
            }
            int platform = StringUtil.isEmpty(platformStr) ? getPlatformByAppKey(appkey) : Integer.parseInt(platformStr);
            if (platform == -1) {
                jsonObject.put("rs", ResultObjectMsg.CODE_E);
                jsonObject.put("msg", "appkey.A/I.error");
                jsonObject.put("result", new JSONObject());
                return jsonObject.toString();
            }
            //是否是企业版
            Integer enterpriser = AppEnterpriserType.getEnterpriser(appkey);
            appkey = getAppKey(appkey);

            AppDeployment appVersion = JoymeAppConfigServiceSngl.get().getAppDeploymentByCache(appkey, platform, AppDeploymentType.VERSION.getCode(), channel, enterpriser);
            if (appVersion == null) {
                appVersion = JoymeAppConfigServiceSngl.get().getAppDeploymentByCache(appkey, platform, AppDeploymentType.VERSION.getCode(), "", enterpriser);
            }
            if (appVersion != null) {
                AppVersionInfo version = new AppVersionInfo(appVersion.getAppkey(), appVersion.getTitle(), appVersion.getPath(), appVersion.getDescription());
                version.setUpdate_type(appVersion.getAppVersionUpdateType().getCode());

                if (!StringUtil.isEmpty(versionStr)) {
                    String[] currArr = versionStr.split("\\.");
                    if (!CollectionUtil.isEmpty(currArr)) {
                        if (version != null && !StringUtil.isEmpty(version.getVersion())) {
                            String[] newArr = version.getVersion().split("\\.");

                            for (int i = 0; i < currArr.length; i++) {
                                if (i >= newArr.length) {
                                    break;
                                }
                                if (!StringUtil.isEmpty(currArr[i]) && !StringUtil.isEmpty(newArr[i])) {
                                    int curr = Integer.valueOf(currArr[i]);
                                    int ver = Integer.valueOf(newArr[i]);
                                    if (curr < ver) {
                                        jsonObject.put("rs", ResultObjectMsg.CODE_S);
                                        jsonObject.put("msg", "success");

                                        JSONObject versionObj = new JSONObject();
                                        versionObj.put("version", version.getVersion());
                                        versionObj.put("app_key", version.getApp_key());
                                        versionObj.put("version_url", version.getVersion_url());
                                        versionObj.put("update_type", version.getUpdate_type());
                                        versionObj.put("version_info", version.getVersion_info());
                                        jsonObject.put("result", versionObj);
                                        break;
                                    }
                                }
                            }
                        } else {
                            jsonObject.put("rs", ResultObjectMsg.CODE_E);
                            jsonObject.put("msg", "no.need.to.update");
                            jsonObject.put("result", new JSONObject());
                        }
                    } else {
                        jsonObject.put("rs", ResultObjectMsg.CODE_E);
                        jsonObject.put("msg", "success");
                        jsonObject.put("result", new JSONObject());
                    }
                } else {
                    jsonObject.put("rs", ResultObjectMsg.CODE_E);
                    jsonObject.put("msg", "success");
                    jsonObject.put("result", new JSONObject());
                }
            }

            //TODO 动漫历史上的今天
            if (appkey.equals("0G30ZtEkZ4vFBhAfN7Bx4v") ||
                    appkey.equals("1zBwYvQpt3AE6JsykiA2es")) {
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(PushMessageField.APPKEY, appkey));
                queryExpress.add(QueryCriterions.eq(PushMessageField.PUSHLISTTYPE, PushListType.ANIME_HISTORYDAY.getCode()));
                queryExpress.add(QuerySort.add(PushMessageField.SENDDATE, QuerySortOrder.DESC));
                queryExpress.add(QueryCriterions.eq(PushMessageField.PUSHSTATUS, ActStatus.UNACT.getCode()));
                queryExpress.add(QueryCriterions.eq(PushMessageField.TAGS, DateUtil.DateToString(new Date(), DateUtil.PATTERN_DATE)));
                PushMessage pushMessage = MessageServiceSngl.get().getPushMessage(queryExpress);


                if (pushMessage != null) {
                    jsonObject.put("rs", ResultObjectMsg.CODE_S);
                    jsonObject.put("msg", "success");

                    JSONObject history = new JSONObject();
                    PushMessageOption option = pushMessage.getOptions().getList().get(0);
                    history.put("title", pushMessage.getMsgSubject());
                    history.put("desc", pushMessage.getShortMessage());
                    history.put("ji", StringUtil.isEmpty(option.getInfo()) ? "" : option.getInfo());
                    history.put("jt", option.getType() + "");
                    history.put("historyid", pushMessage.getPushMsgId() + "");
                    jsonObject.put("history", history);
                }

            }


            //TODO 海贼迷广告判断
            String value = MiscServiceSngl.get().getAdvertiseInfo(appkey + "_" + clientid);
            if (!StringUtil.isEmpty(value)) {
                HttpParameter[] params = new HttpParameter[]{
                        new HttpParameter("appid", appkey),
                        new HttpParameter("idfa", clientid),
                };
                String ainfoAdwalkercn = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getAinfoAdwalkercn();
                HttpResult httpResult = new HttpClientManager().get(ainfoAdwalkercn, params);
                GAlerter.lan("getversion reponseCode=" + httpResult.getReponseCode() + ",result=" + httpResult.getResult());
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            jsonObject.put("rs", ResultObjectMsg.CODE_E);
            jsonObject.put("msg", "system.error");
            return jsonObject.toString();
        }
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping(value = "/getcontent")
    public String reportPageView(HttpServletRequest request,
                                 @RequestParam(value = "appkey", required = false) String appkey,
                                 @RequestParam(value = "versionnum", required = false) Long version) {
        ResultListMsg resultMsg = new ResultListMsg(ResultListMsg.CODE_S);
        try {
            if (StringUtil.isEmpty(appkey) || version == null) {
                resultMsg.setRs(ResultListMsg.CODE_E);
                resultMsg.setMsg("param.is.empty");
                return JsonBinder.buildNormalBinder().toJson(resultMsg);
            }

            appkey = getAppKey(appkey);
            List<AppContentVersionInfo> infoList = JoymeAppConfigServiceSngl.get().queryContentVersionInfoByAppKey(appkey, version);

            resultMsg.setResult(infoList);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            resultMsg.setRs(ResultListMsg.CODE_E);
            resultMsg.setMsg("system.error");
        }

        return JsonBinder.buildNormalBinder().toJson(resultMsg);
    }

}
