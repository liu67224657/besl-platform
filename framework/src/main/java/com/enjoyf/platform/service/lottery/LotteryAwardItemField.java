package com.enjoyf.platform.service.lottery;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-17
 * Time: 下午2:21
 * To change this template use File | Settings | File Templates.
 */
public class LotteryAwardItemField extends AbstractObjectField {

    public static final LotteryAwardItemField LOTTERY_AWARD_ITEM_ID = new LotteryAwardItemField("lottery_award_item_id", ObjectFieldDBType.LONG, true, true);
    public static final LotteryAwardItemField LOTTERY_AWARD_ID = new LotteryAwardItemField("lottery_award_id", ObjectFieldDBType.LONG, true, false);
    public static final LotteryAwardItemField LOTTERY_STATUS = new LotteryAwardItemField("exchange_status", ObjectFieldDBType.STRING, true, false);
    public static final LotteryAwardItemField OWN_UNO = new LotteryAwardItemField("own_uno", ObjectFieldDBType.STRING, true, false);
    public static final LotteryAwardItemField LOTTERY_DATE = new LotteryAwardItemField("exchange_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final LotteryAwardItemField CREATE_DATE = new LotteryAwardItemField("createdate", ObjectFieldDBType.TIMESTAMP, true, false);

    public LotteryAwardItemField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public LotteryAwardItemField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
