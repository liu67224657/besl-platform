package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-12-16
 * Time: 上午10:04
 * To change this template use File | Settings | File Templates.
 */
public class ProfileDeveloperField extends AbstractObjectField {
    //
    public static final ProfileDeveloperField UNO = new ProfileDeveloperField("uno", ObjectFieldDBType.STRING, true, true);
    public static final ProfileDeveloperField CONTACTS = new ProfileDeveloperField("contacts", ObjectFieldDBType.STRING, true, true);
    public static final ProfileDeveloperField VERIFY_DESC = new ProfileDeveloperField("verify_desc", ObjectFieldDBType.STRING, true, true);
    public static final ProfileDeveloperField EMAIL = new ProfileDeveloperField("email", ObjectFieldDBType.STRING, true, true);
    public static final ProfileDeveloperField QQ = new ProfileDeveloperField("qq", ObjectFieldDBType.STRING, true, true);
    public static final ProfileDeveloperField PHONE = new ProfileDeveloperField("phone", ObjectFieldDBType.STRING, true, true);
    public static final ProfileDeveloperField COMPANY = new ProfileDeveloperField("company", ObjectFieldDBType.STRING, true, true);
    public static final ProfileDeveloperField LICENSE_PIC = new ProfileDeveloperField("license_pic", ObjectFieldDBType.STRING, true, true);
    public static final ProfileDeveloperField LOCATION = new ProfileDeveloperField("location", ObjectFieldDBType.STRING, true, true);

    public static final ProfileDeveloperField CATEGORY = new ProfileDeveloperField("category", ObjectFieldDBType.INT, true, true);
    public static final ProfileDeveloperField VERIFY_STATUS = new ProfileDeveloperField("status", ObjectFieldDBType.STRING, true, true);
    public static final ProfileDeveloperField CREATE_DATE = new ProfileDeveloperField("create_date", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final ProfileDeveloperField CREATE_IP = new ProfileDeveloperField("create_ip", ObjectFieldDBType.STRING, true, true);
    public static final ProfileDeveloperField MODIFY_DATE = new ProfileDeveloperField("modify_date", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final ProfileDeveloperField MODIFY_IP = new ProfileDeveloperField("modify_ip", ObjectFieldDBType.STRING, true, true);
    public static final ProfileDeveloperField VERIFY_DATE = new ProfileDeveloperField("verify_date", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final ProfileDeveloperField VERIFY_IP = new ProfileDeveloperField("verify_ip", ObjectFieldDBType.STRING, true, true);
    public static final ProfileDeveloperField VERIFY_REASON = new ProfileDeveloperField("reason", ObjectFieldDBType.STRING, true, true);
    //
    public ProfileDeveloperField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ProfileDeveloperField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
