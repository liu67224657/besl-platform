package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-8-19
 * Time: 下午3:24
 * To change this template use File | Settings | File Templates.
 */
public class AppTipsField extends AbstractObjectField {

    public static final AppTipsField TIPS_ID = new AppTipsField("tipsid", ObjectFieldDBType.LONG, true, true);
    public static final AppTipsField TITLE = new AppTipsField("tipstitle", ObjectFieldDBType.STRING, true, false);
    public static final AppTipsField DESCRIPTION = new AppTipsField("tipsdescription", ObjectFieldDBType.STRING, true, false);
    public static final AppTipsField PIC = new AppTipsField("tipspic", ObjectFieldDBType.STRING, true, false);
    public static final AppTipsField URL = new AppTipsField("tipsurl", ObjectFieldDBType.STRING, true, false);
    public static final AppTipsField TYPE = new AppTipsField("tipstype", ObjectFieldDBType.INT, true, false);
    public static final AppTipsField REMOVE_STATUS = new AppTipsField("removestatus", ObjectFieldDBType.STRING, true, false);
    public static final AppTipsField CREATE_DATE = new AppTipsField("createdate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final AppTipsField CREATE_IP = new AppTipsField("createip", ObjectFieldDBType.STRING, true, false);
    public static final AppTipsField CREATE_USERID = new AppTipsField("createuserid", ObjectFieldDBType.STRING, true, false);
    public static final AppTipsField MODIFY_DATE = new AppTipsField("modifydate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final AppTipsField MODIFY_IP = new AppTipsField("modifyip", ObjectFieldDBType.STRING, true, false);
    public static final AppTipsField MODIFY_USERID = new AppTipsField("modifyuserid", ObjectFieldDBType.STRING, true, false);
    public static final AppTipsField APP_KEY = new AppTipsField("appid", ObjectFieldDBType.STRING, true, false);
    public static final AppTipsField PLATFORM = new AppTipsField("platform", ObjectFieldDBType.INT, true, false);
    public static final AppTipsField UPDATE_TIME = new AppTipsField("updatetime", ObjectFieldDBType.TIMESTAMP, true, false);

    public AppTipsField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AppTipsField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
