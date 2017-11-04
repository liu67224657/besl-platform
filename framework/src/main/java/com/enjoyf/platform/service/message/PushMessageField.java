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
public class PushMessageField extends AbstractObjectField {

    public static final PushMessageField PUSHMSGID = new PushMessageField("PUSHMSGID", ObjectFieldDBType.LONG, true, true);
    public static final PushMessageField PUSHMSGTYPE = new PushMessageField("PUSHMSGTYPE", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField PUSHMSGCODE = new PushMessageField("PUSHMSGCODE", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField MSGBODY = new PushMessageField("MSGBODY", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField MSGOPTIONS = new PushMessageField("MSGOPTIONS", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField SENDDATE = new PushMessageField("SENDDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final PushMessageField SENDSTATUS = new PushMessageField("SENDSTATUS", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField REMOVESTATUS = new PushMessageField("REMOVESTATUS", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField REMOVEDATE = new PushMessageField("REMOVEDATE", ObjectFieldDBType.TIMESTAMP, true, false);


    //
    public PushMessageField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public PushMessageField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
