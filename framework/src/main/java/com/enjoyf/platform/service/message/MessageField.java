package com.enjoyf.platform.service.message;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-6-1
 * Time: 下午6:51
 * To change this template use File | Settings | File Templates.
 */
public class MessageField extends AbstractObjectField {

    public static final MessageField MSGID = new MessageField("MSGID", ObjectFieldDBType.LONG, true, true);
    public static final MessageField TOPICID = new MessageField("TOPICID", ObjectFieldDBType.LONG, true, true);

    public static final MessageField MSGTYPE = new MessageField("MSGTYPE", ObjectFieldDBType.STRING, true, false);

    public static final MessageField OWNUNO = new MessageField("OWNUNO", ObjectFieldDBType.STRING, true, false);
    public static final MessageField SENDERUNO = new MessageField("SENDERUNO", ObjectFieldDBType.STRING, true, false);
    public static final MessageField RECIEVERUNO = new MessageField("RECIEVERUNO", ObjectFieldDBType.STRING, true, false);

    public static final MessageField REMOVESTATUS = new MessageField("REMOVESTATUS", ObjectFieldDBType.STRING, true, false);
    public static final MessageField READSTATUS = new MessageField("READSTATUS", ObjectFieldDBType.STRING, true, false);


    //
    public MessageField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public MessageField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
