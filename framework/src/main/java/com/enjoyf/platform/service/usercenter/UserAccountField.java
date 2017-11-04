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
public class UserAccountField extends AbstractObjectField {

    //
    public static final UserAccountField UNO = new UserAccountField("uno", ObjectFieldDBType.STRING, false, true);
    public static final UserAccountField MOBILE = new UserAccountField("mobile", ObjectFieldDBType.STRING, true, false);
    public static final UserAccountField ADDRESS = new UserAccountField("address", ObjectFieldDBType.STRING, true, false);
    public static final UserAccountField FLAG = new UserAccountField("flag", ObjectFieldDBType.INT, true, false);
    public static final UserAccountField UPDATETIME = new UserAccountField("updatetime", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final UserAccountField UPDATEIP = new UserAccountField("updateip", ObjectFieldDBType.STRING, true, false);

    //
    public UserAccountField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public UserAccountField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
