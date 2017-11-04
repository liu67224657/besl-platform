package com.enjoyf.webapps.joyme.webpage.util.youku;

import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserCenterCookieUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 和优酷相关的JSTL表达式
 *
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/6/23
 * Description:
 */
public class YoukuTag {

    /**
     * 判断用户是否登录，如果未登录返回youku的urlcall native界面，如果登录返回普通的URL
     *
     * @param request 用来获取用户的登录标记位
     * @param url     不用urlendcoder
     * @return
     */
    public static String ykLogin(HttpServletRequest request, String url, String referer) {
        String isLogin = UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_YOUKU_LOGINFLAG);
        String HD = AppUtil.checkIsIPAD(request) ? "hd" : "";

        if (StringUtil.isEmpty(referer)) {
            referer = url;
        }

        if ("0".equals(isLogin)) {
            try {
//                return "youku" + HD + "://jsblogin?weburl=" + URLEncoder.encode(WebappConfig.get().getUrlYouku() + referer, "UTF-8");
                return "youku" + HD + "://jsblogin?isGameH5=yes";
            } catch (Exception e) {
                return url;
            }
        } else {
            return url;
        }
    }


    /**
     * 判断用户是否登录，如果未登录返回youku的urlcall native界面，如果登录返回普通的URL
     *
     * @param request 用来获取用户的登录标记位
     * @return
     */
    public static Boolean isLogin(HttpServletRequest request) {
        String isLogin = UserCenterCookieUtil.getCookieKeyValue(request, UserCenterCookieUtil.COOKIEKEY_YOUKU_LOGINFLAG);
        GAlerter.lab("===YoukuTag===isLogin=:isTrue:" + isLogin+",seesionid:"+request.getSession().getId());
//        if (StringUtil.isEmpty(isLogin) || "0".equals(isLogin)) {
//            String yktk = CookieUtil.getCookieValue(request, UserCenterCookieUtil.YOUKU_COOKI_KEY_YKTK);
//            if (StringUtil.isEmpty(yktk)) {
//                yktk = request.getParameter(UserCenterCookieUtil.YOUKU_COOKI_KEY_YKTK);
//            }
//            if (!StringUtil.isEmpty(yktk)) {
//                YoukuCookie youkuCookie = YoukuCookieUtil.praseYoukuCookieYKTK(yktk);
//                if (youkuCookie != null) {
//                    UserCenterCookieUtil.saveYoukuCookieValue(request, response, UserCenterCookieUtil.COOKIEKEY_YOUKU_LOGINFLAG,
//                            String.valueOf(UserCenterCookieUtil.COOKIEVALUE_YOUKU_LOGINFLAG_LOGIN), YoukuCookieUtil.COOKIEKEY_YKDOMAIN, -1);
//                    UserCenterCookieUtil.setYoukuLoginFalgCookie(request, response, "1");
//                    isLogin = "1";
//                }
//            }
//        }
//
//        GAlerter.lab("===YoukuTag===YoukuTag=:isLogin:" + isLogin);

        return "1".equals(isLogin);

    }

}
