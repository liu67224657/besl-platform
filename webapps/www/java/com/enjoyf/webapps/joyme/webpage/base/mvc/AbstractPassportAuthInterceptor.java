package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.service.usercenter.Token;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.util.CookieUtil;
import com.enjoyf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/3
 * Description:
 */
public abstract class AbstractPassportAuthInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(AbstractPassportAuthInterceptor.class);

    /**
     * 进入方法之前验证
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("AbstractPassportAuthInterceptor.");
        }

        ResultWap authResult = auth(request, response);

        return handle(request, response, authResult);
    }

    /**
     * 逻辑，true成功 false失败
     *
     * @param request
     * @param response
     * @return
     */
    private ResultWap auth(HttpServletRequest request, HttpServletResponse response) {

        String callback = request.getParameter("callback");
        String appKey = HTTPUtil.getParam(request, "appkey");
        if (StringUtil.isEmpty(appKey)) {
            appKey = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_APPKEY);
        }
        String tokenString = HTTPUtil.getParam(request, "token");
        if (StringUtil.isEmpty(tokenString)) {
            tokenString = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_TOKEN);
        }
        String uno = HTTPUtil.getParam(request, "uno");
        if (StringUtil.isEmpty(uno)) {
            uno = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_UNO);
        }
        long uid = -1l;
        String uidStr = HTTPUtil.getParam(request, "uid");
        if (StringUtil.isEmpty(uidStr)) {
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
        if (StringUtil.isEmpty(loginDomainStr)) {
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
                return new ResultWap(-1, appKey, ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED, callback, loginDomain);
            }
//todo 微服务改造屏蔽
//            Token token = UserCenterServiceSngl.get().getToken(tokenString);
//            //token不存在
//            if (token == null) {
//                com.enjoyf.platform.webapps.common.util.CookieUtil.deleteALLCookies(request,response);
//                return new ResultWap(-1, appKey, ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED, callback, loginDomain);
//            }
//
//            //token对应的profilekey和app的profilekey不一致
//            if (!token.getProfileKey().equals(app.getProfileKey())) {
//                com.enjoyf.platform.webapps.common.util.CookieUtil.deleteALLCookies(request,response);
//                return new ResultWap(-1, appKey, ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED, callback, loginDomain);
//            }
//
//            //uno和token对应的uno不一致
//            if (!StringUtil.isEmpty(uno) && !uno.equals(token.getUno())) {
//                com.enjoyf.platform.webapps.common.util.CookieUtil.deleteALLCookies(request,response);
//                return new ResultWap(-1, appKey, ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED, callback, loginDomain);
//            }
//
//            //uid 和 token对应的uid不一致
//            if (uid > 0l && uid != token.getUid()) {
//                com.enjoyf.platform.webapps.common.util.CookieUtil.deleteALLCookies(request,response);
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

    protected abstract boolean handle(HttpServletRequest request, HttpServletResponse response, ResultWap resultCodeConstants);

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }

    protected class ResultWap {
        private long uid;
        private String appkey;
        private ResultCodeConstants result;
        private String callback;
        private LoginDomain loginDomain;

        public ResultWap(long uid, String appkey, ResultCodeConstants result, String callback, LoginDomain loginDomain) {
            this.uid = uid;
            this.appkey = appkey;
            this.result = result;
            this.callback = callback;
            this.loginDomain = loginDomain;
        }

        public long getUid() {
            return uid;
        }

        public String getAppkey() {
            return appkey;
        }

        public ResultCodeConstants getResult() {
            return result;
        }

        public String getCallback() {
            return callback;
        }

        public LoginDomain getLoginDomain() {
            return loginDomain;
        }
    }
}
