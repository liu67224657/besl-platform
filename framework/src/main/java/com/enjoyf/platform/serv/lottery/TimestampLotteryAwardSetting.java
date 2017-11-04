package com.enjoyf.platform.serv.lottery;/**
 * Created by ericliu on 16/6/21.
 */

import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.redis.RedisManager;

import java.util.Date;
import java.util.List;
import java.util.Random;


/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/6/21
 */
public class TimestampLotteryAwardSetting extends AbstractLotteryAwardSetting {

    private static final String HASHKEY_LOTTERY_CODE_2_AWARD = LotteryConstants.SERVICE_SECTION + "_hlc2a_";

    private static Random random = new Random();

    public TimestampLotteryAwardSetting(LotteryLogic lotteryLogic, RedisManager redisManager, LotteryRuleActionType lotteryRuleActionType) {
        super(lotteryLogic, redisManager, lotteryRuleActionType);
    }


    public void putAwardByRule(LotteryAward lotteryAward) {
        for (Integer code : lotteryAward.getAwardRule().getRangeList()) {
            redisManager.hset(HASHKEY_LOTTERY_CODE_2_AWARD + lotteryAward.getLotteryId(), String.valueOf(code), String.valueOf(lotteryAward.getLotteryAwardId()));
        }
    }

    public long lottery(int code, long lotteryId, String profileId, Date date) throws ServiceException {
        return -1l;
    }

    @Override
    public int getUserLotteryNumber(Lottery lottery, Date date) {
        int randomTime = Long.valueOf(date.getTime() / 1000).intValue();
        return randomTime;
    }

    @Override
    protected int getItemCount(long lotteryAwardId, Date date, LotteryAwardRuleCountType lotteryAwardRuleCountType) {
        return 0;
    }
}
