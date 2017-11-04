package com.enjoyf.webapps.joyme.webpage.controller.joymeapp;

import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.config.AppConfigInfo;
import com.enjoyf.platform.service.joymeapp.config.AppConfigUtil;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.cloudfile.BucketInfo;
import com.enjoyf.platform.cloudfile.FileHandlerFactory;
import com.enjoyf.util.StringUtil;
import net.sf.json.JSONObject;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/1/8
 * Description:
 */
@Controller
@RequestMapping(value = "/joymeapp/baseinfo")
public class JoymeAppBaseInfoController extends JoymeAppBaseController {

    @ResponseBody
    @RequestMapping(value = "/versioninfo")
    public String appinfo(HttpServletRequest request) {
        JSONObject returnObj = new JSONObject();
        try {
            String appkey = HTTPUtil.getParam(request, "appkey");
            String version = HTTPUtil.getParam(request, "version");
            String channel = HTTPUtil.getParam(request, "channelid");
            String platform = HTTPUtil.getParam(request, "platform");
            if (StringUtil.isEmpty(appkey) || StringUtil.isEmpty(channel) || StringUtil.isEmpty(platform)) {
                returnObj.put("rs", ResultCodeConstants.PARAM_EMPTY.getCode());
                returnObj.put("msg", ResultCodeConstants.PARAM_EMPTY.getMsg());
                returnObj.put("result", new JSONObject());
                return returnObj.toString();
            }

            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                returnObj.put("rs", ResultCodeConstants.APP_NOT_EXISTS.getCode());
                returnObj.put("msg", ResultCodeConstants.APP_NOT_EXISTS.getMsg());
                returnObj.put("result", new JSONObject());
                return returnObj.toString();
            }
            JSONObject result = new JSONObject();

            //返回版本信息
            JSONObject rows = getVersion(appkey, Integer.valueOf(platform), channel, version);
            if (rows != null) {
                result.put("rows", rows);
            }

            //返回版本配置项
            JSONObject appinfo = getConfig(appkey, platform, channel, version);
            if (appinfo != null) {
                result.put("appinfo", appinfo);
            }

            //新手引导配置 -- push
            JSONObject freshGuide = getFreshGuide(appkey, platform, channel, version);
            if (freshGuide != null) {
                result.put("freshguide", freshGuide);
            }

            returnObj.put("rs", ResultCodeConstants.SUCCESS.getCode());
            returnObj.put("msg", ResultCodeConstants.SUCCESS.getMsg());
            returnObj.put("result", result);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            returnObj.put("rs", ResultCodeConstants.ERROR.getCode());
            returnObj.put("msg", ResultCodeConstants.ERROR.getMsg());
            returnObj.put("result", new JSONObject());
        }
        return returnObj.toString();
    }

    private JSONObject getFreshGuide(String appkey, String platform, String channel, String version) {
        JSONObject guide = null;
        if (getAppKey(appkey).equals("17yfn24TFexGybOF0PqjdY")) {
            guide = new JSONObject();
            JSONObject message = new JSONObject();
            guide.put("message", message);
        }
        return guide;
    }

    //版本的一些开关项，配置信息
    private JSONObject getConfig(String appkey, String platform, String channel, String versionStr) throws ServiceException {
        if (StringUtil.isEmpty(channel) || StringUtil.isEmpty(platform) || AppPlatform.getByCode(Integer.valueOf(platform)) == null || AppChannelType.getByCode(channel) == null) {
            return null;
        }
        AppConfig appConfig = JoymeAppConfigServiceSngl.get().getAppConfig(AppConfigUtil.getAppConfigId(AppUtil.getAppKey(appkey), platform, versionStr, channel, String.valueOf(AppEnterpriserType.getEnterpriser(appkey))));
        if (appConfig != null) {
            JSONObject appinfo = new JSONObject();
            appinfo.put("shake_open", appConfig.getInfo().getShake_open());//摇一摇
            appinfo.put("defad_url", appConfig.getInfo().getDefad_url());//审核期间的广告
            appinfo.put("gift_open", appConfig.getInfo().getGift_open());//礼包中心开关
            appinfo.put("reddot_interval", appConfig.getInfo().getReddot_interval());//单位:s
            appinfo.put("shake_version", appConfig.getInfo().getShake_version());//摇一摇 0:native,1:wap

            try {
                BucketInfo bucketInfo = BucketInfo.getByCode(appConfig.getBucket());
                String upToken = FileHandlerFactory.getToken(bucketInfo);
                appinfo.put("uptk", upToken);
                appinfo.put("upbk", bucketInfo.getBucket());
                appinfo.put("upcode", bucketInfo.getThirdKey());
                appinfo.put("uphost", bucketInfo.getHost());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return appinfo;
        } else {
            AuthApp authApp = OAuthServiceSngl.get().getApp(AppUtil.getAppKey(appkey));
            appConfig = new AppConfig();
            appConfig.setConfigId(AppConfigUtil.getAppConfigId(AppUtil.getAppKey(appkey), platform, versionStr, channel, String.valueOf(AppEnterpriserType.getEnterpriser(appkey))));
            appConfig.setAppKey(AppUtil.getAppKey(appkey));
            appConfig.setPlatform(AppPlatform.getByCode(Integer.valueOf(platform)));
            appConfig.setVersion(versionStr);
            appConfig.setChannel(channel);
            appConfig.setEnterpriseType(AppEnterpriserType.getByCode(AppEnterpriserType.getEnterpriser(appkey)));
            if (authApp != null) {
                if (AppPlatform.IOS.equals(AppPlatform.getByCode(Integer.valueOf(platform)))) {
                    appConfig.setAppSecret(authApp.getAppSecret().getIos());
                } else if (AppPlatform.ANDROID.equals(AppPlatform.getByCode(Integer.valueOf(platform)))) {
                    appConfig.setAppSecret(authApp.getAppSecret().getAndroid());
                }
            }

            AppConfigInfo info = new AppConfigInfo();
            info.setShake_open("false");
            info.setDefad_url("");
            info.setGift_open("false");
            appConfig.setInfo(info);
            JoymeAppConfigServiceSngl.get().createAppConfig(appConfig);

            JSONObject appinfo = new JSONObject();
            appinfo.put("shake_open", "false");//摇一摇
            appinfo.put("defad_url", "");//审核期间的广告
            appinfo.put("gift_open", "false");//礼包中心开关
            appinfo.put("reddot_interval", info.getReddot_interval());//单位:s
            appinfo.put("shake_version", info.getShake_version());

            try {
                BucketInfo bucketInfo = BucketInfo.getByCode(appConfig.getBucket());
//                String upToken = picManager.getToken();
                String upToken = FileHandlerFactory.getToken(bucketInfo);
                appinfo.put("uptk", upToken);
                appinfo.put("upbk", bucketInfo.getBucket());
                appinfo.put("upcode", bucketInfo.getThirdKey());
                appinfo.put("uphost", bucketInfo.getHost());

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return appinfo;
        }
    }

    //返回版本信息，每个appkey都会有
    private JSONObject getVersion(String appkey, int platform, String channel, String versionStr) throws ServiceException {
        JSONObject jsonObject = null;
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
                                    jsonObject = new JSONObject();
                                    jsonObject.put("version", version.getVersion());
                                    jsonObject.put("app_key", version.getApp_key());
                                    jsonObject.put("version_url", version.getVersion_url());
                                    jsonObject.put("update_type", version.getUpdate_type());
                                    jsonObject.put("version_info", version.getVersion_info());
                                }
                            }
                        }
                    }
                }
            }
        }
        return jsonObject;
    }

}
