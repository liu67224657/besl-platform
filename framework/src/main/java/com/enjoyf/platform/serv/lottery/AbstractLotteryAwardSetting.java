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
public abstract class AbstractLotteryAwardSetting {
    private static final String KEY_LOTTERY_AWARDITEM_LIST = LotteryConstants.SERVICE_SECTION + "_aitemlist_";

    private static final String KEY_LOTTERY_ITEM_TIME_COUNT = LotteryConstants.SERVICE_SECTION + "_aitemcount_";


    protected RedisManager redisManager;

    protected LotteryLogic lotteryLogic;

    public AbstractLotteryAwardSetting(LotteryLogic lotteryLogic, RedisManager redisManager, LotteryRuleActionType lotteryRuleActionType) {
        this.redisManager = redisManager;
        this.lotteryLogic = lotteryLogic;
    }

    //用于创建抽奖奖品时候,或者用于抽奖时候award不存在的情况
    public abstract void putAwardByRule(LotteryAward lotteryAward);

    //抽奖的逻辑
    public abstract long lottery(int code, long lotteryId, String profileId, Date date) throws ServiceException;

    //返回用户的抽奖号码
    public abstract int getUserLotteryNumber(Lottery lottery, Date date);


    public long popAwardItemId(long awardId) {
        String itemId = redisManager.lpop(KEY_LOTTERY_AWARDITEM_LIST + awardId);

        if (!StringUtil.isEmpty(itemId)) {
            return Long.parseLong(itemId);
        }
        return -1l;
    }

    public void lpushAward(long awardId, String... itemIds) {
        redisManager.lpush(KEY_LOTTERY_AWARDITEM_LIST + awardId, itemIds);
    }

    protected abstract int getItemCount(long lotteryAwardId, Date date, LotteryAwardRuleCountType lotteryAwardRuleCountType);

    protected String getItemCountKey(long lotteryAwardId, Date date, LotteryAwardRuleCountType countType) {
        //todo
        if (countType.equals(LotteryAwardRuleCountType.DATE)) {
            return KEY_LOTTERY_ITEM_TIME_COUNT + lotteryAwardId + DateUtil.formatDateToString(date, DateUtil.PATTERN_DATE_SHORT);
        } else if (countType.equals(LotteryAwardRuleCountType.HOUR)) {
            return KEY_LOTTERY_ITEM_TIME_COUNT + lotteryAwardId + DateUtil.formatDateToString(date, DateUtil.DEFAULT_DATE_FORMAT3);
        } else if (countType.equals(LotteryAwardRuleCountType.CUSTOM)) {
            return KEY_LOTTERY_ITEM_TIME_COUNT + lotteryAwardId;
        }
        return null;
    }


}
