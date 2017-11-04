package com.enjoyf.platform.service.joymeappconfig;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-7-24
 * Time: 下午4:59
 * To change this template use File | Settings | File Templates.
 */
public class AppChannelField extends AbstractObjectField{

    public static final AppChannelField APP_CHANNEL_ID = new AppChannelField("app_channel_id", ObjectFieldDBType.LONG, true, true);
    public static final AppChannelField APP_CHANNEL_CODE = new AppChannelField("app_channel_code", ObjectFieldDBType.STRING, true, false);
    public static final AppChannelField APP_CHANNEL_NAME = new AppChannelField("app_channel_name", ObjectFieldDBType.STRING, true, false);

    public AppChannelField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AppChannelField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
       @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
