package com.enjoyf.platform.service.joymeapp.config;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 15-3-3
 * Time: 下午6:53
 * To change this template use File | Settings | File Templates.
 */
public class AppConfigField extends AbstractObjectField {

    public static final AppConfigField CONFIGID = new AppConfigField("configid", ObjectFieldDBType.STRING, true, true);
    public static final AppConfigField APPKEY = new AppConfigField("appkey", ObjectFieldDBType.STRING, true, false);
    public static final AppConfigField PLATFORM = new AppConfigField("platform", ObjectFieldDBType.INT, true, false);
    public static final AppConfigField VERSION = new AppConfigField("version", ObjectFieldDBType.STRING, true, false);
    public static final AppConfigField CHANNEL = new AppConfigField("channel", ObjectFieldDBType.STRING, true, false);
    public static final AppConfigField ENTERPRISE = new AppConfigField("enterprise", ObjectFieldDBType.INT, true, false);
    public static final AppConfigField APPINFO = new AppConfigField("appinfo", ObjectFieldDBType.STRING, true, false);
    public static final AppConfigField CREATEDATE = new AppConfigField("createdate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final AppConfigField CREATEUSERID = new AppConfigField("createuserid", ObjectFieldDBType.STRING, true, false);
    public static final AppConfigField MODIFYDATE = new AppConfigField("modifydate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final AppConfigField MODIFYUSERID = new AppConfigField("modifyuserid", ObjectFieldDBType.STRING, true, false);
    public static final AppConfigField APPSECRET = new AppConfigField("appsecret", ObjectFieldDBType.STRING, true, false);
    public static final AppConfigField BUCKET = new AppConfigField("bucket", ObjectFieldDBType.STRING, true, false);



    public AppConfigField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AppConfigField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
