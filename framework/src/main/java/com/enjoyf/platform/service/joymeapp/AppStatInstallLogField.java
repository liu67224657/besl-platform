package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-24
 * Time: 下午4:31
 * To change this template use File | Settings | File Templates.
 */
public class AppStatInstallLogField extends AbstractObjectField {

    public static final InstalledAppInfoField APP_INSTALL_LOG_ID = new InstalledAppInfoField("app_install_log_id", ObjectFieldDBType.LONG, false, true);
    public static final InstalledAppInfoField APP_ID = new InstalledAppInfoField("app_id", ObjectFieldDBType.LONG, false, false);
    public static final InstalledAppInfoField PLATOFRM = new InstalledAppInfoField("platform", ObjectFieldDBType.INT, false, false);
    public static final InstalledAppInfoField DEST_APP_ID = new InstalledAppInfoField("dest_app_id", ObjectFieldDBType.LONG, false, false);
    public static final InstalledAppInfoField DEST_APP_NAME = new InstalledAppInfoField("dest_app_name", ObjectFieldDBType.STRING, false, false);
    public static final InstalledAppInfoField DEST_APP_INSTALL_COUNT = new InstalledAppInfoField("dest_app_install_count", ObjectFieldDBType.INT, false, false);
    public static final InstalledAppInfoField APP_SEQ_ID = new InstalledAppInfoField("app_log_sequence_id", ObjectFieldDBType.LONG, false, false);
    public static final InstalledAppInfoField CREATE_DATE = new InstalledAppInfoField("create_date", ObjectFieldDBType.DATE, false, false);
    public static final InstalledAppInfoField CREATE_TIME = new InstalledAppInfoField("create_time", ObjectFieldDBType.TIMESTAMP, false, false);
    public static final InstalledAppInfoField DISPLAY_ORDER = new InstalledAppInfoField("display_order", ObjectFieldDBType.INT, false, false);


    //
    public AppStatInstallLogField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public AppStatInstallLogField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
