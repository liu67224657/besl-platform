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
public class SocialHotContentField extends AbstractObjectField {
    //    public static final SocialContentField REPLAY_NUM = new SocialContentField("reply_num", ObjectFieldDBType.INT, false, false);
    public static final SocialHotContentField CONTENTID = new SocialHotContentField("content_id", ObjectFieldDBType.LONG, true, true);
    public static final SocialHotContentField UNO = new SocialHotContentField("uno", ObjectFieldDBType.STRING, true, true);
    public static final SocialHotContentField DISPLAY_ORDER = new SocialHotContentField("display_order", ObjectFieldDBType.INT, true, false);

    public static final SocialHotContentField REMOVE_STATUS = new SocialHotContentField("act_status", ObjectFieldDBType.STRING, true, false);


    public SocialHotContentField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public SocialHotContentField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
