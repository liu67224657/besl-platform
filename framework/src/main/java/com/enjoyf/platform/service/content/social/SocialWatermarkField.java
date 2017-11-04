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
public class SocialWatermarkField extends AbstractObjectField{

    public static final SocialWatermarkField WATERMARK_ID = new SocialWatermarkField("watermark_id", ObjectFieldDBType.LONG, true, true);
    public static final SocialWatermarkField ACTIVITY_ID = new SocialWatermarkField("activity_id", ObjectFieldDBType.LONG, true, false);
    public static final SocialWatermarkField TITLE = new SocialWatermarkField("title", ObjectFieldDBType.STRING, true, false);
    public static final SocialWatermarkField DESCRIPTION = new SocialWatermarkField("description", ObjectFieldDBType.STRING, true, false);
    public static final SocialWatermarkField IOS_ICON = new SocialWatermarkField("ios_icon", ObjectFieldDBType.STRING, true, false);
    public static final SocialWatermarkField ANDROID_ICON = new SocialWatermarkField("android_icon", ObjectFieldDBType.STRING, true, false);
    public static final SocialWatermarkField IOS_PIC = new SocialWatermarkField("ios_pic", ObjectFieldDBType.STRING, true, false);
    public static final SocialWatermarkField ANDROID_PIC = new SocialWatermarkField("android_pic", ObjectFieldDBType.STRING, true, false);
    public static final SocialWatermarkField DISPLAY_ORDER = new SocialWatermarkField("display_order", ObjectFieldDBType.INT, true, false);
    public static final SocialWatermarkField USE_SUM = new SocialWatermarkField("use_sum", ObjectFieldDBType.INT, true, false);
    public static final SocialWatermarkField REMOVE_STATUS = new SocialWatermarkField("remove_status", ObjectFieldDBType.STRING, true, false);
    public static final SocialWatermarkField SUBSCRIPT = new SocialWatermarkField("subscript", ObjectFieldDBType.STRING, true, false);
    public static final SocialWatermarkField CREATE_DATE = new SocialWatermarkField("create_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final SocialWatermarkField CREATE_USERID = new SocialWatermarkField("create_userid", ObjectFieldDBType.STRING, true, false);
    public static final SocialWatermarkField MODIFY_DATE = new SocialWatermarkField("modify_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final SocialWatermarkField MODIFY_IP = new SocialWatermarkField("modify_ip", ObjectFieldDBType.STRING, true, false);
    public static final SocialWatermarkField MODIFY_USERID = new SocialWatermarkField("modify_userid", ObjectFieldDBType.STRING, true, false);
    public static final SocialWatermarkField SUBSCRIPT_TYPE = new SocialWatermarkField("subscript_type", ObjectFieldDBType.INT, true, false);

    public SocialWatermarkField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public SocialWatermarkField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
