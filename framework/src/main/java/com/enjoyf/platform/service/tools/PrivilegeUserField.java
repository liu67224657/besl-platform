/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.tools;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="taijunli@enjoyfound.com">taijunli</a>
 * Create time: 11-12-23 上午14:56
 * Description:
 */
public class PrivilegeUserField extends AbstractObjectField {
    //
    public static final PrivilegeUserField UNO = new PrivilegeUserField("UNO", ObjectFieldDBType.INT, true, false);
    public static final PrivilegeUserField USERID = new PrivilegeUserField("USERID", ObjectFieldDBType.STRING, true, false);
    public static final PrivilegeUserField USERNAME = new PrivilegeUserField("USERNAME", ObjectFieldDBType.STRING, true, false);
    public static final PrivilegeUserField PASSWORD = new PrivilegeUserField("PASSWORD", ObjectFieldDBType.STRING, true, false);
    public static final PrivilegeUserField USTATUS = new PrivilegeUserField("USTATUS", ObjectFieldDBType.STRING, true, false);
    public static final PrivilegeUserField LIMITLOCATION = new PrivilegeUserField("LIMITLOCATION", ObjectFieldDBType.STRING, true, false);

    //
    public PrivilegeUserField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public PrivilegeUserField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
