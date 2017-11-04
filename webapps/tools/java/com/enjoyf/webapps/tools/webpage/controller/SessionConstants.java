package com.enjoyf.webapps.tools.webpage.controller;

/**
 * Author: zhaoxin
 * Date: 11-11-24
 * Time: 下午3:17
 * Desc:
 */
public class SessionConstants {

    /**
     * 菜单Map 存放到session中的key值 用于在leftMenu.jsp 中使用
     */
    public final static String LEFT_ROOT_MENU_TREE = "leftRootMenuMap";

    /**
     * 一级菜单Map 存放到session中的key值
     */
    public final static String TOP_ROOT_MENU_TREE = "topRootMenu";

    /**
     * 系统资源Map 存放到session中的key值
     */
    public final static String LIST_RESOURCE = "listResource";

    /**
     * 用户登陆后，存放用户的全部权限 编号
     */
    public final static String USER_SESSION_PRIVILEGE_ID = "mapUserSessionPriviegeId";

    /**
     * 用户SESSION 中的用户DTO
     */
    public static final String CURRENT_USER = "current_user";

      public static final String UPLOAD_TOKEN = "upload_token";

    public static String KEY_ACCESS_TOKEN="at";
}
