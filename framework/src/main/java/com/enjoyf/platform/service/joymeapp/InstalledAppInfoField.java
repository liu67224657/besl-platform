package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-4
 * Time: 下午12:50
 * To change this template use File | Settings | File Templates.
 */
public class InstalledAppInfoField extends AbstractObjectField {

    public static final InstalledAppInfoField CLIENTID = new InstalledAppInfoField("clientid", ObjectFieldDBType.STRING, false, true);
    public static final InstalledAppInfoField CLIENTTOKEN = new InstalledAppInfoField("clienttoken", ObjectFieldDBType.STRING, false, false);
    public static final InstalledAppInfoField APPKEY = new InstalledAppInfoField("appkey", ObjectFieldDBType.STRING, false, false);

    public static final InstalledAppInfoField INSTALLED_INFO = new InstalledAppInfoField("installed_info", ObjectFieldDBType.STRING, false, false);
    public static final InstalledAppInfoField PLATFORM = new InstalledAppInfoField("platform", ObjectFieldDBType.INT, false, false);

    public static final InstalledAppInfoField CREATETIME = new InstalledAppInfoField("create_time", ObjectFieldDBType.TIMESTAMP, false, false);

    //
    public InstalledAppInfoField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public InstalledAppInfoField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
