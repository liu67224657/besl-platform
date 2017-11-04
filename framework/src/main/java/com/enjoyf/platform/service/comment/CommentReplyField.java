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
public class CommentReplyField extends AbstractObjectField {
    public static final CommentReplyField REPLY_ID = new CommentReplyField("reply_id", ObjectFieldDBType.LONG, true, true);
    public static final CommentReplyField REPLY_UNO = new CommentReplyField("reply_uno", ObjectFieldDBType.STRING, true, false);
    public static final CommentReplyField REPLY_PROFILEID = new CommentReplyField("reply_profileid", ObjectFieldDBType.STRING, true, false);
    public static final CommentReplyField REPLY_PROFILEKEY = new CommentReplyField("reply_profilekey", ObjectFieldDBType.STRING, true, false);


    public static final CommentReplyField COMMENT_ID = new CommentReplyField("comment_id", ObjectFieldDBType.STRING, true, false);
    public static final CommentReplyField PARENT_ID = new CommentReplyField("parent_id", ObjectFieldDBType.LONG, true, false);
    public static final CommentReplyField PARENT_UNO = new CommentReplyField("parent_uno", ObjectFieldDBType.STRING, true, false);
    public static final CommentReplyField PARENT_PROFILEID = new CommentReplyField("parent_profileid", ObjectFieldDBType.STRING, true, false);
    public static final CommentReplyField PARENT_PROFILEKEY = new CommentReplyField("parent_profilekey", ObjectFieldDBType.STRING, true, false);


    public static final CommentReplyField ROOT_ID = new CommentReplyField("root_id", ObjectFieldDBType.LONG, true, false);
    public static final CommentReplyField ROOT_UNO = new CommentReplyField("root_uno", ObjectFieldDBType.STRING, true, false);
    public static final CommentReplyField ROOT_PROFILEID = new CommentReplyField("root_profileid", ObjectFieldDBType.STRING, true, false);
    public static final CommentReplyField ROOT_PROFILEKEY = new CommentReplyField("root_profilekey", ObjectFieldDBType.STRING, true, false);

    public static final CommentReplyField SUB_REPLY_SUM = new CommentReplyField("sub_reply_sum", ObjectFieldDBType.INT, true, false);
    public static final CommentReplyField AGREE_SUM = new CommentReplyField("agree_sum", ObjectFieldDBType.INT, true, false);
    public static final CommentReplyField DISAGREE_SUM = new CommentReplyField("disagree_sum", ObjectFieldDBType.INT, true, false);
    public static final CommentReplyField REPLY_BODY = new CommentReplyField("reply_body", ObjectFieldDBType.STRING, true, false);
    public static final CommentReplyField CREATE_TIME = new CommentReplyField("create_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final CommentReplyField CREATE_IP = new CommentReplyField("create_ip", ObjectFieldDBType.STRING, true, false);
    public static final CommentReplyField MODIFY_TIME = new CommentReplyField("modify_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final CommentReplyField REMOVE_STATUS = new CommentReplyField("remove_status", ObjectFieldDBType.STRING, true, false);
    public static final CommentReplyField FLOOR_NUM = new CommentReplyField("floor_num", ObjectFieldDBType.INT, true, false);
    public static final CommentReplyField TOTAL_ROWS = new CommentReplyField("total_rows", ObjectFieldDBType.INT, true, false);
    public static final CommentReplyField DISPLAY_ORDER = new CommentReplyField("display_order", ObjectFieldDBType.LONG, true, false);
    public static final CommentReplyField DOMAIN = new CommentReplyField("domain", ObjectFieldDBType.INT, true, false);
    public static final CommentReplyField REPLY_AGREE_SUM = new CommentReplyField("reply_agree_sum", ObjectFieldDBType.INT, true, false);
    public static final CommentReplyField DISPLAY_HOT = new CommentReplyField("display_hot", ObjectFieldDBType.INT, true, false);
    public static final CommentReplyField CUSTOMER_STATUS = new CommentReplyField("customer_status", ObjectFieldDBType.INT, true, false);
    public static final CommentReplyField SUBKEY = new CommentReplyField("subkey", ObjectFieldDBType.STRING, true, false);

    public CommentReplyField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public CommentReplyField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
