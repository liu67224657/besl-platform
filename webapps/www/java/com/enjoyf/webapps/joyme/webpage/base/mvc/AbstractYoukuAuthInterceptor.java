package com.enjoyf.webapps.joyme.webpage.base.mvc;


import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.usercenter.AuthProfile;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.UserCenterUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.platform.webapps.common.util.YoukuCookie;
import com.enjoyf.platform.webapps.common.util.YoukuCookieUtil;
import com.enjoyf.util.StringUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/6/17
 * Description:
 */
public abstract class AbstractYoukuAuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String yktk = CookieUtil.getCookieValue(request, UserCenterCookieUtil.YOUKU_COOKI_KEY_YKTK);
        if (StringUtil.isEmpty(yktk)) {
            yktk = request.getParameter(UserCenterCookieUtil.YOUKU_COOKI_KEY_YKTK);
        }

        if (StringUtil.isEmpty(yktk)) {
            //falied
            handlerError(request, response);
            return true;
        }

        YoukuCookie youkuCookie = YoukuCookieUtil.praseYoukuCookieYKTK(yktk);
        if (youkuCookie == null) {
            //falied
            handlerError(request, response);
            return true;
        }

        String tokenString = UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_TOKEN);
        String uno = UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_UNO);
        String uidStr = UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_UID);
        String profileId = UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_PROFILEID);
        if (!StringUtil.isEmpty(tokenString) && !StringUtil.isEmpty(uno) && !StringUtil.isEmpty(uidStr) && !StringUtil.isEmpty(profileId)) {
            //已经登录
            handlerSuccess(request, response);
            return true;
        }

        AuthApp app = OAuthServiceSngl.get().getApp(YoukuCookieUtil.YOUKU_APPKEY);
        if (app == null) {
            //todo loger
            handlerError(request, response);
            return true;
        }

        //call usercenter api
        AuthProfile authProfile = UserCenterServiceSngl.get().auth(youkuCookie.getYtid(), LoginDomain.YOUKU, null, "", youkuCookie.getNick() + youkuCookie.getYtid(), app.getProfileKey(), "127.0.0.1", new Date(), new HashMap<String, String>());
        UserCenterCookieUtil.writeYoukuAuthCookie(request, response, authProfile, app.getAppId(), LoginDomain.YOUKU, youkuCookie.getNick(), yktk);
        handlerSuccess(request, response);
        return true;
    }


    protected void handlerError(HttpServletRequest request, HttpServletResponse response) {
        UserCenterCookieUtil.saveYoukuCookieValue(request, response, UserCenterCookieUtil.COOKIEKEY_YOUKU_LOGINFLAG, String.valueOf(UserCenterCookieUtil.COOKIEVALUE_YOUKU_LOGINFLAG_NOLOGIN), YoukuCookieUtil.COOKIEKEY_YKDOMAIN, -1);
    }

    protected void handlerSuccess(HttpServletRequest request, HttpServletResponse response) {
        UserCenterCookieUtil.saveYoukuCookieValue(request, response, UserCenterCookieUtil.COOKIEKEY_YOUKU_LOGINFLAG, String.valueOf(UserCenterCookieUtil.COOKIEVALUE_YOUKU_LOGINFLAG_LOGIN), YoukuCookieUtil.COOKIEKEY_YKDOMAIN, -1);
    }
}
