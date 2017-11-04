package com.enjoyf.platform.service.social;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-5-29
 * Time: 下午5:11
 * To change this template use File | Settings | File Templates.
 */
public class SocialBlackField extends AbstractObjectField {
    public static final SocialBlackField REMOVE_STATUS = new SocialBlackField("remove_status", ObjectFieldDBType.STRING, false, false);

    public static final SocialBlackField SRCUNO = new SocialBlackField("srcuno", ObjectFieldDBType.STRING, false, false);

    public static final SocialBlackField DESUNO = new SocialBlackField("desuno", ObjectFieldDBType.STRING, false, false);

    public static final SocialBlackField UPDATE_TIME = new SocialBlackField("update_time", ObjectFieldDBType.TIMESTAMP, false, false);


    public SocialBlackField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public SocialBlackField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
