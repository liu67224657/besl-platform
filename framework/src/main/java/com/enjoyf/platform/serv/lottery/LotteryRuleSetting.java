package com.enjoyf.platform.serv.lottery;/**
 * Created by ericliu on 16/6/21.
 */

import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.redis.RedisManager;

import java.util.Date;
import java.util.Map;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/6/21
 */
public class LotteryRuleSetting {
    private static final String KEY_LOTTERY_USER_CHANCE = LotteryConstants.SERVICE_SECTION + "_auserchance_";

    private static final String KEY_LOTTERY_USER_TIMES = LotteryConstants.SERVICE_SECTION + "_ut_";

    private static final String KEY_LOTTERY_TIMES_BY_DAY = LotteryConstants.SERVICE_SECTION + "_ltbd_";

    private static final int TIME_OUT_BY_DAY_SEC = 60 * 60 * 24 * 2;

    private RedisManager manager;


    public LotteryRuleSetting(RedisManager manager) {
        this.manager = manager;
    }


    //判断是否有机会
    public boolean check(Lottery lottery, String profileId, Date date) {

        //还有几次机会
        int userChance = getChance(lottery.getLotteryRule(), lottery.getLotteryId(), profileId, date);


        //是否有抽奖机会
        boolean bVal = hasChance(userChance);

        if (!bVal) {
            return bVal;//todo
        }

        bVal = hasTimes(lottery.getLotteryRule(), lottery.getLotteryId(), profileId, date, userChance);

        return bVal;
    }

    private boolean hasChance(int userChance) {
        //无限制
        if (userChance == -100) {
            return true;
        }
        return userChance > 0;
    }

    private int getChance(LotteryRule lotteryRule, long lotteryId, String profileId, Date date) {
        //如果活动是无限制抽奖次数
        if (LotteryRuleChanceType.INFINITE.equals(lotteryRule.getRuleChanceType())) {
            return -100;
        }

        //初始抽奖数
        int initChance = lotteryRule.getInitChance();


        //获取
        String key = getUserLotteryChanceKey(lotteryRule.getRuleChanceType(), lotteryId, profileId, date);
        if (!key.equals("-1")) {
            String value = manager.get(key);
            if (StringUtil.isEmpty(value)) {
                manager.incr(key, initChance, TIME_OUT_BY_DAY_SEC);
                return initChance;
            }
        }

        return -100;
    }

    public void incrUserLotteryChance(LotteryRuleChanceType chanceType, long lotteryId, String profileId, Date date, int incrValue) {
        String key = getUserLotteryChanceKey(chanceType, lotteryId, profileId, date);
        manager.incr(key, incrValue, TIME_OUT_BY_DAY_SEC);
    }

    private String getUserLotteryChanceKey(LotteryRuleChanceType chanceType, long lotteryId, String profileId, Date date) {
        String key = "";
        if (LotteryRuleChanceType.BYDAY.equals(chanceType)) {
            key = KEY_LOTTERY_USER_CHANCE + lotteryId + profileId + DateUtil.formatDateToString(date, DateUtil.YYYYMMDD_FORMAT);
        } else if (LotteryRuleChanceType.PERMANENT.equals(chanceType)) {
            key = KEY_LOTTERY_USER_CHANCE + lotteryId + profileId;
        } else {
            key = "-1";
        }

        return key;
    }


    ////////////
    private boolean hasTimes(LotteryRule lotteryRule, long lotteryId, String profileId, Date date, int chance) {

        //如果活动是无限制抽奖次数
        if (LotteryRuleChanceType.INFINITE.equals(lotteryRule.getRuleChanceType())) {
            return true;
        }

        int limit = lotteryRule.getMaxChance();
        int userTimes = getUserLotteryTimes(lotteryRule.getRuleChanceType(), lotteryId, profileId, date);

        //用户抽奖次数<限制 && 机会大于次数
        return (userTimes == -100 || userTimes < limit) && (chance != -100 || chance > userTimes);
    }

    private int getUserLotteryTimes(LotteryRuleChanceType lotteryRuleChanceType, long lotteryId, String profileId, Date date) {

        String key = getUserLotteryLimitKey(lotteryRuleChanceType, lotteryId, profileId, date);
        if (!key.equals("-100")) {
            return Integer.parseInt(manager.get(key));
        }

        return -100;
    }

    private String getUserLotteryLimitKey(LotteryRuleChanceType lotteryRuleChanceType, long lotteryId, String profileId, Date date) {

        String key = "";
        if (LotteryRuleChanceType.BYDAY.equals(lotteryRuleChanceType)) {
            key = KEY_LOTTERY_USER_TIMES + lotteryId + profileId + DateUtil.formatDateToString(date, DateUtil.YYYYMMDD_FORMAT);
        } else if (LotteryRuleChanceType.PERMANENT.equals(lotteryRuleChanceType)) {
            key = KEY_LOTTERY_USER_TIMES + lotteryId + profileId;
        } else {
            key = "-100";
        }

        return key;
    }

    /////////// todo 抽经code的表述.中奖的code是放在lotteryRule还是AwardRule??
    public int getLotteryCode(Lottery lottery, Date date) {
        int code = -1;
        LotteryRule lotteryRule = lottery.getLotteryRule();
        LotteryRuleActionType actionType = lotteryRule.getLotteryRuleActionType();
        if (LotteryRuleActionType.RANDOM.equals(actionType)) {
            code = (int) (Math.random() * (lottery.getBaseRate() - 1)) + 1;
        } else if (LotteryRuleActionType.TIMES.equals(actionType)) {
            code = getLotteryTimes(lottery.getLotteryId(), date);//todo 同上
        } else if (LotteryRuleActionType.TIMESTAMP.equals(actionType)) {
            code = Long.valueOf(date.getTime() / 1000).intValue();
        }
        return code;
    }


    public int getLotteryTimes(long lotteryId, Date date) {//todo
        String value = manager.get(KEY_LOTTERY_TIMES_BY_DAY + lotteryId + DateUtil.formatDateToString(date, DateUtil.DEFAULT_DATE_FORMAT3));

        if (StringUtil.isEmpty(value)) {
            return 0;
        }

        return Integer.valueOf(value);
    }


}
