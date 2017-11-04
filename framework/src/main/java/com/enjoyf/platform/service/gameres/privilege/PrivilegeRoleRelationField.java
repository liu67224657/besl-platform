package com.enjoyf.platform.service.gameres.privilege;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-14
 * Time: 下午2:38
 * To change this template use File | Settings | File Templates.
 */
public class PrivilegeRoleRelationField extends AbstractObjectField {

    public static final PrivilegeRoleRelationField RELATION_ID = new PrivilegeRoleRelationField("relation_id", ObjectFieldDBType.LONG, true, true);
    public static final PrivilegeRoleRelationField PRIVILEGE_ID = new PrivilegeRoleRelationField("privilege_id", ObjectFieldDBType.LONG, true, false);
    public static final PrivilegeRoleRelationField ROLE_ID = new PrivilegeRoleRelationField("role_id", ObjectFieldDBType.LONG, true, false);
    public static final PrivilegeRoleRelationField STATUS = new PrivilegeRoleRelationField("status", ObjectFieldDBType.STRING, true, false);
    public static final PrivilegeRoleRelationField LAST_MODIFY_TIME = new PrivilegeRoleRelationField("last_modify_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final PrivilegeRoleRelationField LAST_MODIFY_IP = new PrivilegeRoleRelationField("last_modify_ip", ObjectFieldDBType.STRING, true, false);
    public static final PrivilegeRoleRelationField LAST_MODIFY_USERID = new PrivilegeRoleRelationField("last_modify_userid", ObjectFieldDBType.STRING, true, false);

    public PrivilegeRoleRelationField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public PrivilegeRoleRelationField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
