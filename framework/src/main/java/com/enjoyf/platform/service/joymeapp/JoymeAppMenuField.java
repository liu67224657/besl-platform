package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-1
 * Time: 下午6:51
 * To change this template use File | Settings | File Templates.
 */
public class JoymeAppMenuField extends AbstractObjectField {

    public static final JoymeAppMenuField BUTTOM_MENU_ID = new JoymeAppMenuField("buttom_menu_id", ObjectFieldDBType.LONG, true, true);

    public static final JoymeAppMenuField PID = new JoymeAppMenuField("pid", ObjectFieldDBType.LONG, true, false);
    public static final JoymeAppMenuField APPKEY = new JoymeAppMenuField("appkey", ObjectFieldDBType.STRING, true, false);
    //todo
    public static final JoymeAppMenuField PIC_URL = new JoymeAppMenuField("pic_url", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppMenuField PIC = new JoymeAppMenuField("pic", ObjectFieldDBType.STRING, true, false);

    public static final JoymeAppMenuField MENU_NAME = new JoymeAppMenuField("menu_name", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppMenuField LINK_URL = new JoymeAppMenuField("link_url", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppMenuField MENU_TYPE = new JoymeAppMenuField("menu_type", ObjectFieldDBType.INT, true, false);
    public static final JoymeAppMenuField MENU_DESC = new JoymeAppMenuField("menu_desc", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppMenuField MENU_ISNEW = new JoymeAppMenuField("menu_isnew", ObjectFieldDBType.BOOLEAN, true, false);
    public static final JoymeAppMenuField MENU_ISHOT = new JoymeAppMenuField("menu_ishot", ObjectFieldDBType.BOOLEAN, true, false);
    public static final JoymeAppMenuField CREATE_USERID = new JoymeAppMenuField("create_userid", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppMenuField CREATEDATE = new JoymeAppMenuField("createdate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final JoymeAppMenuField CREATE_IP = new JoymeAppMenuField("create_ip", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppMenuField LASTMODIFY_USERID = new JoymeAppMenuField("lastmodify_userid", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppMenuField LASTMODIFY_IP = new JoymeAppMenuField("lastmodify_ip", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppMenuField LASTMODIFYDATE = new JoymeAppMenuField("lastmodifydate", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final JoymeAppMenuField REMOVESTATUS = new JoymeAppMenuField("removestatus", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppMenuField DISPLAY_ORDER = new JoymeAppMenuField("display_order", ObjectFieldDBType.INT, true, false);
    public static final JoymeAppMenuField TAG_ID = new JoymeAppMenuField("tag_id", ObjectFieldDBType.LONG, true, false);
    public static final JoymeAppMenuField DISPLAY_TYPE = new JoymeAppMenuField("display_type", ObjectFieldDBType.INT, true, false);

    public static final JoymeAppMenuField MODULE_TYPE = new JoymeAppMenuField("menucategory", ObjectFieldDBType.INT, true, false);
    public static final JoymeAppMenuField EXPFIELD = new JoymeAppMenuField("expfield", ObjectFieldDBType.STRING, true, false);

    //todo
    public static final JoymeAppMenuField STATUS_DESCRIPTION = new JoymeAppMenuField("statusdescription", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppMenuField RECOMMENDE_STAR = new JoymeAppMenuField("recomstar", ObjectFieldDBType.INT, true, false);
    public static final JoymeAppMenuField CONTENT_TYPE = new JoymeAppMenuField("contenttype", ObjectFieldDBType.INT, true, false);

    //
    public JoymeAppMenuField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public JoymeAppMenuField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
