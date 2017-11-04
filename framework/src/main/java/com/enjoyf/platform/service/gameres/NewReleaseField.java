package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-21
 * Time: 下午5:06
 * To change this template use File | Settings | File Templates.
 */
public class NewReleaseField extends AbstractObjectField{

    public static final NewReleaseField NEW_RELEASE_ID = new NewReleaseField("new_game_info_id", ObjectFieldDBType.LONG, true, true);
    public static final NewReleaseField NEW_GAME_NAME = new NewReleaseField("game_name", ObjectFieldDBType.STRING, true, false);
    public static final NewReleaseField DISPLAY_ORDER = new NewReleaseField("display_order", ObjectFieldDBType.INT, true, false);
    public static final NewReleaseField VALID_STATUS = new NewReleaseField("valid_status", ObjectFieldDBType.STRING, true, false);
    public static final NewReleaseField COMPANY_NAME = new NewReleaseField("company_name", ObjectFieldDBType.STRING, true, false);
    public static final NewReleaseField COOPRATE_TYPE = new NewReleaseField("cooprate_type", ObjectFieldDBType.INT, true, false);
    public static final NewReleaseField PUBLISH_DATE = new NewReleaseField("pub_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final NewReleaseField CREATE_DATE = new NewReleaseField("create_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final NewReleaseField VERIFY_DATE = new NewReleaseField("verify_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final NewReleaseField FOCUS_NUM = new NewReleaseField("focus_num", ObjectFieldDBType.INT, true, false);
    public static final NewReleaseField CREATE_UNO = new NewReleaseField("create_uno", ObjectFieldDBType.STRING, true, false);
    public static final NewReleaseField SHARE_ID = new NewReleaseField("share_id", ObjectFieldDBType.LONG, true, false);
    public static final NewReleaseField NEW_GAME_DESC = new NewReleaseField("game_desc", ObjectFieldDBType.STRING, true, false);
    public static final NewReleaseField NEW_GAME_ICON = new NewReleaseField("game_icon", ObjectFieldDBType.STRING, true, false);
    public static final NewReleaseField CONTACTS = new NewReleaseField("contacts", ObjectFieldDBType.STRING, true, false);
    public static final NewReleaseField EMAIL = new NewReleaseField("email", ObjectFieldDBType.STRING, true, false);
    public static final NewReleaseField NEW_GAME_PIC_SET = new NewReleaseField("game_pics", ObjectFieldDBType.STRING, true, false);
    public static final NewReleaseField QQ = new NewReleaseField("qq", ObjectFieldDBType.STRING, true, false);
    public static final NewReleaseField PHONE = new NewReleaseField("phone", ObjectFieldDBType.STRING, true, false);
    public static final NewReleaseField PEOPLE_NUM_TYPE = new NewReleaseField("people_num", ObjectFieldDBType.INT, true, false);
    public static final NewReleaseField PUBLISH_AREA = new NewReleaseField("pub_area", ObjectFieldDBType.INT, true, false);
    public static final NewReleaseField LAST_MODIFY_DATE = new NewReleaseField("last_modify_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final NewReleaseField LAST_MODIFY_IP = new NewReleaseField("last_modify_ip", ObjectFieldDBType.STRING, true, false);
    public static final NewReleaseField LAST_MODIFY_TYPE = new NewReleaseField("last_modify_type", ObjectFieldDBType.INT, true, false);
    public static final NewReleaseField NOTICE =new NewReleaseField("notice", ObjectFieldDBType.STRING, true, false);

    public NewReleaseField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public NewReleaseField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
