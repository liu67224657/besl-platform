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
public class TimeLineItemDetailField extends AbstractObjectField {

    public static final TimeLineItemDetailField TLID = new TimeLineItemDetailField("TLID", ObjectFieldDBType.LONG, false, true);
    public static final TimeLineItemDetailField DETAILUNO = new TimeLineItemDetailField("DETAILUNO", ObjectFieldDBType.STRING, false, false);
    public static final TimeLineItemDetailField OWNUNO = new TimeLineItemDetailField("OWNUNO", ObjectFieldDBType.STRING, false, false);
    public static final TimeLineItemDetailField TLDOMAIN = new TimeLineItemDetailField("TLDOMAIN", ObjectFieldDBType.STRING, false, false);
    public static final TimeLineItemDetailField DIRECTUNO = new TimeLineItemDetailField("DIRECTUNO", ObjectFieldDBType.STRING, false, false);
    public static final TimeLineItemDetailField DIRECTID = new TimeLineItemDetailField("DIRECTID", ObjectFieldDBType.STRING, false, false);
    public static final TimeLineItemDetailField REMOVESTATUS = new TimeLineItemDetailField("REMOVESTATUS", ObjectFieldDBType.STRING, true, false);
    public static final TimeLineItemDetailField CREATEDATE = new TimeLineItemDetailField("CREATEDATE", ObjectFieldDBType.TIMESTAMP, false, false);
    public static final TimeLineItemDetailField REMOVEDATE = new TimeLineItemDetailField("REMOVEDATE", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final TimeLineItemDetailField DETAILTYPE = new TimeLineItemDetailField("DETAILTYPE", ObjectFieldDBType.STRING, true, false);

    //
    public TimeLineItemDetailField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public TimeLineItemDetailField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
