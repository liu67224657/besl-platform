package com.enjoyf.platform.serv.point;


import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.db.point.PointHandler;
import com.enjoyf.platform.serv.point.quartz.PrestigeQuartzCronTrigger;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.IntRemoveStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.content.GoodsActionType;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.HotActivityEvent;
import com.enjoyf.platform.service.event.system.UserPointEvent;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.point.pointwall.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.http.AppUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.util.timer.FixedRateTimerManagerPool;
import com.enjoyf.platform.util.timer.TimerTasker;
import com.enjoyf.util.MD5Util;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: liangtang
 * Date: 13-5-29
 * Time: 下午1:57
 * To change this template use File | Settings | File Templates.
 */
public class PointLogic implements PointService {


    private static final Logger logger = LoggerFactory.getLogger(PointLogic.class);
    private static final int QUERY_HOT_ACTIVITY_PAGE_NO = 1;
    private static final int QUERY_HOT_ACTIVITY_PAGE_SIZE_SIX = 8;
    private PointHandler writeAbleHandler;
    private HandlerPool<PointHandler> readonlyHandlersPool;

    private PointConfig config;
    private QueueThreadN eventProcessQueueThreadN = null;

    private static final int TAO_CODE_RESULT_SIZE = 10;
    // private static final long TAO_CODE_INTRAVL_MILLIS = 72l * 60l * 60l * 1000l;
    private static final long TAO_CODE_INTRAVL_MILLIS = 60l * 60l * 1l * 1000l;

    private static final String GIFTMARKET_INDEX_TYPE_GET = "get";
    private static final String GIFTMARKET_INDEX_TYPE_TAO = "tao";
    private static final int GIFTMARKET_INDEX_SIZE = 8;

    private PointCache pointCache;
    private PointRedis pointRedis;

    private static final int USER_RECENT_LOG_PAGE_SIZE = 20;
    private static final int CURRENT_PAGE = 1;
    private static final String GOODS = "goods";
    private static final String EXCHANGE_GOODS = "exchangeGoods";

    private static final String POINT_RANK_TYPE = "point";
    public static final String PRESTIGE_RANK_TYPE = "prestige";
    private static final String CONSUMPTION_RANK_TYPE = "consumption";
    public static final int RANK_ALL = -1;


    public PointLogic(PointConfig cfg) {
        config = cfg;
        try {
            writeAbleHandler = new PointHandler(cfg.getWriteableDataSourceName(), cfg.getProps());

            readonlyHandlersPool = new HandlerPool<PointHandler>();
            for (String dsn : cfg.getReadonlyDataSourceNames()) {
                readonlyHandlersPool.add(new PointHandler(dsn, cfg.getProps()));
            }

            //init pointCache
            pointCache = new PointCache(cfg.getMemCachedConfig());
            pointRedis = new PointRedis(config.getProps());
            //relaod tao cache
            if (cfg.isCalTaoCodeTimerOpen()) {
                FixedRateTimerManagerPool.get().addTask(new TimerTasker() {
                    @Override
                    public void run() {
                        reloadTaoCode();
                    }
                }, cfg.getCalTaoCodeTimerinterval(), "reCalTaoCodePool");
            }


            if (cfg.isCalPrestigeTimerOpen()) {
                try {
                    //每月声望推送
                    PrestigeQuartzCronTrigger prestigeQuartzCronTrigger = new PrestigeQuartzCronTrigger(this);
                    prestigeQuartzCronTrigger.init();
                    prestigeQuartzCronTrigger.start();
                } catch (SchedulerException e) {
                    e.printStackTrace();
                }

            }
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());
            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        eventProcessQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {

            public void process(Object obj) {
                if (obj instanceof Event) {
                    processQueuedEvent((Event) obj);
                } else {
                    GAlerter.lab(this.getClass().getName() + " eventProcessQueueThreadN, there is a unknown obj." + obj);
                }
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "eventProcessQueue"));

        //定时器
        FixedRateTimerManagerPool.get().addTask(new TimerTasker() {
            @Override
            public void run() {
                reloadUserRecentLog();
            }
        }, 5l * 60L * 1000L, "reloadUserRecentLog");
        //the queue thread n initialize.


//        reloadSevenDaysHotActivity();
        FixedRateTimerManagerPool.get().addTask(new TimerTasker() {
            @Override
            public void run() {
                reloadSevenDaysHotActivity();
            }
        }, 60l * 60l * 1000, "reloadSevenDaysHotActivity");
    }

    private void reloadSevenDaysHotActivity() {
        logger.info("-----------------------reloadSevenDaysHotActivity is start----------------------------------");
//        pointCache.removeActivityHotRanks();
        try {
            List<ActivityHotRanks> list = readonlyHandlersPool.getHandler().activityHotRanksList(new QueryExpress()
                    .add(QueryCriterions.eq(ActivityHotRanksField.REMOVE_STATUS, ActStatus.UNACT.getCode())));
            long lastExchangeTime;
            long now = System.currentTimeMillis();
            long sevenDays = 60 * 60 * 24 * 7 * 1000;
            if (!CollectionUtil.isEmpty(list)) {
                List<Long> longList = new ArrayList<Long>();
                for (ActivityHotRanks activityHotRanks : list) {
                    lastExchangeTime = activityHotRanks.getLastExchangeTime().getTime();
                    if ((lastExchangeTime + sevenDays) < now) {
                        longList.add(activityHotRanks.getActivityHotRanksId());
                    }
                }
                if (!CollectionUtil.isEmpty(longList)) {
                    for (Long id : longList) {
                        PointServiceSngl.get().modifyActivityHotRanks(new UpdateExpress()
                                .set(ActivityHotRanksField.EXCHANGE_NUM, 1)
                                .set(ActivityHotRanksField.LAST_EXCHANGE_TIME, new Date()), new QueryExpress().add(QueryCriterions.eq(ActivityHotRanksField.ACTIVITY_HOT_RANKS_ID, id)));
                    }
                }
                queryActivityHot(new QueryExpress().add(QueryCriterions.eq(ActivityHotRanksField.REMOVE_STATUS, ActStatus.UNACT.getCode()))
                        .add(QuerySort.add(ActivityHotRanksField.EXCHANGE_NUM, QuerySortOrder.DESC)));
            }
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("The event is recieved, event:" + event);
        }

        eventProcessQueueThreadN.add(event);

        return true;
    }


    private void processQueuedEvent(Event event) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to processQueuedEvent:" + event);
        }
        if (event instanceof UserPointEvent) {
            UserPointEvent catEvent = (UserPointEvent) event;
            try {
                PointKeyType pointKeyType = getPointKeyType(catEvent.getAppkey());
                PointActionHistory pointActionHistory = new PointActionHistory();
                pointActionHistory.setUserNo(catEvent.getUno());
                pointActionHistory.setProfileId(catEvent.getProfileId());
                pointActionHistory.setActionDate(catEvent.getActionDate());
                pointActionHistory.setActionDescription(catEvent.getDescription());
                pointActionHistory.setCreateDate(catEvent.getActionDate());
                pointActionHistory.setDestId(catEvent.getObjectId());
                pointActionHistory.setPointValue(catEvent.getPoint());
                pointActionHistory.setActionType(catEvent.getPointActionType());
                pointActionHistory.setPrestige(catEvent.getPrestige());
                pointActionHistory.setHistoryActionType(catEvent.getHistoryActionType());
                pointActionHistory.setAppkey(StringUtil.isEmpty(catEvent.getAppkey()) ? PointKeyType.WWW.getCode() : catEvent.getAppkey());
                pointActionHistory.setPointkey(pointKeyType.getValue());

                if (catEvent.getHistoryActionType().equals(HistoryActionType.PRESTIGE)) {
                    increasePointActionHistory(pointActionHistory, pointKeyType);
                } else {
                    if (catEvent.getPointActionType().equals(PointActionType.WANBA_SIGN)) {
                        int value = pointRedis.getPointRule(pointActionHistory.getDestId()); //获得该key签到次数
                        if (value > 1) {
                            return;
                        }
                    }
                    if (catEvent.getPointActionType().equals(PointActionType.ANSWER)) {
                        pointRedis.incrWanbaQuestionPoint(catEvent.getProfileId(), catEvent.getPoint());
                    }

                    //积分操作
                    increasePointActionHistory(pointActionHistory, pointKeyType);  //modified by tony 2015-05-04
                }
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " processQueuedEvent UserPointEvent occur exception.e", e);
            }
        } else if (event instanceof HotActivityEvent) {
            HotActivityEvent catEvent = (HotActivityEvent) event;
            long goodsId = catEvent.getGoodsId();
            int activityType = catEvent.getActivityType();
            try {
                ActivityHotRanks activityHotRanks = readonlyHandlersPool.getHandler().getActivityHotRanks(new QueryExpress()
                        .add(QueryCriterions.eq(ActivityHotRanksField.GOODS_ID, goodsId))
                        .add(QueryCriterions.eq(ActivityHotRanksField.ACTIVITY_TYPE, activityType)));
                if (activityHotRanks == null) {
                    activityHotRanks = new ActivityHotRanks();
                    ActivityGoods activity = getActivityGoods(goodsId);
                    activityHotRanks.setGoodsId(goodsId);
                    activityHotRanks.setLastExchangeTime(new Date());
                    activityHotRanks.setExchange_num(1);
                    activityHotRanks.setActivityType(ActivityType.getByCode(activityType));
                    activityHotRanks.setActivityId(activity.getActivityGoodsId());
                    activityHotRanks.setActivityName(activity.getActivitySubject());
                    activityHotRanks.setPic(activity.getActivityPicUrl());
                    activityHotRanks.setRemoveStatus(ActStatus.UNACT);
                    writeAbleHandler.insertActivityHotRanks(activityHotRanks);
                } else {
                    long now = System.currentTimeMillis();
                    long sevenDays = 60 * 60 * 24 * 7 * 1000;
                    long lastExchangeTime = activityHotRanks.getLastExchangeTime().getTime();
                    if ((sevenDays + lastExchangeTime) > now) {
                        writeAbleHandler.modifyActivityHotRanks(new UpdateExpress().increase(ActivityHotRanksField.EXCHANGE_NUM, 1)
                                        .set(ActivityHotRanksField.LAST_EXCHANGE_TIME, new Date())
                                        .set(ActivityHotRanksField.REMOVE_STATUS, ActStatus.UNACT.getCode()),
                                new QueryExpress().add(QueryCriterions.eq(ActivityHotRanksField.GOODS_ID, goodsId))
                                        .add(QueryCriterions.eq(ActivityHotRanksField.ACTIVITY_TYPE, activityType)));
                    } else {
                        writeAbleHandler.modifyActivityHotRanks(new UpdateExpress()
                                        .set(ActivityHotRanksField.EXCHANGE_NUM, 1)
                                        .set(ActivityHotRanksField.LAST_EXCHANGE_TIME, new Date())
                                        .set(ActivityHotRanksField.REMOVE_STATUS, ActStatus.UNACT.getCode()),
                                new QueryExpress().add(QueryCriterions.eq(ActivityHotRanksField.GOODS_ID, goodsId))
                                        .add(QueryCriterions.eq(ActivityHotRanksField.ACTIVITY_TYPE, activityType)));
                    }
                }
            } catch (Exception e) {
                GAlerter.lab("ContentLogic processQueuedEvent HotActivityEvent error.", e);
            }
        } else {
            logger.info(this.getClass().getName() + " discard the unknown event:" + event);
        }

    }

    private void reloadUserRecentLog() {
//        pointCache.removeUserRecentLog();
        List<UserRecentLogEntry> entryList = getUserRecentLogList();
        if (!CollectionUtil.isEmpty(entryList)) {
            pointCache.putUserRecentLog(entryList);
        }
    }

    private List<UserRecentLogEntry> getUserRecentLogList() {
        List<UserRecentLogEntry> entryList = new ArrayList<UserRecentLogEntry>();
        try {
            List<UserConsumeLog> userConsumeLogList = readonlyHandlersPool.getHandler().queryConsumeLogByPage(new QueryExpress().add(QuerySort.add(UserConsumeLogField.CONSUME_DATE, QuerySortOrder.DESC)), new Pagination(USER_RECENT_LOG_PAGE_SIZE, CURRENT_PAGE, USER_RECENT_LOG_PAGE_SIZE));
            for (UserConsumeLog userConsumeLog : userConsumeLogList) {
                Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(userConsumeLog.getProfileId());
                if (profile == null) {
                    continue;
                }
                Goods goods = readonlyHandlersPool.getHandler().getGoodsById(userConsumeLog.getGoodsId());
                if (goods == null) {
                    continue;
                }
                UserRecentLogEntry entry = new UserRecentLogEntry();
                entry.setScreenName(profile.getNick());
                entry.setGoodsName(goods.getGoodsName());
                entry.setType(GOODS);
                entry.setDate(userConsumeLog.getConsumeDate());

                entryList.add(entry);
            }
            List<UserExchangeLog> userExchangeLogList = readonlyHandlersPool.getHandler().queryUserExchangeByPage(new QueryExpress().add(QuerySort.add(UserExchangeLogField.EXCHANGE_TIME, QuerySortOrder.DESC)), new Pagination(USER_RECENT_LOG_PAGE_SIZE, CURRENT_PAGE, USER_RECENT_LOG_PAGE_SIZE));
            for (UserExchangeLog userExchangeLog : userExchangeLogList) {
                Profile profile = UserCenterServiceSngl.get().getProfileByProfileId(userExchangeLog.getProfileId());
                if (profile == null) {
                    continue;
                }
                ActivityGoods activityGoods = readonlyHandlersPool.getHandler().getActivityGoods(new QueryExpress().add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_GOODS_ID, userExchangeLog.getGoodsId())));
                if (activityGoods == null) {
                    continue;
                }
                UserRecentLogEntry entry = new UserRecentLogEntry();
                entry.setScreenName(profile.getNick());
                entry.setGoodsName(activityGoods.getActivitySubject());
                entry.setType(EXCHANGE_GOODS);
                entry.setDate(userExchangeLog.getExhangeTime());

                entryList.add(entry);
            }
            Collections.sort(entryList);
        } catch (Exception e) {
            GAlerter.lab("PointLogic queryUserRecentLog Exception.e:", e);
        }
        return entryList;
    }

    //userPoint method
    @Override
    public UserPoint addUserPoint(UserPoint userPoint) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("addPoint, userPoint is " + userPoint);
        }
        return writeAbleHandler.insertUserPoint(userPoint);
    }

    @Override
    public PageRows<UserPoint> queryUserPointByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryPoint, queryExpress is " + queryExpress + " pagination is " + pagination);
        }
        return readonlyHandlersPool.getHandler().queryUserPoint(queryExpress, pagination);
    }

    @Override
    public UserPoint getUserPoint(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getPoint, queryExpress is " + queryExpress);
        }

        UserPoint userPoint = readonlyHandlersPool.getHandler().getUserPoint(queryExpress);
        if (userPoint != null) {
            int num = pointRedis.getWorshipNum(userPoint.getProfileId());
            userPoint.setWorshipNum(num);
        }
        return userPoint;
    }

    //pointaction method   积分
    @Override
    public boolean increasePointActionHistory(PointActionHistory pointActionHistory, PointKeyType pointKeyType) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("addPointAction, userPoint is " + pointActionHistory);
        }
        if (pointKeyType == null) {
            pointKeyType = PointKeyType.DEFAULT;
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(UserPointField.PROFILEID, pointActionHistory.getProfileId()));
        queryExpress.add(QueryCriterions.eq(UserPointField.POINTKEY, pointKeyType.getValue()));
        boolean returnBoolean = false;
        UserPoint userPoint = readonlyHandlersPool.getHandler().getUserPoint(queryExpress);
        if (userPoint != null) {
            pointCache.putUserPointExistCache(pointActionHistory.getProfileId(), pointActionHistory.getPointkey());
        }
        //判断usepoint是否已经存在
        boolean bool = pointCache.getUserPointExistCache(pointActionHistory.getProfileId(), pointActionHistory.getPointkey());
        if (HistoryActionType.PRESTIGE.equals(pointActionHistory.getHistoryActionType())) {
            if (userPoint == null && !bool) {  //userpoint不存在 同时缓存key=false（key没有） 则进入if
                if (pointActionHistory.getPrestige() < 0) {
                    return returnBoolean;
                }
                UserPoint newPoint = new UserPoint();
                newPoint.setUserNo(pointActionHistory.getUserNo());
                newPoint.setProfileId(pointActionHistory.getProfileId());
                newPoint.setUserPoint(pointActionHistory.getPointValue());
                newPoint.setPrestige(pointActionHistory.getPrestige());
                newPoint.setPointKey(pointKeyType.getValue());
                writeAbleHandler.insertUserPoint(newPoint);

                returnBoolean = newPoint.getUserPointId() > 0;
                if (returnBoolean) {
                    pointCache.putUserPointExistCache(pointActionHistory.getProfileId(), pointActionHistory.getPointkey());
                }
            } else {
                userPoint = readonlyHandlersPool.getHandler().getUserPoint(queryExpress);
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.increase(UserPointField.PRESTIGE, pointActionHistory.getPrestige());

                QueryExpress queryExpressNewForOnly = new QueryExpress();
                queryExpressNewForOnly.add(QueryCriterions.eq(UserPointField.USERPOINTID, userPoint.getUserPointId()));
                if (pointActionHistory.getPrestige() < 0) {
                    queryExpressNewForOnly.add(QueryCriterions.geq(UserPointField.PRESTIGE, Math.abs(pointActionHistory.getPrestige())));
                }
                returnBoolean = writeAbleHandler.updateUserPoint(updateExpress, queryExpressNewForOnly);
            }
            if (returnBoolean) {
                pointActionHistory = writeAbleHandler.insertPointActionHistory(pointActionHistory);
                userPoint = readonlyHandlersPool.getHandler().getUserPoint(queryExpress);
                if (userPoint != null) {
                    //更新声望信息
                    pointRedis.updateUserPrestige(pointActionHistory.getProfileId(), pointActionHistory.getPrestige(), userPoint.getPrestige());
                }
                //增加膜拜人 和 被膜拜次数
                if (pointActionHistory.getActionType().equals(PointActionType.WORSHIP)) {
                    pointRedis.putWorship(pointActionHistory.getProfileId(), pointActionHistory.getDestId(), pointActionHistory.getWorshipNum());
                }
            }
        } else {
            if (userPoint == null && !bool) {
                if (pointActionHistory.getPointValue() < 0) {
                    return returnBoolean;
                }
                userPoint = new UserPoint();
                userPoint.setUserNo(pointActionHistory.getUserNo());
                userPoint.setProfileId(pointActionHistory.getProfileId());
                userPoint.setUserPoint(pointActionHistory.getPointValue());
                userPoint.setPointKey(pointKeyType.getValue());
                writeAbleHandler.insertUserPoint(userPoint);

                returnBoolean = userPoint.getUserPointId() > 0;
                if (returnBoolean) {
                    pointCache.putUserPointExistCache(pointActionHistory.getProfileId(), pointActionHistory.getPointkey());
                }
            } else {
                if (pointActionHistory.getPointValue() < 0 && userPoint.getUserPoint() - Math.abs(pointActionHistory.getPointValue()) < 0) {
                    throw new ServiceException(PointServiceException.USER_POINT_NOT_ENOUGH, PointServiceException.USER_POINT_NOT_ENOUGH.getName());
                }
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.increase(UserPointField.USERPOINT, pointActionHistory.getPointValue());

                QueryExpress queryExpressNewForOnly = new QueryExpress();
                queryExpressNewForOnly.add(QueryCriterions.eq(UserPointField.USERPOINTID, userPoint.getUserPointId()));
                if (pointActionHistory.getPointValue() < 0) {
                    queryExpressNewForOnly.add(QueryCriterions.geq(UserPointField.USERPOINT, Math.abs(pointActionHistory.getPointValue())));
                }
                returnBoolean = writeAbleHandler.updateUserPoint(updateExpress, queryExpressNewForOnly);
            }
            if (returnBoolean) {
                pointActionHistory = writeAbleHandler.insertPointActionHistory(pointActionHistory);
                if (pointActionHistory.getPointValue() > 0) {
                    pointRedis.putUserPointByDay(pointActionHistory.getProfileId(), pointActionHistory.getPointValue());
                    //积分排行榜
                    pointRedis.pushRankList(POINT_RANK_TYPE, pointActionHistory.getProfileId(), pointActionHistory.getPointValue()); //更新积分排行
                } else if (pointActionHistory.getPointValue() < 0) {
                    //消费排行榜
                    int pointValue = Math.abs(pointActionHistory.getPointValue());
                    pointRedis.pushRankList(CONSUMPTION_RANK_TYPE, pointActionHistory.getProfileId(), pointValue); //更新消费排行
                }

                if (pointActionHistory.getActionHistoryId() > 0) {
                    pointRedis.putMyPoint(pointActionHistory);
                }
            }
        }
        return returnBoolean;
    }


    @Override
    public PageRows<PointActionHistory> queryPointActionHistoryByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryPointActionHistoryByPage, queryExpress is " + queryExpress + " pagination is " + pagination);
        }
        return readonlyHandlersPool.getHandler().queryPointActionHistory(queryExpress, pagination);
    }


    @Override
    public List<PointActionHistory> queryPointActionHistory(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryPointActionHistory, queryExpress is " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryPointActionHistory(queryExpress);
    }

    @Override
    public PointActionHistory getPointActionHistory(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getPointActionHistory, queryExpress is " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getPointActionHistory(queryExpress);
    }

    @Override
    public boolean modifyPointActionHistory(long actionHistoryId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("modifyPointActionHistory, actionHistoryId is " + actionHistoryId);
        }
        return writeAbleHandler.updatePointActionHistory(updateExpress, new QueryExpress().add(QueryCriterions.eq(PointActionHistoryField.ACTIONHISTORYID, actionHistoryId)));
    }


    @Override
    public UserDayPoint getUserDayPoint(QueryExpress queryExpress, Date date) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getUserDayPoint, queryExpress is " + queryExpress);
        }

        return writeAbleHandler.getUserDayPoint(queryExpress, date);
    }

    @Override
    public List<UserDayPoint> queryUserDayPoint(QueryExpress queryExpress, Date date) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryUserDayPoint,queryExpress is " + queryExpress);
        }
        return writeAbleHandler.queryUserDayPoint(queryExpress, date);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Goods addGoods(Goods goods) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("addGoods, goods is " + goods);
        }
        return writeAbleHandler.insertGoods(goods);
    }

    @Override
    public PageRows<Goods> queryGoodsByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryGoodsByPage, queryExpress is " + queryExpress + "pagination" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryGoodsByPage(queryExpress, pagination);
    }

    @Override
    public List<Goods> queryGoods(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryGoodsByPage, queryExpress is " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryGoods(queryExpress);
    }

    @Override
    public Goods getGoodsById(long goodsId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getGoodsById, goodsId is " + goodsId);
        }
        return readonlyHandlersPool.getHandler().getGoodsById(goodsId);
    }

    @Override
    public boolean modifyGoodsById(UpdateExpress updateExpress, long goodsId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call modifyGoodsById updateExpress:" + updateExpress + "goodsId:" + goodsId);
        }
        return writeAbleHandler.updateGoodsById(updateExpress, goodsId);
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public int addGoodsItem(List<GoodsItem> goodsItem) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("addGoodsItem, goodsItem is " + goodsItem);
        }
        return writeAbleHandler.insertGoodsItem(goodsItem);
    }

    @Override
    public PageRows<GoodsItem> queryGoodsItemByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryGoodsItemByPage.queryExpress:" + queryExpress + ",pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryGoodsItemByPage(queryExpress, pagination);
    }

    @Override
    public List<GoodsItem> queryGoodsItem(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic queryGoodsItem, queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryGoodsItem(queryExpress);
    }

    @Override
    public GoodsItem getGoodsItemById(long goodsItemId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getGoodsItemById, goodsItemId is " + goodsItemId);
        }
        return readonlyHandlersPool.getHandler().getGoodsItemById(goodsItemId);
    }

    @Override
    public boolean modifyGoodsItemById(UpdateExpress updateExpress, long goodsItemId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call modifyGoodsItemById updateExpress:" + updateExpress + "goodsItemId:" + goodsItemId);
        }

        return writeAbleHandler.updateGoodsItemById(updateExpress, goodsItemId);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public UserConsumeLog consumeGoods(String uno, String profileId, String appKey, long goodsId, Date consumeDate, String consumeIp, GoodsActionType goodsActionType, String address) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call modifyGoodsItemById uno:" + uno + "goodsId:" + goodsId);
        }
        pointCache.addPointConsumeCounter(consumeDate);
        try {
            return writeAbleHandler.consumeGoods(uno, profileId, appKey, goodsId, consumeDate, consumeIp, goodsActionType, address);
        } finally {
            pointCache.deleteActivityGoodsByCache(goodsId);
        }
    }

    @Override
    public PageRows<UserConsumeLog> queryConsumeLog(String profileId, Date startDate, Date endDate, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryUserConsumeLogByPage, userNo is " + profileId + "startDate is" + startDate + "endDate is" + endDate + "pagination is" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryConsumeLog(profileId, startDate, endDate, pagination);
    }

    @Override
    public List<UserConsumeLog> queryConsumeLogList(QueryExpress queryExpress, GoodsActionType type) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryConsumeLogByPage, queryExpress is " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryConsumeLogList(queryExpress, type);
    }

    @Override
    public PageRows<UserConsumeLog> queryConsumeLogByPage(QueryExpress queryExpress, Pagination pagination, GoodsActionType type) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryConsumeLogByPage, queryExpress is " + queryExpress + "pagination is" + pagination);
        }

        return readonlyHandlersPool.getHandler().queryConsumeByPage(queryExpress, pagination, type);

    }

    @Override
    public List<UserConsumeLog> queryConsumeLogByGoodsIdConsumeTime(String profileId, long goodsId, Date consumeTime) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryUserConsumeLogByPage, profileId is " + profileId + "goodsId is" + goodsId + "consumeTime is" + consumeTime);
        }
        return readonlyHandlersPool.getHandler().queryConsumeLogByGoodsIdConsumeTime(profileId, goodsId, consumeTime);
    }

    @Override
    public List<UserConsumeLog> queryConsumeLogByGoodsId(String profileId, long goodsId, String consumeOrder) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryUserConsumeLogByPage, profileId is " + profileId + "goodsId is" + goodsId);
        }
        return readonlyHandlersPool.getHandler().queryConsumeLogByGoodsId(profileId, goodsId, consumeOrder);
    }
    ///////////////////////////////////////////////////////////////////////////////////


    @Override
    public PageRows<ExchangeGoods> queryExchangeGoodsByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryExchangeGoodsByPage, QueryExpress is " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryExchangeGoodsByPage(queryExpress, pagination);
    }

    @Override
    public ExchangeGoods getExchangeGoodS(Long goodsId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getExchangeGoodS, goodsId is " + goodsId);
        }

        return readonlyHandlersPool.getHandler().getExchangeGoodsById(goodsId);
    }

    @Override
    public List<ExchangeGoods> listExchangeGoods(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("listExchangeGoods, QueryExpress is " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryExchangeGoods(queryExpress);
    }

    @Override
    public int addExchangeGoodsItem(List<ExchangeGoodsItem> list) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("addExchangeGoodsItem, List<ExchangeGoodsItem> is " + list);
        }

        return writeAbleHandler.insertExchangeGoodsItem(list);
    }

    @Override
    public boolean modifyExchangeItemGoods(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("modifyExchangeItemGoods, updateExpress is " + updateExpress + " QueryExpress" + queryExpress);
        }
        return writeAbleHandler.updateExchangeGoodsItem(updateExpress, queryExpress);
    }

    @Override
    public ExchangeGoods insertExchangeGoods(ExchangeGoods exchangeGoods) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("insertExchangeGoods, ExchangeGoods is " + exchangeGoods);
        }
        return writeAbleHandler.insertExchangeGoods(exchangeGoods);
    }

    @Override
    public boolean modifyExchangeGoods(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("modifyExchangeGoods, UpdateExpress is " + updateExpress + " QueryExpress=" + queryExpress);
        }
        return writeAbleHandler.updateExchangeGoods(updateExpress, queryExpress);
    }
    /*
    * ExchangeGoodsItem
    * */

    @Override
    public PageRows<ExchangeGoodsItem> queryExchangeGoodsItemByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryExchangeGoodsItemByPage, QueryExpress is " + queryExpress + " Pagination=" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryExchangeGoodsItemByPage(queryExpress, pagination);
    }


    @Override
    public UserExchangeLog exchangeGoodsItem(String uno, String profileId, String appKey, long goodsId, Date exchangeDate, String exchangeIp, String userExchangeDomain, boolean isFree) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler exchangeGoodsItem,uno : " + uno + " goodsId: " + goodsId + " exchangeDate:" + exchangeDate + " exchangeIp: " + exchangeIp);
        }
        ActivityGoods activityGoods = getActivityGoods(goodsId);
        if (!activityGoods.getTimeType().equals(ConsumeTimesType.MANYTIMESADAY)) {
            String userExchangeLogStr = pointCache.getUserExchangeLog(profileId, goodsId);
            if (!StringUtil.isEmpty(userExchangeLogStr)) {
                return null;
            }
            pointCache.putUserExchangeLog(profileId, goodsId);
        }
        UserExchangeLog userExchangeLog = writeAbleHandler.exhangeGoodsItem(uno, profileId, appKey, goodsId, exchangeDate, exchangeIp, userExchangeDomain, isFree);
        if (userExchangeLog != null) {
            pointCache.deleteExchangeGoodsItemList(goodsId);
            pointCache.addGetCodeCounter(new Date());
            pointRedis.putMyGiftCache(profileId, appKey, userExchangeLog);
            pointCache.deleteActivityGoodsByCache(goodsId);
        }

        return userExchangeLog;
    }

    @Override
    public List<ExchangeGoodsItem> taoExchangeGoodsItemByGoodsId(String uno, String profileId, String appKey, long goodsId, Date taoDate, String taoIp) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryUserConsumeLogByPage,goodsId is." + goodsId);
        }

        List<ExchangeGoodsItem> returnList = new ArrayList<ExchangeGoodsItem>();

        ActivityGoods activityGoods = readonlyHandlersPool.getHandler().getActivityGoods(new QueryExpress().add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_GOODS_ID, goodsId)));
        if (activityGoods == null) {
            logger.error("activityGoods is null.goodsId:" + goodsId);
            return returnList;
        }
        List<ExchangeGoodsItem> goodsItemList = pointCache.getExchangeGoodsItemList(goodsId);
        if (CollectionUtil.isEmpty(goodsItemList)) {
            goodsItemList = calTaoCodePool(activityGoods, new Date());

            pointCache.putExchangeGoodsItemList(goodsId, goodsItemList);
        }
        if (CollectionUtil.isEmpty(goodsItemList)) {
            return goodsItemList;
        }
        returnList = RandomUtil.getRandomByList(goodsItemList, TAO_CODE_RESULT_SIZE);

        return returnList;
    }

    @Override
    public PageRows<UserExchangeLog> queryByUser(String profileId, Date startTime, Date endTime, Pagination pagination, String appkey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryUserConsumeLogByPage, profileId is " + profileId + "startDate is" + startTime + "endDate is" + endTime + "pagination is" + pagination);
        }
        PageRows<UserExchangeLog> pageRows = null;
        PageRows<String> logIds = null;
        if (startTime == null || endTime == null) {
            logIds = pointRedis.getMyGiftCache(profileId, appkey, pagination);
        } else {
            logIds = pointRedis.getMyGiftCacheByScore(profileId, appkey, pagination, startTime, endTime);
        }
        if (logIds == null || CollectionUtil.isEmpty(logIds.getRows())) {
            pageRows = readonlyHandlersPool.getHandler().queryUserExchangeLog(profileId, startTime, endTime, pagination, appkey);
            if (pageRows != null && CollectionUtil.isEmpty(pageRows.getRows())) {
                for (UserExchangeLog log : pageRows.getRows()) {
                    pointRedis.putMyGiftCache(profileId, appkey, log);
                }
            }
        } else {
            List<UserExchangeLog> logList = new ArrayList<UserExchangeLog>();
            for (String id : logIds.getRows()) {
                UserExchangeLog log = getUserExchangeLog(Long.valueOf(id));
                if (log != null) {
                    logList.add(log);
                }
            }
            pageRows = new PageRows<UserExchangeLog>();
            pageRows.setPage(logIds.getPage());
            pageRows.setRows(logList);
        }
        return pageRows;
    }

    private UserExchangeLog getUserExchangeLog(Long userExchangeLogId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getUserExchangeLog, userExchangeLogId is " + userExchangeLogId);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(UserExchangeLogField.USER_EXCHANGE_LOG_ID, userExchangeLogId));
        return readonlyHandlersPool.getHandler().getUserExchangeLog(queryExpress);
    }

    @Override
    public PageRows<UserExchangeLog> queryByUserExchangePage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryByUserExchangePage, queryExpress is " + queryExpress + "pagination is" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryUserExchangeLogByPageRows(queryExpress, pagination);
    }

    @Override
    public PageRows<UserExchangeLog> queryUserExchangeByUno(String uno, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryUserExchangeByUno, userNo is " + uno + "pagination is" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryUserExchangeLogByUno(uno, pagination);
    }

    @Override
    public List<UserExchangeLog> queryUserExchangeByQueryExpress(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryUserExchangeByQueryExpress, queryExpress is " + queryExpress);
        }
        return writeAbleHandler.queryUserExchangeByQueryExress(queryExpress);
    }

    @Override
    public int queryUserExchangeByDate(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryUserExchangeByQueryExpress, queryExpress is " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryUserExchangeLogByDate(queryExpress);
    }

    @Override
    public List<UserRecentLogEntry> queryUserRecentLog() throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryProfileGiftLog");
        }
        return pointCache.getUserRecentLog();
    }

    @Override
    public boolean increaseSmsCountExchangLog(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("increaseSmsCountExchangLog");
        }
        return writeAbleHandler.increaseSmsCountExchangLog(updateExpress, queryExpress);
    }

    @Override
    public PageRows<GiftReserve> queryGiftReserveByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryGiftReserveByPage ,QueryExpress " + queryExpress + "pagination is" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryGiftReserveByPaga(queryExpress, pagination);
    }

    @Override
    public List<GiftReserve> queryGiftReserveByList(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryGiftReserveByList ,QueryExpress " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryGiftReserveByList(queryExpress);
    }

    @Override
    public Map<Long, GiftReserve> checkGiftReserve(String porfileId, String appkey, Set<Long> giftReserveIds) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryGiftReserveByList ,porfileId " + porfileId + "appkey," + appkey + " giftReserveIds:" + giftReserveIds);
        }

        //todo addcache
        Map<Long, GiftReserve> map = new HashMap<Long, GiftReserve>();
        if (CollectionUtil.isEmpty(giftReserveIds)) {
            return null;
        }

        List<GiftReserve> giftReserves = readonlyHandlersPool.getHandler().queryGiftReserveByList(new QueryExpress()
                .add(QueryCriterions.eq(GiftReserveField.PROFILEID, porfileId))
                .add(QueryCriterions.eq(GiftReserveField.APPKEY, appkey))
                .add(QueryCriterions.eq(GiftReserveField.VALID_STATUS, ValidStatus.VALID.getCode())).add(QueryCriterions.in(GiftReserveField.AID, giftReserveIds.toArray())));

        for (GiftReserve giftReserve : giftReserves) {
            map.put(giftReserve.getAid(), giftReserve);
        }

        return map;
    }

    @Override
    public GiftReserve getGiftReserve(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getGiftReserve ,QueryExpress " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getGiftReserve(queryExpress);
    }

    @Override
    public GiftReserve createGiftReserve(GiftReserve giftReserve) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getGiftReserve ,GiftReserve " + giftReserve);
        }
        return writeAbleHandler.createGiftReserve(giftReserve);
    }

    @Override
    public boolean modifyGiftReserve(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("modifyGiftReserve ,UpdateExpress :" + updateExpress + " QueryExpress:" + queryExpress);
        }
        return writeAbleHandler.modifyGiftReserve(updateExpress, queryExpress);
    }


    /**
     * 积分墙 积份墙列表
     */
    @Override
    public PageRows<PointwallWall> queryPointwallWallByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryPointwallWallByPage ,QueryExpress " + queryExpress + "pagination is" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryPointwallWallByPage(queryExpress, pagination);
    }

    @Override
    public List<PointwallWall> queryPointwallWall(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryPointwallWall ,QueryExpress " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryPointwallWall(queryExpress);
    }


    @Override
    public int countPointwallWall(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("countPointwallWall,PointwallWall " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().countPointwallWall(queryExpress);
    }


    @Override
    public boolean updatePointwallWall(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("updatePointwallWall ,QueryExpress " + queryExpress + "UpdateExpress  " + updateExpress);
        }
        return writeAbleHandler.updatePointwallWall(updateExpress, queryExpress);
    }

    @Override
    public PointwallWall insertPointwallWall(PointwallWall pointwallWall) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("insertPointwallWall ,PointwallWall " + pointwallWall);
        }
        return writeAbleHandler.insertPointwallWall(pointwallWall);
    }

    @Override
    public int deletePointwallWall(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("deletePointwallWall ,PointwallWall " + queryExpress);
        }
        return writeAbleHandler.deletePointwallWall(queryExpress);
    }

    @Override
    public PointwallWall getPointwallWall(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getPointwallWall ,queryExpress " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getPointwallWall(queryExpress);
    }


    /**
     * 积分墙 app管理
     */
    @Override
    public PageRows<PointwallApp> queryPointwallAppByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryPointwallAppByPage ,QueryExpress " + queryExpress + "pagination is" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryPointwallAppByPage(queryExpress, pagination);
    }

    @Override
    public boolean updatePointwallApp(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("updatePointwallApp ,QueryExpress " + queryExpress + "UpdateExpress  " + updateExpress);
        }
        return writeAbleHandler.updatePointwallApp(updateExpress, queryExpress);
    }

    @Override
    public PointwallApp insertPointwallApp(PointwallApp pointwallApp) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("insertPointwallApp ,PointwallApp " + pointwallApp);
        }
        return writeAbleHandler.insertPointwallApp(pointwallApp);
    }

    @Override
    public int deletePointwallApp(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("deletePointwallApp ,PointwallApp " + queryExpress);
        }
        return writeAbleHandler.deletePointwallApp(queryExpress);
    }

    @Override
    public PointwallApp getPointwallApp(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getPointwallApp ,queryExpress " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getPointwallApp(queryExpress);
    }

    //#############################  积分墙 单积分墙列表管理   start
    @Override
    public PageRows<PointwallWallApp> queryPointwallWallAppByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryPointwallWallAppByPage ,QueryExpress " + queryExpress + "pagination is" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryPointwallWallAppByPage(queryExpress, pagination);
    }

    @Override
    public List<PointwallWallApp> queryPointwallWallAppAll(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryPointwallWallAppAll ,QueryExpress " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryPointwallWallAppAll(queryExpress);
    }


    @Override
    public boolean updatePointwallWallApp(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("updatePointwallWallApp ,QueryExpress " + queryExpress + "UpdateExpress  " + updateExpress);
        }
        return writeAbleHandler.updatePointwallWallApp(updateExpress, queryExpress);
    }

    @Override
    public PointwallWallApp insertPointwallWallApp(PointwallWallApp pointwallWallApp) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("insertPointwallWallApp ,PointwallWallApp " + pointwallWallApp);
        }
        return writeAbleHandler.insertPointwallWallApp(pointwallWallApp);
    }

    @Override
    public int deletePointwallWallApp(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("deletePointwallWallApp,PointwallWallApp " + queryExpress);
        }
        return writeAbleHandler.deletePointwallWallApp(queryExpress);
    }

    @Override
    public PointwallWallApp getPointwallWallApp(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getPointwallWallApp ,queryExpress " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getPointwallWallApp(queryExpress);
    }

    @Override
    public int countPointwallWallAppTotalOfApps(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("countPointwallWallAppTotalOfApps,PointwallWallApp " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().countPointwallWallAppTotalOfApps(queryExpress);
    }

    @Override
    public int countPointwallWallAppOfOneWall(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("countPointwallWallAppOfOneWall,PointwallWallApp " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().countPointwallWallAppOfOneWall(queryExpress);
    }

    //#############################  积分墙 单积分墙列表管理     end


    /*
       point.pw_tasklog管理    start
    */
    @Override
    public PageRows<PointwallTasklog> queryPointwallTasklogByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryPointwallTasklogByPage ,QueryExpress " + queryExpress + "pagination is" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryPointwallTasklogByPage(queryExpress, pagination);
    }

    @Override
    public int countPointwallTasklog(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("countPointwallWallAppTotalOfApps,PointwallWallApp " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().countPointwallTasklog(queryExpress);

    }

    @Override
    public List<PointwallTasklog> queryPointwallTasklogAll(QueryExpress queryExpress, int startIndex, int size) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryPointwallTasklogAll ,QueryExpress " + queryExpress + "startIndex " + startIndex + "size " + size);
        }
        return readonlyHandlersPool.getHandler().queryPointwallTasklogAll(queryExpress, startIndex, size);

    }

    @Override
    public PointwallTasklog insertPointwallTasklog(PointwallTasklog pointwallTasklog) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("insertPointwallTasklog ,PointwallTasklog " + pointwallTasklog);
        }
        return writeAbleHandler.insertPointwallTasklog(pointwallTasklog);
    }

    @Override
    public PointwallTasklog getPointwallTasklog(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getPointwallTasklog    " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getPointwallTasklog(queryExpress);

    }

    //#############################  point.pw_tasklog管理    end


    //礼包中心合并后的表  begin         ###################################################################
    @Override
    public PageRows<ActivityGoods> queryActivityGoodsByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryActivityGoodsByPage ,QueryExpress: " + queryExpress + " Pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryActivityGoodsByPage(queryExpress, pagination);
    }

    @Override
    public ActivityGoods createActivityGoods(ActivityGoods activityGoods) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("createActivityGoods :" + activityGoods);
        }
        activityGoods = writeAbleHandler.createActivity(activityGoods);
        if (activityGoods != null && activityGoods.getActivityGoodsId() > 0) {
            //首字母缓存，只有礼包用到，商品没必要
            if (activityGoods.getActivityType().equals(ActivityType.EXCHANGE_GOODS)) {
                pointRedis.putGiftLetterCache(activityGoods);
            }
        }
        return activityGoods;
    }

    @Override
    public ActivityGoods getActivityGoods(long goodsId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getActivityGoods goodsId:" + goodsId);
        }
        ActivityGoods activityGoods = pointCache.getActivityGoodsByCache(goodsId);
        if (activityGoods == null) {
            activityGoods = readonlyHandlersPool.getHandler().getActivityGoods(new QueryExpress().add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_GOODS_ID, goodsId)));
            if (activityGoods != null) {
                pointCache.putActivityGoodsByCache(goodsId, activityGoods);
            }
        }
        return activityGoods;
    }

    @Override
    public boolean modifyActivityGoods(long activityGoodsId, UpdateExpress updateExpress, Map<String, Object> paramMap) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("modifyActivityGoods activityGoodsId:" + activityGoodsId + " UpdateExpress:" + updateExpress);
        }
        boolean bool = writeAbleHandler.modifyActivityGoods(updateExpress, new QueryExpress().add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_GOODS_ID, activityGoodsId)));
        if (bool) {
            pointCache.deleteActivityGoodsByCache(activityGoodsId);
            ActivityGoods activityGoods = getActivityGoods(activityGoodsId);
            pointCache.deleteActivityGoodsByFristLetter(activityGoods.getFirstLetter());
            pointCache.removeActivityHotRanks();
            pointCache.removeActivityGoodsByGameId(activityGoods.getGameDbId());

            if (paramMap != null) {
                if (paramMap.containsKey("oldLetter")) {
                    pointRedis.removeGiftLetterCache(activityGoods, String.valueOf(paramMap.get("oldLetter")));
                }
                if (paramMap.containsKey("newLetter")) {
                    pointRedis.putGiftLetterCache(activityGoods);
                }
            }
        }
        return bool;

    }

    @Override
    public List<ActivityGoods> listActivityGoods(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("listActivityGoods QueryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryActivityGoods(queryExpress);
    }

    public int queryActivityGoodsCount(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryActivityGoodsCount QueryExpress:" + queryExpress);
        }

        return readonlyHandlersPool.getHandler().countActivitySum(queryExpress);
    }

    @Override
    public Map<String, List<ActivityGoods>> queryGiftmarketIndexList() throws ServiceException {
        Map<String, List<ActivityGoods>> returnMap = new HashMap<String, List<ActivityGoods>>();
        List<ActivityGoods> giftList = pointCache.getActivityGoodsIndex(GIFTMARKET_INDEX_TYPE_GET);
        if (CollectionUtil.isEmpty(giftList)) {
            PageRows<ActivityGoods> activityGoodsPageRows = queryActivityGoodsByPage(new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                    .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC)), new Pagination(GIFTMARKET_INDEX_SIZE, 1, GIFTMARKET_INDEX_SIZE));
            if (activityGoodsPageRows != null && !CollectionUtil.isEmpty(activityGoodsPageRows.getRows())) {
                returnMap.put("indexgiftlist", activityGoodsPageRows.getRows());
                pointCache.putActivityGoodsIndex(activityGoodsPageRows.getRows(), GIFTMARKET_INDEX_TYPE_GET);
            }
        } else {
            returnMap.put("indexgiftlist", giftList);
        }

        List<ActivityGoods> taoList = pointCache.getActivityGoodsIndex(GIFTMARKET_INDEX_TYPE_TAO);
        if (CollectionUtil.isEmpty(taoList)) {
            PageRows<ActivityGoods> taoPageRows = queryActivityGoodsByPage(new QueryExpress()
                            .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                            .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                            .add(QueryCriterions.eq(ActivityGoodsField.GOODS_RESETAMOUNT, 0))
                            .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC))
                    , new Pagination(GIFTMARKET_INDEX_SIZE, 1, GIFTMARKET_INDEX_SIZE));
            if (taoPageRows != null && !CollectionUtil.isEmpty(taoPageRows.getRows())) {
                returnMap.put("taoindexlist", taoPageRows.getRows());
                pointCache.putActivityGoodsIndex(taoPageRows.getRows(), GIFTMARKET_INDEX_TYPE_TAO);
            }
        } else {
            returnMap.put("taoindexlist", taoList);
        }
        return returnMap;
    }

    @Override
    public PageRows<ActivityGoods> queryActivityGoodsByLetter(ActivityType activityType, String letter, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryActivityGoodsByLetter letter:" + letter);
        }
        if (StringUtil.isEmpty(letter)) {
            return null;
        }
        PageRows<ActivityGoods> pageRows = null;
        PageRows<String> ids = pointRedis.getGiftIdsByLetter(activityType, letter, pagination);
        if (ids == null || CollectionUtil.isEmpty(ids.getRows())) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, activityType.getCode()));
            queryExpress.add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()));
            queryExpress.add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
            if (letter.length() == 1) {
                queryExpress.add(QueryCriterions.eq(ActivityGoodsField.FIRST_LETTER, letter));
            } else {
                Set<String> letterSet = new HashSet<String>();
                for (int i = 0; i < letter.length(); i++) {
                    letterSet.add(letter.substring(i, i + 1));
                }
                queryExpress.add(QueryCriterions.in(ActivityGoodsField.FIRST_LETTER, letterSet.toArray()));
            }
            pageRows = queryActivityGoodsByPage(queryExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                for (ActivityGoods gift : pageRows.getRows()) {
                    pointRedis.putGiftLetterCache(gift);
                }
            }
        } else {
            List<ActivityGoods> list = new ArrayList<ActivityGoods>();
            for (String id : ids.getRows()) {
                ActivityGoods gift = getActivityGoods(Long.valueOf(id));
                if (gift != null) {
                    list.add(gift);
                }
            }
            pageRows = new PageRows<ActivityGoods>();
            pageRows.setPage(ids.getPage());
            pageRows.setRows(list);
        }
        return pageRows;
    }

    @Override
    public List<ActivityHotRanks> queryActivityHot(QueryExpress queryExpress) throws ServiceException {
        Pagination pagination = new Pagination(QUERY_HOT_ACTIVITY_PAGE_SIZE_SIX, QUERY_HOT_ACTIVITY_PAGE_NO, QUERY_HOT_ACTIVITY_PAGE_SIZE_SIX);

        List<ActivityHotRanks> list = pointCache.getActivityHotRanksList();
        if (CollectionUtil.isEmpty(list)) {
            PageRows<ActivityHotRanks> pageRows = readonlyHandlersPool.getHandler().activityHotRanksPage(queryExpress, pagination);
            if (CollectionUtil.isEmpty(pageRows.getRows())) {
                return pageRows.getRows();
            }
            pointCache.putActivityHotRanks(pageRows.getRows());
            return pageRows.getRows();
        }
        return list;
    }

    @Override
    public boolean modifyActivityHotRanks(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler getActivityHotRanks :QueryExpress" + queryExpress + " UpdateExpress= " + updateExpress);
        }
        //activityCache.removeActivityHotRanks();
        return writeAbleHandler.modifyActivityHotRanks(updateExpress, queryExpress);
    }

    @Override
    public List<ActivityGoods> queryActivityGoodsByGameId(long gameDbId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryActivityGoodsByGameId .gameDbId:" + gameDbId);
        }
        List<ActivityGoods> list = pointCache.getActivityGoodsByGameId(gameDbId);
        if (CollectionUtil.isEmpty(list)) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ActivityGoodsField.GAME_DB_ID, gameDbId));
            queryExpress.add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()));
            queryExpress.add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()));
            queryExpress.add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
            list = readonlyHandlersPool.getHandler().queryActivityGoods(queryExpress);
            if (!CollectionUtil.isEmpty(list)) {
                pointCache.putActivityGoodsByGameId(gameDbId, list);
            }
        }
        return list;
    }


//    @Override
//    public ActivityGoods getActivityGoodsByCache(long goodsId) throws ServiceException {
//        ActivityGoods activityGoods = pointCache.getActivityGoodsByCache(goodsId);
//        if (activityGoods == null) {
//            activityGoods = writeAbleHandler.getActivityGoods(new QueryExpress().add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_GOODS_ID, goodsId)));
//            if (activityGoods != null) {
//                pointCache.putActivityGoodsByCache(goodsId, activityGoods);
//            }
//        }
//        return activityGoods;  //To change body of implemented methods use File | Settings | File Templates.
//    }

    //礼包中心合并后的表  end         ###################################################################

    private <T> List<T> getRandomByList(List<T> list, int resultSize) {
        List<T> returnList = new ArrayList<T>();

        int taoIdx = 0;
        Map<Integer, T> map = new LinkedHashMap<Integer, T>();
        int i = RandomUtil.getRandomInt(list.size());
        int end = i + resultSize;
        for (; i < end; i++) {
            if (i <= list.size() - 1) {
                map.put(i, list.get(i));
            } else {
                if (map.containsKey(taoIdx)) {
                    continue;
                }
                map.put(taoIdx, list.get(taoIdx));
                taoIdx++;
            }
        }

        returnList.addAll(map.values());
        return returnList;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void reloadTaoCode() {
        logger.info(" reloadTaoCode start.....");
        try {
            //有效状态
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()));
            queryExpress.add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()));
            List<ActivityGoods> exchangeGoodsList = readonlyHandlersPool.getHandler().queryActivityGoods(queryExpress);

            for (ActivityGoods activityGoods : exchangeGoodsList) {
                List<ExchangeGoodsItem> itemList = calTaoCodePool(activityGoods, new Date());

                //更新缓存,查找72小时之前
                pointCache.putExchangeGoodsItemList(activityGoods.getActivityGoodsId(), itemList);
            }

        } catch (Exception e) {
            GAlerter.latd(this.getClass().getName() + " reload exchange goods occured Exception.e:", e);
        }
    }

    private List<ExchangeGoodsItem> calTaoCodePool(ActivityGoods activityGoods, Date now) throws DbException {
        List<ExchangeGoodsItem> itemList = null;
        if (activityGoods.getGoodsResetAmount() > 0) {
            QueryExpress queryItemExpress = new QueryExpress();
            queryItemExpress.add(QueryCriterions.eq(ExchangeGoodsItemField.EXCHANGE_GOODS_ID, activityGoods.getActivityGoodsId()));
            queryItemExpress.add(QueryCriterions.lt(ExchangeGoodsItemField.EXCHANGE_TIME, new Date(now.getTime() - TAO_CODE_INTRAVL_MILLIS)));
            queryItemExpress.add(QueryCriterions.eq(ExchangeGoodsItemField.EXCHANGE_STATUS, ActStatus.ACTED.getCode()));
            itemList = readonlyHandlersPool.getHandler().queryExchangeGoodsItem(queryItemExpress);
        } else {
            QueryExpress queryItemExpress = new QueryExpress();
            queryItemExpress.add(QueryCriterions.eq(ExchangeGoodsItemField.EXCHANGE_GOODS_ID, activityGoods.getActivityGoodsId()));
            queryItemExpress.add(QueryCriterions.eq(ExchangeGoodsItemField.EXCHANGE_STATUS, ActStatus.ACTED.getCode()));
            itemList = readonlyHandlersPool.getHandler().queryExchangeGoodsItem(queryItemExpress);
        }

        return itemList;
    }

    public PointKeyType getPointKeyType(String appkey) throws ServiceException {
        appkey = AppUtil.getAppKey(appkey);
        PointKeyType pointKeyType = null;
        try {
            if (StringUtil.isEmpty(appkey)) {
                pointKeyType = PointKeyType.WWW;
                return pointKeyType;
            }
            PointwallWall wall = readonlyHandlersPool.getHandler().getPointwallWall(new QueryExpress().add(QueryCriterions.eq(PointwallWallAppField.APPKEY, appkey)));

            if (wall != null && !StringUtil.isEmpty(wall.getPointKey())) {
                pointKeyType = PointKeyType.getByCode(wall.getPointKey());
            }

        } catch (Exception e) {
            GAlerter.latd(this.getClass().getName() + "getPointKeyType Exception.e:", e);
        }
        return pointKeyType;
    }

    @Override
    public GoodsSeckill createGoodsSeckill(GoodsSeckill goodsSeckill) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic createGoodsSeckill.goodsSeckill:" + goodsSeckill.toString());
        }
        goodsSeckill = writeAbleHandler.createGoodsSeckill(goodsSeckill);
        if (goodsSeckill != null && goodsSeckill.getSeckillId() > 0l) {
            pointRedis.putGoodsSeckillByEndTime(goodsSeckill.getGoodsActionType(), Long.valueOf(goodsSeckill.getGoodsId()), goodsSeckill);
        }
        return goodsSeckill;
    }

    @Override
    public GoodsSeckill getGoodsSeckill(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic getGoodsSeckill.queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getGoodsSeckill(queryExpress);
    }

    @Override
    public GoodsSeckill getGoodsSeckillById(long seckillId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic getGoodsSeckillById.seckillId:" + seckillId);
        }
        GoodsSeckill goodsSeckill = pointCache.getGoodsSeckill(seckillId);
        if (goodsSeckill == null) {
            goodsSeckill = readonlyHandlersPool.getHandler().getGoodsSeckill(new QueryExpress().add(QueryCriterions.eq(GoodsSeckillField.SECKILL_ID, seckillId)));
            if (goodsSeckill != null) {
                pointCache.putGoodsSeckill(seckillId, goodsSeckill);
            }
        }
        return goodsSeckill;
    }

    @Override
    public List<GoodsSeckill> queryGoodsSeckill(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic queryGoodsSeckill.queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryGoodsSeckill(queryExpress);
    }

    @Override
    public PageRows<GoodsSeckill> queryGoodsSeckillByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic queryGoodsSeckillByPage.queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryGoodsSeckillByPage(queryExpress, pagination);
    }

    //todo 调用前认真看代码
    @Override
    public List<GoodsSeckill> queryGoodsSeckillByCache(GoodsActionType goodsActionType, long goodsId, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic queryGoodsSeckillByCache.goodsActionType:" + goodsActionType.getCode() + ",goodsId:" + goodsId);
        }
        //取所有没结束的秒杀
        Set<String> seckillIdSet = pointRedis.getGoodsSeckillByEndTime(goodsActionType, goodsId, pagination);
        if (CollectionUtil.isEmpty(seckillIdSet)) {
            QueryExpress seckillExpress = new QueryExpress();
            if (goodsId > 0l) {
                seckillExpress.add(QueryCriterions.eq(GoodsSeckillField.GOODS_ID, String.valueOf(goodsId)));
            }
            seckillExpress.add(QueryCriterions.eq(GoodsSeckillField.GOODS_ACTION_TYPE, goodsActionType.getCode()));
            seckillExpress.add(QueryCriterions.gt(GoodsSeckillField.END_TIME, new Date()));
            seckillExpress.add(QueryCriterions.eq(GoodsSeckillField.REMOVE_STATUS, IntRemoveStatus.USED.getCode()));
            seckillExpress.add(QuerySort.add(GoodsSeckillField.END_TIME, QuerySortOrder.ASC));
            PageRows<GoodsSeckill> pageRows = readonlyHandlersPool.getHandler().queryGoodsSeckillByPage(seckillExpress, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                for (GoodsSeckill goodsSeckill : pageRows.getRows()) {
                    pointRedis.putGoodsSeckillByEndTime(goodsActionType, goodsId, goodsSeckill);
                }
                return pageRows.getRows();
            } else {
                return null;
            }
        }
        List<GoodsSeckill> list = new ArrayList<GoodsSeckill>();
        for (String seckillId : seckillIdSet) {
            GoodsSeckill goodsSeckill = getGoodsSeckillById(Long.valueOf(seckillId));
            if (goodsSeckill != null) {
                list.add(goodsSeckill);
            }
        }
        return list;
    }

    @Override
    public boolean modifyGoodsSeckill(long seckillId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic modifyGoodsSeckill.seckillId:" + seckillId);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(GoodsSeckillField.SECKILL_ID, seckillId));
        GoodsSeckill goodsSeckill = getGoodsSeckillById(seckillId);
        boolean bool = writeAbleHandler.modifyGoodsSeckill(queryExpress, updateExpress);
        if (bool) {
            pointCache.removeGoodsSeckill(seckillId);
            if (updateExpress.getUpdateValueByField(GoodsSeckillField.REMOVE_STATUS) != null && IntRemoveStatus.REMOVE.getCode() == (Integer) updateExpress.getUpdateValueByField(GoodsSeckillField.REMOVE_STATUS)) {
                pointRedis.removeGoodsSeckillByEndTime(goodsSeckill.getGoodsActionType(), Long.valueOf(goodsSeckill.getGoodsId()), goodsSeckill);
            } else if (updateExpress.getUpdateValueByField(GoodsSeckillField.REMOVE_STATUS) != null && IntRemoveStatus.USED.getCode() == (Integer) updateExpress.getUpdateValueByField(GoodsSeckillField.REMOVE_STATUS)) {
                pointRedis.putGoodsSeckillByEndTime(goodsSeckill.getGoodsActionType(), Long.valueOf(goodsSeckill.getGoodsId()), goodsSeckill);
            } else {
                //可用状态下的修改操作
                if (goodsSeckill.getRemoveStatus().equals(IntRemoveStatus.USED)) {
                    GoodsActionType goodsActionType = GoodsActionType.getByCode((Integer) updateExpress.getUpdateValueByField(GoodsSeckillField.GOODS_ACTION_TYPE));
                    Long goodsId = Long.valueOf((String) updateExpress.getUpdateValueByField(GoodsSeckillField.GOODS_ID));
                    goodsSeckill.setEndTime((Date) updateExpress.getUpdateValueByField(GoodsSeckillField.END_TIME));
                    pointRedis.removeGoodsSeckillByEndTime(goodsSeckill.getGoodsActionType(), Long.valueOf(goodsSeckill.getGoodsId()), goodsSeckill);
                    pointRedis.putGoodsSeckillByEndTime(goodsActionType, goodsId, goodsSeckill);
                }
            }
        }
        return bool;
    }

    @Override
    public ActivityGoodsDTO seckillGoods(long seckillId, long goodsId, String profileId, String appKey, String uno, Date consumeDate, String consumeIp, GoodsActionType goodsActionType, String address) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic seckillGoods.seckillId:" + seckillId + ",goodsId:" + goodsId + ",profileId:" + profileId + ",appKey:" + appKey);
        }
        long times = pointRedis.incrSeckillGoodsTimes(seckillId, 1l);
        GoodsSeckill goodsSeckill = getGoodsSeckillById(seckillId);
        if (goodsSeckill == null) {
            pointRedis.incrSeckillGoodsTimes(seckillId, -1l);
            throw new ServiceException(PointServiceException.GOODS_SECKILL_IS_NULL, "goods seckill is null.seckillId:" + seckillId);
        }
        if (times > goodsSeckill.getSeckillTotal()) {
            pointRedis.incrSeckillGoodsTimes(seckillId, -1l);
            throw new ServiceException(PointServiceException.GOODS_SECKILL_TOTAL_BEYOND, "goods seckill total beyond.seckillId:" + seckillId);
        }
        if (consumeDate.getTime() > goodsSeckill.getEndTime().getTime()) {
            pointRedis.incrSeckillGoodsTimes(seckillId, -1l);
            throw new ServiceException(PointServiceException.GOODS_SECKILL_END, "goods seckill end.seckillId:" + seckillId);
        }
        if (consumeDate.getTime() < goodsSeckill.getStartTime().getTime()) {
            pointRedis.incrSeckillGoodsTimes(seckillId, -1l);
            throw new ServiceException(PointServiceException.GOODS_SECKILL_NO_START, "goods seckill no start.seckillId:" + seckillId);
        }
        writeAbleHandler.modifyGoodsSeckill(new QueryExpress().add(QueryCriterions.eq(GoodsSeckillField.SECKILL_ID, seckillId)), new UpdateExpress().increase(GoodsSeckillField.SECKILL_SUM, -1));
        pointCache.removeGoodsSeckill(seckillId);
        UserConsumeLog consumeLog = null;
        try {
            consumeLog = consumeGoods(uno, profileId, appKey, goodsId, consumeDate, consumeIp, goodsActionType, address);
        } catch (ServiceException e) {
            pointRedis.incrSeckillGoodsTimes(seckillId, -1l);
            writeAbleHandler.modifyGoodsSeckill(new QueryExpress().add(QueryCriterions.eq(GoodsSeckillField.SECKILL_ID, seckillId)), new UpdateExpress().increase(GoodsSeckillField.SECKILL_SUM, 1));
            pointCache.removeGoodsSeckill(seckillId);
            throw e;
        }
        ActivityGoodsDTO activityGoodsDTO = buildActivityGoodsDTO(goodsId, consumeLog, goodsSeckill);
        return activityGoodsDTO;
    }

    @Override
    public Map<Long, ActivityGoods> queryActivityGoodsIdSet(Set<Long> goodsIdSet) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic queryActivityGoodsIdSet.goodsIdSet:" + goodsIdSet);
        }
        Map<Long, ActivityGoods> map = new HashMap<Long, ActivityGoods>();
        Set<Long> dbSet = new HashSet<Long>();
        for (Long goodsId : goodsIdSet) {
            ActivityGoods activityGoods = pointCache.getActivityGoodsByCache(goodsId);
            if (activityGoods != null) {
                map.put(goodsId, activityGoods);
            } else {
                dbSet.add(goodsId);
            }
        }
        if (!CollectionUtil.isEmpty(dbSet)) {
            List<ActivityGoods> list = listActivityGoods(new QueryExpress().add(QueryCriterions.in(ActivityGoodsField.ACTIVITY_GOODS_ID, dbSet.toArray())));
            if (!CollectionUtil.isEmpty(list)) {
                for (ActivityGoods goods : list) {
                    if (goods != null) {
                        map.put(goods.getActivityGoodsId(), goods);
                        pointCache.putActivityGoodsByCache(goods.getActivityGoodsId(), goods);
                    }
                }
            }
        }
        return map;
    }

    @Override
    public PageRows<ActivityGoods> queryActivityGoodsAllListByCache(ActivityType activityType, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic queryActivityGoodsAllListByCache.page:" + page.getStartRowIdx() + "," + page.getEndRowIdx());
        }
        PageRows<ActivityGoods> pageRows = null;
        PageRows<String> allIds = pointRedis.getActivityGoodsAllListIds(activityType, page);
        if (allIds == null || CollectionUtil.isEmpty(allIds.getRows())) {
            QueryExpress queryExpress = new QueryExpress()
                    .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, activityType.getCode()))
                    .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                    .add(QuerySort.add(ActivityGoodsField.DISPLAY_ORDER, QuerySortOrder.ASC));
            pageRows = queryActivityGoodsByPage(queryExpress, page);
            if (pageRows != null && CollectionUtil.isEmpty(pageRows.getRows())) {
                for (ActivityGoods goods : pageRows.getRows()) {
                    pointRedis.putGiftLetterCache(goods);
                }
            }
        } else {
            List<ActivityGoods> goodsList = new ArrayList<ActivityGoods>();
            for (String id : allIds.getRows()) {
                ActivityGoods goods = getActivityGoods(Long.valueOf(id));
                if (goods != null) {
                    goodsList.add(goods);
                }
            }
            pageRows = new PageRows<ActivityGoods>();
            pageRows.setPage(allIds.getPage());
            pageRows.setRows(goodsList);
        }
        return pageRows;
    }

    @Override
    public UserExchangeLog getUserExchangeLog(String profileId, long aid, long logId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic getUserExchangeLog.profileId:" + profileId + ",aid:" + aid + ",logid:" + logId);
        }
        UserExchangeLog exchangeLog = pointCache.getUserExchangeLogById(profileId, aid, logId);
        if (exchangeLog == null) {
            exchangeLog = readonlyHandlersPool.getHandler().getUserExchangeLog(new QueryExpress()
                    .add(QueryCriterions.eq(UserExchangeLogField.USER_EXCHANGE_LOG_ID, logId))
                    .add(QueryCriterions.eq(UserExchangeLogField.EXCHANGE_GOODS_ID, aid))
                    .add(QueryCriterions.eq(UserExchangeLogField.PROFILEID, profileId)));
            if (exchangeLog != null) {
                pointCache.putUserExchangeLogById(profileId, aid, logId, exchangeLog);
            }
        }
        return exchangeLog;
    }

    @Override
    public long incrPointRule(String key, Long value) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic incrPointRule.key:" + key);
        }

        return pointRedis.pointRuleIncr(key, value);
    }

    @Override
    public long getWanbaQuestionPoint(String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic getWanbaQuestionPoint.profileId:" + profileId);
        }

        return pointRedis.getWanbaQuestionPoint(profileId);
    }


    @Override
    public Map<String, Integer> queryRankListByType(String key, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic queryRankListByType.type:" + key + "  pagination=" + pagination.toString());
        }
        return pointRedis.queryRankList(key, pagination);
    }

    @Override
    public int getGiftBoxNum(String profileId) throws ServiceException {
        return pointRedis.getLotteryNum(profileId);
    }

    private static final int DEFAULT_BOX_POINT = 500;

    @Override
    public int exchangeGiftBox(int num, String profileId, String appkey) throws ServiceException {
        if (num < 1) {
            return -1;
        }
        PointKeyType pointKeyType = getPointKeyType(appkey);
        if (pointKeyType == null) {
            pointKeyType = PointKeyType.DEFAULT;
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(UserPointField.PROFILEID, profileId));
        queryExpress.add(QueryCriterions.eq(UserPointField.POINTKEY, pointKeyType.getValue()));
        UserPoint userPoint = writeAbleHandler.getUserPoint(queryExpress);
        if (userPoint == null) {
            return -1;
        }

        int pointValue = num * DEFAULT_BOX_POINT;
        if (userPoint.getUserPoint() < pointValue) {
            return -1;
        }

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.increase(UserPointField.USERPOINT, -pointValue);

        QueryExpress queryExpressNewForOnly = new QueryExpress();
        queryExpressNewForOnly.add(QueryCriterions.eq(UserPointField.USERPOINTID, userPoint.getUserPointId()));
        queryExpressNewForOnly.add(QueryCriterions.gt(UserPointField.USERPOINT, DEFAULT_BOX_POINT));
        boolean returnBoolean = writeAbleHandler.updateUserPoint(updateExpress, queryExpressNewForOnly);
        if (!returnBoolean) {
            return -1;
        }
        String key = PointActionType.GIFT_LOTTERY.getCode() + "_" + profileId + "_" + DateUtil.formatDateToString(new Date(), "yyyyMMdd");

        PointActionHistory pointActionHistory = new PointActionHistory();
        pointActionHistory.setProfileId(profileId);
        pointActionHistory.setActionDate(new Date());
        pointActionHistory.setActionDescription("兑换" + num + "个宝箱");
        pointActionHistory.setCreateDate(new Date());
        pointActionHistory.setDestId(key);
        pointActionHistory.setPointValue(-pointValue);
        pointActionHistory.setActionType(PointActionType.GIFT_LOTTERY);
        pointActionHistory.setPrestige(0);
        pointActionHistory.setHistoryActionType(HistoryActionType.POINT);
        pointActionHistory.setAppkey(appkey);
        pointActionHistory.setPointkey(pointKeyType.getValue());

        pointActionHistory = writeAbleHandler.insertPointActionHistory(pointActionHistory);
        pointRedis.pushRankList(CONSUMPTION_RANK_TYPE, pointActionHistory.getProfileId(), pointValue); //更新消费排行
        if (pointActionHistory.getActionHistoryId() > 0) {
            pointRedis.putMyPoint(pointActionHistory);
        }
        pointRedis.incrLotteryNum(profileId, num);

        return num;
    }

    @Override
    public UserLotteryLog openGiftLottery(String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic openGiftLottery.profileId:" + profileId);
        }
        //小于0则表示没有抽奖次数了
        int lotteryNum = pointRedis.decrLotteryNum(profileId);
        if (lotteryNum < 0) {
            throw new ServiceException(PointServiceException.GIFT_LOTTERY_NOT_ENOUGH, PointServiceException.GIFT_LOTTERY_NOT_ENOUGH.getName());
        }

        List<GiftLottery> giftLotteries = queryGiftLotteryByCache();
        if (CollectionUtil.isEmpty(giftLotteries)) {
            //没有奖品 返还用户抽奖次数
            pointRedis.incrLotteryNum(profileId, 1);
            throw new ServiceException(PointServiceException.GIFT_LOTTERY_NOT_EXISTS, PointServiceException.GIFT_LOTTERY_NOT_ENOUGH.getName());
        }

        //抽奖逻辑
        GiftLottery giftLottery = LotteryUtil.lottery(giftLotteries);

        String userLotteryLogId = MD5Util.Md5(profileId + giftLottery.getGiftLotteryId());
        UserLotteryLog userLotteryLog = getUserLotteryLog(userLotteryLogId);
        //判断用户是否获得 如果已经获得该礼物 则返还对应积分
        if (userLotteryLog == null) {
            userLotteryLog = new UserLotteryLog();
            userLotteryLog.setGiftLotteryId(giftLottery.getGiftLotteryId());
            userLotteryLog.setGiftLotteryName(giftLottery.getGiftLotteryName());
            userLotteryLog.setProfileId(profileId);
            userLotteryLog.setLotteryDate(new Date());
            userLotteryLog.setUserLotteryLogId(userLotteryLogId);
            try {
                writeAbleHandler.insertUserLotteryLog(userLotteryLog);
                pointCache.removeGiftLotteryLogByCache(profileId);
                return userLotteryLog;
            } catch (DbException e) {
                if ("23000".equals(e.getSQLException().getSQLState())) {
                    //防止并发，主键冲突表示用户已经抽中过 返还积分
                    returnPoint(profileId, giftLottery);
                    GAlerter.lan(this.getClass().getName() + " DbException.MySQLIntegrityConstraintViolationException  returnPoint userLotteryLog.name=" + giftLottery.getGiftLotteryName() + " pid=" + profileId + " lid=" + giftLottery.getGiftLotteryId());
                    throw new ServiceException(PointServiceException.GIFT_LOTTERY_EXIST, giftLottery.toJson());
                } else {
                    //插入失败 返还抽奖次数
                    pointRedis.incrLotteryNum(profileId, 1);
                    throw new ServiceException(PointServiceException.USER_LOTTERY_LOG_INSERT_FAILED, PointServiceException.USER_LOTTERY_LOG_INSERT_FAILED.getName());
                }
            }
        } else {
            //抽中已经有的  返还积分
            returnPoint(profileId, giftLottery);
            throw new ServiceException(PointServiceException.GIFT_LOTTERY_EXIST, giftLottery.toJson());
        }
    }

    public static void main(String agres[]) {
        FiveProps fiveProps = Props.instance().getServProps();
        MemCachedConfig memCachedConfig = new MemCachedConfig(fiveProps);
        PointCache pointCache = new PointCache(memCachedConfig);
//            PointConfig cfg = new PointConfig(fiveProps);
//            PointHandler writeAbleHandler = new PointHandler(cfg.getWriteableDataSourceName(), cfg.getProps());
//            String userLotteryLogId = MD5Util.Md5("bd160712c0660292effb7a00accb299a28");
//
//            UserLotteryLog userLotteryLog = new UserLotteryLog();
//            userLotteryLog.setGiftLotteryId(28);
//            userLotteryLog.setGiftLotteryName("评论紫1");
//            userLotteryLog.setProfileId("bd160712c0660292effb7a00accb299a");
//            userLotteryLog.setLotteryDate(new Date());
//            userLotteryLog.setUserLotteryLogId(userLotteryLogId);
//            writeAbleHandler.insertUserLotteryLog(userLotteryLog);
        List<UserLotteryLog> userLotteryLogs = pointCache.queryGiftLotteryLogByCache("bd160712c0660292effb7a00accb299a");
        System.out.println(userLotteryLogs);

        pointCache.removeGiftLotteryLogByCache("bd160712c0660292effb7a00accb299a");


    }

    /**
     * 抽中已经有的  返还积分
     *
     * @param profileId
     * @param giftLottery
     * @return
     */
    private boolean returnPoint(String profileId, GiftLottery giftLottery) throws ServiceException {
        String key = PointActionType.GIFT_LOTTERY.getCode() + "_" + profileId + "_" + DateUtil.formatDateToString(new Date(), "yyyyMMdd");
        UserPointEvent userPointEvent = new UserPointEvent();
        userPointEvent.setHistoryActionType(HistoryActionType.POINT);
        userPointEvent.setPointActionType(PointActionType.GIFT_LOTTERY);
        userPointEvent.setProfileId(profileId);
        userPointEvent.setPrestige(0);
        userPointEvent.setActionDate(new Date());
        userPointEvent.setPoint(giftLottery.getReturnPoint());
        userPointEvent.setObjectId(key);
        userPointEvent.setDescription("抽中" + giftLottery.getGiftLotteryName() + "返还" + giftLottery.getReturnPoint() + "分");
        boolean bool = EventDispatchServiceSngl.get().dispatch(userPointEvent);
        return bool;
    }


    private UserLotteryLog getUserLotteryLog(String userLotteryLogId) {
        UserLotteryLog userLotteryLog = null;
        try {
            userLotteryLog = pointCache.getUserLotteryLog(userLotteryLogId);
            if (userLotteryLog == null) {
                userLotteryLog = readonlyHandlersPool.getHandler().getUserLotteryLog(new QueryExpress()
                        .add(QueryCriterions.eq(UserLotteryLogField.USERLOTTERYLOGID, userLotteryLogId)));
                if (userLotteryLog != null) {
                    pointCache.putUserLotteryLog(userLotteryLog);
                }
            }
        } catch (DbException e) {
            GAlerter.lab(this.getClass().getName() + " getUserLotteryLog error", e);
        }
        return userLotteryLog;
    }

    @Override
    public List<GiftLottery> queryGiftLotteryByCache() {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic queryGiftLotteryByCache.:");
        }
        List<GiftLottery> giftLotteries = null;
        try {
            giftLotteries = pointCache.queryGiftLotteryList();
            if (CollectionUtil.isEmpty(giftLotteries)) {
                giftLotteries = readonlyHandlersPool.getHandler().queryGiftLottery(new QueryExpress().add(QuerySort.add(GiftLotteryField.PROBABILITY, QuerySortOrder.ASC)));
                if (giftLotteries != null) {
                    pointCache.putGiftLotteryList(giftLotteries);
                }
            }
        } catch (DbException e) {
            GAlerter.lab(this.getClass().getName() + " queryGiftLotteryList error", e);
        }
        return giftLotteries;
    }

    public Map<String, Integer> queryPretigeByMonth(String key, Pagination pagination) {
        return pointRedis.queryPretigeByMonth(key, pagination);
    }

    @Override
    public Map<Long, UserLotteryLog> queryUserLotteryLogByCache(String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic queryUserLotteryLogByCache. profileId:" + profileId);
        }
        List<UserLotteryLog> userLotteryLogs = pointCache.queryGiftLotteryLogByCache(profileId);
        if (CollectionUtil.isEmpty(userLotteryLogs)) {
            userLotteryLogs = readonlyHandlersPool.getHandler().queryUserLotteryLog(new QueryExpress().add(QueryCriterions.eq(UserLotteryLogField.PROFILEID, profileId)));
            if (!CollectionUtil.isEmpty(userLotteryLogs)) {
                pointCache.putGiftLogByCache(profileId, userLotteryLogs);
            }
        }
        Map<Long, UserLotteryLog> returnMap = new HashMap<Long, UserLotteryLog>();
        if (!CollectionUtil.isEmpty(userLotteryLogs)) {
            for (UserLotteryLog userLotteryLog : userLotteryLogs) {
                returnMap.put(userLotteryLog.getGiftLotteryId(), userLotteryLog);
            }
        }
        return returnMap;
    }

    @Override
    public boolean chooseLottery(String profileId, long lotteryId, boolean bool) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic chooseLottery. profileId:" + profileId + " lotteryId=" + lotteryId);
        }
        Map<Long, UserLotteryLog> userLotteryLogMap = queryUserLotteryLogByCache(profileId);
        if (userLotteryLogMap != null && userLotteryLogMap.get(lotteryId) != null) {
            GiftLottery giftLottery = readonlyHandlersPool.getHandler().getGiftLottery(new QueryExpress().add(QueryCriterions.eq(GiftLotteryField.GIFTLOTTERYID, lotteryId)));
            if (bool && giftLottery != null) {
                pointRedis.putUserChooseGift(profileId, giftLottery.getLotteryType(), giftLottery.getPicKey());
                return true;
            } else {
                pointRedis.putUserChooseGift(profileId, giftLottery.getLotteryType(), "");
            }
        }
        return false;
    }

    @Override
    public Map<String, String> getChooseLottery(String profileId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic getUserChooseLottery. profileId:" + profileId);
        }

        return pointRedis.getUserChooseGift(profileId);
    }


    @Override
    public Map<String, Map<String, String>> queryChooseLottery(Set<String> profileIds) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic queryUserChooseLottery. profileId:" + profileIds.toString());
        }
        if (CollectionUtil.isEmpty(profileIds)) {
            return null;
        }
        Map<String, Map<String, String>> returnMap = new HashMap<String, Map<String, String>>();
        for (String profileId : profileIds) {
            Map<String, String> chooseMap = getChooseLottery(profileId);
            if (chooseMap != null && !chooseMap.isEmpty()) {
                returnMap.put(profileId, chooseMap);
            }
        }
        return returnMap;
    }

    @Override
    public int getUserPointByDay(String profileId, String flag) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic getUserPointByDay. profileId:" + profileId);
        }
        if (StringUtil.isEmpty(flag)) {
            return pointRedis.getUserPointByDay(profileId);
        } else {
            return pointRedis.getPointRule(flag);
        }
    }

    @Override
    public PageRows<PointActionHistory> queryMyPointByCache(String profileId, String type, String pointkey, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic queryMyPointByCache. profileId:" + profileId + " type=" + type);
        }

        PageRows<String> myPointIdPage = pointRedis.queryMyPoint(profileId, type, pointkey, pagination);
        PageRows<PointActionHistory> returnMap = new PageRows<PointActionHistory>();
        ;
        if (myPointIdPage != null && !CollectionUtil.isEmpty(myPointIdPage.getRows())) {
            List<PointActionHistory> pointActionHistories = new ArrayList<PointActionHistory>();
            for (String hid : myPointIdPage.getRows()) {
                PointActionHistory pointActionHistory = getPointActionHistoryByCache(Long.parseLong(hid));
                if (pointActionHistory != null) {
                    pointActionHistories.add(pointActionHistory);
                }
            }
            returnMap.setPage(myPointIdPage.getPage());
            returnMap.setRows(pointActionHistories);
        }
        return returnMap;
    }

    public PointActionHistory getPointActionHistoryByCache(Long hisotryId) throws ServiceException {
        PointActionHistory pointActionHistory = pointCache.getMyPointHisotryByCache(hisotryId);
        if (pointActionHistory == null) {
            pointActionHistory = PointServiceSngl.get().getPointActionHistory(new QueryExpress().add(QueryCriterions.eq(PointActionHistoryField.ACTIONHISTORYID, hisotryId)));
            if (pointActionHistory != null) {
                pointCache.putPointActionHistory(pointActionHistory.getActionHistoryId(), pointActionHistory);
            }
        }
        return pointActionHistory;
    }

    @Override
    public PageRows<String> queryWorship(String profileId, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("PointLogic queryWorship. profileId:" + profileId);
        }

        return pointRedis.queryWorship(profileId, pagination);
    }

    /**
     * 获得某月里有多少数据 传空则查询所有的总数
     *
     * @param key
     * @return
     */
    public int queryPrestigeSize(String key) {
        return pointRedis.queryPrestigeSize(key);
    }

    /**
     * 查询用户的声望排行
     *
     * @param pids
     * @return map<ProfileID,排名>
     */
    public Map<String, Integer> queryUserPrestigeRanks(Set<String> pids) {
        return pointRedis.queryUserPrestigeRanks(pids);
    }

    /**
     * 查询用户声望值
     *
     * @param pids
     * @return <pid,声望值>
     */
    public Map<String, Integer> queryUserPresige(Set<String> pids) {
        return pointRedis.queryUserPresige(pids);
    }

    private ActivityGoodsDTO buildActivityGoodsDTO(long goodsId, UserConsumeLog consumeLog, GoodsSeckill goodsSeckill) throws ServiceException {
        if (goodsId <= 0l || consumeLog == null) {
            return null;
        }
        ActivityGoods activityGoods = getActivityGoods(goodsId);
        if (activityGoods == null) {
            return null;
        }
        ActivityGoodsDTO activityGoodsDTO = new ActivityGoodsDTO();
        activityGoodsDTO.setGoodsId(activityGoods.getActivityGoodsId());
        activityGoodsDTO.setGoodsName(activityGoods.getActivitySubject());
        activityGoodsDTO.setGoodsDesc(activityGoods.getActivityDesc());
        activityGoodsDTO.setGoodsPic(activityGoods.getActivityPicUrl());
        activityGoodsDTO.setGoodsType(activityGoods.getActivitygoodsType().getCode());
        if (consumeLog.getGoodsItemId() > 0l) {
            GoodsItem goodsItem = getGoodsItemById(consumeLog.getGoodsItemId());
            if (goodsItem != null) {
                activityGoodsDTO.setGoodsItemId(goodsItem.getGoodsItemId());
                activityGoodsDTO.setItemName1(goodsItem.getSnName1());
                activityGoodsDTO.setItemValue1(goodsItem.getSnValue1());
                activityGoodsDTO.setItemName2(goodsItem.getSnName2());
                activityGoodsDTO.setItemValue2(goodsItem.getSnValue2());
            }
        }
        activityGoodsDTO.setConsumeOrder(consumeLog.getConsumeOrder());
        if (goodsSeckill != null) {
            activityGoodsDTO.setSeckillId(goodsSeckill.getSeckillId());
        }
        return activityGoodsDTO;
    }
}
