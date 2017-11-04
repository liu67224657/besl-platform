package com.enjoyf.platform.service.timeline;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-14
 * Time: 下午1:57
 * SocialTimeLineItem
 */
public class SocialTimeLineItemField extends AbstractObjectField {

    public static final SocialTimeLineItemField SID = new SocialTimeLineItemField("sid", ObjectFieldDBType.LONG, false, true);
    public static final SocialTimeLineItemField DOMAIN = new SocialTimeLineItemField("domain", ObjectFieldDBType.STRING, false, true);
    public static final SocialTimeLineItemField REMOVE_STATUS = new SocialTimeLineItemField("remove_status", ObjectFieldDBType.STRING, false, true);
    public static final SocialTimeLineItemField OWN_UNO = new SocialTimeLineItemField("own_uno", ObjectFieldDBType.STRING, false, true);
    public static final SocialTimeLineItemField CONTENT_ID = new SocialTimeLineItemField("content_id", ObjectFieldDBType.STRING, true, false);
    public static final SocialTimeLineItemField CRAETE_TIME = new SocialTimeLineItemField("create_time", ObjectFieldDBType.TIMESTAMP, true, false);

    public SocialTimeLineItemField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public SocialTimeLineItemField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
