package com.enjoyf.platform.service.ask.wiki;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class ContentField extends AbstractObjectField {
    public static final ContentField ID = new ContentField("id", ObjectFieldDBType.LONG);
    public static final ContentField COMMENT_ID = new ContentField("comment_id", ObjectFieldDBType.STRING);
    public static final ContentField TITLE = new ContentField("title", ObjectFieldDBType.STRING);
    public static final ContentField DESCRIPTION  = new ContentField("description", ObjectFieldDBType.STRING);
    public static final ContentField PIC = new ContentField("pic", ObjectFieldDBType.STRING);
    public static final ContentField AUTHOR = new ContentField("author", ObjectFieldDBType.STRING);
    public static final ContentField GAME_ID = new ContentField("game_id", ObjectFieldDBType.STRING);
    public static final ContentField PUBLISH_TIME = new ContentField("publish_time", ObjectFieldDBType.TIMESTAMP);
    public static final ContentField WEB_URL = new ContentField("web_url", ObjectFieldDBType.STRING);
    public static final ContentField SOURCE = new ContentField("source", ObjectFieldDBType.INT);
    public static final ContentField REMOVE_STATUS = new ContentField("remove_status", ObjectFieldDBType.STRING);
    public static final ContentField DISPLAY_ORDER = new ContentField("display_order", ObjectFieldDBType.DOUBLE);
    public static final ContentField CREATE_DATE = new ContentField("create_date", ObjectFieldDBType.TIMESTAMP);


    public ContentField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}