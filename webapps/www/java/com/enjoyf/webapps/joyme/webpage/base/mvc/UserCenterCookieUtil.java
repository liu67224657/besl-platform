package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.AuthProfile;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.UserCenterUtil;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;
import com.enjoyf.platform.webapps.common.util.CookieUtil;
import com.enjoyf.platform.webapps.common.util.YoukuCookieUtil;
import com.enjoyf.webapps.joyme.webpage.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/30
 * Description:
 */
public class UserCenterCookieUtil {
    private static Logger logger = LoggerFactory.getLogger(UserCenterCookieUtil.class);

    public static final String COOKIEKEY_CID = "jmuc_cid";
    public static final String COOKIEKEY_UID = "jmuc_u";
    public static final String COOKIEKEY_UNO = "jmuc_uno";
    public static final String COOKIEKEY_TIME = "jmuc_t";
    public static final String COOKIEKEY_SIGN = "jmuc_s";
    public static final String COOKIEKEY_TOKEN = "jmuc_token";
    public static final String COOKIEKEY_APPKEY = "jmuc_appkey";
    public static final String COOKIEKEY_LOGINDOMAIN = "jmuc_lgdomain";
    public static final String COOKIEKEY_NICKNAME = "jmuc_nn";
    public static final String COOKIEKEY_PROFILEID = "jmuc_pid";

    public static final String COOKIE_SECR = "as__-d(*^(";
    public static final int MAX_AGE_AUTH_USER = 30 * 24 * 60 * 60;

    //来自于优酷
    public static final String COOKIEKEY_YOUKU_LOGINFLAG = "yk_lgfg";
    public static final String COOKIEVALUE_YOUKU_LOGINFLAG_LOGIN = "1";
    public static final String COOKIEVALUE_YOUKU_LOGINFLAG_NOLOGIN = "0";
    public static final String YOUKU_COOKI_KEY_YKTK = "yktk";
    //ugc wiki
    public static final String UGCWIKI_COOKIEKEY_CID = "jmucwiki_cid";
    public static final String UGCWIKI_COOKIEKEY_UID = "jmucwiki_u";
    public static final String UGCWIKI_COOKIEKEY_UNO = "jmucwiki_uno";
    public static final String UGCWIKI_COOKIEKEY_TIME = "jmucwiki_t";
    public static final String UGCWIKI_COOKIEKEY_SIGN = "jmucwiki_s";
    public static final String UGCWIKI_COOKIEKEY_TOKEN = "jmucwiki_token";
    public static final String UGCWIKI_COOKIEKEY_APPKEY = "jmucwiki_appkey";
    public static final String UGCWIKI_COOKIEKEY_LOGINDOMAIN = "jmucwiki_lgdomain";
    public static final String UGCWIKI_COOKIEKEY_NICKNAME = "jmucwiki_nn";
    public static final String UGCWIKI_COOKIEKEY_PROFILEID = "jmucwiki_pid";

    public static void writeUGCWikiAuthCookie(HttpServletRequest request, HttpServletResponse response, AuthProfile profile, String appkey, LoginDomain loginDomain) {
        long time = System.currentTimeMillis();
        CookieUtil.setCookie(request, response, UGCWIKI_COOKIEKEY_UID, String.valueOf(profile.getProfile().getUid()), MAX_AGE_AUTH_USER);
        CookieUtil.setCookie(request, response, UGCWIKI_COOKIEKEY_UNO, profile.getProfile().getUno(), MAX_AGE_AUTH_USER);
        CookieUtil.setCookie(request, response, UGCWIKI_COOKIEKEY_TIME, String.valueOf(time), MAX_AGE_AUTH_USER);
        CookieUtil.setCookie(request, response, UGCWIKI_COOKIEKEY_SIGN, Md5Utils.md5(profile.getProfile().getUid() + profile.getProfile().getUno() + time + COOKIE_SECR), MAX_AGE_AUTH_USER);
        CookieUtil.setCookie(request, response, UGCWIKI_COOKIEKEY_TOKEN, profile.getToken().getToken(), MAX_AGE_AUTH_USER);
        CookieUtil.setCookie(request, response, UGCWIKI_COOKIEKEY_APPKEY, appkey, MAX_AGE_AUTH_USER);
        CookieUtil.setCookie(request, response, UGCWIKI_COOKIEKEY_PROFILEID, profile.getProfile().getProfileId(), MAX_AGE_AUTH_USER);
//        if (profile.getUserLogin() != null) {
        CookieUtil.setCookie(request, response, UGCWIKI_COOKIEKEY_LOGINDOMAIN, loginDomain.getCode(), MAX_AGE_AUTH_USER);
//        }
    }

    public static void writeAuthCookie(HttpServletRequest request, HttpServletResponse response, AuthProfile profile, String appkey, LoginDomain loginDomain) {
        long time = System.currentTimeMillis();
        CookieUtil.setCookie(request, response, COOKIEKEY_UID, String.valueOf(profile.getProfile().getUid()), MAX_AGE_AUTH_USER);
        CookieUtil.setCookie(request, response, COOKIEKEY_UNO, profile.getProfile().getUno(), MAX_AGE_AUTH_USER);
        CookieUtil.setCookie(request, response, COOKIEKEY_TIME, String.valueOf(time), MAX_AGE_AUTH_USER);
        CookieUtil.setCookie(request, response, COOKIEKEY_SIGN, Md5Utils.md5(profile.getProfile().getUid() + profile.getProfile().getUno() + time + COOKIE_SECR), MAX_AGE_AUTH_USER);
        CookieUtil.setCookie(request, response, COOKIEKEY_TOKEN, profile.getToken().getToken(), MAX_AGE_AUTH_USER);
        CookieUtil.setCookie(request, response, COOKIEKEY_APPKEY, appkey, MAX_AGE_AUTH_USER);
        CookieUtil.setCookie(request, response, COOKIEKEY_PROFILEID, profile.getProfile().getProfileId(), MAX_AGE_AUTH_USER);
//        if (profile.getUserLogin() != null) {
        CookieUtil.setCookie(request, response, COOKIEKEY_LOGINDOMAIN, loginDomain.getCode(), MAX_AGE_AUTH_USER);
//        }
    }

    public static void writeAuthCookie(HttpServletRequest request, HttpServletResponse response, AuthProfile profile, String appkey, LoginDomain loginDomain, int maxAge) {
        long time = System.currentTimeMillis();
        CookieUtil.setCookie(request, response, COOKIEKEY_UID, String.valueOf(profile.getProfile().getUid()), maxAge);
        CookieUtil.setCookie(request, response, COOKIEKEY_UNO, profile.getProfile().getUno(), maxAge);
        CookieUtil.setCookie(request, response, COOKIEKEY_TIME, String.valueOf(time), maxAge);
        CookieUtil.setCookie(request, response, COOKIEKEY_SIGN, Md5Utils.md5(profile.getProfile().getUid() + profile.getProfile().getUno() + time + COOKIE_SECR), maxAge);
        CookieUtil.setCookie(request, response, COOKIEKEY_TOKEN, profile.getToken().getToken(), maxAge);
        CookieUtil.setCookie(request, response, COOKIEKEY_APPKEY, appkey, maxAge);
        CookieUtil.setCookie(request, response, COOKIEKEY_PROFILEID, profile.getProfile().getProfileId(), maxAge);
//        if (profile.getUserLogin() != null) {
        CookieUtil.setCookie(request, response, COOKIEKEY_LOGINDOMAIN, loginDomain.getCode(), maxAge);
//        }
    }

    public static void writeYoukuAuthCookie(HttpServletRequest request, HttpServletResponse response, AuthProfile profile, String appkey, LoginDomain loginDomain, String nickname, String yktk) {
        long time = System.currentTimeMillis();
        try {
            saveYoukuCookieValue(request, response, COOKIEKEY_NICKNAME, URLEncoder.encode(nickname, "UTF-8"), YoukuCookieUtil.COOKIEKEY_YKDOMAIN, MAX_AGE_AUTH_USER);
        } catch (UnsupportedEncodingException e) {
        }
        saveYoukuCookieValue(request, response, COOKIEKEY_UID, String.valueOf(profile.getProfile().getUid()), YoukuCookieUtil.COOKIEKEY_YKDOMAIN, MAX_AGE_AUTH_USER);
        saveYoukuCookieValue(request, response, COOKIEKEY_UNO, profile.getProfile().getUno(), YoukuCookieUtil.COOKIEKEY_YKDOMAIN, MAX_AGE_AUTH_USER);
        saveYoukuCookieValue(request, response, COOKIEKEY_TIME, String.valueOf(time), YoukuCookieUtil.COOKIEKEY_YKDOMAIN, MAX_AGE_AUTH_USER);
        String sign = Md5Utils.md5(profile.getProfile().getUid() + profile.getProfile().getUno() + time + COOKIE_SECR);
        saveYoukuCookieValue(request, response, COOKIEKEY_SIGN, sign, YoukuCookieUtil.COOKIEKEY_YKDOMAIN, MAX_AGE_AUTH_USER);
        saveYoukuCookieValue(request, response, COOKIEKEY_TOKEN, profile.getToken().getToken(), YoukuCookieUtil.COOKIEKEY_YKDOMAIN, MAX_AGE_AUTH_USER);
        saveYoukuCookieValue(request, response, COOKIEKEY_APPKEY, appkey, YoukuCookieUtil.COOKIEKEY_YKDOMAIN, MAX_AGE_AUTH_USER);
        saveYoukuCookieValue(request, response, COOKIEKEY_LOGINDOMAIN, loginDomain.getCode(), YoukuCookieUtil.COOKIEKEY_YKDOMAIN, MAX_AGE_AUTH_USER);
        //saveYoukuCookieValue(request, response, YOUKU_COOKI_KEY_YKTK, yktk, YoukuCookieUtil.COOKIEKEY_YKDOMAIN, MAX_AGE_AUTH_USER);
        saveYoukuCookieValue(request, response, COOKIEKEY_PROFILEID, profile.getProfile().getProfileId(), YoukuCookieUtil.COOKIEKEY_YKDOMAIN, MAX_AGE_AUTH_USER);
    }


    //写到request和cookie
    public static void saveYoukuCookieValue(HttpServletRequest request, HttpServletResponse response, String name, String value, String domain, int maxAge) {
        CookieUtil.setCookie(request, response, name, value, domain, maxAge);
        request.setAttribute(name, value);
    }

    /**
     * 优先从parameter取值，然后从attribute取值，最后从cookie取值
     *
     * @param request
     * @param name
     * @return
     */
    public static String getCookieKeyValue(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (!StringUtil.isEmpty(value)) {
            return value;
        }

        Object obj = request.getAttribute(name);
        if (obj != null) {
            value = String.valueOf(obj);
            return value;
        }

        value = CookieUtil.getCookieValue(request, name);
        if (!StringUtil.isEmpty(value)) {
            return value;
        }

        return value;
    }


    /**
     * 0-未登录 1-登录
     *
     * @param request
     * @param response
     * @param flag
     */
    public static void setYoukuLoginFalgCookie(HttpServletRequest request, HttpServletResponse response, String flag) {
        if (logger.isDebugEnabled()) {
            logger.debug("setYoukuLoginFalgCookie,flag:" + flag + ",seesionid:" + request.getSession().getId());
        }

        saveYoukuCookieValue(request, response, COOKIEKEY_YOUKU_LOGINFLAG, String.valueOf(flag), YoukuCookieUtil.COOKIEKEY_YKDOMAIN, -1);
    }

    /**
     * 0-未登录 1-登录
     *
     * @param request
     */
    public static String getYoukuLoginFalgCookie(HttpServletRequest request) {
        return UserCenterCookieUtil.getCookieKeyValue(request, COOKIEVALUE_YOUKU_LOGINFLAG_NOLOGIN);
    }

    public static String getClietnIdByCookie(HttpServletRequest request, HttpServletResponse response) {
        String clientid = CookieUtil.getCookieValue(request, COOKIEKEY_CID);
        if (StringUtil.isEmpty(clientid)) {
            clientid = Md5Utils.md5(UUID.randomUUID().toString());
            CookieUtil.setCookie(request, response, COOKIEKEY_CID, clientid, Integer.MAX_VALUE);
        }

        return clientid;
    }


    public static UserCenterSession buildUserCenterSession(AuthProfile profile, HttpServletRequest request) {
        UserCenterSession userCenterSession = new UserCenterSession();
        userCenterSession.setUno(profile.getProfile().getUno());
        userCenterSession.setAppkey(profile.getProfile().getProfileKey());
        userCenterSession.setBirthday(profile.getProfile().getBirthday());
        userCenterSession.setCityId(profile.getProfile().getCityId());
        userCenterSession.setProvinceId(profile.getProfile().getProvinceId());
        userCenterSession.setAppkey(profile.getProfile().getProfileKey());
        userCenterSession.setDescription(profile.getProfile().getDescription());
        userCenterSession.setDomain(profile.getProfile().getDomain());
        userCenterSession.setFlag(profile.getProfile().getFlag());
        userCenterSession.setIcon(profile.getProfile().getIcon());
        userCenterSession.setNick(profile.getProfile().getNick());
        userCenterSession.setProfileId(profile.getProfile().getProfileId());
        userCenterSession.setQq(profile.getProfile().getQq());
        userCenterSession.setRealName(profile.getProfile().getRealName());
        userCenterSession.setSex(profile.getProfile().getSex());
        userCenterSession.setUid(profile.getProfile().getUid());
        userCenterSession.setMobile(profile.getProfile().getMobile());
        userCenterSession.setAccountFlag(profile.getUserAccount().getAccountFlag());
        userCenterSession.setIcons(profile.getProfile().getIcons());
        userCenterSession.setToken(profile.getToken().getToken());
        userCenterSession.setProfileKey(profile.getProfile().getProfileKey());

        HttpSession session = request.getSession();
        if (null != session.getAttribute(Constant.SESSION_USERCENTER)) {
            removeUserFromSession(request);
        }
        session.setAttribute(Constant.SESSION_USERCENTER, userCenterSession);
        return userCenterSession;
    }

    public static UserCenterSession buildUserCenterSessionUGCWiki(AuthProfile profile, HttpServletRequest request) {
        UserCenterSession userCenterSession = new UserCenterSession();

        userCenterSession.setUno(profile.getProfile().getUno());
        userCenterSession.setAppkey(profile.getProfile().getProfileKey());
        userCenterSession.setBirthday(profile.getProfile().getBirthday());
        userCenterSession.setCityId(profile.getProfile().getCityId());
        userCenterSession.setProvinceId(profile.getProfile().getProvinceId());
        userCenterSession.setAppkey(profile.getProfile().getProfileKey());
        userCenterSession.setDescription(profile.getProfile().getDescription());
        userCenterSession.setDomain(profile.getProfile().getDomain());
        userCenterSession.setFlag(profile.getProfile().getFlag());
        userCenterSession.setIcon(profile.getProfile().getIcon());
        userCenterSession.setNick(profile.getProfile().getNick());
        userCenterSession.setProfileId(profile.getProfile().getProfileId());
        userCenterSession.setQq(profile.getProfile().getQq());
        userCenterSession.setRealName(profile.getProfile().getRealName());
        userCenterSession.setSex(profile.getProfile().getSex());
        userCenterSession.setUid(profile.getProfile().getUid());
        userCenterSession.setMobile(profile.getProfile().getMobile());
        userCenterSession.setAccountFlag(profile.getUserAccount().getAccountFlag());
        userCenterSession.setIcons(profile.getProfile().getIcons());
        userCenterSession.setToken(profile.getToken().getToken());
        userCenterSession.setProfileKey(profile.getProfile().getProfileKey());

        HttpSession session = request.getSession();
        if (null != session.getAttribute(Constant.SESSION_USERCENTER_UGCWIKI)) {
            removeUserFromSessionUGCWiki(request);
        }
        session.setAttribute(Constant.SESSION_USERCENTER_UGCWIKI, userCenterSession);
        return userCenterSession;
    }

    public static void removeUserFromSession(HttpServletRequest request) {
        request.getSession().removeAttribute(Constant.SESSION_USER_INFO);
    }

    public static void removeUserFromSessionUGCWiki(HttpServletRequest request) {
        request.getSession().removeAttribute(Constant.SESSION_USER_INFO_UGCWIKI);
    }


    public static boolean valideteUserCenterSessionByCookie(HttpServletRequest request) {
        String uidString = UserCenterCookieUtil.getCookieKeyValue(request, COOKIEKEY_UID);
        String uno = UserCenterCookieUtil.getCookieKeyValue(request, COOKIEKEY_UNO);
        String time = UserCenterCookieUtil.getCookieKeyValue(request, COOKIEKEY_TIME);
        if (StringUtil.isEmpty(uidString) || StringUtil.isEmpty(uno) || StringUtil.isEmpty(time)) {
            return false;
        }

        String sign = UserCenterCookieUtil.getCookieKeyValue(request, COOKIEKEY_SIGN);
        String signPlatform = Md5Utils.md5(uidString + uno + time + COOKIE_SECR);
        return signPlatform.equalsIgnoreCase(sign);
    }

    public static boolean validateUserCenterSessionByCookieUGCWiki(HttpServletRequest request) {
        String uidString = UserCenterCookieUtil.getCookieKeyValue(request, UGCWIKI_COOKIEKEY_UID);
        String uno = UserCenterCookieUtil.getCookieKeyValue(request, UGCWIKI_COOKIEKEY_UNO);
        String time = UserCenterCookieUtil.getCookieKeyValue(request, UGCWIKI_COOKIEKEY_TIME);
        if (StringUtil.isEmpty(uidString) || StringUtil.isEmpty(uno) || StringUtil.isEmpty(time)) {
            return false;
        }

        String sign = UserCenterCookieUtil.getCookieKeyValue(request, UGCWIKI_COOKIEKEY_SIGN);
        String signPlatform = Md5Utils.md5(uidString + uno + time + COOKIE_SECR);
        return signPlatform.equalsIgnoreCase(sign);
    }

    public static UserCenterSession getUserCenterSeesion(HttpServletRequest request) {
        if (!valideteUserCenterSessionByCookie(request)) {
            return null;
        }

        String uidString = UserCenterCookieUtil.getCookieKeyValue(request, COOKIEKEY_UID);

        //logindomain是client 直接返回
        String loginDomainStr = UserCenterCookieUtil.getCookieKeyValue(request, COOKIEKEY_LOGINDOMAIN);
        if (LoginDomain.CLIENT.getCode().equals(loginDomainStr)) {
            return null;
        }


        long uid = -1l;
        try {
            uid = Long.parseLong(uidString);
        } catch (NumberFormatException e) {
        }

        if (uid <= 0l) {
            return null;
        }

        AuthProfile authProfile = null;
        try {
//            HashMap<String, String> map = HTTPUtil.getRequestToMap(request);
//            map.put(UserCenterUtil.TOKEN_STRING, UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_TOKEN));
//
//            authProfile = UserCenterServiceSngl.get().getAuthProfileByUid(uid, map);
            //todo 微服务改造
            UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getToken(request));
            authProfile = UserCenterServiceSngl.get().getCurrentAccount();

            if (authProfile == null) {
                return null;
            }

            return buildUserCenterSession(authProfile, request);
        } catch (ServiceException e) {
            GAlerter.lab(UserCenterCookieUtil.class.getName() + " occured ServceException.e:", e);
        }

        return null;
    }

    public static UserCenterSession getUserCenterSessionUGCWiki(HttpServletRequest request) {
        if (!validateUserCenterSessionByCookieUGCWiki(request)) {
            return null;
        }
        String uidString = UserCenterCookieUtil.getCookieKeyValue(request, UGCWIKI_COOKIEKEY_UID);
        //logindomain是client 直接返回
        String loginDomainStr = UserCenterCookieUtil.getCookieKeyValue(request, UGCWIKI_COOKIEKEY_LOGINDOMAIN);
        if (LoginDomain.CLIENT.getCode().equals(loginDomainStr)) {
            return null;
        }

        long uid = -1l;
        try {
            uid = Long.parseLong(uidString);
        } catch (NumberFormatException e) {
        }

        if (uid <= 0l) {
            return null;
        }

        AuthProfile authProfile = null;
        try {
//            HashMap<String, String> map = HTTPUtil.getRequestToMap(request);
//            map.put(UserCenterUtil.TOKEN_STRING, UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.UGCWIKI_COOKIEKEY_TOKEN));
//
//            authProfile = UserCenterServiceSngl.get().getAuthProfileByUid(uid, map);

            //todo 微服务改造
            UserCenterServiceSngl.get().setOauthToken(UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.UGCWIKI_COOKIEKEY_TOKEN));
            authProfile = UserCenterServiceSngl.get().getCurrentAccount();
            //TODO

            if (authProfile == null) {
                return null;
            }
            return buildUserCenterSessionUGCWiki(authProfile, request);
        } catch (ServiceException e) {
            GAlerter.lab(UserCenterCookieUtil.class.getName() + " occured ServceException.e:", e);
        }
        return null;
    }

    public static boolean userIsLogin(HttpServletRequest request) {
        String uidString = CookieUtil.getCookieValue(request, COOKIEKEY_UID);
        String uno = CookieUtil.getCookieValue(request, COOKIEKEY_UNO);
        String time = CookieUtil.getCookieValue(request, COOKIEKEY_TIME);
        if (StringUtil.isEmpty(uidString) || StringUtil.isEmpty(uno) || StringUtil.isEmpty(time)) {
            return false;
        }
        long uid = -1l;
        try {
            uid = Long.parseLong(uidString);
        } catch (NumberFormatException e) {
        }
        if (uid <= 0l) {
            return false;
        }


        String sign = CookieUtil.getCookieValue(request, COOKIEKEY_SIGN);
        String signPlatform = Md5Utils.md5(uidString + uno + time + COOKIE_SECR);
        return signPlatform.equalsIgnoreCase(sign);
    }

    public static String getToken(HttpServletRequest request){
        String token = UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_TOKEN);
        if(!StringUtil.isEmpty(token))
            return token;
        token = HTTPUtil.getParam(request,"token");
        return token;
    }
}


