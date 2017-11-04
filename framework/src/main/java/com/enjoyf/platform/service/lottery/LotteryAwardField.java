package com.enjoyf.platform.service.lottery;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-24
 * Time: 下午12:15
 * To change this template use File | Settings | File Templates.
 */
public class LotteryAwardField extends AbstractObjectField {

    public static final LotteryAwardField LOTTERY_AWARD_ID = new LotteryAwardField("lottery_award_id", ObjectFieldDBType.LONG, true, true);
    public static final LotteryAwardField LOTTERY_ID = new LotteryAwardField("lottery_id", ObjectFieldDBType.LONG, true, false);

    public static final LotteryAwardField LOTTERY_AWARD_LEVEL = new LotteryAwardField("lottery_award_level", ObjectFieldDBType.INT, true, false);
    public static final LotteryAwardField LOTTERY_AWARD_NAME = new LotteryAwardField("lottery_award_name", ObjectFieldDBType.STRING, true, false);
    public static final LotteryAwardField LOTTERY_AWARD_DESC = new LotteryAwardField("lottery_award_description", ObjectFieldDBType.STRING, true, false);
    public static final LotteryAwardField LOTTERY_AWARD_PIC = new LotteryAwardField("lottery_award_pic", ObjectFieldDBType.STRING, true, false);
    public static final LotteryAwardField LOTTERY_AWARD_AMOUNT = new LotteryAwardField("lottery_award_amount", ObjectFieldDBType.INT, true, false);
    public static final LotteryAwardField LOTTERY_AWARD_REST_AMOUNT = new LotteryAwardField("lottery_award_rest_amount", ObjectFieldDBType.INT, true, false);
    public static final LotteryAwardField AWARD_MIN_RATE = new LotteryAwardField("award_min_rate", ObjectFieldDBType.INT, true, false);
    public static final LotteryAwardField AWARD_MAX_RATE = new LotteryAwardField("award_max_rate", ObjectFieldDBType.INT, true, false);
    public static final LotteryAwardField CREATEDATE = new LotteryAwardField("createdate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final LotteryAwardField CREATEIP = new LotteryAwardField("createip", ObjectFieldDBType.STRING, true, false);
    public static final LotteryAwardField LASTMODIFYDATE = new LotteryAwardField("lastmodifydate", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final LotteryAwardField LASTMODIFYIP = new LotteryAwardField("lastmodifyip", ObjectFieldDBType.STRING, true, false);
    public static final LotteryAwardField LOTTERY_AWARD_TYPE = new LotteryAwardField("lottery_award_type", ObjectFieldDBType.INT, true, false);
    public static final LotteryAwardField VALID_STATUS = new LotteryAwardField("valid_status", ObjectFieldDBType.STRING, true, false);
    public static final LotteryAwardField LOTTERY_CODE = new LotteryAwardField("lottery_code", ObjectFieldDBType.STRING, true, false);
    public static final LotteryAwardField LOTTERY_AWARD_RULE = new LotteryAwardField("lottery_award_rule", ObjectFieldDBType.STRING, true, false);

    public LotteryAwardField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public LotteryAwardField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
