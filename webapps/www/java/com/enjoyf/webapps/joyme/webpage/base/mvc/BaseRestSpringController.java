package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ThirdApiHotdeployConfig;
import com.enjoyf.platform.service.profile.ProfileSum;
import com.enjoyf.platform.service.usercenter.AuthProfile;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.thirdapi.ThirdApiProps;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.webapps.joyme.webpage.util.Constant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * BaseAction定义了一些web层基础方法
 */
public class BaseRestSpringController {
    protected static final String DEFAULT_REURL = "http://www.joyme.com";
    protected static final String DEFAULT_APPKEY = "default";

    protected void writeAuthCookie(HttpServletRequest request, HttpServletResponse response, AuthProfile profile, String appkey, LoginDomain loginDomain) {
        UserCenterCookieUtil.writeAuthCookie(request, response, profile, appkey, loginDomain);
    }

    protected void writeAuthCookieByMaxAge(HttpServletRequest request, HttpServletResponse response, AuthProfile profile, String appkey, LoginDomain loginDomain, int maxAge) {
        UserCenterCookieUtil.writeAuthCookie(request, response, profile, appkey, loginDomain, maxAge);
    }

    protected String getClietnIdByCookie(HttpServletRequest request, HttpServletResponse response) {
        return UserCenterCookieUtil.getClietnIdByCookie(request, response);
    }

    /**
     * 将用户保存到session中
     *
     * @param request
     * @param userSession
     */
    @Deprecated
    protected void saveUserInSession(HttpServletRequest request, UserSession userSession) {
        HttpSession session = request.getSession();
        if (null != session.getAttribute(Constant.SESSION_USER_INFO)) {
            this.removeUserFromSession(request);
        }

        session.setAttribute(Constant.SESSION_USER_INFO, userSession);
    }

    /**
     * 将用户从session中取出
     *
     * @param request
     * @return
     */
    @Deprecated
    protected UserSession getUserBySession(HttpServletRequest request) {
        Object obj = request.getSession().getAttribute(Constant.SESSION_USER_INFO);
        if (obj != null) {
            return (UserSession) obj;
        }
        return null;
    }

    protected UserCenterSession buildUserCenterSession(AuthProfile profile, HttpServletRequest request) {
        return UserCenterCookieUtil.buildUserCenterSession(profile, request);
    }

    protected UserCenterSession getUserCenterSeesion(HttpServletRequest request) {
        return UserCenterCookieUtil.getUserCenterSeesion(request);
    }

    protected UserCenterSession getUserCenterSessionUGCWiki(HttpServletRequest request) {
        return UserCenterCookieUtil.getUserCenterSessionUGCWiki(request);
    }

    protected void removeUserFromSession(HttpServletRequest request) {
        UserCenterCookieUtil.removeUserFromSession(request);
    }

    protected boolean userIsLogin(HttpServletRequest request) {
        UserSession userSession = getUserBySession(request);
        return userSession == null ? false : true;
    }

    protected void resaveCountdataInSession(HttpServletRequest request, ProfileSum profileSum) {
        UserSession userSession = getUserBySession(request);
        userSession.setCountdata(profileSum);
        saveUserInSession(request, userSession);
    }

    protected Set<LoginDomain> getBindLoginDomain(UserCenterSession userSession) {
        return userSession.getFlag().getLoginDomain();
    }

    /**
     * 得到ip，（通过Nginx的ip需要从请求头部取）
     *
     * @param request
     * @return
     */
    protected String getIp(HttpServletRequest request) {
        return HTTPUtil.getRemoteAddr(request);
    }

    protected Map<String, Object> putErrorMessage(String errorMessage) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        mapMessage.put("message", errorMessage);
        return mapMessage;
    }

    //todo
    protected Map<String, LoginDomain> getSupportBindDomain() {
        //support syncprovider
        Map<String, LoginDomain> syncProviderMap = new LinkedHashMap<String, LoginDomain>();
        for (Map.Entry<String, LoginDomain> accountDomainEntry : HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class).getProviderMap().entrySet()) {

            //props 为空或者 在登录页面显示的属性为true
            ThirdApiProps thirdApiProps = HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class).getThirdApiPropByAccountDomain(accountDomainEntry.getValue());
            if (thirdApiProps == null || thirdApiProps.isBindDisplay()) {
                syncProviderMap.put(accountDomainEntry.getKey(), accountDomainEntry.getValue());
            }
        }

        return syncProviderMap;
    }

    protected Map<String, String> getJParam(String JParam) {
        return HTTPUtil.getJParam(JParam);
    }

}
