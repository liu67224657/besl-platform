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
public class SocialContentField extends AbstractObjectField {
    //    public static final SocialContentField REPLAY_NUM = new SocialContentField("reply_num", ObjectFieldDBType.INT, false, false);
    public static final SocialContentField REPLAY_NUM = new SocialContentField("reply_num", ObjectFieldDBType.INT, false, false);

    public static final SocialContentField PLAY_NUM = new SocialContentField("play_num", ObjectFieldDBType.INT, false, false);
    public static final SocialContentField CONTENTID = new SocialContentField("content_id", ObjectFieldDBType.LONG, true, false);

    public static final SocialContentField UNO = new SocialContentField("uno", ObjectFieldDBType.STRING, true, false);

    public static final SocialContentField AGREE_NUM = new SocialContentField("agree_num", ObjectFieldDBType.INT, true, false);
    public static final SocialContentField REMOVE_STATUS = new SocialContentField("remove_status", ObjectFieldDBType.STRING, true, false);
    public static final SocialContentField SHARE_NUM = new SocialContentField("share_num", ObjectFieldDBType.INT, true, false);


    public SocialContentField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public SocialContentField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
