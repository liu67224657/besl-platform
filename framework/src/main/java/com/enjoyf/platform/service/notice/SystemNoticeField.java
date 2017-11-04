package com.enjoyf.platform.service.notice;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class SystemNoticeField extends AbstractObjectField {
    public static final SystemNoticeField SYSTEMNOTICEID = new SystemNoticeField("system_notice_id", ObjectFieldDBType.LONG);
    public static final SystemNoticeField TEXT = new SystemNoticeField("text", ObjectFieldDBType.STRING);
    public static final SystemNoticeField CREATETIME = new SystemNoticeField("create_time", ObjectFieldDBType.TIMESTAMP);
    public static final SystemNoticeField APPKEY = new SystemNoticeField("appkey", ObjectFieldDBType.STRING);
    public static final SystemNoticeField JT = new SystemNoticeField("jt", ObjectFieldDBType.STRING);
    public static final SystemNoticeField JI = new SystemNoticeField("ji", ObjectFieldDBType.STRING);
    public static final SystemNoticeField TITLE = new SystemNoticeField("title", ObjectFieldDBType.STRING);
    public static final SystemNoticeField PLATFORM = new SystemNoticeField("platform", ObjectFieldDBType.INT);
    public static final SystemNoticeField VERSION = new SystemNoticeField("version", ObjectFieldDBType.STRING);

    public SystemNoticeField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}