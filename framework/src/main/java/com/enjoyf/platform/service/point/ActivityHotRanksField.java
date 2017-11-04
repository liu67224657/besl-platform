package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-7-12
 * Time: 下午2:51
 * To change this template use File | Settings | File Templates.
 */
public class ActivityHotRanksField extends AbstractObjectField {
    public static final ActivityHotRanksField ACTIVITY_HOT_RANKS_ID = new ActivityHotRanksField("activity_hot_ranks_id", ObjectFieldDBType.LONG, false, false);
    public static final ActivityHotRanksField GOODS_ID = new ActivityHotRanksField("goods_id", ObjectFieldDBType.LONG, true, false);
    public static final ActivityHotRanksField EXCHANGE_NUM = new ActivityHotRanksField("exchange_num", ObjectFieldDBType.INT, true, false);
    public static final ActivityHotRanksField ACTIVITY_TYPE = new ActivityHotRanksField("activity_type", ObjectFieldDBType.INT, true, false);
    public static final ActivityHotRanksField ACTIVITY_ID = new ActivityHotRanksField("activity_id", ObjectFieldDBType.LONG, true, false);
    public static final ActivityHotRanksField ACTIVITY_NAME = new ActivityHotRanksField("activity_name", ObjectFieldDBType.STRING, true, false);
    public static final ActivityHotRanksField PIC = new ActivityHotRanksField("pic", ObjectFieldDBType.STRING, true, false);
    public static final ActivityHotRanksField LAST_EXCHANGE_TIME = new ActivityHotRanksField("last_exchange_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ActivityHotRanksField REMOVE_STATUS = new ActivityHotRanksField("remove_status", ObjectFieldDBType.STRING, true, false);



    public ActivityHotRanksField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ActivityHotRanksField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
