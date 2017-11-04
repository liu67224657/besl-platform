package com.enjoyf.platform.service.advertise.app;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  2014/6/9 14:45
 * Description:
 */
public class AppAdvertisePublishField extends AbstractObjectField {

    public static final AppAdvertisePublishField PUBLISH_ID = new AppAdvertisePublishField("publish_id", ObjectFieldDBType.LONG, false, true);
    public static final AppAdvertisePublishField PUBLISH_NAME = new AppAdvertisePublishField("publish_name", ObjectFieldDBType.STRING, true, false);
    public static final AppAdvertisePublishField PUBLISH_DESC = new AppAdvertisePublishField("publish_desc", ObjectFieldDBType.STRING, true, false);
    public static final AppAdvertisePublishField ADVERTISE_ID = new AppAdvertisePublishField("advertise_id", ObjectFieldDBType.LONG, false, false);
    public static final AppAdvertisePublishField START_TIME = new AppAdvertisePublishField("start_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final AppAdvertisePublishField END_TIME = new AppAdvertisePublishField("end_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final AppAdvertisePublishField CREATE_TIME = new AppAdvertisePublishField("create_time", ObjectFieldDBType.TIMESTAMP, false, false);
    public static final AppAdvertisePublishField CREATE_USER = new AppAdvertisePublishField("create_user", ObjectFieldDBType.LONG, true, false);
    public static final AppAdvertisePublishField CREATE_IP = new AppAdvertisePublishField("create_ip", ObjectFieldDBType.STRING, false, false);
    public static final AppAdvertisePublishField PUBLISH_TYPE = new AppAdvertisePublishField("publish_type", ObjectFieldDBType.INT, true, false);
    public static final AppAdvertisePublishField PUBLISH_PARAM = new AppAdvertisePublishField("publish_param", ObjectFieldDBType.STRING, true, false);
    public static final AppAdvertisePublishField REMOVE_STATUS = new AppAdvertisePublishField("remove_status", ObjectFieldDBType.STRING, false, false);
    public static final AppAdvertisePublishField APP_KEY = new AppAdvertisePublishField("appkey", ObjectFieldDBType.STRING, false, true);
    public static final AppAdvertisePublishField CHANNEL = new AppAdvertisePublishField("channel", ObjectFieldDBType.STRING, true, false);

    public AppAdvertisePublishField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AppAdvertisePublishField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

}
