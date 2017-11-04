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
public class PrivilegeResourceField extends AbstractObjectField {
    //
    public static final PrivilegeResourceField RSID = new PrivilegeResourceField("RSID", ObjectFieldDBType.INT, true, false);
    public static final PrivilegeResourceField RSNAME = new PrivilegeResourceField("RSNAME", ObjectFieldDBType.STRING, true, false);
    public static final PrivilegeResourceField RSURL = new PrivilegeResourceField("RSURL", ObjectFieldDBType.STRING, true, false);
    public static final PrivilegeResourceField RSLEVEL = new PrivilegeResourceField("RSLEVEL", ObjectFieldDBType.INT, true, false);
    public static final PrivilegeResourceField RSSTATUS = new PrivilegeResourceField("RSSTATUS", ObjectFieldDBType.STRING, true, false);
    public static final PrivilegeResourceField PARENTID = new PrivilegeResourceField("PARENTID", ObjectFieldDBType.INT, true, false);
    public static final PrivilegeResourceField ORDERFIELD = new PrivilegeResourceField("ORDERFIELD", ObjectFieldDBType.INT, true, false);
    public static final PrivilegeResourceField ICONURL = new PrivilegeResourceField("ICONURL", ObjectFieldDBType.STRING, true, false);
    public static final PrivilegeResourceField RSTYPE = new PrivilegeResourceField("RSTYPE", ObjectFieldDBType.INT, true, false);
    public static final PrivilegeResourceField ISMENU = new PrivilegeResourceField("ISMENU", ObjectFieldDBType.STRING, true, false);
    public static final PrivilegeResourceField DESCRIPTION = new PrivilegeResourceField("DESCRIPTION", ObjectFieldDBType.STRING, true, false);

    //
    public PrivilegeResourceField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public PrivilegeResourceField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
