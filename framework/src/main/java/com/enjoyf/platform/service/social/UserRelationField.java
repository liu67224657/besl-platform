package com.enjoyf.platform.service.social;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class UserRelationField extends AbstractObjectField {
    public static final UserRelationField RELATION_ID = new UserRelationField("relation_id", ObjectFieldDBType.STRING);
    public static final UserRelationField SRC_PROFILEID = new UserRelationField("src_profileid", ObjectFieldDBType.STRING);
    public static final UserRelationField DEST_PROFILEID = new UserRelationField("dest_profileid", ObjectFieldDBType.STRING);
    public static final UserRelationField SRC_STATUS = new UserRelationField("src_status", ObjectFieldDBType.INT);
    public static final UserRelationField DEST_STATUS = new UserRelationField("dest_status", ObjectFieldDBType.INT);
    public static final UserRelationField PROFILE_KEY = new UserRelationField("profilekey", ObjectFieldDBType.STRING);
    public static final UserRelationField RELATION_TYPE = new UserRelationField("relation_type", ObjectFieldDBType.INT);
    public static final UserRelationField EXTSTRING = new UserRelationField("extstring", ObjectFieldDBType.STRING);
    public static final UserRelationField MODIFYIP = new UserRelationField("modify_ip", ObjectFieldDBType.STRING);
    public static final UserRelationField MODIFYTIME = new UserRelationField("modify_time", ObjectFieldDBType.TIMESTAMP);


    public UserRelationField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}