package com.enjoyf.platform.service.comment;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created with IntelliJ IDEA.
 * User: Tony Diao
 * Date: 15-1-6
 * Time: 下午12:36
 * To change this template use File | Settings | File Templates.
 */
public class CommentVoteOptionField extends AbstractObjectField {
    public static final CommentVoteOptionField VOTE_OPTION_ID = new CommentVoteOptionField("vote_option_id", ObjectFieldDBType.STRING, false, true);
    public static final CommentVoteOptionField COMMENT_ID = new CommentVoteOptionField("comment_id", ObjectFieldDBType.STRING, false, false);
    public static final CommentVoteOptionField TITLE = new CommentVoteOptionField("title", ObjectFieldDBType.STRING, true, false);
    public static final CommentVoteOptionField PIC = new CommentVoteOptionField("pic", ObjectFieldDBType.STRING, true, false);

    public static final CommentVoteOptionField OPTION_TOTAL = new CommentVoteOptionField("option_total", ObjectFieldDBType.LONG, true, false);

    public static final CommentVoteOptionField REMOVE_STATUS = new CommentVoteOptionField("remove_status", ObjectFieldDBType.STRING, true, false);

    public static final CommentVoteOptionField DISPLAY_ORDER = new CommentVoteOptionField("display_order", ObjectFieldDBType.LONG, true, false);

    public static final CommentVoteOptionField START_TIME = new CommentVoteOptionField("start_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final CommentVoteOptionField CREATE_USER = new CommentVoteOptionField("create_user", ObjectFieldDBType.STRING, true, false);


    public CommentVoteOptionField(String column, ObjectFieldDBType type, boolean modify, boolean unique) {
        super(column, type, modify, unique);
    }

    public CommentVoteOptionField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
