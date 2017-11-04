package com.enjoyf.platform.serv.point;

import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;
import com.enjoyf.util.MD5Util;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-6-7
 * Time: 下午12:05
 * To change this template use File | Settings | File Templates.
 */
public class PointCache {

    private static final long TIME_OUT_SEC = 5l * 60l;
    private static final long HOT_TIME_OUT_SEC = 60 * 60;

    private static final String CACHE_EXCHANGE_CODE_POOL_PREFIX = "exchange_code_pool_";
    private static final String CACHE_USER_RECENT_LOG = "user_recent_log";

    private static final String CACHE_GET_CODE_COUNTER = "_gccounter_";
    private static final String KAY_POINT_INCRESE_COUNTER = "_pointincresecounter_";
    private static final String KAY_POINT_CONSUME_COUNTER = "_pointconsumecounter_";
    private static final String KAY_POINT_USEREXCHANGELOG = "_pointuser_exchangelog_";

    private static final String KEY_QUERY_GIFTMARKET = "_giftmarket_index_";
    private static final String KEY_QUERY_FIRST_LETTER = "_query_first_letter_";
    private static final String KEY_GET_ACTIVITY_GOODS_BY_CACHE = "get_activity_by_cache_";
    private static final String KEY_MEMCACHE_ACTIVITY_HOT_RANKS = "activity_hot_ranks";
    private static final String KEY_QUERY_ACTIVITY_GOODS_BY_GAME_ID = "_queryactivitygoods_gameid_";

    private static final String KEY_GOODS_SECKILL = "_goods_seckill_";

    private static final String KEY_USER_EXCHANGE_LOG = "_user_exchange_log_id_";

    private static final String KEY_GET_USER_LOTTERY_LOG = PointConstants.SERVICE_PREFIX + "_user_lottery_log_id_";
    private static final String KEY_QUERY_GIFT_LOTTERY_LIST = PointConstants.SERVICE_PREFIX + "_query_gift_lottery_list_";


    private static final String KEY_QUERY_LOTTERY_LOG_BY_CACHE = PointConstants.SERVICE_PREFIX + "_query_lottery_log_by_cache_";
    private static final String KEY_GET_POINT_HISTORY_BY_CACHE = PointConstants.SERVICE_PREFIX + "_get_point_history_by_cache_";

    private static final String KEY_USER_POINT_EXIST_CACHE = PointConstants.SERVICE_PREFIX + "_get_user_point_exist_cache_";

    private MemCachedConfig config;

    private MemCachedManager manager;


    public PointCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(this.config);
    }

    public void putUserPointExistCache(String profileId, String pointkey) {
        String key = MD5Util.Md5(profileId + pointkey);
        manager.put(KEY_USER_POINT_EXIST_CACHE + key, "true", -1);
    }

    public boolean getUserPointExistCache(String profileId, String pointkey) {
        String key = MD5Util.Md5(profileId + pointkey);
        Object userPointKey = manager.get(KEY_USER_POINT_EXIST_CACHE + key);
        if (userPointKey == null) {
            return false;
        }
        return true;
    }

    public void putExchangeGoodsItemList(long goodsId, List<ExchangeGoodsItem> exchangeGoodsItemList) {
        manager.put(CACHE_EXCHANGE_CODE_POOL_PREFIX + goodsId, exchangeGoodsItemList, TIME_OUT_SEC);
    }

    public List<ExchangeGoodsItem> getExchangeGoodsItemList(long goodsId) {
        Object exchgneGoddsItemList = manager.get(CACHE_EXCHANGE_CODE_POOL_PREFIX + goodsId);
        if (exchgneGoddsItemList == null) {
            return null;
        }
        return (List<ExchangeGoodsItem>) exchgneGoddsItemList;
    }

    public boolean deleteExchangeGoodsItemList(long goodsId) {
        return manager.remove(CACHE_EXCHANGE_CODE_POOL_PREFIX + goodsId);
    }


    public void putUserRecentLog(List<UserRecentLogEntry> entryList) {
        manager.put(CACHE_USER_RECENT_LOG, entryList, TIME_OUT_SEC);
    }

    public List<UserRecentLogEntry> getUserRecentLog() {
        Object userRecentLogList = manager.get(CACHE_USER_RECENT_LOG);
        if (userRecentLogList == null) {
            return null;
        }
        return (List<UserRecentLogEntry>) userRecentLogList;
    }

    public boolean removeUserRecentLog() {
        return manager.remove(CACHE_USER_RECENT_LOG);
    }


    //pointservice_gccounter_2015042110
    public void addGetCodeCounter(Date date) {
        manager.addOrIncr(PointConstants.SERVICE_SECTION + CACHE_GET_CODE_COUNTER + DateUtil.formatDateToString(date, "yyyyMMddHH"), 1l, 60 * 60 * 2);
    }

    //pointservice_gccounter_2015042110
    public Long getGetCodeCounter(Date date) {
        Object obj = manager.get(PointConstants.SERVICE_SECTION + CACHE_GET_CODE_COUNTER + DateUtil.formatDateToString(date, "yyyyMMddHH"));

        if (obj != null) {
            return Long.valueOf(obj.toString());
        }

        return null;
    }

    public void addPointIncreseCounter(Date date) {
        manager.addOrIncr(PointConstants.SERVICE_SECTION + KAY_POINT_INCRESE_COUNTER + DateUtil.formatDateToString(date, "yyyyMMddHH"), 1l, 60 * 60 * 2);
    }

    public void addPointConsumeCounter(Date date) {
        manager.addOrIncr(PointConstants.SERVICE_SECTION + KAY_POINT_CONSUME_COUNTER + DateUtil.formatDateToString(date, "yyyyMMddHH"), 1l, 60 * 60 * 2);
    }

    public void putUserExchangeLog(String profileId, long goodsId) {
        manager.put(KAY_POINT_USEREXCHANGELOG + profileId + goodsId, profileId + goodsId, 60 * 60 * 12);
    }

    public String getUserExchangeLog(String profileId, long goodsId) {
        Object infoList = manager.get(KAY_POINT_USEREXCHANGELOG + profileId + goodsId);
        if (infoList == null) {
            return null;
        }
        return (String) infoList;
    }

    public boolean deleteUserExchangeLog(String profileId, long goodsId) {
        return manager.remove(KAY_POINT_USEREXCHANGELOG + profileId + goodsId);
    }


    //////////////礼包中心首页////////////////////

    public void putActivityGoodsIndex(List<ActivityGoods> list, String type) {
        manager.put(KEY_QUERY_GIFTMARKET + type, list, 60 * 30);
    }

    public boolean deleteActivityGoodsIndex(String type) {
        return manager.remove(KEY_QUERY_GIFTMARKET + type);
    }

    public List<ActivityGoods> getActivityGoodsIndex(String type) {
        Object activityGoodsIndexList = manager.get(KEY_QUERY_GIFTMARKET + type);
        if (activityGoodsIndexList == null) {
            return null;
        }
        return (List<ActivityGoods>) activityGoodsIndexList;
    }

    //按首字母缓存的列表
    public void putActivityGoodsByFristLetter(String firstLetter, List<ActivityGoods> list) {
        manager.put(KEY_QUERY_FIRST_LETTER + firstLetter, list, 60 * 30);
    }

    public boolean deleteActivityGoodsByFristLetter(String firstLetter) {
        return manager.remove(KEY_QUERY_FIRST_LETTER + firstLetter);
    }

    public List<ActivityGoods> getActivityGoodsByFristLetter(String firstLetter) {
        Object activityGoodsList = manager.get(KEY_QUERY_FIRST_LETTER + firstLetter);
        if (activityGoodsList == null) {
            return null;
        }
        return (List<ActivityGoods>) activityGoodsList;
    }
    //   getActivityByCACHE


    public void putActivityGoodsByCache(long id, ActivityGoods activityGoods) {
        manager.put(KEY_GET_ACTIVITY_GOODS_BY_CACHE + id, activityGoods, 60 * 30);
    }

    public boolean deleteActivityGoodsByCache(long id) {
        return manager.remove(KEY_GET_ACTIVITY_GOODS_BY_CACHE + id);
    }

    public ActivityGoods getActivityGoodsByCache(long activityId) {
        Object activityGoods = manager.get(KEY_GET_ACTIVITY_GOODS_BY_CACHE + activityId);
        if (activityGoods == null) {
            return null;
        }
        return (ActivityGoods) activityGoods;
    }

    /////////////////////
    /////////////////////////////
    public List<ActivityHotRanks> getActivityHotRanksList() {
        Object activity = manager.get(KEY_MEMCACHE_ACTIVITY_HOT_RANKS);
        if (activity == null) {
            return null;
        }
        return (List<ActivityHotRanks>) activity;
    }

    public void putActivityHotRanks(List<ActivityHotRanks> list) {
        manager.put(KEY_MEMCACHE_ACTIVITY_HOT_RANKS, list, HOT_TIME_OUT_SEC);
    }

    public boolean removeActivityHotRanks() {
        return manager.remove(KEY_MEMCACHE_ACTIVITY_HOT_RANKS);
    }

    public void putActivityGoodsByGameId(long gameDbId, List<ActivityGoods> list) {
        manager.put(PointConstants.SERVICE_SECTION + KEY_QUERY_ACTIVITY_GOODS_BY_GAME_ID + gameDbId, list, 60l * 30l);
    }

    public List<ActivityGoods> getActivityGoodsByGameId(long gameDbId) {
        Object list = manager.get(PointConstants.SERVICE_SECTION + KEY_QUERY_ACTIVITY_GOODS_BY_GAME_ID + gameDbId);
        if (list == null) {
            return null;
        }
        return (List<ActivityGoods>) list;
    }

    public boolean removeActivityGoodsByGameId(long gameDbId) {
        return manager.remove(PointConstants.SERVICE_SECTION + KEY_QUERY_ACTIVITY_GOODS_BY_GAME_ID + gameDbId);
    }

    public void putGoodsSeckill(long seckillId, GoodsSeckill goodsSeckill) {
        manager.put(PointConstants.SERVICE_PREFIX + KEY_GOODS_SECKILL + seckillId, goodsSeckill, 6l * 60l * 60l);
    }

    public GoodsSeckill getGoodsSeckill(long seckillId) {
        Object seckill = manager.get(PointConstants.SERVICE_PREFIX + KEY_GOODS_SECKILL + seckillId);
        if (seckill == null) {
            return null;
        }
        return (GoodsSeckill) seckill;
    }

    public boolean removeGoodsSeckill(long seckillId) {
        return manager.remove(PointConstants.SERVICE_PREFIX + KEY_GOODS_SECKILL + seckillId);
    }

    public UserExchangeLog getUserExchangeLogById(String profileId, long aid, long logId) {
        Object log = manager.get(PointConstants.SERVICE_PREFIX + KEY_USER_EXCHANGE_LOG + profileId + "_" + aid + "_" + logId);
        if (log == null) {
            return null;
        }
        return (UserExchangeLog) log;
    }

    public void putUserExchangeLogById(String profileId, long aid, long logId, UserExchangeLog exchangeLog) {
        manager.put(PointConstants.SERVICE_PREFIX + KEY_USER_EXCHANGE_LOG + profileId + "_" + aid + "_" + logId, exchangeLog, 30l * 60l);
    }


    public void putUserLotteryLog(UserLotteryLog userLotteryLog) {
        if (userLotteryLog == null) {
            return;
        }
        manager.put(KEY_GET_USER_LOTTERY_LOG + userLotteryLog.getUserLotteryLogId(), userLotteryLog, 60l * 60l * 24l);
    }

    public UserLotteryLog getUserLotteryLog(String userLotteryLogId) {
        Object log = manager.get(KEY_GET_USER_LOTTERY_LOG + userLotteryLogId);
        if (log == null) {
            return null;
        }
        return (UserLotteryLog) log;
    }

    public void putGiftLotteryList(List<GiftLottery> giftLotteries) {
        manager.put(KEY_QUERY_GIFT_LOTTERY_LIST, giftLotteries, 60l * 60l * 24l);
    }

    public List<GiftLottery> queryGiftLotteryList() {
        Object log = manager.get(KEY_QUERY_GIFT_LOTTERY_LIST);
        if (log == null) {
            return null;
        }
        return (List<GiftLottery>) log;
    }

    public boolean removeGiftLotteryList() {
        return manager.remove(KEY_QUERY_GIFT_LOTTERY_LIST);
    }


    public void putGiftLogByCache(String profileId, List<UserLotteryLog> list) {
        manager.put(KEY_QUERY_LOTTERY_LOG_BY_CACHE + profileId, list, 60l * 60l * 24l);
    }

    public List<UserLotteryLog> queryGiftLotteryLogByCache(String profileId) {
        Object log = manager.get(KEY_QUERY_LOTTERY_LOG_BY_CACHE + profileId);
        if (log == null) {
            return null;
        }
        return (List<UserLotteryLog>) log;
    }

    public boolean removeGiftLotteryLogByCache(String profileId) {
        return manager.remove(KEY_QUERY_LOTTERY_LOG_BY_CACHE + profileId);
    }


    public void putPointActionHistory(Long historyId, PointActionHistory pointActionHistory) {
        manager.put(KEY_GET_POINT_HISTORY_BY_CACHE + historyId, pointActionHistory, 60l * 60l * 24l * 30l);
    }

    public PointActionHistory getMyPointHisotryByCache(Long historyId) {
        Object log = manager.get(KEY_GET_POINT_HISTORY_BY_CACHE + historyId);
        if (log == null) {
            return null;
        }
        return (PointActionHistory) log;
    }


    public static void main(String agres[]) {
        FiveProps props = Props.instance().getServProps();
        PointCache pointCache = new PointCache(new MemCachedConfig(props));

        pointCache.removeGiftLotteryList();
    }
}