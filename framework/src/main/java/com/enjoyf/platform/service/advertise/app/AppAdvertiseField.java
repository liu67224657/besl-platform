package com.enjoyf.platform.service.advertise.app;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  2014/6/9 14:45
 * Description:
 */
public class AppAdvertiseField extends AbstractObjectField {

    public static final AppAdvertiseField ADVERTISE_ID = new AppAdvertiseField("advertise_id", ObjectFieldDBType.LONG, false, true);
    public static final AppAdvertiseField ADVERTISE_NAME = new AppAdvertiseField("advertise_name", ObjectFieldDBType.STRING, true, false);
    public static final AppAdvertiseField ADVERTISE_DESC = new AppAdvertiseField("advertise_desc", ObjectFieldDBType.STRING, true, false);
    public static final AppAdvertiseField ADVERTISE_URL = new AppAdvertiseField("advertise_url", ObjectFieldDBType.STRING, true, false);
    public static final AppAdvertiseField ADVERTISE_PICURL1 = new AppAdvertiseField("advertise_picurl1", ObjectFieldDBType.STRING, true, false);
    public static final AppAdvertiseField ADVERTISE_PICURL2 = new AppAdvertiseField("advertise_picurl2", ObjectFieldDBType.STRING, true, false);
    public static final AppAdvertiseField ADVERTISE_PLATFORM = new AppAdvertiseField("advertise_platform", ObjectFieldDBType.INT, true, false);
    public static final AppAdvertiseField CREATE_TIME = new AppAdvertiseField("create_time", ObjectFieldDBType.TIMESTAMP, false, false);
    public static final AppAdvertiseField CREATE_USER = new AppAdvertiseField("create_user", ObjectFieldDBType.STRING, false, false);
    public static final AppAdvertiseField CREATE_IP = new AppAdvertiseField("create_ip", ObjectFieldDBType.STRING, false, false);
    public static final AppAdvertiseField REMOVE_STATUS = new AppAdvertiseField("remove_status", ObjectFieldDBType.STRING, false, false);
    public static final AppAdvertiseField REDIRECT_TYPE = new AppAdvertiseField("redirect_type", ObjectFieldDBType.INT, false, false);


    public static final AppAdvertiseField ADVERTISE_TYPE = new AppAdvertiseField("advertise_type", ObjectFieldDBType.INT, false, false);
    public static final AppAdvertiseField EXTSTRING = new AppAdvertiseField("extstring", ObjectFieldDBType.STRING, false, false);

    public AppAdvertiseField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AppAdvertiseField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

}
