package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-13
 * Time: 上午10:40
 * To change this template use File | Settings | File Templates.
 */
public class UserDayPointField extends AbstractObjectField {

    public static final UserDayPointField USERDAYPOINTID = new UserDayPointField("user_day_pointid", ObjectFieldDBType.LONG, true, true);
    public static final UserDayPointField USERNO = new UserDayPointField("user_no", ObjectFieldDBType.STRING, false, true);
    public static final UserDayPointField POINTACTIONTYPE = new UserDayPointField("action_type", ObjectFieldDBType.INT, false, true);
    public static final UserDayPointField POINTVALUE = new UserDayPointField("point_value", ObjectFieldDBType.INT, false, true);
    public static final UserDayPointField POINTDATE = new UserDayPointField("point_date", ObjectFieldDBType.DATE, false, true);
    public static final UserDayPointField PROFILEID = new UserDayPointField("profileid", ObjectFieldDBType.STRING, false, true);

    public UserDayPointField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public UserDayPointField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
