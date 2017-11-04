package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-24
 * Time: 上午10:20
 * To change this template use File | Settings | File Templates.
 */
public class AppContentVersionField extends AbstractObjectField {
    public static final AppContentVersionField ID = new AppContentVersionField("id", ObjectFieldDBType.LONG, false, true);

    public static final AppContentVersionField APPKEY = new AppContentVersionField("appkey", ObjectFieldDBType.STRING, false, true);
    public static final AppContentVersionField REMOVESTTAUS = new AppContentVersionField("removestatus", ObjectFieldDBType.STRING, false, true);
    public static final AppContentVersionField CURRENT_VERSION = new AppContentVersionField("curr_version", ObjectFieldDBType.LONG, false, true);
    public static final AppContentVersionField VERSION_URL = new AppContentVersionField("version_url", ObjectFieldDBType.STRING, false, true);
    public static final AppContentVersionField VERSION_INFO = new AppContentVersionField("version_info", ObjectFieldDBType.STRING, false, true);

    public static final AppContentVersionField PACKAGE_TYPE = new AppContentVersionField("package_type", ObjectFieldDBType.INT, false, true);
    public static final AppContentVersionField NECESSARY_UPDATE = new AppContentVersionField("necessary_update", ObjectFieldDBType.BOOLEAN, false, true);


    public AppContentVersionField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AppContentVersionField(String column, ObjectFieldDBType type) {
        super(column, type);
    }


    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
