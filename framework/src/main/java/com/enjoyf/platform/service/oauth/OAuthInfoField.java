package com.enjoyf.platform.service.oauth;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-24
 * Time: 上午10:20
 * To change this template use File | Settings | File Templates.
 */
public class OAuthInfoField extends AbstractObjectField {
    public static final OAuthInfoField ACCESS_TOKEN = new OAuthInfoField("access_token", ObjectFieldDBType.STRING, false, true);

    public static final OAuthInfoField REFRESH_TOKEN = new OAuthInfoField("refresh_token", ObjectFieldDBType.STRING, false, true);

    public static final OAuthInfoField APPKEY = new OAuthInfoField("app_key", ObjectFieldDBType.STRING, false, true);

    public static final OAuthInfoField UNO = new OAuthInfoField("uno", ObjectFieldDBType.STRING, false, true);
    public static final OAuthInfoField EXPIRE_LONGTIME = new OAuthInfoField("expire_longtime", ObjectFieldDBType.LONG, false, true);

    public OAuthInfoField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public OAuthInfoField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
