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
public class ShareBodyField extends AbstractObjectField {
    public static final ShareBodyField SHAREBODYID = new ShareBodyField("share_body_id", ObjectFieldDBType.LONG, true, true);
    public static final ShareBodyField SHAREID = new ShareBodyField("share_id", ObjectFieldDBType.LONG, true, false);
    public static final ShareBodyField SHARESUBJECT = new ShareBodyField("share_subject", ObjectFieldDBType.STRING, false, true);
    public static final ShareBodyField SHAREBODY = new ShareBodyField("share_body", ObjectFieldDBType.STRING, false, true);
     public static final ShareBodyField PIC_URL = new ShareBodyField("pic_url", ObjectFieldDBType.STRING, false, true);

    public static final ShareBodyField REMOVESTATUS = new ShareBodyField("removestatus", ObjectFieldDBType.STRING, false, true);
    public static final ShareBodyField CREATEUSERID = new ShareBodyField("createuserid", ObjectFieldDBType.STRING, false, true);
    public static final ShareBodyField CREATEDATE = new ShareBodyField("createdate", ObjectFieldDBType.TIMESTAMP, false, true);
    public static final ShareBodyField CREATEUSERIP = new ShareBodyField("createuserip", ObjectFieldDBType.STRING, false, true);

    public static final ShareBodyField LASTMODIFYUSERID = new ShareBodyField("modifyuserid", ObjectFieldDBType.STRING, false, true);
    public static final ShareBodyField LASTMODIFYDATE = new ShareBodyField("modifydate", ObjectFieldDBType.TIMESTAMP, false, true);
    public static final ShareBodyField LASTMODIFYIP = new ShareBodyField("modifyip", ObjectFieldDBType.STRING, false, true);

    public ShareBodyField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ShareBodyField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
