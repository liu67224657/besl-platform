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
public class SocialLogField extends AbstractObjectField{
    public static final SocialLogField SAL_ID = new SocialLogField("salid", ObjectFieldDBType.LONG, true, true);
    public static final SocialLogField ACTIVITY_ID = new SocialLogField("activity_id", ObjectFieldDBType.LONG, true, true);
    public static final SocialLogField CREATE_DATE = new SocialLogField("create_date", ObjectFieldDBType.TIMESTAMP, true, false);

    public SocialLogField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public SocialLogField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
