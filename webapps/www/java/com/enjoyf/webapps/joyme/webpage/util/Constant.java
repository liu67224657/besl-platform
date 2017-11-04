/**
 *
 */
package com.enjoyf.webapps.joyme.webpage.util;

import com.enjoyf.platform.webapps.common.util.WebUtil;

/**
 * Action 常量类
 *
 * @author xinzhao
 */
public class Constant {
    /**
     * session 中用户信息key
     */
    public final static String SESSION_USER_INFO = "ssuserinfo";
    public static final String SESSION_USER_INFO_UGCWIKI = "userinfougcwiki";

    public final static String SESSION_USERCENTER="susercenter";
    public final static String SESSION_USERCENTER_UGCWIKI="usercenterugcwiki";

    public final static String SESSION_REGHT_MENU_FLAG = "rmflag";
    public final static String SESSION_HEARDER_MENU_FLAG = "hdflag";

    //用户信息sessionkey value 1为开 0为关闭
    public final static String SESSION_KEY_USER_INFOSTATUS ="infostatus";
    public final static String SESSION_KEY_USER_INFOREBACKURL ="rebackurl";

    public final static String COOKIE_KEY_CURRENT_MENU_1 ="currentMenu1";
    public final static String COOKIE_KEY_CURRENT_MENU_2 ="currentMenu2";

    public final static String KEY_USER_INFO_STATUS_COMPLETE="Y";

    public final static String SESSION_SESSION_ST = "sessionst";
    public final static String REQUES_KEY_PARAM_REFER_CODE = "rfcode";
    public final static String SESSION_SEND_INTRAV = "sessionsi";
     public final static String SESSION_MOBILE_CODE= "session_mcode";

    /**
     * 用户关注类型的三中状态 e互相关注 t已关注 f未关注
     */
    public static final String FLAG_FOCUSTYPE_EACHOTHER = "e";
    public static final String FLAG_FOCUSTYPE_FOCUS = "t";
    public static final String FLAG_FOCUSTYPE_NONE_FOCUS = "f";
    public static final String FLAG_FOCUSTYPE_FANS = "fan";

    public static final Integer USER_PWD_FORGOT_MAX = 24;//24小时

    public static final long USER_EMAIL_AUTH_PROIED = 2L * 60L * 60L * 1000L;
    public static final long USER_EMAIL_AUTH_TIMEOUT = 24L * 60L * 60L * 1000L;

    public static final int GAME_INVITE_SID_LENTH=5;

    /**
     * cookie 成功状态
     */
    public final static String KEY_COOKIE_LOGIN_SUCCESS = "~en~joy~success";



    /*------------- 读取配置文件web.properties 中常量 -----------------------*/

    /**
     * 主站域名
     */
    public static final String URL_WWW = WebUtil.getUrlWww();
    /**
     * 网站用户资源域名.例如：图片地址的域名
     */
    public static final String DOMAIN = WebUtil.getDomain();



}
