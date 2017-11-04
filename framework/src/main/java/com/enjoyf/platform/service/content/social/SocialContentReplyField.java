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
public class SocialContentReplyField extends AbstractObjectField {

    public static final SocialContentReplyField CONTENT_ID = new SocialContentReplyField("content_id", ObjectFieldDBType.LONG, true, false);
    public static final SocialContentReplyField REPLY_ID = new SocialContentReplyField("reply_id", ObjectFieldDBType.LONG, true, false);
    public static final SocialContentReplyField REPLY_UNO = new SocialContentReplyField("reply_uno", ObjectFieldDBType.STRING, true, false);
    public static final SocialContentReplyField BODY = new SocialContentReplyField("body", ObjectFieldDBType.STRING, true, false);


    public static final SocialContentReplyField REMOVE_STATUS = new SocialContentReplyField("remove_status", ObjectFieldDBType.STRING, true, false);


    public SocialContentReplyField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public SocialContentReplyField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
