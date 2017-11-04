package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.oauth.AuthToken;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.webapps.common.oauth.OAuthException;
import com.enjoyf.platform.webapps.common.oauth.OAuthTokenGenerator;
import com.enjoyf.platform.webapps.common.security.DES;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 11-9-16
 * Time: 下午8:14
 * To change this template use File | Settings | File Templates.
 */
public class LoginUtil {
    private static final String COOKIE_LOGIN_KEY = "k";
    private static final String COOKIE_LOGIN_STATUS = "~en~joy~success";// cookie status
    private static final String COOKIE_LOGIN_SPLIT = "~en~joy~";
    private final static int COOKIE_MAX_AGE = 60 * 60 * 24 * 14;// 2周

    private static Logger logger = LoggerFactory.getLogger(LoginUtil.class);


    public static boolean logindScrKeyExists(HttpServletRequest request) {
        String key = CookieUtil.getCookieValue(request, COOKIE_LOGIN_KEY);
        return !StringUtil.isEmpty(key);
    }

    public static String getLoginScrKey(HttpServletRequest request) {
        return CookieUtil.getCookieValue(request, COOKIE_LOGIN_KEY);
    }

    public static void setLoginScrKey(HttpServletRequest request, HttpServletResponse response, String userid, String pwd) throws Exception {
        String cookie_kv = DES.urlEncrypt(DES.md5(userid + pwd) + userid + COOKIE_LOGIN_STATUS);
        // 设定时间为两周
        int maxAge = COOKIE_MAX_AGE;// 2周
        CookieUtil.setCookie(request, response, COOKIE_LOGIN_KEY, cookie_kv, maxAge);
    }

    public static LoginInfo getLoginInfoByScrKey(String loginKey) throws Exception {
        String sKey = DES.urlDecrypt(loginKey);
        String userid = sKey.substring(32, sKey.indexOf(COOKIE_LOGIN_SPLIT));

        boolean isSuccess = sKey.endsWith(COOKIE_LOGIN_STATUS);

        return new LoginInfo(userid, isSuccess);
    }


    public static boolean writeAuthToken(HttpServletRequest request, HttpServletResponse response, UserSession userSession) {

        boolean bValue = false;
        if (request == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("get request is null at writeAuthToken.");
            }
            return bValue;
        }

        try {
            AuthToken authToken = OAuthTokenGenerator.generateAccessToken(EnvConfig.get().getOauthAppId(), userSession.getBlogwebsite().getUno());

            userSession.setAuthToken(authToken);
            CookieUtil.setCookie(request, response, CookieUtil.ACCESS_TOKEN, authToken.getToken(), -1);
            bValue = true;

        } catch (OAuthException oe) {
            GAlerter.lab(LoginUtil.class.getName() + " writeAuthToken occured OAuthException:", oe);
        }

        return bValue;
    }


    /**
     * 将用户保存到session中
     *
     * @param request
     * @param userSession
     */
    private static void saveUserInSession(HttpServletRequest request, UserSession userSession) {
        HttpSession session = request.getSession();

        if (null != session.getAttribute(Constant.SESSION_USER_INFO)) {
            request.getSession().removeAttribute(Constant.SESSION_USER_INFO);
        }

        session.setAttribute(Constant.SESSION_USER_INFO, userSession);
    }


    public static void main(String[] args) {
        try {
            String cookie_kv = DES.urlEncrypt(DES.md5("liu67224657@qq.com" + "111111") + "liu67224657@qq.com" + COOKIE_LOGIN_STATUS);
            System.out.println(cookie_kv);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
