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
public class ClientLineFlagField extends AbstractObjectField {
    public static final ClientLineFlagField FLAG_ID = new ClientLineFlagField("flag_id", ObjectFieldDBType.LONG, true, true);
    public static final ClientLineFlagField FLAG_DESC = new ClientLineFlagField("flag_desc", ObjectFieldDBType.STRING, true, true);
    public static final ClientLineFlagField LINE_ID = new ClientLineFlagField("line_id", ObjectFieldDBType.LONG, true, false);
    public static final ClientLineFlagField LINE_CODE = new ClientLineFlagField("line_code", ObjectFieldDBType.STRING, true, false);

    public static final ClientLineFlagField MAX_ITEM_ID = new ClientLineFlagField("max_item_id", ObjectFieldDBType.LONG, true, false);

    public static final ClientLineFlagField FLAG_TYPE = new ClientLineFlagField("flag_type", ObjectFieldDBType.INT, true, false);

    public static final ClientLineFlagField CREATE_DATE = new ClientLineFlagField("create_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ClientLineFlagField CREATE_USERID = new ClientLineFlagField("create_userid", ObjectFieldDBType.STRING, true, false);
    public static final ClientLineFlagField CREATE_IP = new ClientLineFlagField("create_ip", ObjectFieldDBType.STRING, true, false);

    public static final ClientLineFlagField MODIFY_USERID = new ClientLineFlagField("modify_userid", ObjectFieldDBType.STRING, true, false);
    public static final ClientLineFlagField MODIFY_DATE = new ClientLineFlagField("modify_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ClientLineFlagField MODIFY_IP = new ClientLineFlagField("modify_ip", ObjectFieldDBType.STRING, true, false);

    public static final ClientLineFlagField VALID_STATUS = new ClientLineFlagField("valid_status", ObjectFieldDBType.STRING, true, false);

    public ClientLineFlagField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ClientLineFlagField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
