package com.enjoyf.platform.service.point;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;


public class UserLotteryLogField extends AbstractObjectField {
    public static final UserLotteryLogField USERLOTTERYLOGID = new UserLotteryLogField("user_lottery_log_id", ObjectFieldDBType.STRING);
    public static final UserLotteryLogField GIFTLOTTERYID = new UserLotteryLogField("gift_lottery_id", ObjectFieldDBType.LONG);
    public static final UserLotteryLogField PROFILEID = new UserLotteryLogField("profile_id", ObjectFieldDBType.STRING);
    public static final UserLotteryLogField LOTTERYDATE = new UserLotteryLogField("lottery_date", ObjectFieldDBType.TIMESTAMP);


    public UserLotteryLogField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}