package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.util.StringUtil;

/**
 * 用户中心的工具类
 * Created by ericliu on 14/10/22.
 */
public class UserCenterUtil {

    public static final String TOKEN_STRING = "token";
    public static final String UNO_STRING = "uno";


    //profile表里appkey字段，用于来源
    public static final String PROFILE_TABLE_APPKEY = "_profile_appkey_";

    public static String getUserLoginId(String loginKey, LoginDomain domain) {
        return MD5Util.Md5(loginKey + domain.getCode());
    }

    public static String getProfileId(String uno, String appKey) {
        return MD5Util.Md5(uno + appKey);
    }

    public static String getTokenCacheKey(String uno, String appKey) {
        return MD5Util.Md5(uno + appKey);
    }

    public static String getPassowrd(String password, String passwdTime) {
        return MD5Util.Md5(password + (StringUtil.isEmpty(passwdTime) ? "" : passwdTime));
    }


    public static String getProfileMobileId(String mobile, String profileKey) {
        return MD5Util.Md5(mobile + profileKey);
    }


    ///////////
    public static String getAppSumId(String appKey, String subKey) {
        return MD5Util.Md5(appKey + subKey);
    }


    public static String getActivityUserId(String appKey, String subKey, String profileId) {
        return MD5Util.Md5(appKey + subKey + profileId);
    }

    public static void main(String[] args) {
//        String pwd = "abcd@1234";
        System.out.println(getPassowrd("1234456", "1490001950786"));
    }
}
