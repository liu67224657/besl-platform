package com.enjoyf.platform.service.ask.wiki;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class ContentTagField extends AbstractObjectField {
    public static final ContentTagField ID = new ContentTagField("id", ObjectFieldDBType.LONG);
    public static final ContentTagField NAME = new ContentTagField("name", ObjectFieldDBType.STRING);
    public static final ContentTagField TAGTYPE = new ContentTagField("tag_type", ObjectFieldDBType.INT);
    public static final ContentTagField DISPLAYORDER = new ContentTagField("display_order", ObjectFieldDBType.LONG);
    public static final ContentTagField VALIDSTATUS = new ContentTagField("valid_status", ObjectFieldDBType.STRING);
    public static final ContentTagField CREATEDATE = new ContentTagField("create_date", ObjectFieldDBType.TIMESTAMP);

    public static final ContentTagField TAGLINE = new ContentTagField("tag_line", ObjectFieldDBType.STRING);

    public ContentTagField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}