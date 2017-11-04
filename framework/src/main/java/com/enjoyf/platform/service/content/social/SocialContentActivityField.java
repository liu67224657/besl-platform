package com.enjoyf.platform.service.content.social;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-13
 * Time: 下午7:54
 * To change this template use File | Settings | File Templates.
 */
public class SocialContentActivityField extends AbstractObjectField{
    public static final SocialContentActivityField SOCIAL_CONTENT_ACTIVITY_ID = new SocialContentActivityField("sca_id", ObjectFieldDBType.LONG, true, false);
    public static final SocialContentActivityField ACTIVITY_ID = new SocialContentActivityField("activity_id", ObjectFieldDBType.LONG, true, false);
    public static final SocialContentActivityField CONTENT_ID = new SocialContentActivityField("content_id", ObjectFieldDBType.LONG, true, false);
    public static final SocialContentActivityField CONTENT_UNO = new SocialContentActivityField("content_uno", ObjectFieldDBType.STRING, true, false);
    public static final SocialContentActivityField REMOVE_STATUS = new SocialContentActivityField("remove_status", ObjectFieldDBType.STRING, true, false);
    public static final SocialContentActivityField CREATE_DATE = new SocialContentActivityField("create_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final SocialContentActivityField MODIFY_DATE = new SocialContentActivityField("modify_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final SocialContentActivityField MODIFY_IP = new SocialContentActivityField("modify_ip", ObjectFieldDBType.STRING, true, false);
    public static final SocialContentActivityField MODIFY_USERID = new SocialContentActivityField("modify_userid", ObjectFieldDBType.STRING, true, false);
    public static final SocialContentActivityField DISPLAY_ORDER = new SocialContentActivityField("display_order", ObjectFieldDBType.LONG, true, false);

    public static final SocialContentActivityField HOT_RANK = new SocialContentActivityField("hot_rank", ObjectFieldDBType.INT, true, false);


    public SocialContentActivityField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public SocialContentActivityField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
