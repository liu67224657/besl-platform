package com.enjoyf.platform.service.point.pointwall;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: tony diao
 * Date: 28-11-14
 * Time: 下午2:21
 * To change this template use File | Settings | File Templates.
 */
public class PointwallWallField extends AbstractObjectField {

    public static final PointwallWallField APPKEY = new PointwallWallField("appkey", ObjectFieldDBType.STRING, true, true);
    public static final PointwallWallField POINT_KEY = new PointwallWallField("point_key", ObjectFieldDBType.STRING, true, false);
    public static final PointwallWallField SHOP_KEY = new PointwallWallField("shop_key", ObjectFieldDBType.INT, true, false);
    public static final PointwallWallField WALL_MONEY_NAME = new PointwallWallField("wall_money_name", ObjectFieldDBType.STRING, true, false);
    public static final PointwallWallField TEMPLATE= new PointwallWallField("template", ObjectFieldDBType.STRING, true, false);

    public PointwallWallField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public PointwallWallField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
