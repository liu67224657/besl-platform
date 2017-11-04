package com.enjoyf.webapps.joyme.webpage.controller.auth;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.service.joymeapp.AppEnterpriserType;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.config.AppConfig;
import com.enjoyf.platform.service.joymeapp.config.AppConfigInfo;
import com.enjoyf.platform.service.joymeapp.config.AppConfigUtil;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigServiceSngl;
import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.platform.webapps.common.util.YoukuCookie;
import com.enjoyf.platform.webapps.common.util.YoukuCookieUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterCookieUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by zhimingli on 2015/9/7.
 */

@Controller
@RequestMapping("/youku/api/auth")
public class YoukuAuthApiController extends AbstractAuthController {

    private Logger logger = LoggerFactory.getLogger(YoukuAuthApiController.class);

    @ResponseBody
    @RequestMapping
    public String auth(HttpServletRequest request, HttpServletResponse response) {
        String yktk = CookieUtil.getCookieValue(request, UserCenterCookieUtil.YOUKU_COOKI_KEY_YKTK);
        if (StringUtil.isEmpty(yktk)) {
            yktk = request.getParameter(UserCenterCookieUtil.YOUKU_COOKI_KEY_YKTK);
        }

        //yktk不存在
        if (StringUtil.isEmpty(yktk)) {
            handlerError(request, response);
            return ResultCodeConstants.PARAM_EMPTY.getJsonString();
        }


        try {
            String appKey = HTTPUtil.getParam(request, "appkey");
            String sign = HTTPUtil.getParam(request, "sign");
            String time = HTTPUtil.getParam(request, "time");
            if (StringUtil.isEmpty(appKey) || StringUtil.isEmpty(sign) || StringUtil.isEmpty(time)) {
                handlerError(request, response);
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }

            //优酷未设置cookie
            YoukuCookie youkuCookie = YoukuCookieUtil.praseYoukuCookieYKTK(yktk);
            if (youkuCookie == null) {
                handlerError(request, response);
                return ResultCodeConstants.PARAM_EMPTY.getJsonString();
            }


            String platform = HTTPUtil.getParam(request, "platform");
            String channelid = HTTPUtil.getParam(request, "channelid");
            String version = HTTPUtil.getParam(request, "version");
            String tokenString = UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_TOKEN);
            String uno = UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_UNO);
            String uidStr = UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_UID);
            String profileId = UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_PROFILEID);
            String ip = getIp(request);

            AuthApp app = OAuthServiceSngl.get().getApp(appKey);
            if (app == null) {
                handlerError(request, response);
                return ResultCodeConstants.APP_NOT_EXISTS.getJsonString();
            }


            HashMap<String, String> paramMap = HTTPUtil.getRequestToMap(request);

            //验证加密是否正确.
            //ipad 转换成iphone
            platform = platform.equals("2") ? "0" : platform;

            AppConfig appConfig = JoymeAppConfigServiceSngl.get().getAppConfig(AppConfigUtil.getAppConfigId(appKey, platform, version,
                    channelid, String.valueOf(AppEnterpriserType.getEnterpriser(appKey))));
            if (appConfig == null) {
                createAppconfig(appKey, app, platform, version, channelid);
            }
            String secret = null;
            //先从appconfig里面查找是否存在
            if (appConfig == null || StringUtil.isEmpty(appConfig.getAppSecret())) {
                Integer platfromInt = Integer.valueOf(platform);
                if (AppPlatform.IOS.equals(AppPlatform.getByCode(platfromInt))) {
                    secret = app.getAppSecret().getIos();
                } else if (AppPlatform.ANDROID.equals(AppPlatform.getByCode(platfromInt))) {
                    secret = app.getAppSecret().getAndroid();
                }
            } else {
                secret = appConfig.getAppSecret();
            }
            String signString = HTTPUtil.getSignature(paramMap, secret);
            if (!signString.equalsIgnoreCase(sign)) {
                handlerError(request, response);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(ResultCodeConstants.USERCENTER_AUTH_SIGNERROR.getCode()));
                jsonObject.put("msg", ResultCodeConstants.USERCENTER_AUTH_SIGNERROR.getMsg() + " yknew");
                return jsonObject.toString();
            }


            //已经登录
            if (!StringUtil.isEmpty(tokenString) && !StringUtil.isEmpty(uno) && StringUtil.isEmpty(uidStr) && !StringUtil.isEmpty(profileId)) {
                handlerSuccess(request, response);
                //todo 微服务改造
                //AuthProfile authProfile = UserCenterServiceSngl.get().getAuthProfileByUid(Long.valueOf(uidStr), paramMap);

                UserCenterServiceSngl.get().setOauthToken(tokenString);
                AuthProfile authProfile = UserCenterServiceSngl.get().getCurrentAccount();
                //TODO

                if (authProfile != null) {
                    return getResultByAuthProfile(authProfile).toString();
                }
            }


            //call usercenter api
            AuthProfile authProfile = UserCenterServiceSngl.get().auth(youkuCookie.getYtid(), LoginDomain.YOUKU, null, "", youkuCookie.getNick() + youkuCookie.getYtid(), app.getProfileKey(), ip, new Date(), paramMap);
            UserCenterCookieUtil.writeYoukuAuthCookie(request, response, authProfile, app.getAppId(), LoginDomain.YOUKU, youkuCookie.getNick(), yktk);
            handlerSuccess(request, response);
            return getResultByAuthProfile(authProfile).toString();
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            if (e.equals(UserCenterServiceException.USERLOGIN_NOT_EXISTS)) {
                return ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString();
            } else if (e.equals(UserCenterServiceException.PROFILE_NOT_EXISTS)) {
                return ResultCodeConstants.USERCENTER_USERLOGIN_NOT_EXISTS.getJsonString();
            } else if (e.equals(UserCenterServiceException.USERLOGIN_WRONG_LOGINDOMAIN)) {
                return ResultCodeConstants.USERCENTER_WRONG_DOMAIN.getJsonString();
            } else {
                return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
            return ResultCodeConstants.SYSTEM_ERROR.getJsonString();
        }


    }

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("appkey", "08pkvrWvx5ArJNvhYf19kN");
        map.put("channelid", "appstore");
        map.put("clientid", "7A6ADF79-DCE4-43BB-93F0-DA1B8D471C94");
        map.put("entry", "0");
        map.put("idfa", "bbbb");
        map.put("logindomain", "client");
        map.put("mock", "0");
        map.put("otherid", "7A6ADF79-DCE4-43BB-93F0-DA1B8D471C94");
        map.put("platform", "0");
        //8b61f49ea2d35f135d44e33ac61f854f
        //8B61F49EA2D35F135D44E33AC61F854F
        // map.put("sign","8B61F49EA2D35F135D44E33AC61F854F");
        map.put("source", "1");
        map.put("time", "1443088963771");
        map.put("version", "1");
        map.put("yktk", "1%7C1434426667%7C15%7CaWQ6Mzc5NDQ3NTQ1LG5uOmFudG9ueTUxNix2aXA6ZmFsc2UseXRpZDozNzk0NDc1NDUsdGlkOjA%3D%7C6511532db1b6a5d7e3768cb49afcff76%7Cc079d6717134a9480ae8934ab56d576efbf36320%7C1");
        String signString = HTTPUtil.getSignature(map, "6e61af2f1f3f7069d80061af01070a15");
        System.out.println(signString);

        System.out.println(MD5Util.Md5("appkey08pkvrWvx5ArJNvhYf19kNchannelidappstoreclientidC0AFA579-7973-4B03-A885-FDCB1CF4777Dentry0idfaaaaalogindomainclientmock1otheridC0AFA579-7973-4B03-A885-FDCB1CF4777Dplatform0source1time1443090846929version1yktk1|1434426667|15|aWQ6Mzc5NDQ3NTQ1LG5uOmFudG9ueTUxNix2aXA6ZmFsc2UseXRpZDozNzk0NDc1NDUsdGlkOjA=|6511532db1b6a5d7e3768cb49afcff76|c079d6717134a9480ae8934ab56d576efbf36320|16e61af2f1f3f7069d80061af01070a15"));

    }


    protected void handlerError(HttpServletRequest request, HttpServletResponse response) {
        UserCenterCookieUtil.saveYoukuCookieValue(request, response, UserCenterCookieUtil.COOKIEKEY_YOUKU_LOGINFLAG, String.valueOf(UserCenterCookieUtil.COOKIEVALUE_YOUKU_LOGINFLAG_NOLOGIN), YoukuCookieUtil.COOKIEKEY_YKDOMAIN, -1);
        UserCenterCookieUtil.setYoukuLoginFalgCookie(request, response, "0");
    }

    protected void handlerSuccess(HttpServletRequest request, HttpServletResponse response) {
        UserCenterCookieUtil.saveYoukuCookieValue(request, response, UserCenterCookieUtil.COOKIEKEY_YOUKU_LOGINFLAG, String.valueOf(UserCenterCookieUtil.COOKIEVALUE_YOUKU_LOGINFLAG_LOGIN), YoukuCookieUtil.COOKIEKEY_YKDOMAIN, -1);
        UserCenterCookieUtil.setYoukuLoginFalgCookie(request, response, "1");
    }

    protected AppConfig createAppconfig(String appkey, AuthApp authApp, String platform, String versionStr, String channel) {
        AppConfig appConfig = new AppConfig();
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
        try {
            appConfig = JoymeAppConfigServiceSngl.get().createAppConfig(appConfig);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return appConfig;
    }
}
