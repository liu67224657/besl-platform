package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-12
 * Time: 下午2:51
 * To change this template use File | Settings | File Templates.
 */
public class SocialProfileRecommendField extends AbstractObjectField {
    //    public static final SocialContentField REPLAY_NUM = new SocialContentField("reply_num", ObjectFieldDBType.INT, false, false);
    public static final SocialProfileRecommendField SOCIAL_RECOMMEND_ID = new SocialProfileRecommendField("social_recommend_id", ObjectFieldDBType.LONG, true, true);
    public static final SocialProfileRecommendField UNO = new SocialProfileRecommendField("uno", ObjectFieldDBType.STRING, true, true);
    public static final SocialProfileRecommendField DISPLAY_ORDER = new SocialProfileRecommendField("display_order", ObjectFieldDBType.INT, true, false);

    public static final SocialProfileRecommendField REMOVE_STATUS = new SocialProfileRecommendField("act_status", ObjectFieldDBType.STRING, true, false);
    public static final SocialProfileRecommendField RECOMMEND_TYPE = new SocialProfileRecommendField("recomtype", ObjectFieldDBType.INT, true, false);
    public static final SocialProfileRecommendField MODIFY_DATE = new SocialProfileRecommendField("modifydate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final SocialProfileRecommendField MODIFY_USERID = new SocialProfileRecommendField("modifyuserid", ObjectFieldDBType.STRING, true, false);

    public SocialProfileRecommendField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public SocialProfileRecommendField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
