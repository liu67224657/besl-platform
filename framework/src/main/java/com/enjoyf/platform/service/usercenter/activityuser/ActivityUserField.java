package com.enjoyf.platform.service.usercenter.activityuser;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/4/9
 * Description:
 */
public class ActivityUserField extends AbstractObjectField {
    //
    public static final ActivityUserField ACTIVITY_USER_ID = new ActivityUserField("activity_user_id", ObjectFieldDBType.STRING);
    public static final ActivityUserField APPKEY = new ActivityUserField("appkey", ObjectFieldDBType.STRING);
    public static final ActivityUserField SUBKEY = new ActivityUserField("subkey", ObjectFieldDBType.STRING);
    public static final ActivityUserField PROFILEID = new ActivityUserField("profileid", ObjectFieldDBType.STRING);
    public static final ActivityUserField UNO = new ActivityUserField("uno", ObjectFieldDBType.STRING);
    public static final ActivityUserField ACTION_TYPE = new ActivityUserField("action_type", ObjectFieldDBType.INT);
    public static final ActivityUserField OBJECT_TYPE = new ActivityUserField("object_type", ObjectFieldDBType.INT);
    public static final ActivityUserField OBJECT_ID = new ActivityUserField("object_id", ObjectFieldDBType.STRING);
    public static final ActivityUserField ACTION_TIME = new ActivityUserField("action_time", ObjectFieldDBType.TIMESTAMP);


    //

    public ActivityUserField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
