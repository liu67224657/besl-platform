package com.enjoyf.platform.service.gameres.privilege;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-14
 * Time: 下午12:01
 * To change this template use File | Settings | File Templates.
 */
public class RoleField extends AbstractObjectField {

    public static final RoleField ROLE_ID = new RoleField("role_id", ObjectFieldDBType.LONG, true, true);
    public static final RoleField GROUP_ID = new RoleField("group_id", ObjectFieldDBType.LONG, true, false);
    public static final RoleField ROLE_LEVEL = new RoleField("role_level", ObjectFieldDBType.INT, true, false);
    public static final RoleField ROLE_NAME = new RoleField("role_name", ObjectFieldDBType.STRING, true, false);
    public static final RoleField ROLE_DESC = new RoleField("role_desc", ObjectFieldDBType.STRING, true, false);
    public static final RoleField ROLE_TYPE = new RoleField("role_type", ObjectFieldDBType.INT, true, false);
    public static final RoleField STATUS = new RoleField("status", ObjectFieldDBType.STRING, true, false);
    public static final RoleField LAST_MODIFY_TIME = new RoleField("lsat_modify_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final RoleField LAST_MODIFY_IP = new RoleField("last_modify_ip", ObjectFieldDBType.STRING, true, false);
    public static final RoleField LAST_MODIFY_USERID = new RoleField("last_modify_userid", ObjectFieldDBType.STRING, true, false);

    public RoleField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public RoleField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
