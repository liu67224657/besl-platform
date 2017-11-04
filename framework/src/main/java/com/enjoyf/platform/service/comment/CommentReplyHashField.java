package com.enjoyf.platform.service.comment;/**
 * Created by ericliu on 16/8/30.
 */

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/8/30
 */
public class CommentReplyHashField extends AbstractObjectField {
    public static final CommentReplyHashField REPLY_ID = new CommentReplyHashField("reply_id", ObjectFieldDBType.LONG);
    public static final CommentReplyHashField COMMENT_ID = new CommentReplyHashField("comment_id", ObjectFieldDBType.STRING);

    public static final CommentReplyHashField HOTSUM = new CommentReplyHashField("hot_sum", ObjectFieldDBType.INT);
    public static final CommentReplyHashField COMMENTSUM = new CommentReplyHashField("comment_sum", ObjectFieldDBType.INT);
    public static final CommentReplyHashField AGREESUM = new CommentReplyHashField("agree_sum", ObjectFieldDBType.INT);
    public static final CommentReplyHashField TOTAL = new CommentReplyHashField("total", ObjectFieldDBType.INT);


    public CommentReplyHashField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

}
