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
public class CommentBeanField extends AbstractObjectField {
    public static final CommentBeanField COMMENT_ID = new CommentBeanField("comment_id", ObjectFieldDBType.STRING, true, true);
    public static final CommentBeanField UNI_KEY = new CommentBeanField("unikey", ObjectFieldDBType.STRING, true, false);
    public static final CommentBeanField URI = new CommentBeanField("uri", ObjectFieldDBType.STRING, true, false);
    public static final CommentBeanField DOMAIN = new CommentBeanField("domain", ObjectFieldDBType.INT, true, false);
    public static final CommentBeanField TITLE = new CommentBeanField("title", ObjectFieldDBType.STRING, true, false);
    public static final CommentBeanField PIC = new CommentBeanField("pic", ObjectFieldDBType.STRING, true, false);
    public static final CommentBeanField DESCRIPTION = new CommentBeanField("description", ObjectFieldDBType.STRING, true, false);
    public static final CommentBeanField CREATE_TIME = new CommentBeanField("create_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final CommentBeanField MODIFY_TIME = new CommentBeanField("modify_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final CommentBeanField REMOVE_STATUS = new CommentBeanField("remove_status", ObjectFieldDBType.STRING, true, false);
    public static final CommentBeanField TOTAL_ROWS = new CommentBeanField("total_rows", ObjectFieldDBType.INT, true, false);
    public static final CommentBeanField COMMENT_SUM = new CommentBeanField("comment_sum", ObjectFieldDBType.INT, true, false);
    public static final CommentBeanField LONG_COMMENT_SUM = new CommentBeanField("long_comment_sum", ObjectFieldDBType.INT, true, false);
    public static final CommentBeanField AVERAGE_SCORE = new CommentBeanField("average_score", ObjectFieldDBType.DOUBLE, true, false);
    public static final CommentBeanField DISPLAY_ORDER = new CommentBeanField("display_order", ObjectFieldDBType.LONG, true, false);
    public static final CommentBeanField SCORE_COMMENT_SUM = new CommentBeanField("score_comment_sum", ObjectFieldDBType.INT, true, false);
    public static final CommentBeanField SCORE_SUM = new CommentBeanField("score_sum", ObjectFieldDBType.INT, true, false);
    public static final CommentBeanField SCORE_TIMES = new CommentBeanField("score_times", ObjectFieldDBType.INT, true, false);
    public static final CommentBeanField FIVE_USER_SUM = new CommentBeanField("five_user_sum", ObjectFieldDBType.INT, true, false);
    public static final CommentBeanField FOUR_USER_SUM = new CommentBeanField("four_user_sum", ObjectFieldDBType.INT, true, false);
    public static final CommentBeanField THREE_USER_SUM = new CommentBeanField("three_user_sum", ObjectFieldDBType.INT, true, false);
    public static final CommentBeanField TWO_USER_SUM = new CommentBeanField("two_user_sum", ObjectFieldDBType.INT, true, false);
    public static final CommentBeanField ONE_USER_SUM = new CommentBeanField("one_user_sum", ObjectFieldDBType.INT, true, false);
    public static final CommentBeanField SHARE_SUM = new CommentBeanField("share_sum", ObjectFieldDBType.INT, true, false);
    public static final CommentBeanField EXPANDSTR = new CommentBeanField("expandstr", ObjectFieldDBType.STRING, true, false);
    public static final CommentBeanField GROUPID = new CommentBeanField("group_id", ObjectFieldDBType.LONG, true, false);

    public CommentBeanField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public CommentBeanField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
