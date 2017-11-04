package com.enjoyf.platform.service.point.pointwall;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by tonydiao on 2014/12/1.
 */
public class PointwallAppField extends AbstractObjectField {
    public static final PointwallAppField APP_ID = new PointwallAppField("app_id", ObjectFieldDBType.LONG, false, true);
    public static final PointwallAppField PACKAGE_NAME = new PointwallAppField("package_name", ObjectFieldDBType.STRING, true, false);
    public static final PointwallAppField APP_NAME = new PointwallAppField("app_name", ObjectFieldDBType.STRING, true, false);
    public static final PointwallAppField VER_NAME = new PointwallAppField("ver_name", ObjectFieldDBType.STRING, true, false);
    public static final PointwallAppField PLATFORM = new PointwallAppField("platform", ObjectFieldDBType.INT, true, false);
    public static final PointwallAppField APP_ICON = new PointwallAppField("app_icon", ObjectFieldDBType.STRING, true, false);
    public static final PointwallAppField APP_DESC = new PointwallAppField("app_desc", ObjectFieldDBType.STRING, true, false);
    public static final PointwallAppField SPONSOR_NAME = new PointwallAppField("sponsor_name", ObjectFieldDBType.STRING, true, false);
    public static final PointwallAppField DOWNLOAD_URL = new PointwallAppField("download_url", ObjectFieldDBType.STRING, true, false);
    public static final PointwallAppField REPORT_URL = new PointwallAppField("report_url", ObjectFieldDBType.STRING, true, false);
    public static final PointwallAppField INIT_SCORE = new PointwallAppField("init_score", ObjectFieldDBType.INT, true, false);
    public static final PointwallAppField CREATE_TIME = new PointwallAppField("create_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final PointwallAppField REMOVE_STATUS = new PointwallAppField("remove_status", ObjectFieldDBType.STRING, true, false);

    public PointwallAppField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public PointwallAppField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
