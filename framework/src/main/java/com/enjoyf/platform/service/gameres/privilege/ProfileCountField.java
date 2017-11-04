package com.enjoyf.platform.service.gameres.privilege;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-10-16
 * Time: 下午6:38
 * To change this template use File | Settings | File Templates.
 */
public class ProfileCountField extends AbstractObjectField {

    public static final ProfileCountField PROFILE_COUNT_ID = new ProfileCountField("profile_count_id", ObjectFieldDBType.LONG, true, true);
    public static final ProfileCountField GROUP_ID = new ProfileCountField("group_id", ObjectFieldDBType.LONG, true, false);
    public static final ProfileCountField UNO = new ProfileCountField("uno", ObjectFieldDBType.STRING, true, false);
    public static final ProfileCountField POST_NUM = new ProfileCountField("post_num", ObjectFieldDBType.INT, true, false);
    public static final ProfileCountField NEW_POST_NUM = new ProfileCountField("new_post_num", ObjectFieldDBType.INT, true, false);
    public static final ProfileCountField REPLY_NUM = new ProfileCountField("reply_num", ObjectFieldDBType.INT, true, false);
    public static final ProfileCountField NEW_REPLY_NUM = new ProfileCountField("new_reply_num", ObjectFieldDBType.INT, true, false);
    public static final ProfileCountField DELETE_NUM = new ProfileCountField("delete_num", ObjectFieldDBType.INT, true, false);
    public static final ProfileCountField NEW_DELETE_NUM = new ProfileCountField("new_delete_num", ObjectFieldDBType.INT, true, false);


    public ProfileCountField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ProfileCountField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
