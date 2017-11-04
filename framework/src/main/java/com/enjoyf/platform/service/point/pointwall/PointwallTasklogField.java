package com.enjoyf.platform.service.point.pointwall;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by tonydiao on 2014/12/1.
 */
public class PointwallTasklogField extends AbstractObjectField {
    public static final PointwallTasklogField TASK_ID = new PointwallTasklogField("task_id", ObjectFieldDBType.STRING, false, true);
    public static final PointwallTasklogField CLIENTID = new PointwallTasklogField("clientid", ObjectFieldDBType.STRING, false, false);
    public static final PointwallTasklogField PROFILEID = new PointwallTasklogField("profileid", ObjectFieldDBType.STRING, false, false);
    public static final PointwallTasklogField APP_ID = new PointwallTasklogField("app_id", ObjectFieldDBType.LONG, false, false);
    public static final PointwallTasklogField PACKAGE_NAME = new PointwallTasklogField("package_name", ObjectFieldDBType.STRING, false, false);
    public static final PointwallTasklogField APPKEY = new PointwallTasklogField("appkey", ObjectFieldDBType.STRING, false, false);
    public static final PointwallTasklogField STATUS = new PointwallTasklogField("status", ObjectFieldDBType.INT, false, false);
    public static final PointwallTasklogField CREATE_TIME = new PointwallTasklogField("create_time", ObjectFieldDBType.TIMESTAMP, false, false);
    public static final PointwallTasklogField CREATE_IP = new PointwallTasklogField("create_ip", ObjectFieldDBType.STRING, false, false);
    public static final PointwallTasklogField POINT_AMOUNT = new PointwallTasklogField("point_amount", ObjectFieldDBType.INT, false, false);
    public static final PointwallTasklogField PLATFORM = new PointwallTasklogField("platform", ObjectFieldDBType.INT, false, false);


    public PointwallTasklogField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public PointwallTasklogField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
