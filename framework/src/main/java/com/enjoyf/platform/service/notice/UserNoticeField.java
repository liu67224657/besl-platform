package com.enjoyf.platform.service.notice;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class UserNoticeField extends AbstractObjectField {
    public static final UserNoticeField USER_NOTICE_ID = new UserNoticeField("user_notice_id", ObjectFieldDBType.LONG);
    public static final UserNoticeField PROFILEID = new UserNoticeField("profile_id", ObjectFieldDBType.STRING);
    public static final UserNoticeField APPKEY = new UserNoticeField("appkey", ObjectFieldDBType.STRING);
    public static final UserNoticeField NOTICETYPE = new UserNoticeField("notice_type", ObjectFieldDBType.STRING);
    public static final UserNoticeField BODY = new UserNoticeField("body", ObjectFieldDBType.STRING);
    public static final UserNoticeField CREATETIME = new UserNoticeField("create_time", ObjectFieldDBType.TIMESTAMP);
    public static final UserNoticeField DESTID = new UserNoticeField("dest_id", ObjectFieldDBType.STRING);

    public UserNoticeField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}