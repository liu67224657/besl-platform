package com.enjoyf.platform.service.content.social;

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
public class SocialContentActionField extends AbstractObjectField {

    public static final SocialContentActionField CONTENT_ID = new SocialContentActionField("content_id", ObjectFieldDBType.LONG, true, false);
    public static final SocialContentActionField ACTION_TYPE = new SocialContentActionField("action_type", ObjectFieldDBType.INT, true, false);
    public static final SocialContentActionField UNO = new SocialContentActionField("action_uno", ObjectFieldDBType.STRING, true, false);
    public static final SocialContentActionField ACTION_ID = new SocialContentActionField("action_id", ObjectFieldDBType.LONG, true, false);
    public static final SocialContentActionField REMOVE_STATUS = new SocialContentActionField("remove_status", ObjectFieldDBType.STRING, true, false);
     public static final SocialContentActionField CREATE_TIME = new SocialContentActionField("create_time", ObjectFieldDBType.TIMESTAMP, true, false);

    public SocialContentActionField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public SocialContentActionField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
