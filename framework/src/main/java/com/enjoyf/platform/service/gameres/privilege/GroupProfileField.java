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
public class GroupProfileField extends AbstractObjectField {

    public static final GroupProfileField GROUP_PROFILE_ID = new GroupProfileField("group_profile_id", ObjectFieldDBType.LONG, true, true);
    public static final GroupProfileField GROUP_ID = new GroupProfileField("group_id", ObjectFieldDBType.LONG, true, true);
    public static final GroupProfileField UNO = new GroupProfileField("uno", ObjectFieldDBType.STRING, true, true);
    public static final GroupProfileField PROFILE_TYPE = new GroupProfileField("profile_type", ObjectFieldDBType.INT, true, true);
    public static final GroupProfileField STATUS = new GroupProfileField("status", ObjectFieldDBType.INT, true, true);
    public static final GroupProfileField CREATE_TIME = new GroupProfileField("create_time", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final GroupProfileField CREATE_IP = new GroupProfileField("create_ip", ObjectFieldDBType.STRING, true, true);
    public static final GroupProfileField CREATE_UNO = new GroupProfileField("create_uno", ObjectFieldDBType.STRING, true, true);
    public static final GroupProfileField LAST_MODIFY_TIME = new GroupProfileField("last_modify_time", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final GroupProfileField LAST_MODIFY_IP = new GroupProfileField("last_modify_ip", ObjectFieldDBType.STRING, true, true);
    public static final GroupProfileField LAST_MODIFY_UNO = new GroupProfileField("last_modify_uno", ObjectFieldDBType.STRING, true, true);
    public static final GroupProfileField SILENCED_TIME = new GroupProfileField("silenced_time", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final GroupProfileField SILENCED_END_TIME = new GroupProfileField("silenced_end_time", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final GroupProfileField LAST_LOGIN_TIME = new GroupProfileField("last_login_time", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final GroupProfileField SILENCED_REASON = new GroupProfileField("silenced_reason", ObjectFieldDBType.STRING, true, true);


    public GroupProfileField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public GroupProfileField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
