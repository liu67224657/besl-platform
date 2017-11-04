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
public class ForignContentReplyLogField extends AbstractObjectField {

    public static final ForignContentReplyLogField LOG_ID = new ForignContentReplyLogField("log_id", ObjectFieldDBType.LONG, true, false);
    public static final ForignContentReplyLogField UNO = new ForignContentReplyLogField("uno", ObjectFieldDBType.STRING, true, false);
    public static final ForignContentReplyLogField CONTENT_ID = new ForignContentReplyLogField("content_id", ObjectFieldDBType.LONG, true, false);
    public static final ForignContentReplyLogField REPLY_ID = new ForignContentReplyLogField("reply_id", ObjectFieldDBType.LONG, true, false);
    public static final ForignContentReplyLogField LOG_TYPE = new ForignContentReplyLogField("log_type", ObjectFieldDBType.INT, true, false);

    public static final ForignContentReplyLogField CREATE_DATE = new ForignContentReplyLogField("create_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ForignContentReplyLogField CREATE_IP = new ForignContentReplyLogField("create_ip", ObjectFieldDBType.STRING, true, false);

    public ForignContentReplyLogField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ForignContentReplyLogField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
