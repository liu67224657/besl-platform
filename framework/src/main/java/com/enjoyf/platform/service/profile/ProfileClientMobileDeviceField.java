package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-1
 * Time: 下午7:07
 * To change this template use File | Settings | File Templates.
 */
public class ProfileClientMobileDeviceField extends AbstractObjectField {
    public static final ProfileClientMobileDeviceField DEVICE_ID = new ProfileClientMobileDeviceField("device_id", ObjectFieldDBType.LONG, true, true);
    public static final ProfileClientMobileDeviceField UNO = new ProfileClientMobileDeviceField("uno", ObjectFieldDBType.STRING, true, false);
    public static final ProfileClientMobileDeviceField PLATFORM = new ProfileClientMobileDeviceField("platform", ObjectFieldDBType.INT, true, false);
    public static final ProfileClientMobileDeviceField CLIENT_ID = new ProfileClientMobileDeviceField("client_id", ObjectFieldDBType.STRING, true, false);
    public static final ProfileClientMobileDeviceField CLIENT_TOKEN = new ProfileClientMobileDeviceField("client_token", ObjectFieldDBType.STRING, true, false);
    public static final ProfileClientMobileDeviceField APP_ID = new ProfileClientMobileDeviceField("app_id", ObjectFieldDBType.STRING, true, false);
    public static final ProfileClientMobileDeviceField LAST_MSG_ID = new ProfileClientMobileDeviceField("last_msg_id", ObjectFieldDBType.LONG, true, false);
    //

    public ProfileClientMobileDeviceField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ProfileClientMobileDeviceField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
