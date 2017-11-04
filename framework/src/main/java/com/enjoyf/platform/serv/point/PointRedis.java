/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.point;


import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.redis.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author <a href=mailto:ericliu@staff.joyme.com>Eirc Liu</a>
 */

public class PointRedis {
    //
    private static final Logger logger = LoggerFactory.getLogger(PointRedis.class);
    private static final int TIME_OUT_SEC = 24 * 60 * 60;
    private static final String KEY_PREFIX = PointConstants.SERVICE_SECTION;
    private static final String SECKILL_GOODS_TIMES = "_seckill_goods_times_";
    private static final String GOODS_SECKILL_BY_ENDTIME = "_goods_seckill_by_endtime_";
    private static final String GIFT_LETTER = "_gift_letter_";
    private static final String GIFT_ALL_LIST = "_gift_all_list_ids_";
    private static final String MY_GIFT = "_my_gift_";

    private static final String WANBA_POINT_RULE = "_point_rule_";
    private static final String WANBA_QUESTION_POINT = KEY_PREFIX + "_wanba_question_point_";//玩霸回答问题获得积分


    private static final String RANK_LIST = KEY_PREFIX + "_rank_list_by_";  //排行榜
    private static final String USER_PRESTIGE_LIST = KEY_PREFIX + "_user_prestige_list_";//用户的声望值

    private static final String LOTTERY_NUM = KEY_PREFIX + "_lottery_num_"; //宝箱数量

    private static final String USER_CHOOSE_GIFT = KEY_PREFIX + "_user_choose_gift_"; //用户选择的道具
    private static final String USER_POINT_BY_DAY = KEY_PREFIX + "_user_point_by_day_"; //用户选择的道具

    private static final String QUERY_WORSHIP_BY_PROFILEID = KEY_PREFIX + "_query_worship_by_profileid_";//查询膜拜该用户人的列表
    private static final String WORSHIP_NUM_BY_PROFILEID = KEY_PREFIX + "_worship_num_by_profileid_";//被膜拜的总次数

    private static final String MYPOINT_LIST = KEY_PREFIX + "_my_point_"; //我的积分


    private static Map<String, String> letterMap = new HashMap<String, String>();

    static {
        letterMap.put("a", "abc");
        letterMap.put("b", "abc");
        letterMap.put("c", "abc");

        letterMap.put("d", "def");
        letterMap.put("e", "def");
        letterMap.put("f", "def");

        letterMap.put("g", "ghi");
        letterMap.put("h", "ghi");
        letterMap.put("i", "ghi");

        letterMap.put("j", "jkl");
        letterMap.put("k", "jkl");
        letterMap.put("l", "jkl");

        letterMap.put("m", "mno");
        letterMap.put("n", "mno");
        letterMap.put("o", "mno");


        letterMap.put("p", "pqr");
        letterMap.put("q", "pqr");
        letterMap.put("r", "pqr");

        letterMap.put("s", "stu");
        letterMap.put("t", "stu");
        letterMap.put("u", "stu");

        letterMap.put("v", "vw");
        letterMap.put("w", "vw");

        letterMap.put("x", "xyz");
        letterMap.put("y", "xyz");
        letterMap.put("z", "xyz");
    }

    private RedisManager manager;

    public PointRedis(FiveProps p) {
        manager = new RedisManager(p);
    }

    private final String INCREASE = "increase";
    private final String REDUCE = "reduce";

    public void putMyPoint(PointActionHistory pointActionHistory) {
        if (pointActionHistory.getPointValue() > 0) {
            manager.zadd(MYPOINT_LIST + INCREASE + "_" + pointActionHistory.getProfileId() + "_" + pointActionHistory.getPointkey(), pointActionHistory.getActionDate().getTime() / 1000, String.valueOf(pointActionHistory.getActionHistoryId()), -1);
        } else if (pointActionHistory.getPointValue() < 0) {
            manager.zadd(MYPOINT_LIST + REDUCE + "_" + pointActionHistory.getProfileId() + "_" + pointActionHistory.getPointkey(), pointActionHistory.getActionDate().getTime() / 1000, String.valueOf(pointActionHistory.getActionHistoryId()), -1);
        }
    }

    public PageRows<String> queryMyPoint(String profileId, String type, String pointKey, Pagination page) {
        String myPointKey = MYPOINT_LIST + type + "_" + profileId + "_" + pointKey;
        Set<String> ids = manager.zrange(myPointKey, page.getStartRowIdx(), page.getEndRowIdx(), RedisManager.RANGE_ORDERBY_DESC);
        PageRows<String> pageRows = null;
        if (!CollectionUtil.isEmpty(ids)) {
            pageRows = new PageRows<String>();
            page.setTotalRows((int) manager.zcard(myPointKey));
            pageRows.setPage(page);
            List<String> idList = new ArrayList<String>();
            for (String id : ids) {
                idList.add(id);
            }
            pageRows.setRows(idList);
        }
        return pageRows;
    }


    public Long incrSeckillGoodsTimes(long seckillId, long inrcby) {
        return manager.incr(KEY_PREFIX + SECKILL_GOODS_TIMES + seckillId, inrcby, 0);
    }

    public Set<String> getGoodsSeckillByEndTime(GoodsActionType goodsActionType, Long goodsId, Pagination pagination) {
        String key = KEY_PREFIX + GOODS_SECKILL_BY_ENDTIME + goodsActionType.getCode();
        if (goodsId != null && goodsId > 0l) {
            key += ("_" + goodsId);
        }
        //结束时间大于当前时间，到正无穷，的开区间
        if (pagination == null) {
            return manager.zrangeByScore(key, "(" + (System.currentTimeMillis() / 1000), "+inf");
        } else {
            return manager.zrangeByScoreOffset(key, "(" + (System.currentTimeMillis() / 1000), "+inf", pagination.getStartRowIdx(), pagination.getPageSize());
        }
    }

    public void putGoodsSeckillByEndTime(GoodsActionType goodsActionType, Long goodsId, GoodsSeckill goodsSeckill) {
        String key = KEY_PREFIX + GOODS_SECKILL_BY_ENDTIME + goodsActionType.getCode();
        manager.zadd(key, goodsSeckill.getEndTime().getTime() / 1000, String.valueOf(goodsSeckill.getSeckillId()), 0);
        if (goodsId != null && goodsId > 0l) {
            key += ("_" + goodsId);
        }
        manager.zadd(key, goodsSeckill.getStartTime().getTime() / 1000, String.valueOf(goodsSeckill.getSeckillId()), 0);
    }

    public void removeGoodsSeckillByEndTime(GoodsActionType goodsActionType, Long goodsId, GoodsSeckill goodsSeckill) {
        String key = KEY_PREFIX + GOODS_SECKILL_BY_ENDTIME + goodsActionType.getCode();
        manager.zrem(key, String.valueOf(goodsSeckill.getSeckillId()));
        if (goodsId != null && goodsId > 0l) {
            key += ("_" + goodsId);
        }
        manager.zrem(key, String.valueOf(goodsSeckill.getSeckillId()));
    }

    //只用于礼包
    public void putGiftLetterCache(ActivityGoods activityGoods) {
        //单字母缓存
        String firstLetterKey = KEY_PREFIX + GIFT_LETTER + activityGoods.getActivityType().getCode() + "_" + activityGoods.getFirstLetter().toLowerCase();
        manager.zadd(firstLetterKey, activityGoods.getDisplayOrder(), String.valueOf(activityGoods.getActivityGoodsId()), -1);
        //字母分组 缓存
        String letter = letterMap.containsKey(activityGoods.getFirstLetter().toLowerCase()) ? letterMap.get(activityGoods.getFirstLetter().toLowerCase()) : "others";
        String letterGroupKey = KEY_PREFIX + GIFT_LETTER + activityGoods.getActivityType().getCode() + "_" + letter;
        manager.zadd(letterGroupKey, activityGoods.getDisplayOrder(), String.valueOf(activityGoods.getActivityGoodsId()), -1);
        //全部
        String allListKey = KEY_PREFIX + GIFT_ALL_LIST + activityGoods.getActivityType().getCode();
        manager.zadd(allListKey, activityGoods.getDisplayOrder(), String.valueOf(activityGoods.getActivityGoodsId()), -1);
    }

    public void removeGiftLetterCache(ActivityGoods activityGoods, String oldLetter) {
        String letterKey = KEY_PREFIX + GIFT_LETTER + activityGoods.getActivityType().getCode() + "_" + oldLetter.toLowerCase();
        manager.zrem(letterKey, String.valueOf(activityGoods.getActivityGoodsId()));

        String letter = letterMap.containsKey(oldLetter.toLowerCase()) ? letterMap.get(oldLetter.toLowerCase()) : "others";
        String groupKey = KEY_PREFIX + GIFT_LETTER + activityGoods.getActivityType().getCode() + "_" + letter;
        manager.zrem(groupKey, String.valueOf(activityGoods.getActivityGoodsId()));
        String allKey = KEY_PREFIX + GIFT_ALL_LIST + activityGoods.getActivityType().getCode();
        manager.zrem(allKey, String.valueOf(activityGoods.getActivityGoodsId()));
    }

    public PageRows<String> getActivityGoodsAllListIds(ActivityType activityType, Pagination page) {
        PageRows<String> pageRows = null;
        String allListKey = KEY_PREFIX + GIFT_ALL_LIST + activityType.getCode();
        Set<String> ids = manager.zrange(allListKey, page.getStartRowIdx(), page.getEndRowIdx(), RedisManager.RANGE_ORDERBY_ASC);
        if (CollectionUtil.isEmpty(ids)) {
            pageRows = new PageRows<String>();
            page.setTotalRows((int) manager.zcard(allListKey));
            pageRows.setPage(page);
            List<String> idList = new ArrayList<String>();
            for (String id : ids) {
                idList.add(id);
            }

            pageRows.setRows(idList);
        }
        return pageRows;
    }

    public PageRows<String> getGiftIdsByLetter(ActivityType activityType, String firstLetter, Pagination page) {
        String firstLetterKey = KEY_PREFIX + GIFT_LETTER + activityType.getCode() + "_" + firstLetter.toLowerCase();
        PageRows<String> pageRows = null;
        Set<String> ids = manager.zrange(firstLetterKey, (page == null ? 0 : page.getStartRowIdx()), (page == null ? -1 : page.getEndRowIdx()), RedisManager.RANGE_ORDERBY_ASC);
        if (!CollectionUtil.isEmpty(ids)) {
            pageRows = new PageRows<String>();
            int totals = (int) manager.zcard(firstLetterKey);
            if (page == null) {
                page = new Pagination(totals, 1, totals);
            } else {
                page.setTotalRows(totals);
            }
            pageRows.setPage(page);
            List<String> idList = new ArrayList<String>();
            for (String id : ids) {
                idList.add(id);
            }
            pageRows.setRows(idList);
        }
        return pageRows;
    }

    public void putMyGiftCache(String profileId, String appKey, UserExchangeLog userExchangeLog) {
        String myGiftKey = KEY_PREFIX + MY_GIFT + profileId + appKey;
        manager.zadd(myGiftKey, userExchangeLog.getExhangeTime().getTime() / 1000, String.valueOf(userExchangeLog.getUserExchangeLogId()), -1);
    }

    public PageRows<String> getMyGiftCache(String profileId, String appKey, Pagination page) {
        String myGiftKey = KEY_PREFIX + MY_GIFT + profileId + appKey;
        PageRows<String> pageRows = null;
        Set<String> ids = manager.zrange(myGiftKey, page.getStartRowIdx(), page.getEndRowIdx(), RedisManager.RANGE_ORDERBY_DESC);
        if (!CollectionUtil.isEmpty(ids)) {
            pageRows = new PageRows<String>();
            page.setTotalRows((int) manager.zcard(myGiftKey));
            pageRows.setPage(page);
            List<String> idList = new ArrayList<String>();
            for (String id : ids) {
                idList.add(id);
            }

            pageRows.setRows(idList);
        }
        return pageRows;
    }

    public PageRows<String> getMyGiftCacheByScore(String profileId, String appKey, Pagination page, Date startTime, Date endTime) {
        String myGiftKey = KEY_PREFIX + MY_GIFT + profileId + appKey;
        PageRows<String> pageRows = null;
        Set<String> ids = manager.zrangeByScoreOffset(myGiftKey, startTime.getTime() / 1000, endTime.getTime() / 1000, (-1 - page.getStartRowIdx()), (-1 - page.getEndRowIdx()));
        if (CollectionUtil.isEmpty(ids)) {
            pageRows = new PageRows<String>();
            page.setTotalRows((int) manager.zcard(myGiftKey));
            pageRows.setPage(page);
            List<String> idList = new ArrayList<String>();
            for (String id : ids) {
                idList.add(id);
            }

            pageRows.setRows(idList);
        }
        return pageRows;
    }

    public int getPointRule(String key) {
        String value = manager.get(KEY_PREFIX + WANBA_POINT_RULE + key);
        if (StringUtil.isEmpty(value)) {
            return -1;
        }
        return Integer.parseInt(value);
    }

    public Long pointRuleIncr(String key, long value) {
        return manager.incr(KEY_PREFIX + WANBA_POINT_RULE + key, value, -1);
    }

    public int getWanbaQuestionPoint(String key) {
        String value = manager.get(WANBA_QUESTION_POINT + key);
        if (StringUtil.isEmpty(value)) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    public Long incrWanbaQuestionPoint(String key, long value) {
        return manager.incr(WANBA_QUESTION_POINT + key, value, -1);
    }

    //更新排行榜
    public void pushRankList(String key, String profileId, int value) {
        String weekKey = DateUtil.getRankLinKey(key, Calendar.DAY_OF_WEEK);
        String monthKey = DateUtil.getRankLinKey(key, Calendar.DAY_OF_MONTH);
        String allKey = DateUtil.getRankLinKey(key, PointLogic.RANK_ALL);
        if (value > 0) {
            //周排行
            manager.zincrby(RANK_LIST + weekKey, (double) value, profileId);
            //月排行
            manager.zincrby(RANK_LIST + monthKey, (double) value, profileId);
            //总排行
            manager.zincrby(RANK_LIST + allKey, (double) value, profileId);
        }
    }

    public void updateUserPrestige(String profileId, int value, int sumValue) {
        //更新用户的总积分
        manager.zadd(USER_PRESTIGE_LIST, (double) sumValue, profileId, -1);
        //放入操作记录 用于推送每月声望有变化的用户
        String monthKey = DateUtil.getRankLinKey(PointLogic.PRESTIGE_RANK_TYPE, Calendar.DAY_OF_MONTH);
        manager.zincrby(USER_PRESTIGE_LIST + monthKey, (double) value, profileId);

        pushRankList(PointLogic.PRESTIGE_RANK_TYPE, profileId, value); //更新声望排行
    }

    //查询排行榜信息
    public Map<String, Integer> queryRankList(String key, Pagination pagination) {
        Set<String> rankSet = manager.zrange(RANK_LIST + key, pagination.getStartRowIdx(), pagination.getEndRowIdx(), RedisManager.RANGE_ORDERBY_DESC);
        if (CollectionUtil.isEmpty(rankSet)) {
            return null;
        }
        Map<String, Integer> returnMap = new LinkedHashMap<String, Integer>();

        for (String pid : rankSet) {
            returnMap.put(pid, getRankScore(key, pid));
        }
        return returnMap;
    }

    public int queryPrestigeSize(String key) {
        Long size = manager.zcard(USER_PRESTIGE_LIST + key);
        if (size == null) {
            return 0;
        }
        return size.intValue();
    }

    //获得排行榜内某个用户的分数
    public int getRankScore(String key, String pid) {
        Double score = manager.zscore(RANK_LIST + key, pid);
        if (score == null) {
            return 0;
        }
        return score.intValue();
    }

    //按月查询声望有变化的用户
    public Map<String, Integer> queryPretigeByMonth(String key, Pagination pagination) {
        Set<String> rankSet = manager.zrange(USER_PRESTIGE_LIST + key, pagination.getStartRowIdx(), pagination.getEndRowIdx(), RedisManager.RANGE_ORDERBY_DESC);
        if (CollectionUtil.isEmpty(rankSet)) {
            return null;
        }
        Map<String, Integer> returnMap = new LinkedHashMap<String, Integer>();

        for (String pid : rankSet) {
            returnMap.put(pid, getRankScore(key, pid));
        }
        return returnMap;
    }

    /**
     * 查询用户的声望排行
     *
     * @param pids
     * @return map<ProfileID,排名>
     */
    public Map<String, Integer> queryUserPrestigeRanks(Set<String> pids) {
        Map<String, Integer> returnMap = new HashMap<String, Integer>();
        for (String pid : pids) {
            Long rank = manager.zrank(USER_PRESTIGE_LIST, pid, RedisManager.RANGE_ORDERBY_DESC);
            if (rank == null) {
                returnMap.put(pid, -1);
            }
            returnMap.put(pid, rank.intValue());
        }
        return returnMap;
    }

    /**
     * 查询用户声望值
     *
     * @param pids
     * @return <pid,声望值>
     */
    public Map<String, Integer> queryUserPresige(Set<String> pids) {
        Map<String, Integer> returnMap = new HashMap<String, Integer>();
        for (String pid : pids) {
            int presige = getUserPresige(pid);
            returnMap.put(pid, presige);
        }
        return returnMap;
    }

    //获得用户的声望值
    public int getUserPresige(String pid) {
        Double score = manager.zscore(USER_PRESTIGE_LIST, pid);
        if (score == null) {
            return 0;
        }
        return score.intValue();
    }


    //进方法后减少用户抽奖次数，防止并发
    public int decrLotteryNum(String pid) {
        Long num = manager.incr(LOTTERY_NUM + pid, -1, 0);
        if (num < 0) {   //如果是负数 表示次数是0 返回-1；
            manager.set(LOTTERY_NUM + pid, "0");
            return -1;
        }
        return num.intValue();
    }

    //增加抽奖次数
    public void incrLotteryNum(String pid, int num) {
        manager.incr(LOTTERY_NUM + pid, num, 0);
    }

    public int getLotteryNum(String pid) {
        String num = manager.get(LOTTERY_NUM + pid);
        if (StringUtil.isEmpty(num)) {
            return 0;
        }
        return Integer.parseInt(num);
    }

    public void putUserChooseGift(String profileId, LotteryType lotteryType, String picKey) {
        manager.hset(USER_CHOOSE_GIFT + profileId, "lotteryType_" + lotteryType.getCode(), picKey);
    }

    public Map<String, String> getUserChooseGift(String profileId) {
        return manager.hgetAll(USER_CHOOSE_GIFT + profileId);
    }


    public void putUserPointByDay(String profileId, int value) {
        manager.incr(USER_POINT_BY_DAY + DateUtil.formatDateToString(new Date(), DateUtil.PATTERN_DATE_SHORT) + profileId, value, 60 * 60 * 24 * 1000);
    }

    public int getUserPointByDay(String profileId) {
        String num = manager.get(USER_POINT_BY_DAY + DateUtil.formatDateToString(new Date(), DateUtil.PATTERN_DATE_SHORT) + profileId);
        if (StringUtil.isEmpty(num)) {
            return 0;
        }
        return Integer.parseInt(num);
    }

    /**
     * @param profileId     //被膜拜人的PID
     * @param destProfileId //膜拜人的PID
     * @param num//         次数
     */
    public void putWorship(String profileId, String destProfileId, int num) {
        manager.zincrby(QUERY_WORSHIP_BY_PROFILEID + profileId, (double) num, destProfileId);  //膜拜人
        manager.zincrby(WORSHIP_NUM_BY_PROFILEID, (double) num, profileId);//被膜拜的总次数
    }

    public PageRows<String> queryWorship(String profileId, Pagination pagination) {
        Set<String> worshipSet = manager.zrange(QUERY_WORSHIP_BY_PROFILEID + profileId, pagination.getStartRowIdx(), pagination.getEndRowIdx(), RedisManager.RANGE_ORDERBY_DESC);
        Long count = manager.zcard(QUERY_WORSHIP_BY_PROFILEID + profileId);
        pagination.setTotalRows(count.intValue());

        PageRows<String> queryWorhipPage = new PageRows<String>();

        if (CollectionUtil.isEmpty(worshipSet)) {
            queryWorhipPage.setPage(pagination);
            return queryWorhipPage;
        }

        List<String> queryWorship = new ArrayList<String>();
        queryWorship.addAll(worshipSet);
        queryWorhipPage.setPage(pagination);
        queryWorhipPage.setRows(queryWorship);

        return queryWorhipPage;
    }

    //查询用户被膜拜次数
    public int getWorshipNum(String profileId) {
        Double num = manager.zscore(WORSHIP_NUM_BY_PROFILEID, profileId);
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }


    public static void main(String agres[]) {
        FiveProps fiveProps = Props.instance().getServProps();
        final PointLogic pointLogic = new PointLogic(new PointConfig(fiveProps));
        final PointRedis pointRedis = new PointRedis(fiveProps);
        ExecutorService exec = Executors.newCachedThreadPool();
        final Semaphore semp = new Semaphore(10);
        RedisManager manager = new RedisManager(fiveProps);
//        try {
//
//            String monthKey = DateUtil.getRankLinKey(PointLogic.PRESTIGE_RANK_TYPE, Calendar.DAY_OF_MONTH);
//            Map<String, Integer> map = pointRedis.queryRankList(monthKey, new Pagination(1000, 1, 1000));
//            for (String ac : map.keySet()) {
//                manager.zadd(USER_PRESTIGE_LIST + monthKey, (double) map.get(ac), ac, -1);
//            }
        manager.zincrby(USER_PRESTIGE_LIST + "_xupengtest", (double) -2, "xupengtest");
//            Set<String> aa = map.keySet();
//            PageRows<UserPoint> page = pointLogic.queryUserPointByPage(new QueryExpress().add(QueryCriterions.eq(UserPointField.POINTKEY, "newsyhb"))
//                    .add(QueryCriterions.in(UserPointField.PROFILEID, aa.toArray())), new Pagination(1000, 1, 1000));
//            for (UserPoint userPoint : page.getRows()) {
//                manager.zadd(USER_PRESTIGE_LIST, (double) userPoint.getPrestige(), userPoint.getProfileId(), -1);
//            }
//
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }

        //  pointRedis.pushRankList("prestige_20161101_5", "3bda115aa509a02f93a9c03b70077a07", 2000);
//        for (int index = 0; index < 200; index++) {
//            final int NO = index;
//            Runnable run = new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        // 获取许可
//                        semp.acquire();
//                        try {
//                            int i = pointLogic.exchangeGiftBox(1, "886f8fb76c0cc3ca3e7d9fb2552bed09", "default");
//                            System.out.println(i);
//                        } catch (ServiceException e) {
//                            if (e.equals(PointServiceException.GIFT_LOTTERY_NOT_ENOUGH)) {
//                                System.out.println("第" + NO + "次==GIFT_LOTTERY_NOT_ENOUGH");
//                            } else if (e.equals(PointServiceException.GIFT_LOTTERY_NOT_EXISTS)) {
//                                System.out.println("第" + NO + "次==GIFT_LOTTERY_NOT_EXISTS");
//                            } else if (e.equals(PointServiceException.GIFT_LOTTERY_EXIST)) {
//                                System.out.println("第" + NO + "次==GIFT_LOTTERY_EXIST" + e.getName());
//                            } else {
//                                System.out.println("第" + NO + "次==SYSTEM_ERROR");
//                            }
//                        }
//
//                        // System.out.println(result);
//                        // Thread.sleep((long) (Math.random()) * 1000);
//                        // 释放
//
//                        semp.release();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//            exec.execute(run);
//        }
////         退出线程池
//        exec.shutdown();
//        int aa = pointRedis.getLotteryNum("xupengtest");
//        System.out.println(aa);
    }


}




