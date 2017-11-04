package com.enjoyf.platform.service.joymeapp;

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

    public static final PushMessageField PUSHMSGID = new PushMessageField("pushmsgid", ObjectFieldDBType.LONG, true, true);

    public static final PushMessageField APPKEY = new PushMessageField("appkey", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField PLATFORM = new PushMessageField("platform", ObjectFieldDBType.INT, true, false);
    public static final PushMessageField MSGICON = new PushMessageField("msgicon", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField MSGSUBJECT = new PushMessageField("msgsubject", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField SHORTMESSAGE = new PushMessageField("shortmessage", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField OPTIONS = new PushMessageField("options", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField REMOVESTATUS = new PushMessageField("removestatus", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField PUSHSTATUS = new PushMessageField("pushstatus", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField CREATEUSERID = new PushMessageField("createuserid", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField CREATEDATE = new PushMessageField("createdate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final PushMessageField SENDDATE = new PushMessageField("senddate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final PushMessageField PUSHLISTTYPE = new PushMessageField("pushlisttype", ObjectFieldDBType.INT, true, false);
    //unos,pushchannel,appchannel,appversion,tags,devices,msgsound,msgbadge
    public static final PushMessageField UNOS = new PushMessageField("unos", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField PUSH_CHANNEL = new PushMessageField("pushchannel", ObjectFieldDBType.INT, true, false);
    public static final PushMessageField CHANNEL_TYPE = new PushMessageField("appchannel", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField VERSION = new PushMessageField("appversion", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField TAGS = new PushMessageField("tags", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField DEVICES = new PushMessageField("devices", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField SOUND = new PushMessageField("msgsound", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField BADGE = new PushMessageField("msgbadge", ObjectFieldDBType.INT, true, false);
    public static final PushMessageField MODIFY_DATE = new PushMessageField("modifydate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final PushMessageField MODIFY_USER_ID = new PushMessageField("modifyuserid", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField SEND_STATUS = new PushMessageField("sendstatus", ObjectFieldDBType.STRING, true, false);
	public static final PushMessageField E_SEND_STATUS = new PushMessageField("e_sendstatus", ObjectFieldDBType.STRING, true, false);
    public static final PushMessageField ENTERPRISE_TYPE = new PushMessageField("enterprisetype", ObjectFieldDBType.INT, true, false);


    //
    public PushMessageField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public PushMessageField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
