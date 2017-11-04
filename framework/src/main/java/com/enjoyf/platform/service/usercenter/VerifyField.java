package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class VerifyField extends AbstractObjectField {
    public static final VerifyField VERIFY_ID = new VerifyField("verify_id", ObjectFieldDBType.LONG);
    public static final VerifyField VERIFY_NAME = new VerifyField("verify_name", ObjectFieldDBType.STRING);
    public static final VerifyField VALID_STATUS = new VerifyField("valid_status", ObjectFieldDBType.STRING);

    public VerifyField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}