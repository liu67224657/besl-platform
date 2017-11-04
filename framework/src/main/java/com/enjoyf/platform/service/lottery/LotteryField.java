package com.enjoyf.platform.service.lottery;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-24
 * Time: 下午12:29
 * To change this template use File | Settings | File Templates.
 */
public class LotteryField extends AbstractObjectField{

    public static final LotteryField LOTTERY_ID = new LotteryField("lottery_id",ObjectFieldDBType.LONG, true, true);
    public static final LotteryField LOTTERY_NAME = new LotteryField("lottery_name",ObjectFieldDBType.STRING, true, false);
    public static final LotteryField LOTTERY_DESC = new LotteryField("lottery_desc",ObjectFieldDBType.STRING, true, false);
    public static final LotteryField BASE_RATE = new LotteryField("base_rate",ObjectFieldDBType.INT, true, false);
    public static final LotteryField AWARD_LEVEL_COUNT = new LotteryField("award_level_count",ObjectFieldDBType.INT, true, false);
    public static final LotteryField LOTTERY_TYPE = new LotteryField("lottery_type",ObjectFieldDBType.INT, true, false);
    public static final LotteryField VALID_STATUS = new LotteryField("valid_status",ObjectFieldDBType.STRING, true, false);
    public static final LotteryField CREATE_DATE = new LotteryField("createdate",ObjectFieldDBType.TIMESTAMP, true, false);
    public static final LotteryField CREATE_IP = new LotteryField("createip",ObjectFieldDBType.STRING, true, false);
    public static final LotteryField LAST_MODIFY_DATE = new LotteryField("lastmodifydate",ObjectFieldDBType.TIMESTAMP, true, false);
    public static final LotteryField LAST_MODIFY_IP = new LotteryField("lastmodifyip",ObjectFieldDBType.STRING, true, false);
    public static final LotteryField LOTTERY_TIMES_TYPE = new LotteryField("lottery_times_type",ObjectFieldDBType.INT, true, false);
    public static final LotteryField SHARE_ID = new LotteryField("share_id",ObjectFieldDBType.LONG, true, false);

    public static final LotteryField LOTTERY_RULE = new LotteryField("lottery_rule",ObjectFieldDBType.STRING, true, false);
    public static final LotteryField START_DATE = new LotteryField("start_date",ObjectFieldDBType.TIMESTAMP, true, false);
    public static final LotteryField END_DATE = new LotteryField("end_date",ObjectFieldDBType.TIMESTAMP, true, false);


    public LotteryField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public LotteryField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
