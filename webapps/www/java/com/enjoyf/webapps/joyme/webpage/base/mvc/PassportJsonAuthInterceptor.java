package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.util.CookieUtil;
import com.enjoyf.util.StringUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/3
 * Description:
 */
public class PassportJsonAuthInterceptor extends AbstractPassportAuthInterceptor {

    //海贼迷的appkey
    private static String HAI_ZEI = "0G30ZtEkZ4vFBhAfN7Bx4v";

    private Logger logger = LoggerFactory.getLogger(PassportJsonAuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("AbstractPassportAuthInterceptor.");
        }

        ResultWap authResult = auth(request, response);

        return handle(request, response, authResult);
    }

    private ResultWap auth(HttpServletRequest request, HttpServletResponse response) {

        String callback = request.getParameter("callback");
        String appKey = HTTPUtil.getParam(request, "appkey");


        String appKeySplit = AppUtil.getAppKey(appKey);

        //如果是海贼迷的取cookie
        if (StringUtil.isEmpty(appKey) || HAI_ZEI.equals(appKeySplit)) {
            appKey = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_APPKEY);
        }


        String tokenString = HTTPUtil.getParam(request, "token");
        //如果是海贼迷的取cookie
        if (StringUtil.isEmpty(tokenString) || HAI_ZEI.equals(appKeySplit)) {
            tokenString = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_TOKEN);
        }

        String uno = HTTPUtil.getParam(request, "uno");
        //如果是海贼迷的取cookie
        if (StringUtil.isEmpty(uno) || HAI_ZEI.equals(appKeySplit)) {
            uno = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_UNO);
        }
        long uid = -1l;
        String uidStr = HTTPUtil.getParam(request, "uid");
        //如果是海贼迷的取cookie
        if (StringUtil.isEmpty(uidStr) || HAI_ZEI.equals(appKeySplit)) {
            uidStr = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_UID);
        }

        if (!StringUtil.isEmpty(uidStr)) {
            try {
                uid = Long.parseLong(uidStr);
            } catch (NumberFormatException e) {
            }
        }

        LoginDomain loginDomain = null;
        String loginDomainStr = HTTPUtil.getParam(request, "logindomain");
        //如果是海贼迷的取cookie
        if (StringUtil.isEmpty(loginDomainStr) || HAI_ZEI.equals(appKeySplit)) {
            loginDomainStr = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_LOGINDOMAIN);
        }

        if (!StringUtil.isEmpty(loginDomainStr)) {
            loginDomain = LoginDomain.getByCode(loginDomainStr);
        }

        if (StringUtil.isEmpty(tokenString) || StringUtil.isEmpty(appKey)
                || (StringUtil.isEmpty(uno) && uid < 0l) || loginDomain == null) {
            //
            return new ResultWap(-1, appKey, ResultCodeConstants.PARAM_EMPTY, callback, loginDomain);
        }

        try {

            AuthApp app = OAuthServiceSngl.get().getApp(appKey);
            if (app == null || StringUtil.isEmpty(app.getProfileKey())) {
                GAlerter.lan("==== PassportJsonAuthInterceptorcheck failed app is null:=== appkey:" + appKey);
                return new ResultWap(-1, appKey, ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED, callback, loginDomain);
            }

//            Token token = UserCenterServiceSngl.get().getToken(tokenString);
//            //token不存在
//            if (token == null) {
//                GAlerter.lan("==== PassportJsonAuthInterceptorcheck failed token is null:=== token:" + tokenString);
//                return new ResultWap(-1, appKey, ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED, callback, loginDomain);
//            }
//
//            //token对应的profilekey和app的profilekey不一致
//            if (!token.getProfileKey().equals(app.getProfileKey())) {
//                GAlerter.lan("==== PassportJsonAuthInterceptorcheck failed profilekey is error:=== tokenprofilekey:" + token.getProfileKey() + " appprofilekey" + app.getProfileKey());
//                return new ResultWap(-1, appKey, ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED, callback, loginDomain);
//            }
//
//            //uno和token对应的uno不一致
//            if (!StringUtil.isEmpty(uno) && !uno.equals(token.getUno())) {
//                GAlerter.lan("==== PassportJsonAuthInterceptorcheck failed uno is error:=== uno:" + uno + " tokenuno:" + token.getUno());
//                return new ResultWap(-1, appKey, ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED, callback, loginDomain);
//            }
//
//            //uid 和 token对应的uid不一致
//            if (uid > 0l && uid != token.getUid()) {
//                GAlerter.lan("==== PassportJsonAuthInterceptorcheck failed uno is error:=== uid:" + uid + " tokenuid:" +token.getUid());
//                return new ResultWap(-1, appKey, ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED, callback, loginDomain);
//            }

        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ResultWap(-1, appKey, ResultCodeConstants.SYSTEM_ERROR, callback, loginDomain);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
            return new ResultWap(-1, appKey, ResultCodeConstants.SYSTEM_ERROR, callback, loginDomain);
        }

        return new ResultWap(uid, appKey, ResultCodeConstants.SUCCESS, callback, loginDomain);
    }

    protected boolean handle(HttpServletRequest request, HttpServletResponse response, ResultWap result) {
        if (result.getResult().getCode() == ResultCodeConstants.SUCCESS.getCode()) {
            String uidString = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_UID);
            if (StringUtil.isEmpty(uidString)) {
                try {

//                    HashMap<String, String> map = HTTPUtil.getRequestToMap(request);
//                    map.put(UserCenterUtil.TOKEN_STRING, CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_TOKEN));
//
//                    AuthProfile authProfile = UserCenterServiceSngl.get().getAuthProfileByUid(result.getUid(), map);
                    //todo 微服务改造
                    UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
                    AuthProfile authProfile = UserCenterServiceSngl.get().getCurrentAccount();
                    //TODO

                    if (authProfile != null) {
                        UserCenterCookieUtil.writeAuthCookie(request, response, authProfile, result.getAppkey(), result.getLoginDomain());
                    }
                } catch (Exception e) {
                    GAlerter.lab(this.getClass().getName() + " occured ServiceException.e: ", e);
                }
            }
            return true;
        }

        try {
            String callback = result.getCallback();
            if (callback == null || StringUtil.isEmpty(callback)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(result.getResult().getCode()));
                jsonObject.put("msg", result.getResult().getMsg());
                HTTPUtil.writeJson(response, jsonObject.toString());
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rs", String.valueOf(result.getResult().getCode()));
                jsonObject.put("msg", result.getResult().getMsg());
                String callbackStr = callback + "([" + jsonObject.toString() + "])";
                HTTPUtil.writeJson(response, callbackStr);
            }

        } catch (Exception e) {
            logger.error("write not PassportWebAuthInterceptor.", e);
        }

        return false;
    }


}
