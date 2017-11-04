/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.content;


import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.content.CmsArticleHandler;
import com.enjoyf.platform.db.content.ContentHandler;
import com.enjoyf.platform.db.content.ContentHandlerMongo;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WallHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.serv.content.processor.*;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.*;
import com.enjoyf.platform.service.content.social.*;
import com.enjoyf.platform.service.content.wall.WallBlock;
import com.enjoyf.platform.service.content.wall.WallLayout;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.event.system.*;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.joymeapp.AppShareChannel;
import com.enjoyf.platform.service.message.*;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.profile.ProfileSumField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.timeline.*;
import com.enjoyf.platform.service.tools.ContentAuditStatus;
import com.enjoyf.platform.service.tools.ContentReplyAuditStatus;
import com.enjoyf.platform.service.viewline.*;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.util.timer.FixedRateTimerManagerPool;
import com.enjoyf.platform.util.timer.TimerTasker;
import com.google.common.base.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>  ,zx
 */

public class ContentLogic implements ContentService {

    //
    private static final Logger logger = LoggerFactory.getLogger(ContentLogic.class);
    //
    private static final int QUERY_HOT_ACTIVITY_PAGE_SIZE = 5;
    private static final int QUERY_HOT_ACTIVITY_PAGE_NO = 1;
    private static final int QUERY_HOT_ACTIVITY_PAGE_SIZE_SIX = 6;

    private static final int SOCIAL_CONTENT_HOT_SIGN_SIZE = 15;


    //
    private ContentConfig config;

    //the handler's
    private ContentHandler writeAbleContentHandler;
    private HandlerPool<ContentHandler> readonlyContentHandlersPool;

    private ContentHandlerMongo writeAbleContentHandlerMongo;
    private HandlerPool<ContentHandlerMongo> readonlyContentHandlersPoolMongo;

    //the discovery wall cache.
    private int currentMapSize = 0;
    private int reloadMapSize = 0;
    private Map<Integer, WallLayout> wallLayoutMapCache = null;
    private Date currentDate = null;
    private WallContentProcessor wallContentProcessor = null;
    private String serviceName;

    //the event queue thread n.
    private QueueThreadN eventProcessQueueThreadN = null;

    //the event queue thread n. context process (@ url game ...)
    private QueueThreadN contextProcessQueueThreadN = null;

    private ChainProcessor contentProcessor = null;

    //the content cache.(ehcache)
    private ContentCache contentCache;

    private ActivityCache activityCache;

    private ForignContentReplyCache forignConentReplyCache;
    private static final int CONTENT_REPLY_PAGESIZE = 5;

    private SocialContentCache socialContentCache;

    //hot category id
    private int hotCategoryId = 0;
    private int hotViewLineId = 0;
    private static final String HOT_CATEGORY_CODE = "talk";
    private static final String HOT_LINE_LOCATION_CODE = "hot";

    private CmsArticleHandler cmsWriteHandler;
    private HandlerPool<CmsArticleHandler> cmsReadHandlerPool;

    public ContentLogic(ContentConfig cfg) {
        config = cfg;

        //initialize the handler.
        try {
            //mysql
            writeAbleContentHandler = new ContentHandler(config.getWriteableDataSourceName(), config.getProps());
            readonlyContentHandlersPool = new HandlerPool<ContentHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyContentHandlersPool.add(new ContentHandler(dsn, config.getProps()));
            }
            //mongod
            writeAbleContentHandlerMongo = new ContentHandlerMongo(config.getMongoDbWriteAbleDateSourceName(), config.getProps());
            readonlyContentHandlersPoolMongo = new HandlerPool<ContentHandlerMongo>();
            for (String dsn : config.getMongoDbReadonlyDataSourceNames()) {
                readonlyContentHandlersPoolMongo.add(new ContentHandlerMongo(dsn, config.getProps()));
            }
            //
            try {
                cmsWriteHandler = new CmsArticleHandler(config.getCmsWriteableDataSourceName(), config.getProps());
                cmsReadHandlerPool = new HandlerPool<CmsArticleHandler>();
                for (String dsn : config.getCmsReadonlyDataSourceNames()) {
                    cmsReadHandlerPool.add(new CmsArticleHandler(dsn, config.getProps()));
                }
            } catch (DbException e) {
                GAlerter.lab("There isn't database cms article connection pool in the configure." + this.getClass(), e);
            }

        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());

            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }


        //the cache setting.
        contentCache = new ContentCache(config.getMemDiskCacheConfig());

        activityCache = new ActivityCache(config.getMemCachedConfig());
        forignConentReplyCache = new ForignContentReplyCache(config.getMemCachedConfig());
        socialContentCache = new SocialContentCache(config.getMemCachedConfig());


        //initialize the content processor.
        contentProcessor = new ChainProcessor();

        contentProcessor.addProcessor(new AtProcessor());
        contentProcessor.addProcessor(new ResourceFileUsingProcessor(writeAbleContentHandler));
        contentProcessor.addProcessor(new BillingEncourageProcessor());

        wallContentProcessor = new WallContentProcessor();
        wallLayoutMapCache = new HashMap<Integer, WallLayout>();
        serviceName = config.getServiceName();

        //the queue thread n initialize.
        eventProcessQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                //
                if (obj instanceof Event) {
                    //
                    processQueuedEvent((Event) obj);
                } else if (obj instanceof ResourceFile) {
                    //
                    processQueuedResourceFile((ResourceFile) obj);
                } else {
                    GAlerter.lab("In eventProcessQueueThreadN, there is a unknown obj.");
                }
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "eventProcessQueue"));


        //the context process queue thread n initialize.
        contextProcessQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {
            @Override
            public void process(Object obj) {
                contentProcessor.process(obj);
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "contextProcessQueue"));

        //重新加载
        reloadHotActivity();
        //定时器
        FixedRateTimerManagerPool.get().addTask(new TimerTasker() {
            @Override
            public void run() {
                reloadHotActivity();
            }
        }, 30L * 60L * 1000L, "reloadHotActivity");
    }

    private void reloadHotActivity() {
        try {
            List<Activity> activitylist = queryHotActivityByDb();
            //activityCache.removeLastedActivity();
            activityCache.putLastedActivity(activitylist);
        } catch (Exception e) {
            GAlerter.lab("ContentLogic reloadHotActivity Exception.e:" + e);
        }
    }


    private void processQueuedEvent(Event event) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to processQueuedEvent:" + event);
        }

        //check the event type.
        if (event instanceof ContentSumIncreaseEvent) {
            ContentSumIncreaseEvent castEvent = (ContentSumIncreaseEvent) event;

            try {
                //
                boolean bval = writeAbleContentHandler.updateContentNum(castEvent.getContentId(), castEvent.getField(), castEvent.getCount());

                if (!bval) {
                    return;
                }

                //get content
                Content content = getContentFromCache(castEvent.getContentId());

                //get from db.
                if (content == null) {
                    content = writeAbleContentHandler.getContentByCidUno(castEvent.getContentId());
                } else {
                    content = increaseContentSum(content, castEvent.getField(), castEvent.getCount());
                }

            } catch (Exception e) {
                //
                GAlerter.lab("ContentLogic processQueuedEvent error.", e);
            }
        } else if (event instanceof ContentInteractionSumIncreaseEvent) {
            ContentInteractionSumIncreaseEvent cisiEvent = (ContentInteractionSumIncreaseEvent) event;

            UpdateExpress updateExpress = new UpdateExpress().increase(cisiEvent.getField(), cisiEvent.getCount());
            QueryExpress updateQueryExpress = new QueryExpress().add(QueryCriterions.eq(ContentInteractionField.INTERACTIONID, cisiEvent.getContentInteractionId()));

            try {
                boolean bval = writeAbleContentHandler.updateInteraction(cisiEvent.getContentId(), updateExpress, updateQueryExpress);
            } catch (Exception e) {
                GAlerter.lab("ContentLogic processQueuedEvent ContentInteractionSumIncreaseEvent error.interactionId:" + cisiEvent.getContentInteractionId() + " e:", e);
            }

        } else if (event instanceof ContentFavoriteEvent) {
            ContentFavoriteEvent cfEvent = (ContentFavoriteEvent) event;
            try {
                Content content = getContentById(cfEvent.getContentId());

                if (content == null) {
                    GAlerter.lab("ContentLogic processQueuedEvent ContentFavoriteEvent.content is null.cid:" + cfEvent.getContentId());
                    return;
                }

                //
                ContentInteraction interaction = new ContentInteraction();
                interaction.setInteractionUno(cfEvent.getFavoriteUno());
                interaction.setContentUno(cfEvent.getContentUno());
                interaction.setContentId(cfEvent.getContentId());
                interaction.setCreateIp(cfEvent.getFavoriteIp());
                interaction.setInteractionType(InteractionType.FAVORITE);
                interaction.setCreateDate(cfEvent.getFavoriteDate());

                QueryExpress getExpress = new QueryExpress();
                getExpress.add(QueryCriterions.eq(ContentInteractionField.CONTENTID, cfEvent.getContentId()));
                getExpress.add(QueryCriterions.eq(ContentInteractionField.INTERACTIONUNO, cfEvent.getFavoriteUno()));
                getExpress.add(QueryCriterions.eq(ContentInteractionField.INTERACTIONTYPE, InteractionType.FAVORITE.getCode()));
                ContentInteraction oldInteraction = readonlyContentHandlersPool.getHandler().getInteraction(cfEvent.getContentId(), getExpress);
                if (oldInteraction == null) {
                    interaction = writeAbleContentHandler.insertInterAction(interaction);
                } else if (oldInteraction.getRemoveStatus().equals(ActStatus.ACTED)) {
                    QueryExpress updateQueryExpress = new QueryExpress();
                    updateQueryExpress.add(QueryCriterions.eq(ContentInteractionField.INTERACTIONID, oldInteraction.getInteractionId()));
                    UpdateExpress updateExpress = new UpdateExpress();
                    updateExpress.set(ContentInteractionField.REMOVESTATUS, ActStatus.UNACT.getCode());
                    updateExpress.set(ContentInteractionField.CREATEDATE, new Date());
                    writeAbleContentHandler.updateInteraction(cfEvent.getContentId(), updateExpress, updateQueryExpress);
                }

                //
                sendOutFavoriteContentEvent(cfEvent.getFavoriteUno(), content);
            } catch (Exception e) {
                GAlerter.lab("ContentLogic processQueuedEvent error.", e);
            }
        } else if (event instanceof ContentRepliedEvent) {
            ContentRepliedEvent contentRepliedEvent = (ContentRepliedEvent) event;

            try {
                //update the db.
                Map<ObjectField, Object> updateMap = new HashMap<ObjectField, Object>();
                updateMap.put(ContentField.LASTREPLYID, contentRepliedEvent.getReplyId());

                //if updating is successful, update the cache.
                if (writeAbleContentHandler.updateContent(contentRepliedEvent.getContentId(), updateMap)) {
                    //flush the cache.
                    Content content = contentCache.getContent(contentRepliedEvent.getContentId());
                    if (content != null) {
                        content.setLastReplyId(contentRepliedEvent.getReplyId());
                    }
                }
            } catch (Exception e) {
                GAlerter.lab("ContentLogic processQueuedEvent error.", e);
            }
        } else if (event instanceof ContentFavoriteRemoveEvent) {
            ContentFavoriteRemoveEvent cfEvent = (ContentFavoriteRemoveEvent) event;

            try {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(ContentInteractionField.REMOVESTATUS, ActStatus.ACTED.getCode());

                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(ContentInteractionField.REMOVESTATUS, ActStatus.UNACT.getCode()));
                queryExpress.add(QueryCriterions.eq(ContentInteractionField.CONTENTID, cfEvent.getContentId()));
                queryExpress.add(QueryCriterions.eq(ContentInteractionField.CONTENTUNO, cfEvent.getContentUno()));
                queryExpress.add(QueryCriterions.eq(ContentInteractionField.INTERACTIONUNO, cfEvent.getFavoriteUno()));
                queryExpress.add(QueryCriterions.eq(ContentInteractionField.INTERACTIONTYPE, InteractionType.FAVORITE.getCode()));

                boolean b = writeAbleContentHandler.updateInteraction(cfEvent.getContentId(), updateExpress, queryExpress);

                if (b) {
                    sendOutFavoriteRemoveContentEvent(cfEvent.getFavoriteUno(), cfEvent.getContentId(), cfEvent.getContentUno());
                }
            } catch (Exception e) {
                GAlerter.lab("ContentLogic processQueuedEvent ContentFavoriteRemoveEvent error.", e);
            }
        } else if (event instanceof ContentRelationCreateEvent) {
            ContentRelationCreateEvent crcEvent = (ContentRelationCreateEvent) event;
            if (StringUtil.isEmpty(crcEvent.getRelationId())) {
                return;
            }

            try {
                QueryExpress getExpress = new QueryExpress()
                        .add(QueryCriterions.eq(ContentRelationField.RELATIONID, crcEvent.getRelationId()))
                        .add(QueryCriterions.eq(ContentRelationField.CONTENTID, crcEvent.getContentId()))
                        .add(QueryCriterions.eq(ContentRelationField.RELATIONTYPE, crcEvent.getContentRelationType().getCode()));

                ContentRelation contentRelation = writeAbleContentHandler.getRelation(getExpress);
                if (contentRelation == null) {
                    //insert and increase to cache
                    contentRelation = new ContentRelation();
                    contentRelation.setRelationType(crcEvent.getContentRelationType());
                    contentRelation.setContentId(crcEvent.getContentId());
                    contentRelation.setRelationId(crcEvent.getRelationId());
                    contentRelation = writeAbleContentHandler.insertRelation(contentRelation);
                    insertRelationFromCache(contentRelation);
                } else {
                    if (!contentRelation.getRemoveStatus().equals(ActStatus.UNACT)) {
                        //update and increase to cache
                        UpdateExpress updateExpress = new UpdateExpress()
                                .set(ContentRelationField.REMOVESTATUS, ActStatus.UNACT.getCode())
                                .set(ContentRelationField.CREATEDATE, new Date());
                        boolean bVal = writeAbleContentHandler.updateRelation(updateExpress, getExpress);
                        if (bVal) {
                            insertRelationFromCache(contentRelation);
                        }
                    }
                }
            } catch (Exception e) {
                GAlerter.lab("insert relation error.retry update status");
            }
        } else if (event instanceof ContentRelationRemoveEvent) {
            ContentRelationRemoveEvent crrEvent = (ContentRelationRemoveEvent) event;
            if (StringUtil.isEmpty(crrEvent.getRelationId())) {
                return;
            }

            try {
                QueryExpress getExpress = new QueryExpress()
                        .add(QueryCriterions.eq(ContentRelationField.RELATIONID, crrEvent.getRelationId()))
                        .add(QueryCriterions.eq(ContentRelationField.CONTENTID, crrEvent.getContentId()))
                        .add(QueryCriterions.eq(ContentRelationField.RELATIONTYPE, crrEvent.getContentRelationType().getCode()));

                UpdateExpress updateExpress = new UpdateExpress().set(ContentRelationField.REMOVESTATUS, ActStatus.ACTED.getCode());
                boolean bVal = writeAbleContentHandler.updateRelation(updateExpress, getExpress);
                if (bVal) {
                    removeRelationFromCache(crrEvent);
                }
            } catch (DbException e) {
                GAlerter.lab("remove relation error.retry update status");
            }
        } else if (event instanceof ActivityCalEventTimeEvent) {
            ActivityCalEventTimeEvent cet = (ActivityCalEventTimeEvent) event;
            if (cet.getActivityId() == null) {
                return;
            }
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(ActivityField.EVENT_TIME, new Date());

            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ActivityField.ACTIVITY_ID, cet.getActivityId()));

            try {
                writeAbleContentHandler.modifyActivity(updateExpress, queryExpress);
            } catch (DbException e) {
                GAlerter.lab("update activity error.");
            }

        } else if (event instanceof SocialHotContentEvent) {
            SocialHotContentEvent catEvent = (SocialHotContentEvent) event;

            SocialHotContent hotContent = new SocialHotContent();
            hotContent.setUno(catEvent.getUno());
            hotContent.setContentId(catEvent.getContentId());
            hotContent.setCreateDate(new Date());
            hotContent.setRemoveStatus(ActStatus.UNACT);
            try {
                insertSocialHotContent(hotContent);
            } catch (ServiceException e) {
                GAlerter.lab("ContentLogic processQueuedEvent SocialHotContentEvent error.", e);
            }
        } else if (event instanceof SocialLogEvent) {
            SocialLogEvent catEvent = (SocialLogEvent) event;
            if (catEvent.getSocialLogType().equals(SocialLogType.SOCIAL_ACTIVITY)) {
                UpdateExpress updateExpress = new UpdateExpress();
                SocialLogCategory category = null;
                if (catEvent.getSocialLogCategory().equals(SocialLogCategory.USE)) {
                    updateExpress.increase(SocialActivityField.USE_SUM, catEvent.getIncreaseValue());
                    category = SocialLogCategory.USE;
                } else if (catEvent.getSocialLogCategory().equals(SocialLogCategory.REPLY)) {
                    updateExpress.increase(SocialActivityField.REPLY_SUM, catEvent.getIncreaseValue());
                    category = SocialLogCategory.REPLY;
                } else if (catEvent.getSocialLogCategory().equals(SocialLogCategory.AGREE)) {
                    updateExpress.increase(SocialActivityField.AGREE_SUM, catEvent.getIncreaseValue());
                    category = SocialLogCategory.AGREE;
                } else if (catEvent.getSocialLogCategory().equals(SocialLogCategory.GIFT)) {
                    updateExpress.increase(SocialActivityField.GIFT_SUM, catEvent.getIncreaseValue());
                    category = SocialLogCategory.GIFT;
                } else if (catEvent.getSocialLogCategory().equals(SocialLogCategory.TOTALS)) {
                    updateExpress.increase(SocialActivityField.TOTALS, catEvent.getIncreaseValue());
                    category = SocialLogCategory.TOTALS;
                }
                try {


                    modifySocialActivity(catEvent.getForeignId(), updateExpress);

                    if (category.equals(SocialLogCategory.AGREE) && catEvent.getIncreaseValue() > 0) {
                        modifySocialContentActivity(catEvent.getForeignId(), catEvent.getContentId(), new UpdateExpress().increase(SocialContentActivityField.HOT_RANK, 1));
                    }

                    //social_log
                    insertSocialLog(catEvent.getForeignId(), catEvent.getUno(), catEvent.getContentId(), SocialLogType.SOCIAL_ACTIVITY, category, catEvent.getPlatform(), catEvent.getShareChannel());
                } catch (Exception e) {
                    GAlerter.lab("ContentLogic processQueuedEvent SocialActivityEvent error.", e);
                }
            } else if (catEvent.getSocialLogType().equals(SocialLogType.SOCIAL_WATERMARK)) {
                UpdateExpress updateExpress = new UpdateExpress();
                SocialLogCategory category = null;
                if (catEvent.getSocialLogCategory().equals(SocialLogCategory.USE)) {
                    updateExpress.increase(SocialWatermarkField.USE_SUM, catEvent.getIncreaseValue());
                    category = SocialLogCategory.USE;
                }
                try {
                    writeAbleContentHandler.modifySocialWatermark(new QueryExpress()
                            .add(QueryCriterions.eq(SocialWatermarkField.WATERMARK_ID, catEvent.getForeignId())), updateExpress);

                    //social_log
                    insertSocialLog(catEvent.getForeignId(), catEvent.getUno(), catEvent.getContentId(), SocialLogType.SOCIAL_WATERMARK, category, catEvent.getPlatform(), catEvent.getShareChannel());
                } catch (DbException e) {
                    GAlerter.lab("ContentLogic processQueuedEvent SocialActivityEvent error.", e);
                }
            } else if (catEvent.getSocialLogType().equals(SocialLogType.SOCIAL_BGAUDIO)) {
                UpdateExpress updateExpress = new UpdateExpress();
                SocialLogCategory category = null;
                if (catEvent.getSocialLogCategory().equals(SocialLogCategory.USE)) {
                    updateExpress.increase(SocialBackgroundAudioField.USE_SUM, catEvent.getIncreaseValue());
                    category = SocialLogCategory.USE;
                }
                try {
                    writeAbleContentHandler.modifySocialBgAudio(new QueryExpress()
                            .add(QueryCriterions.eq(SocialBackgroundAudioField.AUDIO_ID, catEvent.getForeignId())), updateExpress);

                    //social_log
                    insertSocialLog(catEvent.getForeignId(), catEvent.getUno(), catEvent.getContentId(), SocialLogType.SOCIAL_BGAUDIO, category, catEvent.getPlatform(), catEvent.getShareChannel());
                } catch (DbException e) {
                    GAlerter.lab("ContentLogic processQueuedEvent SocialActivityEvent error.", e);
                }
            } else if (catEvent.getSocialLogType().equals(SocialLogType.SOCIAL_CONTENT)) {
                UpdateExpress updateExpress = new UpdateExpress();
                SocialLogCategory category = null;
                if (catEvent.getSocialLogCategory().equals(SocialLogCategory.SHARE)) {
                    updateExpress.increase(SocialContentField.SHARE_NUM, catEvent.getIncreaseValue());
                    category = SocialLogCategory.SHARE;
                }
                try {
                    writeAbleContentHandler.updateSocialContent(updateExpress, new QueryExpress()
                            .add(QueryCriterions.eq(SocialContentField.CONTENTID, catEvent.getContentId())));

                    //social_log
                    insertSocialLog(catEvent.getForeignId(), catEvent.getUno(), catEvent.getContentId(), SocialLogType.SOCIAL_CONTENT, category, catEvent.getPlatform(), catEvent.getShareChannel());
                } catch (DbException e) {
                    GAlerter.lab("ContentLogic processQueuedEvent SocialActivityEvent error.", e);
                }

            }
        } else {
            logger.info("ContentLogic discard the unknown event:" + event);
        }
    }

    private void insertRelationFromCache(ContentRelation contentRelation) {
        Content content = getContentFromCache(contentRelation.getContentId());
        if (content != null) {
            if (content.getRelationSet() == null) {
                content.setRelationSet(new ContentRelationSet());
            }
            if (contentRelation.getRelationType().equals(ContentRelationType.GAME)) {
                if (content.getRelationSet().getGameRelationMap() == null) {
                    content.getRelationSet().setGameRelationMap(new HashMap<String, ContentRelation>());
                }
                content.getRelationSet().getGameRelationMap().put(contentRelation.getRelationId(), contentRelation);
            } else if (contentRelation.getRelationType().equals(ContentRelationType.GROUP)) {
                content.getRelationSet().setGroupRelation(contentRelation);
            } else if (contentRelation.getRelationType().equals(ContentRelationType.ADMIN_ADJUST_POINT)) {
                content.getRelationSet().setGroupPointRelation(contentRelation);
            }
        }
    }

    private void removeRelationFromCache(ContentRelationRemoveEvent crrEvent) {
        Content content = getContentFromCache(crrEvent.getContentId());
        if (content != null) {
            if (content.getRelationSet() == null) {
                content.setRelationSet(new ContentRelationSet());
            }
            if (crrEvent.getContentRelationType().equals(ContentRelationType.GAME)) {
                if (content.getRelationSet().getGameRelationMap() == null) {
                    content.getRelationSet().setGameRelationMap(new HashMap<String, ContentRelation>());
                }
                content.getRelationSet().getGameRelationMap().remove(crrEvent.getRelationId());
            } else if (crrEvent.getContentRelationType().equals(ContentRelationType.GROUP)) {
                content.getRelationSet().setGroupRelation(null);
            }
        }
    }

    private void sendOutFavoriteContentEvent(String inveractionUno, Content content) {
        SocialFavoriteContentBroadcastEvent sfcbEvent = new SocialFavoriteContentBroadcastEvent();

        sfcbEvent.setContentId(content.getContentId());
        sfcbEvent.setContentUno(content.getUno());
        sfcbEvent.setRootContentId(content.getRootContentId());
        sfcbEvent.setRootContentUno(content.getRootContentUno());
        sfcbEvent.setParentContentId(content.getParentContentId());
        sfcbEvent.setParentContentId(content.getParentContentUno());
        sfcbEvent.setFavoriteUno(inveractionUno);
        sfcbEvent.setContentType(content.getContentType());
        sfcbEvent.setPostDate(new Date());

        try {
            EventDispatchServiceSngl.get().dispatch(sfcbEvent);
        } catch (Exception e) {
            //
            GAlerter.lan("ContentLogic send out sendOutFavoriteContentEvent error.", e);
        }
    }

    private void sendOutFavoriteRemoveContentEvent(String inveractionUno, String contentId, String contentUno) {
        SocialFavoriteRemoveContentBroadcastEvent sfcbEvent = new SocialFavoriteRemoveContentBroadcastEvent();
        sfcbEvent.setContentId(contentId);
        sfcbEvent.setContentUno(contentUno);
        sfcbEvent.setFavoriteUno(inveractionUno);
        try {
            EventDispatchServiceSngl.get().dispatch(sfcbEvent);
        } catch (Exception e) {
            //
            GAlerter.lan("ContentLogic send out sendOutFavoriteRemoveContentEvent error.", e);
        }
    }

    private void processQueuedResourceFile(ResourceFile file) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to processQueuedResourceFile:" + file);
        }

        try {
            writeAbleContentHandler.insertResourceFile(file);
        } catch (Exception e) {
            //
            GAlerter.lab("ContentLogic processQueuedResourceFile error.", e);
        }
    }

    //add increase Sum by field
    private Content increaseContentSum(Content content, ObjectField field, int increaseSum) {
        if (field.equals(ContentField.FAVORTIMES)) {
            content.setFavorTimes(content.getFavorTimes() + increaseSum);
        } else if (field.equals(ContentField.DINGIMES)) {
            content.setDingTimes(content.getDingTimes() + increaseSum);
        } else if (field.equals(ContentField.CAITIMES)) {
            content.setReplyTimes(content.getCaiTimes() + increaseSum);
        } else if (field.equals(ContentField.FORWARDTIMES)) {
            content.setForwardTimes(content.getForwardTimes() + increaseSum);
        } else if (field.equals(ContentField.REPLYTIMES)) {
            content.setReplyTimes(content.getReplyTimes() + increaseSum);
        } else if (field.equals(ContentField.VIEWTIMES)) {
            content.setViewTimes(content.getViewTimes() + increaseSum);
        } else if (field.equals(ContentField.FLOORTIMES)) {
            content.setFloorTimes(content.getFloorTimes() + increaseSum);
        }

        return content;
    }




    @Override
    public Content postContent(Content content) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleContentHandler to save content, content is " + content);
        }

        //write the content into the db.
        Content returnValue = writeAbleContentHandler.insertContent(content);


        if (returnValue != null) {

            sendOutPostContentEvent(returnValue);
            //set context process queue
            contextProcessQueueThreadN.add(returnValue);
        }

        //
        return returnValue;
    }

    @Override
    public List<Content> queryContentByQuery(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyContentHandler to query content, queryExpress:" + queryExpress);
        }
        return readonlyContentHandlersPool.getHandler().queryContentByQuery(queryExpress);
    }

    private void sendOutPostContentEvent(Content content) {
        //write self blog timeline
        try {

            TimeLineItem timeLineItem = new TimeLineItem();

            timeLineItem.setOwnUno(content.getUno());
            timeLineItem.setDomain(TimeLineDomain.BLOG);

            timeLineItem.setDirectUno(content.getUno());
            timeLineItem.setDirectId(content.getContentId());

            timeLineItem.setParentUno(content.getParentContentUno());
            timeLineItem.setParentId(content.getParentContentId());

            timeLineItem.setRelationUno(content.getRootContentUno());
            timeLineItem.setRelationId(content.getRootContentId());

            timeLineItem.setCreateDate(content.getPublishDate());
            timeLineItem.setFilterType(TimeLineFilterType.ContentTypeToTimeLineFilterType(content.getContentType()));

            //insert into the blog timeline immediately
            TimeLineServiceSngl.get().insertTimeLine(content.getUno(), timeLineItem);

            //insert into the home timeline
            timeLineItem.setDomain(TimeLineDomain.HOME);
            TimeLineServiceSngl.get().insertTimeLine(content.getUno(), timeLineItem);
        } catch (Exception e) {
            //
            GAlerter.lan("ContentLogic call timeline service to insert self timeline error..", e);
        }

        //send out the system event
        SocialPostContentBroadcastEvent scbEvent = new SocialPostContentBroadcastEvent();

        scbEvent.setContentUno(content.getUno());
        scbEvent.setContentId(content.getContentId());

        scbEvent.setRootContentUno(content.getRootContentUno());
        scbEvent.setRootContentId(content.getRootContentId());

        scbEvent.setParentContentUno(content.getParentContentUno());
        scbEvent.setParentContentId(content.getParentContentId());

        scbEvent.setTimeLineFilterType(TimeLineFilterType.ContentTypeToTimeLineFilterType(content.getContentType()));

        scbEvent.setPostDate(content.getPublishDate());

        //update the profile sum increase event
        ProfileSumIncreaseEvent psiEvent = new ProfileSumIncreaseEvent();

        psiEvent.setCount(1);
        psiEvent.setProfileId(content.getUno());

        //the content sum increase event.
        ContentSumIncreaseEvent csiEvent1 = null;
        ContentSumIncreaseEvent csiEvent2 = null;
        if (ContentPublishType.FORWARD.equals(content.getPublishType())) {
            //content sum increase
            if (!Strings.isNullOrEmpty(content.getRootContentId())) {
                csiEvent1 = new ContentSumIncreaseEvent();

                csiEvent1.setContentId(content.getRootContentId());
                csiEvent1.setContentUno(content.getRootContentUno());
                csiEvent1.setCount(1);
                csiEvent1.setField(ContentField.FORWARDTIMES);
            }

            if (!Strings.isNullOrEmpty(content.getParentContentId()) && !content.getParentContentId().equals(content.getRootContentId())) {
                csiEvent2 = new ContentSumIncreaseEvent();

                csiEvent2.setContentId(content.getParentContentId());
                csiEvent2.setContentUno(content.getParentContentUno());
                csiEvent2.setCount(1);
                csiEvent2.setField(ContentField.FORWARDTIMES);
            }
        }

        ProfileLastInteractionIncreaseEvent lastInteractionIncreaseEvent = new ProfileLastInteractionIncreaseEvent();
        lastInteractionIncreaseEvent.setField(ProfileSumField.LASTCONTENTID);
        lastInteractionIncreaseEvent.setOwnUno(content.getUno());
        lastInteractionIncreaseEvent.setValue(content.getContentId());

        //habit tag event.
//        HabitTagInsertEvent htiEvent = new HabitTagInsertEvent();
//
//        htiEvent.setContentId(content.getContentId());
//        htiEvent.setTag(content.getContentTag());
//        htiEvent.setTagType(UserTagType.POST);
//        htiEvent.setOwnUno(content.getUno());

        //
        try {
            EventDispatchServiceSngl.get().dispatch(scbEvent);
            EventDispatchServiceSngl.get().dispatch(psiEvent);
//            EventDispatchServiceSngl.get().dispatch(htiEvent);

            EventDispatchServiceSngl.get().dispatch(lastInteractionIncreaseEvent);

            EventDispatchServiceSngl.get().dispatch(csiEvent1);
            EventDispatchServiceSngl.get().dispatch(csiEvent2);
        } catch (Exception e) {
            //
            GAlerter.lan("ContentLogic send out sendOutPostContentEvent error.", e);
        }
    }

    /**
     * remove content
     */
    @Override
    public boolean removeContent(String contentId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleContentHandler to remove content, contentid is " + contentId);
        }

        //
        boolean returnValue = false;

        Content content = writeAbleContentHandler.getContentByCidUno(contentId);
        if (content != null) {

            if (ActStatus.ACTED.equals(content.getRemoveStatus())) {
                returnValue = true;
            } else {
                returnValue = writeAbleContentHandler.removeContent(contentId);

                if (returnValue) {
                    sendOutRemoveContentEvent(content);
                } else {
                    logger.info("Content logic removeContent() call writeAbleContentHandler removeContent error.");
                }
            }
        } else {
            logger.info("Content logic removeContent() call writeAbleContentHandler getContentById, just get null.");
        }

        //
        contentCache.removeContent(contentId);

        return returnValue;
    }

    private void sendOutRemoveContentEvent(Content content) {
        //send out the system event
        SocialRemoveContentBroadcastEvent srcbEvent = new SocialRemoveContentBroadcastEvent();

        srcbEvent.setContentUno(content.getUno());
        srcbEvent.setContentId(content.getContentId());

        //update the profile sum increase event
        ProfileSumIncreaseEvent psiEvent = new ProfileSumIncreaseEvent();

        psiEvent.setCount(-1);
        psiEvent.setProfileId(content.getUno());

        //
        ContentSumIncreaseEvent csiEvent1 = null;
        ContentSumIncreaseEvent csiEvent2 = null;
        if (ContentPublishType.FORWARD.equals(content.getPublishType())) {
            //content sum increase
            if (!Strings.isNullOrEmpty(content.getRootContentId())) {
                csiEvent1 = new ContentSumIncreaseEvent();

                csiEvent1.setContentId(content.getRootContentId());
                csiEvent1.setContentUno(content.getRootContentUno());
                csiEvent1.setCount(-1);
                csiEvent1.setField(ContentField.FORWARDTIMES);
            }

            if (!Strings.isNullOrEmpty(content.getParentContentId()) && !content.getParentContentId().equals(content.getRootContentId())) {
                csiEvent2 = new ContentSumIncreaseEvent();

                csiEvent2.setContentId(content.getParentContentId());
                csiEvent2.setContentUno(content.getParentContentUno());
                csiEvent2.setCount(-1);
                csiEvent2.setField(ContentField.FORWARDTIMES);
            }
        }

        ProfileLastInteractionIncreaseEvent lastInteractionIncreaseEvent = new ProfileLastInteractionIncreaseEvent();
        lastInteractionIncreaseEvent.setField(ProfileSumField.LASTCONTENTID);
        lastInteractionIncreaseEvent.setOwnUno(content.getUno());

        try {
            EventDispatchServiceSngl.get().dispatch(srcbEvent);
            EventDispatchServiceSngl.get().dispatch(psiEvent);

            EventDispatchServiceSngl.get().dispatch(lastInteractionIncreaseEvent);

            EventDispatchServiceSngl.get().dispatch(csiEvent1);
            EventDispatchServiceSngl.get().dispatch(csiEvent2);
        } catch (Exception e) {
            //
            GAlerter.lan("ContentLogic send out sendOutRemoveContentEvent error.", e);
        }
    }

    /**
     * modify content
     */
    @Override
    public boolean modifyContent(String contentId, Map<ObjectField, Object> map) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleContentHandler to modifyContent , contentid is " + contentId + ", map is " + map);
        }

        boolean returnValue = false;

        if (map != null && map.size() > 0 && map.get(ContentField.REMOVESTATUS) != null) {
            ActStatus actStatus = ActStatus.getByCode(map.get(ContentField.REMOVESTATUS).toString());
            ContentAuditStatus auditStatus = new ContentAuditStatus((Integer) map.get(ContentField.AUDITSTATUS));

            Content content = writeAbleContentHandler.getContentByCidUno(contentId);
            if (content != null) {

                if (content.getRemoveStatus().equals(actStatus) && content.getAuditStatus().getValue() == auditStatus.getValue()) {
                    returnValue = true;
                } else {
                    returnValue = writeAbleContentHandler.updateContent(contentId, map);
                    if (returnValue && !content.getRemoveStatus().equals(actStatus)) {
                        //contentCache.removeContent(contentId);

                        // 如果是删?
                        if (ActStatus.ACTED.equals(actStatus)) {
                            sendOutRemoveContentEvent(content);

                        } else if (ActStatus.UNACT.equals(actStatus)) {
                            sendOutPostContentEvent(content);
                        }
                    } else {
                        logger.info("ContentLogic modifyContent return false or not removestatus column, ignore sending event.");
                    }

                }
                //更新缓存
                contentCache.removeContent(contentId);


            } else {
                logger.info("Content logic modifyContent() call writeAbleContentHandler getContentById, just get null.");
            }
        } else {
            // 普通的修改操作
            Content content = writeAbleContentHandler.getContentByCidUno(contentId);

            if (content != null) {
                returnValue = writeAbleContentHandler.updateContent(contentId, map);

                if (returnValue) {
                    contentCache.removeContent(contentId);

                    if (map.containsKey(ContentField.CONTENTTAG)) {
//                        sendOutModifyContentEvent(contentId, content.getUno(), ContentTag.parse((String) map.get(ContentField.CONTENTTAG)));
                    }
                } else {
                    logger.info("ContentLogic modifyContent return false or not contain the tag column, ignore sending event.");
                }
            }
        }

        return returnValue;
    }

    /**
     * 更新内容中的数，收藏数、转发数、回复数?
     */
    @Override
    public boolean modifyContentNum(String contentId, ObjectField field, Integer value) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleContentHandler to modifyContentNum , contentid is " + contentId + ", value:" + value);
        }

        //
        contentCache.removeContent(contentId);

        return writeAbleContentHandler.updateContentNum(contentId, field, value);
    }

    /**
     * 查询单条记录，根据ID，UNO
     */
    @Override
    public Content getContentById(String contentId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyContentHandlersPool to findById , contentid is " + contentId);
        }

        Content returnValue = null;

        //get from cache;
        returnValue = getContentFromCache(contentId);

        //get from db.
        if (returnValue == null) {
            returnValue = readonlyContentHandlersPool.getHandler().getContentByCidUno(contentId);

            //put back to cache
            contentCache.putContent(contentId, returnValue);
        }

        return returnValue;
    }

    @Override
    public ContentRelation createContentRelation(String contentId, ContentRelation contentRelation) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleContentHandler to createContentRelation , contentid is " + contentId + " contentRelation:" + contentRelation);
        }
        ContentRelation returnValue = writeAbleContentHandler.insertRelation(contentRelation);

        contentCache.removeContent(contentId);

        return returnValue;
    }

    public List<Content> queryContents(Set<String> contentIds) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyContentHandlersPool to queryContents ,  contentIds is " + contentIds);
        }

        List<Content> returnValue = new ArrayList<Content>();

        Set<String> restContentIds = new HashSet<String>();
        //chech the cache;
        for (String contentId : contentIds) {
            //if the content is in cache.
            Content content = getContentFromCache(contentId);

            if (content != null) {
                returnValue.add(content);
            } else {
                restContentIds.add(contentId);
            }
        }

        //query the rest from the db.
        if (restContentIds.size() > 0) {
            List<Content> contents = readonlyContentHandlersPool.getHandler().queryContents(restContentIds);

            //put to cache.
            for (Content c : contents) {
                contentCache.putContent(c.getContentId(), c);
            }

            //put to return
            returnValue.addAll(contents);
        }

        return returnValue;
    }

    @Override
    public PageRows<Content> queryContentsByUno(String uno, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyContentHandlersPool to queryContentsByUno ,  uno is " + uno);
        }

        return readonlyContentHandlersPool.getHandler().queryContents(uno, page);
    }

    @Override
    public PageRows<Content> queryContentsByDateStep(Date startDate, Date endDate, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyContentHandlersPool to queryContentsByDateStep startDate: " + new java.sql.Timestamp(startDate.getTime()) + " endDate: " + new java.sql.Timestamp(endDate.getTime()));
        }
        return readonlyContentHandlersPool.getHandler().queryContentsByDateStep(startDate, endDate, page);
    }

    @Override
    public Map<String, List<Content>> queryLastestContentsByUno(Set<String> uno, Integer size) throws ServiceException {
        return null;
    }


    public Map<String, List<Content>> queryContents(Set<String> unos, Integer size) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyContentHandlersPool to queryContents ,  unos is " + unos);
        }

        Map<String, List<Content>> returnValue = new HashMap<String, List<Content>>();

        for (String uno : unos) {
            List<Content> list = readonlyContentHandlersPool.getHandler().queryLastestContents(uno, size);

            returnValue.put(uno, list);
        }

        return returnValue;
    }

    @Override
    public boolean postResourceFile(ResourceFile file) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Put the file into eventProcessQueueThreadN, file:" + file);
        }

        eventProcessQueueThreadN.add(file);

        return true;
    }

    @Override
    public Map<Integer, WallLayout> queryWallLayoutMap(Integer mapSize) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyContentHandlersPool to queryWallLayoutMap, mapSize:" + mapSize);
        }

        Map<Integer, WallLayout> returnValue = new LinkedHashMap<Integer, WallLayout>();
        int displaySize = HotdeployConfigFactory.get().getConfig(WallHotdeployConfig.class).getWallLayoutPageDisplaySize();


        synchronized (wallLayoutMapCache) {
            if (!CollectionUtil.isEmpty(wallLayoutMapCache)) {
                if (mapSize == 0) {
                    mapSize = currentMapSize + 1;
                }
                //
                for (int i = 0; i < displaySize; i++) {
                    if (wallLayoutMapCache.get(--mapSize) != null) {
                        returnValue.put(mapSize, wallLayoutMapCache.get(mapSize));
                    } else {
                        returnValue.put(currentMapSize, wallLayoutMapCache.get(currentMapSize));
                        mapSize = currentMapSize;
                    }
                }
            }
        }

        return returnValue;
    }

    @Override
    public PageRows<Content> queryContentByQueryExpress(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyContentHandlersPool to queryContentByQueryExpress, page:" + page + "   queryExpress:" + queryExpress);
        }

        PageRows<Content> returnValue = new PageRows<Content>();
        List list = readonlyContentHandlersPool.getHandler().query(queryExpress, page);

        returnValue.setPage(page);
        returnValue.setRows(list);

        return returnValue;

    }

    @Override
    public PageRows<ContentInteraction> queryContentReply(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyContentHandlersPool to queryContentReplyByQueryExpress, page:" + page + "   QueryExpress:" + queryExpress);
        }

        PageRows<ContentInteraction> returnValue = new PageRows<ContentInteraction>();
        List<ContentInteraction> list = readonlyContentHandlersPool.getHandler().queryContentReply(queryExpress, page);

        returnValue.setPage(page);
        returnValue.setRows(list);

        return returnValue;
    }

    @Override
    public long queryContentReplyTimes(String contentId, Date from, Date to) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyContentHandlersPool to queryContentReplyTimes, contentId:" + contentId + ", from:" + from + ", to:" + to);
        }

        return readonlyContentHandlersPool.getHandler().queryContentReplyTimes(contentId, from, to);
    }


    @Override
    public boolean auditContentReply(String replyId, String uno, String contentId, Map<ObjectField, Object> map) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleContentHandler to modifyContentReply , contentid is " + contentId + ", uno is " + uno + ", replyId:" + replyId + ", map is " + map);
        }

        boolean returnValue = false;

        if (!CollectionUtil.isEmpty(map) && map.get(ContentInteractionField.REMOVESTATUS) != null) {
            ActStatus actStatus = ActStatus.getByCode(map.get(ContentInteractionField.REMOVESTATUS).toString());
            ContentReplyAuditStatus auditStatus = new ContentReplyAuditStatus(Integer.parseInt(map.get(ContentInteractionField.AUDITSTATUS).toString()));

            QueryExpress getExpress = new QueryExpress();
            getExpress.add(QueryCriterions.eq(ContentInteractionField.INTERACTIONID, replyId));
            ContentInteraction reply = writeAbleContentHandler.getInteraction(contentId, getExpress);

            if (logger.isDebugEnabled()) {
                logger.debug("(tools audit modifyReply for remomveStatus)：" + actStatus.getCode());
            }

            if (reply == null) {
                logger.info("ContentLogic modifyContentReply() reply is empty .");
                return returnValue;
            }


            if (reply.getRemoveStatus().equals(actStatus) && reply.getAuditStatus().getValue().equals(auditStatus.getValue())) {
                returnValue = true;
            } else {

                QueryExpress updateQueryExpress = new QueryExpress();
                updateQueryExpress.add(QueryCriterions.eq(ContentInteractionField.INTERACTIONID, replyId));

                UpdateExpress updateExpress = new UpdateExpress();
                for (Map.Entry<ObjectField, Object> entry : map.entrySet()) {
                    updateExpress.set(entry.getKey(), entry.getValue());
                }
                returnValue = writeAbleContentHandler.updateInteraction(contentId, updateExpress, updateQueryExpress);

                if (logger.isDebugEnabled()) {
                    logger.debug("tools audit modifyReply update returnValue:" + returnValue);
                }

                if (returnValue && !reply.getRemoveStatus().equals(actStatus)) {

                    // 如果是删除
                    if (ActStatus.ACTED.equals(actStatus)) {
                        sendOutRemoveReplyEvent(reply);

                    } else if (ActStatus.UNACT.equals(actStatus)) {
                        sendOutPostReplyEvent(reply);
                    }
                } else {
                    logger.info("ContentLogic modifyContentReply return false .");
                }
            }

        } else {
            // 普通
            QueryExpress updateQueryExpress = new QueryExpress();
            updateQueryExpress.add(QueryCriterions.eq(ContentInteractionField.INTERACTIONID, replyId));

            UpdateExpress updateExpress = new UpdateExpress();
            for (Map.Entry<ObjectField, Object> entry : map.entrySet()) {
                updateExpress.set(entry.getKey(), entry.getValue());
            }
            returnValue = writeAbleContentHandler.updateInteraction(contentId, updateExpress, updateQueryExpress);
        }


        return returnValue;
    }

    @Override
    public ContentInteraction createInteraction(ContentInteraction interaction) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleContentHandler to createInteraction , interaction is " + interaction);
        }

        ContentInteraction returnValue = writeAbleContentHandler.insertInterAction(interaction);

        if (returnValue != null) {
            if (returnValue.getInteractionType().equals(InteractionType.REPLY)) {
                processReply(returnValue);
            }
        }

        return returnValue;
    }

    private void processReply(ContentInteraction reply) {
        sendOutPostReplyEvent(reply);

        contextProcessQueueThreadN.add(reply);
    }

    private void sendOutPostReplyEvent(ContentInteraction reply) {
        //send out timeline event to timeline server.
        //for my reply timeline
        TimeLineInsertEvent myReplyEvent = new TimeLineInsertEvent();

        myReplyEvent.setOwnUno(reply.getInteractionUno());
        myReplyEvent.setDomain(TimeLineDomain.MYREPLY);

        myReplyEvent.setDirectUno(reply.getInteractionUno());
        myReplyEvent.setDirectId(reply.getInteractionId());

        myReplyEvent.setParentUno(reply.getParentUno());
        myReplyEvent.setParentId(reply.getParentId());

        myReplyEvent.setRelationUno(reply.getContentUno());
        myReplyEvent.setRelationId(reply.getContentId());
        myReplyEvent.setTimeLineDate(reply.getCreateDate());

        //for reply me timeline
        TimeLineInsertEvent replyMeEvent = null;

        if (StringUtil.isEmpty(reply.getRootId()) && !reply.getContentUno().equals(reply.getInteractionUno())) {
            replyMeEvent = new TimeLineInsertEvent();

            replyMeEvent.setOwnUno(reply.getContentUno());
            replyMeEvent.setDomain(TimeLineDomain.REPLYME);

            replyMeEvent.setProcessAtStatus(reply.getProcessAtStatus());

            replyMeEvent.setDirectUno(reply.getInteractionUno());
            replyMeEvent.setDirectId(reply.getInteractionId());

            replyMeEvent.setParentUno(reply.getParentUno());
            replyMeEvent.setParentId(reply.getParentId());

            replyMeEvent.setRelationUno(reply.getContentUno());
            replyMeEvent.setRelationId(reply.getContentId());
            replyMeEvent.setTimeLineDate(reply.getCreateDate());
        }

        //insert the reply timeline to the parent reply.
        TimeLineInsertEvent replyMeRootEvent = null;

        //如果是回复，主楼的发布人不是评论发布人 并且被回复的人不是作者，要给被回复的人的replyme的timeline插入一条记录
        if (!StringUtil.isEmpty(reply.getRootUno())
                && !reply.getRootUno().equals(reply.getInteractionUno())) {
            replyMeRootEvent = new TimeLineInsertEvent();

            replyMeRootEvent.setOwnUno(reply.getRootUno());
            replyMeRootEvent.setDomain(TimeLineDomain.REPLYME);

            replyMeRootEvent.setProcessAtStatus(reply.getProcessAtStatus());

            replyMeRootEvent.setDirectUno(reply.getInteractionUno());
            replyMeRootEvent.setDirectId(reply.getInteractionId());

            //回复主楼
            if (StringUtil.isEmpty(reply.getParentUno())) {
                replyMeRootEvent.setParentUno(reply.getRootUno());
                replyMeRootEvent.setParentId(reply.getRootId());
            } else {
                //回复楼中楼
                replyMeRootEvent.setParentUno(reply.getParentUno());
                replyMeRootEvent.setParentId(reply.getParentId());
            }

            replyMeRootEvent.setRelationUno(reply.getContentUno());
            replyMeRootEvent.setRelationId(reply.getContentId());

            replyMeRootEvent.setTimeLineDate(reply.getCreateDate());
        }


        //insert the reply timeline to the parent reply.
        TimeLineInsertEvent replyMeParentEvent = null;

        if (!StringUtil.isEmpty(reply.getParentUno())
                && !reply.getParentUno().equals(reply.getInteractionUno())) {
            replyMeParentEvent = new TimeLineInsertEvent();

            replyMeParentEvent.setOwnUno(reply.getParentUno());
            replyMeParentEvent.setDomain(TimeLineDomain.REPLYME);

            replyMeParentEvent.setProcessAtStatus(reply.getProcessAtStatus());

            replyMeParentEvent.setDirectUno(reply.getInteractionUno());
            replyMeParentEvent.setDirectId(reply.getInteractionId());

            replyMeParentEvent.setParentUno(reply.getParentUno());
            replyMeParentEvent.setParentId(reply.getParentId());

            replyMeParentEvent.setRelationUno(reply.getContentUno());
            replyMeParentEvent.setRelationId(reply.getContentId());

            replyMeParentEvent.setTimeLineDate(reply.getCreateDate());
        }

        //更新内容中的数，收藏数、转发数、回复数
        //for the content sum
        ContentSumIncreaseEvent csiEvent = new ContentSumIncreaseEvent();
        csiEvent.setContentId(reply.getContentId());
        csiEvent.setContentUno(reply.getContentUno());
        csiEvent.setCount(1);
        csiEvent.setField(ContentField.REPLYTIMES);

        ContentSumIncreaseEvent csiFloorEvent = null;
        ContentInteractionSumIncreaseEvent cisiEvent = null;
        if (!StringUtil.isEmpty(reply.getRootId())) {
            cisiEvent = new ContentInteractionSumIncreaseEvent();
            cisiEvent.setContentId(reply.getContentId());
            cisiEvent.setContentUno(reply.getContentUno());
            cisiEvent.setContentInteractionId(reply.getRootId());
            cisiEvent.setCount(1);
            cisiEvent.setField(ContentInteractionField.REPLYTIMES);
        } else {
            csiFloorEvent = new ContentSumIncreaseEvent();
            csiFloorEvent.setContentId(reply.getContentId());
            csiFloorEvent.setContentUno(reply.getContentUno());
            csiFloorEvent.setCount(1);
            csiFloorEvent.setField(ContentField.FLOORTIMES);
        }

        //the content replied event
        ContentRepliedEvent contentRepliedEvent = new ContentRepliedEvent();

        contentRepliedEvent.setContentId(reply.getContentId());
        contentRepliedEvent.setContentUno(reply.getContentUno());
        contentRepliedEvent.setReplyId(reply.getInteractionId());
        contentRepliedEvent.setReplyDate(reply.getCreateDate());
        contentRepliedEvent.setReplyIp(reply.getCreateIp());
        contentRepliedEvent.setReplyUno(reply.getInteractionUno());

        try {
            EventDispatchServiceSngl.get().dispatch(myReplyEvent);
            EventDispatchServiceSngl.get().dispatch(replyMeEvent);
            EventDispatchServiceSngl.get().dispatch(replyMeRootEvent);
            EventDispatchServiceSngl.get().dispatch(replyMeParentEvent);

            EventDispatchServiceSngl.get().dispatch(csiFloorEvent);
            EventDispatchServiceSngl.get().dispatch(csiEvent);

            EventDispatchServiceSngl.get().dispatch(cisiEvent);

            EventDispatchServiceSngl.get().dispatch(contentRepliedEvent);
        } catch (Exception e) {
            //
            GAlerter.lan("ContentLogic send out two system events for postReply.", e);
        }
    }

    @Override
    public boolean removeInteraction(String interactionId, String contentId, String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleContentHandler to removeInteraction , removeInteraction  interactionId:" + interactionId);
        }

        boolean returnValue = false;
        QueryExpress getExpress = new QueryExpress();
        getExpress.add(QueryCriterions.eq(ContentInteractionField.INTERACTIONID, interactionId));

        ContentInteraction interaction = writeAbleContentHandler.getInteraction(contentId, getExpress);
        if (interaction != null) {
            if (ActStatus.ACTED.equals(interaction.getRemoveStatus())) {
                returnValue = true;
            } else {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.set(ContentInteractionField.REMOVESTATUS, ActStatus.ACTED.getCode());


                returnValue = writeAbleContentHandler.updateInteraction(contentId, updateExpress, getExpress);

                if (returnValue) {

                    if (interaction.getInteractionType().equals(InteractionType.REPLY)) {
                        sendOutRemoveReplyEvent(interaction);
                    }
                } else {
                    logger.info("Content Logic removeInteraction error.");
                }
            }
        } else {
            logger.info("Content Logic removeInteraction to remove reply, found a null content.");
        }


        return returnValue;
    }

    private void sendOutRemoveReplyEvent(ContentInteraction interaction) {
        //send out timeline event to timeline server.
        //for my reply timeline
        TimeLineRemoveEvent myReplyEvent = new TimeLineRemoveEvent();

        myReplyEvent.setOwnUno(interaction.getInteractionUno());
        myReplyEvent.setDomain(TimeLineDomain.MYREPLY);
        myReplyEvent.setDirectId(interaction.getInteractionId());

        //for reply me timeline
        TimeLineRemoveEvent replyMeEvent = new TimeLineRemoveEvent();

        replyMeEvent.setOwnUno(interaction.getContentUno());
        replyMeEvent.setDomain(TimeLineDomain.REPLYME);
        replyMeEvent.setDirectId(interaction.getInteractionId());


        //for reply me timeline  parent
        TimeLineRemoveEvent replyMeParentEvent = new TimeLineRemoveEvent();

        replyMeParentEvent.setOwnUno(interaction.getParentUno());
        replyMeParentEvent.setDomain(TimeLineDomain.REPLYME);
        replyMeParentEvent.setDirectId(interaction.getInteractionId());

        TimeLineRemoveEvent replyMeRootEvent = new TimeLineRemoveEvent();

        replyMeRootEvent.setOwnUno(interaction.getRootUno());
        replyMeRootEvent.setDomain(TimeLineDomain.REPLYME);
        replyMeRootEvent.setDirectId(interaction.getInteractionId());


        //for the content sum
        int reduceReplyTimes = -1;
        if (StringUtil.isEmpty(interaction.getRootId())) {
            reduceReplyTimes = reduceReplyTimes - interaction.getReplyTimes();
        }
        ContentSumIncreaseEvent csiEvent = new ContentSumIncreaseEvent();
        csiEvent.setContentId(interaction.getContentId());
        csiEvent.setContentUno(interaction.getContentUno());
        csiEvent.setCount(reduceReplyTimes);
        csiEvent.setField(ContentField.REPLYTIMES);

        //主楼 楼数-1
        ContentSumIncreaseEvent csiFloorEvent = null;
        if (StringUtil.isEmpty(interaction.getRootId())) {
            csiFloorEvent = new ContentSumIncreaseEvent();
            csiFloorEvent.setContentId(interaction.getContentId());
            csiFloorEvent.setContentUno(interaction.getContentUno());
            csiFloorEvent.setCount(-1);
            csiFloorEvent.setField(ContentField.FLOORTIMES);
        }

        //楼中楼 主楼-1
        ContentInteractionSumIncreaseEvent ciciEvent = null;
        if (!StringUtil.isEmpty(interaction.getRootId())) {
            ciciEvent = new ContentInteractionSumIncreaseEvent();
            ciciEvent.setContentId(interaction.getContentId());
            ciciEvent.setContentUno(interaction.getContentUno());
            ciciEvent.setContentInteractionId(interaction.getRootId());
            ciciEvent.setCount(-1);
            ciciEvent.setField(ContentInteractionField.REPLYTIMES);
        }

        try {
            EventDispatchServiceSngl.get().dispatch(myReplyEvent);
            EventDispatchServiceSngl.get().dispatch(replyMeEvent);
            EventDispatchServiceSngl.get().dispatch(replyMeParentEvent);
            EventDispatchServiceSngl.get().dispatch(replyMeRootEvent);
            EventDispatchServiceSngl.get().dispatch(csiEvent);

            EventDispatchServiceSngl.get().dispatch(csiFloorEvent);

            EventDispatchServiceSngl.get().dispatch(ciciEvent);

        } catch (Exception e) {
            //
            GAlerter.lan("ContentLogic send out two system events for postReply.", e);
        }
    }

    @Override
    public ContentInteraction getInteractionByIidCidUno(String interactionId, String contentId, String contentUno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyContentHandlersPool to ContentReply , getReplyByRidCidUno  interactionId:" + interactionId);
        }

        QueryExpress getExpress = new QueryExpress();
        getExpress.add(QueryCriterions.eq(ContentInteractionField.INTERACTIONID, interactionId));
        ContentInteraction interaction = writeAbleContentHandler.getInteraction(contentId, getExpress);

        return interaction;
    }

    @Override
    public List<ContentInteraction> queryInteractionByCidIUno(String interactionUno, String contentId, InteractionType type) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyContentHandlersPool queryInteractionByCidIUno interactionUno:" + interactionUno + " contentId:" + contentId + " type:" + type);
        }

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ContentInteractionField.INTERACTIONUNO, interactionUno));
        queryExpress.add(QueryCriterions.eq(ContentInteractionField.CONTENTID, contentId));
        queryExpress.add(QueryCriterions.eq(ContentInteractionField.REMOVESTATUS, ActStatus.UNACT.getCode()));
        queryExpress.add(QueryCriterions.eq(ContentInteractionField.INTERACTIONTYPE, type.getCode()));
        List<ContentInteraction> interaction = writeAbleContentHandler.queryInteraction(contentId, queryExpress);

        return interaction;
    }

    @Override
    public Map<String, ContentInteraction> queryInteractionsMapByRids(ContentInteractionQueryMap queryMap) throws ServiceException {
        return null;
    }

    @Override
    public PageRows<ContentInteraction> queryInteractionsByCidType(String contentId, String contentUno, InteractionType interactionType, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyContentHandlersPool to queryInteractionsByCid, contentId:" + contentId);
        }

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ContentInteractionField.CONTENTID, contentId));
        queryExpress.add(QueryCriterions.eq(ContentInteractionField.REMOVESTATUS, ActStatus.UNACT.getCode()));
        queryExpress.add(QueryCriterions.eq(ContentInteractionField.INTERACTIONTYPE, interactionType.getCode()));
        queryExpress.add(QuerySort.add(ContentInteractionField.CREATEDATE, QuerySortOrder.DESC));
        return readonlyContentHandlersPool.getHandler().queryInteractionsByPage(contentId, queryExpress, page);
    }

    @Override
    public PageRows<ContentInteraction> queryCurrentInteractionsByInteractionIdCidType(String interactionId, String contentId, String contentUno, QueryExpress queryExpress, int pageSize) throws ServiceException {

        ContentInteraction interaction = getInteractionByIidCidUno(interactionId, contentId, contentUno);
        if (interaction == null) {
            return null;
        }
        if (!StringUtil.isEmpty(interaction.getRootId())) {
            interaction = getInteractionByIidCidUno(interaction.getRootId(), contentId, contentUno);
        }
        if (interaction == null) {
            return null;
        }

        QueryExpress currentFloorNumExpress = new QueryExpress();
        currentFloorNumExpress.add(QueryCriterions.eq(ContentInteractionField.CONTENTID, interaction.getContentId()));
        currentFloorNumExpress.add(QueryCriterions.leq(ContentInteractionField.FLOORNUM, interaction.getFloorNo()));
        currentFloorNumExpress.add(QueryCriterions.eq(ContentInteractionField.REMOVESTATUS, ActStatus.UNACT.getCode()));
        int currentFloorNum = readonlyContentHandlersPool.getHandler().countByQueryExpress(contentId, currentFloorNumExpress);
        Pagination p = new Pagination(currentFloorNum, currentFloorNum, pageSize);

        QueryExpress toltalFloorExpress = new QueryExpress();
        toltalFloorExpress.add(QueryCriterions.eq(ContentInteractionField.CONTENTID, interaction.getContentId()));
        toltalFloorExpress.add(QueryCriterions.eq(ContentInteractionField.REMOVESTATUS, ActStatus.UNACT.getCode()));
        toltalFloorExpress.add(QueryCriterions.eq(ContentInteractionField.INTERACTIONTYPE, InteractionType.REPLY.getCode()));

        int totalFloorNum = readonlyContentHandlersPool.getHandler().countByQueryExpress(contentId, toltalFloorExpress);
        p.setTotalRows(totalFloorNum);

        return queryInteractionsByExpress(contentId, contentUno, queryExpress, p);
    }

    @Override
    public PageRows<ContentInteraction> queryCurrentChildrenInteractionsByChildrenInteractionIdCidType(String interactionId, String contentId, String contentUno, QuerySortOrder querySortOrder, int pageSize) throws ServiceException {
        ContentInteraction interaction = getInteractionByIidCidUno(interactionId, contentId, contentUno);
        if (interaction == null || StringUtil.isEmpty(interaction.getRootId())) {
            return null;
        }

        QueryExpress currentChildrenNumExpress = new QueryExpress();
        currentChildrenNumExpress.add(QueryCriterions.eq(ContentInteractionField.ROOTID, interaction.getRootId()));
        currentChildrenNumExpress.add(QueryCriterions.leq(ContentInteractionField.FLOORNUM, interaction.getFloorNo()));
        currentChildrenNumExpress.add(QueryCriterions.eq(ContentInteractionField.REMOVESTATUS, ActStatus.UNACT.getCode()));
        int currentChildrenNum = readonlyContentHandlersPool.getHandler().countByQueryExpress(contentId, currentChildrenNumExpress);
        Pagination p = new Pagination(currentChildrenNum, currentChildrenNum, pageSize);

        QueryExpress toltalFloorExpress = new QueryExpress();
        toltalFloorExpress.add(QueryCriterions.eq(ContentInteractionField.ROOTID, interaction.getRootId()));
        toltalFloorExpress.add(QueryCriterions.eq(ContentInteractionField.REMOVESTATUS, ActStatus.UNACT.getCode()));
        toltalFloorExpress.add(QueryCriterions.eq(ContentInteractionField.INTERACTIONTYPE, InteractionType.REPLY.getCode()));

        int totalFloorNum = readonlyContentHandlersPool.getHandler().countByQueryExpress(contentId, toltalFloorExpress);
        p.setTotalRows(totalFloorNum);

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ContentInteractionField.CONTENTID, contentId));
        queryExpress.add(QueryCriterions.eq(ContentInteractionField.CONTENTUNO, contentUno));
        queryExpress.add(QueryCriterions.eq(ContentInteractionField.ROOTID, interaction.getRootId()));
        queryExpress.add(QueryCriterions.eq(ContentInteractionField.INTERACTIONTYPE, InteractionType.REPLY.getCode()));
        queryExpress.add(QueryCriterions.eq(ContentInteractionField.REMOVESTATUS, ActStatus.UNACT.getCode()));
        queryExpress.add(QuerySort.add(ContentInteractionField.CREATEDATE, querySortOrder));

        return queryInteractionsByExpress(contentId, contentUno, queryExpress, p);
    }

    @Override
    public PageRows<ContentInteraction> queryInteractionsByCid(String contentId, String contentUno, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyContentHandlersPool to queryInteractionsByCid, contentId:" + contentId);
        }

        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ContentInteractionField.CONTENTID, contentId));
        queryExpress.add(QueryCriterions.eq(ContentInteractionField.REMOVESTATUS, ActStatus.UNACT.getCode()));
        queryExpress.add(QuerySort.add(ContentInteractionField.CREATEDATE, QuerySortOrder.DESC));
        return readonlyContentHandlersPool.getHandler().queryInteractionsByPage(contentId, queryExpress, page);
    }

    @Override
    public PageRows<ContentInteraction> queryInteractionsByExpress(String contentId, String contentUno, QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyContentHandlersPool to queryInteractionsByExpress, contentId:" + contentId + " queryExpress:" + queryExpress);
        }

        return readonlyContentHandlersPool.getHandler().queryInteractionsByPage(contentId, queryExpress, page);
    }

    public List<ContentInteraction> queryInteractionsByCidRidsMap(Map<String, List<String>> cidIidsMap) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyContentHandlersPool to queryInteractionsByCidRidsMap, cidIidsMap:" + cidIidsMap);
        }

        List<ContentInteraction> returnValue = new ArrayList<ContentInteraction>();

        for (Map.Entry<String, List<String>> entry : cidIidsMap.entrySet()) {
            returnValue.addAll(readonlyContentHandlersPool.getHandler().queryInteractions(entry.getValue(), entry.getKey()));
        }

        return returnValue;
    }

    ////////////////////////////////////////////////////////////////
    //private methods
    private void initWall() {
        int wallLayoutTotalDisplaySize = HotdeployConfigFactory.get().getConfig(WallHotdeployConfig.class).getWallLayoutTotalDisplaySize();

        Map<Integer, WallLayout> tmpWallLayoutMap = null;
        tmpWallLayoutMap = initWallLayoutMap(wallLayoutTotalDisplaySize);

        if (!CollectionUtil.isEmpty(tmpWallLayoutMap)) {
            synchronized (wallLayoutMapCache) {
                // init.
                if (!CollectionUtil.isEmpty(wallLayoutMapCache)) {
                    // reload init
                    int mapSize = currentMapSize;
                    for (int i : tmpWallLayoutMap.keySet()) {
                        wallLayoutMapCache.put(++mapSize, tmpWallLayoutMap.get(i));
                        wallLayoutMapCache.remove(mapSize - wallLayoutTotalDisplaySize);
                    }
                    currentMapSize = mapSize;
                } else {
                    // first init
                    wallLayoutMapCache = tmpWallLayoutMap;
                    currentMapSize = tmpWallLayoutMap.size();
                }
            }
        }
    }

    private Map<Integer, WallLayout> initWallLayoutMap(int wallLayoutTotalDisplaySize) {
        Date date = null;
        List<Content> contents = null;
        Pagination page = new Pagination();
        page.setPageSize(config.getWallContentSize());
//        int serviceIdx = Integer.parseInt(serviceName.split(ContentConstants.SERVICE_SECTION)[1]);

        Map<Integer, DiscoveryWallContent> wallContentMap = new HashMap<Integer, DiscoveryWallContent>();
        Map<Integer, WallLayout> layoutMap = new LinkedHashMap<Integer, WallLayout>();

        if (currentDate == null) {
            int repeatIndex = 0;
            while (layoutMap.size() < wallLayoutTotalDisplaySize && repeatIndex < 10) {
                contents = queryWallContents(page, QuerySortOrder.DESC);

                wallContentMap = wallContentProcessor.filterContents(contents, wallContentMap);
                if (date == null && wallContentMap.size() > 0) {
                    date = wallContentMap.get(1).getPublishDate();
                }

                layoutMap = wallContentProcessor.initWallLayout(wallContentMap, layoutMap, currentMapSize, wallLayoutTotalDisplaySize);

                page.setCurPage(page.getCurPage() + 1);
                repeatIndex++;

                if (logger.isDebugEnabled()) {
                    logger.debug("wall reload layoutMap size : " + layoutMap.size());
                }
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("wall reload start date : " + DateUtil.formatDateToString(currentDate, DateUtil.PATTERN_DATE_TIME));
            }

            contents = queryWallContents(page, QuerySortOrder.ASC);
            wallContentMap = wallContentProcessor.filterContents(contents, wallContentMap);
            layoutMap = wallContentProcessor.reloadWallLayout(wallContentMap, layoutMap, currentMapSize);

            if (logger.isDebugEnabled()) {
                logger.debug("wall reload content size : " + wallContentMap.size());
            }

            // reload date
            if (layoutMap.size() > 0) {
                WallLayout wallLayout = layoutMap.get(layoutMap.size());
                WallBlock wallBlock = wallLayout.getWallBlockList().get(wallLayout.getWallBlockList().size() - 1);
                date = wallBlock.getDiscoveryWallContent().getPublishDate();
            }

            if (logger.isDebugEnabled()) {
                logger.debug("wall reload layoutMap size : " + layoutMap.size());
            }

            //refresh old layoutMap
            wallContentMap = new HashMap<Integer, DiscoveryWallContent>();
            Map<Integer, WallLayout> oldLayoutMap = new LinkedHashMap<Integer, WallLayout>();
            if (layoutMap.size() < wallLayoutTotalDisplaySize) {
                //set the currentDate == null   first init
                int repeatIndex = 0;
                reloadMapSize = currentMapSize + layoutMap.size() - wallLayoutTotalDisplaySize;
                while (oldLayoutMap.size() < wallLayoutTotalDisplaySize - layoutMap.size() && repeatIndex < 10) {
                    contents = queryWallContentsByDate(page, QuerySortOrder.DESC, currentDate);

                    wallContentMap = wallContentProcessor.filterContents(contents, wallContentMap);
                    oldLayoutMap = wallContentProcessor.initWallLayout(wallContentMap, oldLayoutMap, reloadMapSize, wallLayoutTotalDisplaySize - layoutMap.size());

                    page.setCurPage(page.getCurPage() + 1);
                    repeatIndex++;

                    if (logger.isDebugEnabled()) {
                        logger.debug("wall reload oldLayoutMap size : " + oldLayoutMap.size());
                    }
                }
                if (oldLayoutMap.size() > 0) {
                    synchronized (wallLayoutMapCache) {
                        // init.
                        if (!CollectionUtil.isEmpty(wallLayoutMapCache)) {
                            // reload init
                            int idx = 0;
                            for (int i = oldLayoutMap.size(); i > 0; i--) {
                                if (oldLayoutMap.get(i) != null) {
                                    wallLayoutMapCache.put(currentMapSize - idx, oldLayoutMap.get(i));
                                    idx++;
                                }
                            }
                        }
                    }
                }
            }

        }
        if (date != null) {
            currentDate = date;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("wall reload end date : " + DateUtil.formatDateToString(currentDate, DateUtil.PATTERN_DATE_TIME));
        }

        return layoutMap;
    }

    private List<Content> queryWallContents(Pagination page, QuerySortOrder sortOrder) {
        List<Content> contents = null;
        try {
            QueryExpress queryExpress = new QueryExpress();

            queryExpress.add(QueryCriterions.eq(ContentField.PUBLISHTYPE, ContentPublishType.ORIGINAL.getCode()));
            queryExpress.add(QueryCriterions.eq(ContentField.REMOVESTATUS, ActStatus.UNACT.getCode()));

            queryExpress.add(QueryCriterions.bitwiseAnd(ContentField.CONTENTTYPE, QueryCriterionRelation.GT, ContentType.getWallContentValue(), 0));
            queryExpress.add(QueryCriterions.bitwiseAnd(ContentField.AUDITSTATUS, QueryCriterionRelation.EQ, ContentAuditStatus.ILLEGAL_TEXT, 0));
            if (currentDate != null) {
                queryExpress.add(QueryCriterions.gt(ContentField.PUBLISHDATE, currentDate));
            }

            queryExpress.add(QuerySort.add(ContentField.PUBLISHDATE, sortOrder));

            contents = readonlyContentHandlersPool.getHandler().query(queryExpress, page);

        } catch (Exception e) {
            //
            GAlerter.lab("ContentLogic initWall call readonlyContentHandlersPool queryLastestContents error.", e);
        }
        return contents;
    }

    private List<Content> queryWallContentsByDate(Pagination page, QuerySortOrder sortOrder, Date date) {
        List<Content> contents = null;
        try {
            QueryExpress queryExpress = new QueryExpress();

            queryExpress.add(QueryCriterions.eq(ContentField.PUBLISHTYPE, ContentPublishType.ORIGINAL.getCode()));
            queryExpress.add(QueryCriterions.eq(ContentField.REMOVESTATUS, ActStatus.UNACT.getCode()));

            queryExpress.add(QueryCriterions.bitwiseAnd(ContentField.CONTENTTYPE, QueryCriterionRelation.GT, ContentType.getWallContentValue(), 0));
            queryExpress.add(QueryCriterions.bitwiseAnd(ContentField.AUDITSTATUS, QueryCriterionRelation.EQ, ContentAuditStatus.ILLEGAL_TEXT, 0));
            queryExpress.add(QueryCriterions.leq(ContentField.PUBLISHDATE, date));

            queryExpress.add(QuerySort.add(ContentField.PUBLISHDATE, sortOrder));

            contents = readonlyContentHandlersPool.getHandler().query(queryExpress, page);

        } catch (Exception e) {
            //
            GAlerter.lab("ContentLogic initWall call readonlyContentHandlersPool queryLastestContents error.", e);
        }
        return contents;
    }

    private Content getContentFromCache(String contentId) {
        return contentCache.getContent(contentId);
    }

    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        eventProcessQueueThreadN.add(event);

        return true;
    }


    class WallReloadTask extends TimerTask {
        //
        public void run() {
            if (logger.isDebugEnabled()) {
                logger.debug("WallReloadTask start to reload.");
            }

            //reload the pool.
            try {
                initWall();
            } catch (Exception e) {
                //
                GAlerter.lan("WallReloadTask occured error.", e);
            }


            if (logger.isDebugEnabled()) {
                logger.debug("WallReloadTask finish to reload.");
                logger.debug("layout currentMapSize :" + currentMapSize);
            }

        }
    }

    @Override
    public PageRows<Activity> queryActivity(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler QueryExpress:queryExpress" + queryExpress + " Pagination=" + pagination);
        }
        PageRows<Activity> rows = readonlyContentHandlersPool.getHandler().pageActivity(queryExpress, pagination);

        if (rows != null && !CollectionUtil.isEmpty(rows.getRows())) {
            List<Activity> list = buildActivityRelaitonByList(rows.getRows());
            rows.setRows(list);
        }

        return rows;
    }

    @Override
    public List<Activity> queryActivityByList(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler QueryExpress:queryExpress" + queryExpress);
        }
        List<Activity> list = readonlyContentHandlersPool.getHandler().listActivity(queryExpress);
        if (!CollectionUtil.isEmpty(list)) {
            list = buildActivityRelaitonByList(list);
        }
        return list;
    }

    private Activity buildRelationSet(Activity activity, List<ActivityRelation> relationList) {
        ActivityRelations relations = new ActivityRelations();
        relations.setActivityId(activity.getActivityId());
        for (ActivityRelation relation : relationList) {
            if (relation.getActivityType().equals(ActivityType.EXCHANGE_GOODS)) {
                relations.setExchangeGoodRelation(relation);
            } else if (relation.getActivityType().equals(ActivityType.GOODS)) {
                relations.setGoodsRelation(relation);
            }

            activity.setActivityRelations(relations);
            putActivityToCache(relation.getRelationId(), activity);
        }
        return activity;
    }


    @Override
    public Activity insertActivity(Activity activity) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler insertActivity :Activity" + activity);
        }


        return writeAbleContentHandler.insertActivity(activity);
    }

    @Override
    public Activity getActivityById(Long activityId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler getActivityById :activityId" + activityId);
        }
        Activity activity = readonlyContentHandlersPool.getHandler().getActivity(activityId);

        if (activity != null) {
            activity = buildActivityRelaitonByList(activity);
        }
        return activity;
    }

    @Override
    public boolean modifyActivity(UpdateExpress updateExpress, long activityId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler modifyActivity :updateExpress" + updateExpress);
        }
        boolean bVal = writeAbleContentHandler.modifyActivity(updateExpress, new QueryExpress().add(QueryCriterions.eq(ActivityField.ACTIVITY_ID, activityId)));
        if (bVal) {
//            activityCache.removeActivityHotRanks();
        }

        delActivityFromeCache(activityId);

        return bVal;
    }

    @Override
    public ActivityRelation insertActivityRelation(ActivityRelation activityRelation) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler insertActivityRelation :ActivityRelation" + activityRelation);
        }


        activityRelation = writeAbleContentHandler.insertActivityRelation(activityRelation);

        delActivityFromeCache(activityRelation.getActivityId());
        return activityRelation;
    }

    @Override
    public List<ActivityRelation> listActivityRelation(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler listActivityRelation :QueryExpress" + queryExpress);
        }

        return readonlyContentHandlersPool.getHandler().queryActivityRelationList(queryExpress);
    }

    @Override
    public ActivityRelation getActivityRelation(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler getActivityRelation :QueryExpress" + queryExpress);
        }

        return readonlyContentHandlersPool.getHandler().getActivityRelation(queryExpress);
    }

    @Override
    public List<Activity> queryHotActivity() throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryLastestActivity.");
        }
        //查询缓存
        List<Activity> returnList = activityCache.getLastedActivity();
        //缓存中没有 查db
        if (CollectionUtil.isEmpty(returnList)) {
            returnList = queryHotActivityByDb();
            activityCache.putLastedActivity(returnList);
        }
        return returnList;
    }


    public Map<Long, Activity> queryActivityByActivityId(Set<Long> activityId) throws ServiceException {
        Map<Long, Activity> returnMap = new LinkedHashMap<Long, Activity>();

        //setp1 queryBy memcached
        Set<Long> queryDbSet = new HashSet<Long>();
        Map<Long, Activity> memcachedMap = new HashMap<Long, Activity>();
        for (Long id : activityId) {
            Activity activity = activityCache.getActivityById(id);
            if (activity == null) {
                queryDbSet.add(id);
            } else {
                memcachedMap.put(id, activity);
            }
        }

        Map<Long, Activity> dbMap = new HashMap<Long, Activity>();
        if (!CollectionUtil.isEmpty(queryDbSet)) {
            QueryExpress activityQueryExpress = new QueryExpress();
            activityQueryExpress.add(QueryCriterions.in(ActivityField.ACTIVITY_ID, queryDbSet.toArray()));
            activityQueryExpress.add(QueryCriterions.eq(ActivityField.REMOVE_STATUS, ActStatus.ACTED.getCode()));
            List<Activity> list = buildActivityRelaitonByList(readonlyContentHandlersPool.getHandler().listActivity(activityQueryExpress));
            for (Activity activty : list) {
                dbMap.put(activty.getActivityId(), activty);
            }
        }

        //step3 merage by memcachedMap and dbMap
        for (long aId : activityId) {
            Activity activity = memcachedMap.get(aId);
            if (activity == null) {
                activity = dbMap.get(aId);
            }
            if (activity != null) {
                returnMap.put(aId, activity);
            }
        }
        return returnMap;
    }


    @Override
    public Map<Long, Activity> queryActivityByRelations(Set<Long> relationIdSet) throws ServiceException {
        Map<Long, Activity> returnMap = new LinkedHashMap<Long, Activity>();

        //setp1 queryBy memcached
        Set<Long> queryDbSet = new HashSet<Long>();
        Map<Long, Activity> memcachedMap = new HashMap<Long, Activity>();
        for (Long id : relationIdSet) {
            Activity activity = null;
            if (activity == null) {
                queryDbSet.add(id);
            } else {
                memcachedMap.put(id, activity);
            }
        }

        //step2 query by db
        Map<Long, Activity> dbMap = queryActivityByDb(queryDbSet);

        //step3 merage by memcachedMap and dbMap
        for (Long relationId : relationIdSet) {
            Activity activity = memcachedMap.get(relationId);
            if (activity == null) {
                activity = dbMap.get(relationId);

                if (activity != null) {
                    putActivityToCache(relationId, activity);
                }
            }

            returnMap.put(relationId, activity);
        }

        return returnMap;
    }

    /**
     * @param queryDbSet
     * @return Map:realtionId--->Activity
     * @throws DbException
     */
    private Map<Long, Activity> queryActivityByDb(Set<Long> queryDbSet) throws DbException {
        Map<Long, Activity> returnMap = new LinkedHashMap<Long, Activity>();

        //relationId->activityId
        Map<Long, Long> relationIdToActivityId = new LinkedHashMap<Long, Long>();
        //activity-->activityId
        Map<Long, Activity> activityIdToActivity = new HashMap<Long, Activity>();

        //build idmap reliatonmap
//        QueryCriterions[] relationInCriterions = new QueryCriterions[queryDbSet.size()];
//        int relationIdx = 0;
//        for (Long queryRealitonId : queryDbSet) {
//            relationInCriterions[relationIdx] = QueryCriterions.eq(ActivityRelationField.ACTIVITY_RELATION_ID, queryRealitonId);
//            relationIdx++;
//        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.in(ActivityRelationField.ACTIVITY_RELATION_ID, queryDbSet.toArray()));
        for (ActivityRelation activtyRelation : readonlyContentHandlersPool.getHandler().queryActivityRelationList(queryExpress)) {
            relationIdToActivityId.put(activtyRelation.getRelationId(), activtyRelation.getActivityId());
        }

        //build activitymap
        if (!CollectionUtil.isEmpty(relationIdToActivityId)) {
//            QueryCriterions[] activitryInCriterions = new QueryCriterions[relationIdToActivityId.size()];
//            int activityIdIdx = 0;
//            for (Long activityId : relationIdToActivityId.values()) {
//                relationInCriterions[activityIdIdx] = QueryCriterions.eq(ActivityRelationField.ACTIVITY_ID, activityId);
//                activityIdIdx++;
//            }
            QueryExpress activityQueryExpress = new QueryExpress();
            activityQueryExpress.add(QueryCriterions.in(ActivityRelationField.ACTIVITY_ID, relationIdToActivityId.values().toArray()));

            List<Activity> list = buildActivityRelaitonByList(readonlyContentHandlersPool.getHandler().listActivity(activityQueryExpress));
            for (Activity activty : list) {
                activityIdToActivity.put(activty.getActivityId(), activty);
            }
        }

        for (Map.Entry<Long, Long> entry : relationIdToActivityId.entrySet()) {
            Activity activity = activityIdToActivity.get(entry.getValue());
            if (activity != null) {
                returnMap.put(entry.getKey(), activity);
            }
        }
        return returnMap;
    }

    private List<Activity> queryHotActivityByDb() throws ServiceException {
        QueryExpress hotQueryExpress = new QueryExpress()
                .add(QueryCriterions.eq(ActivityField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                .add(QuerySort.add(ActivityField.EVENT_TIME, QuerySortOrder.DESC));

        Pagination pagination = new Pagination(QUERY_HOT_ACTIVITY_PAGE_SIZE, QUERY_HOT_ACTIVITY_PAGE_NO, QUERY_HOT_ACTIVITY_PAGE_SIZE);

        PageRows<Activity> pageRows = readonlyContentHandlersPool.getHandler().pageActivity(hotQueryExpress, pagination);
        List<Activity> returnList = pageRows.getRows();
        return returnList;
    }

    private List<Activity> buildActivityRelaitonByList(List<Activity> activityList) throws DbException {
        Set<Long> relatioIdSet = new HashSet<Long>();
        for (Activity activity : activityList) {
            relatioIdSet.add(activity.getActivityId());
        }


        if (!CollectionUtil.isEmpty(relatioIdSet)) {
            QueryExpress queryRelationExpress = new QueryExpress();
            queryRelationExpress.add(QueryCriterions.in(ActivityRelationField.ACTIVITY_ID, relatioIdSet.toArray()));

            List<ActivityRelation> activityRelaitonList = readonlyContentHandlersPool.getHandler().queryActivityRelationList(queryRelationExpress);
            Map<Long, List<ActivityRelation>> returnMap = new HashMap<Long, List<ActivityRelation>>();
            for (ActivityRelation activityRealtion : activityRelaitonList) {
                if (!returnMap.containsKey(activityRealtion.getActivityId())) {
                    returnMap.put(activityRealtion.getActivityId(), new ArrayList<ActivityRelation>());
                }

                returnMap.get(activityRealtion.getActivityId()).add(activityRealtion);
            }

            for (Activity activity : activityList) {
                if (returnMap.containsKey(activity.getActivityId())) {
                    activity = buildRelationSet(activity, returnMap.get(activity.getActivityId()));
                }
            }
        }

        return activityList;
    }


    private Activity buildActivityRelaitonByList(Activity activity) throws DbException {

        List<ActivityRelation> activityRelaitonList = readonlyContentHandlersPool.getHandler().
                queryActivityRelationList(new QueryExpress().add(QueryCriterions.eq(ActivityRelationField.ACTIVITY_ID, activity.getActivityId())));

        if (!CollectionUtil.isEmpty(activityRelaitonList)) {
            activity = buildRelationSet(activity, activityRelaitonList);
        }

        return activity;
    }

    private void putActivityToCache(long relationId, Activity activity) {
        activityCache.putActivity(activity);
        activityCache.putRelationidByActivityId(activity.getActivityId(), relationId);
        activityCache.putActivityByRelationId(relationId, activity.getActivityType(), activity);
    }

    private boolean delActivityFromeCache(long activityId) {
        boolean bVal = false;

        Activity activity = activityCache.getActivityById(activityId);
        if (activity == null) {
            return bVal;
        }

        activityCache.removeActivityById(activityId);
        Long relaitonId = activityCache.getRelationidByActivityId(activityId);
        if (relaitonId == null) {
            return bVal;
        }

        bVal = activityCache.removeActivityByRelationId(relaitonId, activity.getActivityType());
        if (bVal) {
            bVal = activityCache.removeRelationidByActivityId(activityId);
        }

        return bVal;
    }

    public ForignContent getForignContent(String fid, String url, String title, String content, ForignContentDomain domain, String keyWords) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getForignContent by fid.fid:" + fid);
        }
        ForignContent forignContent = readonlyContentHandlersPool.getHandler().queryForignContentById(fid, domain);

        if (forignContent == null) {
            forignContent = new ForignContent();
            forignContent.setContentUrl(url);
            forignContent.setForignId(fid);
            forignContent.setContentDomain(domain);
            forignContent.setContentTitle(title);
            forignContent.setContentDesc(content);
            forignContent.setCreateTime(new Date());
            forignContent.setKeyWords(keyWords);

            forignContent = writeAbleContentHandler.insertForignContent(forignContent);
        }
        return forignContent;
    }

    @Override
    public Map<Long, ForignContent> getForignContentBySet(Set<Long> contentIds) throws ServiceException {
        if (contentIds == null) {
            throw new ServiceException(ServiceException.INVALID_ARGUMENT);
        }
        Map<Long, ForignContent> forignContentMap = new HashMap<Long, ForignContent>();
        for (Long contentId : contentIds) {
            forignContentMap.put(contentId, getForignContentById(contentId));
        }
        return forignContentMap;
    }

    @Override
    public ForignContent getForignContentByFidCdomain(String forignId, ForignContentDomain domain) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getForignContentByFidCdomain forignId:" + forignId);
        }
        return readonlyContentHandlersPool.getHandler().queryForignContentById(forignId, domain);
    }

    @Override
    public ForignContent getForignContentById(long contentId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getForignContentById contentId:" + contentId);
        }
        ForignContent forignContent = forignConentReplyCache.getForignContent(contentId);

        if (forignContent == null) {
            forignContent = readonlyContentHandlersPool.getHandler().getForignContentById(contentId);

            if (forignContent != null) {
                forignConentReplyCache.putForignContent(forignContent);
            }
        }

        return forignContent;
    }

    @Override
    public boolean updateForignContent(UpdateExpress updateExpress, QueryExpress queryExpress, long contentId) throws ServiceException {
        boolean bval = writeAbleContentHandler.updateForignContent(updateExpress, queryExpress);
        if (bval) {
            forignConentReplyCache.deleteForignContent(contentId);
        }
        return bval;
    }

    //得到主楼的评论
    @Override
    public PageRows<ForignContentReply> queryForignReplays(long contentId, long rootId, Pagination page, boolean desc) throws ServiceException {
        PageRows<ForignContentReply> returnObj = new PageRows<ForignContentReply>();

        PageRows<Long> replyIdRows = getForignReplyId(contentId, rootId, page, desc);
        if (replyIdRows == null || CollectionUtil.isEmpty(replyIdRows.getRows())) {
            returnObj.setPage(page);
            return returnObj;
        }
        List<Long> replyIdList = replyIdRows.getRows();
        Map<Long, ForignContentReply> forignContentReplyMap = queryForignContentReplyBySet(new HashSet<Long>(replyIdList));

        List<ForignContentReply> replyList = new ArrayList<ForignContentReply>();

        for (Long replyId : replyIdList) {
            ForignContentReply contentReply = forignContentReplyMap.get(replyId);

            if (contentReply != null) {
                replyList.add(contentReply);
            }
        }

        returnObj = new PageRows<ForignContentReply>();
        returnObj.setPage(page);
        returnObj.setRows(replyList);

        return returnObj;
    }

    @Override
    public ForignContentReply postForignReply(ForignContentReply forignContentReply) throws ServiceException {
        //insert post content
        if (logger.isDebugEnabled()) {
            logger.debug("call postForignReply,reply content is:" + forignContentReply);
        }

        int times = forignConentReplyCache.getPostLimit(forignContentReply.getReplyUno());
        if (times >= 0) {
            throw new ServiceException(ContentServiceException.POST_FORIGN_CONTENTREPLY_LIMIT, "post error user has limited.");
        }


        int totalRows = -1;
        //判断rootId是否为0，如果是则是评论文章，否则是评论回复
        if (forignContentReply.getRootId() == 0) {
            ForignContent forignContent = readonlyContentHandlersPool.getHandler().getForignContentById(forignContentReply.getContentId());
            //评论的文章是否存在
            if (forignContent == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("content is does not exist,content ID is:" + forignContentReply.getContentId());
                }
                throw new ServiceException(ServiceException.INVALID_ARGUMENT);
            }
            //将评论信息插入forign_content_reply表中
            forignContentReply = writeAbleContentHandler.insertForignContentReply(forignContentReply);

            //手游排行榜,更新文章回复数信息
            if (forignContentReply.getForignContentDomain() != null && forignContentReply.getForignContentDomain().getCode().equals(ForignContentDomain.SHORT_COMMENTS.getCode())) {
                ForignContent forignContent1 = getForignContentById(forignContentReply.getContentId());
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.increase(ForignContentField.REPLY_NUM, 1);

                //有评分
                if (forignContentReply.getScore() > 0) {
                    int reply_num = forignContent1.getReplyNum() + 1;
                    double total_rows = forignContent1.getTotalRows() + forignContentReply.getScore();
                    double average_score = total_rows / (reply_num * 1.0);
                    updateExpress.increase(ForignContentField.SCOREREPLY_NUM, 1);
                    updateExpress.increase(ForignContentField.TOTAL_ROWS, Double.valueOf(forignContentReply.getScore()).intValue());
                    updateExpress.set(ForignContentField.AVERAGE_SCORE, get2Double(average_score));
                }

                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(ForignContentField.CONTENT_ID, forignContentReply.getContentId()));
                boolean bVal = writeAbleContentHandler.updateForignContent(updateExpress, queryExpress);
                if (bVal) {
                    forignConentReplyCache.deleteForignContent(forignContent.getContentId());
                }

            } else {
                UpdateExpress updateExpress = new UpdateExpress();
                updateExpress.increase(ForignContentField.REPLY_NUM, 1);
                updateExpress.increase(ForignContentField.TOTAL_ROWS, 1);
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(ForignContentField.CONTENT_ID, forignContentReply.getContentId()));
                boolean bVal = writeAbleContentHandler.updateForignContent(updateExpress, queryExpress);
                if (bVal) {
                    forignConentReplyCache.deleteForignContent(forignContent.getContentId());
                }
            }


            //回复数加一，稍后用于更新缓存中的回复数
            totalRows = forignContent.getTotalRows() + 1;
        } else {
            //评论回复，获取root回复，没有则直接返回
            ForignContentReply rootReply = getForignReply(forignContentReply.getRootId());
            if (rootReply == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("rootReply is does not exist,root ID is:" + forignContentReply.getRootId());
                }
                throw new ServiceException(ServiceException.INVALID_ARGUMENT);
            }
            forignContentReply.setRootUno(rootReply.getReplyUno());

            //get partintid
            //获取父类回复，如果没有，则设置为Root回复
            if (forignContentReply.getPartentId() > 0) {
                ForignContentReply parentReply = getForignReply(forignContentReply.getPartentId());
                if (parentReply != null) {
                    forignContentReply.setParentUno(parentReply.getReplyUno());
                }
            } else {
                forignContentReply.setPartentId(rootReply.getReplyId());
                forignContentReply.setParentUno(rootReply.getReplyUno());
            }
            //插入数据库
            forignContentReply = writeAbleContentHandler.insertForignContentReply(forignContentReply);
            //更新数据库中Root回复的回复数
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.increase(ForignContentReplyField.REPLY_SUM, 1);
            updateExpress.increase(ForignContentField.TOTAL_ROWS, 1);
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ForignContentReplyField.REPLY_ID, forignContentReply.getRootId()));
            boolean bVal = writeAbleContentHandler.updateForignContentReply(updateExpress, queryExpress);
            if (bVal) {
                forignConentReplyCache.deleteForignContentReply(forignContentReply.getRootId());
            }

            //如果父类ID不等于RootID，更新数据库中父类回复的回复数
            if (!(forignContentReply.getRootId() == forignContentReply.getPartentId())) {
                UpdateExpress replyUpdateExpress = new UpdateExpress();
                replyUpdateExpress.increase(ForignContentReplyField.REPLY_SUM, 1);
                replyUpdateExpress.increase(ForignContentField.TOTAL_ROWS, 1);
                QueryExpress replyQueryExpress = new QueryExpress();
                replyQueryExpress.add(QueryCriterions.eq(ForignContentReplyField.REPLY_ID, forignContentReply.getPartentId()));
                boolean bReplyVal = writeAbleContentHandler.updateForignContentReply(replyUpdateExpress, replyQueryExpress);
                if (bReplyVal) {
                    forignConentReplyCache.deleteForignContentReply(forignContentReply.getPartentId());
                }
            }

            totalRows = rootReply.getTotalRows() + 1;
        }

        //update cache
        //更新缓存中的回复数
        updateForingReplyListCache(forignContentReply, totalRows);

        //sendout event
        //发送Timeline事件，向TimeLine表中写入数据
        if (forignContentReply.getForignContentDomain() != null && !forignContentReply.getForignContentDomain().getCode().equals(ForignContentDomain.SHORT_COMMENTS.getCode())) {
            sendOutForignReplyEvent(forignContentReply);
        }

        forignConentReplyCache.putPostLimit(forignContentReply.getReplyUno(), 1);
        return forignContentReply;
    }

    private void updateForingReplyListCache(ForignContentReply forignContentReply, int totalRows) {
        Pagination pagination = new Pagination(totalRows, 1, CONTENT_REPLY_PAGESIZE);
        List<Long> replyIdList = forignConentReplyCache.getContentReplyIdList(forignContentReply.getContentId(), forignContentReply.getRootId(), pagination.getMaxPage());
        if (replyIdList != null) {
            replyIdList.add(forignContentReply.getReplyId());
            Collections.sort(replyIdList, new Comparator<Long>() {
                @Override
                public int compare(Long o1, Long o2) {
                    return o1 > o2 ? 1 : (o1 == o2 ? 0 : -1);
                }
            });
            forignConentReplyCache.putContentReplyIdList(forignContentReply.getContentId(), forignContentReply.getRootId(), pagination.getMaxPage(), replyIdList);
        }
    }

    private void sendOutForignReplyEvent(ForignContentReply forignContentReply) {
        //myreply event
        //在评论人时间线中插入事件
        sendReplyTimeLineInsertEvent(forignContentReply, TimeLineDomain.MYREPLY, forignContentReply.getReplyUno());
        //如果有父类的回复且父类回复评论人不等于评论人时，向父类时间线中插入事件
        if (!forignContentReply.getReplyUno().equals(forignContentReply.getParentUno())) {
            //replyme event

            if (!StringUtil.isEmpty(forignContentReply.getParentUno())) {
                sendReplyTimeLineInsertEvent(forignContentReply, TimeLineDomain.REPLYME, forignContentReply.getParentUno());
            }
            //如果有Root回复，且Root回复人不等于父类回复人则向Root回复人中插入事件
            if (!StringUtil.isEmpty(forignContentReply.getRootUno()) && !(forignContentReply.getRootUno().equals(forignContentReply.getParentUno()))) {
                sendReplyTimeLineInsertEvent(forignContentReply, TimeLineDomain.REPLYME, forignContentReply.getRootUno());
            }
        }
    }

    private void sendReplyTimeLineInsertEvent(ForignContentReply forignContentReply, TimeLineDomain domain, String ownUno) {
        TimeLineInsertEvent myreply = new TimeLineInsertEvent();
        if (forignContentReply.getRootId() > 0) {
            myreply.setRootId(Long.toString(forignContentReply.getRootId()));
            myreply.setRootUno(forignContentReply.getRootUno());
        }
        myreply.setDomain(domain);
        myreply.setType(TimeLineContentType.REPLY);
        myreply.setDirectId(Long.toString(forignContentReply.getReplyId()));
        myreply.setDirectUno(forignContentReply.getReplyUno());
        myreply.setOwnUno(ownUno);
        myreply.setParentId(Long.toString(forignContentReply.getPartentId()));
        myreply.setParentUno(forignContentReply.getParentUno());
        myreply.setRelationId(Long.toString(forignContentReply.getContentId()));
        myreply.setSource(CommentType.FOREIGNSOURCE);
        myreply.setTimeLineDate(forignContentReply.getCreateTime() == null ? forignContentReply.getCreateTime() : new Date());
        try {
            EventDispatchServiceSngl.get().dispatch(myreply);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
        }
    }

    @Override
    public boolean modifyForignReplyById(UpdateExpress updateExpress, long replyId, ForignContentReplyLog replyLog) throws ServiceException {
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ForignContentReplyField.REPLY_ID, replyId));
        boolean bReplyVal = writeAbleContentHandler.updateForignContentReply(updateExpress, queryExpress);
        if (bReplyVal) {
            //log
            writeAbleContentHandler.createForignContentReplyLog(replyLog);

            forignConentReplyCache.deleteForignContentReply(replyId);
            return true;
        }
        return false;
    }

    @Override
    public List<ForignContentReply> queryHotForignReply(long contentId, int size) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryHotForignReply.contentId:" + contentId);
        }
        List<ForignContentReply> replyList = new ArrayList<ForignContentReply>();

        List<Long> replyIdList = forignConentReplyCache.getHotReplyList(contentId);
        if (CollectionUtil.isEmpty(replyIdList)) {
            replyIdList = readonlyContentHandlersPool.getHandler().queryHotForignReplyId(contentId, size);

            if (!CollectionUtil.isEmpty(replyIdList)) {
                forignConentReplyCache.putHotReplyList(contentId, replyIdList);
            }
        }

        Map<Long, ForignContentReply> forignContentReplyMap = queryForignContentReplyBySet(new HashSet<Long>(replyIdList));
        for (Long replyId : replyIdList) {
            ForignContentReply contentReply = forignContentReplyMap.get(replyId);

            if (contentReply != null) {
                replyList.add(contentReply);
            }
        }

        return replyList;
    }

    @Override
    public List<ForignContentReply> queryMobileGameGagForignReply(long contentId, int size) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryMobileGameGagForignReply.contentId:" + contentId);
        }
        List<ForignContentReply> returnList = forignConentReplyCache.getMobileGameGagReplyList(contentId);

        if (returnList == null) {
            returnList = new ArrayList<ForignContentReply>();
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ForignContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            queryExpress.add(QuerySort.add(ForignContentReplyField.DISPLAY_ORDER, QuerySortOrder.ASC));
            queryExpress.add(QueryCriterions.eq(ForignContentReplyField.CONTENT_ID, contentId));
            queryExpress.add(QueryCriterions.lt(ForignContentReplyField.DISPLAY_ORDER, 0L));
            List<ForignContentReply> list = readonlyContentHandlersPool.getHandler().queryForignContentReply(queryExpress);
            if (!CollectionUtil.isEmpty(list)) {
                returnList.addAll(list);
            }

            QueryExpress queryExpress2 = new QueryExpress();
            queryExpress2.add(QueryCriterions.eq(ForignContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            queryExpress2.add(QuerySort.add(ForignContentReplyField.DISPLAY_ORDER, QuerySortOrder.ASC));
            queryExpress2.add(QueryCriterions.eq(ForignContentReplyField.CONTENT_ID, contentId));
            queryExpress2.add(QueryCriterions.gt(ForignContentReplyField.DISPLAY_ORDER, 0L));
            int pageSize = 100;
            PageRows<ForignContentReply> list2 = readonlyContentHandlersPool.getHandler().queryForignContentReplyByPage(queryExpress2, new Pagination(pageSize, 1, pageSize));
            if (!CollectionUtil.isEmpty(list2.getRows())) {
                returnList.addAll(RandomUtil.getRandomByList(list2.getRows(), (size - list.size())));
            }
            if (returnList.size() > 0) {
                forignConentReplyCache.putMobileGameGagReplyList(contentId, returnList);
            }
        }
        return returnList;
    }

    @Override
    public boolean removeForignReply(long replyId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " removeForignReply.replyId:" + replyId);
        }

        ForignContentReply forignContentReply = getForignContentReply(replyId);
        if (forignContentReply == null) {
            return false;
        }

        if (forignContentReply.getRootId() == 0) {
            writeAbleContentHandler.updateForignContent(new UpdateExpress().increase(ForignContentField.REPLY_NUM, -1),
                    new QueryExpress().add(QueryCriterions.eq(ForignContentField.CONTENT_ID, forignContentReply.getContentId())));
            forignConentReplyCache.deleteForignContent(forignContentReply.getContentId());
        } else {
            writeAbleContentHandler.updateForignContentReply(new UpdateExpress().increase(ForignContentReplyField.REPLY_SUM, -1),
                    new QueryExpress().add(QueryCriterions.eq(ForignContentReplyField.REPLY_ID, forignContentReply.getRootId())));
        }

        if (forignContentReply.getPartentId() != forignContentReply.getRootId()) {
            writeAbleContentHandler.updateForignContentReply(new UpdateExpress().increase(ForignContentReplyField.REPLY_SUM, -1),
                    new QueryExpress().add(QueryCriterions.eq(ForignContentReplyField.REPLY_ID, forignContentReply.getPartentId())));
        }

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.set(ForignContentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode());
        boolean result = writeAbleContentHandler.updateForignContentReply(updateExpress, new QueryExpress().add(QueryCriterions.eq(ForignContentReplyField.REPLY_ID, replyId)));

        if (result) {
            forignConentReplyCache.deleteForignContentReply(replyId);
        }
        sendOutRemoveForignReplyEvent(forignContentReply);
        return result;
    }

    private void sendOutRemoveForignReplyEvent(ForignContentReply reply) {
        //send out timeline event to timeline server.
        //for my reply timeline
        TimeLineRemoveEvent myReplyEvent = new TimeLineRemoveEvent();

        myReplyEvent.setOwnUno(reply.getReplyUno());
        myReplyEvent.setDomain(TimeLineDomain.MYREPLY);
        myReplyEvent.setDirectId(String.valueOf(reply.getReplyId()));

        //for reply me timeline
        TimeLineRemoveEvent replyMeEvent = new TimeLineRemoveEvent();

        replyMeEvent.setOwnUno(reply.getRootUno());
        replyMeEvent.setDomain(TimeLineDomain.REPLYME);
        replyMeEvent.setDirectId(String.valueOf(reply.getReplyId()));


        //for reply me timeline  parent
        TimeLineRemoveEvent replyMeParentEvent = new TimeLineRemoveEvent();

        replyMeParentEvent.setOwnUno(reply.getParentUno());
        replyMeParentEvent.setDomain(TimeLineDomain.REPLYME);
        replyMeParentEvent.setDirectId(String.valueOf(reply.getReplyId()));

        try {
            EventDispatchServiceSngl.get().dispatch(myReplyEvent);
            EventDispatchServiceSngl.get().dispatch(replyMeEvent);
            EventDispatchServiceSngl.get().dispatch(replyMeParentEvent);
        } catch (Exception e) {
            //
            GAlerter.lan("ContentLogic send out two system events for postReply.", e);
        }
    }

    @Override
    public PageRows<ForignContentReply> queryForignContentReplyByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryForignContentReplay.queryExpress:" + queryExpress + ",pagination:" + pagination);
        }
        return readonlyContentHandlersPool.getHandler().queryForignContentReplyByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyForignContentReply(Long replayId, UpdateExpress updateExpress, Long contentid, ForignContentDomain forignContentDomain) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifyForignContentReplay.replayId:" + replayId + ",updateExpress:" + updateExpress);
        }

        ForignContentReply reply = getForignContentReply(replayId);

        QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(ForignContentReplyField.REPLY_ID, replayId));
        boolean bool = writeAbleContentHandler.modifyForignContentReplay(updateExpress, queryExpress);
        if (bool) {
            forignConentReplyCache.deleteForignContentReply(replayId);
            if (contentid != null) {
                forignConentReplyCache.deleteMobileGameGagReplyList(contentid);
            }
        }

        Object object = updateExpress.getUpdateValueByField(ForignContentReplyField.REMOVE_STATUS);
        if (object != null) {
            String status = (String) object;

            //手游排行榜短评
            if (forignContentDomain != null && forignContentDomain.getCode().equals(ForignContentDomain.SHORT_COMMENTS.getCode())) {
                ForignContent forignContent = getForignContentById(contentid);
                UpdateExpress up = new UpdateExpress();
                if (ActStatus.getByCode(status).equals(ActStatus.UNACT)) {
                    up.increase(ForignContentField.REPLY_NUM, 1);
                    if (reply.getScore() > 0) {
                        up.increase(ForignContentField.SCOREREPLY_NUM, 1);
                        up.increase(ForignContentField.TOTAL_ROWS, Double.valueOf(reply.getScore()).intValue());
                        double average_score = (forignContent.getTotalRows() + reply.getScore()) / ((forignContent.getScorereply_num() + 1) * 1.0);
                        up.set(ForignContentField.AVERAGE_SCORE, get2Double(average_score));
                    }
                } else {
                    up.increase(ForignContentField.REPLY_NUM, -1);
                    if (reply.getScore() > 0) {
                        up.increase(ForignContentField.SCOREREPLY_NUM, -1);
                        up.increase(ForignContentField.TOTAL_ROWS, -(Double.valueOf(reply.getScore()).intValue()));
                        double average_score = (forignContent.getTotalRows() - reply.getScore()) / ((forignContent.getScorereply_num() - 1) * 1.0);
                        up.set(ForignContentField.AVERAGE_SCORE, get2Double(average_score));
                    }
                }
                writeAbleContentHandler.updateForignContent(up,
                        new QueryExpress().add(QueryCriterions.eq(ForignContentField.CONTENT_ID, reply.getContentId())));
                forignConentReplyCache.deleteForignContent(reply.getContentId());

            } else {
                if (ActStatus.getByCode(status).equals(ActStatus.UNACT)) {
                    if (reply.getRootId() == 0) {
                        writeAbleContentHandler.updateForignContent(new UpdateExpress().increase(ForignContentField.REPLY_NUM, 1),
                                new QueryExpress().add(QueryCriterions.eq(ForignContentField.CONTENT_ID, reply.getContentId())));
                        forignConentReplyCache.deleteForignContent(reply.getContentId());
                    } else {
                        writeAbleContentHandler.updateForignContentReply(new UpdateExpress().increase(ForignContentReplyField.REPLY_SUM, 1),
                                new QueryExpress().add(QueryCriterions.eq(ForignContentReplyField.REPLY_ID, reply.getRootId())));
                    }

                    if (reply.getPartentId() != reply.getRootId()) {
                        writeAbleContentHandler.updateForignContentReply(new UpdateExpress().increase(ForignContentReplyField.REPLY_SUM, 1),
                                new QueryExpress().add(QueryCriterions.eq(ForignContentReplyField.REPLY_ID, reply.getPartentId())));
                    }
                    sendOutForignReplyEvent(reply);
                } else {
                    if (reply.getRootId() == 0) {
                        writeAbleContentHandler.updateForignContent(new UpdateExpress().increase(ForignContentField.REPLY_NUM, -1),
                                new QueryExpress().add(QueryCriterions.eq(ForignContentField.CONTENT_ID, reply.getContentId())));
                        forignConentReplyCache.deleteForignContent(reply.getContentId());
                    } else {
                        writeAbleContentHandler.updateForignContentReply(new UpdateExpress().increase(ForignContentReplyField.REPLY_SUM, -1),
                                new QueryExpress().add(QueryCriterions.eq(ForignContentReplyField.REPLY_ID, reply.getRootId())));
                    }

                    if (reply.getPartentId() != reply.getRootId()) {
                        writeAbleContentHandler.updateForignContentReply(new UpdateExpress().increase(ForignContentReplyField.REPLY_SUM, -1),
                                new QueryExpress().add(QueryCriterions.eq(ForignContentReplyField.REPLY_ID, reply.getPartentId())));
                    }
                    sendOutRemoveForignReplyEvent(reply);
                }
            }
        }

        return bool;
    }

    @Override
    public ForignContentReply getForignContentReply(Long replyId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getForignContentReply.replyId:" + replyId);
        }
        ForignContentReply returnObj = forignConentReplyCache.getForignContentReply(replyId);
        if (returnObj == null) {
            returnObj = readonlyContentHandlersPool.getHandler().getForignContentReplyById(replyId);
            if (returnObj != null && returnObj.getRemoveStatus().equals(ActStatus.UNACT)) {
                forignConentReplyCache.putForignContentReply(returnObj);
            }
        }
        return returnObj;
    }

    @Override
    public List<ForignContentReply> queryForignContentReply(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryForignContentReplay.queryExpress:" + queryExpress);
        }
        return readonlyContentHandlersPool.getHandler().queryForignContentReply(queryExpress);
    }

    //从缓存读取该页的评论IDLIST
    private PageRows<Long> getForignReplyId(long contentId, long rootId, Pagination page, boolean desc) throws DbException {
        PageRows<Long> pageRows = new PageRows<Long>();
        List<Long> replyList = new ArrayList<Long>();

        //get content by contentid get totalNums
        int fetchCacheTimes = page.getPageSize() / CONTENT_REPLY_PAGESIZE;

        for (int times = 0; times < fetchCacheTimes; times++) {
            //得到分页的参数 第N页就从缓存取 总页数-N页*times+1,根据外面传入的pageSize可以得到需要从缓存取多少次
            Pagination pageByCache = new Pagination(page.getTotalRows(), 1, CONTENT_REPLY_PAGESIZE);
            if (desc) {
                pageByCache.setCurPage(pageByCache.getMaxPage() - (page.getCurPage() - 1) * fetchCacheTimes - times);
            } else {
                pageByCache.setCurPage((page.getCurPage() - 1) * fetchCacheTimes + 1 + times);
            }


            //get by cache
            List<Long> replyIdList = forignConentReplyCache.getContentReplyIdList(contentId, rootId, pageByCache.getCurPage());

            //get by db
            if (CollectionUtil.isEmpty(replyIdList)) {
                PageRows<ForignContentReply> dbRows = readonlyContentHandlersPool.getHandler().queryContentReplyList(contentId, rootId, pageByCache);
                List<ForignContentReply> dbList = dbRows.getRows();
                replyIdList = new ArrayList<Long>();
                for (int i = dbList.size() - 1; i >= 0; i--) {
                    replyIdList.add(dbList.get(i).getReplyId());
                }
                forignConentReplyCache.putContentReplyIdList(contentId, rootId, pageByCache.getCurPage(), replyIdList);
            }
            replyList.addAll(replyIdList);

            if ((desc && pageByCache.getCurPage() <= 1) || (!desc && pageByCache.getCurPage() >= pageByCache.getMaxPage())) {
                break;
            }
        }

        Collections.sort(replyList, new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return o1 < o2 ? 1 : (o1 == o2 ? 0 : -1);
            }
        });
        pageRows.setPage(page);
        pageRows.setRows(replyList);
        return pageRows;
    }

    @Override
    public Map<Long, ForignContentReply> queryForignContentReplyBySet(Set<Long> replyIdSet) throws ServiceException {
        Map<Long, ForignContentReply> contentReplyMap = new HashMap<Long, ForignContentReply>();

        Set<Long> queryDbSet = new HashSet<Long>();

        //first get by memached
        for (Long replyId : replyIdSet) {
            ForignContentReply contentReply = forignConentReplyCache.getForignContentReply(replyId);

            if (contentReply != null && !contentReply.getRemoveStatus().equals(ActStatus.UNACT)) {
                contentReplyMap.put(replyId, contentReply);
            } else {
                queryDbSet.add(replyId);
            }
        }

        if (CollectionUtil.isEmpty(queryDbSet)) {
            return contentReplyMap;
        }

        //second get by db
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.in(ForignContentReplyField.REPLY_ID, queryDbSet.toArray()));
        queryExpress.add(QueryCriterions.eq(ForignContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
        List<ForignContentReply> forignContentReplyList = readonlyContentHandlersPool.getHandler().queryForignContentReply(queryExpress);

        for (ForignContentReply forignContentReply : forignContentReplyList) {
            contentReplyMap.put(forignContentReply.getReplyId(), forignContentReply);
            forignConentReplyCache.putForignContentReply(forignContentReply);
        }

        return contentReplyMap;
    }

    private ForignContentReply getForignReply(long replyId) throws ServiceException {
        ForignContentReply reply = forignConentReplyCache.getForignContentReply(replyId);
        if (reply == null) {
            reply = readonlyContentHandlersPool.getHandler().getForignContentReplyById(replyId);
        }
        return reply;
    }

    @Override
    public String getRightHtmlByArticleId(ForignContent forignContent) throws ServiceException {
        String returnObj = "";

        returnObj = forignConentReplyCache.getRightHtml(forignContent.getContentId());

        if (returnObj == null) {
            //缓存为空 取righthtml 存缓存
            try {
                String url = forignContent.getContentUrl();
                String s = URLUtils.getHtmlStringByURl(url);
                Document document = Jsoup.parse(s);
                if (document != null) {
                    Elements elements = document.getElementsByClass("subpage-right");
                    if (elements != null && elements.size() > 0) {
                        returnObj = elements.get(0).html();
                        forignConentReplyCache.putRightHtml(forignContent.getContentId(), returnObj);
                    }
                } else {
                    returnObj = "";
                }

            } catch (IOException e) {
                GAlerter.lab(this.getClass().getName() + " occured Exception.e:", e);
            }
        }

        return returnObj;
    }

    @Override
    public ForignContentReplyLog createForignContentReplyLog(ForignContentReplyLog replyLog) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " createForignContentReplyLog.replyLog:" + replyLog);
        }
        return writeAbleContentHandler.createForignContentReplyLog(replyLog);
    }

    @Override
    public ForignContentReplyLog getForignContentReplyLog(long replyId, String uno, int logType) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getForignContentReplyLog.replyId:" + replyId + ",uno:" + uno + ",logType:" + logType);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ForignContentReplyLogField.REPLY_ID, replyId));
        queryExpress.add(QueryCriterions.eq(ForignContentReplyLogField.UNO, uno));
        queryExpress.add(QueryCriterions.eq(ForignContentReplyLogField.LOG_TYPE, logType));
        return readonlyContentHandlersPool.getHandler().getForignContentReplyLog(queryExpress);
    }

    @Override
    public int agreeForignCotnentReply(long replyId, String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " agreeForignCotnentReply.replyId:" + replyId + ",uno:" + uno);
        }
        ForignContentReplyLog replyLog = forignConentReplyCache.getForignContentReplyLog(replyId, uno, ForignContentReplyLogType.AGREE.getCode());

        if (replyLog == null) {
            ForignContentReplyLog log = ContentServiceSngl.get().getForignContentReplyLog(replyId, uno, ForignContentReplyLogType.AGREE.getCode());
            if (log != null) {
                forignConentReplyCache.putForignContentReplyLog(replyId, uno, ForignContentReplyLogType.AGREE.getCode(), log);
                return -1;
            }
        } else {
            return -1;
        }

        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.increase(ForignContentReplyField.AGREE_NUM, 1);
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ForignContentReplyField.REPLY_ID, replyId));
        boolean bReplyVal = writeAbleContentHandler.updateForignContentReply(updateExpress, queryExpress);
        if (bReplyVal) {
            ForignContentReplyLog forignContentReplyLog = new ForignContentReplyLog();
            forignContentReplyLog.setReplyId(replyId);
            forignContentReplyLog.setUno(uno);
            forignContentReplyLog.setLogType(ForignContentReplyLogType.AGREE);
            forignContentReplyLog.setCreateDate(new Date());
            ForignContentReplyLog forignLog = writeAbleContentHandler.createForignContentReplyLog(forignContentReplyLog);
            if (forignLog != null) {
                forignConentReplyCache.putForignContentReplyLog(forignLog.getReplyId(), forignLog.getUno(), forignLog.getLogType().getCode(), forignLog);
            }
            forignConentReplyCache.deleteForignContentReply(replyId);
            return 1;
        }
        return 0;
    }

    ////////////////////////////////////////////////////////////////

    @Override
    public SocialContent postSocialContent(SocialContent socialContent) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " postSocialContent:" + socialContent);
        }
        //插入social_content
        socialContent = writeAbleContentHandler.insertSocialContent(socialContent);
        //
        if (socialContent.getActivityId() > 0l) {
            // social_content_activity 建立 关联
            createSocialContentActivity(socialContent.getContentId(), socialContent.getUno(), socialContent.getActivityId());
            sendOutSocialLogEvent(socialContent.getActivityId(), socialContent.getUno(), socialContent.getContentId(), SocialLogType.SOCIAL_ACTIVITY, SocialLogCategory.TOTALS, 1, AppPlatform.CLIENT.getCode(), null);
        }
        sendOutSocialContentPostEvent(socialContent);
        return socialContent;
    }

    private void createSocialContentActivity(long contentId, String uno, long activityId) {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " createSocialContentActivity.contentId:" + contentId);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(SocialContentActivityField.CONTENT_ID, contentId));
        try {
            SocialContentActivity socialContentActivity = readonlyContentHandlersPool.getHandler().getSocialContentActivity(queryExpress);
            if (socialContentActivity == null) {
                socialContentActivity = new SocialContentActivity();
                socialContentActivity.setActivityId(activityId);
                socialContentActivity.setContentId(contentId);
                socialContentActivity.setContentUno(uno);
                socialContentActivity.setCreateDate(new Date());
                socialContentActivity.setRemoveStatus(ValidStatus.VALID);
                writeAbleContentHandler.insertSocialContentActivity(socialContentActivity);
            }
        } catch (DbException e) {
            GAlerter.lab("ContentLogic processQueuedEvent SocialActivityEvent error.", e);
        }
    }

    @Override
    public SocialContent getSocialContentByContentId(long contentId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialContentByContentId.contentId:" + contentId);
        }
        SocialContent socialContent = socialContentCache.getSocialContent(contentId);

        if (socialContent == null) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(SocialContentField.CONTENTID, contentId));
            queryExpress.add(QueryCriterions.eq(SocialContentField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            socialContent = readonlyContentHandlersPool.getHandler().getSocialContentByContentId(queryExpress);

            if (socialContent != null) {
                socialContentCache.putSocialContent(contentId, socialContent);
            }
        }
        return socialContent;
    }

    @Override
    public boolean removeSocialContent(String uno, long contentId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " removeSocialContent.contentId: " + contentId);
        }
        //TODO read update writeAbleContentHandler
        SocialContent content = writeAbleContentHandler.getSocialContentByContentId(new QueryExpress()
                .add(QueryCriterions.eq(SocialContentField.CONTENTID, contentId))
                .add(QueryCriterions.eq(SocialContentField.REMOVE_STATUS, ActStatus.UNACT.getCode())));
        if (content == null) {
            return true;
        }
        boolean bVal = modifySocialContent(new UpdateExpress().set(SocialContentField.REMOVE_STATUS, ActStatus.ACTED.getCode()), contentId);

        return bVal;
    }




    @Override
    public PageRows<SocialContent> querySocialContentByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialContentByPage.queryExpress:" + queryExpress + ",pagination:" + pagination);
        }
        PageRows<SocialContent> pageRows = readonlyContentHandlersPool.getHandler().querySocialContentByUno(queryExpress, pagination);
        if (pageRows == null || CollectionUtil.isEmpty(pageRows.getRows())) {
            return null;
        }
        List<SocialContent> list = pageRows.getRows();
        for (SocialContent sc : list) {
            socialContentCache.putSocialContent(sc.getContentId(), sc);
        }
        return pageRows;
    }

    private void sendOutSocialContentPostEvent(SocialContent socialContent) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " sendOutEventToProfileTimeLine.SocialContent:" + socialContent);
        }
//        //insert immediately timeline
//        SocialTimeLineItem item = new SocialTimeLineItem();
//        item.setOwnUno(socialContent.getUno());
//        item.setDomain(SocialTimeLineDomain.BLOG);
//        item.setContentId(String.valueOf(socialContent.getContentId()));
//        item.setContentUno(socialContent.getUno());
//        item.setRemoveStatus(ActStatus.UNACT);
//        TimeLineServiceSngl.get().insertSocialTimeLineItem(SocialTimeLineDomain.BLOG, socialContent.getUno(), item);
//
//        //自己发的文章，也到我关注列表
//        SocialTimeLineItemEvent itemEvent = new SocialTimeLineItemEvent();
//        itemEvent.setOwnUno(socialContent.getUno());
//        itemEvent.setDomain(SocialTimeLineDomain.HOME);
//        itemEvent.setContentUno(socialContent.getUno());
//        itemEvent.setContentId(String.valueOf(socialContent.getContentId()));
//        EventDispatchServiceSngl.get().dispatch(itemEvent);
//
//        //sendout profile modify profile article sum
//        ProfileSumIncreaseEvent psiEvent = new ProfileSumIncreaseEvent();
//        psiEvent.setCount(1);
//        psiEvent.setField(ProfileSumField.SOCIALBLOGSUM);
//        psiEvent.setOwnUno(socialContent.getUno());
//        EventDispatchServiceSngl.get().dispatch(psiEvent);
//
//        SocialContentBoardCastEvent socialTimeLineEvent = new SocialContentBoardCastEvent();
//        socialTimeLineEvent.setSocialTimeLineDomain(SocialTimeLineDomain.HOME);
//        socialTimeLineEvent.setContentId(String.valueOf(socialContent.getContentId()));
//        socialTimeLineEvent.setOwnUno(socialContent.getUno());
//        EventDispatchServiceSngl.get().dispatch(socialTimeLineEvent);

    }

    @Override
    public SocialContentReply postSocialContentReply(SocialContentReply reply) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " postSocialContentReply.SocialContentReply:" + reply);
        }
        SocialContent socialContent = getSocialContentByContentId(reply.getContentId());
        if (socialContent == null) {
            return null;
        }

        reply = writeAbleContentHandler.insertSocialContentReply(reply);

        boolean bool = modifySocialContent(new UpdateExpress().increase(SocialContentField.REPLAY_NUM, 1), reply.getContentId());
        if (bool) {
            socialContentCache.deleteSocialContent(reply.getContentId());

            Pagination pagination = new Pagination(socialContent.getReplyNum() + 1, 1, CONTENT_REPLY_PAGESIZE);
            List<Long> replyIdList = socialContentCache.getReplyIdList(socialContent.getContentId(), 0l, pagination.getMaxPage());
            if (replyIdList != null) {
                replyIdList.add(reply.getReplyId());
                socialContentCache.putReplyIdList(socialContent.getContentId(), 0l, pagination.getMaxPage(), replyIdList);
            }

            //todo
            GAlerter.lab("-----------------------------------------START SEND REPLY SOCIAL MESSAGE------------------------------------------------");
            replySocialMessage(reply);

        }
        sendOutSocialHotContentInsertEvent(reply.getContentId());
        sendOutPostNoticeEvent(reply.getContentUno());
        sendOutPostNoticeEvent(reply.getRootUno());
        sendOutPostNoticeEvent(reply.getParentUno());

        SocialContentActivity sca = readonlyContentHandlersPool.getHandler().getSocialContentActivity(new QueryExpress()
                .add(QueryCriterions.eq(SocialContentActivityField.CONTENT_ID, reply.getContentId())));
        if (sca != null) {
            sendOutSocialLogEvent(sca.getActivityId(), sca.getContentUno(), sca.getContentId(), SocialLogType.SOCIAL_ACTIVITY, SocialLogCategory.REPLY, 1, AppPlatform.CLIENT.getCode(), null);
        }

        return reply;
    }

    @Override
    public boolean removeSocialContentReply(long contentId, long replyId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " removeSocialContentReply.contentId:" + contentId + ":replyId:" + replyId);
        }
        PageRows<SocialContentReply> replyRows = readonlyContentHandlersPool.getHandler().querySocialContentReplyByPage(new QueryExpress().add(QueryCriterions.eq(SocialContentReplyField.REPLY_ID, replyId)), new Pagination(1, 1, 1));
        if (replyRows == null || CollectionUtil.isEmpty(replyRows.getRows())) {
            return false;
        }
        //删除
        boolean bVal = writeAbleContentHandler.updateSocialContentReply(
                new UpdateExpress().set(SocialContentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode()),
                new QueryExpress().add(QueryCriterions.eq(SocialContentReplyField.REPLY_ID, replyId)).add(QueryCriterions.eq(SocialContentReplyField.CONTENT_ID, contentId)).add(QueryCriterions.eq(SocialContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode())));

        if (bVal) {
            //查找文章
            SocialContent content = getSocialContentByContentId(contentId);

            //更新文章计数文章缓存、更新评论缓存
            if (content != null) {
                modifySocialContent(new UpdateExpress().increase(SocialContentField.REPLAY_NUM, -1), contentId);
                refreshSocialReplyCache(content, replyId);
            }

            SocialContentReply reply = replyRows.getRows().get(0);
            String ownUno = "";
            if (!StringUtil.isEmpty(reply.getContentUno()) && StringUtil.isEmpty(reply.getRootUno())) {
                ownUno = reply.getContentUno();
                boolean bool = MessageServiceSngl.get().modifySocialMessage(ownUno, new QueryExpress()
                        .add(QueryCriterions.eq(SocialMessageField.OWN_UNO, ownUno))
                        .add(QueryCriterions.eq(SocialMessageField.REPLY_ID, replyId))
                        .add(QueryCriterions.eq(SocialMessageField.CONTENT_ID, contentId)), new UpdateExpress()
                        .set(SocialMessageField.REMOVE_STATUS, ActStatus.ACTED.getCode())
                        .set(SocialMessageField.MODIFY_DATE, new Date()));
                if (bool) {
                    Map<ObjectField, Object> paramMap = new HashMap<ObjectField, Object>();
                    paramMap.put(ProfileSumField.SOCIALREPLYMSGSUM, -1);
                    ProfileServiceSngl.get().increaseProfileSum(ownUno, paramMap);
                }

            }
            if (!StringUtil.isEmpty(reply.getContentUno()) && !StringUtil.isEmpty(reply.getRootUno()) && !StringUtil.isEmpty(reply.getParentUno()) && reply.getRootUno().equals(reply.getParentUno())) {
                ownUno = reply.getRootUno();
                MessageServiceSngl.get().modifySocialMessage(ownUno, new QueryExpress()
                        .add(QueryCriterions.eq(SocialMessageField.OWN_UNO, ownUno))
                        .add(QueryCriterions.eq(SocialMessageField.REPLY_ID, replyId))
                        .add(QueryCriterions.eq(SocialMessageField.CONTENT_ID, contentId)), new UpdateExpress()
                        .set(SocialMessageField.REMOVE_STATUS, ActStatus.ACTED.getCode())
                        .set(SocialMessageField.MODIFY_DATE, new Date()));

                ownUno = reply.getContentUno();
                MessageServiceSngl.get().modifySocialMessage(ownUno, new QueryExpress()
                        .add(QueryCriterions.eq(SocialMessageField.OWN_UNO, ownUno))
                        .add(QueryCriterions.eq(SocialMessageField.REPLY_ID, replyId))
                        .add(QueryCriterions.eq(SocialMessageField.CONTENT_ID, contentId)), new UpdateExpress()
                        .set(SocialMessageField.REMOVE_STATUS, ActStatus.ACTED.getCode())
                        .set(SocialMessageField.MODIFY_DATE, new Date()));
            }
            if (!StringUtil.isEmpty(reply.getContentUno()) && !StringUtil.isEmpty(reply.getRootUno()) && !StringUtil.isEmpty(reply.getParentUno()) && !reply.getRootUno().equals(reply.getParentUno())) {
                ownUno = reply.getParentUno();
                MessageServiceSngl.get().modifySocialMessage(ownUno, new QueryExpress()
                        .add(QueryCriterions.eq(SocialMessageField.OWN_UNO, ownUno))
                        .add(QueryCriterions.eq(SocialMessageField.REPLY_ID, replyId))
                        .add(QueryCriterions.eq(SocialMessageField.CONTENT_ID, contentId)), new UpdateExpress()
                        .set(SocialMessageField.REMOVE_STATUS, ActStatus.ACTED.getCode())
                        .set(SocialMessageField.MODIFY_DATE, new Date()));

                ownUno = reply.getRootUno();
                MessageServiceSngl.get().modifySocialMessage(ownUno, new QueryExpress()
                        .add(QueryCriterions.eq(SocialMessageField.OWN_UNO, ownUno))
                        .add(QueryCriterions.eq(SocialMessageField.REPLY_ID, replyId))
                        .add(QueryCriterions.eq(SocialMessageField.CONTENT_ID, contentId)), new UpdateExpress()
                        .set(SocialMessageField.REMOVE_STATUS, ActStatus.ACTED.getCode())
                        .set(SocialMessageField.MODIFY_DATE, new Date()));

                ownUno = reply.getContentUno();
                MessageServiceSngl.get().modifySocialMessage(ownUno, new QueryExpress()
                        .add(QueryCriterions.eq(SocialMessageField.OWN_UNO, ownUno))
                        .add(QueryCriterions.eq(SocialMessageField.REPLY_ID, replyId))
                        .add(QueryCriterions.eq(SocialMessageField.CONTENT_ID, contentId)), new UpdateExpress()
                        .set(SocialMessageField.REMOVE_STATUS, ActStatus.ACTED.getCode())
                        .set(SocialMessageField.MODIFY_DATE, new Date()));
            }
        }
        return bVal;
    }

    @Override
    public boolean recoverSocialContentReply(long contentId, long replyId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " recoverSocialContentReply.contentId:" + contentId + ":replyId:" + replyId);
        }
        //恢复评论
        boolean bVal = writeAbleContentHandler.updateSocialContentReply(
                new UpdateExpress().set(SocialContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode()),
                new QueryExpress().add(QueryCriterions.eq(SocialContentReplyField.REPLY_ID, replyId)).add(QueryCriterions.eq(SocialContentReplyField.CONTENT_ID, contentId)).add(QueryCriterions.eq(SocialContentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode())));
        if (bVal) {
            //查找文章
            SocialContent content = getSocialContentByContentId(contentId);

            //更新文章计数文章缓存、更新评论缓存
            if (content != null) {
                modifySocialContent(new UpdateExpress().increase(SocialContentField.REPLAY_NUM, 1), contentId);
                refreshSocialReplyCache(content, replyId);
            }
        }
        return bVal;
    }

    private boolean refreshSocialReplyCache(SocialContent content, long replyId) {

        Pagination pagination = new Pagination(content.getReplyNum(), 1, CONTENT_REPLY_PAGESIZE);

        for (int i = 1; i < pagination.getMaxPage(); i++) {
            socialContentCache.deleteContentReply(replyId);
            socialContentCache.deleteReplyIdList(content.getContentId(), 0l, i);
        }
        return true;
    }

    public boolean modifySocialContent(UpdateExpress updateExpress, long contentId) throws ServiceException {
        boolean bVal = writeAbleContentHandler.updateSocialContent(updateExpress, new QueryExpress().add(QueryCriterions.eq(SocialContentField.CONTENTID, contentId)));
        if (bVal) {
            socialContentCache.deleteSocialContent(contentId);
        }
        return bVal;
    }

    @Override
    public PageRows<SocialContentReply> querySocialContentReply(long contentId, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialContentReply.contentId:" + contentId + ":Pagination:" + page);
        }
        PageRows<SocialContentReply> returnObj = new PageRows<SocialContentReply>();

        //获取文章的评论数
        SocialContent socialContent = getSocialContentByContentId(contentId);
        if (socialContent == null) {
            return returnObj;
        }
        page.setTotalRows(socialContent.getReplyNum());

        PageRows<Long> replyIdRows = getSocialReplyId(contentId, page);
        if (replyIdRows == null || CollectionUtil.isEmpty(replyIdRows.getRows())) {
            returnObj.setPage(page);
            return returnObj;
        }

        List<Long> replyIdList = replyIdRows.getRows();
        Map<Long, SocialContentReply> forignContentReplyMap = querySocialContentReplyBySet(new HashSet<Long>(replyIdList));

        List<SocialContentReply> replyList = new ArrayList<SocialContentReply>();
        for (Long replyId : replyIdList) {
            SocialContentReply contentReply = forignContentReplyMap.get(replyId);

            if (contentReply != null) {
                replyList.add(contentReply);
            }
        }

        returnObj = new PageRows<SocialContentReply>();
        returnObj.setPage(page);
        returnObj.setRows(replyList);

        return returnObj;
    }

    @Override
    public PageRows<SocialContentReply> querySocialContentReplyByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialContentReplyByPage.queryExpress:" + queryExpress + ",pagination:" + pagination);
        }
        return readonlyContentHandlersPool.getHandler().querySocialContentReplyByPage(queryExpress, pagination);
    }

    //从缓存读取该页的评论IDLIST
    private PageRows<Long> getSocialReplyId(long contentId, Pagination page) throws DbException {
        PageRows<Long> pageRows = new PageRows<Long>();
        List<Long> replyList = new ArrayList<Long>();

        //get content by contentid get totalNums
        int fetchCacheTimes = page.getPageSize() / CONTENT_REPLY_PAGESIZE;

        for (int times = 0; times < fetchCacheTimes; times++) {
            //得到分页的参数 第N页就从缓存取 总页数-N页*times+1,根据外面传入的pageSize可以得到需要从缓存取多少次
            Pagination pageByCache = new Pagination(page.getTotalRows(), 1, CONTENT_REPLY_PAGESIZE);
            pageByCache.setCurPage(pageByCache.getMaxPage() - (page.getCurPage() - 1) * fetchCacheTimes - times);

            //get by cache
            List<Long> replyIdList = socialContentCache.getReplyIdList(contentId, 0l, pageByCache.getCurPage());

            //get by db
            if (CollectionUtil.isEmpty(replyIdList)) {
                PageRows<SocialContentReply> dbRows = readonlyContentHandlersPool.getHandler().querySocialContentReplyList(contentId, null, pageByCache);
                List<SocialContentReply> dbList = dbRows.getRows();
                replyIdList = new ArrayList<Long>();
                for (int i = dbList.size() - 1; i >= 0; i--) {
                    replyIdList.add(dbList.get(i).getReplyId());
                }
                socialContentCache.putReplyIdList(contentId, 0l, pageByCache.getCurPage(), replyIdList);
            }
            replyList.addAll(replyIdList);

            if (pageByCache.getCurPage() <= 1) {
                break;
            }
        }

        Collections.sort(replyList, new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return o1 < o2 ? 1 : (o1.equals(o2) ? 0 : -1);
            }
        });
        pageRows.setPage(page);
        pageRows.setRows(replyList);
        return pageRows;
    }


    private Map<Long, SocialContentReply> querySocialContentReplyBySet(Set<Long> replyIdSet) throws ServiceException {
        Map<Long, SocialContentReply> contentReplyMap = new HashMap<Long, SocialContentReply>();

        Set<Long> queryDbSet = new HashSet<Long>();

        //first get by memached
        for (Long replyId : replyIdSet) {
            SocialContentReply contentReply = socialContentCache.getContentReply(replyId);

            if (contentReply != null && !contentReply.getRemoveStatus().equals(ActStatus.UNACT)) {
                contentReplyMap.put(replyId, contentReply);
            } else {
                queryDbSet.add(replyId);
            }
        }

        if (CollectionUtil.isEmpty(queryDbSet)) {
            return contentReplyMap;
        }

        //second get by db
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.in(SocialContentReplyField.REPLY_ID, queryDbSet.toArray()));
        queryExpress.add(QueryCriterions.eq(SocialContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode()));

        List<SocialContentReply> socialContentReplyList = readonlyContentHandlersPool.getHandler().querySocialContentReply(queryExpress);

        for (SocialContentReply soicalReply : socialContentReplyList) {
            contentReplyMap.put(soicalReply.getReplyId(), soicalReply);
            socialContentCache.putContentReply(soicalReply);
        }

        return contentReplyMap;
    }


    @Override
    public SocialContentAction createSocialContentAction(SocialContentAction action) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " createSocialContentAction.SocialContentAction:" + action);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(SocialContentActionField.UNO, action.getUno()));
        queryExpress.add(QueryCriterions.eq(SocialContentActionField.CONTENT_ID, action.getContentId()));
        SocialContentAction existAction = writeAbleContentHandler.getSocialContentAction(queryExpress);

        boolean bVal = false;
        if (existAction == null || existAction.getUno().equals(HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getKadaGuestUno())) {
            action = writeAbleContentHandler.insertSocialContentAction(action);
            bVal = true;
            if (action != null && !action.getUno().equals(HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getKadaGuestUno())) {
                //todo
                if (!action.getContentUno().equals(action.getUno())) {
                    Map<ObjectField, Object> propsMap = new HashMap<ObjectField, Object>();
                    propsMap.put(SocialMessageField.MSG_TYPE, SocialMessageType.AGREE.getCode());
                    propsMap.put(SocialMessageField.MSG_CATEGORY, SocialMessageCategory.DEFAULT_MSG.getCode());
                    propsMap.put(SocialMessageField.SEND_UNO, action.getUno());
                    propsMap.put(SocialMessageField.OWN_UNO, action.getContentUno());
                    propsMap.put(SocialMessageField.RECEIVE_UNO, action.getContentUno());
                    propsMap.put(SocialMessageField.CONTENT_ID, action.getContentId());
                    propsMap.put(SocialMessageField.CONTENT_UNO, action.getContentUno());
                    propsMap.put(SocialMessageField.CREATE_DATE, new Date());
                    sendSocialMessEvent(propsMap);
                }
            }
        } else {
            if (existAction.getRemoveStatus().equals(ActStatus.ACTED)) {
                UpdateExpress up = new UpdateExpress()
                        .set(SocialContentActionField.REMOVE_STATUS, ActStatus.UNACT.getCode())
                        .set(SocialContentActionField.CREATE_TIME, new Date());
                bVal = writeAbleContentHandler.updateSocialContentAction(up, queryExpress);
                //todo 只写数据，不发消息
                SocialMessage socialMessage = new SocialMessage();
                socialMessage.setMsgBody("");
                socialMessage.setMsgType(SocialMessageType.AGREE);
                socialMessage.setMsgCategory(SocialMessageCategory.DEFAULT_MSG);

                socialMessage.setOwnUno(action.getContentUno());
                socialMessage.setSendUno(action.getUno());
                socialMessage.setReceiveUno(action.getContentUno());

                socialMessage.setReplyId(0l);
                socialMessage.setReplyUno("");
                socialMessage.setContentId(action.getContentId());
                socialMessage.setContentUno(action.getContentUno());
                socialMessage.setParentId(0l);
                socialMessage.setParentUno("");
                socialMessage.setRootId(0l);
                socialMessage.setRootUno("");

                socialMessage.setCreateDate(new Date());
                socialMessage.setRemoveStatus(ActStatus.UNACT);
                MessageServiceSngl.get().createSocialMessage(socialMessage);

                Map<ObjectField, Object> paramMap = new HashMap<ObjectField, Object>();
                paramMap.put(ProfileSumField.SOCIALAGREEMSGSUM, 1);
                ProfileServiceSngl.get().increaseProfileSum(action.getContentUno(), paramMap);
            }
        }

        if (action.getType().equals(SocialContentActionType.AGREE) && bVal) {
            modifySocialContent(new UpdateExpress().increase(SocialContentField.AGREE_NUM, 1), action.getContentId());

            SocialContentActivity sca = readonlyContentHandlersPool.getHandler().getSocialContentActivity(new QueryExpress()
                    .add(QueryCriterions.eq(SocialContentActivityField.CONTENT_ID, action.getContentId())));
            if (sca != null) {
                sendOutSocialLogEvent(sca.getActivityId(), sca.getContentUno(), sca.getContentId(), SocialLogType.SOCIAL_ACTIVITY, SocialLogCategory.AGREE, 1, AppPlatform.CLIENT.getCode(), null);
            }
        }

        if (bVal) {
            socialContentCache.deleteSocialContentIdSetByAction(action.getUno(), action.getContentId(), action.getType().getCode());

            //热门文章的 事件
            sendOutSocialHotContentInsertEvent(action.getContentId());
        }
        return action;
    }

    @Override
    public boolean removeSocialContentAction(long contentId, String uno, SocialContentActionType type) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getSocialContent contentId:" + contentId);
        }

        //删除
        boolean bVal = writeAbleContentHandler.updateSocialContentAction(
                new UpdateExpress().set(SocialContentReplyField.REMOVE_STATUS, ActStatus.ACTED.getCode()),
                new QueryExpress().add(QueryCriterions.eq(SocialContentActionField.UNO, uno)).add(QueryCriterions.eq(SocialContentReplyField.CONTENT_ID, contentId)).add(QueryCriterions.eq(SocialContentReplyField.REMOVE_STATUS, ActStatus.UNACT.getCode())));

        if (bVal && SocialContentActionType.AGREE.equals(type)) {
            modifySocialContent(new UpdateExpress().increase(SocialContentField.AGREE_NUM, -1), contentId);
        }

        if (bVal) {
            socialContentCache.deleteSocialContentIdSetByAction(uno, contentId, type.getCode());

            SocialContent content = readonlyContentHandlersPool.getHandler().getSocialContentByContentId(contentId);
            if (content != null) {
                //TODO 	add SEND_UNO
                boolean bool = MessageServiceSngl.get().modifySocialMessage(content.getUno(), new QueryExpress()
                        .add(QueryCriterions.eq(SocialMessageField.OWN_UNO, content.getUno()))
                        .add(QueryCriterions.eq(SocialMessageField.SEND_UNO, uno))
                        .add(QueryCriterions.eq(SocialMessageField.REPLY_ID, 0l))
                        .add(QueryCriterions.eq(SocialMessageField.CONTENT_ID, contentId)), new UpdateExpress()
                        .set(SocialMessageField.REMOVE_STATUS, ActStatus.ACTED.getCode())
                        .set(SocialMessageField.MODIFY_DATE, new Date()));
                if (bool) {
                    Map<ObjectField, Object> paramMap = new HashMap<ObjectField, Object>();
                    paramMap.put(ProfileSumField.SOCIALAGREEMSGSUM, -1);
                    ProfileServiceSngl.get().increaseProfileSum(content.getUno(), paramMap);
                }
            }

        }

        return bVal;
    }

    @Override
    public NextPageRows<SocialContentAction> querySocialContentAction(long contentId, SocialContentActionType type, NextPagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialContentAction contentId:" + contentId + ", type:" + contentId + ", page:" + page);
        }

        return readonlyContentHandlersPool.getHandler().querySocialContentAction(contentId, type, page);
    }

    @Override
    public SocialHotContent insertSocialHotContent(SocialHotContent socialHotContent) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialHotContent.socialHotContent: " + socialHotContent);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(SocialHotContentField.CONTENTID, socialHotContent.getContentId()));
        SocialHotContent hotContent = readonlyContentHandlersPool.getHandler().getSocialHotContent(queryExpress);
        if (hotContent == null) {
            hotContent = writeAbleContentHandler.insertSocialHotContent(socialHotContent);

            //todo
            Map<ObjectField, Object> propsMap = new HashMap<ObjectField, Object>();
            propsMap.put(SocialMessageField.MSG_BODY, "很多人喜欢您的文章，被推荐到了咔哒热门频道，快来看看吧~");
            propsMap.put(SocialMessageField.MSG_TYPE, SocialMessageType.HOT.getCode());
            propsMap.put(SocialMessageField.MSG_CATEGORY, SocialMessageCategory.DEFAULT_MSG.getCode());
            propsMap.put(SocialMessageField.OWN_UNO, hotContent.getUno());
            propsMap.put(SocialMessageField.CONTENT_ID, hotContent.getContentId());
            propsMap.put(SocialMessageField.CONTENT_UNO, hotContent.getUno());
            propsMap.put(SocialMessageField.CREATE_DATE, new Date());

            GAlerter.lab("-----------------------------------------START SEND HOT SOCIAL MESSAGE------------------------------------------------");
            sendSocialMessEvent(propsMap);

        } else if (hotContent.getRemoveStatus().equals(ActStatus.ACTED)) {
            UpdateExpress updateExpress = new UpdateExpress();
            updateExpress.set(SocialHotContentField.REMOVE_STATUS, ActStatus.UNACT.getCode());
            writeAbleContentHandler.updateSocialHotContent(updateExpress, queryExpress);
        }
        return hotContent;
    }


    @Override
    public List<SocialHotContent> querySocialHotContent(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialHotContent.queryExpress: " + queryExpress);
        }
        return readonlyContentHandlersPool.getHandler().querySocialHotContent(queryExpress);
    }

    @Override
    public SocialHotContent getSocialHotContent(long socialContentId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getSocialHotContent.socialContentId:" + socialContentId);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(SocialHotContentField.CONTENTID, socialContentId));
        return readonlyContentHandlersPool.getHandler().getSocialHotContent(queryExpress);
    }

    @Override
    public NextPageRows<SocialHotContent> querySocialHotContentByPage(NextPagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialHotContentByPage.pagination: " + pagination);
        }
        return readonlyContentHandlersPool.getHandler().querySocialHotContentByPage(pagination);
    }

    @Override
    public boolean modifySocialHotContent(UpdateExpress updateExpress, long socialContentId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifySocialHotContent.socialContentId: " + socialContentId);
        }
        return writeAbleContentHandler.updateSocialHotContent(updateExpress, new QueryExpress().add(QueryCriterions.eq(SocialHotContentField.CONTENTID, socialContentId)));
    }

    @Override
    public Set<Long> checkSocialContentAction(String uno, SocialContentActionType actionType, Set<Long> contentIdSet) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " checkSocialContentAction.uno: " + uno + ",actionType:" + actionType + ",contentIdSet" + contentIdSet);
        }

        Set<Long> returnSet = new HashSet<Long>();

        Set<Long> queryDbContentId = new HashSet<Long>();
        for (Long contentId : contentIdSet) {
            Long contentidFromCache = socialContentCache.getSocialContentIdSetByAction(uno, contentId, actionType.getCode());
            if (contentidFromCache == null) {
                queryDbContentId.add(contentId);
            } else {
                returnSet.add(contentId);
            }
        }

        if (!CollectionUtil.isEmpty(queryDbContentId)) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(SocialContentActionField.ACTION_TYPE, actionType.getCode()));
            queryExpress.add(QueryCriterions.eq(SocialContentActionField.UNO, uno));
            queryExpress.add(QueryCriterions.in(SocialContentActionField.CONTENT_ID, queryDbContentId.toArray()));
            queryExpress.add(QueryCriterions.eq(SocialContentActionField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            List<SocialContentAction> list = readonlyContentHandlersPool.getHandler().querySocialContentActionByQuery(queryExpress);

            for (SocialContentAction action : list) {
                returnSet.add(action.getContentId());
                socialContentCache.putSocialContentIdSetByAction(uno, actionType.getCode(), action.getContentId());
            }
        }
        return returnSet;
    }

    @Override
    public List<SocialContent> querySocialContentByIdSet(Set<Long> contentIdSet) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialContentByIdSet.contentIdSet: " + contentIdSet);
        }
        Set<Long> querySet = new HashSet<Long>();
        List<SocialContent> returnList = new ArrayList<SocialContent>();

        for (Long contentId : contentIdSet) {
            SocialContent socialContent = socialContentCache.getSocialContent(contentId);
            if (socialContent == null) {
                querySet.add(contentId);
            } else {
                returnList.add(socialContent);
            }
        }
        if (!CollectionUtil.isEmpty(querySet)) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.in(SocialContentField.CONTENTID, querySet.toArray()));
            List<SocialContent> queryList = readonlyContentHandlersPool.getHandler().querySocialContent(queryExpress);
            for (SocialContent socialContent : queryList) {
                socialContentCache.putSocialContent(socialContent.getContentId(), socialContent);
            }
            returnList.addAll(queryList);
        }

        return returnList;
    }

    @Override
    public Map<Long, SocialContent> querySocialContentMapByIdSet(Set<Long> contentIdSet) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialContentByIdSet.contentIdSet: " + contentIdSet);
        }
        Set<Long> querySet = new HashSet<Long>();
        Map<Long, SocialContent> returnMap = new HashMap<Long, SocialContent>();

        for (Long contentId : contentIdSet) {
            SocialContent socialContent = socialContentCache.getSocialContent(contentId);
            if (socialContent == null) {
                querySet.add(contentId);
            } else {
                returnMap.put(socialContent.getContentId(), socialContent);
            }
        }
        if (!CollectionUtil.isEmpty(querySet)) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.in(SocialContentField.CONTENTID, querySet.toArray()));
            queryExpress.add(QueryCriterions.eq(SocialContentField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            List<SocialContent> queryList = readonlyContentHandlersPool.getHandler().querySocialContent(queryExpress);
            for (SocialContent socialContent : queryList) {
                returnMap.put(socialContent.getContentId(), socialContent);
                socialContentCache.putSocialContent(socialContent.getContentId(), socialContent);
            }
        }

        return returnMap;
    }

    @Override
    public PageRows<SocialReport> querySocialReportByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialReportByPage.queryExpress: " + queryExpress + " Pagination" + pagination);
        }
        return readonlyContentHandlersPool.getHandler().querySocialReportByPage(queryExpress, pagination);
    }

    @Override
    public SocialReport insertSocialReport(SocialReport socialReport) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " insertSocialReport.SocialReport: " + socialReport);
        }
        return writeAbleContentHandler.createSocialRepost(socialReport);
    }

    @Override
    public boolean modifySocialReport(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifySocialReport.UpdateExpress: " + updateExpress + " QueryExpress" + queryExpress);
        }
        return writeAbleContentHandler.modifySocialReport(updateExpress, queryExpress);
    }

    @Override
    public SocialReport getSocialReport(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getSocialReport.QueryExpress: " + queryExpress);
        }
        return readonlyContentHandlersPool.getHandler().getSocialReport(queryExpress);
    }

    @Override
    public boolean insertSocialContentPlay(List<SocialContentPlay> socialContentPlayList) throws ServiceException {

        return true;
    }

    @Override
    public SocialContent getSocialContent(long cid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getSocialContent.cid: " + cid);
        }

        return readonlyContentHandlersPool.getHandler().getSocialContentByContentId(cid);
    }

    @Override
    public PageRows<SocialHotContent> querySocialHotContentPageRows(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialHotContentPageRows.queryExpress: " + queryExpress + " Pagination:" + pagination);
        }
        return readonlyContentHandlersPool.getHandler().querySocialHotContentByPage(queryExpress, pagination);
    }


    public SocialContentAction getSocialContentAction(Long contentId, String actionUno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getSocialContentAction.contentId: " + contentId + " actionUno:" + actionUno);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(SocialContentActionField.CONTENT_ID, contentId));
        queryExpress.add(QueryCriterions.eq(SocialContentActionField.UNO, actionUno));
        return readonlyContentHandlersPool.getHandler().getSocialContentAction(queryExpress);
    }

    @Override
    public SocialActivity insertSocialActivity(SocialActivity socialActivity) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " insertSocialActivity.socialActivity: " + socialActivity);
        }
        return writeAbleContentHandler.insertSocialActivity(socialActivity);
    }

    @Override
    public SocialActivity getSocialActivity(long activityId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getSocialActivity.activityId: " + activityId);
        }
        SocialActivity returnObj = null;
        SocialActivity activity = socialContentCache.getSocialActivity(activityId);
        if (activity == null) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(SocialActivityField.ACTIVITY_ID, activityId));
//            queryExpress.add(QueryCriterions.eq(SocialActivityField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            returnObj = readonlyContentHandlersPool.getHandler().getSocialActivity(queryExpress);
            if (returnObj != null) {
                if (ValidStatus.VALID.equals(returnObj.getRemoveStatus())) {
                    socialContentCache.putSocialActivity(activityId, returnObj);
                }
            }
        }
        return returnObj;
    }

    @Override
    public List<SocialActivity> querySocialActivity(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialActivity.queryExpress: " + queryExpress);
        }
        return readonlyContentHandlersPool.getHandler().querySocialActivity(queryExpress);
    }

    @Override
    public PageRows<SocialActivity> querySocialActivityByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialActivityByPage.queryExpress: " + queryExpress);
        }
        //
        return readonlyContentHandlersPool.getHandler().querySocialActivityByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifySocialActivity(long activityId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifySocialActivity.activityId: " + activityId);
        }
        boolean bool = writeAbleContentHandler.modifySocialActivity(new QueryExpress().add(QueryCriterions.eq(SocialActivityField.ACTIVITY_ID, activityId)), updateExpress);
        if (bool) {
            socialContentCache.deleteSocialActivity(activityId);
        }
        return bool;
    }

    @Override
    public SocialContentActivity getSocialContentActivity(long activityId, long contentId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getSocialContentActivity.activityId: " + activityId + ",contentId:" + contentId);
        }
        return readonlyContentHandlersPool.getHandler().getSocialContentActivity(new QueryExpress()
                .add(QueryCriterions.eq(SocialContentActivityField.ACTIVITY_ID, activityId))
                .add(QueryCriterions.eq(SocialContentActivityField.CONTENT_ID, contentId)));
    }

    @Override
    public PageRows<SocialContentActivity> querySocialContentActivity(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialContentActivity.queryExpress: " + queryExpress);
        }
        return readonlyContentHandlersPool.getHandler().querySocialContentActivity(queryExpress, pagination);
    }

    @Override
    public PageRows<SocialTag> querySocialTagByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialTagByPage.queryExpress: " + queryExpress);
        }
        return readonlyContentHandlersPool.getHandler().querySocialTagByPage(queryExpress, pagination);
    }

    @Override
    public PageRows<SocialWatermark> querySocialWatermarkByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialWatermarkByPage.queryExpress: " + queryExpress);
        }
        return readonlyContentHandlersPool.getHandler().querySocialWatermarkByPage(queryExpress, pagination);
    }

    @Override
    public SocialWatermark insertSocialWatermark(SocialWatermark watermark) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " insertSocialWatermark.watermark: " + watermark);
        }
        return writeAbleContentHandler.insertSocialWatermark(watermark);
    }

    @Override
    public SocialWatermark getSocialWatermark(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getSocialWatermark.queryExpress: " + queryExpress);
        }
        return readonlyContentHandlersPool.getHandler().getSocialWatermark(queryExpress);
    }

    @Override
    public boolean modifySocialWatermark(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifySocialWatermark.queryExpress: " + queryExpress);
        }
        return writeAbleContentHandler.modifySocialWatermark(queryExpress, updateExpress);
    }

    @Override
    public NextPageRows<SocialWatermark> querySocialWatermarkByNext(NextPagination nextPagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialWatermarkByNext.nextPagination: " + nextPagination);
        }
        return readonlyContentHandlersPool.getHandler().querySocialWatermarkByNext(nextPagination);
    }

    @Override
    public NextPageRows<SocialActivity> querySocialActivityByNext(NextPagination nextPagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialActivityByNext.nextPagination: " + nextPagination);
        }
        return readonlyContentHandlersPool.getHandler().querySocialActivityByNext(nextPagination);
    }

    @Override
    public PageRows<SocialBackgroundAudio> querySocialBgAudio(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialBgAudio.queryExpress: " + queryExpress);
        }
        return readonlyContentHandlersPool.getHandler().querySocialBgAudio(queryExpress, pagination);
    }

    @Override
    public SocialBackgroundAudio insertSocialBgAudio(SocialBackgroundAudio bgAudio) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " insertSocialBgAudio.bgAudio: " + bgAudio);
        }
        return writeAbleContentHandler.insertSocialBgAudio(bgAudio);
    }

    @Override
    public SocialBackgroundAudio getSocialBgAudio(long audioId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getSocialBgAudio.audioId: " + audioId);
        }
        SocialBackgroundAudio returnObj = null;
        SocialBackgroundAudio bgAudio = socialContentCache.getSocialBgAudio(audioId);
        if (bgAudio == null) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(SocialBackgroundAudioField.AUDIO_ID, audioId));
//            queryExpress.add(QueryCriterions.eq(SocialBackgroundAudioField.REMOVE_STATUS, ValidStatus.VALID.getCode()));
            returnObj = readonlyContentHandlersPool.getHandler().getSocialBgAudio(queryExpress);
            if (returnObj != null) {
                if (ValidStatus.VALID.equals(returnObj.getRemoveStatus())) {
                    socialContentCache.putSocialBgAudio(audioId, returnObj);
                }
            }
        }
        return returnObj;
    }

    @Override
    public boolean modifySocialBgAudio(long audioId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifySocialBgAudio.audioId: " + audioId);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(SocialBackgroundAudioField.AUDIO_ID, audioId));
        boolean bool = writeAbleContentHandler.modifySocialBgAudio(queryExpress, updateExpress);
        if (bool) {
            socialContentCache.deleteSocialBgAudio(audioId);
        }
        return bool;
    }

    @Override
    public NextPageRows<SocialBackgroundAudio> querySocialBgAudioByNext(NextPagination nextPagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialWatermarkByNext.nextPagination: " + nextPagination);
        }
        return readonlyContentHandlersPool.getHandler().querySocialBgAudioByNext(nextPagination);
    }

    @Override
    public NextPageRows<SocialContentActivity> querySocialContentActivityByNext(long activityId, NextPagination nextPagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialWatermarkByNext.nextPagination: " + nextPagination);
        }
        return readonlyContentHandlersPool.getHandler().querySocialContentActivityByNext(activityId, nextPagination);
    }

    @Override
    public Map<Long, SocialActivity> querySocialActivityByIdSet(Set<Long> activityIdSet) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialActivityByIdSet.activityIdSet: " + activityIdSet);
        }
        Map<Long, SocialActivity> returnMap = new HashMap<Long, SocialActivity>();
        Set<Long> querySet = new HashSet<Long>();
        for (Long aid : activityIdSet) {
            SocialActivity activity = socialContentCache.getSocialActivity(aid);
            if (activity == null) {
                querySet.add(aid);
            } else {
                returnMap.put(aid, activity);
            }
        }
        if (!CollectionUtil.isEmpty(querySet)) {
            List<SocialActivity> list = readonlyContentHandlersPool.getHandler().querySocialActivity(new QueryExpress()
                    .add(QueryCriterions.in(SocialActivityField.ACTIVITY_ID, querySet.toArray())));
            if (!CollectionUtil.isEmpty(list)) {
                for (SocialActivity activity : list) {
                    returnMap.put(activity.getActivityId(), activity);
                    socialContentCache.putSocialActivity(activity.getActivityId(), activity);
                }
            }

        }
        return returnMap;
    }

    @Override
    public boolean modifySocialContentActivity(long activityId, long contentId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifySocialContentActivity.activityId: " + activityId + ",contentId:" + contentId);
        }
        return writeAbleContentHandler.modifySocialContentActivity(new QueryExpress()
                .add(QueryCriterions.eq(SocialContentActivityField.ACTIVITY_ID, activityId))
                .add(QueryCriterions.eq(SocialContentActivityField.CONTENT_ID, contentId)), updateExpress);
    }

    @Override
    public boolean sendOutSocialLogEvent(long foreignId, String uno, long contentId, SocialLogType logType, SocialLogCategory logCategory, int increaseValue, int platform, String channel) throws ServiceException {
        SocialLogEvent socialLogEvent = new SocialLogEvent();
        socialLogEvent.setForeignId(foreignId);
        socialLogEvent.setContentId(contentId);
        socialLogEvent.setUno(uno);
        socialLogEvent.setSocialLogType(logType);
        socialLogEvent.setSocialLogCategory(logCategory);
        socialLogEvent.setIncreaseValue(increaseValue);
        socialLogEvent.setPlatform(AppPlatform.getByCode(platform));
        socialLogEvent.setShareChannel(AppShareChannel.getByCode(channel));
        try {
            EventDispatchServiceSngl.get().dispatch(socialLogEvent);
        } catch (Exception e) {
            GAlerter.lab("ContentLogic sendOutSocialActivityEvent error.", e);
        }
        return true;
    }

    @Override
    public ForignContent getWikiContent(String wikiId, String wikiKey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifySocialContentActivity.wikiId: " + wikiId + ",wikiKey:" + wikiKey);
        }

        ForignContent forignContent = readonlyContentHandlersPool.getHandler().queryForignContentById(wikiId, ForignContentDomain.WIKI_CONTENT);

        if (forignContent == null) {
            forignContent = new ForignContent();
            forignContent.setContentUrl("");
            forignContent.setForignId(wikiId);
            forignContent.setContentDomain(ForignContentDomain.WIKI_CONTENT);
            forignContent.setContentTitle("");
            forignContent.setContentDesc("");
            forignContent.setCreateTime(new Date());
            forignContent.setKeyWords(wikiKey);

            forignContent = writeAbleContentHandler.insertForignContent(forignContent);
        }
        return forignContent;

    }

    /**
     * 热门文章 事件
     *
     * @param socialContentId
     */
    private void sendOutSocialHotContentInsertEvent(long socialContentId) {
        SocialHotContentEvent hotEvent = new SocialHotContentEvent();

        SocialContent socialContent = socialContentCache.getSocialContent(socialContentId);
        if (socialContent == null) {
            try {
                socialContent = getSocialContentByContentId(socialContentId);
            } catch (ServiceException e) {
                GAlerter.lab("ContentLogic querySocialContentByContentId occured ServiceException.", e);
            }
        }
        if (socialContent != null) {
            int replySum = socialContent.getReplyNum();
            int agreeSum = socialContent.getAgreeNum();
            if ((replySum + agreeSum) > SOCIAL_CONTENT_HOT_SIGN_SIZE) {
                hotEvent.setContentId(socialContent.getContentId());
                hotEvent.setUno(socialContent.getUno());
                try {
                    EventDispatchServiceSngl.get().dispatch(hotEvent);
                } catch (Exception e) {
                    GAlerter.lab("ContentLogic sendOutSocialHotContentInsertEvent error.", e);
                }
            }
        }


    }

    //消息
    private void sendOutPostNoticeEvent(String uno) {
        if (!StringUtil.isEmpty(uno)) {

            NoticeInsertEvent noticeInsertEvent = new NoticeInsertEvent();
            noticeInsertEvent.setCount(1);
            noticeInsertEvent.setOwnUno(uno);
            noticeInsertEvent.setNoticeType(NoticeType.SOCIAL_CLIENT);

            try {
                EventDispatchServiceSngl.get().dispatch(noticeInsertEvent);
            } catch (Exception e) {
                GAlerter.lab("ContentLogic sendOutPostNoticeEvent error.", e);
            }
        }
    }

    private void insertSocialLog(long foreignId, String uno, long contentId, SocialLogType logType, SocialLogCategory logCategory, AppPlatform platform, AppShareChannel channel) {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " insertSocialLog");
        }
        SocialLog socialLog = new SocialLog();
        socialLog.setForeignId(foreignId);
        socialLog.setUno(uno);
        socialLog.setContentId(contentId);
        socialLog.setLogType(logType);
        socialLog.setLogCategory(logCategory);
        socialLog.setCreateDate(new Date());
        socialLog.setPlatform(platform);
        socialLog.setShareChannel(channel);
        try {
            writeAbleContentHandlerMongo.insertSocialLog(socialLog);
        } catch (Exception e) {
            GAlerter.lab("ContentLogic insertSocialLog Exception.e", e);
        }
    }

    private void replySocialMessage(SocialContentReply reply) {
        if (StringUtil.isEmpty(reply.getContentUno()) || StringUtil.isEmpty(reply.getReplyUno())) {
            return;
        }
        Map<ObjectField, Object> propsMap = new HashMap<ObjectField, Object>();
        propsMap.put(SocialMessageField.MSG_BODY, reply.getBody());
        propsMap.put(SocialMessageField.MSG_TYPE, SocialMessageType.REPLY.getCode());
        propsMap.put(SocialMessageField.MSG_CATEGORY, SocialMessageCategory.DEFAULT_MSG.getCode());
        propsMap.put(SocialMessageField.SEND_UNO, reply.getReplyUno());
        propsMap.put(SocialMessageField.CONTENT_ID, reply.getContentId());
        propsMap.put(SocialMessageField.CONTENT_UNO, reply.getContentUno());
        propsMap.put(SocialMessageField.REPLY_ID, reply.getReplyId());
        propsMap.put(SocialMessageField.REPLY_UNO, reply.getReplyUno());
        propsMap.put(SocialMessageField.PARENT_ID, reply.getParentId());
        propsMap.put(SocialMessageField.PARENT_UNO, reply.getParentUno());
        propsMap.put(SocialMessageField.ROOT_ID, reply.getRootId());
        propsMap.put(SocialMessageField.ROOT_UNO, reply.getRootUno());
        propsMap.put(SocialMessageField.CREATE_DATE, new Date());

        if (!reply.getContentUno().equals(reply.getReplyUno())) {
            propsMap.put(SocialMessageField.OWN_UNO, reply.getContentUno());
            propsMap.put(SocialMessageField.RECEIVE_UNO, reply.getContentUno());

            GAlerter.lab("-----------------------------------------START SEND SOCIAL MESSAGE EVENT------------------------------------------------");
            sendSocialMessEvent(propsMap);
        }
        if (!StringUtil.isEmpty(reply.getRootUno()) && !reply.getRootUno().equals(reply.getContentUno()) && !reply.getRootUno().equals(reply.getReplyUno())) {
            propsMap.put(SocialMessageField.OWN_UNO, reply.getRootUno());
            propsMap.put(SocialMessageField.RECEIVE_UNO, reply.getRootUno());

            GAlerter.lab("-----------------------------------------START SEND SOCIAL MESSAGE EVENT------------------------------------------------");
            sendSocialMessEvent(propsMap);
        }
        if (!StringUtil.isEmpty(reply.getParentUno()) && !reply.getParentUno().equals(reply.getReplyUno()) && !reply.getParentUno().equals(reply.getContentUno()) && !reply.getParentUno().equals(reply.getRootUno())) {
            propsMap.put(SocialMessageField.OWN_UNO, reply.getParentUno());
            propsMap.put(SocialMessageField.RECEIVE_UNO, reply.getParentUno());

            GAlerter.lab("-----------------------------------------START SEND SOCIAL MESSAGE EVENT------------------------------------------------");
            sendSocialMessEvent(propsMap);
        }

    }

    private void sendSocialMessEvent(Map<ObjectField, Object> propsMap) {
        GAlerter.lab("-----------------------------------------START SEND SOCIAL MESSAGE EVENT------------------------------------------------");
        SocialMessageEvent event = new SocialMessageEvent();
        event.setOwnUno(propsMap.containsKey(SocialMessageField.OWN_UNO) ? String.valueOf(propsMap.get(SocialMessageField.OWN_UNO)) : "");
        event.setSendUno(propsMap.containsKey(SocialMessageField.SEND_UNO) ? String.valueOf(propsMap.get(SocialMessageField.SEND_UNO)) : "");
        event.setReceiveUno(propsMap.containsKey(SocialMessageField.RECEIVE_UNO) ? String.valueOf(propsMap.get(SocialMessageField.RECEIVE_UNO)) : "");
        event.setType(propsMap.containsKey(SocialMessageField.MSG_TYPE) ? (Integer) propsMap.get(SocialMessageField.MSG_TYPE) : 0);
        event.setMsgBody(propsMap.containsKey(SocialMessageField.MSG_BODY) ? String.valueOf(propsMap.get(SocialMessageField.MSG_BODY)) : "");
        event.setContentId(propsMap.containsKey(SocialMessageField.CONTENT_ID) ? (Long) propsMap.get(SocialMessageField.CONTENT_ID) : 0l);
        event.setContentUno(propsMap.containsKey(SocialMessageField.CONTENT_UNO) ? String.valueOf(propsMap.get(SocialMessageField.CONTENT_UNO)) : "");
        event.setReplyId(propsMap.containsKey(SocialMessageField.REPLY_ID) ? (Long) propsMap.get(SocialMessageField.REPLY_ID) : 0l);
        event.setReplyUno(propsMap.containsKey(SocialMessageField.REPLY_UNO) ? String.valueOf(propsMap.get(SocialMessageField.REPLY_UNO)) : "");
        event.setRootId(propsMap.containsKey(SocialMessageField.ROOT_ID) ? (Long) propsMap.get(SocialMessageField.ROOT_ID) : 0l);
        event.setRootUno(propsMap.containsKey(SocialMessageField.ROOT_UNO) ? String.valueOf(propsMap.get(SocialMessageField.ROOT_UNO)) : "");
        event.setParentId(propsMap.containsKey(SocialMessageField.PARENT_ID) ? (Long) propsMap.get(SocialMessageField.PARENT_ID) : 0l);
        event.setParentUno(propsMap.containsKey(SocialMessageField.PARENT_UNO) ? String.valueOf(propsMap.get(SocialMessageField.PARENT_UNO)) : "");
        event.setCreateDate(propsMap.containsKey(SocialMessageField.CREATE_DATE) ? (Date) propsMap.get(SocialMessageField.CREATE_DATE) : new Date());
        try {
            EventDispatchServiceSngl.get().dispatch(event);
        } catch (Exception e) {
            GAlerter.lab("ContentLogic sendSocialMessEvent error.", e);
        }
    }

    //保留1位小数
    private static double get2Double(double a) {
        DecimalFormat df = new DecimalFormat("0.0");
        return new Double(df.format(a).toString());
    }

}
