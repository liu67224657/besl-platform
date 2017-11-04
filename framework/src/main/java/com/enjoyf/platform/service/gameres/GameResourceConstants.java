package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * Author: zhaoxin
 * Date: 11-8-25
 * Time: 下午12:51
 * Desc:
 */
public class GameResourceConstants {
    //
    public static final String SERVICE_SECTION = "gameresservice";
    public static final String SERVICE_PREFIX = "gameresserver";
    public static final String SERVICE_TYPE = "gameresserver";
    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();
    //
    public static final byte GAME_RESOURCE_CREATE = 1;

    //
    public static final byte GAME_RESOURCE_GET = 2;
    public static final byte GAME_RESOURCE_QUERY = 3;
    public static final byte GAME_RESOURCE_QUERY_BY_PAGE = 4;
    public static final byte GAME_RESOURCE_QUERY_BY_SYNONYMS = 5;
    public static final byte GAME_RESOURCE_QUERY_MAP_BY_SYNONYMSES = 6;
    public static final byte GAME_RESOURCE_MODIFY = 7;
    public static final byte GAME_RESOURCE_DELETE = 8;
    public static final byte GAME_RESOURCE_SEARCHE_WORD = 9;

    public static final byte GAME_RELATION_CREATE = 10;
    public static final byte GAME_RELATION_MODIFY = 11;
    public static final byte GAME_RESOURCE_QUERY_BY_RELATIONVALUE = 12;
    public static final byte GAME_RELATION_GET = 13;
    public static final byte GAME_RELATION_QUERY = 14;
    public static final byte GAME_RELATION_QUERY_GAMERESOURCE = 15;
    public static final byte GAME_PROPERTY_BATCHICREATE = 16;


    public static final byte CREATE_GROUPUSER = 17;
    public static final byte GET_GROUPUSER_UNO_GROUPID = 18;
    public static final byte QUERY_GROUPUSER_BY_PAGE = 19;
    public static final byte MODIFY_GROUPUSER = 20;

    public static final byte GAME_PROPERTY_CREATE = 21;
    public static final byte GAME_PROPERTY_MODIFY = 22;

    public static final byte GAME_LAYOUT_CREATE = 23;
    public static final byte GAME_LAYOUT_MODIFY = 24;
    public static final byte GAME_LAYOUT_GET = 25;


    public static final byte GAME_PRIVACT_CREATE = 26;
    public static final byte GAME_PRIVACT_GET = 27;
    public static final byte GAME_PRIVACT_QUERY = 28;
    public static final byte GAME_PRIVACT_MODIFY = 29;
    public static final byte GAME_PRIVACT_DELETE = 30;


    public static final byte WIKI_RESOURCE_QUERY_BY_PAGE = 31;
    public static final byte WIKI_RESOURCE_GET = 32;
    public static final byte WIKI_RESOURCE_MODIFY = 33;
    public static final byte WIKI_RESOURCE_CREATE = 34;
    public static final byte WIKI_RESOURCE_QUERY = 35;
    public static final byte WIKI_RESOURCE_STAT = 36;

    //
    public static final byte RECIEVE_EVENT = 127;
    //new game preview
    public static final byte CREATE_NEW_GAME_INFO = 37;
    public static final byte MODIFY_NEW_GAME_INFO = 38;
    public static final byte GET_NEW_GAME_INFO = 39;
    public static final byte QUERY_NEW_GAME_INFO = 40;
    public static final byte QUERY_NEW_GAME_INFO_BY_PAGE = 41;
    public static final byte CREATE_NEW_GAME_TAG = 42;
    public static final byte MODIFY_NEW_GAME_TAG = 43;
    public static final byte GET_NEW_GAME_TAG = 44;
    public static final byte QUERY_NEW_GAME_TAG = 45;
    public static final byte QUERY_NEW_GAME_TAG_BY_PAGE = 46;
    public static final byte QUERY_NEW_TEG_RELATION = 47;
    public static final byte CREATE_CITY = 48;
    public static final byte MODIFY_CITY = 49;
    public static final byte QUERY_CITY = 50;
    public static final byte QUERY_CITY_RELATION = 51;
    public static final byte MODIFY_CITY_RELATION = 52;
    public static final byte CREATE_TAG_RELATION = 53;
    public static final byte MODIFY_TAG_RELATION = 54;
    //group privilege
    public static final byte CREATE_ROLE = 55;
    public static final byte GET_ROLE = 56;
    public static final byte QUERY_ROLE = 57;
    public static final byte QUERY_ROLE_BY_PAGE = 58;
    public static final byte MODIFY_ROLE = 59;

    public static final byte CREATE_GROUP_PRIVILEGE = 60;
    public static final byte GET_GROUP_PRIVILEGE = 61;
    public static final byte QUERY_GROUP_PRIVILEGE = 62;
    public static final byte QUERY_GROUP_PRIVILEGE_BY_PAGE = 63;
    public static final byte MODIFY_GROUP_PRIVILEGE = 64;

    public static final byte CREATE_PRIVILEGE_ROLE_RELATION = 65;
    public static final byte GET_PRIVILEGE_ROLE_RELATION = 66;
    public static final byte QUERY_PRIVILEGE_ROLE_RELATION = 67;
    public static final byte QUERY_PRIVILEGE_ROLE_RELATION_BY_PAGE = 68;
    public static final byte MODIFY_PRIVILEGE_ROLE_RELATION = 69;

    public static final byte CREATE_GROUP_PROFILE = 70;
    public static final byte GET_GROUP_PROFILE = 71;
    public static final byte QUERY_GROUP_PROFILE = 72;
    public static final byte QUERY_GROUP_PROFILE_BY_PAGE = 73;
    public static final byte MODIFY_GROUP_PROFILE = 74;
    public static final byte CREATE_GROUP_PROFILE_PRIVILEGE = 75;
    public static final byte GET_GROUP_PROFILE_PRIVILEGE = 76;
    public static final byte QUERY_GROUP_PROFILE_PRIVILEGE = 77;
    public static final byte QUERY_GROUP_PROFILE_PRIVILEGE_BY_PAGE = 78;
    public static final byte MODIFY_GROUP_PROFILE_PRIVILEGE = 79;
    public static final byte GET_ROLE_BY_QUERYEXPRESS = 80;
    public static final byte GET_GROUP_PROFILE_BY_QUERY = 81;
    public static final byte QUERY_ROLE_BY_QUERY = 82;

    public static final byte GAME_DB_CREATE = 83;
    public static final byte GAME_DB_QUERY = 84;
    public static final byte GAME_DB_GET = 85;
    public static final byte GAME_DB_UPDATE = 86;

    public static final byte GAME_DB_CHANNEL_CREATE = 87;
    public static final byte GAME_DB_CHANNEL_QUERY = 88;
    public static final byte GAME_DB_QUERY_PAGE = 89;
    public static final byte CREATE_GAME_PROPERTY_INFO = 91;
    public static final byte QUERY_GAME_PROPERTY_INFO = 92;
    public static final byte QUERY_GAME_PROPERTY_INFO_BY_PAGE = 93;

    public static final byte QUERY_GAMEDB_BY_SET_RETURN_MAP = 94;

    public static final byte INSERT_GAME_SUBSCRIBE = 95;
    public static final byte QUERY_GAME_SUBSCRIBE = 96;
    public static final byte GET_GAME_SUBSCRIBE = 97;
    public static final byte UPDATE_GAME_SUBSCRIBE = 98;
    public static final byte GAME_DB_CATEGORY = 99;

    public static final byte GAME_DB_COUNT = 100;

    public static final byte GAME_DB_RELATION_CREATE = 101;
    public static final byte GAME_DB_RELATION_QUERY = 102;
    public static final byte GAME_DB_RELATION_UPDATE = 103;
    public static final byte GAME_DB_RELATION_DELETE = 104;
    public static final byte GAME_DB_RELATION_GET = 105;

    public static final byte GAME_DB_BY_CACHE = 106;
    public static final byte GAME_DBCHANNEL_BY_CODE = 107;

    public static final byte GAME_BRAND_CREATE = 108;
    public static final byte GAME_BRAND_QUERY = 109;
    public static final byte PUT_GAME_COLLECTION_LIST_CACHE = 110;
    public static final byte GET_GAME_COLLECTION_LIST_CACHE = 111;
    public static final byte REMOVE_GAME_COLLECTION_LIST_CACHE = 112;
    public static final byte GET_GAME_DB_BY_ANOTHER_NAME = 113;
    public static final byte QUERY_GAME_ARCHIVES_BY_CACHE = 114;
    public static final byte PUT_GAME_ARCHIVES_BY_CACHE = 115;
    public static final byte REMOVE_GAME_ARCHIVES_BY_CACHE = 116;
    public static final byte GET_USER_LIKE_GAME = 117;
    public static final byte INC_USER_LIKE_GAME = 118;
    public static final byte GAME_BRAND_MODIFY = 119;
    public static final byte INCR_GAME_COLLECTION_LIST_CACHE = 120;
    public static final byte GET_GAME_ORDERED = 121;
    public static final byte CREATE_GAME_ORDERED = 122;
    public static final byte GET_GAME_FILTER_GROUP = 123;
    public static final byte UPDATE_GAME_FILTER_GROUP = 124;
    public static final byte INSERT_GAME_SHARE_NUM = 125;
    public static final byte GET_GAME_SHARE_NUM = 126;

    static {
        //
        transProfileContainer.put(new TransProfile(GAME_RESOURCE_CREATE, "GAME_RESOURCE_CREATE"));
        transProfileContainer.put(new TransProfile(GAME_RESOURCE_GET, "GAME_RESOURCE_GET"));

        transProfileContainer.put(new TransProfile(GAME_RESOURCE_QUERY, "GAME_RESOURCE_QUERY"));
        transProfileContainer.put(new TransProfile(GAME_RESOURCE_QUERY_BY_PAGE, "GAME_RESOURCE_QUERY_BY_PAGE"));
        transProfileContainer.put(new TransProfile(GAME_RESOURCE_QUERY_BY_SYNONYMS, "GAME_RESOURCE_QUERY_BY_SYNONYMS"));
        transProfileContainer.put(new TransProfile(GAME_RESOURCE_QUERY_MAP_BY_SYNONYMSES, "GAME_RESOURCE_QUERY_MAP_BY_SYNONYMSES"));

        transProfileContainer.put(new TransProfile(GAME_RESOURCE_MODIFY, "GAME_RESOURCE_MODIFY"));

        transProfileContainer.put(new TransProfile(GAME_RESOURCE_DELETE, "GAME_RESOURCE_DELETE"));

        transProfileContainer.put(new TransProfile(GAME_RELATION_CREATE, "GAME_RELATION_CREATE"));

        transProfileContainer.put(new TransProfile(GAME_RELATION_MODIFY, "GAME_RELATION_MODIFY"));

        transProfileContainer.put(new TransProfile(GAME_RESOURCE_QUERY_BY_RELATIONVALUE, "GAME_RESOURCE_QUERY_BY_RELATIONVALUE"));

        transProfileContainer.put(new TransProfile(GAME_RELATION_GET, "GAME_RELATION_GET"));

        transProfileContainer.put(new TransProfile(GAME_RELATION_QUERY, "GAME_RELATION_QUERY"));

        transProfileContainer.put(new TransProfile(GAME_RELATION_QUERY_GAMERESOURCE, "GAME_RELATION_QUERY_GAMERESOURCE"));

        transProfileContainer.put(new TransProfile(GAME_PROPERTY_CREATE, "GAME_PROPERTY_CREATE"));
        transProfileContainer.put(new TransProfile(GAME_PROPERTY_BATCHICREATE, "GAME_PROPERTY_BATCHICREATE"));
        transProfileContainer.put(new TransProfile(GAME_PROPERTY_MODIFY, "GAME_PROPERTY_MODIFY"));

        //
        transProfileContainer.put(new TransProfile(RECIEVE_EVENT, "RECIEVE_EVENT"));


        transProfileContainer.put(new TransProfile(GAME_PRIVACT_CREATE, "GAME_PRIVACT_CREATE"));
        transProfileContainer.put(new TransProfile(GAME_PRIVACT_GET, "GAME_PRIVACT_GET"));
        transProfileContainer.put(new TransProfile(GAME_PRIVACT_QUERY, "GAME_PRIVACT_QUERY"));
        transProfileContainer.put(new TransProfile(GAME_PRIVACT_MODIFY, "GAME_PRIVACT_MODIFY"));
        transProfileContainer.put(new TransProfile(GAME_PRIVACT_DELETE, "GAME_PRIVACT_DELETE"));

        transProfileContainer.put(new TransProfile(WIKI_RESOURCE_QUERY_BY_PAGE, "WIKI_RESOURCE_QUERY_BY_PAGE"));
        transProfileContainer.put(new TransProfile(WIKI_RESOURCE_GET, "WIKI_RESOURCE_GET"));
        transProfileContainer.put(new TransProfile(WIKI_RESOURCE_MODIFY, "WIKI_RESOURCE_MODIFY"));
        transProfileContainer.put(new TransProfile(WIKI_RESOURCE_CREATE, "WIKI_RESOURCE_CREATE"));
        transProfileContainer.put(new TransProfile(WIKI_RESOURCE_QUERY, "WIKI_RESOURCE_QUERY"));
        transProfileContainer.put(new TransProfile(WIKI_RESOURCE_STAT, "WIKI_RESOURCE_STAT"));

        //new game preview
        transProfileContainer.put(new TransProfile(CREATE_NEW_GAME_INFO, "CREATE_NEW_GAME_INFO"));
        transProfileContainer.put(new TransProfile(MODIFY_NEW_GAME_INFO, "MODIFY_NEW_GAME_INFO"));
        transProfileContainer.put(new TransProfile(GET_NEW_GAME_INFO, "GET_NEW_GAME_INFO"));
        transProfileContainer.put(new TransProfile(QUERY_NEW_GAME_INFO, "QUERY_NEW_GAME_INFO"));
        transProfileContainer.put(new TransProfile(QUERY_NEW_GAME_INFO_BY_PAGE, "QUERY_NEW_GAME_INFO_BY_PAGE"));
        transProfileContainer.put(new TransProfile(CREATE_NEW_GAME_TAG, "CREATE_NEW_GAME_TAG"));
        transProfileContainer.put(new TransProfile(MODIFY_NEW_GAME_TAG, "MODIFY_NEW_GAME_TAG"));
        transProfileContainer.put(new TransProfile(GET_NEW_GAME_TAG, "GET_NEW_GAME_TAG"));
        transProfileContainer.put(new TransProfile(QUERY_NEW_GAME_TAG, "QUERY_NEW_GAME_TAG"));
        transProfileContainer.put(new TransProfile(QUERY_NEW_GAME_TAG_BY_PAGE, "QUERY_NEW_GAME_TAG_BY_PAGE"));
        transProfileContainer.put(new TransProfile(QUERY_NEW_TEG_RELATION, "QUERY_NEW_TEG_RELATION"));
        transProfileContainer.put(new TransProfile(CREATE_CITY, "CREATE_CITY"));
        transProfileContainer.put(new TransProfile(MODIFY_CITY, "MODIFY_CITY"));
        transProfileContainer.put(new TransProfile(QUERY_CITY, "QUERY_CITY"));
        transProfileContainer.put(new TransProfile(QUERY_CITY_RELATION, "QUERY_CITY_RELATION"));
        transProfileContainer.put(new TransProfile(MODIFY_CITY_RELATION, "MODIFY_CITY_RELATION"));
        transProfileContainer.put(new TransProfile(CREATE_TAG_RELATION, "CREATE_TAG_RELATION"));
        transProfileContainer.put(new TransProfile(MODIFY_TAG_RELATION, "MODIFY_TAG_RELATION"));

        transProfileContainer.put(new TransProfile(CREATE_GROUPUSER, "CREATE_GROUPUSER"));
        transProfileContainer.put(new TransProfile(GET_GROUPUSER_UNO_GROUPID, "GET_GROUPUSER_UNO_GROUPID"));
        transProfileContainer.put(new TransProfile(QUERY_GROUPUSER_BY_PAGE, "QUERY_GROUPUSER_BY_PAGE"));
        transProfileContainer.put(new TransProfile(MODIFY_GROUPUSER, "MODIFY_GROUPUSER"));

        transProfileContainer.put(new TransProfile(CREATE_ROLE, "CREATE_ROLE"));
        transProfileContainer.put(new TransProfile(GET_ROLE, "GET_ROLE"));
        transProfileContainer.put(new TransProfile(QUERY_ROLE, "QUERY_ROLE"));
        transProfileContainer.put(new TransProfile(QUERY_ROLE_BY_PAGE, "QUERY_ROLE_BY_PAGE"));
        transProfileContainer.put(new TransProfile(MODIFY_ROLE, "MODIFY_ROLE"));

        transProfileContainer.put(new TransProfile(CREATE_GROUP_PRIVILEGE, "CREATE_GROUP_PRIVILEGE"));
        transProfileContainer.put(new TransProfile(GET_GROUP_PRIVILEGE, "GET_GROUP_PRIVILEGE"));
        transProfileContainer.put(new TransProfile(QUERY_GROUP_PRIVILEGE, "QUERY_GROUP_PRIVILEGE"));
        transProfileContainer.put(new TransProfile(QUERY_GROUP_PRIVILEGE_BY_PAGE, "QUERY_GROUP_PRIVILEGE_BY_PAGE"));
        transProfileContainer.put(new TransProfile(MODIFY_GROUP_PRIVILEGE, "MODIFY_GROUP_PRIVILEGE"));

        transProfileContainer.put(new TransProfile(CREATE_PRIVILEGE_ROLE_RELATION, "CREATE_PRIVILEGE_ROLE_RELATION"));
        transProfileContainer.put(new TransProfile(GET_PRIVILEGE_ROLE_RELATION, "GET_PRIVILEGE_ROLE_RELATION"));
        transProfileContainer.put(new TransProfile(QUERY_PRIVILEGE_ROLE_RELATION, "QUERY_PRIVILEGE_ROLE_RELATION"));
        transProfileContainer.put(new TransProfile(QUERY_PRIVILEGE_ROLE_RELATION_BY_PAGE, "QUERY_PRIVILEGE_ROLE_RELATION_BY_PAGE"));
        transProfileContainer.put(new TransProfile(MODIFY_PRIVILEGE_ROLE_RELATION, "MODIFY_PRIVILEGE_ROLE_RELATION"));

        transProfileContainer.put(new TransProfile(CREATE_GROUP_PROFILE, "CREATE_GROUP_PROFILE"));
        transProfileContainer.put(new TransProfile(GET_GROUP_PROFILE, "GET_GROUP_PROFILE"));
        transProfileContainer.put(new TransProfile(QUERY_GROUP_PROFILE, "QUERY_GROUP_PROFILE"));
        transProfileContainer.put(new TransProfile(QUERY_GROUP_PROFILE_BY_PAGE, "QUERY_GROUP_PROFILE_BY_PAGE"));
        transProfileContainer.put(new TransProfile(MODIFY_GROUP_PROFILE, "MODIFY_GROUP_PROFILE"));

        transProfileContainer.put(new TransProfile(CREATE_GROUP_PROFILE_PRIVILEGE, "CREATE_GROUP_PROFILE_PRIVILEGE"));
        transProfileContainer.put(new TransProfile(GET_GROUP_PROFILE_PRIVILEGE, "GET_GROUP_PROFILE_PRIVILEGE"));
        transProfileContainer.put(new TransProfile(QUERY_GROUP_PROFILE_PRIVILEGE, "QUERY_GROUP_PROFILE_PRIVILEGE"));
        transProfileContainer.put(new TransProfile(QUERY_GROUP_PROFILE_PRIVILEGE_BY_PAGE, "QUERY_GROUP_PROFILE_PRIVILEGE_BY_PAGE"));
        transProfileContainer.put(new TransProfile(MODIFY_GROUP_PROFILE_PRIVILEGE, "MODIFY_GROUP_PROFILE_PRIVILEGE"));
        transProfileContainer.put(new TransProfile(GET_ROLE_BY_QUERYEXPRESS, "GET_ROLE_BY_QUERYEXPRESS"));
        transProfileContainer.put(new TransProfile(GET_GROUP_PROFILE_BY_QUERY, "GET_GROUP_PROFILE_BY_QUERY"));
        transProfileContainer.put(new TransProfile(QUERY_ROLE_BY_QUERY, "QUERY_ROLE_BY_QUERY"));

        transProfileContainer.put(new TransProfile(GAME_DB_CREATE, "GAME_DB_CREATE"));
        transProfileContainer.put(new TransProfile(GAME_DB_QUERY, "GAME_DB_QUERY"));
        transProfileContainer.put(new TransProfile(GAME_DB_GET, "GAME_DB_GET"));
        transProfileContainer.put(new TransProfile(GAME_DB_UPDATE, "GAME_DB_UPDATE"));


        transProfileContainer.put(new TransProfile(CREATE_GAME_PROPERTY_INFO, "CREATE_GAME_PROPERTY_INFO"));
        transProfileContainer.put(new TransProfile(QUERY_GAME_PROPERTY_INFO, "QUERY_GAME_PROPERTY_INFO"));
        transProfileContainer.put(new TransProfile(QUERY_GAME_PROPERTY_INFO_BY_PAGE, "QUERY_GAME_PROPERTY_INFO_BY_PAGE"));

        transProfileContainer.put(new TransProfile(GAME_DB_CHANNEL_CREATE, "GAME_DB_CHANNEL_CREATE"));
        transProfileContainer.put(new TransProfile(GAME_DB_CHANNEL_QUERY, "GAME_DB_CHANNEL_QUERY"));
        transProfileContainer.put(new TransProfile(GAME_DB_QUERY_PAGE, "GAME_DB_QUERY_PAGE"));
        transProfileContainer.put(new TransProfile(QUERY_GAMEDB_BY_SET_RETURN_MAP, "QUERY_GAMEDB_BY_SET_RETURN_MAP"));
        transProfileContainer.put(new TransProfile(INSERT_GAME_SUBSCRIBE, "INSERT_GAME_SUBSCRIBE"));
        transProfileContainer.put(new TransProfile(QUERY_GAME_SUBSCRIBE, "QUERY_GAME_SUBSCRIBE"));
        transProfileContainer.put(new TransProfile(GET_GAME_SUBSCRIBE, "GET_GAME_SUBSCRIBE"));
        transProfileContainer.put(new TransProfile(UPDATE_GAME_SUBSCRIBE, "UPDATE_GAME_SUBSCRIBE"));
        transProfileContainer.put(new TransProfile(GAME_DB_CATEGORY, "GAME_DB_CATEGORY"));

        transProfileContainer.put(new TransProfile(GAME_DB_COUNT, "GAME_DB_COUNT"));

        transProfileContainer.put(new TransProfile(GAME_DB_RELATION_CREATE, "GAME_DB_RELATION_CREATE"));
        transProfileContainer.put(new TransProfile(GAME_DB_RELATION_QUERY, "GAME_DB_RELATION_QUERY"));
        transProfileContainer.put(new TransProfile(GAME_DB_RELATION_UPDATE, "GAME_DB_RELATION_UPDATE"));
        transProfileContainer.put(new TransProfile(GAME_DB_RELATION_DELETE, "GAME_DB_RELATION_DELETE"));
        transProfileContainer.put(new TransProfile(GAME_DB_RELATION_GET, "GAME_DB_RELATION_GET"));
        transProfileContainer.put(new TransProfile(GAME_DB_BY_CACHE, "GAME_DB_BY_CACHE"));
        transProfileContainer.put(new TransProfile(GAME_DBCHANNEL_BY_CODE, "GAME_DBCHANNEL_BY_CODE"));

        transProfileContainer.put(new TransProfile(GAME_BRAND_CREATE, "GAME_BRAND_CREATE"));
        transProfileContainer.put(new TransProfile(GAME_BRAND_QUERY, "GAME_BRAND_QUERY"));
        transProfileContainer.put(new TransProfile(PUT_GAME_COLLECTION_LIST_CACHE, "PUT_GAME_COLLECTION_LIST_CACHE"));
        transProfileContainer.put(new TransProfile(GET_GAME_COLLECTION_LIST_CACHE, "GET_GAME_COLLECTION_LIST_CACHE"));
        transProfileContainer.put(new TransProfile(REMOVE_GAME_COLLECTION_LIST_CACHE, "REMOVE_GAME_COLLECTION_LIST_CACHE"));
        transProfileContainer.put(new TransProfile(GET_GAME_DB_BY_ANOTHER_NAME, "GET_GAME_DB_BY_ANOTHER_NAME"));
        transProfileContainer.put(new TransProfile(QUERY_GAME_ARCHIVES_BY_CACHE, "QUERY_GAME_ARCHIVES_BY_CACHE"));
        transProfileContainer.put(new TransProfile(PUT_GAME_ARCHIVES_BY_CACHE, "PUT_GAME_ARCHIVES_BY_CACHE"));
        transProfileContainer.put(new TransProfile(REMOVE_GAME_ARCHIVES_BY_CACHE, "REMOVE_GAME_ARCHIVES_BY_CACHE"));
        transProfileContainer.put(new TransProfile(GET_USER_LIKE_GAME, "GET_USER_LIKE_GAME"));
        transProfileContainer.put(new TransProfile(INC_USER_LIKE_GAME, "INC_USER_LIKE_GAME"));
        transProfileContainer.put(new TransProfile(GAME_BRAND_MODIFY, "GAME_BRAND_MODIFY"));
        transProfileContainer.put(new TransProfile(INCR_GAME_COLLECTION_LIST_CACHE, "INCR_GAME_COLLECTION_LIST_CACHE"));

        transProfileContainer.put(new TransProfile(GET_GAME_ORDERED, "GET_GAME_ORDERED"));
        transProfileContainer.put(new TransProfile(CREATE_GAME_ORDERED, "CREATE_GAME_ORDERED"));
        
        transProfileContainer.put(new TransProfile(GET_GAME_FILTER_GROUP, "GET_GAME_FILTER_GROUP"));
        transProfileContainer.put(new TransProfile(UPDATE_GAME_FILTER_GROUP, "UPDATE_GAME_FILTER_GROUP"));
        transProfileContainer.put(new TransProfile(INSERT_GAME_SHARE_NUM, "INSERT_GAME_SHARE_NUM"));
        transProfileContainer.put(new TransProfile(GET_GAME_SHARE_NUM, "INSERT_GAME_SHARE_NUM"));
    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }

}
