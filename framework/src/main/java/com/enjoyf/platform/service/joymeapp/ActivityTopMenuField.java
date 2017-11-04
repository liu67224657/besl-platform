package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-9-17
 * Time: 上午11:58
 * To change this template use File | Settings | File Templates.
 */
public class ActivityTopMenuField extends AbstractObjectField {

    public static final ActivityTopMenuField ACTIVITY_MENU_ID = new ActivityTopMenuField("activity_menu_id", ObjectFieldDBType.LONG, true, true);
    public static final ActivityTopMenuField APP_KEY = new ActivityTopMenuField("appkey", ObjectFieldDBType.STRING, true, false);
    public static final ActivityTopMenuField CHANNEL_ID = new ActivityTopMenuField("channel_id", ObjectFieldDBType.LONG, true, false);
    public static final ActivityTopMenuField PLATFORM = new ActivityTopMenuField("platform", ObjectFieldDBType.INT, true, false);
    public static final ActivityTopMenuField PIC_URL = new ActivityTopMenuField("pic_url", ObjectFieldDBType.STRING, true, false);
    public static final ActivityTopMenuField MENU_NAME = new ActivityTopMenuField("menu_name", ObjectFieldDBType.STRING, true, false);
    public static final ActivityTopMenuField LINK_URL = new ActivityTopMenuField("link_url", ObjectFieldDBType.STRING, true, false);
    public static final ActivityTopMenuField MENU_TYPE = new ActivityTopMenuField("menu_type", ObjectFieldDBType.INT, true, false);
    public static final ActivityTopMenuField MENU_DESC = new ActivityTopMenuField("menu_desc", ObjectFieldDBType.STRING, true, false);
    public static final ActivityTopMenuField IS_NEW = new ActivityTopMenuField("is_new", ObjectFieldDBType.BOOLEAN, true, false);
    public static final ActivityTopMenuField IS_HOT = new ActivityTopMenuField("is_hot", ObjectFieldDBType.BOOLEAN, true, false);
    public static final ActivityTopMenuField DISPLAY_ORDER = new ActivityTopMenuField("display_order", ObjectFieldDBType.INT, true, false);
    public static final ActivityTopMenuField LAST_MODIFY_USERID = new ActivityTopMenuField("lastmodify_userid", ObjectFieldDBType.STRING, true, false);
    public static final ActivityTopMenuField LAST_MODIFY_DATE = new ActivityTopMenuField("lastmodify_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ActivityTopMenuField LAST_MODIFY_IP = new ActivityTopMenuField("lastmodify_ip", ObjectFieldDBType.STRING, true, false);
    public static final ActivityTopMenuField VALID_STATUS = new ActivityTopMenuField("valid_status", ObjectFieldDBType.STRING, true, false);
    public static final ActivityTopMenuField CATEGORY = new ActivityTopMenuField("category", ObjectFieldDBType.INT, true, false);
    public static final ActivityTopMenuField REDIRECT_TYPE = new ActivityTopMenuField("redirect", ObjectFieldDBType.INT, true, false);
    public static final ActivityTopMenuField PARAM_TEXT = new ActivityTopMenuField("param_text", ObjectFieldDBType.STRING, true, false);


    public ActivityTopMenuField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ActivityTopMenuField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
