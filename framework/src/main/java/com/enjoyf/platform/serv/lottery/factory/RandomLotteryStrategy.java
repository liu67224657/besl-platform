package com.enjoyf.platform.serv.lottery.factory;

import com.enjoyf.platform.serv.lottery.LotteryLogic;
import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 按随机数抽奖的策略
 * Created by zhimingli on 2016/6/30 0030.
 */
public class RandomLotteryStrategy extends AbstractLotteryStrategy {

    private static final String KEY_LOTTERY_AWARD_LIST = LotteryConstants.SERVICE_SECTION + "_lottery_award_";

    public RandomLotteryStrategy(LotteryLogic lotteryLogic, LotteryAwardRedis lotteryAwardRedis) {
        super(lotteryLogic, lotteryAwardRedis);
    }

    @Override
    protected int getUserLotteryNumber(Lottery lottery, Date date) {
        return (int) (Math.random() * (lottery.getBaseRate() - 1)) + 1;
    }

    ///TODO 关于把奖品的抽奖规则放入redis中============start
    protected boolean existsLotteryAwardPool(long lotteryId) {
        return lotteryAwardRedis.getRedisManager().keyExists(KEY_LOTTERY_AWARD_LIST + lotteryId);
    }

    protected void initLotteryAwardPool(long lotteryId, List<LotteryAward> list) throws ServiceException {
        if (!CollectionUtil.isEmpty(list)) {
            for (LotteryAward award : list) {
                lotteryAwardRedis.getRedisManager().hset(KEY_LOTTERY_AWARD_LIST + lotteryId, String.valueOf(award.getLotteryAwardId()),
                        award.getAwardRule().getMin() + "_" + award.getAwardRule().getMax());
            }
        }
    }

    protected long getLotteryAwardPool(long lotteryId, int code) {
        Map<String, String> map = lotteryAwardRedis.getRedisManager().hgetAll(KEY_LOTTERY_AWARD_LIST + lotteryId);
        if (CollectionUtil.isEmpty(map)) {
            return -1l;
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                String[] valueSplit = entry.getValue().split("_");
                int min = Integer.parseInt(valueSplit[0]);
                int max = Integer.parseInt(valueSplit[1]);
                if (code <= max && code >= min) {
                    return Long.parseLong(entry.getKey());
                }
            } catch (Exception e) {
                return -1l;
            }
        }
        return -1l;
    }

}
