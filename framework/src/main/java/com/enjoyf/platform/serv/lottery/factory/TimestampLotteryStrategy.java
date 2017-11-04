package com.enjoyf.platform.serv.lottery.factory;

import com.enjoyf.platform.serv.lottery.LotteryLogic;
import com.enjoyf.platform.service.lottery.Lottery;
import com.enjoyf.platform.service.lottery.LotteryAward;
import com.enjoyf.platform.service.lottery.LotteryConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 按时间戳抽奖的策略
 * Created by zhimingli on 2016/6/30 0030.
 */
public class TimestampLotteryStrategy extends AbstractLotteryStrategy {

    private static final String KEY_LOTTERY_AWARD_LIST = LotteryConstants.SERVICE_SECTION + "_lottery_award_";

    public TimestampLotteryStrategy(LotteryLogic lotteryLogic, LotteryAwardRedis lotteryAwardRedis) {
        super(lotteryLogic, lotteryAwardRedis);
    }

    @Override
    protected int getUserLotteryNumber(Lottery lottery, Date date) {
        return (int) (System.currentTimeMillis() / 1000);
    }

    protected boolean existsLotteryAwardPool(long lotteryId) {
        return lotteryAwardRedis.getRedisManager().keyExists(KEY_LOTTERY_AWARD_LIST + lotteryId);
    }

    protected void initLotteryAwardPool(long lotteryId, List<LotteryAward> list) throws ServiceException {
        if (!CollectionUtil.isEmpty(list)) {
            for (LotteryAward award : list) {
                for (int i = 0; i < award.getLotteryAwardAmount(); i++) {
                    int min = (int) (award.getCreateDate().getTime() / 1000);
                    int max = (int) (award.getLastModifyDate().getTime() / 1000);
                    int randomTime = (int) ((Math.random() * (max - min)) + min);
                    lotteryAwardRedis.getRedisManager().hset(KEY_LOTTERY_AWARD_LIST + lotteryId, String.valueOf(award.getLotteryAwardId()), String.valueOf(randomTime));
                }

            }
        }
    }

    protected long getLotteryAwardPool(long lotteryId, int code) {
        Map<String, String> map = lotteryAwardRedis.getRedisManager().hgetAll(KEY_LOTTERY_AWARD_LIST + lotteryId);
        if (CollectionUtil.isEmpty(map)) {
            return -1l;
        }

        for (String timesKey : map.keySet()) {
            try {
                String value = map.get(timesKey);
                if (value.equals(String.valueOf(timesKey))) {
                    //返回lotteyAwardId
                    return Long.valueOf(timesKey);
                }

            } catch (Exception e) {
                return -1l;
            }
        }
        return -1l;
    }
}
