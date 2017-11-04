package com.enjoyf.platform.serv.lottery.factory;

import com.enjoyf.platform.serv.lottery.LotteryLogic;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.util.Date;
import java.util.List;

/**
 * Created by zhimingli on 2016/6/30 0030.  lotteryStragy
 */
public abstract class AbstractLotteryStrategy implements LotteryStrategy {

    //用户的机会
    private static final String KEY_LOTTERY_USER_TIMES = LotteryConstants.SERVICE_SECTION + "_user_times_";

    //用户使用过的机会
    private static final String KEY_LOTTERY_USER_USE_TIMES = LotteryConstants.SERVICE_SECTION + "_user_use_times_";

    private static final String KEY_LOTTERY_AWARD_RULE = LotteryConstants.SERVICE_SECTION + "_awardrule_";

    protected static final int TIME_OUT_BY_DAY_SEC = 60 * 60 * 24 * 30;//30 day

    protected static final int ZERO = 0;

    protected LotteryLogic lotteryLogic;

    protected LotteryAwardRedis lotteryAwardRedis;

    public AbstractLotteryStrategy(LotteryLogic lotteryLogic, LotteryAwardRedis lotteryAwardRedis) {
        this.lotteryAwardRedis = lotteryAwardRedis;
        this.lotteryLogic = lotteryLogic;
    }

    //返回用户的抽奖号码
    protected abstract int getUserLotteryNumber(Lottery lottery, Date date);

    protected abstract boolean existsLotteryAwardPool(long lotteryId);

    protected abstract void initLotteryAwardPool(long lotteryId, List<LotteryAward> list) throws ServiceException;

    protected abstract long getLotteryAwardPool(long lotteryId, int code);


    public LotteryAwardItem lottery(Lottery lottery, String profileId, Date date, String ip) throws ServiceException {
        //增加用户使用的抽奖机会
        incrUserLotteryTimes(lottery.getLotteryId(), date, lottery.getLotteryRule().getRuleChanceType(), 1, profileId);

        //放入抽奖的数值
        boolean poolExists = existsLotteryAwardPool(lottery.getLotteryId());
        if (!poolExists) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(LotteryAwardField.LOTTERY_ID, lottery.getLotteryId()));
            queryExpress.add(QueryCriterions.eq(LotteryAwardField.VALID_STATUS, ValidStatus.VALID.getCode()));
            List<LotteryAward> list = lotteryLogic.queryLotteryAward(queryExpress);
            if (!CollectionUtil.isEmpty(list)) {
                initLotteryAwardPool(lottery.getLotteryId(), list);
            }
        }

        //产生抽奖号码
        int randomNum = getUserLotteryNumber(lottery, date);

        long lotteryAwardId = getLotteryAwardPool(lottery.getLotteryId(), randomNum);

        LotteryAward award = null;
        if (lotteryAwardId > 0l) {
            award = lotteryLogic.getLotteryAwardById(lotteryAwardId);
        }

        LotteryAwardItem item = null;
        //抽中了一个奖品
        if (award != null) {
            //判断奖品时间段
            if (date.getTime() > award.getCreateDate().getTime() && date.getTime() < award.getLastModifyDate().getTime()) {
                //无限制，抽完为止
                long itemId = 0L;
                if (award.getAwardRule().getLotteryAwardRuleCountType().equals(LotteryAwardRuleCountType.INFINITE)) {
                    itemId = lotteryAwardRedis.popLotteryAwardItemId(lottery.getLotteryId());
                } else {
                    //已经抽中的数量
                    int lotteryCodeNum = getLotteryAwardRule(award.getLotteryAwardId(), date, award.getAwardRule().getLotteryAwardRuleCountType());

                    //奖品限制的数据
                    int lotteryRuleCodeNum = getLotteryAwardRuleCount(award, date);

                    //发出一个奖品
                    if (lotteryRuleCodeNum > 0 && lotteryCodeNum < lotteryRuleCodeNum) {
                        incrLotteryAwardRule(award.getLotteryAwardId(), date, award.getAwardRule().getLotteryAwardRuleCountType(), 1);
                        itemId = lotteryAwardRedis.popLotteryAwardItemId(award.getLotteryAwardId());
                    }
                }


                //抽中奖品
                if (itemId > 0l) {
                    item = lotteryLogic.getLotteryAwardItemById(itemId);
                }

            }
        }


        //写日志并修改item状态
        writeUserLotteryLog(lottery, profileId, award, date, ip, item);
        return item;
    }


    //获取用户机会数
    public int getUserLotteryChance(Lottery lottery, Date date, String profileid) {
        String lotteyRuleKey = getLotteryRuleChanceKey(lottery.getLotteryId(), date, lottery.getLotteryRule().getRuleChanceType(), profileid);
        if (StringUtil.isEmpty(lotteyRuleKey)) {
            return ZERO;
        }

        String returnVal = lotteryAwardRedis.getRedisManager().get(lotteyRuleKey);
        if (StringUtil.isEmpty(returnVal)) {
            returnVal = String.valueOf(lottery.getLotteryRule().getInitChance());
            incrUserLotteryChance(lottery, date, profileid, lottery.getLotteryRule().getInitChance());
        }
        return Integer.valueOf(returnVal);
    }


    //增加用户抽奖机会
    public void incrUserLotteryChance(Lottery lottery, Date date, String profileid, int value) {
        String lotteyRuleKey = getLotteryRuleChanceKey(lottery.getLotteryId(), date, lottery.getLotteryRule().getRuleChanceType(), profileid);
        if (StringUtil.isEmpty(lotteyRuleKey)) {
            return;
        }
        lotteryAwardRedis.getRedisManager().incr(lotteyRuleKey, value, TIME_OUT_BY_DAY_SEC);
    }

    //判断用户是否有机会
    public boolean hasUserLotteryChance(Lottery lottery, Date date, String profileid) {
        //无限制
        if (LotteryRuleChanceType.INFINITE.equals(lottery.getLotteryRule().getRuleChanceType())) {
            return true;
        } else {
            //获取用户抽奖机会数
            int userChance = getUserLotteryChance(lottery, date, profileid);

            //获取用户使用过机会数
            int userUseChance = getUserLotteryTimes(lottery.getLotteryId(), date, lottery.getLotteryRule().getRuleChanceType(), profileid);


            if (userChance < 0 || userUseChance > lottery.getLotteryRule().getMaxChance() || userUseChance >= userChance) {
                //等于最大抽奖机会
                return false;
            }
        }
        return true;
    }


    //写日志并修改lotteryAwardItem状态
    protected void writeUserLotteryLog(Lottery lottery, String profileId, LotteryAward award, Date date, String ip, LotteryAwardItem item) {
        //insert lottery log
        UserLotteryLog userLotteryLog = new UserLotteryLog();
        userLotteryLog.setUno(profileId);
        userLotteryLog.setLotteryAwardPic("");
        userLotteryLog.setScreenName(profileId);
        userLotteryLog.setLotteryId(lottery.getLotteryId());
        if (award != null && item != null) {
            userLotteryLog.setLotteryAwardId(award.getLotteryAwardId());
            userLotteryLog.setLotteryAwardName(award.getLotteryAwardName());
            userLotteryLog.setLotteryAwardDesc(award.getLotteryAwardDesc());
            userLotteryLog.setLotteryAwardLevel(award.getLotteryAwardLevel());
            userLotteryLog.setLottery_code(item.getValue1());
        }
        userLotteryLog.setLotteryDate(date);
        userLotteryLog.setLotteryIp(ip);
        userLotteryLog.setExtension(profileId);
        try {
            lotteryLogic.createUserLotteryLog(userLotteryLog);
            if (item != null) {
                lotteryLogic.modifyLotteryAwardItemById(new UpdateExpress()
                        .set(LotteryAwardItemField.LOTTERY_STATUS, ValidStatus.INVALID.getCode())
                        .set(LotteryAwardItemField.LOTTERY_DATE, date)
                        .set(LotteryAwardItemField.OWN_UNO, profileId), item.getLotteryAwardItemId());
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
        }
    }


    //判断活动次数类型：按天、永久（即活动）、无限制
    protected String getLotteryRuleChanceKey(long lotteryId, Date date, LotteryRuleChanceType lotteryRuleChanceType, String profileid) {
        String key = null;
        if (lotteryRuleChanceType.equals(LotteryRuleChanceType.BYDAY)) {
            key = KEY_LOTTERY_USER_TIMES + lotteryId + DateUtil.formatDateToString(date, DateUtil.PATTERN_DATE_SHORT) + profileid;
        } else if (lotteryRuleChanceType.equals(LotteryRuleChanceType.PERMANENT)) {
            key = KEY_LOTTERY_USER_TIMES + lotteryId + profileid;
        }
        return key;
    }

    //判断活动次数类型：按天、永久（即活动）、无限制
    protected String getLotteryRuleUseChanceKey(long lotteryId, Date date, LotteryRuleChanceType lotteryRuleChanceType, String profileid) {
        if (lotteryRuleChanceType.equals(LotteryRuleChanceType.BYDAY)) {
            return KEY_LOTTERY_USER_USE_TIMES + lotteryId + DateUtil.formatDateToString(date, DateUtil.PATTERN_DATE_SHORT) + profileid;
        } else if (lotteryRuleChanceType.equals(LotteryRuleChanceType.PERMANENT)) {
            return KEY_LOTTERY_USER_USE_TIMES + lotteryId + profileid;
        }
        return null;
    }

    protected String getKeyLotteryAwardRuleKey(long lotteryAwardId, Date date, LotteryAwardRuleCountType countType) {
        if (countType.equals(LotteryAwardRuleCountType.DATE)) {
            return KEY_LOTTERY_AWARD_RULE + lotteryAwardId + DateUtil.formatDateToString(date, DateUtil.PATTERN_DATE_SHORT);
        } else if (countType.equals(LotteryAwardRuleCountType.HOUR)) {
            return KEY_LOTTERY_AWARD_RULE + lotteryAwardId + DateUtil.formatDateToString(date, DateUtil.DEFAULT_DATE_FORMAT3);
        } else if (countType.equals(LotteryAwardRuleCountType.CUSTOM)) {
            return KEY_LOTTERY_AWARD_RULE + lotteryAwardId;
        }
        return null;
    }


    protected void incrLotteryAwardRule(long lotteryAwardId, Date date, LotteryAwardRuleCountType countType, int incrValue) {
        String key = getKeyLotteryAwardRuleKey(lotteryAwardId, date, countType);
        if (!StringUtil.isEmpty(key)) {
            lotteryAwardRedis.getRedisManager().incr(key, incrValue, TIME_OUT_BY_DAY_SEC);
        }
    }


    protected int getLotteryAwardRule(long lotteryAwardId, Date date, LotteryAwardRuleCountType countType) {
        String key = getKeyLotteryAwardRuleKey(lotteryAwardId, date, countType);
        String value = lotteryAwardRedis.getRedisManager().get(key);
        if (StringUtil.isEmpty(value)) {
            return ZERO;
        }
        return Integer.valueOf(value);
    }


    protected int getLotteryAwardRuleCount(LotteryAward award, Date date) {
        if (award.getAwardRule().getLotteryAwardRuleCountType().equals(LotteryAwardRuleCountType.DATE) || award.getAwardRule().getLotteryAwardRuleCountType().equals(LotteryAwardRuleCountType.HOUR)) {
            return Integer.valueOf(award.getAwardRule().getText());
        } else if (award.getAwardRule().getLotteryAwardRuleCountType().equals(LotteryAwardRuleCountType.INFINITE)) {
            return award.getLotteryAwardAmount();
        } else if (award.getAwardRule().getLotteryAwardRuleCountType().equals(LotteryAwardRuleCountType.CUSTOM)) {
            List<LotteryAwardRuleCustom> countRuleList = award.getAwardRule().getCountRule();
            for (LotteryAwardRuleCustom custom : countRuleList) {
                if (date.getTime() > custom.getStartTime().getTime() && date.getTime() < custom.getEndTime().getTime()) {
                    return custom.getTimes();
                }
            }
        }
        return ZERO;
    }


    //增加用户使用机会
    protected void incrUserLotteryTimes(long lotteryId, Date date, LotteryRuleChanceType countType, int incrValue, String profilied) {
        String key = getLotteryRuleUseChanceKey(lotteryId, date, countType, profilied);
        if (!StringUtil.isEmpty(key)) {
            lotteryAwardRedis.getRedisManager().incr(key, incrValue, TIME_OUT_BY_DAY_SEC);
        }

    }

    //获取用户使用机会
    protected int getUserLotteryTimes(long lotteryAwardId, Date date, LotteryRuleChanceType lotteryRuleChanceType, String profilied) {
        String key = getLotteryRuleUseChanceKey(lotteryAwardId, date, lotteryRuleChanceType, profilied);
        String value = lotteryAwardRedis.getRedisManager().get(key);
        if (StringUtil.isEmpty(value)) {
            return ZERO;
        }
        return Integer.valueOf(value);
    }

}
