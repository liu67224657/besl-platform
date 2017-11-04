package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class BlackListHistoryField extends AbstractObjectField {
    public static final BlackListHistoryField ID = new BlackListHistoryField("id", ObjectFieldDBType.LONG);
    public static final BlackListHistoryField STARTTIME = new BlackListHistoryField("start_time", ObjectFieldDBType.TIMESTAMP);
    public static final BlackListHistoryField ENDTIME = new BlackListHistoryField("end_time", ObjectFieldDBType.TIMESTAMP);
    public static final BlackListHistoryField PROFILEID = new BlackListHistoryField("profile_id", ObjectFieldDBType.STRING);
    public static final BlackListHistoryField REASON = new BlackListHistoryField("reason", ObjectFieldDBType.STRING);
    public static final BlackListHistoryField CREATE_TIME = new BlackListHistoryField("create_time", ObjectFieldDBType.TIMESTAMP);


    public BlackListHistoryField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}