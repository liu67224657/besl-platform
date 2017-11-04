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
public class UserLotteryLogField extends AbstractObjectField{

    public static final UserLotteryLogField USER_LOTTERY_ID = new UserLotteryLogField("user_lottery_id",ObjectFieldDBType.LONG, true, true);
    public static final UserLotteryLogField USER_NO = new UserLotteryLogField("user_no",ObjectFieldDBType.STRING, true, false);
    public static final UserLotteryLogField LOTTERY_ID = new UserLotteryLogField("lottery_id",ObjectFieldDBType.LONG, true, false);
    public static final UserLotteryLogField LOTTERY_AWARD_ID = new UserLotteryLogField("lottery_award_id",ObjectFieldDBType.LONG, true, false);
    public static final UserLotteryLogField LOTTERY_AWARD_NAME = new UserLotteryLogField("lottery_award_name",ObjectFieldDBType.STRING, true, false);
    public static final UserLotteryLogField LOTTERY_AWARD_DESC = new UserLotteryLogField("lottery_award_desc",ObjectFieldDBType.STRING, true, false);
    public static final UserLotteryLogField LOTTERY_AWARD_PIC = new UserLotteryLogField("lottery_award_pic",ObjectFieldDBType.STRING, true, false);
    public static final UserLotteryLogField LOTTERY_AWARD_LEVEL = new UserLotteryLogField("lottery_award_level",ObjectFieldDBType.INT, true, false);
    public static final UserLotteryLogField LOTTERY_DATE = new UserLotteryLogField("lottery_date",ObjectFieldDBType.TIMESTAMP, true, false);
    public static final UserLotteryLogField LOTTERY_IP = new UserLotteryLogField("lottery_ip",ObjectFieldDBType.STRING, true, false);
    public static final UserLotteryLogField SCREEN_NAME = new UserLotteryLogField("screen_name",ObjectFieldDBType.STRING, true, false);

    public static final UserLotteryLogField LOTTERY_AWARD_ITEM_ID = new UserLotteryLogField("lottery_award_item_id", ObjectFieldDBType.LONG, true, false);

    public static final UserLotteryLogField LOTTERY_CODE = new UserLotteryLogField("lottery_code", ObjectFieldDBType.STRING, true, false);
    public static final UserLotteryLogField EXTENSION = new UserLotteryLogField("extension", ObjectFieldDBType.STRING, true, false);
    public UserLotteryLogField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public UserLotteryLogField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
