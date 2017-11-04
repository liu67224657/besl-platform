package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.service.oauth.AuthApp;
import com.enjoyf.platform.service.oauth.OAuthServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.AuthProfile;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/2/26
 * Description:
 */
public class DefaultAuthInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(AbstractPassportAuthInterceptor.class);

    private static final String DEFAULT_APPKEY = "default";

    /**
     * 进入方法之前验证
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("AbstractPassportAuthInterceptor.");
        }


        if (UserCenterCookieUtil.valideteUserCenterSessionByCookie(request)) {
            return true;
        }

        LoginDomain loginDomain = LoginDomain.CLIENT;
        String appkey = request.getParameter("appkey") == null ? DEFAULT_APPKEY : request.getParameter("appkey");
        String clientid = UserCenterCookieUtil.getClietnIdByCookie(request, response);
        String ip = HTTPUtil.getRemoteAddr(request);

        Date createDate = new Date();
        if (StringUtil.isEmpty(clientid) || StringUtil.isEmpty(appkey)) {
            return false;
        }

        try {
            AuthApp authApp = OAuthServiceSngl.get().getApp(appkey);
            if (authApp == null) {
                return false;
            }

            String profileKey = authApp.getProfileKey();
            if (StringUtil.isEmpty(profileKey)) {
                return false;
            }

            AuthProfile profile = UserCenterServiceSngl.get().auth(clientid, loginDomain, null, "", "", profileKey, ip, createDate, HTTPUtil.getRequestToMap(request));

            UserCenterCookieUtil.writeAuthCookie(request, response, profile, appkey, loginDomain);
//            UserCenterCookieUtil.buildUserCenterSession(profile, request);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }

        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }

}
