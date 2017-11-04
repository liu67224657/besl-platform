package com.enjoyf.platform.service.message;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-6
 * Time: 下午4:26
 * To change this template use File | Settings | File Templates.
 */
public class SocialNoticeField extends AbstractObjectField {

    public static final SocialNoticeField NOTICE_ID = new SocialNoticeField("noticeid", ObjectFieldDBType.LONG, true, true);
    public static final SocialNoticeField OWN_UNO = new SocialNoticeField("ownuno", ObjectFieldDBType.STRING, true, false);
    public static final SocialNoticeField DESCRIPTION = new SocialNoticeField("description", ObjectFieldDBType.STRING, true, false);
    public static final SocialNoticeField AGREE_COUNT = new SocialNoticeField("agreecount", ObjectFieldDBType.INT, true, false);
    public static final SocialNoticeField REPLY_COUNT = new SocialNoticeField("replycount", ObjectFieldDBType.INT, true, false);
    public static final SocialNoticeField NOTICE_COUNT = new SocialNoticeField("noticecount", ObjectFieldDBType.INT, true, false);
    public static final SocialNoticeField HOT_COUNT = new SocialNoticeField("hotcount", ObjectFieldDBType.INT, true, false);
    public static final SocialNoticeField FOCUS_COUNT = new SocialNoticeField("focuscount", ObjectFieldDBType.INT, true, false);
    public static final SocialNoticeField REMOVE_STATUS = new SocialNoticeField("removestatus", ObjectFieldDBType.STRING, true, false);
    public static final SocialNoticeField CREATE_DATE = new SocialNoticeField("createdate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final SocialNoticeField READ_AGREE_DATE = new SocialNoticeField("readagreedate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final SocialNoticeField READ_REPLY_DATE = new SocialNoticeField("readreplydate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final SocialNoticeField READ_NOTICE_DATE = new SocialNoticeField("readnoticedate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final SocialNoticeField READ_HOT_DATE = new SocialNoticeField("readhotdate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final SocialNoticeField READ_FOCUS_DATE = new SocialNoticeField("readfocusdate", ObjectFieldDBType.TIMESTAMP, true, false);

    public SocialNoticeField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public SocialNoticeField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
