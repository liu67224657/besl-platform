package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class UserPrivacyField extends AbstractObjectField {
    public static final UserPrivacyField PROFILEID = new UserPrivacyField("profile_id", ObjectFieldDBType.STRING);
    public static final UserPrivacyField ALARMSETTING = new UserPrivacyField("alarm_setting", ObjectFieldDBType.STRING);
    public static final UserPrivacyField FUNCTIONSETTING = new UserPrivacyField("function_setting", ObjectFieldDBType.STRING);
    public static final UserPrivacyField CREATETIME = new UserPrivacyField("createtime", ObjectFieldDBType.TIMESTAMP);
    public static final UserPrivacyField CREATEIP = new UserPrivacyField("createip", ObjectFieldDBType.STRING);
    public static final UserPrivacyField UPDATETIME = new UserPrivacyField("updatetime", ObjectFieldDBType.TIMESTAMP);
    public static final UserPrivacyField UPDATEIP = new UserPrivacyField("updateip", ObjectFieldDBType.STRING);


    public UserPrivacyField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}