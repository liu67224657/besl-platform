package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-1
 * Time: 下午7:07
 * To change this template use File | Settings | File Templates.
 */
public class ProfileMobileDeviceField extends AbstractObjectField {
    public static final ProfileMobileDeviceField PMDID = new ProfileMobileDeviceField("PMDID", ObjectFieldDBType.LONG, true, true);
    public static final ProfileMobileDeviceField MDSERIAL = new ProfileMobileDeviceField("MDSERIAL", ObjectFieldDBType.STRING, true, false);
    public static final ProfileMobileDeviceField MDCLIENTTYPE = new ProfileMobileDeviceField("MDCLIENTTYPE", ObjectFieldDBType.STRING, true, false);
    public static final ProfileMobileDeviceField MDHDTYPE = new ProfileMobileDeviceField("MDHDTYPE", ObjectFieldDBType.STRING, true, false);
    public static final ProfileMobileDeviceField MDOSVERSION = new ProfileMobileDeviceField("MDOSVERSION", ObjectFieldDBType.STRING, true, false);
    public static final ProfileMobileDeviceField UNO = new ProfileMobileDeviceField("UNO", ObjectFieldDBType.STRING, true, false);
    public static final ProfileMobileDeviceField PUSHMSGID = new ProfileMobileDeviceField("PUSHMSGID", ObjectFieldDBType.LONG, true, false);
    public static final ProfileMobileDeviceField PUSHSTATUS = new ProfileMobileDeviceField("PUSHSTATUS", ObjectFieldDBType.STRING, true, false);
    public static final ProfileMobileDeviceField VALIDSTATUS = new ProfileMobileDeviceField("VALIDSTATUS", ObjectFieldDBType.STRING, true, false);
    public static final ProfileMobileDeviceField CREATEDATE = new ProfileMobileDeviceField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);


    //
    public ProfileMobileDeviceField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ProfileMobileDeviceField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
