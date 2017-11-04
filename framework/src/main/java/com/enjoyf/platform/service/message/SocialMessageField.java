package com.enjoyf.platform.service.message;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-5-27
 * Time: 下午3:40
 * To change this template use File | Settings | File Templates.
 */
public class SocialMessageField extends AbstractObjectField {
    //msgbody msgtype  msgcategory  ownuno  senduno receiveuno contentid contentuno replyid replyuno parentid parentuno rootid rootuno removestatus  createdate createip createuserid
    public static final SocialMessageField MSG_ID = new SocialMessageField("msgid", ObjectFieldDBType.LONG, true, true);
    public static final SocialMessageField MSG_BODY = new SocialMessageField("msgbody", ObjectFieldDBType.STRING, true, true);
    public static final SocialMessageField MSG_TYPE = new SocialMessageField("msgtype", ObjectFieldDBType.INT, true, true);
    public static final SocialMessageField MSG_CATEGORY = new SocialMessageField("msgcategory", ObjectFieldDBType.INT, true, true);
    public static final SocialMessageField OWN_UNO = new SocialMessageField("ownuno", ObjectFieldDBType.STRING, true, true);
    public static final SocialMessageField SEND_UNO = new SocialMessageField("senduno", ObjectFieldDBType.STRING, true, true);
    public static final SocialMessageField RECEIVE_UNO = new SocialMessageField("receiveuno", ObjectFieldDBType.STRING, true, true);
    public static final SocialMessageField CONTENT_ID = new SocialMessageField("contentid", ObjectFieldDBType.LONG, true, true);
    public static final SocialMessageField CONTENT_UNO = new SocialMessageField("contentuno", ObjectFieldDBType.STRING, true, true);
    public static final SocialMessageField REPLY_ID = new SocialMessageField("replyid", ObjectFieldDBType.LONG, true, true);
    public static final SocialMessageField REPLY_UNO = new SocialMessageField("replyuno", ObjectFieldDBType.STRING, true, true);
    public static final SocialMessageField PARENT_ID = new SocialMessageField("parentid", ObjectFieldDBType.LONG, true, true);
    public static final SocialMessageField PARENT_UNO = new SocialMessageField("parentuno", ObjectFieldDBType.STRING, true, true);
    public static final SocialMessageField ROOT_ID = new SocialMessageField("rootid", ObjectFieldDBType.LONG, true, true);
    public static final SocialMessageField ROOT_UNO = new SocialMessageField("rootuno", ObjectFieldDBType.STRING, true, true);
    public static final SocialMessageField READ_STATUS = new SocialMessageField("readstatus", ObjectFieldDBType.STRING, true, true);
    public static final SocialMessageField READ_TIME = new SocialMessageField("readtime", ObjectFieldDBType.TIMESTAMP, true, true);
    public static final SocialMessageField PUBLISH_STATUS = new SocialMessageField("pubstatus", ObjectFieldDBType.STRING, true, false);
    public static final SocialMessageField PUBLISH_TIME = new SocialMessageField("pubtime", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final SocialMessageField REMOVE_STATUS = new SocialMessageField("removestatus", ObjectFieldDBType.STRING, true, false);
    public static final SocialMessageField CREATE_DATE = new SocialMessageField("createdate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final SocialMessageField MODIFY_DATE = new SocialMessageField("modifydate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final SocialMessageField MODIFY_IP = new SocialMessageField("modifyip", ObjectFieldDBType.STRING, true, false);
    public static final SocialMessageField MODIFY_USERID = new SocialMessageField("modifyuserid", ObjectFieldDBType.STRING, true, false);
    public static final SocialMessageField CREATE_IP = new SocialMessageField("createip", ObjectFieldDBType.STRING, true, false);
    public static final SocialMessageField CREATE_USERID = new SocialMessageField("createuserid", ObjectFieldDBType.STRING, true, false);

    public SocialMessageField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public SocialMessageField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
