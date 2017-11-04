/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class ProfileConstants {
    public static final String SERVICE_SECTION = "profileservice";
    public static final String SERVICE_PREFIX = "profileserver";
    public static final String SERVICE_TYPE = "profileserver";
    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    public static final byte PROFILE_GET_BY_UNO = 1;
    public static final byte PROFILE_QUERY_BY_UNOS = 2;

    public static final byte PROFILE_DETAIL_CREATE = 3;
    public static final byte PROFILE_DETAIL_GET = 4;
    public static final byte PROFILE_DETAIL_UPDATE = 5;
    public static final byte PROFILE_DETAIL_UPDATE_BY_MAP = 6;
    public static final byte PROFILE_BLOG_UPDATE_BY_EXPRESS = 7;

    public static final byte PROFILE_SUM_INCREASE = 8;
    public static final byte PROFILE_SUM_GET = 9;
    public static final byte PROFILE_SUM_UPDATE = 10;
    public static final byte PROFILE_SUM_UPDATE_BY_MAP = 11;

    public static final byte PROFILE_SETTING_CREATE = 12;
    public static final byte PROFILE_SETTING_GET = 13;
    public static final byte PROFILE_SETTING_UPDATE = 14;
    public static final byte PROFILE_SETTING_UPDATE_BY_MAP = 15;

    public static final byte PROFILE_BLOG_CREATE = 16;
    public static final byte PROFILE_BLOG_SEARCH = 17;
    public static final byte PROFILE_BLOG_GET_BY_UNO = 18;
    public static final byte PROFILE_BLOG_GET_BY_SCREENNAME = 19;
    public static final byte PROFILE_BLOG_GET_BY_DOMAIN = 20;
    public static final byte PROFILE_BLOG_UPDATE = 21;
    public static final byte PROFILE_BLOG_UPDATE_BY_MAP = 22;
    public static final byte PROFILE_BLOG_QUERY_BY_SCREENNAME_SET = 23;
    public static final byte PROFILE_BLOG_QUERY_BY_DATE_STEP = 24;
    public static final byte PROFILE_BLOG_CREATE_GENDOMAIN = 25;

    public static final byte PROFILE_EXPERIENCE_CREATE = 26;
    public static final byte PROFILE_EXPERIENCE_REMOVE = 27;
    public static final byte PROFILE_EXPERIENCE_UPDATE = 28;
    public static final byte PROFILE_EXPERIENCE_QUERY = 29;

    public static final byte PROFILE_BLOG_QUERY_BY_PHONENUM_STATUS = 30;


    public static final byte VIEW_ACCOUNT_PROFILE_BLOG_BY_ACCOUNT_QUERY_BY_PARAM = 125;
    public static final byte VIEW_ACCOUNT_PROFILE_BLOG_BY_BLOG_QUERY_BY_PARAM = 126;

    public static final byte RECIEVE_EVENT = 127;

    public static final byte PROFILE_BLOGS_QUERY_BY_LIKE_SCREENNAME = 31;
    public static final byte COMMON_UPDATE = 32;
    public static final byte COMMON_QUERY = 33;

    public static final byte PROFILE_MOBILE_DEVICE_CREATE = 34;
    public static final byte PROFILE_MOBILE_DEVICE_GET = 35;
    public static final byte PROFILE_MOBILE_DEVICE_QUERY_BY_PAGE = 36;
    public static final byte PROFILE_MOBILE_DEVICE_QUERY_BY_RANGE = 37;
    public static final byte PROFILE_MOBILE_DEVICE_UPDATE = 38;

    public static final byte CREATE_PROFILE_NEWRELEASE = 39;
    public static final byte GET_PROFILE_NEWRELEASE = 40;
    public static final byte QUERY_PROFILE_NEWRELEASE = 41;
    public static final byte QUERY_PROFILE_NEWRELEASE_BY_PAGE = 42;
    public static final byte MODIFY_PROFILE_NEWRELEASE = 43;

    public static final byte SAVE_CODE_FOR_MEMCACHED = 44;
    public static final byte GET_CODE_FOR_MEMCACHED = 45;
    public static final byte DELETE_CODE_FOR_MEMCACHED = 46;
    public static final byte CREATE_PROFILE_PLAYING_GAMES = 47;
    public static final byte GET_PROFILE_PLAYING_GAMES = 48;
    public static final byte CREATE_PROFILE_CLIENT_MOBILE_DEVICE = 49;
    public static final byte GET_PROFILE_CLIENT_MOBILE_DEVICE = 50;
    public static final byte SEND_PUSHMESSAGE = 51;

    public static final byte GET_SOCIAL_PROFIEL_BLOG_BY_SCREENNAME = 52;
    public static final byte GET_SOCIAL_PROFIEL_BLOG_BY_UNO = 53;
    public static final byte QUERY_SOCIAL_PROFILE_MAP_BY_UNOS = 54;
    public static final byte CREATE_SOCIAL_PROFILE = 55;

    public static final byte MODIFY_SOCIAL_PROFIEL_BLOG_BY_UNO = 56;
    public static final byte MODIFY_SOCIAL_PROFIEL_DETAIL_BY_UNO = 57;
    public static final byte QUERY_SOCIAL_PROFIELS_BY_UNOS = 58;
    public static final byte QUERY_NEWEST_SOCIAL_PROFILE = 59;

    static {
        transProfileContainer.put(new TransProfile(PROFILE_DETAIL_CREATE, "PROFILE_DETAIL_CREATE"));
        transProfileContainer.put(new TransProfile(PROFILE_DETAIL_GET, "PROFILE_DETAIL_GET"));
        transProfileContainer.put(new TransProfile(PROFILE_DETAIL_UPDATE, "PROFILE_DETAIL_UPDATE"));
        transProfileContainer.put(new TransProfile(PROFILE_DETAIL_UPDATE_BY_MAP, "PROFILE_DETAIL_UPDATE_BY_MAP"));
        transProfileContainer.put(new TransProfile(PROFILE_BLOG_UPDATE_BY_EXPRESS, "PROFILE_DETAIL_UPDATE_BY_EXPRESS"));


        transProfileContainer.put(new TransProfile(PROFILE_SUM_INCREASE, "PROFILE_SUM_INCREASE"));
        transProfileContainer.put(new TransProfile(PROFILE_SUM_GET, "PROFILE_SUM_GET"));
        transProfileContainer.put(new TransProfile(PROFILE_SUM_UPDATE, "PROFILE_SUM_UPDATE"));
        transProfileContainer.put(new TransProfile(PROFILE_SUM_UPDATE_BY_MAP, "PROFILE_SUM_UPDATE_BY_MAP"));

        transProfileContainer.put(new TransProfile(PROFILE_SETTING_CREATE, "PROFILE_SETTING_CREATE"));
        transProfileContainer.put(new TransProfile(PROFILE_SETTING_GET, "PROFILE_SETTING_GET"));
        transProfileContainer.put(new TransProfile(PROFILE_SETTING_UPDATE, "PROFILE_SETTING_UPDATE"));
        transProfileContainer.put(new TransProfile(PROFILE_SETTING_UPDATE_BY_MAP, "PROFILE_SETTING_UPDATE_BY_MAP"));

        transProfileContainer.put(new TransProfile(PROFILE_BLOG_CREATE, "PROFILE_BLOG_CREATE"));
        transProfileContainer.put(new TransProfile(PROFILE_BLOG_CREATE_GENDOMAIN, "PROFILE_BLOG_CREATE_GENDOMAIN"));
        transProfileContainer.put(new TransProfile(PROFILE_BLOG_SEARCH, "PROFILE_BLOG_SEARCH"));

        transProfileContainer.put(new TransProfile(PROFILE_BLOG_GET_BY_UNO, "PROFILE_BLOG_GET_BY_UNO"));
        transProfileContainer.put(new TransProfile(PROFILE_BLOG_GET_BY_SCREENNAME, "PROFILE_BLOG_GET_BY_SCREENNAME"));
        transProfileContainer.put(new TransProfile(PROFILE_BLOG_GET_BY_DOMAIN, "PROFILE_BLOG_GET_BY_DOMAIN"));
        transProfileContainer.put(new TransProfile(PROFILE_BLOG_UPDATE, "PROFILE_BLOG_UPDATE"));
        transProfileContainer.put(new TransProfile(PROFILE_BLOG_UPDATE_BY_MAP, "PROFILE_BLOG_UPDATE_BY_MAP"));
        transProfileContainer.put(new TransProfile(PROFILE_BLOG_QUERY_BY_SCREENNAME_SET, "PROFILE_BLOG_QUERY_BY_SCREENNAME_SET"));
        transProfileContainer.put(new TransProfile(PROFILE_BLOG_QUERY_BY_DATE_STEP, "PROFILE_BLOG_QUERY_BY_DATE_STEP"));

        transProfileContainer.put(new TransProfile(PROFILE_EXPERIENCE_CREATE, "PROFILE_EXPERIENCE_CREATE"));
        transProfileContainer.put(new TransProfile(PROFILE_EXPERIENCE_REMOVE, "PROFILE_EXPERIENCE_REMOVE"));
        transProfileContainer.put(new TransProfile(PROFILE_EXPERIENCE_UPDATE, "PROFILE_EXPERIENCE_UPDATE"));
        transProfileContainer.put(new TransProfile(PROFILE_EXPERIENCE_QUERY, "PROFILE_EXPERIENCE_QUERY"));

        transProfileContainer.put(new TransProfile(PROFILE_GET_BY_UNO, "PROFILE_GET_BY_UNO"));
        transProfileContainer.put(new TransProfile(PROFILE_QUERY_BY_UNOS, "PROFILE_QUERY_BY_UNOS"));

//        transProfileContainer.put(new TransProfile(VIEW_ACCOUNT_PROFILE_BLOG_BY_ACCOUNT_QUERY_BY_PARAM, "VIEW_ACCOUNT_PROFILE_BLOG_BY_ACCOUNT_QUERY_BY_PARAM"));
//        transProfileContainer.put(new TransProfile(VIEW_ACCOUNT_PROFILE_BLOG_BY_BLOG_QUERY_BY_PARAM, "VIEW_ACCOUNT_PROFILE_BLOG_BY_BLOG_QUERY_BY_PARAM"));

        transProfileContainer.put(new TransProfile(RECIEVE_EVENT, "RECIEVE_EVENT"));

        transProfileContainer.put(new TransProfile(PROFILE_BLOGS_QUERY_BY_LIKE_SCREENNAME, "PROFILE_BLOGS_QUERY_BY_LIKE_SCREENNAME"));
        transProfileContainer.put(new TransProfile(COMMON_UPDATE, "COMMON_UPDATE"));
        transProfileContainer.put(new TransProfile(COMMON_QUERY, "COMMON_QUERY"));

        transProfileContainer.put(new TransProfile(PROFILE_MOBILE_DEVICE_CREATE, "PROFILE_MOBILE_DEVICE_CREATE"));
        transProfileContainer.put(new TransProfile(PROFILE_MOBILE_DEVICE_GET, "PROFILE_MOBILE_DEVICE_GET"));
        transProfileContainer.put(new TransProfile(PROFILE_MOBILE_DEVICE_QUERY_BY_PAGE, "PROFILE_MOBILE_DEVICE_QUERY_BY_PAGE"));
        transProfileContainer.put(new TransProfile(PROFILE_MOBILE_DEVICE_QUERY_BY_RANGE, "PROFILE_MOBILE_DEVICE_QUERY_BY_RANGE"));
        transProfileContainer.put(new TransProfile(PROFILE_MOBILE_DEVICE_UPDATE, "PROFILE_MOBILE_DEVICE_UPDATE"));
        transProfileContainer.put(new TransProfile(PROFILE_BLOG_QUERY_BY_PHONENUM_STATUS, "PROFILE_BLOG_QUERY_BY_PHONENUM_STATUS"));

        transProfileContainer.put(new TransProfile(SAVE_CODE_FOR_MEMCACHED, "SAVE_CODE_FOR_MEMCACHED"));
        transProfileContainer.put(new TransProfile(GET_CODE_FOR_MEMCACHED, "GET_CODE_FOR_MEMCACHED"));
        transProfileContainer.put(new TransProfile(DELETE_CODE_FOR_MEMCACHED, "DELETE_CODE_FOR_MEMCACHED"));

        transProfileContainer.put(new TransProfile(CREATE_PROFILE_PLAYING_GAMES, "CREATE_PROFILE_PLAYING_GAMES"));
        transProfileContainer.put(new TransProfile(GET_PROFILE_PLAYING_GAMES, "GET_PROFILE_PLAYING_GAMES"));

        transProfileContainer.put(new TransProfile(CREATE_PROFILE_CLIENT_MOBILE_DEVICE, "CREATE_PROFILE_CLIENT_MOBILE_DEVICE"));
        transProfileContainer.put(new TransProfile(GET_PROFILE_CLIENT_MOBILE_DEVICE, "GET_PROFILE_CLIENT_MOBILE_DEVICE"));
        transProfileContainer.put(new TransProfile(SEND_PUSHMESSAGE, "SEND_PUSHMESSAGE"));

        transProfileContainer.put(new TransProfile(GET_SOCIAL_PROFIEL_BLOG_BY_SCREENNAME, "GET_SOCIAL_PROFIEL_BLOG_BY_SCREENNAME"));
        transProfileContainer.put(new TransProfile(CREATE_SOCIAL_PROFILE, "CREATE_SOCIAL_PROFILE"));
        transProfileContainer.put(new TransProfile(GET_SOCIAL_PROFIEL_BLOG_BY_UNO, "GET_SOCIAL_PROFIEL_BLOG_BY_UNO"));
        transProfileContainer.put(new TransProfile(QUERY_SOCIAL_PROFILE_MAP_BY_UNOS, "QUERY_SOCIAL_PROFILE_MAP_BY_UNOS"));
        transProfileContainer.put(new TransProfile(MODIFY_SOCIAL_PROFIEL_BLOG_BY_UNO, "MODIFY_SOCIAL_PROFIEL_BLOG_BY_UNO"));
        transProfileContainer.put(new TransProfile(MODIFY_SOCIAL_PROFIEL_DETAIL_BY_UNO, "MODIFY_SOCIAL_PROFIEL_DETAIL_BY_UNO"));
        transProfileContainer.put(new TransProfile(QUERY_SOCIAL_PROFIELS_BY_UNOS, "QUERY_SOCIAL_PROFIELS_BY_UNOS"));
        transProfileContainer.put(new TransProfile(QUERY_NEWEST_SOCIAL_PROFILE, "QUERY_NEWEST_SOCIAL_PROFILE"));

    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}