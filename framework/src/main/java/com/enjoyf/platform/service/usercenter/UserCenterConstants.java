package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * Created by ericliu on 14/10/22.
 */
public class UserCenterConstants {
    public static final String SERVICE_SECTION = "usercenterservice";

    public static final String SERVICE_TYPE = "usercenterserver";
    public static final String SERVICE_PREFIX = "usercenterserver";


    public static final String SERVICE_SECTION_TEST = "usercenterservicetest";

    //todo usercenter
    public static final String SERVICE_TYPE_TEST = "usercenterservertest";
    public static final String SERVICE_PREFIX_TEST = "usercenterservertest";

    public static final String KEY_USERCENTER_UID = "redis_uc_uid";
    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    public static final byte AUTH = 1;
    public static final byte BIND = 2;
    public static final byte LOGIN = 3;
    public static final byte REGISTER = 4;
    public static final byte GET_TOKEN = 5;

    public static final byte GET_PROFILE_BYUNO = 6;
    public static final byte GET_PROFILE_BYUID = 7;
    public static final byte QUERY_PROFILE_PROFILES = 8;
    public static final byte GET_USERLOGIN = 9;
    public static final byte QUERY_USERLOGIN_BY_UNO_LOGINDOMAINS = 10;
    public static final byte MODIFY_USERLOGIN = 11;
    public static final byte GET_PROFILE_BYPROFILEID = 12;
    public static final byte GET_PROFILE_BYNICK = 13;
    public static final byte MODIFY_PROFILE = 14;
    public static final byte GET_PROFILE_BYDOMAIN = 15;
    public static final byte UNBIND = 16;

    public static final byte SAVE_CODE_FOR_MEMCACHED = 17;
    public static final byte GET_CODE_FOR_MEMCACHED = 18;
    public static final byte DELETE_CODE_FOR_MEMCACHED = 19;


    public static final byte GET_USERACCOUNT = 20;
    public static final byte MODIFY_USERACCOUNT = 21;
    public static final byte GET_USERACCOUNT_BYPHONE = 22;
    public static final byte GET_USERLOGIN_BY_LOGINID = 23;

    public static final byte INIT_UID = 24;
    public static final byte GET_UID_POOL_LENGTH = 25;

    public static final byte SAVE_PASSWORD_TIME = 26;
    public static final byte GET_PASSWORD_TIME = 27;
    public static final byte REMOVE_PASSWORD_TIME = 28;

    public static final byte QUERY_PROFILE_SUM_BY_PROFILEID = 29;
    public static final byte GET_PROFILE_SUM_BY_PROFILEID = 30;

    public static final byte RECIEVE_EVENT = 31;

    public static final byte GET_AUTHPROFILE_BY_UID = 32;

    public static final byte DELETE_TOKEN = 33;


    public static final byte CHECK_MOBILE_BINDED = 34;
    public static final byte MOBILE_BIND = 35;
    public static final byte MOBILE_UNBIND = 36;

    public static final byte INCREASE_PROFILE_SUM = 37;
    public static final byte GET_PROFILE_BYCHECKNICK = 38;

    public static final byte QUERY_PROFILE_BY_QUERYEXPRESS = 39;
    public static final byte QUERY_PROFILE_BY_PAGEROWS = 42;

    public static final byte QUERY_ACTIVITY_USER = 40;

    public static final byte GET_ACTIVITY_SUM = 41;

    public static final byte GET_AUTHPROFILE_BY_UNO = 49;


    //profile permission
    public static final byte GET_PERMISSION_BY_PROFILE_ID = 51;
    public static final byte ADD_PERMISSION_FRO_PROFILE = 52;
    public static final byte DEL_PERMISSION_FRO_PROFILE = 53;

    public static final byte UNBIND_MOBILE = 54;
    public static final byte QUERY_PROFILE_PROFILES_BY_UIDS = 55;
    public static final byte QUERY_VERIFY_PROFILE_BY_IDS = 56;
    public static final byte GET_VERIFY_PROFILE_BY_ID = 57;
    public static final byte MODIFY_VERIFY_PROFILE = 58;
    public static final byte QUERY_VERIFY_PROFILE_BY_PAGE = 59;
    public static final byte QUERY_VERIFY_PROFILE_BY_TAGID = 60;
    public static final byte VERIFY_PROFILE = 61;
    public static final byte DELETE_VERIFY_PROFILE = 62;
    public static final byte QUERY_FOLLOW_VERIFY_PROFILE = 63;
    public static final byte ADD_VERIFY = 64;
    public static final byte QUERY_VERIFY = 65;
    public static final byte QUERY_VERIFY_BY_PAGE = 66;
    public static final byte GET_VERIFY = 67;
    public static final byte MODIFY_VERIFY = 68;
    public static final byte SORT_VERIFY_PROFILE_BY_TAGID = 69;

    public static final byte CREATE_PROFILE = 70;

    public static final byte GET_VERIFYPROFILE_TAGS_BYPROFILEID = 71;

    public static final byte ADD_USER_PRIVACY = 72;
    public static final byte GET_USER_PRIVACY = 73;
    public static final byte MODIFY_USER_PRIVACY = 74;

    public static final byte ADD_VERIFYPROFILE_TAGS_BYPROFILEID = 75;
    public static final byte REMOVE_VERIFYPROFILE_TAGS_BYPROFILEID = 76;

    static {
        transProfileContainer.put(new TransProfile(AUTH, "AUTH"));

        transProfileContainer.put(new TransProfile(BIND, "BIND"));
        transProfileContainer.put(new TransProfile(UNBIND, "UNBIND"));
        transProfileContainer.put(new TransProfile(LOGIN, "LOGIN"));
        transProfileContainer.put(new TransProfile(REGISTER, "REGISTER"));
        transProfileContainer.put(new TransProfile(GET_TOKEN, "GET_TOKEN"));
        transProfileContainer.put(new TransProfile(GET_PROFILE_BYUNO, "GET_PROFILE_BYUNO"));
        transProfileContainer.put(new TransProfile(GET_PROFILE_BYUID, "GET_PROFILE_BYUID"));
        transProfileContainer.put(new TransProfile(QUERY_PROFILE_PROFILES, "QUERY_PROFILE_PROFILES"));
        transProfileContainer.put(new TransProfile(GET_USERLOGIN, "GET_USERLOGIN"));
        transProfileContainer.put(new TransProfile(MODIFY_USERLOGIN, "MODIFY_USERLOGIN"));
        transProfileContainer.put(new TransProfile(QUERY_USERLOGIN_BY_UNO_LOGINDOMAINS, "QUERY_USERLOGIN_BY_UNO_LOGINDOMAINS"));
        transProfileContainer.put(new TransProfile(GET_PROFILE_BYNICK, "GET_PROFILE_BYNICK"));
        transProfileContainer.put(new TransProfile(MODIFY_PROFILE, "MODIFY_PROFILE"));
        transProfileContainer.put(new TransProfile(GET_PROFILE_BYDOMAIN, "GET_PROFILE_BYDOMAIN"));
        transProfileContainer.put(new TransProfile(SAVE_CODE_FOR_MEMCACHED, "SAVE_CODE_FOR_MEMCACHED"));
        transProfileContainer.put(new TransProfile(GET_CODE_FOR_MEMCACHED, "GET_CODE_FOR_MEMCACHED"));
        transProfileContainer.put(new TransProfile(DELETE_CODE_FOR_MEMCACHED, "DELETE_CODE_FOR_MEMCACHED"));
        transProfileContainer.put(new TransProfile(GET_USERACCOUNT, "GET_USERACCOUNT"));
        transProfileContainer.put(new TransProfile(MODIFY_USERACCOUNT, "MODIFY_USERACCOUNT"));
        transProfileContainer.put(new TransProfile(GET_USERACCOUNT_BYPHONE, "GET_USERACCOUNT_BYPHONE"));
        transProfileContainer.put(new TransProfile(GET_USERLOGIN_BY_LOGINID, "GET_USERLOGIN_BY_LOGINID"));
        transProfileContainer.put(new TransProfile(INIT_UID, "INIT_UID"));
        transProfileContainer.put(new TransProfile(GET_UID_POOL_LENGTH, "GET_UID_POOL_LENGTH"));

        transProfileContainer.put(new TransProfile(SAVE_PASSWORD_TIME, "SAVE_PASSWORD_TIME"));
        transProfileContainer.put(new TransProfile(GET_PASSWORD_TIME, "GET_PASSWORD_TIME"));
        transProfileContainer.put(new TransProfile(REMOVE_PASSWORD_TIME, "REMOVE_PASSWORD_TIME"));

        transProfileContainer.put(new TransProfile(QUERY_PROFILE_SUM_BY_PROFILEID, "QUERY_PROFILE_SUM_BY_PROFILEID"));
        transProfileContainer.put(new TransProfile(GET_PROFILE_SUM_BY_PROFILEID, "GET_PROFILE_SUM_BY_PROFILEID"));
        transProfileContainer.put(new TransProfile(RECIEVE_EVENT, "RECIEVE_EVENT"));
        transProfileContainer.put(new TransProfile(GET_AUTHPROFILE_BY_UID, "GET_AUTHPROFILE_BY_UID"));
        transProfileContainer.put(new TransProfile(DELETE_TOKEN, "DELETE_TOKEN"));

        transProfileContainer.put(new TransProfile(CHECK_MOBILE_BINDED, "CHECK_MOBILE_BINDED"));
        transProfileContainer.put(new TransProfile(MOBILE_BIND, "MOBILE_BIND"));
        transProfileContainer.put(new TransProfile(MOBILE_UNBIND, "MOBILE_UNBIND"));
        transProfileContainer.put(new TransProfile(INCREASE_PROFILE_SUM, "INCREASE_PROFILE_SUM"));
        transProfileContainer.put(new TransProfile(GET_PROFILE_BYCHECKNICK, "GET_PROFILE_BYCHECKNICK"));
        transProfileContainer.put(new TransProfile(QUERY_PROFILE_BY_QUERYEXPRESS, "QUERY_PROFILE_BY_QUERYEXPRESS"));
        transProfileContainer.put(new TransProfile(QUERY_ACTIVITY_USER, "QUERY_ACTIVITY_USER"));
        transProfileContainer.put(new TransProfile(GET_ACTIVITY_SUM, "GET_ACTIVITY_SUM"));
        transProfileContainer.put(new TransProfile(QUERY_PROFILE_BY_PAGEROWS, "QUERY_PROFILE_BY_PAGEROWS"));

        transProfileContainer.put(new TransProfile(GET_AUTHPROFILE_BY_UNO, "GET_AUTHPROFILE_BY_UNO"));

        transProfileContainer.put(new TransProfile(GET_PERMISSION_BY_PROFILE_ID, "GET_PERMISSION_BY_PROFILE_ID"));
        transProfileContainer.put(new TransProfile(ADD_PERMISSION_FRO_PROFILE, "ADD_PERMISSION_FRO_PROFILE"));
        transProfileContainer.put(new TransProfile(DEL_PERMISSION_FRO_PROFILE, "DEL_PERMISSION_FRO_PROFILE"));
        transProfileContainer.put(new TransProfile(UNBIND_MOBILE, "UNBIND_MOBILE"));

        transProfileContainer.put(new TransProfile(QUERY_PROFILE_PROFILES_BY_UIDS, "QUERY_PROFILE_PROFILES_BY_UIDS"));
        transProfileContainer.put(new TransProfile(QUERY_VERIFY_PROFILE_BY_IDS, "QUERY_VERIFY_PROFILE_BY_IDS"));
        transProfileContainer.put(new TransProfile(GET_VERIFY_PROFILE_BY_ID, "GET_VERIFY_PROFILE_BY_ID"));
        transProfileContainer.put(new TransProfile(MODIFY_VERIFY_PROFILE, "MODIFY_VERIFY_PROFILE"));
        transProfileContainer.put(new TransProfile(QUERY_VERIFY_PROFILE_BY_PAGE, "QUERY_VERIFY_PROFILE_BY_PAGE"));
        transProfileContainer.put(new TransProfile(QUERY_VERIFY_PROFILE_BY_TAGID, "QUERY_VERIFY_PROFILE_BY_TAGID"));
        transProfileContainer.put(new TransProfile(VERIFY_PROFILE, "VERIFY_PROFILE"));
        transProfileContainer.put(new TransProfile(DELETE_VERIFY_PROFILE, "DELETE_VERIFY_PROFILE"));
        transProfileContainer.put(new TransProfile(QUERY_FOLLOW_VERIFY_PROFILE, "QUERY_FOLLOW_VERIFY_PROFILE"));
        transProfileContainer.put(new TransProfile(ADD_VERIFY, "ADD_VERIFY"));
        transProfileContainer.put(new TransProfile(QUERY_VERIFY, "QUERY_VERIFY"));
        transProfileContainer.put(new TransProfile(QUERY_VERIFY_BY_PAGE, "QUERY_VERIFY_BY_PAGE"));
        transProfileContainer.put(new TransProfile(GET_VERIFY, "GET_VERIFY"));
        transProfileContainer.put(new TransProfile(MODIFY_VERIFY, "MODIFY_VERIFY"));
        transProfileContainer.put(new TransProfile(SORT_VERIFY_PROFILE_BY_TAGID, "SORT_VERIFY_PROFILE_BY_TAGID"));


        transProfileContainer.put(new TransProfile(CREATE_PROFILE, "CREATE_PROFILE"));

        transProfileContainer.put(new TransProfile(GET_VERIFYPROFILE_TAGS_BYPROFILEID, "GET_VERIFYPROFILE_TAGS_BYPROFILEID"));

        transProfileContainer.put(new TransProfile(ADD_USER_PRIVACY, "ADD_USER_PRIVACY"));
        transProfileContainer.put(new TransProfile(GET_USER_PRIVACY, "GET_USER_PRIVACY"));
        transProfileContainer.put(new TransProfile(MODIFY_USER_PRIVACY, "MODIFY_USER_PRIVACY"));

        transProfileContainer.put(new TransProfile(ADD_VERIFYPROFILE_TAGS_BYPROFILEID, "ADD_VERIFYPROFILE_TAGS_BYPROFILEID"));
        transProfileContainer.put(new TransProfile(REMOVE_VERIFYPROFILE_TAGS_BYPROFILEID, "REMOVE_VERIFYPROFILE_TAGS_BYPROFILEID"));
    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
