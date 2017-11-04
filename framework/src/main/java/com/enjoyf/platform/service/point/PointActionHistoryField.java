package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-7
 * Time: 下午1:35
 * To change this template use File | Settings | File Templates.
 */
public class PointActionHistoryField extends AbstractObjectField {

    public static final PointActionHistoryField ACTIONHISTORYID = new PointActionHistoryField("action_history_id", ObjectFieldDBType.LONG, true, true);
    public static final PointActionHistoryField USERNO = new PointActionHistoryField("user_no", ObjectFieldDBType.STRING, false, true);
    public static final PointActionHistoryField PROFILEID = new PointActionHistoryField("profileid", ObjectFieldDBType.STRING, false, true);
    public static final PointActionHistoryField ACTIONTYPE = new PointActionHistoryField("action_type", ObjectFieldDBType.INT, false, true);
    public static final PointActionHistoryField ACTIONDESCRIPTION = new PointActionHistoryField("action_description", ObjectFieldDBType.STRING, false, true);
    public static final PointActionHistoryField CREATEDATE = new PointActionHistoryField("action_timestamp", ObjectFieldDBType.TIMESTAMP, false, true);
    public static final PointActionHistoryField ACTIONDATE = new PointActionHistoryField("action_time", ObjectFieldDBType.DATE, false, true);
    public static final PointActionHistoryField POINTVALUE = new PointActionHistoryField("point_value", ObjectFieldDBType.INT, false, true);
    public static final PointActionHistoryField DESTID = new PointActionHistoryField("destid", ObjectFieldDBType.STRING, false, true);
    public static final PointActionHistoryField DESTUNO = new PointActionHistoryField("destuno", ObjectFieldDBType.STRING, false, true);
    public static final PointActionHistoryField DESTUNOPROFILEID = new PointActionHistoryField("destprofileid", ObjectFieldDBType.STRING, false, true);
    public static final PointActionHistoryField PRESTIGE = new PointActionHistoryField("prestige", ObjectFieldDBType.INT, false, true);
    public static final PointActionHistoryField HISTORYACTIONTYPE = new PointActionHistoryField("history_type", ObjectFieldDBType.INT, false, true);
    public static final PointActionHistoryField APPKEY = new PointActionHistoryField("appkey", ObjectFieldDBType.STRING, false, true);
    public static final PointActionHistoryField POINTKEY = new PointActionHistoryField("pointkey", ObjectFieldDBType.STRING, false, true);


    public PointActionHistoryField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public PointActionHistoryField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
