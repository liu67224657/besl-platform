package com.enjoyf.platform.service.comment;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-13
 * Time: 下午12:36
 * To change this template use File | Settings | File Templates.
 */
public class CommentHistoryField extends AbstractObjectField {
    public static final CommentHistoryField HISTORY_ID = new CommentHistoryField("history_id", ObjectFieldDBType.LONG, true, true);
    public static final CommentHistoryField PROFILE_ID = new CommentHistoryField("profileid", ObjectFieldDBType.STRING, true, false);
    public static final CommentHistoryField OBJECT_ID = new CommentHistoryField("object_id", ObjectFieldDBType.STRING, true, false);
    public static final CommentHistoryField DOMAIN = new CommentHistoryField("domain", ObjectFieldDBType.INT, true, false);
    public static final CommentHistoryField ACTION_TIMES = new CommentHistoryField("action_times", ObjectFieldDBType.INT, true, false);
    public static final CommentHistoryField ACTION_DATE = new CommentHistoryField("action_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final CommentHistoryField ACTION_IP = new CommentHistoryField("action_ip", ObjectFieldDBType.STRING, true, false);
    public static final CommentHistoryField ACTION_TYPE = new CommentHistoryField("action_type", ObjectFieldDBType.INT, true, false);
    public static final CommentHistoryField COMMENT_ID = new CommentHistoryField("comment_id", ObjectFieldDBType.STRING, true, false);


    public CommentHistoryField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public CommentHistoryField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
