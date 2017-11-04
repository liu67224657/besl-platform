/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.admin;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-31 上午11:16
 * Description:
 */
public class AdminField extends AbstractObjectField {
    //
    public static final AdminField ADMINUNO = new AdminField("ADMINUNO", ObjectFieldDBType.STRING, false, true);

    public static final AdminField LOGINNAME = new AdminField("LOGINNAME", ObjectFieldDBType.STRING, false, false);
    public static final AdminField TRUENAME = new AdminField("TRUENAME", ObjectFieldDBType.STRING, true, false);
    public static final AdminField LOGINPWD = new AdminField("LOGINPWD", ObjectFieldDBType.STRING, true, false);

    public static final AdminField SUPERADMIN = new AdminField("SUPERADMIN", ObjectFieldDBType.BOOLEAN, true, false);
    public static final AdminField ADMINROLES = new AdminField("ADMINROLES", ObjectFieldDBType.STRING, true, false);
    public static final AdminField VALIDSTATUS = new AdminField("VALIDSTATUS", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final AdminField DESCRIPTION = new AdminField("DESCRIPTION", ObjectFieldDBType.STRING, true, false);

    public static final AdminField UPDATEDATE = new AdminField("UPDATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final AdminField UPDATEADMINUNO = new AdminField("UPDATEADMINUNO", ObjectFieldDBType.STRING, true, false);
    public static final AdminField UPDATELOGINNAME = new AdminField("UPDATELOGINNAME", ObjectFieldDBType.STRING, true, false);
    public static final AdminField UPDATEIP = new AdminField("UPDATEIP", ObjectFieldDBType.STRING, true, false);

    //
    public AdminField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AdminField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
