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
public class UserProfileIdxField extends AbstractObjectField {

    //
    public static final UserProfileIdxField UID = new UserProfileIdxField("uid", ObjectFieldDBType.LONG, false, true);
    public static final UserProfileIdxField STATUS = new UserProfileIdxField("status", ObjectFieldDBType.STRING, true, false);
    public static final UserProfileIdxField PROFILEID = new UserProfileIdxField("profileid", ObjectFieldDBType.STRING, true, false);
    public static final UserProfileIdxField PROFILEKEY = new UserProfileIdxField("profilekey", ObjectFieldDBType.STRING, true, false);
    public static final UserProfileIdxField UNO = new UserProfileIdxField("uno", ObjectFieldDBType.STRING, true, false);

    //
    public UserProfileIdxField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public UserProfileIdxField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
