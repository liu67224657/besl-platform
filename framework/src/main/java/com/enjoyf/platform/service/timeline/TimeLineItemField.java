package com.enjoyf.platform.service.timeline;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 11-12-13
 * Time: 下午7:53
 * To change this template use File | Settings | File Templates.
 */
public class TimeLineItemField extends AbstractObjectField {

    public static final TimeLineItemField TLID = new TimeLineItemField("TLID", ObjectFieldDBType.LONG, false, true);
    public static final TimeLineItemField OWNUNO = new TimeLineItemField("OWNUNO", ObjectFieldDBType.STRING, false, false);
    public static final TimeLineItemField TLDOMAIN = new TimeLineItemField("TLDOMAIN", ObjectFieldDBType.STRING, false, false);
    public static final TimeLineItemField DIRECTUNO = new TimeLineItemField("DIRECTUNO", ObjectFieldDBType.STRING, false, false);
    public static final TimeLineItemField DIRECTID = new TimeLineItemField("DIRECTID", ObjectFieldDBType.STRING, false, false);
    public static final TimeLineItemField REMOVESTATUS = new TimeLineItemField("REMOVESTATUS", ObjectFieldDBType.STRING, true, false);
    public static final TimeLineItemField SPREADTYPE = new TimeLineItemField("SPREADTYPE", ObjectFieldDBType.INT, true, false);
    public static final TimeLineItemField ITEMFAVSUM = new TimeLineItemField("ITEMFAVSUM", ObjectFieldDBType.INT, true, false);


    //
    public TimeLineItemField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public TimeLineItemField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
