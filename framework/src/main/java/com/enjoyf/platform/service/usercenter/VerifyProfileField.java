package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class VerifyProfileField extends AbstractObjectField {
    public static final VerifyProfileField PROFILEID = new VerifyProfileField("profile_id", ObjectFieldDBType.STRING);
    public static final VerifyProfileField DESCRIPTION = new VerifyProfileField("description", ObjectFieldDBType.STRING);
    public static final VerifyProfileField VERIFYTYPE = new VerifyProfileField("verify_type", ObjectFieldDBType.LONG);
    public static final VerifyProfileField POINT = new VerifyProfileField("ask_point", ObjectFieldDBType.INT);
    public static final VerifyProfileField NICK = new VerifyProfileField("nick", ObjectFieldDBType.STRING);
    public static final VerifyProfileField APPKEY = new VerifyProfileField("appkey", ObjectFieldDBType.STRING);

    public VerifyProfileField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}