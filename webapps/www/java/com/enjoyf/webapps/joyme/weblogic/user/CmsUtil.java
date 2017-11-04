package com.enjoyf.webapps.joyme.weblogic.user;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.thirdapi.cms.CMSAuthService;
import com.enjoyf.platform.webapps.common.util.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-13 下午4:49
 * Description:
 */
public class CmsUtil {

    private static final String KEY_MD5 = "AbDVg3815L";

    public static String authCMS(long userid, HttpServletRequest request, HttpServletResponse response) {
        String result = null;
        try {
            result = CMSAuthService.get().login(userid);

            CmsUtil.setCmsCookie(request, response, userid);
        } catch (Exception e) {
        }
        return result;
    }

    public static void logOutCMS(HttpServletRequest request, HttpServletResponse response) {
        try {
            CmsUtil.delCmsCookie(request, response);
        } catch (Exception e) {
        }
    }

    private static void setCmsCookie(HttpServletRequest request, HttpServletResponse response, long userId) {
        CookieUtil.setCookie(request, response, "DedeUserID", String.valueOf(userId), 604800000);
        CookieUtil.setCookie(request, response, "DedeUserID__ckMd5", MD5Util.Md5(KEY_MD5 + userId).substring(0, 16), 604800000);
        response.addHeader("P3P", "CP=\"NON DSP COR CURa ADMa DEVa TAIa PSAa PSDa IVAa IVDa CONa HISa TELa OTPa OUR UNRa IND UNI COM NAV INT DEM CNT PRE LOC\"");
    }

    private static void delCmsCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.setCookie(request, response, "DedeUserID", null, 0);
        CookieUtil.setCookie(request, response, "DedeUserID__ckMd5", null, 0);
        response.addHeader("P3P", "CP=\"NON DSP COR CURa ADMa DEVa TAIa PSAa PSDa IVAa IVDa CONa HISa TELa OTPa OUR UNRa IND UNI COM NAV INT DEM CNT PRE LOC\"");
    }

}
