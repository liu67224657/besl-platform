package com.enjoyf.platform.serv.lottery;


import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.db.lottery.LotteryHandler;
import com.enjoyf.platform.serv.lottery.factory.AbstractLotteryStrategy;
import com.enjoyf.platform.serv.lottery.factory.LotteryAwardRedis;
import com.enjoyf.platform.serv.lottery.factory.LotteryStrategy;
import com.enjoyf.platform.serv.lottery.factory.LotteryStrategyFactory;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.UserPointEvent;
import com.enjoyf.platform.service.lottery.*;
import com.enjoyf.platform.service.point.PointActionType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: liangtang
 * Date: 13-5-29
 * Time: 下午1:57
 * To change this template use File | Settings | File Templates.
 */
public class LotteryLogic implements LotteryService {

    private static final int LASETEST_QUERY_AWARDSIZE = 30;

    private static final Logger logger = LoggerFactory.getLogger(LotteryLogic.class);
    private LotteryHandler writeAbleHandler;
    private HandlerPool<LotteryHandler> readonlyHandlersPool;

    private LotteryCache lotteryCache = null;

    private LotteryRedis lotteryRedis = null;

    private QueueThreadN eventProcessQueueThreadN = null;


    private RedisManager redisManager;

    private LotteryAwardRedis lotteryAwardRedis;

    public LotteryLogic(LotteryConfig cfg) {

        redisManager = new RedisManager(cfg.getProps());
        try {
            writeAbleHandler = new LotteryHandler(cfg.getWriteableDataSourceName(), cfg.getProps());

            readonlyHandlersPool = new HandlerPool<LotteryHandler>();
            for (String dsn : cfg.getReadonlyDataSourceNames()) {
                readonlyHandlersPool.add(new LotteryHandler(dsn, cfg.getProps()));
            }
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());
            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        lotteryCache = new LotteryCache(cfg.getMemCachedConfig());

        lotteryRedis = new LotteryRedis(cfg.getProps());

        lotteryAwardRedis = new LotteryAwardRedis(cfg.getProps());

        eventProcessQueueThreadN = new QueueThreadN(cfg.getEventQueueThreadNum(), new QueueListener() {

            public void process(Object obj) {
                if (obj instanceof CreateLotteryItem) {
                    processCreateAwardItems((CreateLotteryItem) obj);
                } else {
                    GAlerter.lab(this.getClass().getName() + " eventProcessQueueThreadN, there is a unknown obj." + obj);
                }
            }
        }, new FQueueQueue(cfg.getQueueDiskStorePath(), "eventProcessQueue"));
    }

    private void processCreateAwardItems(CreateLotteryItem item) {
        try {
            String[] itemids = new String[item.getItem().size()];
            for (int i = 0; i < item.getItem().size(); i++) {
                try {
                    LotteryAwardItem lotteryAwardItem = writeAbleHandler.insertLotteryAwardItem(item.getItem().get(i));
                    itemids[i] = String.valueOf(lotteryAwardItem.getLotteryAwardItemId());
                } catch (ServiceException e) {
                    GAlerter.lan(this.getClass().getName() + " insert award item error.e: ", e);
                }
            }

            // lotteryRedis.lpushAward(item.getAwardId(), itemids);
            lotteryAwardRedis.lpushLotteryAwardItemId(item.getAwardId(), itemids);
            modifyLotteryAwardById(new UpdateExpress()
                    .increase(LotteryAwardField.LOTTERY_AWARD_REST_AMOUNT, itemids.length)
                    .set(LotteryAwardField.VALID_STATUS, ValidStatus.VALID.getCode()), item.getAwardId());
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
        }
    }


    @Override
    public Lottery createLottery(Lottery lottery) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler createLottery:lottery is " + lottery);
        }
        return writeAbleHandler.insertLottery(lottery);
    }

    @Override
    public List<Lottery> queryLottery(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call getHandler queryLottery:queryExpress is " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryLottery(queryExpress);
    }

    @Override
    public PageRows<Lottery> queryLotteryByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandlersPool queryLottery:queryExpress is " + queryExpress + ",pagination is " + pagination);
        }
        return readonlyHandlersPool.getHandler().queryLotteryByPage(queryExpress, pagination);
    }

    @Override
    public Lottery getLotteryById(long lotteryId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandlersPool queryLotteryById:lotteryId is " + lotteryId);
        }
        Lottery lottery = lotteryCache.getLottery(lotteryId);
        if (lottery == null) {
            lottery = readonlyHandlersPool.getHandler().getLottery(lotteryId);
            if (lottery != null) {
                lotteryCache.putLottery(lotteryId, lottery);
            }
        }
        return lottery;
    }

    @Override
    public boolean modifyLotteryById(UpdateExpress updateExpress, long lotteryId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler modifyLotteryById:updateExpress is " + updateExpress + ",lotteryId is " + lotteryId);
        }
        boolean bool = writeAbleHandler.updateLottery(updateExpress, lotteryId);
        if (bool) {
            lotteryCache.deleteLottery(lotteryId);
        }
        return bool;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public LotteryAward createLotteryAward(LotteryAward lotteryAward) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeAbleHandler createLotteryAward:lotteryAward is " + lotteryAward);
        }
        lotteryAward = writeAbleHandler.insertLotteryAward(lotteryAward);

        //如果是实物的话，往item表放入
        if (lotteryAward.getLotteryAwardType().equals(LotteryAwardType.GOODS)) {
            for (int i = 0; i < lotteryAward.getLotteryAwardAmount(); i++) {

                LotteryAwardItem lotteryAwardItem = new LotteryAwardItem();
                lotteryAwardItem.setLotteryAwardId(lotteryAward.getLotteryAwardId());
                lotteryAwardItem.setName1(lotteryAward.getLotteryAwardName());
                lotteryAwardItem.setValue1(UUID.randomUUID().toString().substring(0, 6));
                lotteryAwardItem.setLotteryStatus(ValidStatus.VALID);
                lotteryAwardItem.setCreateDate(new Date());
                lotteryAwardItem.setLotteryId(lotteryAward.getLotteryId());
                lotteryAwardItem = writeAbleHandler.insertLotteryAwardItem(lotteryAwardItem);
                if (lotteryAwardItem.getLotteryAwardItemId() > 0) {
                    lotteryAwardRedis.lpushLotteryAwardItemId(lotteryAward.getLotteryAwardId(), lotteryAwardItem.getLotteryAwardItemId() + "");
                }
            }
        }
        return lotteryAward;
    }

    @Override
    public List<LotteryAward> queryLotteryAward(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandlersPool queryLotteryAward:queryExpress is " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryLotteryAward(queryExpress);
    }

    @Override
    public PageRows<LotteryAward> queryLotteryAwardByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandlersPool queryLotteryAwardByPage:queryExpress is " + queryExpress + ",pagination is " + pagination);
        }
        return readonlyHandlersPool.getHandler().queryLotteryAward(queryExpress, pagination);
    }

    @Override
    public LotteryAward getLotteryAwardById(long lotteryAwardId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandlersPool getLotteryAwardById:lotteryAwardId is " + lotteryAwardId);
        }
        return readonlyHandlersPool.getHandler().getById(lotteryAwardId);
    }

    @Override
    public LotteryAward getLotteryAwardByRate(long lotteryId, int randomNum) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler getLotteryAwardByRate:randomNum is " + randomNum);
        }
        return readonlyHandlersPool.getHandler().getByRate(lotteryId, randomNum);
    }

    @Override
    public LotteryAward getLotteryAwardByLevel(int awardLevel, long lotteryId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandlersPool getLotteryAwardById:awardLevel=" + awardLevel + " lotteryId=" + lotteryId);
        }
        return readonlyHandlersPool.getHandler().getByLevel(awardLevel, lotteryId);
    }

    @Override
    public boolean modifyLotteryAwardById(UpdateExpress updateExpress, long lotteryAwardId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler modifyLotteryAwardById:updateExpress is " + updateExpress + ",lotteryAwardId is " + lotteryAwardId);
        }
        LotteryAward lotteryAward = readonlyHandlersPool.getHandler().getById(lotteryAwardId);
        if (lotteryAward == null) {
            return false;
        }
        boolean bool = writeAbleHandler.updateLotteryAward(updateExpress, lotteryAwardId);
        if (bool) {
            lotteryCache.deleteLotteryAwardList(lotteryAward.getLotteryId());
            lotteryRedis.removeLotteryAwardPool(lotteryAward.getLotteryId());//删除奖品池
            lotteryAwardRedis.removeLotteryAwardPool(lotteryAward.getLotteryId());


            //如果是修改的同时修改item
            UpdateExpress update = new UpdateExpress();
            update.set(LotteryAwardItemField.LOTTERY_STATUS, ValidStatus.INVALID.getCode());

            QueryExpress query = new QueryExpress();
            query.add(QueryCriterions.eq(LotteryAwardItemField.LOTTERY_AWARD_ID, lotteryAwardId));
            boolean bval = writeAbleHandler.modifyLotteryAwardItem(update, query);
            if (bval && lotteryAward.getLotteryAwardType().equals(LotteryAwardType.GOODS)) {
                for (int i = 0; i < lotteryAward.getLotteryAwardAmount(); i++) {
                    LotteryAwardItem lotteryAwardItem = new LotteryAwardItem();
                    lotteryAwardItem.setLotteryAwardId(lotteryAward.getLotteryAwardId());
                    lotteryAwardItem.setName1(lotteryAward.getLotteryAwardName());
                    lotteryAwardItem.setValue1(UUID.randomUUID().toString().substring(0, 6));
                    lotteryAwardItem.setLotteryStatus(ValidStatus.VALID);
                    lotteryAwardItem.setCreateDate(new Date());
                    lotteryAwardItem.setLotteryId(lotteryAward.getLotteryId());
                    lotteryAwardItem = writeAbleHandler.insertLotteryAwardItem(lotteryAwardItem);
                    if (lotteryAwardItem.getLotteryAwardItemId() > 0) {
                        lotteryAwardRedis.lpushLotteryAwardItemId(lotteryAward.getLotteryAwardId(), lotteryAwardItem.getLotteryAwardItemId() + "");
                    }
                }
            }
        }
        return bool;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean createLotteryAwardItem(long awardId, List<LotteryAwardItem> lotteryAwardItemList) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler createLotteryAwardItem:lotteryAwardItemList is " + lotteryAwardItemList);
        }
        if (CollectionUtil.isEmpty(lotteryAwardItemList)) {
            return false;
        }

        eventProcessQueueThreadN.add(new CreateLotteryItem(awardId, lotteryAwardItemList));
        return true;
    }


    @Override
    public PageRows<LotteryAwardItem> queryByLotteryAwardIdPage(long lotteryAwardId, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandlersPool queryByLotteryAwardIdPage:lotteryAwardId is " + lotteryAwardId + ",pagination is " + pagination);
        }
        return readonlyHandlersPool.getHandler().queryLotteryAwardItem(lotteryAwardId, pagination);
    }

    @Override
    public List<LotteryAwardItem> queryLotteryAwardItem(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandlersPool queryLotteryAwardItem:queryExpress is " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryLotteryAwardItem(queryExpress);
    }

    @Override
    public LotteryAwardItem getLotteryAwardItemById(long lotteryAwardItemId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandlersPool getLotteryAwardItemById:lotteryAwardItemId is " + lotteryAwardItemId);
        }
        return readonlyHandlersPool.getHandler().getLotteryAwardItem(lotteryAwardItemId);
    }

    @Override
    public boolean modifyLotteryAwardItemById(UpdateExpress updateExpress, long lotteryAwardItemId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler modifyLotteryAwardItemById:updateExpress is " + updateExpress + ",lotteryAwardItemId is " + lotteryAwardItemId);
        }
        return writeAbleHandler.updateLotteryAwardItemById(updateExpress, lotteryAwardItemId);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public PageRows<UserDayLottery> queryUserDayLottery(long lotteryId, Date date, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandlersPool queryUserDayLottery:lotteryId is " + lotteryId + ",pagination is " + pagination);
        }
        //todo
        return writeAbleHandler.queryUserDayLottery(lotteryId, date, pagination);
    }

    @Override
    public UserDayLottery getUserDayLottery(long lotteryId, String uno, Date date) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandlersPool getUserDayLottery:lotteryId=" + lotteryId + ",uno" + uno + ",date=" + date);
        }
        return writeAbleHandler.getUserDayLottery(lotteryId, uno, date);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public UserLotteryLog createUserLotteryLog(UserLotteryLog userLotteryLog) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandlersPool createUserLotteryLog:" + userLotteryLog);
        }
        return writeAbleHandler.insertUserLotteryLog(userLotteryLog);
    }

    @Override
    public PageRows<UserLotteryLog> queryUserLotteryLogByPage(long lotteryId, String uno, Date from, Date to, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandlersPool queryUserLotteryLogByPage: uno is " + uno + " date from is " + from + " date to is " + to + " pagination is " + pagination);
        }
        return readonlyHandlersPool.getHandler().queryUserLotteryLogByUser(lotteryId, uno, from, to, pagination);
    }

    @Override
    public List<UserLotteryLog> queryUserLotteryLog(long lotteryId, String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler queryUserLotteryLogByPage: uno=" + uno + " lotteryId=" + lotteryId);
        }
        return writeAbleHandler.queryUserLotteryLog(lotteryId, uno);
    }

    @Override
    public UserLotteryLog getUserLotteryLog(long lotteryId, String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler queryUserLotteryLogByPage: uno=" + uno + " lotteryId=" + lotteryId);
        }
        return writeAbleHandler.getUserLotteryLog(lotteryId, uno);
    }

    @Override
    public List<UserLotteryLog> queryLastestUserLotteryLog(long lotteryid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readhandler queryLastestUserLotteryLog:  lotteryId=" + lotteryid);
        }
        List<UserLotteryLog> returnObj = lotteryCache.getUserLotteryLogList(lotteryid);

        if (returnObj == null) {
            returnObj = readonlyHandlersPool.getHandler().queryLastestLotteryLog(lotteryid, LASETEST_QUERY_AWARDSIZE);
            lotteryCache.putUserLotteryLogList(lotteryid, returnObj);
        }

        return returnObj;
    }

    @Override
    public UserLotteryLog userLotteryAward(String uno, String screenName, String ip, Date lotteryDate, long lotteryId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler userLotteryAward:uno is " + uno + " date is " + lotteryDate + " lotteryId is " + lotteryId);
        }

        Lottery lottery = readonlyHandlersPool.getHandler().getLottery(lotteryId);
        if (lottery == null) {
            throw new ServiceException(LotteryServiceException.LOTTERY_NOT_EXISTS, "lottery not exists.lotteryId:" + lotteryId);
        }

        int randomNum = (int) (Math.random() * (lottery.getBaseRate() - 1)) + 1;

        return writeAbleHandler.userLotteryAward(uno, screenName, ip, lotteryDate, randomNum, lotteryId, lottery.getLotteryType());
    }

    @Override
    public AwardEntry userLotteryAwardEntry(String uno, String screenName, String ip, Date lotteryDate, long lotteryId, AwardEntry awardEntry, int baseRate, LotteryType lotteryType) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler userLotteryAward:uno is " + uno + " date is " + lotteryDate + " lotteryId is " + lotteryId);
        }

        int randomNum = (int) (Math.random() * (baseRate - 1)) + 1;

        awardEntry = writeAbleHandler.userLotteryAwardEntry(uno, screenName, ip, lotteryDate, randomNum, lotteryId, lotteryType, awardEntry);
        if (awardEntry != null && awardEntry.getAward() != null && awardEntry.getAward().getLotteryAwardType().equals(LotteryAwardType.POINT)) {
            int point = Integer.parseInt(awardEntry.getAward().getLotteryAwardDesc());
            UserPointEvent event = new UserPointEvent();
            //TODO
            event.setProfileId(uno);
            event.setObjectId("" + awardEntry.getAward().getLotteryAwardId());
            event.setPoint(point);
            event.setPointActionType(PointActionType.LOTTERY_AWARD);
            event.setDescription("活动抽奖");
            EventDispatchServiceSngl.get().dispatch(event);
        }
        awardEntry = new AwardEntry(awardEntry.getAwardItem(), awardEntry.getAward());
        return awardEntry;
    }
//
//    @Override
//    public List<Ticket> queryTicketList(QueryExpress queryExpress) throws ServiceException {
//        if (logger.isDebugEnabled()) {
//            logger.debug("call writeHandler QueryExpress=" + queryExpress);
//        }
//        return readonlyHandlersPool.getHandler().queryTicket(queryExpress);
//    }
//
//    @Override
//    public PageRows<Ticket> queryTicketPageRows(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
//        if (logger.isDebugEnabled()) {
//            logger.debug("call writeHandler QueryExpress=" + queryExpress);
//        }
//        return readonlyHandlersPool.getHandler().queryTicket(queryExpress, pagination);
//    }
//
//    @Override
//    public boolean modifyTicket(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
//        if (logger.isDebugEnabled()) {
//            logger.debug("call writeHandler QueryExpress=" + queryExpress);
//        }
//        return writeAbleHandler.modifyTicket(updateExpress, queryExpress);
//    }
//
//    @Override
//    public Ticket createTicket(Ticket ticket) throws ServiceException {
//        if (logger.isDebugEnabled()) {
//            logger.debug("call writeHandler Ticket=" + ticket);
//        }
//        return writeAbleHandler.insertTicket(ticket);
//    }
//
//    @Override
//    public Ticket getTicketById(QueryExpress queryExpress) throws ServiceException {
//        if (logger.isDebugEnabled()) {
//            logger.debug("call writeHandler QueryExpress=" + queryExpress);
//        }
//        return readonlyHandlersPool.getHandler().getTicket(queryExpress);
//    }
//
//    @Override
//    public List<TicketAward> queryTicketAwardList(QueryExpress queryExpress) throws ServiceException {
//        return readonlyHandlersPool.getHandler().queryTicketAward(queryExpress);
//    }
//
//    @Override
//    public PageRows<TicketAward> queryTicketAwardPageRows(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
//        if (logger.isDebugEnabled()) {
//            logger.debug("call writeHandler QueryExpress=" + queryExpress + " Pagination=" + pagination);
//        }
//        return readonlyHandlersPool.getHandler().queryTicketAward(queryExpress, pagination);
//    }
//
//    @Override
//    public TicketAward getTicketAwardById(QueryExpress queryExpress) throws ServiceException {
//        if (logger.isDebugEnabled()) {
//            logger.debug("call writeHandler QueryExpress=" + queryExpress);
//        }
//        return readonlyHandlersPool.getHandler().getTicketAward(queryExpress);
//    }
//
//    @Override
//    public boolean modifyTicketAward(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
//        if (logger.isDebugEnabled()) {
//            logger.debug("call writeHandler QueryExpress=" + queryExpress + " UpdateExpress=" + updateExpress);
//        }
//        return writeAbleHandler.modifyTicketAward(updateExpress, queryExpress);
//    }
//
//    @Override
//    public TicketAward createTicketAward(TicketAward ticketAward) throws ServiceException {
//        if (logger.isDebugEnabled()) {
//            logger.debug("call writeHandler TicketAward=" + ticketAward);
//        }
//        return writeAbleHandler.createTicketAward(ticketAward);
//    }

    @Override
    public List<LotteryAward> queryLotteryAwardByCache(long lotteryId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryLotteryAwardByCache.lotteryId:" + lotteryId);
        }
        List<LotteryAward> returnList = lotteryCache.getLotteryAwardList(lotteryId);
        if (CollectionUtil.isEmpty(returnList)) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(LotteryAwardField.LOTTERY_ID, lotteryId));
            queryExpress.add(QueryCriterions.eq(LotteryAwardField.VALID_STATUS, ValidStatus.VALID.getCode()));
            queryExpress.add(QuerySort.add(LotteryAwardField.LOTTERY_AWARD_LEVEL, QuerySortOrder.ASC));
            returnList = readonlyHandlersPool.getHandler().queryLotteryAward(queryExpress);
            if (!CollectionUtil.isEmpty(returnList)) {
                lotteryCache.putLotteryAwardList(lotteryId, returnList);
            }
        }
        return returnList;
    }

    @Override
    public LotteryAward userLottery(Long lotteryId, String profileId, String ip, Profile profile, String code) throws ServiceException {
        //2015 12 12
        Date date = new Date();
        String date_short = DateUtil.formatDateToString(date, DateUtil.PATTERN_DATE_SHORT);
        //增加调用的次数 todo
        lotteryCache.addLotteryCounter(date);

        int shareTime = lotteryRedis.getShuang12ShareTime(profile.getUid(), date_short);
        if (shareTime <= 0) {
            throw new ServiceException(LotteryServiceException.USER_HAS_NO_TIME, "user has no time " + profileId);
        }

        // 如果这个人中了 不进行
        long uid = lotteryRedis.getShuang12HasLottery(profile.getUid(), date_short);
        //已经抽过了
        if (uid > 0) {
            throw new ServiceException(LotteryServiceException.USER_HAD_LOTTERY_AWARD_TODAY, "user had lottery award " + profileId);
        }

        //先放抽奖
        lotteryRedis.putShuang12HasLottery(profile.getUid(), date_short);

        List<LotteryAward> returnList = lotteryCache.getLotteryAwardList(lotteryId);
        if (CollectionUtil.isEmpty(returnList)) {
            lotteryCache.deleteLotteryCodeMap(lotteryId);
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(LotteryAwardField.LOTTERY_ID, lotteryId));
            queryExpress.add(QueryCriterions.eq(LotteryAwardField.VALID_STATUS, ValidStatus.VALID.getCode()));
            returnList = writeAbleHandler.queryLotteryAward(queryExpress);
            if (!CollectionUtil.isEmpty(returnList)) {
                lotteryCache.putLotteryAwardList(lotteryId, returnList);
            }
        }

        if (CollectionUtil.isEmpty(returnList)) {
            return null;
        }

        //用户抽中的号码 抽奖方法
        int curTimeInt = Long.valueOf(date.getTime() / 1000).intValue();


        //将查询出来的奖品都放到map，key是中奖数
        LotteryAward returnAward = null;
        Map<Integer, LotteryAward> lotteryCodeMap = lotteryCache.getLotteryCodeMap(lotteryId);
        if (CollectionUtil.isEmpty(lotteryCodeMap)) {
            lotteryCodeMap = new HashMap<Integer, LotteryAward>();
            for (LotteryAward award : returnList) {
                //如果没有奖品剩余，则不放入
                if (award.getLotteryAwardRestAmount() <= 0) {
                    continue;
                }
                Set<Integer> set = award.getLotteryCode();
                for (Integer i : set) {
                    lotteryCodeMap.put(i, award);
                }
            }
            lotteryCache.putLotteryCodeMap(lotteryId, lotteryCodeMap);
        }

        //用户抽中的号码与中奖数一致
        if (lotteryCodeMap.containsKey(curTimeInt)) {

            LotteryAward lotteryAward = lotteryCodeMap.get(curTimeInt);
            //如果奖品剩余数小于等于0
            if (lotteryAward.getLotteryAwardRestAmount() <= 0) {
                return null;
            }

            UserLotteryLog userLotteryLog = new UserLotteryLog();
            userLotteryLog.setUno(profile == null ? "" : profile.getProfileId());
            userLotteryLog.setLotteryAwardPic("");
            userLotteryLog.setScreenName(profile == null ? "" : profile.getProfileId());
            userLotteryLog.setLotteryId(lotteryId);
            userLotteryLog.setLotteryAwardId(lotteryId);
            userLotteryLog.setLotteryAwardName(lotteryAward.getLotteryAwardName());
            userLotteryLog.setLotteryAwardDesc(lotteryAward.getLotteryAwardDesc());
            userLotteryLog.setLotteryAwardLevel(lotteryAward.getLotteryAwardLevel());
            userLotteryLog.setLottery_code(curTimeInt + "");
            userLotteryLog.setLotteryDate(date);
            userLotteryLog.setLotteryIp(ip);
            userLotteryLog.setExtension(profile.getProfileId());

            UserLotteryLog userLotteryLog1 = writeAbleHandler.insertUserLotteryLog(userLotteryLog);
            if (userLotteryLog1 != null) {
                //中奖后，删除重新放入缓存。
                lotteryCache.deleteLotteryAwardList(lotteryId);
                //奖品剩余数-1
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.increase(LotteryAwardField.LOTTERY_AWARD_REST_AMOUNT, -1);
                boolean bool = writeAbleHandler.updateLotteryAward(updateExpress, lotteryAward.getLotteryAwardId());
                if (bool) {
                    returnAward = lotteryCodeMap.get(curTimeInt);
                } else {
                    return null;
                }
            }
        } else {
            UserLotteryLog userLotteryLog = new UserLotteryLog();
            userLotteryLog.setUno(profile == null ? "" : profile.getProfileId());
            userLotteryLog.setLotteryAwardPic("");
            userLotteryLog.setScreenName(profile == null ? "" : profile.getProfileId());
            userLotteryLog.setLotteryId(lotteryId);
            userLotteryLog.setLotteryAwardId(lotteryId);
            userLotteryLog.setLotteryAwardName("");
            userLotteryLog.setLotteryAwardDesc("");
            userLotteryLog.setLotteryAwardLevel(0);
            userLotteryLog.setLotteryDate(date);
            userLotteryLog.setLottery_code(curTimeInt + "");
            userLotteryLog.setLotteryIp(ip);
            userLotteryLog.setExtension(profile.getProfileId());
            try {
                writeAbleHandler.insertUserLotteryLog(userLotteryLog);
            } catch (DbException e) {
                //因为设置了唯一约束，所以需要捕获异常，将这次抽奖记录记录下
                userLotteryLog.setLottery_code(curTimeInt + "_" + System.currentTimeMillis());
                writeAbleHandler.insertUserLotteryLog(userLotteryLog);
            }
        }

        return returnAward;


//        Date date = new Date();
//        //实物商品
//        Map<Integer, String> awardMap = new HashMap<Integer, String>();
//        awardMap.put(100, "着迷logo帽");
//        awardMap.put(300, "迷仔");
//        //虚拟商品
//        Map<Integer, String> pointMap = new HashMap<Integer, String>();
//        pointMap.put(200, "100");
//        pointMap.put(400, "100");
//        pointMap.put(700, "100");
//        pointMap.put(600, "200");
//        pointMap.put(800, "200");
//        //增加调用的次数
//        lotteryCache.addLotteryCounter(date);
//
//        String date_short = DateUtil.formatDateToString(date, DateUtil.PATTERN_DATE_SHORT);
//
//        int totleTime = lotteryRedis.getEverytTotleTime(date_short + profileId);//总次数
//        int useTime = lotteryRedis.getEverytUseTime(date_short + profileId);//用过次数
//
//
//        //次数用完了
//        if (useTime >= totleTime || useTime >= 8) {
//
//            LotteryAward lotteryAward = new LotteryAward();
//            lotteryAward.setLotteryAwardLevel(-1);
//            lotteryAward.setCreateIp("" + useTime);
//            return lotteryAward;
//        }
//
//        //用过次数+1，先减少一次抽奖
//        lotteryRedis.putEverytUseTime(date_short + profileId);
//        //抽奖的次数
//        //中秋活动中奖规则：每天第100次中帽子 300次中迷仔 600、800次中200迷豆 200、400、700次 中100迷豆 其余的 随机10-30迷豆
//        int total = lotteryRedis.getTodayTotal(date_short);
//        lotteryRedis.putTodayTotal(date_short);
//
//        UserLotteryLog userLotteryLog = new UserLotteryLog();
//        userLotteryLog.setUno(profile == null ? "" : profile.getProfileId());
//        userLotteryLog.setLotteryAwardPic(profileId);
//        userLotteryLog.setScreenName(profile == null ? "" : profile.getNick());
//        userLotteryLog.setLotteryId(lotteryId);
//        userLotteryLog.setLotteryAwardId(lotteryId);
//        userLotteryLog.setLotteryAwardLevel(0);
//        userLotteryLog.setLottery_code(date_short + "_" + total);
//        userLotteryLog.setLotteryDate(date);
//        userLotteryLog.setLotteryIp(ip);
//        userLotteryLog.setExtension(profile.toString());
//        String wardName = awardMap.get(total);
//
//        //除了规定的中奖次数规则以外 其余的给10 20 30随机迷豆
//        int[] point = {10, 20, 30};
//        //实物商品
//        String lotteryValue = lotteryRedis.getTodayLevel(date_short + profile.getProfileId());
//        String pointName = pointMap.get(total);
//        if (!StringUtil.isEmpty(wardName)) {
//            //每人每天只能中一次实物商品
//            if (!StringUtil.isEmpty(lotteryValue)) {
//                int num = (int) (Math.random() * 2);
//                userLotteryLog.setLotteryAwardName("迷豆");
//                //中过实物，然后抽了100迷豆
//                userLotteryLog.setLotteryAwardDesc(StringUtil.isEmpty(pointName) ? String.valueOf(point[num]) : pointName);
//            } else {
//                userLotteryLog.setLotteryAwardLevel(total);
//                userLotteryLog.setLotteryAwardName(wardName);
//                userLotteryLog.setLotteryAwardDesc(wardName);
//            }
//        } else {
//            if (!StringUtil.isEmpty(pointName)) {
//                userLotteryLog.setLotteryAwardName("迷豆");
//                userLotteryLog.setLotteryAwardDesc(pointName);
//            } else {
//                int num = (int) (Math.random() * 2);
//                userLotteryLog.setLotteryAwardName("迷豆");
//                userLotteryLog.setLotteryAwardDesc(String.valueOf(point[num]));
//            }
//        }
//
//
//        UserLotteryLog userLotteryLog1 = null;
//        LotteryAward returnAward = new LotteryAward();
//        try {
//            userLotteryLog1 = writeAbleHandler.insertUserLotteryLog(userLotteryLog);
//            returnAward.setLotteryAwardType(LotteryAwardType.POINT);
//            returnAward.setLotteryAwardLevel(0);
//            //每个用户每天只能抽取到一次实物商品
//            if (!StringUtil.isEmpty(wardName) && StringUtil.isEmpty(lotteryValue)) {
//                returnAward.setLotteryAwardLevel(total);
//                returnAward.setLotteryAwardType(LotteryAwardType.GOODS);
//                lotteryRedis.putTodayLevel(date_short + profile.getProfileId(), wardName);
//            }
//
//            returnAward.setCreateDate(new Date());
//            returnAward.setLotteryAwardDesc(userLotteryLog1.getLotteryAwardDesc());
//            returnAward.setLotteryAwardName(userLotteryLog1.getLotteryAwardName());
//            return returnAward;
//        } catch (DbException e) {
//            //因为设置了唯一约束，如果并发出异常 则后面抽的人随机给10-30迷豆并且记录下来             .
//            int random = (int) (Math.random() * 50000);
//            userLotteryLog.setLottery_code(date_short + "_" + total + "_" + random);
//            int num = (int) (Math.random() * 2);
//            userLotteryLog.setLotteryAwardName("迷豆");
//            //
//            userLotteryLog.setLotteryAwardDesc(String.valueOf(point[num]));
//            userLotteryLog1 = writeAbleHandler.insertUserLotteryLog(userLotteryLog);
//
//            returnAward.setLotteryAwardType(LotteryAwardType.POINT);
//            returnAward.setCreateDate(new Date());
//            returnAward.setLotteryAwardLevel(0);
//            returnAward.setLotteryAwardDesc(userLotteryLog1.getLotteryAwardDesc());
//            returnAward.setLotteryAwardName(userLotteryLog1.getLotteryAwardName());
//            return returnAward;
//        }


        //20150507活动
//        List<LotteryAward> returnList = lotteryCache.getLotteryAwardList(lotteryId);
//
//
//        if (CollectionUtil.isEmpty(returnList)) {
//            lotteryCache.deleteLotteryCodeMap(lotteryId);
//            QueryExpress queryExpress = new QueryExpress();
//            queryExpress.add(QueryCriterions.eq(LotteryAwardField.LOTTERY_ID, lotteryId));
//            queryExpress.add(QueryCriterions.eq(LotteryAwardField.VALID_STATUS, ValidStatus.VALID.getCode()));
//            returnList = writeAbleHandler.queryLotteryAward(queryExpress);
//            if (!CollectionUtil.isEmpty(returnList)) {
//                lotteryCache.putLotteryAwardList(lotteryId, returnList);
//            }
//        }
//
//        if (CollectionUtil.isEmpty(returnList)) {
//            return null;
//        }

        //用户抽中的号码
        //  int curTimeInt = Long.valueOf(date.getTime() / 1000).intValue();

        //测试用
//        if (!StringUtil.isEmpty(code)) {
//            curTimeInt = Integer.valueOf(code);
//        }


//        //将查询出来的奖品都放到map，key是中奖数
//        LotteryAward returnAward = null;
//        Map<Integer, LotteryAward> lotteryCodeMap = lotteryCache.getLotteryCodeMap(lotteryId);
//        if (CollectionUtil.isEmpty(lotteryCodeMap)) {
//            lotteryCodeMap = new HashMap<Integer, LotteryAward>();
//            for (LotteryAward award : returnList) {
//                //如果没有奖品剩余，则不放入
//                if (award.getLotteryAwardRestAmount() <= 0) {
//                    continue;
//                }
//                Set<Integer> set = award.getLotteryCode();
//                for (Integer i : set) {
//                    lotteryCodeMap.put(i, award);
//                }
//            }
//            lotteryCache.putLotteryCodeMap(lotteryId, lotteryCodeMap);
//        }

        //用户抽中的号码与中奖数一致
//        if (lotteryCodeMap.containsKey(curTimeInt)) {
//
//            LotteryAward lotteryAward = lotteryCodeMap.get(curTimeInt);
//            //如果奖品剩余数小于等于0
//            if (lotteryAward.getLotteryAwardRestAmount() <= 0) {
//                return null;
//            }
//
//
//            //查询设备今天是否中过奖，如果今天中奖了，直接返回
//            String lotteryValue = lotteryRedis.getTodayLevel(date_short + clientid);
//            if (!StringUtil.isEmpty(lotteryValue)) {
//                return null;
//            }
//            //查询用户今天是否中过奖，如果今天中奖了，直接返回
//            lotteryValue = lotteryRedis.getTodayLevel(date_short + profile.getProfileId());
//            if (!StringUtil.isEmpty(lotteryValue)) {
//                return null;
//            }
//
//
//            //如果此设备在活动中过1、2等奖
//            String onttwoLotteyValue = lotteryRedis.getOneortwoLevel(clientid);
//            if (!StringUtil.isEmpty(onttwoLotteyValue) && lotteryAward.getLotteryAwardLevel() <= 2) {
//                return null;
//            }
//
//            //如果此用户在活动中过1、2等奖
//            onttwoLotteyValue = lotteryRedis.getOneortwoLevel(profile.getProfileId());
//            if (!StringUtil.isEmpty(onttwoLotteyValue) && lotteryAward.getLotteryAwardLevel() <= 2) {
//                return null;
//            }
//
//
//            UserLotteryLog userLotteryLog = new UserLotteryLog();
//            userLotteryLog.setUno(profile == null ? "" : profile.getProfileId());
//            userLotteryLog.setLotteryAwardPic(clientid);
//            userLotteryLog.setScreenName(profile == null ? "" : profile.getNick());
//            userLotteryLog.setLotteryId(lotteryId);
//            userLotteryLog.setLotteryAwardId(lotteryId);
//            userLotteryLog.setLotteryAwardName(lotteryAward.getLotteryAwardName());
//            userLotteryLog.setLotteryAwardDesc(lotteryAward.getLotteryAwardDesc());
//            userLotteryLog.setLotteryAwardLevel(lotteryAward.getLotteryAwardLevel());
//            userLotteryLog.setLottery_code(curTimeInt + "");
//            userLotteryLog.setLotteryDate(date);
//            userLotteryLog.setLotteryIp(ip);
//            userLotteryLog.setExtension(profile.toString());
//
//            UserLotteryLog userLotteryLog1 = writeAbleHandler.insertUserLotteryLog(userLotteryLog);
//            if (userLotteryLog1 != null) {
//
//                //中奖了，放入当天中奖记录或者1、2等奖，设备和用户
//                if (userLotteryLog1.getLotteryAwardLevel() <= 2) {
//                    lotteryRedis.putOneortwoLevel(clientid, lotteryAward.toString());
//                    lotteryRedis.putOneortwoLevel(profile.getProfileId(), lotteryAward.toString());
//                }
//
//                //把这个用户和设备今天的中间状态放入redis
//                lotteryRedis.putTodayLevel(date_short + clientid, lotteryAward.toString());
//                lotteryRedis.putTodayLevel(date_short + profile.getProfileId(), lotteryAward.toString());
//
//
//                //中奖后，删除重新放入缓存。
//                lotteryCache.deleteLotteryAwardList(lotteryId);
//                //奖品剩余数-1
//                UpdateExpress updateExpress = new UpdateExpress();
//                updateExpress.increase(LotteryAwardField.LOTTERY_AWARD_REST_AMOUNT, -1);
//                writeAbleHandler.updateLotteryAward(updateExpress, lotteryAward.getLotteryAwardId());
//                returnAward = lotteryCodeMap.get(curTimeInt);
//            }
//        } else {
//            UserLotteryLog userLotteryLog = new UserLotteryLog();
//            userLotteryLog.setUno(profile == null ? "" : profile.getProfileId());
//            userLotteryLog.setLotteryAwardPic(clientid);
//            userLotteryLog.setScreenName(profile == null ? "" : profile.getNick());
//            userLotteryLog.setLotteryId(lotteryId);
//            userLotteryLog.setLotteryAwardId(lotteryId);
//            userLotteryLog.setLotteryAwardName("");
//            userLotteryLog.setLotteryAwardDesc("");
//            userLotteryLog.setLotteryAwardLevel(0);
//            userLotteryLog.setLotteryDate(date);
//            userLotteryLog.setLottery_code(curTimeInt + "");
//            userLotteryLog.setLotteryIp(ip);
//            userLotteryLog.setExtension(profile.toString());
//            try {
//                writeAbleHandler.insertUserLotteryLog(userLotteryLog);
//            } catch (DbException e) {
//                //因为设置了唯一约束，所以需要捕获异常，将这次抽奖记录记录下
//                userLotteryLog.setLottery_code(curTimeInt + "_" + System.currentTimeMillis());
//                writeAbleHandler.insertUserLotteryLog(userLotteryLog);
//            }
//        }
    }

    @Override
    public boolean setUserLotteryShareTime(String clientid, String date_short) throws ServiceException {
        //增加分享调用的次数
        lotteryCache.addLotteryShareCounter(new Date());
        lotteryRedis.putEverytTotleTime(date_short + clientid);
        return true;
    }

    @Override
    public boolean addOrIncrTime(String key) throws ServiceException {
        lotteryCache.addOrIncrTime(new Date(), key);
        return true;
    }

    @Override
    public void putShuang12Cache(Profile profile, String words) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic putShuang12Cache:" + profile.getUid() + "," + words);
        }
        String value = String.valueOf(profile.getUid()) + "|" + words;
        lotteryRedis.putShuang12Cache(value);
    }

    @Override
    public PageRows<String> getShuang12Cache(Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic putShuang12Cache:" + pagination.getCurPage());
        }
        return lotteryRedis.getShuang12Cache(pagination);
    }

    @Override
    public void incrShuang12ShareTime(Profile profile) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("CommentLogic incrShuang12ShareTime:" + profile.getUid());
        }
        Date date = new Date();
        String date_short = DateUtil.formatDateToString(date, DateUtil.PATTERN_DATE_SHORT);
        lotteryRedis.incrShuang12ShareTime(profile.getUid(), date_short);
    }


    //////////////////////////////////////////////////
    public LotteryAwardItem lottery(long lotteryId, String profileId, String ip, Date date, String nick) throws ServiceException {
        ////////////////////todo getLottery by loggeryId and get rule 获取配置////////////////////////////
        Lottery lottery = getLotteryById(lotteryId);
        if (lottery == null) {
            return null;
        }

        LotteryRule lotteryRule = lottery.getLotteryRule();

        //todo check user chance
        int lotteryChance = lotteryRedis.getUserLotteryChance(lotteryId, profileId, lottery.getLotteryTimesType(), date, 1);
        if (lotteryChance <= 0l) {
            return null;
        }
//        lotteryRedis.incrUserLotteryChance(lotteryId, profile.getProfileId(), lottery.getLotteryTimesType(), -1, date);

        //todo check user lottery times 通过配置目前写死
        int lotteryTimes = lotteryRedis.getUserLotteryTimes(lotteryId, profileId, lottery.getLotteryTimesType(), date);
        if (lotteryTimes >= 2 || lotteryTimes >= lotteryChance) {
            //TODO 没机会了，页面需要根据这个提示
            LotteryAwardItem item = new LotteryAwardItem();
            item.setLotteryAwardItemId(-1000);
            return item;
        }
        //todo check user lottery result this 这里不用写
        //////////////////todo end////////////////////////////

        //todo use time
        lotteryRedis.incrUserLotteryTimes(lotteryId, profileId, lottery.getLotteryTimesType(), date, 1);

        //lottery action.1 get random number--->awardlist 2 pop award
        boolean poolExists = lotteryRedis.existsAwardPool(lotteryId);
        if (!poolExists) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(LotteryAwardField.LOTTERY_ID, lotteryId));
            queryExpress.add(QueryCriterions.eq(LotteryAwardField.VALID_STATUS, ValidStatus.VALID.getCode()));
            List<LotteryAward> list = readonlyHandlersPool.getHandler().queryLotteryAward(queryExpress);
            if (!CollectionUtil.isEmpty(list)) {
                lotteryRedis.initLotteryAwardPool(lotteryId, list);
            }
        }
        int randomNum = (int) (Math.random() * (lottery.getBaseRate() - 1)) + 1;
        long lotteryAwardId = lotteryRedis.getLotteryAwardPool(lotteryId, randomNum);
        LotteryAward award = null;
        if (lotteryAwardId > 0l) {
            award = getLotteryAwardById(lotteryAwardId);
        }

        LotteryAwardItem item = null;
        if (award != null) {
            //todo 写死逻辑start==========
            if (award.getLotteryAwardLevel() == 2) { //激活码
                //TODO 抽中了激活码,判断下小时剩余数
                int itemCount = lotteryRedis.getItemCount(lotteryId, date);
                if (itemCount >= 30) {
                    return null;
                }

                lotteryRedis.incrItemCount(lotteryId, 1, date);
            } else if (award.getLotteryAwardLevel() == 3) {//3是京东卡
                //TODO 京东卡每天1个 可以后getItemCount抽象
                int jd = lotteryRedis.getUserLotteryJD(lotteryId, date);
                if (jd >= 1) {
                    return null;
                } else {
                    //todo 实物卡也用code--->码来考虑
                    lotteryRedis.incrUserLotteryJD(lotteryId, 1, date);
                }
            } else if (award.getLotteryAwardLevel() == 5) {
                //TODO 现金红包10元每日限量20个（早上十点到晚上十点每个小时放出一个）,即每小时一个
                String startTime = DateUtil.formatDateToString(date, DateUtil.DATE_FORMAT) + " 10:00:00";
                String endTime = DateUtil.formatDateToString(date, DateUtil.DATE_FORMAT) + " 21:59:59";
                //不在时间段内
                if (!DateUtil.isInDate(date, startTime, endTime)) {
                    return null;
                } else {
                    int cash = lotteryRedis.getUserLotteryCash(lotteryId, date);
                    if (cash >= 1) {
                        return null;
                    } else {
                        lotteryRedis.incrUserLotteryCash(lotteryId, 1, date);
                    }
                }
            } else if (award.getLotteryAwardLevel() == 6) {
                //TODO 话费充值卡50元每日限量2个（下午两点到晚上七点一个，晚上七点到12点一个）按照时间段
                String startTime1 = DateUtil.formatDateToString(date, DateUtil.DATE_FORMAT) + " 14:00:00";
                String endTime1 = DateUtil.formatDateToString(date, DateUtil.DATE_FORMAT) + " 18:59:59";


                String startTime2 = DateUtil.formatDateToString(date, DateUtil.DATE_FORMAT) + " 19:00:00";
                String endTime2 = DateUtil.formatDateToString(date, DateUtil.DATE_FORMAT) + " 23:59:59";
                //不在时间段内
                String key = "";
                if (DateUtil.isInDate(date, startTime1, endTime1)) {
                    key = startTime1;
                } else if (DateUtil.isInDate(date, startTime2, endTime2)) {
                    key = startTime2;
                } else {
                    return null;
                }

                int topupphone = 0;
                try {

                    topupphone = lotteryRedis.getUserLotteryTopupphone(lotteryId, DateUtil.formatStringToDate(key, DateUtil.DEFAULT_DATE_FORMAT3));
                    if (topupphone >= 1) {
                        return null;
                    }
                    lotteryRedis.incrUserLotteryTopupphone(lotteryId, 1, DateUtil.formatStringToDate(key, DateUtil.DEFAULT_DATE_FORMAT3));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            //todo 写死逻辑end==========

            long itemId = lotteryRedis.popAwardItemId(lotteryAwardId);
            if (itemId >= 0l) {
                item = getLotteryAwardItemById(itemId);
            } else {
                item = new LotteryAwardItem();
                item.setLotteryAwardId(lotteryAwardId);
            }

        }

        //insert lottery log
        UserLotteryLog userLotteryLog = new UserLotteryLog();
        userLotteryLog.setUno(profileId);
        userLotteryLog.setLotteryAwardPic("");
        userLotteryLog.setScreenName(nick);
        userLotteryLog.setLotteryId(lotteryId);
        if (award != null) {
            userLotteryLog.setLotteryAwardId(award.getLotteryAwardId());
            userLotteryLog.setLotteryAwardName(award.getLotteryAwardName());
            userLotteryLog.setLotteryAwardDesc(award.getLotteryAwardDesc());
            userLotteryLog.setLotteryAwardLevel(award.getLotteryAwardLevel());
        }
        if (item != null) {
            userLotteryLog.setLottery_code(item.getValue1());
        }
        userLotteryLog.setLotteryDate(date);
        userLotteryLog.setLotteryIp(ip);
        userLotteryLog.setExtension(profileId);
        try {
            userLotteryLog = writeAbleHandler.insertUserLotteryLog(userLotteryLog);
            if (item != null) {
                writeAbleHandler.updateLotteryAwardItemById(new UpdateExpress()
                        .set(LotteryAwardItemField.LOTTERY_STATUS, ValidStatus.INVALID.getCode())
                        .set(LotteryAwardItemField.LOTTERY_DATE, date)
                        .set(LotteryAwardItemField.OWN_UNO, profileId), item.getLotteryAwardItemId());
            }

        } catch (DbException e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
        }


        return item;
    }

    @Override
    public boolean addChance(long lotteryId, String profileId, int value, Date date) throws ServiceException {
        Lottery lottery = getLotteryById(lotteryId);
        //todo get url check 是否给用户加上抽奖机会
        lotteryRedis.incrUserLotteryChance(lotteryId, profileId, lottery.getLotteryTimesType(), value, date);
        return true;
    }


    @Override
    public boolean modifyLotteryAddressByUid(LotteryAddress lotteryAddress, String profileid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call modifyLotteryAddressByUid LotteryAddress=" + lotteryAddress + ",profileid=" + profileid);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(LotteryAddressField.PROFILEID, profileid));
        LotteryAddress lottery = readonlyHandlersPool.getHandler().getLotteryAddress(queryExpress);
        if (lottery == null) {
            writeAbleHandler.insertLotteryAddress(lotteryAddress);
            return true;
        }
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(LotteryAddressField.ADDRESS, lotteryAddress.getAddress() != null ? lotteryAddress.getAddress().toJsonStr() : "");
        updateExpress.set(LotteryAddressField.UPDATEIP, lotteryAddress.getCreateIp());
        updateExpress.set(LotteryAddressField.UPDATETIME, lotteryAddress.getCreateTime());
        return writeAbleHandler.modifyLotteryAddress(updateExpress, queryExpress);
    }

    @Override
    public LotteryItemResult takelottery(long lotteryId, String profileId, String ip, Date curDate, String nick) throws ServiceException {
        LotteryItemResult result = new LotteryItemResult();

        Lottery lottery = getLotteryById(lotteryId);
        //无活动
        if (lottery == null) {
            result.setLotteryItemResultType(LotteryItemResultType.NOT_LOTTERY);
            return result;
        }

        LotteryAwardItem item = null;
        try {
            //未开始
            if (curDate.getTime() <= lottery.getStartDate().getTime()) {
                result.setLotteryItemResultType(LotteryItemResultType.NOT_START);
                return result;
            }

            //已结束
            if (curDate.getTime() >= lottery.getEndDate().getTime()) {
                result.setLotteryItemResultType(LotteryItemResultType.HAS_END);
                return result;
            }

            //返回活动具体类型
           LotteryStrategy lotteryStrategy = LotteryStrategyFactory.get().factory(this, lotteryAwardRedis, lottery.getLotteryRule().getLotteryRuleActionType());

            //判断是否有抽奖的机会 todo 如果用户初始就有2次抽奖机会呢?
            boolean existUserChance = lotteryStrategy.hasUserLotteryChance(lottery, curDate, profileId);
            if (!existUserChance) {
                result.setLotteryItemResultType(LotteryItemResultType.NO_CHANCE_LOTTERY);
                return result;
            }

            //抽奖
            item = lotteryStrategy.lottery(lottery, profileId, curDate, ip);

            result.setLotteryItemResultType(LotteryItemResultType.SUCCESS);
            result.setLotteryAwardItem(item);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error.e:", e);
            result.setLotteryItemResultType(LotteryItemResultType.FAILED);
        }
        return result;
    }

    @Override
    public boolean addLotteryChance(long lotteryId, String profileId, int value, Date date) throws ServiceException {
        Lottery lottery = getLotteryById(lotteryId);
        if (lottery == null) {
            return false;
        }

        AbstractLotteryStrategy lotteryStrategy = LotteryStrategyFactory.get().factory(this, lotteryAwardRedis, lottery.getLotteryRule().getLotteryRuleActionType());
        lotteryStrategy.incrUserLotteryChance(lottery, date, profileId, value);
        return true;
    }
}
