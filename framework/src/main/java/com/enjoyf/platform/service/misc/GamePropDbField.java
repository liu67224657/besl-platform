package com.enjoyf.platform.service.misc;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-14
 * Time: 下午5:12
 * To change this template use File | Settings | File Templates.
 */
public class GamePropDbField extends AbstractObjectField {
    public static final GamePropDbField ID = new GamePropDbField("id", ObjectFieldDBType.INT, false, true);
    public static final GamePropDbField KEY_ID = new GamePropDbField("key_id", ObjectFieldDBType.LONG, true, false);
    public static final GamePropDbField TYPE = new GamePropDbField("type", ObjectFieldDBType.INT, false, true);
    public static final GamePropDbField KEY_NAME = new GamePropDbField("key_name", ObjectFieldDBType.STRING, true, false);

    public static final GamePropDbField STRING_VALUE = new GamePropDbField("string_value", ObjectFieldDBType.STRING, false, true);
    public static final GamePropDbField NUM_VALUE = new GamePropDbField("num_value", ObjectFieldDBType.FLOAT, true, false);

    public static final GamePropDbField DATE_VALUE = new GamePropDbField("date_value", ObjectFieldDBType.TIMESTAMP, false, true);

    public static final GamePropDbField VALUE_TYPE = new GamePropDbField("value_type", ObjectFieldDBType.INT, false, true);

    //
    public GamePropDbField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public GamePropDbField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
