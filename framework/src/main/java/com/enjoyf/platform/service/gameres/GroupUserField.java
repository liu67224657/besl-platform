package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

import java.util.Date;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-25 上午11:47
 * Description:
 */
public class GroupUserField extends AbstractObjectField {
    public static final GroupUserField GROUP_USER_ID = new GroupUserField("group_user_id", ObjectFieldDBType.LONG, true, true);
    public static final GroupUserField GROUP_ID = new GroupUserField("group_id", ObjectFieldDBType.LONG, true, false);
    public static final GroupUserField UNO = new GroupUserField("uno", ObjectFieldDBType.STRING, true, false);
    public static final GroupUserField VALID_STATUS = new GroupUserField("valid_status", ObjectFieldDBType.INT, true, false);
    public static final GroupUserField VALID_TIME = new GroupUserField("valid_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final GroupUserField CREATE_TIME = new GroupUserField("create_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final GroupUserField CREATE_IP = new GroupUserField("create_ip", ObjectFieldDBType.STRING, true, false);
    public static final GroupUserField CREATE_REASON = new GroupUserField("create_reason", ObjectFieldDBType.STRING, true, false);

    public static final GroupUserField VALID_USERID = new GroupUserField("valid_userid", ObjectFieldDBType.STRING, true, false);
    public static final GroupUserField VALID_UNO = new GroupUserField("valid_uno", ObjectFieldDBType.STRING, true, false);

    public static final GroupUserField LAST_POST_CONTENTID = new GroupUserField("last_post_contentid", ObjectFieldDBType.STRING, true, false);
    public static final GroupUserField LAST_POST_DATE = new GroupUserField("last_post_date", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final GroupUserField LAST_REPLY_CONTENTID = new GroupUserField("last_reply_contentid", ObjectFieldDBType.STRING, true, false);
    public static final GroupUserField LAST_REPLYID = new GroupUserField("last_replyid", ObjectFieldDBType.STRING, true, false);
    public static final GroupUserField LAST_REPLY_TIME = new GroupUserField("last_reply_time", ObjectFieldDBType.TIMESTAMP, true, false);


    public GroupUserField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public GroupUserField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
