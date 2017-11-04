/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="erickliu@enjoyfound.com">ericliu</a>
 * Create time: 11-8-31 上午11:16
 * Description:
 */
public class GamePropertyInfoField extends AbstractObjectField {

    public static final GamePropertyInfoField PRIMARY_KEY = new GamePropertyInfoField("primary_id", ObjectFieldDBType.LONG, true, true);
    public static final GamePropertyInfoField KEY_ID = new GamePropertyInfoField("key_id", ObjectFieldDBType.LONG, true, false);
    public static final GamePropertyInfoField KEY_NAME = new GamePropertyInfoField("key_name", ObjectFieldDBType.STRING, true, false);
    public static final GamePropertyInfoField INT_VALUE = new GamePropertyInfoField("int_value", ObjectFieldDBType.INT, true, false);
    public static final GamePropertyInfoField STRING_VALUE = new GamePropertyInfoField("string_value", ObjectFieldDBType.STRING, true, false);
    public static final GamePropertyInfoField BOOLEAN_VALUE = new GamePropertyInfoField("boolean_value", ObjectFieldDBType.BOOLEAN, true, false);
    public static final GamePropertyInfoField DATE_VALUE = new GamePropertyInfoField("date_value", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final GamePropertyInfoField VALUE_TYPE = new GamePropertyInfoField("value_type", ObjectFieldDBType.INT, true, false);
    public static final GamePropertyInfoField CREATE_DATE = new GamePropertyInfoField("create_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final GamePropertyInfoField CREATE_ID = new GamePropertyInfoField("create_id", ObjectFieldDBType.STRING, true, false);
    public static final GamePropertyInfoField CREATE_IP = new GamePropertyInfoField("create_ip", ObjectFieldDBType.STRING, true, false);
    public static final GamePropertyInfoField LAST_MODIFY_DATE = new GamePropertyInfoField("last_modify_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final GamePropertyInfoField LAST_MODIFY_ID = new GamePropertyInfoField("last_modify_id", ObjectFieldDBType.STRING, true, false);
    public static final GamePropertyInfoField LAST_MODIFY_IP = new GamePropertyInfoField("last_modify_id", ObjectFieldDBType.STRING, true, false);

    //
    public GamePropertyInfoField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public GamePropertyInfoField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
