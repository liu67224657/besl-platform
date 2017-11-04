/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.usercenter;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 11-12-23 上午14:56
 * Description:
 */
public class ProfileMobileField extends AbstractObjectField {

    //
    public static final ProfileMobileField PROFILE_MOBILE_ID = new ProfileMobileField("profile_mobile_id", ObjectFieldDBType.STRING, true, false);
    public static final ProfileMobileField PROFILEID = new ProfileMobileField("profile_id", ObjectFieldDBType.STRING, true, false);
    public static final ProfileMobileField UID = new ProfileMobileField("uid", ObjectFieldDBType.LONG, true, false);
    public static final ProfileMobileField CREATETIME = new ProfileMobileField("create_time", ObjectFieldDBType.LONG, true, false);
    public static final ProfileMobileField UNO = new ProfileMobileField("uno", ObjectFieldDBType.STRING, true, false);
    public static final ProfileMobileField PROFILEKEY = new ProfileMobileField("profilekey", ObjectFieldDBType.STRING, true, false);
    public static final ProfileMobileField MOBILE = new ProfileMobileField("mobile", ObjectFieldDBType.STRING, true, false);

    //
    public ProfileMobileField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ProfileMobileField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
