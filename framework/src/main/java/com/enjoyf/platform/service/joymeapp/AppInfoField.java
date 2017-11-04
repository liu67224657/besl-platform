package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-24
 * Time: 上午10:20
 * To change this template use File | Settings | File Templates.
 */
public class AppInfoField extends AbstractObjectField {
    public static final AppInfoField INFO_ID = new AppInfoField("info_id", ObjectFieldDBType.LONG, false, true);

    public static final AppInfoField APP_NAME = new AppInfoField("app_name", ObjectFieldDBType.STRING, false, true);
    public static final AppInfoField APP_KEY = new AppInfoField("app_key", ObjectFieldDBType.STRING, false, true);
    public static final AppInfoField PLATFORM = new AppInfoField("platform", ObjectFieldDBType.INT, false, true);
    public static final AppInfoField APP_TYPE = new AppInfoField("app_type", ObjectFieldDBType.INT, false, true);
    public static final AppInfoField APP_PACKAGENAME = new AppInfoField("app_packagename", ObjectFieldDBType.STRING, false, true);

    public static final AppInfoField RECOMMEND_NUM = new AppInfoField("recommend_num", ObjectFieldDBType.INT, false, true);
    public static final AppInfoField CREATE_TIME = new AppInfoField("create_time", ObjectFieldDBType.TIMESTAMP, false, true);
    public static final AppInfoField CREATE_USERID = new AppInfoField("create_userid", ObjectFieldDBType.STRING, false, true);
    public static final AppInfoField CREATE_IP = new AppInfoField("create_ip", ObjectFieldDBType.STRING, false, true);
    public static final AppInfoField APP_ISSEARCH = new AppInfoField("app_issearch", ObjectFieldDBType.BOOLEAN, false, true);
    public static final AppInfoField APP_ISCOMPLETE = new AppInfoField("app_iscomplete", ObjectFieldDBType.BOOLEAN, false, true);
    public static final AppInfoField VERSION = new AppInfoField("version", ObjectFieldDBType.STRING, false, true);
    public static final AppInfoField CHANNEL_TYPE = new AppInfoField("channel", ObjectFieldDBType.STRING, false, true);
    public static final AppInfoField HAS_GIFT = new AppInfoField("hasgift", ObjectFieldDBType.BOOLEAN, false, true);

    public static final AppInfoField MODIFY_DATE = new AppInfoField("modifydate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final AppInfoField MODIFY_IP = new AppInfoField("modifyip", ObjectFieldDBType.STRING, true, false);
    public static final AppInfoField MODIFY_USERID = new AppInfoField("modifyuserid", ObjectFieldDBType.STRING, true, false);
    public static final AppInfoField REMOVE_STATUS = new AppInfoField("removestatus", ObjectFieldDBType.STRING, true, false);


    public AppInfoField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AppInfoField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

}
