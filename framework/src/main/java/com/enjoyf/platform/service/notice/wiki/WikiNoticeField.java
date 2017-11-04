package com.enjoyf.platform.service.notice.wiki;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by ericliu on 16/3/12.
 */
public class WikiNoticeField extends AbstractObjectField {


    public static final WikiNoticeField NOTICE_ID = new WikiNoticeField("notice_id", ObjectFieldDBType.LONG);
    public static final WikiNoticeField OWN_PROFILE_ID = new WikiNoticeField("own_profile_id", ObjectFieldDBType.STRING);
    public static final WikiNoticeField DEST_PROFILE = new WikiNoticeField("dest_profile_id", ObjectFieldDBType.STRING);
    public static final WikiNoticeField DEST_ID = new WikiNoticeField("dest_id", ObjectFieldDBType.STRING);
    public static final WikiNoticeField WIKI_ID = new WikiNoticeField("wiki_id", ObjectFieldDBType.STRING);

    public static final WikiNoticeField NOTICE_TYPE = new WikiNoticeField("notice_type", ObjectFieldDBType.INT);
    public static final WikiNoticeField MESSAGE_BODY = new WikiNoticeField("message_body", ObjectFieldDBType.STRING);
    public static final WikiNoticeField REMOVE_STATUS = new WikiNoticeField("remove_status", ObjectFieldDBType.INT);
    public static final WikiNoticeField CREATE_TIME = new WikiNoticeField("create_time", ObjectFieldDBType.TIMESTAMP);


    public WikiNoticeField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public WikiNoticeField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
