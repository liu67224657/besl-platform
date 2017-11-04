package com.enjoyf.platform.serv.lottery;

import com.enjoyf.platform.service.lottery.LotteryAward;
import com.enjoyf.platform.service.lottery.LotteryConstants;
import com.enjoyf.platform.service.lottery.LotteryTimesType;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.redis.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhimingli on 2015/5/7.
 */
public class LotteryRedis {
    //
    private static final Logger logger = LoggerFactory.getLogger(LotteryRedis.class);

    private static final String SHUANG12_ACTIVITY_CACHE = "2015s12_activity_cache";
    private static final String LOTTERY_TIME = "_lottery_time_";
    private static final String HAS_LOTTERY = "_has_lottery_";

    private static final String KEY_PREFIX = LotteryConstants.SERVICE_SECTION;


    //初始抽奖机会：每日启动即获得初始2次抽奖机会，仅限当日使用
    //额外奖励机会：每次分享成功，获得额外1次抽奖机会，最多可得5次/日，仅限当日使用
    //奖励机会上限：达到分享奖励次数上限的，可继续分享，但不再给予奖励抽奖次数
    //可抽奖机会上限：初始机会和额外机会可累计，但仅限当日使用
    private static final String KEY_WANBA_FIVE_MONTH_TOTLETIME = "_wanbafivemonth_totletime_";
    private static final String KEY_WANBA_FIVE_MONTH_USETIME = "_wanbafivemonth_usetime_";

    // 1等和2等奖： 活动中，每个设备只能中一次此两等奖品，中奖当日不可再获得3~5档奖品
    //3~5等奖：用户每天只能中1次，次日仍然有概率中奖
    private static final String KEY_WANBA_FIVE_MONTH_ONEORTWO_LEVE = "_wanbafivemonth_lotty_oneortwo_level_";
    private static final String KEY_WANBA_FIVE_MONTH_TODAY_LEVE = "_wanbafivemonth_lotty_today_level_";

    //2015中秋活动 已抽奖累计次数记录;
    private static final String KEY_WANBA_TODAY_TOTAL = "_wanba_today_total";


    private static final int TIME_OUT_BY_DAY_SEC = 60 * 60 * 24 * 2;

    private static final String KEY_LOTTERY_USER_TIMES = KEY_PREFIX + "_ut_";

    private static final String KEY_LOTTERY_AWARDRATE_LIST = KEY_PREFIX + "_aratelist_";

    private static final String KEY_LOTTERY_AWARDITEM_LIST = KEY_PREFIX + "_aitemlist_";

    private static final String KEY_LOTTERY_USER_CHANCE = KEY_PREFIX + "_auserchance_";


    private static final String KEY_LOTTERY_ITEM_TIME_COUNT = KEY_PREFIX + "_itemcount_"; //激活码

    private static final String KEY_LOTTERY_JD = KEY_PREFIX + "_jd_"; //京东卡


    private static final String KEY_LOTTERY_CASH = KEY_PREFIX + "_cash_";//现金

    private static final String KEY_LOTTERY_TOP_UP_PHONE = KEY_PREFIX + "_top_up_phone_";//花费充值

    private static final String KEY_LOTTERY_TIMES_BY_DAY = KEY_PREFIX + "_ltbd_";

    private RedisManager manager;

    public LotteryRedis(FiveProps p) {
        manager = new RedisManager(p);
    }

    public int getEverytUseTime(String key) {
        String value = manager.get(KEY_PREFIX + KEY_WANBA_FIVE_MONTH_USETIME + key);
        if (StringUtil.isEmpty(value)) {
            return 0;
        }
        return Integer.valueOf(value);
    }

    public void putEverytUseTime(String key) {
        int usetime = getEverytUseTime(key);
        manager.set(KEY_PREFIX + KEY_WANBA_FIVE_MONTH_USETIME + key, (usetime + 1) + "");
    }


    public int getEverytTotleTime(String key) {
        String value = manager.get(KEY_PREFIX + KEY_WANBA_FIVE_MONTH_TOTLETIME + key);
        if (StringUtil.isEmpty(value)) {
            manager.set(KEY_PREFIX + KEY_WANBA_FIVE_MONTH_TOTLETIME + key, 3 + "");
            return 3;
        }
        return Integer.valueOf(value);
    }


    public void putEverytTotleTime(String key) {
        String value = manager.get(KEY_PREFIX + KEY_WANBA_FIVE_MONTH_TOTLETIME + key);
        if (StringUtil.isEmpty(value)) {
            manager.set(KEY_PREFIX + KEY_WANBA_FIVE_MONTH_TOTLETIME + key, 4 + "");
        } else {
            if (Integer.valueOf(value) < 8) {
                manager.set(KEY_PREFIX + KEY_WANBA_FIVE_MONTH_TOTLETIME + key, (Integer.valueOf(value) + 1) + "");
            }
        }
    }

    public void putTodayTotal(String key) {
        String value = manager.get(KEY_PREFIX + KEY_WANBA_TODAY_TOTAL + key);
        if (StringUtil.isEmpty(value)) {
            manager.set(KEY_PREFIX + KEY_WANBA_TODAY_TOTAL + key, 1 + "");
        } else {
            manager.set(KEY_PREFIX + KEY_WANBA_TODAY_TOTAL + key, (Integer.valueOf(value) + 1) + "");
        }
    }

    public void setTodayTotal(String key, String value) {
        manager.set(KEY_PREFIX + KEY_WANBA_TODAY_TOTAL + key, value);
    }

    public int getTodayTotal(String key) {
        String value = manager.get(KEY_PREFIX + KEY_WANBA_TODAY_TOTAL + key);
        if (StringUtil.isEmpty(value)) {
            return 1;
        }
        return Integer.valueOf(value);
    }


    public String getOneortwoLevel(String key) {
        String value = manager.get(KEY_PREFIX + KEY_WANBA_FIVE_MONTH_ONEORTWO_LEVE + key);
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        return value;
    }

    public void putOneortwoLevel(String key, String value) {
        manager.set(KEY_PREFIX + KEY_WANBA_FIVE_MONTH_ONEORTWO_LEVE + key, value);
    }

    public String getTodayLevel(String key) {
        String value = manager.get(KEY_PREFIX + KEY_WANBA_FIVE_MONTH_TODAY_LEVE + key);
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        return value;
    }

    public void putTodayLevel(String key, String value) {
        manager.set(KEY_PREFIX + KEY_WANBA_FIVE_MONTH_TODAY_LEVE + key, value);
    }


    public void putShuang12Cache(String value) {
        manager.lpush(SHUANG12_ACTIVITY_CACHE, value);
    }

    public PageRows<String> getShuang12Cache(Pagination pagination) {
        PageRows<String> pageRows = new PageRows<String>();
        List<String> list = manager.lrange(SHUANG12_ACTIVITY_CACHE, pagination.getStartRowIdx(), pagination.getEndRowIdx());

        pagination.setTotalRows((int) manager.length(SHUANG12_ACTIVITY_CACHE));

        pageRows.setPage(pagination);
        pageRows.setRows(list);
        return pageRows;
    }

    public int getShuang12ShareTime(long uid, String date_short) {
        String time = manager.get(SHUANG12_ACTIVITY_CACHE + LOTTERY_TIME + uid + date_short);
        if (!StringUtil.isEmpty(time)) {
            return Integer.parseInt(time);
        }
        return 0;
    }

    public long incrShuang12ShareTime(long uid, String date_short) {
        return manager.incr(SHUANG12_ACTIVITY_CACHE + LOTTERY_TIME + uid + date_short, 1l, -1);
    }

    public long getShuang12HasLottery(long uid, String date_short) {
        String uidCache = manager.get(SHUANG12_ACTIVITY_CACHE + HAS_LOTTERY + uid + date_short);
        if (!StringUtil.isEmpty(uidCache)) {
            return Long.valueOf(uidCache);
        }
        return 0l;
    }

    public void putShuang12HasLottery(long uid, String date_short) {
        manager.set(SHUANG12_ACTIVITY_CACHE + HAS_LOTTERY + uid + date_short, String.valueOf(uid));
    }


    ////////////
    @Deprecated
    public int getUserLotteryTimes(long lotteryId, String profileId, LotteryTimesType timesType, Date date) {
        String value = manager.get(getUserLotteryTimesKey(lotteryId, profileId, timesType, date));
        if (StringUtil.isEmpty(value)) {
            return 0;
        }
        /*
        青青总是欺负我肿么办呢?
         */
        return Integer.parseInt(value);
    }

    public int getUserLotteryTimes(String key) {
        String value = manager.get(key);
        if (StringUtil.isEmpty(value)) {
            return 0;
        }
        return Integer.parseInt(value);
    }


    public void incrUserLotteryTimes(long lotteryId, String profileId, LotteryTimesType timesType, Date date, int value) {
        manager.incr(getUserLotteryTimesKey(lotteryId, profileId, timesType, date), value, TIME_OUT_BY_DAY_SEC);
    }

    private String getUserLotteryTimesKey(long lotteryId, String profileId, LotteryTimesType timesType, Date date) {
        return KEY_LOTTERY_USER_TIMES + lotteryId + profileId + DateUtil.formatDateToString(date, DateUtil.YYYYMMDD_FORMAT) + timesType.getCode();
    }

    public void initLotteryAwardPool(long lotteryId, List<LotteryAward> list) {
        for (LotteryAward award : list) {
            manager.hset(KEY_LOTTERY_AWARDRATE_LIST + lotteryId, String.valueOf(award.getLotteryAwardId()), award.getLotteryAwardMinRate() + "_" + award.getLotteryAwardMaxRate());
        }
    }

    public long getLotteryAwardPool(long lotteryId, int code) {
        Map<String, String> map = manager.hgetAll(KEY_LOTTERY_AWARDRATE_LIST + lotteryId);
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

    public boolean removeLotteryAwardPool(long lotteryId) {
        return manager.remove(KEY_LOTTERY_AWARDRATE_LIST + lotteryId) > 0;
    }

    public boolean existsAwardPool(long lotteryId) {
        return manager.keyExists(KEY_LOTTERY_AWARDRATE_LIST + lotteryId);
    }

    public void lpushAward(long awardId, String... itemIds) {
        manager.lpush(KEY_LOTTERY_AWARDITEM_LIST + awardId, itemIds);
    }

    public long popAwardItemId(long awardId) {
        String itemId = manager.lpop(KEY_LOTTERY_AWARDITEM_LIST + awardId);

        if (!StringUtil.isEmpty(itemId)) {
            return Long.parseLong(itemId);
        }
        return -1l;
    }


    //todo gen key by timesType
    @Deprecated
    public int getUserLotteryChance(long lotteryId, String profileId, LotteryTimesType timesType, Date date, int initValue) {
        String value = manager.get(getUserLotteryChanceKey(lotteryId, profileId, timesType, date));
        if (StringUtil.isEmpty(value)) {
            manager.incr(getUserLotteryChanceKey(lotteryId, profileId, timesType, date), initValue, TIME_OUT_BY_DAY_SEC);
            return initValue;
        }
        return Integer.parseInt(value);
    }


    public void incrUserLotteryChance(long lotteryId, String profileId, LotteryTimesType timesType, int value, Date date) {
        manager.incr(getUserLotteryChanceKey(lotteryId, profileId, timesType, date), value, TIME_OUT_BY_DAY_SEC);
    }

    private String getUserLotteryChanceKey(long lotteryId, String profileId, LotteryTimesType timesType, Date date) {
        return KEY_LOTTERY_USER_CHANCE + lotteryId + profileId + DateUtil.formatDateToString(date, DateUtil.YYYYMMDD_FORMAT) + timesType.getCode();
    }


    //激活嘛每小时30个
    private String getUserLotteryItemNumcountKey(long lotteryId, Date date) {
        return KEY_LOTTERY_ITEM_TIME_COUNT + lotteryId + DateUtil.formatDateToString(date, DateUtil.DEFAULT_DATE_FORMAT3);
    }

    public void incrItemCount(long lotteryId, int value, Date date) {
        manager.incr(getUserLotteryItemNumcountKey(lotteryId, date), value, TIME_OUT_BY_DAY_SEC);
    }

    public int getItemCount(long lotteryId, Date date) {
        String value = manager.get(getUserLotteryItemNumcountKey(lotteryId, date));
        if (StringUtil.isEmpty(value)) {
            return 0;
        }
        return Integer.valueOf(value);
    }


    //jd卡每天1个
    private String getUserLotteryJDKey(long lotteryId, Date date) {
        return KEY_LOTTERY_JD + lotteryId + DateUtil.formatDateToString(date, DateUtil.DATE_FORMAT);
    }

    public void incrUserLotteryJD(long lotteryId, int value, Date date) {
        manager.incr(getUserLotteryJDKey(lotteryId, date), value, TIME_OUT_BY_DAY_SEC);
    }

    public int getUserLotteryJD(long lotteryId, Date date) {
        String value = manager.get(getUserLotteryJDKey(lotteryId, date));
        if (StringUtil.isEmpty(value)) {
            return 0;
        }
        return Integer.valueOf(value);
    }

    //现金
    private String getUserLotteryCashKey(long lotteryId, Date date) {
        return KEY_LOTTERY_CASH + lotteryId + DateUtil.formatDateToString(date, DateUtil.DEFAULT_DATE_FORMAT3);
    }

    public void incrUserLotteryCash(long lotteryId, int value, Date date) {
        manager.incr(getUserLotteryCashKey(lotteryId, date), value, TIME_OUT_BY_DAY_SEC);
    }

    public int getUserLotteryCash(long lotteryId, Date date) {
        String value = manager.get(getUserLotteryCashKey(lotteryId, date));
        if (StringUtil.isEmpty(value)) {
            return 0;
        }
        return Integer.valueOf(value);
    }

    //话费充值
    private String getUserLotteryTopupphoneKey(long lotteryId, Date date) {
        return KEY_LOTTERY_TOP_UP_PHONE + lotteryId + DateUtil.formatDateToString(date, DateUtil.DEFAULT_DATE_FORMAT3);
    }

    public void incrUserLotteryTopupphone(long lotteryId, int value, Date date) {
        manager.incr(getUserLotteryTopupphoneKey(lotteryId, date), value, TIME_OUT_BY_DAY_SEC);
    }

    public int getUserLotteryTopupphone(long lotteryId, Date date) {
        String value = manager.get(getUserLotteryTopupphoneKey(lotteryId, date));
        if (StringUtil.isEmpty(value)) {
            return 0;
        }
        return Integer.valueOf(value);
    }


    public int getLotteryTimes(long lotteryId, Date date) {
        String value = manager.get(KEY_LOTTERY_TIMES_BY_DAY + lotteryId + DateUtil.formatDateToString(date, DateUtil.DEFAULT_DATE_FORMAT3));

        if (StringUtil.isEmpty(value)) {
            return 0;
        }

        return Integer.valueOf(value);
    }

}
