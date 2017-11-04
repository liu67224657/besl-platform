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
public class UserDayLotteryField extends AbstractObjectField{

    public static final UserDayLotteryField USER_DAY_LOTTERY_ID = new UserDayLotteryField("user_day_lottery_id", ObjectFieldDBType.LONG, true, true);
    public static final UserDayLotteryField USER_NO = new UserDayLotteryField("user_no",ObjectFieldDBType.STRING, true, false);
    public static final UserDayLotteryField LOTTERY_ID = new UserDayLotteryField("lottery_id",ObjectFieldDBType.LONG, true, false);
    public static final UserDayLotteryField LOTTERY_TIMES = new UserDayLotteryField("lottery_times",ObjectFieldDBType.INT, true, false);
    public static final UserDayLotteryField LOTTERY_DATE = new UserDayLotteryField("lottery_date",ObjectFieldDBType.DATE, true, false);

    public UserDayLotteryField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public UserDayLotteryField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
