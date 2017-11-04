package com.enjoyf.platform.service.message;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-1
 * Time: 下午6:51
 * To change this template use File | Settings | File Templates.
 */
public class PushMessageDeviceField extends AbstractObjectField {

    public static final PushMessageDeviceField DEVICEID = new PushMessageDeviceField("deviceid", ObjectFieldDBType.LONG, false, true);
    public static final PushMessageDeviceField CLIENTID = new PushMessageDeviceField("clientid", ObjectFieldDBType.STRING, false, false);
    public static final PushMessageDeviceField APPKEY = new PushMessageDeviceField("appkey", ObjectFieldDBType.STRING, false, false);

    public static final PushMessageDeviceField DEVICETOKEN = new PushMessageDeviceField("devicetoken", ObjectFieldDBType.STRING, false, false);
    public static final PushMessageDeviceField PLATFORM = new PushMessageDeviceField("platform", ObjectFieldDBType.STRING, false, false);
    public static final PushMessageDeviceField LASTMSGID = new PushMessageDeviceField("lastmsgid", ObjectFieldDBType.LONG, false, false);
    public static final PushMessageDeviceField PUBLISHSTATUS = new PushMessageDeviceField("publishstatus", ObjectFieldDBType.STRING, false, false);
    public static final PushMessageDeviceField VALIDSTATUS = new PushMessageDeviceField("validstatus", ObjectFieldDBType.STRING, false, false);

    //
    public PushMessageDeviceField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public PushMessageDeviceField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
