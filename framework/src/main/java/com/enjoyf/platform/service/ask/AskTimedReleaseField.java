package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class AskTimedReleaseField extends AbstractObjectField {
    public static final AskTimedReleaseField TIMEID = new AskTimedReleaseField("timeid", ObjectFieldDBType.LONG);
    public static final AskTimedReleaseField TIMERELSEASETYPE = new AskTimedReleaseField("time_relsease_type", ObjectFieldDBType.INT);
    public static final AskTimedReleaseField TITLE = new AskTimedReleaseField("title", ObjectFieldDBType.STRING);
    public static final AskTimedReleaseField VALIDSTATUS = new AskTimedReleaseField("valid_status", ObjectFieldDBType.STRING);
    public static final AskTimedReleaseField CREATETIME = new AskTimedReleaseField("create_time", ObjectFieldDBType.TIMESTAMP);
    public static final AskTimedReleaseField RELEASETIME = new AskTimedReleaseField("release_time", ObjectFieldDBType.TIMESTAMP);
    public static final AskTimedReleaseField RELEASETIMES = new AskTimedReleaseField("release_times", ObjectFieldDBType.INT);
    public static final AskTimedReleaseField EXTSTR = new AskTimedReleaseField("ext_str", ObjectFieldDBType.STRING);


    public AskTimedReleaseField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}