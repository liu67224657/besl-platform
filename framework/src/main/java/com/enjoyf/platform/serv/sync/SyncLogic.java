package com.enjoyf.platform.serv.sync;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.db.sync.SyncHandler;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ThirdApiHotdeployConfig;
import com.enjoyf.platform.serv.sync.processor.SyncProcessor;
import com.enjoyf.platform.serv.sync.processor.SyncProcessorFactory;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.account.*;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.ShareSyncEvent;
import com.enjoyf.platform.service.event.system.SyncPostContentEvent;
import com.enjoyf.platform.service.event.user.UserEvent;
import com.enjoyf.platform.service.event.user.UserEventType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.*;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.service.usercenter.UserLogin;
import com.enjoyf.platform.service.vote.Vote;
import com.enjoyf.platform.service.vote.VoteServiceSngl;
import com.enjoyf.platform.text.ImageResolveUtil;
import com.enjoyf.platform.text.ImageSize;
import com.enjoyf.platform.thirdapi.ThirdApiProps;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
class SyncLogic implements SyncService {
    private static final Logger logger = LoggerFactory.getLogger(SyncLogic.class);

    private SyncConfig config;

    private QueueThreadN eventProcessorQueueThreadN;

    private SyncContextProcessor syncContextProcessor;
    private SyncImageGenerator syncImageGenerator;
    private ShareContextProcessor shareContextProcessor;

    private SyncHandler writeAbleHandler;
    private HandlerPool<SyncHandler> readonlyHandlersPool;

    private ShareCache shareCache;

    SyncLogic(SyncConfig cfg) {
        this.config = cfg;
        try {
            writeAbleHandler = new SyncHandler(config.getWriteableDataSourceName(), config.getProps());
            readonlyHandlersPool = new HandlerPool<SyncHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyHandlersPool.add(new SyncHandler(dsn, config.getProps()));
            }

            syncContextProcessor = new SyncContextProcessor();
            syncImageGenerator = new SyncImageGenerator();

            shareContextProcessor = new ShareContextProcessor();
        } catch (Exception e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass(), e);

            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        eventProcessorQueueThreadN = new QueueThreadN(cfg.getEventQueueThreadNum(), new QueueListener() {
            @Override
            public void process(Object obj) {
                if (obj instanceof SyncPostContentEvent) {
                    processQueuedSyncPostContentEvent((SyncPostContentEvent) obj);
                } else if (obj instanceof ShareSyncEvent) {
                    processShareExchangeGiftEvent((ShareSyncEvent) obj);
                } else if (obj instanceof ShareWrap) {
                    processShareQueue((ShareWrap) obj);
                } else {
                    GAlerter.lab("In eventProcessQueueThreadN, there is a unknown obj.");
                }
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "eventProcessorQueue"));

        try {
            FileUtil.createDirectory(config.getTempImagePath());
        } catch (FileNotFoundException e) {
            GAlerter.lab("createDirectory temp image path Exception." + this.getClass());

            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        shareCache = new ShareCache(config.getMemCachedConfig());
    }

    @Override
    public ShareBaseInfo createShareInfo(ShareBaseInfo shareInfo) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("createShareInfo, shareInfo is " + shareInfo);
        }

        return writeAbleHandler.insertShareInfo(shareInfo);
    }

    @Override
    public PageRows<ShareBaseInfo> queryShareInfoByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryShareInfo, queryExpress is " + queryExpress + " pagination is " + pagination);
        }

        return readonlyHandlersPool.getHandler().queryShareInfo(queryExpress, pagination);
    }

    @Override
    public List<ShareBaseInfo> queryShareInfo(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryShareInfo, queryExpress is " + queryExpress);
        }

        return readonlyHandlersPool.getHandler().queryShareInfo(queryExpress);
    }

    @Override
    public ShareBaseInfo getShareInfo(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryShareInfo, queryExpress is " + queryExpress);
        }

        return readonlyHandlersPool.getHandler().getShareInfo(queryExpress);
    }

    @Override
    public ShareBaseInfo getShareInfoById(long shareId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("queryShareInfo, shareId is " + shareId);
        }

        ShareBaseInfo baseInfo = shareCache.getShareBaseInfoById(shareId);

        if (baseInfo == null) {
            baseInfo = readonlyHandlersPool.getHandler().getShareInfo(new QueryExpress().add(QueryCriterions.eq(ShareBaseInfoField.SHAREID, shareId)));
            shareCache.putShareBaseInfo(shareId, baseInfo);
        }
        return baseInfo;
    }

    @Override
    public boolean modifyShareInfo(UpdateExpress updateExpress, QueryExpress queryExpress, long shareId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call modifyShareInfo updateExpress:" + updateExpress + " queryExpress:" + queryExpress);
        }

        boolean returnBoolen = writeAbleHandler.updateShareInfo(updateExpress, queryExpress);
        if (returnBoolen) {
            shareCache.deleteBaseInfoSource(shareId);
        }

        return returnBoolen;

    }


    @Override
    public ShareTopic createShareTopic(ShareTopic shareTopic) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("createShareTopic, shareTopic is " + shareTopic);
        }

        return writeAbleHandler.insertShareTopic(shareTopic);
    }

    @Override
    public PageRows<ShareTopic> queryShareTopicByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("createShareTopic, queryExpress is " + queryExpress + " pagination is " + pagination);
        }
        return readonlyHandlersPool.getHandler().queryShareTopic(queryExpress, pagination);
    }

    @Override
    public ShareBody createShareBody(ShareBody shareBody) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("createShareBody, shareBody is " + shareBody);
        }

        return writeAbleHandler.insertShareBody(shareBody);
    }

    @Override
    public List<ShareTopic> queryShareTopic(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("createShareTopic, queryExpress is " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryShareTopic(queryExpress);
    }

    @Override
    public boolean modifyShareTopic(UpdateExpress updateExpress, QueryExpress queryExpress, long shareid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("createShareTopic, updateExpress is " + updateExpress + " queryExpress is " + queryExpress);
        }

        boolean returnBoolen = writeAbleHandler.updateShareTopic(updateExpress, queryExpress);
        if (returnBoolen) {
            shareCache.deleteTopicList(shareid);
        }

        return returnBoolen;
    }

    @Override
    public List<ShareBody> queryShareBody(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("createShareBody, queryExpress is " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryShareBody(queryExpress);
    }

    @Override
    public PageRows<ShareBody> queryShareBodyByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("createShareBody, queryExpress is " + queryExpress + " pagination is " + pagination);
        }
        return readonlyHandlersPool.getHandler().queryShareBodyByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyShareBody(UpdateExpress updateExpress, QueryExpress queryExpress, long shareid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("createShareBody, updateExpress is " + updateExpress + " queryExpress is " + queryExpress);
        }

        boolean returnBoolen = writeAbleHandler.updateShareBody(updateExpress, queryExpress);
        if (returnBoolen) {
            shareCache.deleteBodyList(shareid);
        }

        return returnBoolen;
    }

    @Override
    public ShareInfo choiceShareInfo(long shareId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("choiceShareInfo, by shareId.shareId: " + shareId);
        }
        ShareInfo returnObj = null;

        ShareBaseInfo shareBaseInfo = shareCache.getShareBaseInfoBySource(shareId);
        if (shareBaseInfo == null) {
            shareBaseInfo = readonlyHandlersPool.getHandler().getShareInfo(new QueryExpress()
                    .add(QueryCriterions.eq(ShareBaseInfoField.SHAREID, shareId))
                    .add(QueryCriterions.eq(ShareBaseInfoField.REMOVESTATUS, ActStatus.UNACT.getCode())));

            shareCache.putShareBaseInfo(shareId, shareBaseInfo);
        }

        if (shareBaseInfo == null || shareBaseInfo.getShareId() == null) {
            return returnObj;
        }
        List<ShareBody> shareBodyList = getShareBody(shareBaseInfo.getShareId());
        List<ShareTopic> shareTopicList = getShareTopic(shareBaseInfo.getShareId());

        if (CollectionUtil.isEmpty(shareBodyList) || CollectionUtil.isEmpty(shareTopicList)) {
            return returnObj;
        }

        //chioce random
        returnObj = new ShareInfo();
        returnObj.setBaseInfo(shareBaseInfo);
        returnObj.setShareBody(CollectionUtil.getRandomObj(shareBodyList));
        returnObj.setShareTopic(CollectionUtil.getRandomObj(shareTopicList));

        return returnObj;
    }

    @Override
    public boolean syncShareInfo(SyncContent syncContent, TokenInfo tokenInfo, LoginDomain loginDomain, String profileId, long shareId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Put the event to eventProcessorQueueThreadN, syncContent is" + syncContent + " tokenInfo is " + tokenInfo + " accountDomain is " + loginDomain);
        }

        eventProcessorQueueThreadN.add(new ShareWrap(tokenInfo, syncContent, loginDomain, profileId, shareId));

        return false;
    }

    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Put the event to eventProcessorQueueThreadN, event is " + event);
        }

        eventProcessorQueueThreadN.add(event);
        return false;
    }

    private List<ShareBody> getShareBody(long shareId) throws DbException {
        List<ShareBody> bodyList = shareCache.getBodyList(shareId);
        if (bodyList == null) {
            bodyList = readonlyHandlersPool.getHandler().queryShareBody(new QueryExpress()
                    .add(QueryCriterions.eq(ShareBodyField.SHAREID, shareId))
                    .add(QueryCriterions.eq(ShareBodyField.REMOVESTATUS, ActStatus.UNACT.getCode())));
            shareCache.putBodyList(shareId, bodyList);
        }


        return bodyList;
    }

    private List<ShareTopic> getShareTopic(long shareId) throws DbException {
        List<ShareTopic> topicList = shareCache.getTopicList(shareId);
        if (topicList == null) {
            topicList = readonlyHandlersPool.getHandler().queryShareTopic(new QueryExpress()
                    .add(QueryCriterions.eq(ShareTopicField.SHAREID, shareId))
                    .add(QueryCriterions.eq(ShareTopicField.REMOVESTATUS, ActStatus.UNACT.getCode())));
            shareCache.putTopicList(shareId, topicList);
        }

        return topicList;
    }

    private void processShareQueue(ShareWrap shareWrap) {
        //todo   usercenter ????
        ThirdApiProps thirdApiProps = HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class).getThirdApiPropByAccountDomain(shareWrap.getLoginDomain());
        if (thirdApiProps == null || !thirdApiProps.isSupportShare()) {
            GAlerter.lan("share contnet not support.loginDomain: " + shareWrap.getLoginDomain());
            return;
        }
        AuthVersion authVersion = HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class).getAuthVersionMap().get(shareWrap.getLoginDomain());

        String tempImg = null;
        try {
            SyncProcessor processor = SyncProcessorFactory.get().getByProviderAndVsersion(shareWrap.getLoginDomain(), authVersion);

            tempImg = syncImageGenerator.generatorTempImage(shareWrap.getSyncContent().getSyncImg(), config.getTempImagePath(), String.valueOf(shareWrap.getShareId() + System.currentTimeMillis()));
            shareWrap.getSyncContent().setSyncImg(tempImg);
            SyncContent syncContent = shareContextProcessor.generatorContext(shareWrap.getSyncContent());

            int times = 0;
            do {
                if (processor.processShare(shareWrap.getTokenInfo(), syncContent)) {

                    //?????
                    if (shareWrap.getShareId() == 5) {
                        writeUserShareLog(shareWrap.getProfileId(), shareWrap.getShareId(), shareWrap.getLoginDomain(), ShareType.CLIENT, shareWrap.getSyncContent());
                        break;
                    }

                    ShareBaseInfo shareBaseInfo = getShareInfoById(shareWrap.getShareId());
                    if (shareBaseInfo == null) {
                        break;
                    }
                    writeUserShareLog(shareWrap.getProfileId(), shareWrap.getShareId(), shareWrap.getLoginDomain(), shareBaseInfo.getShareType(), shareWrap.getSyncContent());

                    break;
                }
                times++;
            } while (times < config.getRetryTimes());
        } catch (Exception e) {
            GAlerter.lan("processQueuedEvent occured Exception.e", e);
        } finally {
            //同步结束删除临时图片
            if (!StringUtil.isEmpty(tempImg)) {
                try {
                    FileUtils.forceDelete(new File(tempImg));
                } catch (Exception e) {
                    GAlerter.lab("delete tempImg failed.img is:" + tempImg + " Exception: ", e);
                }
            }
        }
    }

    private void processShareExchangeGiftEvent(ShareSyncEvent segEvent) {

        SyncContent syncContent = null;
        ShareInfo shareInfo = null;
        try {
            shareInfo = choiceShareInfo(segEvent.getShareId());

            if (shareInfo != null && shareInfo.getShareBody() != null && shareInfo.getShareTopic() != null && shareInfo.getBaseInfo() != null) {
                syncContent = new SyncContent();
                syncContent.setSyncTitle(shareInfo.getShareBody().getShareSubject());
                syncContent.setSyncText(shareInfo.getShareBody().getShareBody());
                syncContent.setSyncImg(shareInfo.getShareBody().getPicUrl());

                syncContent.setSyncTopic(shareInfo.getShareTopic().getShareTopic());

                syncContent.setSyncContentUrl(shareInfo.getBaseInfo().getShareSource());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }

        if (syncContent == null) {
            GAlerter.lab(this.getClass().getName() + " processShareExchangeGiftEvent shareBaseInfo is empty.shareBaseId." + segEvent.getShareId());
            return;
        }

        Set<LoginDomain> loginDomainSet=segEvent.getProfileFlag().getLoginDomain();

        //todo usercenter query userLogins by uno;
        List<UserLogin> userLoginList= null;
        try {
            userLoginList = UserCenterServiceSngl.get().queryUserLoginUno(segEvent.getProfileUno(),loginDomainSet);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName()+" occured ServiceException.e:",e);
            return;
        }

        //调用同步接口重试3次
        for (UserLogin userLogin : userLoginList) {
            //todo  usercenter
            ThirdApiProps thirdApiProps = HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class).getThirdApiPropByAccountDomain(userLogin.getLoginDomain());
            if (thirdApiProps == null || !thirdApiProps.isSupportShare()) {
                GAlerter.lan("sync contnet not support.userLogin: " + userLogin);
                continue;
            }

            try {
//                AccountThird accountThird = getAccountThird(segEvent.getProfileUno(), accountDomain);
//                if (accountThird == null) {
//                    continue;
//                }

                AuthVersion authVersion = HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class).getAuthVersionMap().get(userLogin.getLoginDomain());

                String tempImg = null;
                try {
                    SyncProcessor processor = SyncProcessorFactory.get().getByProviderAndVsersion(userLogin.getLoginDomain(), authVersion);

                    tempImg = syncImageGenerator.generatorTempImage(syncContent.getSyncImg(), config.getTempImagePath(), String.valueOf(System.currentTimeMillis()));
                    syncContent.setSyncImg(tempImg);
                    syncContent = shareContextProcessor.generatorContext(syncContent);

                    int times = 0;
                    do {
                        if (processor.processShare(userLogin.getTokenInfo(), syncContent)) {
                            writeUserShareLog(segEvent.getProfileUno(), shareInfo.getBaseInfo().getShareId(), userLogin.getLoginDomain(), shareInfo.getBaseInfo().getShareType(), syncContent);
                            break;
                        }
                        times++;
                    } while (times < config.getRetryTimes());
                } catch (Exception e) {
                    GAlerter.lan("processQueuedEvent occured Exception.e", e);
                } finally {
                    //同步结束删除临时图片
                    if (!StringUtil.isEmpty(tempImg)) {
                        try {
                            FileUtils.forceDelete(new File(tempImg));
                        } catch (Exception e) {
                            GAlerter.lab("delete tempImg failed.img is:" + tempImg + " Exception: ", e);
                        }
                    }
                }
            } catch (Exception e) {
                GAlerter.lan("processQueuedEvent occured Exception.e", e);
            }
        }

    }

    //处理事件
    private void processQueuedSyncPostContentEvent(SyncPostContentEvent spcEvent) {

        //查询文章和转贴内容
        Content content = null;
        Content relationContent = null;
        try {
            content = ContentServiceSngl.get().getContentById(spcEvent.getContentId());

            if (!StringUtil.isEmpty(spcEvent.getRelationId()) && !StringUtil.isEmpty(spcEvent.getRelationUno())) {
                relationContent = ContentServiceSngl.get().getContentById(spcEvent.getRelationId());
            }
        } catch (Exception e) {
            GAlerter.lab("process SyncPostContentEvent occured Exception,", e);
        }

        if (content == null) {
            GAlerter.lan("content is null,event:" + spcEvent);
            return;
        }

        //生成临时图片
        String tempImg = "";
        try {
            if (relationContent != null && !content.getContentType().hasImage() && StringUtil.isEmpty(content.getVoteSubjectId())) {
                tempImg = syncImageGenerator.generatorRelationImage(relationContent, config.getTempImagePath(), config.getSyncMsyhTTFDir(), config.getSyncSimSunTTFDir());
            } else if (content.getContentType().hasApp()) {
                tempImg = syncImageGenerator.generatorTempImage(ImageResolveUtil.genImageByTemplate(content.getApps().getApps().iterator().next().getAppSrc(), null), config.getTempImagePath(), content.getContentId());
            } else if (content.getContentType().hasImage()) {
                tempImg = syncImageGenerator.generatorTempImage(ImageResolveUtil.genImageByTemplate(content.getImages().getImages().iterator().next().getM(), ImageSize.IMAGE_SIZE_M), config.getTempImagePath(), content.getContentId());
            } else if (content.getContentType().hasVote()) {
                if (!StringUtil.isEmpty(content.getVoteSubjectId())) {
                    Vote vote = VoteServiceSngl.get().getVote(content.getVoteSubjectId());
                    if (vote != null && vote.getVoteSubject().getImageSet().getImages().size() > 0) {
                        tempImg = syncImageGenerator.generatorTempImage(ImageResolveUtil.genImageByTemplate(vote.getVoteSubject().getImageSet().getImages().iterator().next().getM(), ImageSize.IMAGE_SIZE_M), config.getTempImagePath(), content.getContentId());
                    }
                }
            } else if (relationContent != null && content.getContentType().hasVoteForWard()) {
                if (!StringUtil.isEmpty(relationContent.getVoteSubjectId())) {
                    Vote vote = VoteServiceSngl.get().getVote(relationContent.getVoteSubjectId());
                    if (vote != null && vote.getVoteSubject().getImageSet().getImages().size() > 0) {
                        tempImg = syncImageGenerator.generatorTempImage(ImageResolveUtil.genImageByTemplate(vote.getVoteSubject().getImageSet().getImages().iterator().next().getM(), ImageSize.IMAGE_SIZE_M), config.getTempImagePath(), relationContent.getContentId());
                    }
                }
            }
        } catch (Exception e) {
            GAlerter.lab("generator tempImg occured Exception,e:", e);
        }
        SyncContent syncContent = syncContextProcessor.processor(content, tempImg);

        Set<LoginDomain> loginDomainSet=spcEvent.getProfileFlag().getLoginDomain();
        List<UserLogin> userLoginList= null;
        try {
            userLoginList = UserCenterServiceSngl.get().queryUserLoginUno(spcEvent.getContentUno(),loginDomainSet);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName()+" occured ServiceException.e:",e);
            return;
        }

        //调用同步接口重试3次
        for (UserLogin userLogin : userLoginList) {
            //todo
            ThirdApiProps thirdApiProps =HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class).getThirdApiPropByAccountDomain(userLogin.getLoginDomain());
            if (thirdApiProps == null || !thirdApiProps.isSupportSync()) {
                GAlerter.lan("sync contnet not support.userLogin: " + userLogin);
                continue;
            }

            try {
                //todo usercenter
//                AccountThird accountThird = null;

//                getAccountThird(spcEvent.getContentUno(), loginDomain);

//                if (accountThird == null) {
//                    continue;
//                }

                int times = 0;
                do {
                    AuthVersion authVersion = HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class).getAuthVersionMap().get(userLogin.getLoginDomain());

                    SyncProcessor processor = SyncProcessorFactory.get().getByProviderAndVsersion(userLogin.getLoginDomain(), authVersion);

                    if (processor.processShare(userLogin.getTokenInfo(), syncContent)) {
                        UserEvent userEvent = new UserEvent(content.getUno());
                        userEvent.setEventType(UserEventType.USER_CONTENT_SYNC);
                        userEvent.setCount(1l);
                        userEvent.setEventDate(new Date());
                        userEvent.setDestUno(content.getContentId());
                        userEvent.setDescription(userLogin.getLoginDomain().getCode());

                        try {
                            EventDispatchServiceSngl.get().dispatch(userEvent);
                        } catch (Exception e) {
                            GAlerter.lab("sync content dispatch event error. ", e);
                        }
                        break;
                    }

                    times++;
                } while (times < config.getRetryTimes());
            } catch (Exception e) {
                GAlerter.lan("processQueuedEvent occured Exception.e", e);
            }
        }

        //同步结束删除临时图片
        if (!StringUtil.isEmpty(tempImg)) {
            try {
                FileUtils.forceDelete(new File(tempImg));
            } catch (Exception e) {
                GAlerter.lab("delete tempImg failed.img is:" + tempImg + " Exception: ", e);
            }
        }
    }

    private void writeUserShareLog(String profileUno, long shareBaseId, LoginDomain loginDomain, ShareType shareType, SyncContent syncContent) {
        ShareUserLog shareUserLog = new ShareUserLog();

        Date now = new Date();
        shareUserLog.setProfileUno(profileUno);
        shareUserLog.setLoginDomain(loginDomain);
        shareUserLog.setShareBaseInfoId(shareBaseId);
        shareUserLog.setShareDate(now);
        shareUserLog.setShareTime(now);
        shareUserLog.setShareType(shareType);
        shareUserLog.setShareurl(syncContent.getSyncContentUrl());
        try {
            writeAbleHandler.insertShareUserLog(shareUserLog);
        } catch (DbException e) {
            GAlerter.lab(this.getClass().getName() + " occured DbException.e", e);
        }
    }
}
