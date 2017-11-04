package com.enjoyf.platform.service.ask.wiki;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class ContentSumField extends AbstractObjectField {
    public static final ContentSumField ID = new ContentSumField("id", ObjectFieldDBType.LONG);
    public static final ContentSumField AGREE_NUM = new ContentSumField("agree_num", ObjectFieldDBType.INT);
    public static final ContentSumField CREATEDATE = new ContentSumField("create_date", ObjectFieldDBType.TIMESTAMP);


    public ContentSumField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}