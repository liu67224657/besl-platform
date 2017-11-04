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
public class SocialActivityField extends AbstractObjectField{

    public static final SocialActivityField ACTIVITY_ID = new SocialActivityField("activity_id", ObjectFieldDBType.LONG, true, true);
    public static final SocialActivityField TITLE = new SocialActivityField("title", ObjectFieldDBType.STRING, true, false);
    public static final SocialActivityField DESCRIPTION = new SocialActivityField("description", ObjectFieldDBType.STRING, true, false);
    public static final SocialActivityField IOS_ICON = new SocialActivityField("ios_icon", ObjectFieldDBType.STRING, true, false);
    public static final SocialActivityField IOS_SMALL_PIC = new SocialActivityField("ios_small_pic", ObjectFieldDBType.STRING, true, false);
    public static final SocialActivityField IOS_BIG_PIC = new SocialActivityField("ios_big_pic", ObjectFieldDBType.STRING, true, false);
    public static final SocialActivityField ANDROID_ICON = new SocialActivityField("android_icon", ObjectFieldDBType.STRING, true, false);
    public static final SocialActivityField ANDROID_SMALL_PIC = new SocialActivityField("android_small_pic", ObjectFieldDBType.STRING, true, false);
    public static final SocialActivityField ANDROID_BIG_PIC = new SocialActivityField("android_big_pic", ObjectFieldDBType.STRING, true, false);
    public static final SocialActivityField DISPLAY_ORDER = new SocialActivityField("display_order", ObjectFieldDBType.INT, true, false);
    public static final SocialActivityField USE_SUM = new SocialActivityField("use_sum", ObjectFieldDBType.INT, true, false);
    public static final SocialActivityField REPLY_SUM = new SocialActivityField("reply_sum", ObjectFieldDBType.INT, true, false);
    public static final SocialActivityField AGREE_SUM = new SocialActivityField("agree_sum", ObjectFieldDBType.INT, true, false);
    public static final SocialActivityField GIFT_SUM = new SocialActivityField("gift_sum", ObjectFieldDBType.INT, true, false);
    public static final SocialActivityField REMOVE_STATUS = new SocialActivityField("remove_status", ObjectFieldDBType.STRING, true, false);
    public static final SocialActivityField SUBSCRIPT = new SocialActivityField("subscript", ObjectFieldDBType.STRING, true, false);
    public static final SocialActivityField SHARE_ID = new SocialActivityField("share_id", ObjectFieldDBType.LONG, true, false);
    public static final SocialActivityField JSON_AWARD = new SocialActivityField("awards", ObjectFieldDBType.STRING, true, false);
    public static final SocialActivityField CREATE_DATE = new SocialActivityField("create_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final SocialActivityField CREATE_USERID = new SocialActivityField("create_userid", ObjectFieldDBType.STRING, true, false);
    public static final SocialActivityField MODIFY_DATE = new SocialActivityField("modify_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final SocialActivityField MODIFY_IP = new SocialActivityField("modify_ip", ObjectFieldDBType.STRING, true, false);
    public static final SocialActivityField MODIFY_USERID = new SocialActivityField("modify_userid", ObjectFieldDBType.STRING, true, false);
    public static final SocialActivityField BIND_STATUS = new SocialActivityField("bind_status", ObjectFieldDBType.STRING, true, false);
    public static final SocialActivityField SUBSCRIPT_TYPE = new SocialActivityField("subscript_type", ObjectFieldDBType.INT, true, false);
    public static final SocialActivityField TOTALS = new SocialActivityField("totals", ObjectFieldDBType.INT, true, false);
    public static final SocialActivityField RETYPE = new SocialActivityField("retype", ObjectFieldDBType.INT, true, false);
    public static final SocialActivityField REURL = new SocialActivityField("reurl", ObjectFieldDBType.STRING, true, false);

    public SocialActivityField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public SocialActivityField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
