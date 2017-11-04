package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午2:21
 * To change this template use File | Settings | File Templates.
 */
public class ActivityGoodsField extends AbstractObjectField {

    public static final ActivityGoodsField ACTIVITY_GOODS_ID = new ActivityGoodsField("activity_goods_id", ObjectFieldDBType.LONG, false, true);
    public static final ActivityGoodsField GAME_DB_ID = new ActivityGoodsField("game_db_id", ObjectFieldDBType.LONG, false, true);
    public static final ActivityGoodsField ACTIVITY_SUBJECT = new ActivityGoodsField("activity_subject", ObjectFieldDBType.STRING, false, true);
    public static final ActivityGoodsField ACTIVITY_DESC = new ActivityGoodsField("activity_desc", ObjectFieldDBType.STRING, false, true);
    public static final ActivityGoodsField ACTIVITY_DESC_JSON = new ActivityGoodsField("activity_descjson", ObjectFieldDBType.STRING, false, true);
    public static final ActivityGoodsField ACTIVITY_PICURL = new ActivityGoodsField("activity_picurl", ObjectFieldDBType.STRING, false, true);
    public static final ActivityGoodsField ACTIVITY_TYPE = new ActivityGoodsField("activity_type", ObjectFieldDBType.INT, false, true);
    public static final ActivityGoodsField CHANNEL_TYPE = new ActivityGoodsField("channel_type", ObjectFieldDBType.INT, false, true);
    public static final ActivityGoodsField START_TIME = new ActivityGoodsField("start_time", ObjectFieldDBType.TIMESTAMP, false, true);
    public static final ActivityGoodsField END_TIME = new ActivityGoodsField("end_time", ObjectFieldDBType.TIMESTAMP, false, true);
    public static final ActivityGoodsField SHARE_ID = new ActivityGoodsField("share_id", ObjectFieldDBType.LONG, false, true);
    public static final ActivityGoodsField PASSIVE_SHARE_ID = new ActivityGoodsField("passive_share_id", ObjectFieldDBType.LONG, false, true);
    public static final ActivityGoodsField NOTICE_BODY = new ActivityGoodsField("notice_body", ObjectFieldDBType.STRING, false, true);
    public static final ActivityGoodsField FIRST_LETTER = new ActivityGoodsField("first_letter", ObjectFieldDBType.STRING, false, true);
    public static final ActivityGoodsField HOT_ACTIVITY = new ActivityGoodsField("hot_activity", ObjectFieldDBType.INT, false, true);
    public static final ActivityGoodsField RESERVE_TYPE = new ActivityGoodsField("reserve_type", ObjectFieldDBType.INT, false, true);
    public static final ActivityGoodsField ACTIVITY_GOODS_TYPE = new ActivityGoodsField("activity_goods_type", ObjectFieldDBType.INT, false, true);
    public static final ActivityGoodsField GOODS_AMOUNT = new ActivityGoodsField("goods_amount", ObjectFieldDBType.INT, false, true);
    public static final ActivityGoodsField GOODS_RESETAMOUNT = new ActivityGoodsField("goods_resetamount", ObjectFieldDBType.INT, false, true);
    public static final ActivityGoodsField GOODS_POINT = new ActivityGoodsField("goods_point", ObjectFieldDBType.INT, false, true);
    public static final ActivityGoodsField TIME_TYPE = new ActivityGoodsField("time_type", ObjectFieldDBType.INT, false, true);
    public static final ActivityGoodsField TAO_TIME_TYPE = new ActivityGoodsField("tao_time_type", ObjectFieldDBType.INT, false, true);
    public static final ActivityGoodsField DISPLAY_TYPE = new ActivityGoodsField("display_type", ObjectFieldDBType.INT, false, true);
    public static final ActivityGoodsField DISPLAY_ORDER = new ActivityGoodsField("display_order", ObjectFieldDBType.INT, false, true);
    public static final ActivityGoodsField REMOVE_STATUS = new ActivityGoodsField("remove_status", ObjectFieldDBType.STRING, false, true);
    public static final ActivityGoodsField CREATEUSERID = new ActivityGoodsField("createuserid", ObjectFieldDBType.STRING, false, true);
    public static final ActivityGoodsField CREATEIP = new ActivityGoodsField("createip", ObjectFieldDBType.STRING, false, true);
    public static final ActivityGoodsField CREATEDATE = new ActivityGoodsField("createdate", ObjectFieldDBType.TIMESTAMP, false, true);
    public static final ActivityGoodsField SUB_DESC = new ActivityGoodsField("sub_desc", ObjectFieldDBType.STRING, false, true);
    public static final ActivityGoodsField GOODS_ACTION_TYPE = new ActivityGoodsField("goods_action_type", ObjectFieldDBType.INT, false, true);
    public static final ActivityGoodsField BG_PIC = new ActivityGoodsField("bg_pic", ObjectFieldDBType.STRING, false, true);
    public static final ActivityGoodsField PLATFORM = new ActivityGoodsField("platform", ObjectFieldDBType.INT, false, true);
    public static final ActivityGoodsField SECKILL_INFO = new ActivityGoodsField("seckill_info", ObjectFieldDBType.STRING, false, true);
    public static final ActivityGoodsField SECKILL_TYPE = new ActivityGoodsField("seckill_type", ObjectFieldDBType.INT, false, true);


    public ActivityGoodsField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ActivityGoodsField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
