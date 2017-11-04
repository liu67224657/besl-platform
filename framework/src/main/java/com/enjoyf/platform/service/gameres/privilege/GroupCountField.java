package com.enjoyf.platform.service.gameres.privilege;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-16
 * Time: 下午6:37
 * To change this template use File | Settings | File Templates.
 */
public class GroupCountField extends AbstractObjectField {

    public static final GroupCountField GROUP_COUNT_ID = new GroupCountField("group_count_id", ObjectFieldDBType.LONG, true, true);
    public static final GroupCountField GROUP_ID = new GroupCountField("group_id", ObjectFieldDBType.LONG, true, false);
    public static final GroupCountField PROFILE_NUM = new GroupCountField("profile_num", ObjectFieldDBType.INT, true, false);
    public static final GroupCountField NEW_PROFILE_NUM = new GroupCountField("new_profile_num", ObjectFieldDBType.INT, true, false);
    public static final GroupCountField NOTE_NUM = new GroupCountField("note_num", ObjectFieldDBType.INT, true, false);
    public static final GroupCountField NEW_NOTE_NUM = new GroupCountField("new_note_num", ObjectFieldDBType.INT, true, false);
    public static final GroupCountField VISIT_NUM = new GroupCountField("visit_num", ObjectFieldDBType.INT, true, false);
    public static final GroupCountField NEW_VISIT_NUM = new GroupCountField("new_visit_num", ObjectFieldDBType.INT, true, false);

    public GroupCountField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public GroupCountField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
