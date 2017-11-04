package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-4
 * Time: 下午12:50
 * To change this template use File | Settings | File Templates.
 */
public class ClientLineField extends AbstractObjectField {

    public static final ClientLineField LINE_ID = new ClientLineField("line_id", ObjectFieldDBType.LONG, true, true);
    public static final ClientLineField CODE = new ClientLineField("code", ObjectFieldDBType.STRING, true, false);
    public static final ClientLineField LINE_NAME = new ClientLineField("line_name", ObjectFieldDBType.STRING, true, false);

    public static final ClientLineField ITEM_TYPE = new ClientLineField("item_type", ObjectFieldDBType.INT, true, false);
    public static final ClientLineField CREATE_DATE = new ClientLineField("create_date", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final ClientLineField CREATE_USERID = new ClientLineField("create_userid", ObjectFieldDBType.STRING, true, false);

    public static final ClientLineField MODIFY_USERID = new ClientLineField("modify_userid", ObjectFieldDBType.STRING, true, false);
    public static final ClientLineField MODIFY_DATE = new ClientLineField("modify_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ClientLineField VALID_STATUS = new ClientLineField("valid_status", ObjectFieldDBType.STRING, true, false);
    public static final ClientLineField LINE_TYPE = new ClientLineField("line_type", ObjectFieldDBType.INT, true, false);

    public static final ClientLineField PLATFORM = new ClientLineField("platform", ObjectFieldDBType.INT, true, false);

    public static final ClientLineField ANGULAR = new ClientLineField("angular", ObjectFieldDBType.INT, true, false);
    public static final ClientLineField BIGPIC = new ClientLineField("bigpic", ObjectFieldDBType.STRING, true, false);
    public static final ClientLineField SMALLPIC = new ClientLineField("smallpic", ObjectFieldDBType.STRING, true, false);
    public static final ClientLineField LINE_DESC = new ClientLineField("line_desc", ObjectFieldDBType.STRING, true, false);
    public static final ClientLineField DISPLAY_ORDER = new ClientLineField("display_order", ObjectFieldDBType.INT, true, false);
    public static final ClientLineField HOT = new ClientLineField("is_hot", ObjectFieldDBType.INT, true, false);
    public static final ClientLineField LINE_INTRO = new ClientLineField("line_intro", ObjectFieldDBType.STRING, true, false);
    public static final ClientLineField SHARE_PIC = new ClientLineField("share_pic", ObjectFieldDBType.STRING, true, false);

    //
    public ClientLineField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ClientLineField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
