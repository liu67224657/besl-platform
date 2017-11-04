/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.lottery;

import com.enjoyf.platform.service.event.EventConstants;
import com.enjoyf.platform.service.lottery.Lottery;
import com.enjoyf.platform.service.lottery.LotteryAward;
import com.enjoyf.platform.service.lottery.LotteryConstants;
import com.enjoyf.platform.service.lottery.UserLotteryLog;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
class LotteryCache {

    private static final long TIME_OUT_SEC = 60l * 5l;

    private static final String SERVICE_PREFIX = "lottery";
    private static final String CACHE_KEY_USER_LOTTERY_LOG_PREFIX = "user_lottery_log_";
    private static final String KEY_LOTTERY = "_id_";
    private static final String KEY_LOTTERY_AWARD_LIST = "_award_lid_";
    private static final String KEY_LOTTERY_WANBA_FIVEMONTH = "_wanba_fivemonth_";
    private static final String KEY_LOTTERY_WANBA_FIVEMONTH_MAP = "_wanba_fivemonth_map_";

    private static final String KEY_LOTTERY_COUNT = "_lottery_count_";
    private static final String KEY_LOTTERY_SHARE_COUNT = "_lottery_share_count_";

    private MemCachedConfig config;

    private MemCachedManager manager;

    LotteryCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(config);
    }


    ////////////////////////////////////////////////////////////
    public void putUserLotteryLogList(long lotteryId, List<UserLotteryLog> infolist) {
        manager.put(CACHE_KEY_USER_LOTTERY_LOG_PREFIX + lotteryId, infolist, TIME_OUT_SEC);
    }

    public List<UserLotteryLog> getUserLotteryLogList(long lotteryId) {
        Object infoList = manager.get(CACHE_KEY_USER_LOTTERY_LOG_PREFIX + lotteryId);
        if (infoList == null) {
            return null;
        }
        return (List<UserLotteryLog>) infoList;
    }

    public boolean deleteUserLotteryLogList(long lotteryId) {
        return manager.remove(CACHE_KEY_USER_LOTTERY_LOG_PREFIX + lotteryId);
    }

    /////////////////////////////////////////////


    public void putLottery(long lotteryId, Lottery lottery) {
        manager.put(SERVICE_PREFIX + KEY_LOTTERY + lotteryId, lottery, TIME_OUT_SEC);
    }

    public Lottery getLottery(long lotteryId) {
        Object lottery = manager.get(SERVICE_PREFIX + KEY_LOTTERY + lotteryId);
        if (lottery == null) {
            return null;
        }
        return (Lottery) lottery;
    }

    public boolean deleteLottery(long lotteryId) {
        return manager.remove(SERVICE_PREFIX + KEY_LOTTERY + lotteryId);
    }

    public void putLotteryAwardList(long lotteryId, List<LotteryAward> awardList) {
        manager.put(SERVICE_PREFIX + KEY_LOTTERY_AWARD_LIST + lotteryId, awardList, TIME_OUT_SEC);
    }

    public List<LotteryAward> getLotteryAwardList(long lotteryId) {
        Object awardList = manager.get(SERVICE_PREFIX + KEY_LOTTERY_AWARD_LIST + lotteryId);
        if (awardList == null) {
            return null;
        }
        return (List<LotteryAward>) awardList;
    }

    public boolean deleteLotteryAwardList(long lotteryId) {
        return manager.remove(SERVICE_PREFIX + KEY_LOTTERY_AWARD_LIST + lotteryId);
    }


    public void putUserLottery(long lotteryId, boolean balvalue) {
        manager.put(SERVICE_PREFIX + KEY_LOTTERY_WANBA_FIVEMONTH + lotteryId, balvalue, TIME_OUT_SEC);
    }

    public boolean getUserLottery(long lotteryId) {
        Object bval = manager.get(SERVICE_PREFIX + KEY_LOTTERY_WANBA_FIVEMONTH + lotteryId);
        if (bval == null) {
            return false;
        }
        return (Boolean) bval;
    }

    public void putLotteryCodeMap(long lotteryId, Map<Integer, LotteryAward> lotteryAwardMap) {
        manager.put(SERVICE_PREFIX + KEY_LOTTERY_WANBA_FIVEMONTH_MAP + lotteryId, lotteryAwardMap, TIME_OUT_SEC);
    }

    public Map<Integer, LotteryAward> getLotteryCodeMap(long lotteryId) {
        Object bval = manager.get(SERVICE_PREFIX + KEY_LOTTERY_WANBA_FIVEMONTH_MAP + lotteryId);
        if (bval == null) {
            return null;
        }
        return (Map<Integer, LotteryAward>) bval;
    }

    public void deleteLotteryCodeMap(long lotteryId) {
        manager.remove(SERVICE_PREFIX + KEY_LOTTERY_WANBA_FIVEMONTH_MAP + lotteryId);

    }

    public void addLotteryCounter(Date date) {
        manager.addOrIncr(LotteryConstants.SERVICE_SECTION + KEY_LOTTERY_COUNT + DateUtil.formatDateToString(date, "yyyyMMddHH"), 1l, 60 * 60 * 2);
    }


    public void addLotteryShareCounter(Date date) {
        manager.addOrIncr(LotteryConstants.SERVICE_SECTION + KEY_LOTTERY_SHARE_COUNT + DateUtil.formatDateToString(date, "yyyyMMddHH"), 1l, 60 * 60 * 2);
    }

    public void addOrIncrTime(Date date, String key) {
        manager.addOrIncr(LotteryConstants.SERVICE_SECTION + key + DateUtil.formatDateToString(date, "yyyyMMddHH"), 1l, 60 * 60 * 2);
    }

}
