/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.timeline;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.db.social.SocialHandler;
import com.enjoyf.platform.db.timeline.TimeLineHandler;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.GameClientMiyouPostEvent;
import com.enjoyf.platform.service.event.system.UserTimeLineInsertEvent;
import com.enjoyf.platform.service.event.system.UserTimelineInsertBoardEvent;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.timeline.*;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */

/**
 * The TimeLineLogic class holds the core logic for the server.
 * This class is expected to change almost completely from
 * server to server. <p>
 * <p/>
 * TimeLineLogic is called by TimeLinePacketDecoder.
 */
class TimeLineLogic implements TimeLineService {
    //
    private static final Logger logger = LoggerFactory.getLogger(TimeLineLogic.class);
    //
    private TimeLineConfig config;
    //the handler's
    private TimeLineHandler writeAbleTimeLineHandler;
    private HandlerPool<TimeLineHandler> readonlyTimeLineHandlersPool;
    //todo?
    private HandlerPool<SocialHandler> readonlySocialHandlersPool;
    //queue thread pool
    private QueueThreadN timeLineProcessQueueThreadN = null;
    private QueueThreadN eventProcessQueueThreadN = null;

    private TimeLineRedis timeLineRedis = null;
    private TimeLineCache timeLineCache = null;

    TimeLineLogic(TimeLineConfig cfg) {
        config = cfg;
        timeLineRedis = new TimeLineRedis(config.getProps());
        timeLineCache = new TimeLineCache(config.getMemCachedConfig());
        //initialize the handler.
        try {
            //
            writeAbleTimeLineHandler = new TimeLineHandler(config.getWriteableDataSourceName(), config.getProps());

            //
            readonlyTimeLineHandlersPool = new HandlerPool<TimeLineHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyTimeLineHandlersPool.add(new TimeLineHandler(dsn, config.getProps()));
            }

            readonlySocialHandlersPool = new HandlerPool<SocialHandler>();
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());

            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        eventProcessQueueThreadN = new QueueThreadN(config.getTimeLineQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                if (obj instanceof GameClientMiyouPostEvent) {
                    GameClientMiyouPostEvent gcmypEvent = (GameClientMiyouPostEvent) obj;
                    processGCMiyouPostEvent(gcmypEvent);
                } else if (obj instanceof UserTimeLineInsertEvent) {
                    UserTimeLineInsertEvent userTimeLineInsertEvent = (UserTimeLineInsertEvent) obj;
                    processUserTimelineInsertEvent(userTimeLineInsertEvent);
                } else {
                    GAlerter.lab("In eventProcessQueueThreadN, there is a unknown obj.");
                }
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "eventProcessorQueue"));


    }

    private void processGCMiyouPostEvent(GameClientMiyouPostEvent gcmyEvent) {
        SocialTimeLineItem item = new SocialTimeLineItem();
        item.setProfileId(gcmyEvent.getProfileId());
        item.setDomain(SocialTimeLineDomain.MY_MIYOU);
        item.setDirectId(gcmyEvent.getDirectId());
        item.setDirectProfileId(gcmyEvent.getProfileId());
        item.setRemoveStatus(ActStatus.UNACT);
        try {
            writeAbleTimeLineHandler.insertSocialTimeLineItem(item.getDomain(), item.getProfileId(), item);

            timeLineRedis.removeSocialTimeLineItemList(gcmyEvent.getProfileId(), SocialTimeLineDomain.MY_MIYOU);
        } catch (DbException e) {
            GAlerter.lab("TimeLine logic processGCMiyouPostEvent to insert home error.", e);
        }
    }

    //////////////////////////////////////////////
    //the timeline apis
    //////////////////////////////////////////////
    @Override
    public boolean insertTimeLine(String ownUno, TimeLineItem item) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineLogic call writeAbleTimeLineHandler to insertTimeLine, item is " + item);
        }

        boolean returnValue = false;

        try {
            item = writeAbleTimeLineHandler.insertTimeLine(item);

            returnValue = true;
        } catch (DbException e) {
            //
            if (logger.isDebugEnabled()) {
                logger.debug("TimeLineLogic call writeAbleTimeLineHandler to insertTimeLine error, to recover it." + e.getMessage());
            }

            //
            recoverTimeLine(item);
        }

        return returnValue;
    }

    private void recoverTimeLine(TimeLineItem item) {

        try {
            QueryExpress getExpress = new QueryExpress();
            getExpress.add(QueryCriterions.eq(TimeLineItemField.DIRECTID, item.getDirectId()));
            getExpress.add(QueryCriterions.eq(TimeLineItemField.OWNUNO, item.getOwnUno()));
            TimeLineItem timeLineItem = readonlyTimeLineHandlersPool.getHandler().getItem(item.getOwnUno(), item.getDomain(), getExpress);
            if (timeLineItem == null) {
                return;
            }

            QueryExpress updateQueryExpress = new QueryExpress();
            updateQueryExpress.add(QueryCriterions.eq(TimeLineItemField.TLID, timeLineItem.getTlId()));

            //已经被删除状态的
            UpdateExpress updateExpress = new UpdateExpress();
            if (timeLineItem.getRemoveStatus().equals(ActStatus.ACTED)) {
                updateExpress.set(TimeLineItemField.REMOVESTATUS, ActStatus.UNACT.getCode());
                if (!timeLineItem.getSpreadType().hasDef()) {
                    updateExpress.set(TimeLineItemField.SPREADTYPE, ItemSpreadType.DEF);
                }
            } else {
                //没有呗删除状态但是推送类型不是post
                if (!timeLineItem.getSpreadType().hasDef()) {
                    updateExpress.set(TimeLineItemField.SPREADTYPE, timeLineItem.getSpreadType().has(ItemSpreadType.DEF).getValue());
                }
            }

            if (!CollectionUtil.isEmpty(updateExpress.getUpdateAttributes())) {
                writeAbleTimeLineHandler.updateTimeLineItem(timeLineItem.getOwnUno(), timeLineItem.getDomain(), updateExpress, updateQueryExpress);
            }
        } catch (Exception e) {
            //
            GAlerter.lab("TimeLineLogic call handler to recoverTimeLine error.", e);
        }
    }

    @Override
    public List<TimeLineItem> queryTimeLinesBefore(String ownUno, TimeLineDomain domain, Long before, Integer size) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineLogic call readonlyTimeLineHandlersPool to queryTimeLinesBefore, ownUno is " + ownUno + ", domain is " + domain + ", before is " + before + ", size is " + size);
        }

        return readonlyTimeLineHandlersPool.getHandler().queryTimelinesBefore(ownUno, domain, before, size);
    }

    @Override
    public List<TimeLineItem> queryTimeLinesAfter(String ownUno, TimeLineDomain domain, Long after, Integer size) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineLogic call readonlyTimeLineHandlersPool to queryTimeLinesAfter, ownUno is " + ownUno + ", domain is " + domain + ", after is " + after + ", size is " + size);
        }

        return readonlyTimeLineHandlersPool.getHandler().queryTimelinesAfter(ownUno, domain, after, size);
    }

    @Override
    public PageRows<TimeLineItem> queryTimeLines(String ownUno, TimeLineDomain domain, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineLogic call readonlyTimeLineHandlersPool to queryTimeLines, ownUno is " + ownUno + ", domain is " + domain + ", page is " + page);
        }

        PageRows<TimeLineItem> timeLineItemPageRows = readonlyTimeLineHandlersPool.getHandler().queryTimeLines(ownUno, domain, page);

        for (TimeLineItem timeLineItem : timeLineItemPageRows.getRows()) {

            if (timeLineItem.getFavSum() <= 0) {
                continue;
            }

            //select detail
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(TimeLineItemDetailField.TLID, timeLineItem.getTlId()));
            queryExpress.add(QueryCriterions.eq(TimeLineItemDetailField.REMOVESTATUS, ActStatus.UNACT.getCode()));
            queryExpress.add(QueryCriterions.eq(TimeLineItemDetailField.DETAILTYPE, TimeLineDetailType.ITEM_DETAILTYPE_FAV.getCode()));
            queryExpress.add(QuerySort.add(TimeLineItemDetailField.CREATEDATE, QuerySortOrder.DESC));

            Pagination pagination = new Pagination(1, 1, 3);
            PageRows<TimeLineItemDetail> timeLineItemDetails = readonlyTimeLineHandlersPool.getHandler().queryItemDetail(ownUno, queryExpress, pagination);
            timeLineItem.setFavDtail(timeLineItemDetails);
        }

        return timeLineItemPageRows;
    }

    @Override
    public PageRows<TimeLineItem> queryTimeLinesOrg(String ownUno, TimeLineDomain domain, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineLogic call readonlyTimeLineHandlersPool to queryTimeLinesOrg, ownUno is " + ownUno + ", domain is " + domain + ", page is " + page);
        }

        return readonlyTimeLineHandlersPool.getHandler().queryTimeLinesOrg(ownUno, domain, page);
    }

    @Override
    public PageRows<TimeLineItem> queryTimeLinesOnlyFocus(String ownUno, TimeLineDomain domain, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineLogic call readonlyTimeLineHandlersPool to queryTimeLinesOnlyFocus, ownUno is " + ownUno + ", domain is " + domain + ", page is " + page);
        }
        PageRows<TimeLineItem> timeLineItemPageRows = readonlyTimeLineHandlersPool.getHandler().queryTimeLinesOnlyFocus(ownUno, domain, page);

        for (TimeLineItem timeLineItem : timeLineItemPageRows.getRows()) {

            if (timeLineItem.getFavSum() <= 0) {
                continue;
            }

            //select detail
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(TimeLineItemDetailField.TLID, timeLineItem.getTlId()));
            queryExpress.add(QueryCriterions.eq(TimeLineItemDetailField.REMOVESTATUS, ActStatus.UNACT.getCode()));
            queryExpress.add(QueryCriterions.eq(TimeLineItemDetailField.DETAILTYPE, TimeLineDetailType.ITEM_DETAILTYPE_FAV.getCode()));
            queryExpress.add(QuerySort.add(TimeLineItemDetailField.CREATEDATE, QuerySortOrder.DESC));

            Pagination pagination = new Pagination(1, 1, 3);
            PageRows<TimeLineItemDetail> timeLineItemDetails = readonlyTimeLineHandlersPool.getHandler().queryItemDetail(ownUno, queryExpress, pagination);
            timeLineItem.setFavDtail(timeLineItemDetails);
        }
        return timeLineItemPageRows;
    }

    @Override
    public PageRows<TimeLineItem> queryTimeLinesByFilterType(String ownUno, TimeLineDomain domain, TimeLineFilterType timeLineFilterType, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineLogic call readonlyTimeLineHandlersPool to queryTimeLinesByFilterType, ownUno is " + ownUno + ", domain is " + domain + ", page is " + page + ", timeLineFilterType is " + timeLineFilterType.getValue());
        }
        return readonlyTimeLineHandlersPool.getHandler().queryTimeLinesByFilterType(ownUno, domain, timeLineFilterType, page);
    }

    @Override
    public PageRows<TimeLineItem> queryTimeLinesByFilterTypeRelationId(String ownUno, String relationId, TimeLineDomain domain, TimeLineFilterType timeLineFilterType, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineLogic call readonlyTimeLineHandlersPool to queryTimeLinesByFilterTypeRelationId, ownUno is " + ownUno + ", relationId is " + relationId + ", domain is " + domain + ", page is " + page + ", timeLineFilterType is " + timeLineFilterType.getValue());
        }
        return readonlyTimeLineHandlersPool.getHandler().queryTimeLinesByFilterTypeRelationId(ownUno, relationId, domain, timeLineFilterType, page);
    }

    @Override
    public boolean removeTimeLine(String ownUno, TimeLineDomain domain, String directId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineLogic call writeAbleTimeLineHandler to removeTimeLine, ownUno is " + ownUno + ", domain is " + domain + ", directId is " + directId);
        }

        //update the timeline status.
        return writeAbleTimeLineHandler.updateTimeLineStatus(ownUno, domain, directId, ActStatus.ACTED);
    }

    //////////////////////////////////////////////
    //the event server apis
    //////////////////////////////////////////////
    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("The event is recieved, event:" + event);
        }

        eventProcessQueueThreadN.add(event);

        return true;
    }

    ///////////////////////////
    @Override
    public SocialTimeLineItem insertSocialTimeLineItem(SocialTimeLineDomain socialTimeLineDomain, String ownUno, SocialTimeLineItem socialTimeLineItem) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineLogic call writeAbleTimeLineHandler to insertSocialTimeLineItem, socialTimeLineDomain is " + socialTimeLineDomain + ", socialTimeLineItem is " + socialTimeLineItem);
        }
        socialTimeLineItem = writeAbleTimeLineHandler.insertSocialTimeLineItem(socialTimeLineDomain, ownUno, socialTimeLineItem);
        timeLineRedis.putSocialTimeLineItemList(ownUno, socialTimeLineDomain, socialTimeLineItem.toJson());
        return socialTimeLineItem;
    }


    @Override
    public PageRows<SocialTimeLineItem> querySocialTimeLineItemList(SocialTimeLineDomain socialTimeLineDomain, String ownUno, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineLogic call writeAbleTimeLineHandler to insertSocialTimeLineItem, socialTimeLineDomain is " + socialTimeLineDomain + ", ownUno is " + ownUno);
        }
        PageRows<SocialTimeLineItem> pageRows = timeLineRedis.querySocialTimeLineItemList(ownUno, socialTimeLineDomain, page);
        if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(SocialTimeLineItemField.OWN_UNO, ownUno));
            queryExpress.add(QueryCriterions.eq(SocialTimeLineItemField.DOMAIN, socialTimeLineDomain.getCode()));
            queryExpress.add(QueryCriterions.eq(SocialTimeLineItemField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            queryExpress.add(QuerySort.add(SocialTimeLineItemField.SID, QuerySortOrder.DESC));
            pageRows = readonlyTimeLineHandlersPool.getHandler().querySocialTimeLineDomainPageRows(socialTimeLineDomain, ownUno, queryExpress, page);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                for (SocialTimeLineItem item : pageRows.getRows()) {
                    timeLineRedis.putSocialTimeLineItemList(ownUno, socialTimeLineDomain, item.toJson());
                }
            }
        }

        //
        if (!CollectionUtil.isEmpty(pageRows.getRows())) {
            Collections.sort(pageRows.getRows(), new Comparator<SocialTimeLineItem>() {
                @Override
                public int compare(SocialTimeLineItem o1, SocialTimeLineItem o2) {
                    int jt1 = o1.getSid().intValue();
                    int jt2 = o2.getSid().intValue();
                    return jt2 > jt1 ? 1 : (o1 == o2 ? 0 : -1);
                }
            });
        }

        return pageRows;
    }

    @Override
    public SocialTimeLineItem getSocialTimeLineItem(String ownUno, SocialTimeLineDomain socialTimeLineDomain, Long sid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineLogic getSocialTimeLineItem:ownUno:" + ownUno + ",domain:" + socialTimeLineDomain.getCode() + ",sid:" + sid);
        }
        SocialTimeLineItem socialTimeLineItem = timeLineCache.getSocialTimeLineItem(String.valueOf(sid));
        if (socialTimeLineItem == null) {
            socialTimeLineItem = readonlyTimeLineHandlersPool.getHandler().getSocialTimeLineDomainBySId(socialTimeLineDomain, ownUno, sid);
            if (socialTimeLineItem != null) {
                timeLineCache.putSocialTimeLineItem(socialTimeLineItem);
            }
        }
        return socialTimeLineItem;
    }

    @Override
    public NextPageRows<SocialTimeLineItem> querySocialTimeLineItemNextList(SocialTimeLineDomain domain, String uno, NextPagination nextPagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineLogic call writeAbleTimeLineHandler to insertSocialTimeLineItem, socialTimeLineDomain is " + domain + ", ownUno is " + uno);
        }
        return readonlyTimeLineHandlersPool.getHandler().querySocialTimeLineItemNextList(domain, uno, nextPagination);
    }

    @Override
    public boolean modifySocialTimeLineItem(SocialTimeLineDomain socialTimeLineDomain, String ownUno, UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("modifySocialTimeLineItem call writeAbleTimeLineHandler to modifySocialTimeLineItem, SocialTimeLineDomain is " + socialTimeLineDomain + ", ownUno is " + ownUno);
        }


        if (socialTimeLineDomain.equals(SocialTimeLineDomain.MY_MIYOU)) {
            timeLineRedis.removeSocialTimeLineItemList(ownUno, SocialTimeLineDomain.MY_MIYOU);
        }
        return writeAbleTimeLineHandler.updateSocialTimeLineDomain(socialTimeLineDomain, ownUno, updateExpress, queryExpress);
    }

    @Override
    public PageRows<String> queryUGCWikiUserDynamicCache(SocialTimeLineDomain socialTimeLineDomain, SocialTimeLineItemType socialTimeLineItemType, String profileId, Pagination page) {
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineLogic queryUGCWikiUserDynamicCache:domain:" + socialTimeLineDomain.getCode() + ",type:" + socialTimeLineItemType.getCode() + ",profile:" + profileId);
        }
        return timeLineRedis.queryUGCWikiUserDynamicCache(socialTimeLineDomain, socialTimeLineItemType, profileId, page);
    }

    @Override
    public boolean removeAllUGCWikiUserDynamicCache(SocialTimeLineDomain socialTimeLineDomain, SocialTimeLineItemType socialTimeLineItemType, String profileId, SocialTimeLineItem socialTimeLineItem) {
        if (logger.isDebugEnabled()) {
            logger.debug("TimeLineLogic removeAllUGCWikiUserDynamicCache:domain:" + socialTimeLineDomain.getCode() + ",type:" + socialTimeLineItemType.getCode() + ",profile:" + profileId);
        }
        return timeLineRedis.removeAllUGCWikiUserDynamicCache(socialTimeLineDomain, socialTimeLineItemType, profileId);
    }

    @Override
    public UserTimeline buildUserTimeline(UserTimeline timeline) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleUserTimelineHandler to save timeline, timeline is " + timeline);
        }

        UserTimeline returnValue = writeAbleTimeLineHandler.buildUserTimeline(timeline);
        //保存我的动态，我的分类动态
        timeLineRedis.putUserTimeLineByProfileId(returnValue);
        if (returnValue != null) {
            sendOutBuildUserTimelineEvent(returnValue);
        } else {
            logger.error("UserTimeline Logic buildTimeline error.");
        }

        return returnValue;
    }

    private void sendOutBuildUserTimelineEvent(UserTimeline userTimeline) {
        UserTimelineInsertBoardEvent event = new UserTimelineInsertBoardEvent();
        event.setProfileid(userTimeline.getProfileId());
        event.setTimelineId(userTimeline.getItemId());
        event.setType(userTimeline.getType());
        event.setMsgBody(userTimeline.getExtendBody());
        event.setCreateDate(userTimeline.getCreateTime());
        try {
            EventDispatchServiceSngl.get().dispatch(event);
        } catch (Exception e) {
            GAlerter.lab("ContentLogic sendSocialMessEvent error.", e);
        }
    }

    public void processUserTimelineInsertEvent(UserTimeLineInsertEvent userTimeLineInsertEvent) {
        try {
            UserTimeline userTimeline = writeAbleTimeLineHandler.getUserTimeLineById(userTimeLineInsertEvent.getItemId());

            UserTimeline friendUserTimeline = new UserTimeline();
            friendUserTimeline.setActionType(userTimeline.getActionType());
            friendUserTimeline.setDestId(userTimeline.getItemId());
            friendUserTimeline.setDestProfileid(userTimeLineInsertEvent.getDestProfileId());
            friendUserTimeline.setExtendBody(userTimeline.getExtendBody());
            friendUserTimeline.setProfileId(userTimeLineInsertEvent.getProfileId());
            friendUserTimeline.setDomain(UserTimelineDomain.FRIEND.getCode());
            friendUserTimeline.setCreateTime(new Timestamp(userTimeLineInsertEvent.getCreateDate().getTime()));
            friendUserTimeline.setLinekey(timeLineRedis.getUserTimeLineKey(userTimeLineInsertEvent.getProfileId(),userTimeline.getType(),UserTimelineDomain.FRIEND.getCode()));
            friendUserTimeline.setType(userTimeline.getType());
            friendUserTimeline = writeAbleTimeLineHandler.buildUserTimeline(friendUserTimeline);

            timeLineRedis.putUserTimeLineByProfileId(friendUserTimeline);
        } catch (DbException e) {
            GAlerter.lab(this.getClass().getName() + " processUserTimelineInsertEvent", e);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " processUserTimelineInsertEvent", e);
        }

    }

    public void deleteTimelineEvent(List<String> fansList, String profileid, Long itemid) throws ServiceException {
        UserTimeline userTimeline = writeAbleTimeLineHandler.getUserTimeLineById(itemid);
        //先删除所有粉丝的好友动态
        if (null != fansList && !fansList.isEmpty()) {
            for (String fanProfileid : fansList) {
                //redis 删除粉丝的好友动态
                timeLineRedis.removeUserTimelineByProfileId(String.valueOf(itemid));
                //mysql 删除好友动态
                writeAbleTimeLineHandler.delUserTimeline(fanProfileid, itemid);
            }
        }
        //从redis删除我的动态和我分类动态
        timeLineRedis.removeUserTimelineByProfileId(String.valueOf(itemid));
        //从mysql中删除我的动态
        writeAbleTimeLineHandler.delUserTimelineById(itemid);
    }

    @Override
    public PageRows<UserTimeline> queryUserTimeline(String profileid, String domain, String type, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the queryUserTimeline to querySrcTimelinesByUno, srcUno is "
                    + profileid);
        }


//        return timeLineRedis.queryUserTimeLineByProfileId(profileid,type,domain,page);

        return writeAbleTimeLineHandler.queryUserTimeline(profileid, domain, type, page);
    }


    @Override
    public UserTimeline getUserTimeline(String profileid, String type, String domain, String actionType) throws ServiceException {

        return writeAbleTimeLineHandler.getUserTimeline(profileid, type, domain, actionType);
    }

    @Override
    public boolean delUserTimeline(UserTimeline userTimeline) throws ServiceException {

        // TODO: 2016/11/29 删除关注我的人-好友动态
        //sendOutDelUserTimelineEvent(userTimeline);
        return true;
    }

}
