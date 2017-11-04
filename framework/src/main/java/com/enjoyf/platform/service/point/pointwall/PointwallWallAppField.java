package com.enjoyf.platform.service.point.pointwall;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by tonydiao on 2014/12/1.
 */
public class PointwallWallAppField extends AbstractObjectField {
    public static final PointwallWallAppField WALL_APP_ID = new PointwallWallAppField("wall_app_id", ObjectFieldDBType.LONG, false, true);
    public static final PointwallWallAppField APP_ID = new PointwallWallAppField("app_id", ObjectFieldDBType.LONG, false, false);
    public static final PointwallWallAppField APPKEY = new PointwallWallAppField("appkey", ObjectFieldDBType.STRING, false, false);
    public static final PointwallWallAppField PLATFORM = new PointwallWallAppField("platform", ObjectFieldDBType.INT, true, false);
    public static final PointwallWallAppField DISPLAY_ORDER = new PointwallWallAppField("display_order", ObjectFieldDBType.INT, true, false);
    public static final PointwallWallAppField HOT_STATUS = new PointwallWallAppField("hot_status", ObjectFieldDBType.INT, true, false);
    public static final PointwallWallAppField POINT_AMOUNT = new PointwallWallAppField("point_amount", ObjectFieldDBType.INT, true, false);
    public static final PointwallWallAppField STATUS = new PointwallWallAppField("status", ObjectFieldDBType.STRING, true, false);

    public PointwallWallAppField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public PointwallWallAppField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
