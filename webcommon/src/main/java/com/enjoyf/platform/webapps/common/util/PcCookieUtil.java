/**
 * cookie的增、删、查工具类
 */
package com.enjoyf.platform.webapps.common.util;

import com.enjoyf.platform.props.WebappConfig;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PcCookieUtil {
    public static final String ACCESS_TOKEN = "at";
    public static final String UNO = "pc_joyme_u";


    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie cookies[] = request.getCookies();
        if (cookies == null || name == null || name.length() == 0) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            if (name.equals(cookies[i].getName())) {
                return cookies[i];
            }
        }
        return null;
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookies[] = request.getCookies();
        if (cookies == null || name == null || name.length() == 0) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            if (name.equals(cookies[i].getName())) {
                return cookies[i].getValue();
            }
        }
        return null;
    }

    public static void setCookie(HttpServletRequest request,
                                 HttpServletResponse response, String name, String value) {
        // 默认时间是0x278d00（一个月）
        //2*16^5+7*16^4+8*16^3+13*16^2
        //2097152+458752+32768+3328=2592000
        //24*60*60*30=2592000
        setCookie(request, response, name, value, 0x278d00);
    }

    public static void setCookie(HttpServletRequest request,
                                 HttpServletResponse response, String name, String value, int maxAge) {
//        Cookie cookie = new Cookie(name, value == null ? "" : value);
//        cookie.setMaxAge(maxAge);
//        cookie.setPath(getPath(request));
//        cookie.setDomain("." + WebUtil.getDomain());
//        response.addCookie(cookie);
        setCookie(request, response, name, value, "." + WebUtil.getDomain(), maxAge);
    }

    public static void setCookie(HttpServletRequest request,
                                 HttpServletResponse response, String name, String value, String domain, int maxAge) {
        Cookie cookie = new Cookie(name, value == null ? "" : value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(getPath(request));
        cookie.setDomain(domain);
        response.addCookie(cookie);
    }

    private static String getPath(HttpServletRequest request) {
        String path = request.getContextPath();
        return (path == null || path.length() == 0) ? "/" : path;
    }

    //CookieUtil.setCookie(request, response, CookieUtil.ACCESS_TOKEN, authToken.getToken(), -1);

    /**
     * 删除所有cookie
     */
    public static void deleteALLCookies(HttpServletRequest request,
                                        HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie _cookie = cookies[i];
                _cookie.setMaxAge(0);
                _cookie.setPath(getPath(request));
                _cookie.setValue(null);
                response.addCookie(_cookie);
            }
        }
        PcCookieUtil.setCookie(request, response, WebappConfig.get().getDiscuzAuthCookieKey(), null, 0);
        deleteAuthCookie(request, response);

        response.addHeader("P3P", "CP=\"NON DSP COR CURa ADMa DEVa TAIa PSAa PSDa IVAa IVDa CONa HISa TELa OTPa OUR UNRa IND UNI COM NAV INT DEM CNT PRE LOC\"");
    }

    private static final String COOKIEKEY_UID = "pc_jmuc_u";
    private static final String COOKIEKEY_UNO = "pc_jmuc_uno";
    private static final String COOKIEKEY_TIME = "pc_jmuc_t";
    private static final String COOKIEKEY_SIGN = "pc_jmuc_s";
    private static final String COOKIEKEY_TOKEN = "pc_jmuc_token";
    private static final String COOKIEKEY_APPKEY = "pc_jmuc_appkey";
    private static final String COOKIEKEY_LOGINDOMAIN = "pc_jmuc_lgdomain";
    private static final String COOKIEKEY_PID = "pc_jmuc_pid";

    private static void deleteAuthCookie(HttpServletRequest request, HttpServletResponse response) {
        PcCookieUtil.setCookie(request, response, COOKIEKEY_UID, null, 0);
        PcCookieUtil.setCookie(request, response, COOKIEKEY_UNO, null, 0);
        PcCookieUtil.setCookie(request, response, COOKIEKEY_TIME, null, 0);
        PcCookieUtil.setCookie(request, response, COOKIEKEY_SIGN, null, 0);
        PcCookieUtil.setCookie(request, response, COOKIEKEY_TOKEN, null, 0);
        PcCookieUtil.setCookie(request, response, COOKIEKEY_APPKEY, null, 0);
        PcCookieUtil.setCookie(request, response, COOKIEKEY_LOGINDOMAIN, null, 0);
        PcCookieUtil.setCookie(request, response, COOKIEKEY_PID, null, 0);
    }
}
