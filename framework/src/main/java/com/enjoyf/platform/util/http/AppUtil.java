package com.enjoyf.platform.util.http;

import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.joymeapp.AppClientCommonParam;

import javax.servlet.http.HttpServletRequest;


public class AppUtil {
    private static final String[] MOBILE_SPECIFIC_SUBSTRING = {"iPad", "iPhone", "Android", "Windows Phone"};

    private static final String[] MOBILE_SPECIFIC_SUBSTRING_ANDROID = {"Android"};

    private static final String[] MOBILE_SPECIFIC_SUBSTRING_IOS = {"iPad", "iPhone"};

    private static final String[] MOBILE_SPECIFIC_SUBSTRING_IPAD = {"iPad"};

    private static final String MOBILE_SPECIFIC_WEIXIN = "micromessenger";

    /**
     * 检查是否是移动端
     *
     * @param request
     * @return
     */
    public static boolean checkMobile(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        for (String mobile : MOBILE_SPECIFIC_SUBSTRING) {
            if (userAgent.contains(mobile)
                    || userAgent.contains(mobile.toUpperCase())
                    || userAgent.contains(mobile.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static final boolean checkIsAndroid(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        if (userAgent != null) {
            for (String mobile : MOBILE_SPECIFIC_SUBSTRING_ANDROID) {
                if (userAgent.contains(mobile)
                        || userAgent.contains(mobile.toUpperCase())
                        || userAgent.contains(mobile.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static final boolean checkIsIOS(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        if (userAgent != null) {
            for (String mobile : MOBILE_SPECIFIC_SUBSTRING_IOS) {
                if (userAgent.contains(mobile)
                        || userAgent.contains(mobile.toUpperCase())
                        || userAgent.contains(mobile.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static final boolean checkIsIPAD(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        if (userAgent != null) {
            for (String mobile : MOBILE_SPECIFIC_SUBSTRING_IPAD) {
                if (userAgent.contains(mobile)
                        || userAgent.contains(mobile.toUpperCase())
                        || userAgent.contains(mobile.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static final boolean checkIsWeixin(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        if (userAgent != null) {

            return userAgent.toLowerCase().contains(MOBILE_SPECIFIC_WEIXIN);
        }
        return false;
    }


    public static String getAppKey(String appKey) {
        if (com.enjoyf.platform.util.StringUtil.isEmpty(appKey)) {
            return "";
        }
        if (appKey.length() < 23) {
            return appKey;
        }
        return appKey.substring(0, appKey.length() - 1);
    }

    public static AppClientCommonParam getCommonParam(HttpServletRequest request) {
        String platform = request.getParameter("platform") == null ? request.getHeader("platform") : request.getParameter("platform");
        String version = request.getParameter("version") == null ? request.getHeader("version") : request.getParameter("version");
        String appkey = request.getParameter("appkey") == null ? request.getHeader("appkey") : request.getParameter("appkey");
        String clientid = request.getParameter("clientid") == null ? request.getHeader("clientid") : request.getParameter("clientid");
        String channelid = request.getParameter("channelid") == null ? request.getHeader("channelid") : request.getParameter("channelid");
        String token = request.getParameter("token") == null ? request.getHeader("token") : request.getParameter("token");
        String uno = request.getParameter("uno") == null ? request.getHeader("uno") : request.getParameter("uno");
        String uid = request.getParameter("uid") == null ? request.getHeader("uid") : request.getParameter("uid");
        AppClientCommonParam constant = new AppClientCommonParam();
        try {
            constant.setPlatform(Integer.valueOf(platform));
            constant.setVersion(version);
            constant.setAppkey(appkey);
            constant.setClientid(clientid);
            constant.setChannelid(channelid);
            constant.setToken(token);
            constant.setUno(uno);
            constant.setUid(uid);
        } catch (Exception e) {
            return constant;
        }
        return constant;
    }


    public static int getVersionInt(String version) {
        int verionInt = 0;
        if (StringUtil.isEmpty(version)) {
            return verionInt;
        }
        verionInt = Integer.valueOf(version.replaceAll("\\.", ""));
        if (verionInt < 10) {
            verionInt = verionInt * 10;
        } else if (verionInt < 100) {
            verionInt = verionInt * 100;
        }
        return verionInt;
    }

    /**
     * 得到版本号（去掉buildnumber）
     * @param version
     * @return
     */
    public static String getVersionCode(String version){
        int buildNumberIdx=version.lastIndexOf(".");
        if(buildNumberIdx<0){
            return version;
        }

        String[] versionArr=version.split("\\.");
        if(versionArr.length<=3){
            return version;
        }

        return versionArr[0]+"."+versionArr[1]+"."+versionArr[2];
    }

}
