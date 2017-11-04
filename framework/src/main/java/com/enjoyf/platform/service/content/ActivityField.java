/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-31 上午11:16
 * Description:
 */
public class ActivityField extends AbstractObjectField {
    public static final ActivityField ACTIVITY_ID = new ActivityField("activity_id", ObjectFieldDBType.LONG, false, false);
    public static final ActivityField GAME_DB_ID = new ActivityField("game_db_id", ObjectFieldDBType.LONG, false, false);
    public static final ActivityField ACTIVITY_SUBJECT = new ActivityField("activity_subject", ObjectFieldDBType.STRING, true, false);
    public static final ActivityField ACTIVITY_DESC = new ActivityField("activity_desc", ObjectFieldDBType.STRING, true, false);
    public static final ActivityField ACTIVITY_DESCJSON = new ActivityField("activity_descjson", ObjectFieldDBType.STRING, true, false);
    public static final ActivityField ACTIVITY_PICURL = new ActivityField("activity_picurl", ObjectFieldDBType.STRING, true, false);
    public static final ActivityField ACTIVITY_TYPE = new ActivityField("activity_type", ObjectFieldDBType.INT, true, false);
    public static final ActivityField ACTIVITY_GAMENAME = new ActivityField("activity_gamename", ObjectFieldDBType.STRING, true, false);
    public static final ActivityField ACTIVITY_GAMEICON = new ActivityField("activity_gameicon", ObjectFieldDBType.STRING, true, false);
    public static final ActivityField ACTIVITY_GAMEDEVELOPER = new ActivityField("activity_gamedeveloper", ObjectFieldDBType.STRING, true, false);
    public static final ActivityField ACTIVITY_GAME_URL = new ActivityField("activity_game_url", ObjectFieldDBType.STRING, true, false);
    public static final ActivityField HOT_ACTIVITY = new ActivityField("hot_activity", ObjectFieldDBType.INT, false, false);
    public static final ActivityField ACTIVITY_QRURL = new ActivityField("activity_qrurl", ObjectFieldDBType.STRING, true, false);
    public static final ActivityField ACTIVITY_IOSURL = new ActivityField("activity_iosurl", ObjectFieldDBType.STRING, true, false);
    public static final ActivityField ACTIVITY_IOSSIZEMB = new ActivityField("activity_iossizemb", ObjectFieldDBType.DOUBLE, true, false);
    public static final ActivityField ACTIVITY_ANDROIDURL = new ActivityField("activity_androidurl", ObjectFieldDBType.STRING, true, false);

    //
    public static final ActivityField ACTIVITY_ANDROIDSIZEMB = new ActivityField("activity_androidsizemb", ObjectFieldDBType.DOUBLE, true, false);
    public static final ActivityField START_TIME = new ActivityField("start_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ActivityField END_TIME = new ActivityField("end_time", ObjectFieldDBType.TIMESTAMP, true, false);


    //
    public static final ActivityField REMOVE_STATUS = new ActivityField("remove_status", ObjectFieldDBType.STRING, true, false);
    public static final ActivityField CREATEUSERID = new ActivityField("createuserid", ObjectFieldDBType.STRING, true, false);
    public static final ActivityField CREATETIME = new ActivityField("createtime", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ActivityField CREATEIP = new ActivityField("createip", ObjectFieldDBType.STRING, true, false);

    //
    public static final ActivityField LASTMODIFYUSERID = new ActivityField("lastmodifyuserid", ObjectFieldDBType.STRING, true, false);
    public static final ActivityField LASTMODIFYTIME = new ActivityField("lastmodifytime", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ActivityField LASTMODIFYIP = new ActivityField("lastmodifyip", ObjectFieldDBType.STRING, true, false);
    public static final ActivityField CATEGORY = new ActivityField("category", ObjectFieldDBType.INT, true, false);

    public static final ActivityField DISPLAY_ORDER = new ActivityField("display_order", ObjectFieldDBType.INT, true, false);
    public static final ActivityField EVENT_TIME = new ActivityField("event_time", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final ActivityField SUB_DESSSC = new ActivityField("sub_desc", ObjectFieldDBType.STRING, true, false);
    public static final ActivityField ACTIVITY_SHARE_ID = new ActivityField("activity_share_id", ObjectFieldDBType.LONG, true, false);

    public static final ActivityField FIRST_LETTER = new ActivityField("first_letter", ObjectFieldDBType.STRING, true, false);
    public static final ActivityField ACTIVITY_PLATFORM = new ActivityField("activity_platform", ObjectFieldDBType.INT, true, false);
    public static final ActivityField ACTIVITY_COOPERATION = new ActivityField("activity_cooperation", ObjectFieldDBType.INT, true, false);
    public static final ActivityField GOODS_CATEGORY = new ActivityField("goods_category", ObjectFieldDBType.INT, true, false);
    public static final ActivityField IS_EXCLUSIVE = new ActivityField("is_exclusive", ObjectFieldDBType.INT, true, false);
    public static final ActivityField GOODS_ACTION_TYPE = new ActivityField("goods_action_type", ObjectFieldDBType.INT, false, true);
    public static final ActivityField BG_PIC = new ActivityField("bg_pic", ObjectFieldDBType.STRING, false, true);
    public static final ActivityField RESERVE_TYPE = new ActivityField("reserve_type", ObjectFieldDBType.INT, false, true);

    //
    public ActivityField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public ActivityField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
