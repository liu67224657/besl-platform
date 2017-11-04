package com.enjoyf.platform.service.gameres.privilege;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-14
 * Time: 上午11:58
 * To change this template use File | Settings | File Templates.
 */
public class GroupPrivilegeField extends AbstractObjectField{

    public static final GroupPrivilegeField PRIVILEGE_ID = new GroupPrivilegeField("privilege_id", ObjectFieldDBType.LONG, true, true);
    public static final GroupPrivilegeField PRIVILEGE_NAME = new GroupPrivilegeField("privilege_name", ObjectFieldDBType.STRING, true, false);
    public static final GroupPrivilegeField PRIVILEGE_CODE = new GroupPrivilegeField("privilege_code", ObjectFieldDBType.STRING, true, false);
    public static final GroupPrivilegeField PRIVILEGE_DESC = new GroupPrivilegeField("privilege_desc", ObjectFieldDBType.STRING, true, false);
    public static final GroupPrivilegeField PRIVILEGE_TYPE = new GroupPrivilegeField("privilege_type", ObjectFieldDBType.INT, true, false);
    public static final GroupPrivilegeField STATUS = new GroupPrivilegeField("status", ObjectFieldDBType.STRING, true, false);

    public static final GroupPrivilegeField CREATE_DATE = new GroupPrivilegeField("create_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final GroupPrivilegeField LAST_MODIFY_DATE = new GroupPrivilegeField("last_modify_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final GroupPrivilegeField LAST_MODIFY_IP = new GroupPrivilegeField("last_modify_ip", ObjectFieldDBType.STRING, true, false);
    public static final GroupPrivilegeField LAST_MODIFY_USERID = new GroupPrivilegeField("last_modify_userid", ObjectFieldDBType.STRING, true, false);

    public GroupPrivilegeField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public GroupPrivilegeField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
