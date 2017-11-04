package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.*;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
import com.enjoyf.platform.webapps.common.ResultCodeConstants;
import com.enjoyf.util.CookieUtil;
import com.enjoyf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/3
 * Description:
 */
public abstract class AbstractUGCWikiAuthInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(AbstractUGCWikiAuthInterceptor.class);

    private static final String UGC_WIKI_PROFILE_KEY = "ugcwiki";

    /**
     * 进入方法之前验证
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("AbstractPassportAuthInterceptor.");
        }

        ResultWap resultWap = auth(request, response);

        return handle(request, response, resultWap);
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

        String appKey = CookieUtil.getCookieValue(request, UserCenterCookieUtil.UGCWIKI_COOKIEKEY_APPKEY);
        String tokenString = CookieUtil.getCookieValue(request, UserCenterCookieUtil.UGCWIKI_COOKIEKEY_TOKEN);
        String uno = CookieUtil.getCookieValue(request, UserCenterCookieUtil.UGCWIKI_COOKIEKEY_UNO);
        long uid = -1l;
        String uidStr = CookieUtil.getCookieValue(request, UserCenterCookieUtil.UGCWIKI_COOKIEKEY_UID);
        if (!StringUtil.isEmpty(uidStr)) {
            try {
                uid = Long.parseLong(uidStr);
            } catch (NumberFormatException e) {
            }
        }
        LoginDomain loginDomain = null;
        String loginDomainStr = CookieUtil.getCookieValue(request, UserCenterCookieUtil.UGCWIKI_COOKIEKEY_LOGINDOMAIN);
        if (!StringUtil.isEmpty(loginDomainStr)) {
            loginDomain = LoginDomain.getByCode(loginDomainStr);
        }

        //ugc wiki没有登录
        if (StringUtil.isEmpty(tokenString) || StringUtil.isEmpty(appKey)
                || (StringUtil.isEmpty(uno) && uid < 0l) || loginDomain == null || loginDomain.equals(LoginDomain.CLIENT.getCode())) {
            String appKeyWww = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_APPKEY);
            String tokenStringWww = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_TOKEN);
            String unoWww = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_UNO);
            long uidWww = -1l;
            String uidStrWww = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_UID);
            if (!StringUtil.isEmpty(uidStrWww)) {
                try {
                    uidWww = Long.parseLong(uidStrWww);
                } catch (NumberFormatException e) {
                }
            }
            LoginDomain loginDomainWww = null;
            String loginDomainStrWww = CookieUtil.getCookieValue(request, UserCenterCookieUtil.COOKIEKEY_LOGINDOMAIN);
            if (!StringUtil.isEmpty(loginDomainStrWww)) {
                loginDomainWww = LoginDomain.getByCode(loginDomainStrWww);
            }

            //www已登录
            if(!StringUtil.isEmpty(tokenStringWww) && !StringUtil.isEmpty(appKeyWww)
                    && !StringUtil.isEmpty(unoWww) && uidWww > 0l && loginDomainWww != null && !loginDomainWww.equals(LoginDomain.CLIENT.getCode())){
                HashMap<String, String> map = HTTPUtil.getRequestToMap(request);
                map.put(UserCenterUtil.TOKEN_STRING, tokenStringWww);
                map.put(UserCenterUtil.UNO_STRING, unoWww);
                try {
                    //是否存在ugcwiki profile
                    AuthProfile authProfile = UserCenterServiceSngl.get().getAuthProfileByUno(unoWww, UGC_WIKI_PROFILE_KEY, null);
                    //不存在，在该account下生成 新profile
                    if(authProfile == null){
                        AuthProfile authProfileWww = UserCenterServiceSngl.get().getAuthProfileByUid(uidWww, map);
                        if(authProfileWww != null){
                            //需要共用一个uno
                            String clientid = Md5Utils.md5(UUID.randomUUID().toString());
                            authProfile = UserCenterServiceSngl.get().auth(clientid, loginDomainWww, null, "", authProfileWww.getProfile().getNick(), UGC_WIKI_PROFILE_KEY, HTTPUtil.getRemoteAddr(request), new Date(), map);
                        }
                    }
                    if(authProfile != null && authProfile.getProfile() != null && authProfile.getToken() != null && authProfile.getUserAccount() != null && authProfile.getUserLogin() != null){
                        UserCenterCookieUtil.writeUGCWikiAuthCookie(request, response, authProfile, UGC_WIKI_PROFILE_KEY, authProfile.getUserLogin().getLoginDomain());
                    }
                    return new ResultWap(uid, appKey, ResultCodeConstants.SUCCESS, callback, loginDomain);
                } catch (ServiceException e) {
                    GAlerter.lab(this.getClass().getName() + " occur ServiceException.e: ", e);
                }
            }else {
                return new ResultWap(uid, appKey, ResultCodeConstants.SUCCESS, callback, loginDomain);
            }
        }

        try {
            AuthApp app = OAuthServiceSngl.get().getApp(appKey);
            if (app == null || StringUtil.isEmpty(app.getProfileKey())) {
                return new ResultWap(-1, appKey, ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED, callback, loginDomain);
            }

            Token token = UserCenterServiceSngl.get().getToken(tokenString);
            //token不存在
            if (token == null) {
                return new ResultWap(-1, appKey, ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED, callback, loginDomain);
            }


            //token对应的profilekey和app的profilekey不一致
            if (!token.getProfileKey().equals(app.getProfileKey())) {
                return new ResultWap(-1, appKey, ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED, callback, loginDomain);
            }

            //uno和token对应的uno不一致
            if (!StringUtil.isEmpty(uno) && !uno.equals(token.getUno())) {
                return new ResultWap(-1, appKey, ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED, callback, loginDomain);
            }

            //uid 和 token对应的uid不一致
            if (uid > 0l && uid != token.getUid()) {
                return new ResultWap(-1, appKey, ResultCodeConstants.USERCENTER_TOKEN_VALIDATE_FAILED, callback, loginDomain);
            }

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
