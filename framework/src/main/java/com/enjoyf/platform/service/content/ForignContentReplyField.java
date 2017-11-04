package com.enjoyf.platform.service.content;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-1-7
 * Time: 上午11:45
 * To change this template use File | Settings | File Templates.
 */
public class ForignContentReplyField extends AbstractObjectField {

    public static final ForignContentReplyField REPLY_ID = new ForignContentReplyField("reply_id", ObjectFieldDBType.LONG, true, false);
    public static final ForignContentReplyField REPLY_UNO = new ForignContentReplyField("reply_uno", ObjectFieldDBType.STRING, true, false);
    public static final ForignContentReplyField CONTENT_ID = new ForignContentReplyField("content_id", ObjectFieldDBType.LONG, true, false);
    public static final ForignContentReplyField PARENT_ID = new ForignContentReplyField("parent_id", ObjectFieldDBType.LONG, true, false);
    public static final ForignContentReplyField ROOT_ID = new ForignContentReplyField("root_id", ObjectFieldDBType.LONG, true, false);

    public static final ForignContentReplyField REPLY_SUM = new ForignContentReplyField("reply_num", ObjectFieldDBType.INT, true, false);
    public static final ForignContentReplyField AGREE_NUM = new ForignContentReplyField("agree_num", ObjectFieldDBType.INT, true, false);
	public static final ForignContentReplyField DISAGREE_NUM = new ForignContentReplyField("disagree_num", ObjectFieldDBType.INT, true, false);


    public static final ForignContentReplyField BODY = new ForignContentReplyField("body", ObjectFieldDBType.STRING, true, false);
    public static final ForignContentReplyField PIC = new ForignContentReplyField("pic", ObjectFieldDBType.STRING, true, false);


    public static final ForignContentReplyField CREATE_TIME = new ForignContentReplyField("create_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ForignContentReplyField CREATE_IP = new ForignContentReplyField("create_ip", ObjectFieldDBType.STRING, true, false);

    public static final ForignContentReplyField REMOVE_STATUS = new ForignContentReplyField("remove_status", ObjectFieldDBType.STRING, true, false);
    public static final ForignContentReplyField KEY_WORDS = new ForignContentReplyField("key_words", ObjectFieldDBType.STRING, true, false);
    public static final ForignContentReplyField TOTAL_ROWS = new ForignContentReplyField("total_rows", ObjectFieldDBType.INT, true, false);
    //
	public static final ForignContentField SCORE = new ForignContentField("score", ObjectFieldDBType.DOUBLE, true, false);
	public static final ForignContentField DISPLAY_ORDER = new ForignContentField("display_order", ObjectFieldDBType.LONG, true, false);

    public ForignContentReplyField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ForignContentReplyField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
