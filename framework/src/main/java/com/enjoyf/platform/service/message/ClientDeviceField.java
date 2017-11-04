package com.enjoyf.platform.service.message;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-1
 * Time: 下午7:07
 * To change this template use File | Settings | File Templates.
 */
public class ClientDeviceField extends AbstractObjectField {
    public static final ClientDeviceField DEVICE_ID = new ClientDeviceField("device_id", ObjectFieldDBType.LONG, true, true);
    public static final ClientDeviceField UNO = new ClientDeviceField("uno", ObjectFieldDBType.STRING, true, false);
    public static final ClientDeviceField PLATFORM = new ClientDeviceField("platform", ObjectFieldDBType.INT, true, false);
    public static final ClientDeviceField CLIENT_ID = new ClientDeviceField("client_id", ObjectFieldDBType.STRING, true, false);
    public static final ClientDeviceField CLIENT_TOKEN = new ClientDeviceField("client_token", ObjectFieldDBType.STRING, true, false);
    public static final ClientDeviceField APP_ID = new ClientDeviceField("app_id", ObjectFieldDBType.STRING, true, false);
    public static final ClientDeviceField LAST_MSG_ID = new ClientDeviceField("last_msg_id", ObjectFieldDBType.LONG, true, false);
    public static final ClientDeviceField PUSH_CHANNEL = new ClientDeviceField("push_channel", ObjectFieldDBType.INT, true, false);
    public static final ClientDeviceField APP_VERSION = new ClientDeviceField("app_version", ObjectFieldDBType.STRING, true, false);
    public static final ClientDeviceField APP_CHANNEL = new ClientDeviceField("app_channel", ObjectFieldDBType.STRING, true, false);
    public static final ClientDeviceField TAGS = new ClientDeviceField("app_tags", ObjectFieldDBType.STRING, true, false);
    public static final ClientDeviceField ADVID = new ClientDeviceField("adv_id", ObjectFieldDBType.STRING, true, false);
    public static final ClientDeviceField IP = new ClientDeviceField("client_ip", ObjectFieldDBType.STRING, true, false);
	public static final ClientDeviceField ENTERPRISER_TYPE = new ClientDeviceField("enterpriser", ObjectFieldDBType.INT, true, false);
    public static final ClientDeviceField PROFILEID = new ClientDeviceField("profileid", ObjectFieldDBType.STRING, true, false);
    //

    public ClientDeviceField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ClientDeviceField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
