package com.enjoyf.platform.service.usercenter.activityuser;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/4/9
 * Description:
 */
public class ActivityUserLogField extends AbstractObjectField {
    //
    public static final ActivityUserLogField ACTIVITY_USER_LOG_ID = new ActivityUserLogField("activity_user_log_id", ObjectFieldDBType.LONG);
    public static final ActivityUserLogField APPKEY = new ActivityUserLogField("appkey", ObjectFieldDBType.STRING);
    public static final ActivityUserLogField SUBKEY = new ActivityUserLogField("subkey", ObjectFieldDBType.STRING);
    public static final ActivityUserLogField PROFILEID = new ActivityUserLogField("profileid", ObjectFieldDBType.STRING);
    public static final ActivityUserLogField UNO = new ActivityUserLogField("uno", ObjectFieldDBType.STRING);
    public static final ActivityUserLogField ACTION_TYPE = new ActivityUserLogField("action_type", ObjectFieldDBType.INT);
    public static final ActivityUserLogField OBJECT_TYPE = new ActivityUserLogField("object_type", ObjectFieldDBType.INT);
    public static final ActivityUserLogField OBJECT_ID = new ActivityUserLogField("object_id", ObjectFieldDBType.STRING);
    public static final ActivityUserLogField ACTION_TIME = new ActivityUserLogField("action_time", ObjectFieldDBType.TIMESTAMP);


    //

    public ActivityUserLogField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
