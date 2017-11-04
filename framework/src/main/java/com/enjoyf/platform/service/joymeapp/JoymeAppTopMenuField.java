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
public class JoymeAppTopMenuField extends AbstractObjectField {

    public static final JoymeAppTopMenuField TOP_MENU_ID = new JoymeAppTopMenuField("top_menu_id", ObjectFieldDBType.LONG, true, true);
    public static final JoymeAppTopMenuField APPKEY = new JoymeAppTopMenuField("appkey", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppTopMenuField PIC_URL = new JoymeAppTopMenuField("pic_url", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppTopMenuField PIC_URL2 = new JoymeAppTopMenuField("pic_url2", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppTopMenuField MENU_NAME = new JoymeAppTopMenuField("menu_name", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppTopMenuField LINK_URL = new JoymeAppTopMenuField("link_url", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppTopMenuField MENU_TYPE = new JoymeAppTopMenuField("menu_type", ObjectFieldDBType.INT, true, false);
    public static final JoymeAppTopMenuField MENU_DESC = new JoymeAppTopMenuField("menu_desc", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppTopMenuField MENU_ISNEW = new JoymeAppTopMenuField("menu_isnew", ObjectFieldDBType.BOOLEAN, true, false);
    public static final JoymeAppTopMenuField MENU_ISHOT = new JoymeAppTopMenuField("menu_ishot", ObjectFieldDBType.BOOLEAN, true, false);
    public static final JoymeAppTopMenuField DISPLAY_ORDER = new JoymeAppTopMenuField("display_order", ObjectFieldDBType.INT, true, false);
    public static final JoymeAppTopMenuField CREATE_USERID = new JoymeAppTopMenuField("create_userid", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppTopMenuField CREATE_IP = new JoymeAppTopMenuField("create_ip", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppTopMenuField CREATE_DATE = new JoymeAppTopMenuField("createdate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final JoymeAppTopMenuField LASTMODIFY_USERID = new JoymeAppTopMenuField("lastmodify_userid", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppTopMenuField LASTMODIFY_IP = new JoymeAppTopMenuField("lastmodify_ip", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppTopMenuField LASTMODIFY_DATE = new JoymeAppTopMenuField("lastmodifydate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final JoymeAppTopMenuField REMOVESTATUS = new JoymeAppTopMenuField("removestatus", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppTopMenuField DISPLAYORDER = new JoymeAppTopMenuField("display_order", ObjectFieldDBType.INT, true, false);
    public static final JoymeAppTopMenuField CHANNEL_TOPMENU = new JoymeAppTopMenuField("channel_topmenu", ObjectFieldDBType.STRING, true, false);
    public static final JoymeAppTopMenuField GAMEID = new JoymeAppTopMenuField("gameid", ObjectFieldDBType.STRING, true, false);


    //
    public JoymeAppTopMenuField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public JoymeAppTopMenuField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
