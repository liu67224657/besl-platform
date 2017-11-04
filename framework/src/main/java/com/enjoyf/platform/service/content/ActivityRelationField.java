package com.enjoyf.platform.service.content;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-12
 * Time: 下午2:51
 * To change this template use File | Settings | File Templates.
 */
public class ActivityRelationField extends AbstractObjectField {
    public static final ActivityRelationField ACTIVITY_RELATION_ID = new ActivityRelationField("activity_relation_id", ObjectFieldDBType.LONG, false, false);
    public static final ActivityRelationField ACTIVITY_ID = new ActivityRelationField("activity_id", ObjectFieldDBType.LONG, true, false);
    public static final ActivityRelationField ACTIVITY_TYPE = new ActivityRelationField("activity_type", ObjectFieldDBType.INT, true, false);
    public static final ActivityRelationField ACTIVITY_DIRECTID = new ActivityRelationField("activity_directid", ObjectFieldDBType.LONG, true, false);
    public static final ActivityRelationField ACTIVITY_SUBJECT = new ActivityRelationField("activity_subject", ObjectFieldDBType.STRING, true, false);
    public static final ActivityRelationField ACTIVITY_DESC = new ActivityRelationField("activity_desc", ObjectFieldDBType.INT, true, false);
    public static final ActivityRelationField ACTIVITY_PIC = new ActivityRelationField("activity_pic", ObjectFieldDBType.STRING, true, false);
    public static final ActivityRelationField ACTIVITY_LINK = new ActivityRelationField("activity_link", ObjectFieldDBType.STRING, true, false);
    public static final ActivityRelationField ACTIVITY_PIC1 = new ActivityRelationField("activity_pic1", ObjectFieldDBType.STRING, true, false);
    public static final ActivityRelationField ACTIVITY_PIC2 = new ActivityRelationField("activity_pic2", ObjectFieldDBType.STRING, true, false);

    public static final ActivityRelationField DISPLAY_ORDER = new ActivityRelationField("display_order", ObjectFieldDBType.STRING, true, false);
    public static final ActivityRelationField CREATEUSERID = new ActivityRelationField("createuserid", ObjectFieldDBType.STRING, true, false);
    public static final ActivityRelationField CREATETIME = new ActivityRelationField("createtime", ObjectFieldDBType.DOUBLE, true, false);
    public static final ActivityRelationField CREATEIP = new ActivityRelationField("createip", ObjectFieldDBType.STRING, true, false);

    public static final ActivityRelationField LASTMODIFYUSERID = new ActivityRelationField("lastmodifyuserid", ObjectFieldDBType.DOUBLE, true, false);
    public static final ActivityRelationField LASTMODIFYTIME = new ActivityRelationField("lastmodifytime", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ActivityRelationField LASTMODIFYIP = new ActivityRelationField("lastmodifyip", ObjectFieldDBType.TIMESTAMP, true, false);

    public static final ActivityRelationField REMOVE_STATUS = new ActivityRelationField("remove_status", ObjectFieldDBType.STRING, true, false);

    public ActivityRelationField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ActivityRelationField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
