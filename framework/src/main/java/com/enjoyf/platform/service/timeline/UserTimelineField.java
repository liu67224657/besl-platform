package com.enjoyf.platform.service.timeline;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class UserTimelineField extends AbstractObjectField {
    public static final UserTimelineField ITEMID = new UserTimelineField("item_id", ObjectFieldDBType.LONG);
    public static final UserTimelineField PROFILEID = new UserTimelineField("profile_id", ObjectFieldDBType.STRING);
    public static final UserTimelineField DOMAIN = new UserTimelineField("domain", ObjectFieldDBType.STRING);
    public static final UserTimelineField TYPE = new UserTimelineField("type", ObjectFieldDBType.STRING);
    public static final UserTimelineField DESTPROFILEID = new UserTimelineField("dest_profileid", ObjectFieldDBType.STRING);
    public static final UserTimelineField DESTID = new UserTimelineField("dest_id", ObjectFieldDBType.LONG);
    public static final UserTimelineField EXTENDBODY = new UserTimelineField("extend_body", ObjectFieldDBType.STRING);
    public static final UserTimelineField ACTIONTYPE = new UserTimelineField("action_type", ObjectFieldDBType.STRING);
    public static final UserTimelineField LINEKEY = new UserTimelineField("linekey", ObjectFieldDBType.STRING);
    public static final UserTimelineField CREATETIME = new UserTimelineField("create_time", ObjectFieldDBType.TIMESTAMP);


    public UserTimelineField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}