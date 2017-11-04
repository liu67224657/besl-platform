package com.enjoyf.platform.serv.lottery.factory;

import com.enjoyf.platform.service.lottery.LotteryConstants;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.redis.RedisManager;

/**
 * Created by zhimingli on 2016/7/1 0001.
 */
public class LotteryAwardRedis {
    private static final String KEY_PREFIX = LotteryConstants.SERVICE_SECTION;

    private static final int TIME_OUT_BY_DAY_SEC = 60 * 60 * 24 * 30;//30 day


    private static final String KEY_LOTTERY_AWARDITEM_LIST = KEY_PREFIX + "_awarditems_";


    private static final String KEY_LOTTERY_AWARD_LIST = LotteryConstants.SERVICE_SECTION + "_lottery_award_";


    private static final String KEY_LOTTERY_AWARD_TIMES = LotteryConstants.SERVICE_SECTION + "_lottery_award_times";

    public static RedisManager redisManager;

    public LotteryAwardRedis(FiveProps p) {
        redisManager = new RedisManager(p);
    }


    ///关于把奖品的抽奖规则放入redis中============end


    ///TODO 关于把奖品的lotteryAwardItem放入redis中============start
    public void lpushLotteryAwardItemId(long awardId, String... itemIds) {
        redisManager.lpush(KEY_LOTTERY_AWARDITEM_LIST + awardId, itemIds);
    }

    public long popLotteryAwardItemId(long awardId) {
        String itemId = redisManager.lpop(KEY_LOTTERY_AWARDITEM_LIST + awardId);

        if (!StringUtil.isEmpty(itemId)) {
            return Long.parseLong(itemId);
        }
        return -1l;
    }

    ///关于把奖品的lotteryAwardItem放入redis中============end


    public RedisManager getRedisManager() {
        return redisManager;
    }

    public static void setRedisManager(RedisManager redisManager) {
        LotteryAwardRedis.redisManager = redisManager;
    }

    public boolean removeLotteryAwardPool(long lotteryId) {
        return redisManager.remove(KEY_LOTTERY_AWARD_LIST + lotteryId) > 0;
    }


    public int getLotteryTimes(long lotteryId) {
        String returnVal = redisManager.get(KEY_LOTTERY_AWARD_TIMES + lotteryId);
        if (StringUtil.isEmpty(returnVal)) {
            return 0;
        }
        return Integer.valueOf(returnVal);
    }

    public void incrLotteryTimes(long lotteryId, int incrVale) {
        redisManager.incr(KEY_LOTTERY_AWARD_TIMES + lotteryId, incrVale, TIME_OUT_BY_DAY_SEC);
    }
}
