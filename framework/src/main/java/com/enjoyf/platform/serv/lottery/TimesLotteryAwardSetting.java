package com.enjoyf.platform.serv.lottery;/**
 * Created by ericliu on 16/6/21.
 */

import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.redis.RedisManager;

import java.util.Date;


/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/6/21
 */
public class TimesLotteryAwardSetting extends AbstractLotteryAwardSetting {

    private static final String HASHKEY_LOTTERY_CODE_2_AWARD = LotteryConstants.SERVICE_SECTION + "_hlc2a_";

    private static final String LOTTERY_TIMES = LotteryConstants.SERVICE_SECTION + "_lottery_times_";

    private static final int TIME_OUT_BY_DAY_SEC = 60 * 60 * 24 * 2;

    public TimesLotteryAwardSetting(LotteryLogic lotteryLogic, RedisManager redisManager, LotteryRuleActionType lotteryRuleActionType) {
        super(lotteryLogic, redisManager, lotteryRuleActionType);
    }

    public void putAwardByRule(LotteryAward lotteryAward) {
        for (Integer code : lotteryAward.getAwardRule().getRangeList()) {
            redisManager.hset(getAwardItemKey(lotteryAward.getLotteryId()), String.valueOf(code), String.valueOf(lotteryAward.getLotteryAwardId()));
        }
    }

    private String getAwardItemKey(long lotteryId) {
        return HASHKEY_LOTTERY_CODE_2_AWARD + lotteryId;
    }

    public long lottery(int code, long lotteryId, String profileId, Date date) throws ServiceException {

        Lottery lottery = lotteryLogic.getLotteryById(lotteryId);

        //增加一次次数
        incrLotteryTims(lotteryId, date, lottery.getLotteryRule().getRuleChanceType());

        String itemId = redisManager.hget(getAwardItemKey(lotteryId), String.valueOf(code));

        if (StringUtil.isEmpty(itemId)) {
            return -1l;
        }

        return Long.parseLong(itemId);
    }

    @Override
    public int getUserLotteryNumber(Lottery lottery, Date date) {
        return getLotteryTims(lottery.getLotteryId(), date, lottery.getLotteryRule().getRuleChanceType());
    }

    @Override
    protected int getItemCount(long lotteryAwardId, Date date, LotteryAwardRuleCountType lotteryAwardRuleCountType) {
        return 0;
    }

    private void incrLotteryTims(long lotteryId, Date date, LotteryRuleChanceType lotteryRuleChanceType) {
        String key = getUserLotteryChanceKey(lotteryRuleChanceType, lotteryId, date);
        redisManager.incr(key, 1, TIME_OUT_BY_DAY_SEC);
    }

    private int getLotteryTims(long lotteryId, Date date, LotteryRuleChanceType lotteryRuleChanceType) {
        String key = getUserLotteryChanceKey(lotteryRuleChanceType, lotteryId, date);

        String value = redisManager.get(key);
        if (StringUtil.isEmpty(value)) {
            return 0;
        }
        return Integer.valueOf(key);
    }

    private String getUserLotteryChanceKey(LotteryRuleChanceType chanceType, long lotteryId, Date date) {
        String key = "";
        if (LotteryRuleChanceType.BYDAY.equals(chanceType)) {
            key = LOTTERY_TIMES + lotteryId + DateUtil.formatDateToString(date, DateUtil.YYYYMMDD_FORMAT);
        } else if (LotteryRuleChanceType.PERMANENT.equals(chanceType)) {
            key = LOTTERY_TIMES + lotteryId;
        }
        return key;
    }
}
