package com.enjoyf.platform.service.ask.wiki;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class ContentLineField extends AbstractObjectField {
    public static final ContentLineField ID = new ContentLineField("id", ObjectFieldDBType.LONG);
    public static final ContentLineField LINE_KEY = new ContentLineField("line_key", ObjectFieldDBType.STRING);
    public static final ContentLineField OWN_ID = new ContentLineField("own_id", ObjectFieldDBType.INT);
    public static final ContentLineField LINE_TYPE = new ContentLineField("line_type", ObjectFieldDBType.INT);
    public static final ContentLineField DEST_ID = new ContentLineField("dest_id", ObjectFieldDBType.LONG);
    public static final ContentLineField SCORE = new ContentLineField("score", ObjectFieldDBType.DOUBLE);
    public static final ContentLineField CREATE_TIME = new ContentLineField("create_time", ObjectFieldDBType.TIMESTAMP);
    public static final ContentLineField VALID_STATUS = new ContentLineField("valid_status", ObjectFieldDBType.STRING);


    public ContentLineField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}