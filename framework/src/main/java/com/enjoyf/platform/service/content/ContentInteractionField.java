package com.enjoyf.platform.service.content;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 11-12-13
 * Time: 下午7:53
 * To change this template use File | Settings | File Templates.
 */
public class ContentInteractionField extends AbstractObjectField {

    public static final ContentInteractionField INTERACTIONID = new ContentInteractionField("INTERACTIONID", ObjectFieldDBType.STRING, false, true);
    public static final ContentInteractionField CONTENTUNO = new ContentInteractionField("CONTENTUNO", ObjectFieldDBType.STRING, true, false);
    public static final ContentInteractionField CONTENTID = new ContentInteractionField("CONTENTID", ObjectFieldDBType.STRING, true, false);
    public static final ContentInteractionField INTERACTIONUNO = new ContentInteractionField("INTERACTIONUNO", ObjectFieldDBType.STRING, true, false);
    public static final ContentInteractionField INTERACTIONCONTENT = new ContentInteractionField("INTERACTIONCONTENT", ObjectFieldDBType.STRING, true, false);
    public static final ContentInteractionField INTERACTIONCONTENTTYPE = new ContentInteractionField("INTERACTIONCONTENTTYPE", ObjectFieldDBType.INT, true, false);
    public static final ContentInteractionField INTERACTIONDATE = new ContentInteractionField("INTERACTIONDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ContentInteractionField REMOVESTATUS = new ContentInteractionField("REMOVESTATUS", ObjectFieldDBType.STRING, true, false);
    public static final ContentInteractionField AUDITSTATUS = new ContentInteractionField("AUDITSTATUS", ObjectFieldDBType.INT, true, false);
    public static final ContentInteractionField INTERACTIONTYPE = new ContentInteractionField("INTERACTIONTYPE", ObjectFieldDBType.STRING, true, false);
    public static final ContentInteractionField CREATEDATE = new ContentInteractionField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final ContentInteractionField FLOORNUM = new ContentInteractionField("FLOORNUM", ObjectFieldDBType.LONG, true, false);
    public static final ContentInteractionField REPLYTIMES = new ContentInteractionField("REPLYTIMES", ObjectFieldDBType.INT, true, false);

    public static final ContentInteractionField ROOTID = new ContentInteractionField("ROOTID", ObjectFieldDBType.STRING, true, false);
    public static final ContentInteractionField ROOTUNO = new ContentInteractionField("ROOTUNO", ObjectFieldDBType.STRING, true, false);

    //
    public ContentInteractionField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ContentInteractionField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
