package com.enjoyf.platform.service.tools;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 上午9:37
 * Desc:
 */
public class ToolsConstants {
    public static final String SERVICE_SECTION = "toolsservice";
    public static final String SERVICE_PREFIX = "toolsserver";
    public static final String SERVICE_TYPE = "toolsserver";

    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();
    public static final Integer SPLIT_SIZE = 2000;

    public static final byte AUDIT_CONTENT_USER_CREATE = 1;
    public static final byte AUDIT_CONTENT_CREATE = 2;

    public static final byte AUDIT_CONTENT_USER_MODIFY = 3;
    public static final byte AUDIT_CONTENT_MODIFY = 4;

    public static final byte AUDIT_CONTENT_USER_QUERY_UNOS = 5;
    public static final byte AUDIT_CONTENT_QUERY_OBJS = 6;

    public static final byte EDITOR_CREATE = 30;
    public static final byte EDITOR_MODIFY = 31;
    public static final byte EDITOR_GET = 32;
    public static final byte EDITOR_QUERY_BY_PAGE = 33;
    public static final byte EDITOR_QUERY = 34;

    public static final byte STATS_EDITOR_ITEM_CREATE = 37;
    public static final byte STATS_EDITOR_ITEM_MODIFY = 38;
    public static final byte STATS_EDITOR_ITEM_GET = 39;
    public static final byte STATS_EDITOR_ITEM_QUERY_BY_PAGE = 40;
    public static final byte STATS_EDITOR_ITEM_QUERY = 41;

    //TOOLS系统中权限用户账号管理
    public static final byte PRIVILEGE_USER_FIND_BY_ID = 61;
    public static final byte PRIVILEGE_USER_FIND_BY_ID_PWD = 62;

    public static final byte PRIVILEGE_RES_FIND_BY_ID = 63;
    public static final byte PRIVILEGE_RES_QUERY_BY_STATUS = 64;

    public static final byte PRIVILEGE_USER_QUERY_BY_PARAM = 65;
    public static final byte PRIVILEGE_USER_MODIFY_BY_ENTITY = 66;
    public static final byte PRIVILEGE_USER_SAVE_BY_ENTITY = 67;
    public static final byte PRIVILEGE_USER_FIND_BY_UNO = 68;
    public static final byte PRIVILEGE_USER_SWITCH_BY_ENTITY = 69;
    public static final byte PRIVILEGE_USER_MODIFY_PWD_BY_ENTITY = 74;
    public static final byte PRIVILEGE_USER_DELETE_BY_ENTITY = 81;
    public static final byte PRIVILEGE_USER_ROLES_SAVE = 85;
    public static final byte PRIVILEGE_ROLES_SET_BY_STATUS = 86;
    public static final byte PRIVILEGE_USER_ROLES_DELETE = 87;

    public static final byte PRIVILEGE_ROLES_QUERY_BY_PARAM = 70;
    public static final byte PRIVILEGE_ROLES_FIND_BY_ROLENAME = 71;
    public static final byte PRIVILEGE_ROLES_FIND_BY_ROLEID = 100;
    public static final byte PRIVILEGE_ROLES_SAVE_BY_ENTITY = 72;
    public static final byte PRIVILEGE_ROLES_MODIFY_BY_ENTITY = 73;
    public static final byte PRIVILEGE_ROLES_DELETE_BY_ENTITY = 75;

    public static final byte PRIVILEGE_RES_QUERY_BY_PARAM = 76;
    public static final byte PRIVILEGE_RES_DELETE_BY_ENTITY = 77;
    public static final byte PRIVILEGE_RES_MODIFY_BY_ENTITY = 78;
    public static final byte PRIVILEGE_RES_SAVE_BY_ENTITY = 79;
    public static final byte PRIVILEGE_RES_FIND_BY_RSURL = 80;
    public static final byte PRIVILEGE_RES_FIND_BY_ENTITY = 82;
    public static final byte PRIVILEGE_RES_MENU_FIND_BY_ENTITY = 83;
    public static final byte PRIVILEGE_ROLES_AND_RES_SAVE = 84;
    public static final byte PRIVILEGE_RES_GET_BY_RSID = 101;


    // PRIVILEGE LOG
    public static final byte TOOLS_LOG_ADD = 91;
    public static final byte TOOLS_LOG_DEL = 92;
    public static final byte TOOLS_LOG_QUERY = 93;
    public static final byte TOOLS_LOG_QUE = 94;

    public static final byte TOOLS_LOG_LOGIN_ADD = 95;
    public static final byte TOOLS_LOG_LOGIN_DEL = 96;
    public static final byte TOOLS_LOG_LOGIN_QUERY = 97;
    public static final byte TOOLS_LOG_LOGIN_QUE = 98;


    //系统通用日志
    public static final byte TOOLS_SYS_COMMON_LOG = 99;


    static {
        transProfileContainer.put(new TransProfile(AUDIT_CONTENT_USER_CREATE, "AUDIT_CONTENT_USER_CREATE"));
        transProfileContainer.put(new TransProfile(AUDIT_CONTENT_CREATE, "AUDIT_CONTENT_CREATE"));
        transProfileContainer.put(new TransProfile(AUDIT_CONTENT_USER_MODIFY, "AUDIT_CONTENT_USER_MODIFY"));
        transProfileContainer.put(new TransProfile(AUDIT_CONTENT_MODIFY, "AUDIT_CONTENT_MODIFY"));
        transProfileContainer.put(new TransProfile(AUDIT_CONTENT_USER_QUERY_UNOS, "AUDIT_CONTENT_USER_QUERY_UNOS"));
        transProfileContainer.put(new TransProfile(AUDIT_CONTENT_QUERY_OBJS, "AUDIT_CONTENT_QUERY_OBJS"));

        //privilege
        transProfileContainer.put(new TransProfile(PRIVILEGE_USER_FIND_BY_ID, "PRIVILEGE_USER_FIND_BY_ID"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_USER_FIND_BY_ID_PWD, "PRIVILEGE_USER_FIND_BY_ID_PWD"));

        transProfileContainer.put(new TransProfile(PRIVILEGE_RES_FIND_BY_ID, "PRIVILEGE_RES_FIND_BY_ID"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_RES_QUERY_BY_STATUS, "PRIVILEGE_RES_QUERY_BY_STATUS"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_USER_QUERY_BY_PARAM, "PRIVILEGE_USER_QUERY_BY_PARAM"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_USER_MODIFY_BY_ENTITY, "PRIVILEGE_USER_MODIFY_BY_ENTITY"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_USER_MODIFY_PWD_BY_ENTITY, "PRIVILEGE_USER_MODIFY_PWD_BY_ENTITY"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_USER_SAVE_BY_ENTITY, "PRIVILEGE_USER_SAVE_BY_ENTITY"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_USER_FIND_BY_UNO, "PRIVILEGE_USER_FIND_BY_UNO"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_USER_DELETE_BY_ENTITY, "PRIVILEGE_USER_DELETE_BY_ENTITY"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_USER_SWITCH_BY_ENTITY, "PRIVILEGE_USER_SWITCH_BY_ENTITY"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_ROLES_QUERY_BY_PARAM, "PRIVILEGE_ROLES_QUERY_BY_PARAM"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_RES_QUERY_BY_PARAM, "PRIVILEGE_RES_QUERY_BY_PARAM"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_ROLES_FIND_BY_ROLENAME, "PRIVILEGE_ROLES_FIND_BY_ROLENAME"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_ROLES_SAVE_BY_ENTITY, "PRIVILEGE_ROLES_SAVE_BY_ENTITY"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_ROLES_DELETE_BY_ENTITY, "PRIVILEGE_ROLES_DELETE_BY_ENTITY"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_RES_DELETE_BY_ENTITY, "PRIVILEGE_RES_DELETE_BY_ENTITY"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_ROLES_MODIFY_BY_ENTITY, "PRIVILEGE_ROLES_MODIFY_BY_ENTITY"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_RES_MODIFY_BY_ENTITY, "PRIVILEGE_RES_MODIFY_BY_ENTITY"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_RES_SAVE_BY_ENTITY, "PRIVILEGE_RES_SAVE_BY_ENTITY"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_RES_FIND_BY_RSURL, "PRIVILEGE_RES_FIND_BY_RSURL"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_RES_FIND_BY_ENTITY, "PRIVILEGE_RES_FIND_BY_ENTITY"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_RES_MENU_FIND_BY_ENTITY, "PRIVILEGE_RES_MENU_FIND_BY_ENTITY"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_ROLES_AND_RES_SAVE, "PRIVILEGE_ROLES_AND_RES_SAVE"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_USER_ROLES_SAVE, "PRIVILEGE_USER_ROLES_SAVE"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_ROLES_SET_BY_STATUS, "PRIVILEGE_ROLES_SET_BY_STATUS"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_USER_ROLES_DELETE, "PRIVILEGE_USER_ROLES_DELETE"));

        transProfileContainer.put(new TransProfile(TOOLS_LOG_ADD, "TOOLS_LOG_ADD"));
        transProfileContainer.put(new TransProfile(TOOLS_LOG_DEL, "TOOLS_LOG_DEL"));
        transProfileContainer.put(new TransProfile(TOOLS_LOG_QUERY, "TOOLS_LOG_QUERY"));
        transProfileContainer.put(new TransProfile(TOOLS_LOG_QUE, "TOOLS_LOG_QUE"));
        transProfileContainer.put(new TransProfile(TOOLS_LOG_LOGIN_ADD, "TOOLS_LOG_LOGIN_ADD"));
        transProfileContainer.put(new TransProfile(TOOLS_LOG_LOGIN_DEL, "TOOLS_LOG_LOGIN_DEL"));
        transProfileContainer.put(new TransProfile(TOOLS_LOG_LOGIN_QUERY, "TOOLS_LOG_LOGIN_QUERY"));
        transProfileContainer.put(new TransProfile(TOOLS_LOG_LOGIN_QUE, "TOOLS_LOG_LOGIN_QUE"));
        transProfileContainer.put(new TransProfile(TOOLS_SYS_COMMON_LOG, "TOOLS_SYS_COMMON_LOG"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_ROLES_FIND_BY_ROLEID, "PRIVILEGE_ROLES_FIND_BY_ROLEID"));
        transProfileContainer.put(new TransProfile(PRIVILEGE_RES_GET_BY_RSID, "PRIVILEGE_RES_GET_BY_RSID"));

        transProfileContainer.put(new TransProfile(EDITOR_CREATE, "EDITOR_CREATE"));
        transProfileContainer.put(new TransProfile(EDITOR_MODIFY, "EDITOR_MODIFY"));
        transProfileContainer.put(new TransProfile(EDITOR_GET, "EDITOR_GET"));
        transProfileContainer.put(new TransProfile(EDITOR_QUERY_BY_PAGE, "EDITOR_QUERY_BY_PAGE"));
        transProfileContainer.put(new TransProfile(EDITOR_QUERY, "EDITOR_QUERY"));


        transProfileContainer.put(new TransProfile(STATS_EDITOR_ITEM_CREATE, "STATS_EDITOR_ITEM_CREATE"));
        transProfileContainer.put(new TransProfile(STATS_EDITOR_ITEM_MODIFY, "STATS_EDITOR_ITEM_MODIFY"));
        transProfileContainer.put(new TransProfile(STATS_EDITOR_ITEM_GET, "STATS_EDITOR_ITEM_GET"));
        transProfileContainer.put(new TransProfile(STATS_EDITOR_ITEM_QUERY_BY_PAGE, "STATS_EDITOR_ITEM_QUERY_BY_PAGE"));
        transProfileContainer.put(new TransProfile(STATS_EDITOR_ITEM_QUERY, "STATS_EDITOR_ITEM_QUERY"));
    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
