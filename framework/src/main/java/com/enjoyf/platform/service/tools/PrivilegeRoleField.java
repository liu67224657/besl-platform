/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.tools;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="taijunli@enjoyfound.com">taijunli</a>
 * Create time: 11-12-23 上午14:56
 * Description:
 */
public class PrivilegeRoleField extends AbstractObjectField {
    //
    public static final PrivilegeRoleField RID = new PrivilegeRoleField("RID", ObjectFieldDBType.INT, true, false);
    public static final PrivilegeRoleField ROLENAME = new PrivilegeRoleField("ROLENAME", ObjectFieldDBType.STRING, true, false);
    public static final PrivilegeRoleField DESCRIPTION = new PrivilegeRoleField("DESCRIPTION", ObjectFieldDBType.STRING, true, false);
    public static final PrivilegeRoleField RTYPE = new PrivilegeRoleField("RTYPE", ObjectFieldDBType.STRING, true, false);
    public static final PrivilegeRoleField RSTATUS = new PrivilegeRoleField("RSTATUS", ObjectFieldDBType.STRING, true, false);

    //
    public PrivilegeRoleField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public PrivilegeRoleField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
