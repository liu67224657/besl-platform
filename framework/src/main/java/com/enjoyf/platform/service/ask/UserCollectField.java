package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;



public class UserCollectField extends AbstractObjectField {
    public static final UserCollectField ID = new UserCollectField("id", ObjectFieldDBType.LONG);
public static final UserCollectField PROFILEID = new UserCollectField("profile_id", ObjectFieldDBType.STRING);
public static final UserCollectField TITLE = new UserCollectField("title", ObjectFieldDBType.STRING);
public static final UserCollectField DESC = new UserCollectField("from_desc", ObjectFieldDBType.STRING);
public static final UserCollectField URL = new UserCollectField("url", ObjectFieldDBType.STRING);
public static final UserCollectField COLLECTTYPE = new UserCollectField("collect_type", ObjectFieldDBType.INT);
public static final UserCollectField PUBLISHTIME = new UserCollectField("publish_time", ObjectFieldDBType.TIMESTAMP);
public static final UserCollectField CREATEDATE = new UserCollectField("create_date", ObjectFieldDBType.TIMESTAMP);
public static final UserCollectField APPKEY = new UserCollectField("appkey", ObjectFieldDBType.STRING);
public static final UserCollectField CONTENT_ID = new UserCollectField("content_id", ObjectFieldDBType.LONG);




    public UserCollectField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}