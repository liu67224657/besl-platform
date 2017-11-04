package com.enjoyf.platform.service.sync;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-4
 * Time: 下午7:22
 * To change this template use File | Settings | File Templates.
 */
public class ShareTopicField extends AbstractObjectField {
    public static final ShareBodyField SHARETOPICID = new ShareBodyField("share_topic_id", ObjectFieldDBType.LONG, true, true);
    public static final ShareTopicField SHAREID = new ShareTopicField("share_id", ObjectFieldDBType.LONG, true, false);
    public static final ShareTopicField SHARETOPIC = new ShareTopicField("share_topic", ObjectFieldDBType.STRING, false, true);
    public static final ShareTopicField REMOVESTATUS = new ShareTopicField("removestatus", ObjectFieldDBType.STRING, false, true);

    public static final ShareTopicField CREATEUSERID = new ShareTopicField("createuserid", ObjectFieldDBType.STRING, false, true);
    public static final ShareTopicField CREATEDATE = new ShareTopicField("createdate", ObjectFieldDBType.TIMESTAMP, false, true);
    public static final ShareTopicField CREATEUSERIP = new ShareTopicField("createuserip", ObjectFieldDBType.STRING, false, true);

    public static final ShareTopicField LASTMODIFYUSERID = new ShareTopicField("modifyuserid", ObjectFieldDBType.STRING, false, true);
    public static final ShareTopicField LASTMODIFYDATE = new ShareTopicField("modifydate", ObjectFieldDBType.TIMESTAMP, false, true);
    public static final ShareTopicField LASTMODIFYIP = new ShareTopicField("modifyip", ObjectFieldDBType.STRING, false, true);

    public ShareTopicField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ShareTopicField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
