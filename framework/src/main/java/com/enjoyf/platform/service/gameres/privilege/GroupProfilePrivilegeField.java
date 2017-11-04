package com.enjoyf.platform.service.gameres.privilege;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-14
 * Time: 下午6:33
 * To change this template use File | Settings | File Templates.
 */
public class GroupProfilePrivilegeField extends AbstractObjectField {

    public static final GroupProfilePrivilegeField PROFILE_PRIVILEGE_ID = new GroupProfilePrivilegeField("profile_privilege_id", ObjectFieldDBType.LONG, true, true);
    public static final GroupProfilePrivilegeField UNO = new GroupProfilePrivilegeField("uno", ObjectFieldDBType.STRING, true, false);
    public static final GroupProfilePrivilegeField DEST_ID = new GroupProfilePrivilegeField("dest_id", ObjectFieldDBType.LONG, true, false);
    public static final GroupProfilePrivilegeField DEST_TYPE = new GroupProfilePrivilegeField("dest_type", ObjectFieldDBType.INT, true, false);
    public static final GroupProfilePrivilegeField GROUP_ID = new GroupProfilePrivilegeField("group_id", ObjectFieldDBType.LONG, true, false);
    public static final GroupProfilePrivilegeField STATUS = new GroupProfilePrivilegeField("act_status", ObjectFieldDBType.STRING, true, false);
    public static final GroupProfilePrivilegeField LAST_MODIFY_TIME = new GroupProfilePrivilegeField("last_modify_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final GroupProfilePrivilegeField LAST_MODIFY_IP = new GroupProfilePrivilegeField("last_modify_ip", ObjectFieldDBType.STRING, true, false);
    public static final GroupProfilePrivilegeField LAST_MODIFY_UNO = new GroupProfilePrivilegeField("last_modify_uno", ObjectFieldDBType.STRING, true, false);

    public GroupProfilePrivilegeField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public GroupProfilePrivilegeField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
