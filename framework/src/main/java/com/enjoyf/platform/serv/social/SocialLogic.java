/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.social;


import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.db.social.AppSocialHandler;
import com.enjoyf.platform.db.social.SocialHandler;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.*;
import com.enjoyf.platform.service.event.system.wiki.WikiNoticeEvent;
import com.enjoyf.platform.service.gameres.gamedb.GameDBField;
import com.enjoyf.platform.service.notice.NoticeType;
import com.enjoyf.platform.service.notice.wiki.WikiNoticeBody;
import com.enjoyf.platform.service.notice.wiki.WikiNoticeDestType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.social.*;
import com.enjoyf.platform.service.usercenter.ModifyTimeJson;
import com.enjoyf.platform.service.usercenter.ProfileSumField;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */

/**
 * The UserPropsLogic class holds the core logic for the server.
 * This class is expected to change almost completely from
 * server to server. <p>
 * <p/>
 * UserPropsLogic is called by UserPropsPacketDecoder.
 */
class SocialLogic implements SocialService {
    //
    private static final Logger logger = LoggerFactory.getLogger(SocialLogic.class);

    //
    private SocialConfig config;

    //the handler's
    private SocialHandler writeAbleSocialHandler;
    private HandlerPool<SocialHandler> readonlySocialHandlersPool;

    private AppSocialHandler writeAppSocialHandler;
    private HandlerPool<AppSocialHandler> readonlyAppSocialHandlersPool;

    //
    private QueueThreadN eventProcessQueueThreadN = null;

    private SocialBlackCache socialBlackCache;
    private SocialRedis socialRedis;


    SocialLogic(SocialConfig cfg) {
        config = cfg;

        //initialize the handler.
        try {
            //
            writeAbleSocialHandler = new SocialHandler(config.getWriteableDataSourceName(), config.getProps());

            //
            readonlySocialHandlersPool = new HandlerPool<SocialHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlySocialHandlersPool.add(new SocialHandler(dsn, config.getProps()));
            }


            writeAppSocialHandler = new AppSocialHandler(config.getWriteableDataSourceName(), config.getProps());

            readonlyAppSocialHandlersPool = new HandlerPool<AppSocialHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyAppSocialHandlersPool.add(new AppSocialHandler(dsn, config.getProps()));
            }


        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());

            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        //initialize the cache setting.
        socialRedis = new SocialRedis(config.getProps());

        socialBlackCache = new SocialBlackCache(config.getMemCachedConfig());

        //the thread pool.
        eventProcessQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                if (obj instanceof Event) {
                    processQueuedEvent((Event) obj);
                } else if (obj instanceof UserTimelineInsertBoardEvent) {
                    processUserTimelineEvent((UserTimelineInsertBoardEvent) obj);
                } else {
                    GAlerter.lab("In eventProcessQueueThreadN, there is a unknown obj.");
                }
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "eventProcessQueue"));
    }

    private void processUserTimelineEvent(UserTimelineInsertBoardEvent userTimelineInsertBoardEvent) {
        try {
            Set<String> fansList = socialRedis.getAllUserRelationFansByProfileId(userTimelineInsertBoardEvent.getProfileid(), ObjectRelationType.WIKI_PROFILE);
            //缓存丢失我的粉丝处理
//            if (CollectionUtil.isEmpty(fansList)) {
//                QueryExpress queryExpress = new QueryExpress()
//                        .add(QueryCriterions.eq(UserRelationField.DEST_PROFILEID, userTimelineInsertBoardEvent.getProfileid()));
//
//                fansList = new HashSet<String>();
//                List<UserRelation> userRelationList = readonlyAppSocialHandlersPool.getHandler().queryUserRelationList(queryExpress);
//                for (UserRelation userRelation : userRelationList) {
//                    fansList.add(userRelation.getSrcProfileid());
//                    socialRedis.putUserRelationFansProfileId(userRelation.getDestProfileid(), ObjectRelationType.WIKI_PROFILE, userRelation.getModifyTime().getTime(), userRelation.getSrcProfileid());
//                }
//            }

            for (String fansPid : fansList) {
                try {
                    UserTimeLineInsertEvent userTimeLineInsertEvent = new UserTimeLineInsertEvent();
                    userTimeLineInsertEvent.setProfileId(fansPid);
                    userTimeLineInsertEvent.setDestProfileId(userTimelineInsertBoardEvent.getProfileid());
                    userTimeLineInsertEvent.setMsgBody(userTimelineInsertBoardEvent.getMsgBody());
                    userTimeLineInsertEvent.setItemId(userTimelineInsertBoardEvent.getTimelineId());
                    userTimeLineInsertEvent.setCreateDate(userTimelineInsertBoardEvent.getCreateDate());
                    EventDispatchServiceSngl.get().dispatch(userTimeLineInsertEvent);
                } catch (ServiceException e) {
                    GAlerter.lab(this.getClass().getName() + " processUserTimelineEvent occured error.e: ", e);
                }
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error:", e);
        }

    }

    private void processQueuedEvent(Event event) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to processQueuedEvent:" + event);
        }

        //todo wiki add broad to time status
        if (event instanceof UserTimelineInsertBoardEvent) {
            UserTimelineInsertBoardEvent userTimelineInsertBoardEvent = (UserTimelineInsertBoardEvent) event;
            processUserTimelineEvent(userTimelineInsertBoardEvent);
        } else {
            logger.info("SocialLogic discard the unknown event:" + event);
        }
    }

    @Override
    public boolean reportInviteImportInfos(List<InviteImportInfo> inviteImportInfos) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the reportInviteImportInfos to checkRelationsByDestUnos,inviteInfos is " + inviteImportInfos.size());
        }

        for (InviteImportInfo importInfo : inviteImportInfos) {
            try {
                writeAbleSocialHandler.insertInviteImportInfos(importInfo);
            } catch (DbException e) {
                GAlerter.lan(" insert invite info occured dbService.e", e);
            }
        }

        return true;
    }

    public boolean updateSocialRecommend(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {

        return writeAbleSocialHandler.updateSocialRecommend(updateExpress, queryExpress);
    }

    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("The event is recieved, event:" + event);
        }

        eventProcessQueueThreadN.add(event);

        return true;
    }

    public List<SocialRelation> querySocialRelation(String srcUno, RelationType relationType, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("querySocialRelation, srcUno:" + srcUno + " relationType:" + relationType);
        }

        return readonlySocialHandlersPool.getHandler().querySocialRealtion(srcUno, relationType, queryExpress);
    }

    SocialRecommend createSocialRecommend(SocialRecommend socialRecommend) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("createSocialRecommend, socialRecommend:" + socialRecommend);
        }

        return writeAbleSocialHandler.insertSocialRecommend(socialRecommend);
    }

    SocialRecommend getSocialRecommendFromDB(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getSocialRecommendFromDB, queryExpress:" + queryExpress);
        }

        return writeAbleSocialHandler.getSocialRecommend(queryExpress);
    }

    public SocialBlack createSocialBlack(SocialBlack socialBlack) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("createSocialBlack, queryExpress:" + socialBlack);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(SocialBlackField.DESUNO, socialBlack.getDesUno()));
        queryExpress.add(QueryCriterions.eq(SocialBlackField.SRCUNO, socialBlack.getSrcUno()));
        SocialBlack sc = readonlySocialHandlersPool.getHandler().getSocialBlack(socialBlack.getSrcUno(), queryExpress);
        if (sc == null) {
            socialBlack = writeAbleSocialHandler.inserSocialBlack(socialBlack);

            //cache
            socialBlackCache.putSocialBlack(socialBlack.getSrcUno(), socialBlack);
        } else {
            if (!sc.getRemoveStatus().getCode().equals(ActStatus.UNACT.getCode())) {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(SocialBlackField.REMOVE_STATUS, ActStatus.UNACT.getCode());
                updateExpress.set(SocialBlackField.UPDATE_TIME, new Date());
                writeAbleSocialHandler.updateSocialBlack(socialBlack.getSrcUno(), updateExpress, queryExpress);
                sc.setRemoveStatus(ActStatus.UNACT);
                socialBlack = sc;

                //cache
                socialBlackCache.putSocialBlack(socialBlack.getSrcUno(), socialBlack);
            }
        }
        return socialBlack;
    }

    @Override
    public ObjectRelation saveObjectRelation(ObjectRelation relation) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler ProfilePicRelation:,relation " + relation);
        }
        boolean bool = true;
        try {
            String relationId = SocialUtil.getSocialRelationId(relation.getProfileId(), relation.getObjectId(), relation.getProfileKey(), relation.getType());
            QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(ObjectRelationField.PROFILERELATION_ID, relationId));
            ObjectRelation profilePicRelation = writeAppSocialHandler.getObjectRelation(queryExpress);
            if (profilePicRelation != null && profilePicRelation.getStatus().equals(IntValidStatus.VALID)) {
                bool = false;
                return null;
            }
            if (profilePicRelation == null) {
                relation.setRelationId(relationId);
                relation = writeAppSocialHandler.insertObjectRelation(relation);
            } else if (!profilePicRelation.getStatus().equals(IntValidStatus.VALID)) {
                bool = writeAppSocialHandler.updateObjectRelation(new UpdateExpress().set(ObjectRelationField.STATUS, IntValidStatus.VALID.getCode()), queryExpress);
            }

            if (relation.getType().equals(ObjectRelationType.GAME)) {
                sendOutCreateGameRelation(relation.getObjectId());
            }
            if (bool) {
                socialRedis.lpushObjectRelationListCache(relation);
            }
            return relation;
        } finally {
        }
    }


    private void sendOutCreateGameRelation(String objectId) {
        try {
            GameDBSumIncreaseEvent sumIncreaseEvent = new GameDBSumIncreaseEvent();
            sumIncreaseEvent.setGameId(Long.parseLong(objectId));
            sumIncreaseEvent.setGameDBField(GameDBField.FAVOR_SUM);
            sumIncreaseEvent.setIncreateValue(1);

            EventDispatchServiceSngl.get().dispatch(sumIncreaseEvent);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occred error e:" + e + " objectId:" + objectId);
        }
    }

    @Override
    public List<ObjectRelation> queryObjectRelations(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readhandler queryObjectRelations:,QueryExpress " + queryExpress);
        }
        return readonlyAppSocialHandlersPool.getHandler().queryObjectRelation(queryExpress);
    }

    @Override
    public ObjectRelation getObjectRelation(String relationId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readhandler getObjectRelation:,relationId " + relationId);
        }
        return readonlyAppSocialHandlersPool.getHandler().getObjectRelation(new QueryExpress().add(QueryCriterions.eq(ObjectRelationField.PROFILERELATION_ID, relationId)));
    }

    @Override
    public boolean removeObjectRelation(String profileId, String objectId, ObjectRelationType type, String profileKey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler saveObjectRelation:,profileId " + profileId + " objectId:" + objectId);
        }
        boolean bool = true;
        try {
            UpdateExpress updateExpress = new UpdateExpress()
                    .set(ObjectRelationField.STATUS, IntValidStatus.UNVALID.getCode());

            bool = writeAppSocialHandler.updateObjectRelation(updateExpress, new QueryExpress()
                    .add(QueryCriterions.eq(ObjectRelationField.PROFILEID, profileId))
                    .add(QueryCriterions.eq(ObjectRelationField.OBJECTID, objectId))
                    .add(QueryCriterions.eq(ObjectRelationField.OBJECTTYPE, type.getCode()))
                    .add(QueryCriterions.eq(ObjectRelationField.PROFILEKEY, profileKey))
                    .add(QueryCriterions.eq(ObjectRelationField.STATUS, IntValidStatus.VALID.getCode())));

            if (bool && type.equals(ObjectRelationType.GAME)) {
                //TODO
                socialRedis.removeRelationListCache(profileId, type);

                sendOutRemoveGameRelation(objectId);
            }

            return bool;
        } finally {
        }
    }

    private void sendOutRemoveGameRelation(String objectId) {
        try {
            GameDBSumIncreaseEvent sumIncreaseEvent = new GameDBSumIncreaseEvent();
            sumIncreaseEvent.setGameId(Long.parseLong(objectId));
            sumIncreaseEvent.setGameDBField(GameDBField.FAVOR_SUM);
            sumIncreaseEvent.setIncreateValue(-1);
            EventDispatchServiceSngl.get().dispatch(sumIncreaseEvent);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occred e:" + e + " objectId:" + objectId);
        }
    }

    @Override
    public ProfileRelation saveProfileRelation(ProfileRelation relation) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler saveProfileRelation:,relation " + relation);
        }
        relation = writeAppSocialHandler.createProfileRelation(relation);

        if (relation == null) {
            return null;
        }
        socialRedis.putFocusProfileId(relation.getSrcProfileId(), relation.getType(), relation.getDestProfileId());
        socialRedis.putFansProfileId(relation.getDestProfileId(), relation.getType(), relation.getSrcProfileId());
        if (!relation.getType().equals(ObjectRelationType.WIKI_PROFILE)) {
            sendOutCreateProfileRelation(relation);
        }

        return relation;
    }

    private void sendOutCreateProfileRelation(ProfileRelation relation) {
        UserCenterSumIncreaseEvent ucsiEvent = new UserCenterSumIncreaseEvent();
        ucsiEvent.setProfileId(relation.getSrcProfileId());
        ucsiEvent.setLikeSum(1);

        UserCenterSumIncreaseEvent ucsiedEvent = new UserCenterSumIncreaseEvent();
        ucsiedEvent.setProfileId(relation.getDestProfileId());
        ucsiedEvent.setLikedSum(1);
        ModifyTimeJson json = new ModifyTimeJson();
        json.setLikedprofileModifyTime(new Date().getTime());
        ucsiedEvent.setModifyTimeJson(json);
        try {
            EventDispatchServiceSngl.get().dispatch(ucsiEvent);
            EventDispatchServiceSngl.get().dispatch(ucsiedEvent);
        } catch (Exception e) {
            GAlerter.lab(this.getClass() + "occured Exception.e: ", e);
        }
    }


    @Override
    public boolean removeProfileRelation(String srcProfileId, String destProfileId, ObjectRelationType type, String profileKey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler removeProfileRelation:,srcProfileId " + srcProfileId + " destProfileId:" + destProfileId);
        }

        boolean result = writeAppSocialHandler.breakRelation(srcProfileId, destProfileId, type, profileKey);

        if (result) {
            socialRedis.removeFocusProfileId(srcProfileId, type, destProfileId);
            socialRedis.removeFansProfileId(destProfileId, type, srcProfileId);
            if (!type.equals(ObjectRelationType.WIKI_PROFILE)) {
                sendOutBreakProfileRelation(srcProfileId, destProfileId);
            }
        }

        return result;
    }

    private void sendOutBreakProfileRelation(String srcProfileId, String destProfileId) {
        UserCenterSumIncreaseEvent ucsiEvent = new UserCenterSumIncreaseEvent();
        ucsiEvent.setProfileId(srcProfileId);
        ucsiEvent.setLikeSum(-1);

        UserCenterSumIncreaseEvent ucsiedEvent = new UserCenterSumIncreaseEvent();
        ucsiedEvent.setProfileId(destProfileId);
        ucsiedEvent.setLikedSum(-1);

        try {
            EventDispatchServiceSngl.get().dispatch(ucsiEvent);
            EventDispatchServiceSngl.get().dispatch(ucsiedEvent);
        } catch (Exception e) {
            GAlerter.lab(this.getClass() + "occured Exception.e: ", e);
        }
    }


    @Override
    public PageRows<ProfileRelation> querySrcRelation(String srcProfileId, ObjectRelationType relationType, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler querySrcRelation:,srcProfileId " + srcProfileId + " pagination:" + pagination);
        }
        return readonlyAppSocialHandlersPool.getHandler().queryProfileRelation(new QueryExpress()
                .add(QueryCriterions.eq(ProfileRelationField.SRC_PROFILEID, srcProfileId))
                .add(QueryCriterions.eq(ProfileRelationField.RELATIONTYPE, relationType.getCode()))
                .add(QueryCriterions.eq(ProfileRelationField.SRC_STATUS, IntValidStatus.VALID.getCode()))
                .add(QuerySort.add(ProfileRelationField.MODIFYTIME, QuerySortOrder.DESC)), pagination);
    }

    @Override
    public PageRows<ProfileRelation> queryDestRelation(String destProfileId, ObjectRelationType relationType, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler queryDestRelation:,destProfileId " + destProfileId + " pagination:" + pagination);
        }

        return readonlyAppSocialHandlersPool.getHandler().queryProfileRelation(new QueryExpress()
                .add(QueryCriterions.eq(ProfileRelationField.DEST_PROFILEID, destProfileId))
                .add(QueryCriterions.eq(ProfileRelationField.RELATIONTYPE, relationType.getCode()))
                .add(QueryCriterions.eq(ProfileRelationField.SRC_STATUS, IntValidStatus.VALID.getCode()))
                .add(QuerySort.add(ProfileRelationField.MODIFYTIME, QuerySortOrder.DESC)), pagination);
    }

    @Override
    public PageRows<ObjectRelation> queryObjectRelation(String profileId, ObjectRelationType type, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler queryDestRelation:,profileId " + profileId + " type:" + type + " pagination:" + pagination);
        }
        PageRows<ObjectRelation> pageRows = socialRedis.queryObjectRelationListCache(type, profileId, pagination);
        if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
            pageRows = readonlyAppSocialHandlersPool.getHandler().queryObjectRelation(new QueryExpress()
                    .add(QueryCriterions.eq(ObjectRelationField.PROFILEID, profileId))
                    .add(QueryCriterions.eq(ObjectRelationField.OBJECTTYPE, type.getCode()))
                    .add(QueryCriterions.eq(ObjectRelationField.STATUS, IntValidStatus.VALID.getCode()))
                    .add(QuerySort.add(ProfileRelationField.MODIFYTIME, QuerySortOrder.DESC)), pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                for (ObjectRelation relation : pageRows.getRows()) {
                    socialRedis.rpushObjectRelationListCache(relation);
                }
            }
        }
        return pageRows;
    }

    @Override
    public Map<String, ObjectRelation> checkObjectRelation(String profileId, ObjectRelationType type, Set<String> destId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler queryDestRelation:,profileId " + profileId + " type:" + type + " destId:" + destId.size());
        }

        //todo add cache
        List<ObjectRelation> relationList = readonlyAppSocialHandlersPool.getHandler().queryObjectRelation(new QueryExpress().add(QueryCriterions
                .eq(ObjectRelationField.PROFILEID, profileId))
                .add(QueryCriterions.eq(ObjectRelationField.OBJECTTYPE, type.getCode()))
                .add(QueryCriterions.in(ObjectRelationField.OBJECTID, destId.toArray())));

        Map<String, ObjectRelation> map = new HashMap<String, ObjectRelation>();
        for (ObjectRelation relation : relationList) {
            map.put(relation.getObjectId(), relation);
        }
        return map;
    }

    @Override
    public List<String> queryObjectRelationObjectIdList(String profileId, ObjectRelationType relationType, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("SocialLogic queryWikiAppSubscribe.profileId:" + profileId + ",relationType:" + relationType.getCode());
        }
        List<String> objIdList = new ArrayList<String>();
        Set<String> keySet = socialRedis.queryObjectIdList(relationType.getCode(), profileId, pagination);
        if (CollectionUtil.isEmpty(keySet)) {
            PageRows<ObjectRelation> pageRows = queryObjectRelation(profileId, relationType, pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                for (ObjectRelation relation : pageRows.getRows()) {
                    objIdList.add(relation.getObjectId());
                    socialRedis.putObjectId(relationType.getCode(), profileId, relation.getModifyTime().getTime(), relation.getObjectId(), 24 * 60 * 60);
                }
            }
        } else {
            objIdList.addAll(keySet);
        }
        return objIdList;
    }

    @Override
    public PageRows<String> queryFocusProfileId(String profileId, ObjectRelationType relationType, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("SocialLogic queryWikiAppSubscribe.profileId:" + profileId + ",relationType:" + relationType.getCode());
        }
        PageRows<String> profileIds = socialRedis.queryFocusProfileId(profileId, relationType, page);
        if (profileIds == null) {
            profileIds = new PageRows<String>();
        }
        if (CollectionUtil.isEmpty(profileIds.getRows())) {
            List<String> profileIdList = new ArrayList<String>();
            PageRows<ProfileRelation> profileRelationPageRows = querySrcRelation(profileId, relationType, page);
            if (profileRelationPageRows != null && !CollectionUtil.isEmpty(profileRelationPageRows.getRows())) {
                for (ProfileRelation profileRelation : profileRelationPageRows.getRows()) {
                    profileIdList.add(profileRelation.getDestProfileId());
                    socialRedis.putFocusProfileId(profileId, relationType, profileRelation.getDestProfileId());
                }
            }
            profileIds.setRows(profileIdList);
            profileIds.setPage(profileRelationPageRows == null ? page : profileRelationPageRows.getPage());
        }
        return profileIds;
    }

    @Override
    public UserRelation buildUserRelation(UserRelation relation) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleSocialHandler to save relation, relation is " + relation);
        }

        UserRelation returnValue = writeAppSocialHandler.buildUserRelation(relation);

        if (returnValue == null) {
            return null;
        }
        socialRedis.putUserRelationFocusProfileId(relation.getSrcProfileid(), relation.getRelationType(), relation.getModifyTime().getTime(), relation.getDestProfileid());
        socialRedis.putUserRelationFansProfileId(relation.getDestProfileid(), relation.getRelationType(), relation.getModifyTime().getTime(), relation.getSrcProfileid());

        if (relation.getRelationType().equals(ObjectRelationType.WIKI_PROFILE)) {
            String relationId = SocialUtil.getSocialRelationId(relation.getSrcProfileid(), relation.getDestProfileid(), UserRelationRedis.USER_RELATION_PROFILE_KEY, relation.getRelationType());
            socialRedis.removeUserRelationByProfileId(relationId);
            sendOutBuildUserRelationEvent(relation.getSrcProfileid(), relation.getDestProfileid(), relation.getModifyTime());
        }
        return returnValue;
    }

    private void sendOutBuildUserRelationEvent(String srcPid, String destPid, Date date) {
        //todo add increase sum event
        ProfileSumIncreaseEvent psiEvent = new ProfileSumIncreaseEvent();
        psiEvent.setCount(1);
        psiEvent.setField(ProfileSumField.FOLLOWSUM);
        psiEvent.setProfileId(srcPid);

        ProfileSumIncreaseEvent psiFansEvent = new ProfileSumIncreaseEvent();
        psiFansEvent.setCount(1);
        psiFansEvent.setField(ProfileSumField.FANSSUM);
        psiFansEvent.setProfileId(destPid);

        WikiNoticeEvent wikiNoticeEvent = new WikiNoticeEvent();
        WikiNoticeBody body = new WikiNoticeBody();
        body.setDestProfileId(srcPid);
        wikiNoticeEvent.setBody(body);
        wikiNoticeEvent.setProfileId(destPid);
        wikiNoticeEvent.setType(NoticeType.FOLLOW);
        wikiNoticeEvent.setCreateTime(date);
        try {
//            EventDispatchServiceSngl.get().dispatch(psiEvent);
//            EventDispatchServiceSngl.get().dispatch(psiFansEvent);
            this.incrementProfileCount(psiEvent);
            this.incrementProfileCount(psiFansEvent);
            EventDispatchServiceSngl.get().dispatch(wikiNoticeEvent);
        } catch (Exception e) {
            GAlerter.lab("sendOutBuildUserRelationEvent error.", e);
        }
    }

    /**
     * todo 微服务新增
     * @param profileSumIncreaseEvent
     */
    private void incrementProfileCount(ProfileSumIncreaseEvent profileSumIncreaseEvent){
        try {
            UserCenterServiceSngl.get().increaseProfileSum(profileSumIncreaseEvent.getProfileId(),
                    profileSumIncreaseEvent.getField(),profileSumIncreaseEvent.getCount());
        } catch (ServiceException e) {
            logger.info("incrementProfileCount:{} is error:{}",profileSumIncreaseEvent.toString(), e.getMessage());
        }
    }

    @Override
    public boolean removeUserRelation(String srcProfileId, String destProfileId, ObjectRelationType type, String modifyIp) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler removeProfileRelation:,srcProfileId " + srcProfileId + " destProfileId:" + destProfileId);
        }

        boolean result = writeAppSocialHandler.breakUserRelation(srcProfileId, destProfileId, type);

        if (result) {
            //删缓存src-dest
            String relationId = SocialUtil.getSocialRelationId(srcProfileId, destProfileId, UserRelationRedis.USER_RELATION_PROFILE_KEY, type);
            socialRedis.removeUserRelationByProfileId(relationId);

            //删缓存dest-src
            String destRelationId = SocialUtil.getSocialRelationId(destProfileId, srcProfileId, UserRelationRedis.USER_RELATION_PROFILE_KEY, type);
            socialRedis.removeUserRelationByProfileId(destRelationId);

            socialRedis.removeUserRelationFocusProfileId(srcProfileId, type, destProfileId);
            socialRedis.removeUserRelationFansProfileId(destProfileId, type, srcProfileId);

            if (type.equals(ObjectRelationType.WIKI_PROFILE)) {
                sendOutRemoveUserRelationEvent(srcProfileId, destProfileId);
            }
        }
        return result;
    }

    private void sendOutRemoveUserRelationEvent(String srcPid, String destPid) {
        ProfileSumIncreaseEvent psiEvent = new ProfileSumIncreaseEvent();
        psiEvent.setCount(-1);
        psiEvent.setField(ProfileSumField.FOLLOWSUM);
        psiEvent.setProfileId(srcPid);

        ProfileSumIncreaseEvent psiFansEvent = new ProfileSumIncreaseEvent();
        psiFansEvent.setCount(-1);
        psiFansEvent.setField(ProfileSumField.FANSSUM);
        psiFansEvent.setProfileId(destPid);

        WikiNoticeEvent wikiNoticeEvent = new WikiNoticeEvent();
        WikiNoticeBody body = new WikiNoticeBody();
        body.setDestProfileId(srcPid);
        body.setWikiNoticeDestType(WikiNoticeDestType.CANCEL_FOLLOW);
        wikiNoticeEvent.setBody(body);
        wikiNoticeEvent.setProfileId(destPid);
        wikiNoticeEvent.setType(NoticeType.FOLLOW);
        wikiNoticeEvent.setCreateTime(new Date());
        try {
            EventDispatchServiceSngl.get().dispatch(wikiNoticeEvent);
//            EventDispatchServiceSngl.get().dispatch(psiEvent);
//            EventDispatchServiceSngl.get().dispatch(psiFansEvent);
            //todo 微服务改动直接调用API
            this.incrementProfileCount(psiEvent);
            this.incrementProfileCount(psiFansEvent);
        } catch (Exception e) {
            GAlerter.lab("sendOutBuildUserRelationEvent error.", e);
        }

    }

    @Override
    public PageRows<UserRelation> queryFans(String profileid, ObjectRelationType relationType, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyUserRelationHandlersPool to querySrcRelationsByUno, srcUno is "
                    + profileid + ", relationType is" + relationType);
        }
        //todo cache??

        return readonlyAppSocialHandlersPool.getHandler().queryUserRelationByDestProfileId(profileid, relationType, page);
    }

    @Override
    public PageRows<UserRelation> queryFollowUser(String profileid, ObjectRelationType relationType, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyUserRelationHandlersPool to queryDestRelationsByUno, srcUno is "
                    + profileid + ", relationType is" + relationType);
        }

        return readonlyAppSocialHandlersPool.getHandler().queryUserRelationBySrcProfileId(profileid, relationType, page);
    }

    @Override
    public UserRelation getRelation(String srcUno, String destUno, ObjectRelationType type) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyUserRelationHandlersPool to save relation, srcUno is "
                    + srcUno + ", destUno is" + destUno + ", type is " + type);
        }
        String relationId = SocialUtil.getSocialRelationId(srcUno, destUno, UserRelationRedis.USER_RELATION_PROFILE_KEY, type);
        UserRelation userRelation = null;
        String userRelationString = socialRedis.getUserRelationByProfileId(relationId);
        if (StringUtil.isEmpty(userRelationString)) {
            userRelation = readonlyAppSocialHandlersPool.getHandler().getUserRelation(srcUno, destUno, type);
            if (userRelation != null) {
                socialRedis.putUserRelationByProfileId(relationId, userRelation.toJson());
            }
        } else {
            userRelation = UserRelation.getByJson(userRelationString);
        }
        return userRelation;
    }

    @Override
    public PageRows<String> queryFansProfileid(String profileid, ObjectRelationType relationType, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("SocialLogic queryFansProfileId.profileId:" + profileid + ",relationType:" + relationType.getCode());
        }
        Set<String> profileIds = socialRedis.queryUserRelationFansProfileId(profileid, relationType, page);
        if (profileIds == null) {
            profileIds = new HashSet<String>();
        }

        PageRows<String> rows = new PageRows<String>();
        rows.setPage(page);
        rows.setRows(new ArrayList<String>(profileIds));
        return rows;
    }

    @Override
    public PageRows<String> queryFollowProfileid(String profileid, ObjectRelationType relationType, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("SocialLogic queryWikiAppSubscribe.profileId:" + profileid + ",relationType:" + relationType.getCode());
        }
        Set<String> profileIds = socialRedis.queryUserRelationFocusProfileId(profileid, relationType, page);

        if (profileIds == null) {
            profileIds = new HashSet<String>();
        }

        //todo
        PageRows<String> rows = new PageRows<String>();
        rows.setPage(page);
        rows.setRows(new ArrayList<String>(profileIds));

        return rows;
    }

    @Override
    public Set<String> checkFollowStatus(String profileId, Collection<String> pids) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("SocialLogic checkFollowStatus.profileId:" + profileId + ",pids:" + pids);
        }

        if (CollectionUtil.isEmpty(pids)) {
            return new HashSet<String>();
        }


        Set<String> result = new HashSet<String>();
        result.addAll(socialRedis.checkFollowStatus(profileId, ObjectRelationType.WIKI_PROFILE, pids));
        return result;
    }

    @Override
    public Set<String> checkFansStatus(String profileId, Collection<String> pids) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("SocialLogic checkFollowStatus.profileId:" + profileId + ",pids:" + pids);
        }
        if (CollectionUtil.isEmpty(pids)) {
            return new HashSet<String>();
        }
        Set<String> result = new HashSet<String>();
        result.addAll(socialRedis.checkFansStatus(profileId, ObjectRelationType.WIKI_PROFILE, pids));
        return result;
    }
}
