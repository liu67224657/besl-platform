package com.enjoyf.platform.serv.lottery;/**
 * Created by ericliu on 16/6/21.
 */

import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.redis.RedisManager;

import java.util.*;


/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/6/21
 */
public class RandomLotteryAwardSetting extends AbstractLotteryAwardSetting {

    private static final String HASHKEY_LOTTERY_CODE_2_AWARD = LotteryConstants.SERVICE_SECTION + "_hlc2a_";


    public RandomLotteryAwardSetting(LotteryLogic lotteryLogic, RedisManager redisManager, LotteryRuleActionType lotteryRuleActionType) {
        super(lotteryLogic, redisManager, lotteryRuleActionType);
    }

    public void putAwardByRule(LotteryAward lotteryAward) {
        redisManager.hset(HASHKEY_LOTTERY_CODE_2_AWARD + lotteryAward.getLotteryId(), String.valueOf(lotteryAward.getLotteryAwardId()), lotteryAward.getLotteryAwardMinRate() + "_" + lotteryAward.getLotteryAwardMaxRate());
    }

    public long lottery(int code, long lotteryId, String profileId, Date date) throws ServiceException {
        Map<String, String> map = redisManager.hgetAll(HASHKEY_LOTTERY_CODE_2_AWARD + lotteryId);

        if (CollectionUtil.isEmpty(map)) {
            return -1l;
        }

        //get award by code
        long awardId = -1l;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                String[] valueSplit = entry.getValue().split("_");
                int min = Integer.parseInt(valueSplit[0]);
                int max = Integer.parseInt(valueSplit[1]);
                if (code <= max && code >= min) {
                    awardId = Long.parseLong(entry.getKey());
                    break;
                }
            } catch (Exception e) {
                return -1l;
            }
        }
        LotteryAward lotteryAward = lotteryLogic.getLotteryAwardById(awardId);

        //check award count limit
        LotteryAwardRuleCountType lotteryAwardRuleCountType = lotteryAward.getAwardRule().getLotteryAwardRuleCountType();
        List<LotteryAwardRuleCustom> rule = lotteryAward.getAwardRule().getCountRule();
        int countLimit = rule.size();

        int hasLotteryCount = getItemCount(awardId, date, lotteryAwardRuleCountType);
        if (hasLotteryCount >= countLimit) {
            return -1l;
        }

        return popAwardItemId(awardId);
    }

    @Override
    public int getUserLotteryNumber(Lottery lottery, Date date) {
        int randomNum = (int) (Math.random() * (lottery.getBaseRate() - 1)) + 1;
        return randomNum;
    }

    protected int getItemCount(long lotteryAwardId, Date date, LotteryAwardRuleCountType lotteryAwardRuleCountType) {
        String value = redisManager.get(getItemCountKey(lotteryAwardId, date, lotteryAwardRuleCountType));

        //todo 根据不同的type

        if (StringUtil.isEmpty(value)) {
            return 0;
        }
        return Integer.valueOf(value);
    }


}
