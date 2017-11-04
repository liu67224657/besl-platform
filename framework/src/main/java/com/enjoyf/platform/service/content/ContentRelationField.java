package com.enjoyf.platform.service.content;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-1-7
 * Time: 上午11:45
 * To change this template use File | Settings | File Templates.
 */
public class ContentRelationField extends AbstractObjectField {

    public static final ContentInteractionField CONTENTID = new ContentInteractionField("CONTENTID", ObjectFieldDBType.STRING, true, false);
    public static final ContentInteractionField RELATIONTYPE = new ContentInteractionField("RELATIONTYPE", ObjectFieldDBType.STRING, true, false);

    public static final ContentInteractionField RELATIONID = new ContentInteractionField("RELATIONID", ObjectFieldDBType.STRING, true, false);
    public static final ContentInteractionField RELATIONVALUE = new ContentInteractionField("RELATIONVALUE", ObjectFieldDBType.STRING, true, false);

    public static final ContentInteractionField REMOVESTATUS = new ContentInteractionField("REMOVESTATUS", ObjectFieldDBType.STRING, true, false);
    public static final ContentInteractionField CREATEDATE = new ContentInteractionField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);


    //
    public ContentRelationField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ContentRelationField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
