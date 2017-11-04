/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.advertise;


import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.advertise.AdvertiseHandler;
import com.enjoyf.platform.db.advertise.AdvertiseIdGenerator;
import com.enjoyf.platform.db.advertise.AdvertiseStatHandler;
import com.enjoyf.platform.db.advertise.app.AppAdvertiseHandler;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.props.hotdeploy.AdvertiseHotdeployConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.advertise.*;
import com.enjoyf.platform.service.advertise.app.*;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.system.*;
import com.enjoyf.platform.service.event.user.UserEvent;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */

/**
 * The AccountLogic class holds the core logic for the server.
 * This class is expected to change almost completely from
 * server to server. <p>
 * <p/>
 * AccountLogic is called by AccountPacketDecoder.
 */
public class AdvertiseLogic implements AdvertiseService {
    //
    private static final Logger logger = LoggerFactory.getLogger(AdvertiseLogic.class);

    //
    private AdvertiseConfig config;
    private AdvertiseHotdeployConfig advertiseHotdeployConfig = HotdeployConfigFactory.get().getConfig(AdvertiseHotdeployConfig.class);

    //the handler's
    private AdvertiseHandler writeAbleAccountHandler;
    private HandlerPool<AdvertiseHandler> readonlyAccountHandlersPool;

    private AppAdvertiseHandler writeAbleAppAccountHandler;
    private HandlerPool<AppAdvertiseHandler> readonlyAppHandlersPool;

    private QueueThreadN eventProcessQueueThreadN = null;

    private AdvertiseUrlCache advertiseCache;
    private AppAdvertiseCache appAdvertiseCache;

    private AdvertiseStatHandler statHandler;
    private HandlerPool<AdvertiseStatHandler> statReadOnlyHandlerPool;

    private AdvertiseRedis advertiseRedis;

    AdvertiseLogic(AdvertiseConfig cfg) {
        config = cfg;

        //initialize the handler.
        try {
            //
            writeAbleAccountHandler = new AdvertiseHandler(config.getWriteableDataSourceName(), config.getProps());

            writeAbleAppAccountHandler = new AppAdvertiseHandler(config.getWriteableDataSourceName(), config.getProps());

            //
            readonlyAccountHandlersPool = new HandlerPool<AdvertiseHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyAccountHandlersPool.add(new AdvertiseHandler(dsn, config.getProps()));
            }

            //
            readonlyAppHandlersPool = new HandlerPool<AppAdvertiseHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyAppHandlersPool.add(new AppAdvertiseHandler(dsn, config.getProps()));
            }

            try {
                statHandler = new AdvertiseStatHandler(config.getMongoDbWriteAbleDateSourceName(), config.getProps());

                statReadOnlyHandlerPool = new HandlerPool<AdvertiseStatHandler>();
                for (String dsn : config.getMongoDbReadonlyDataSourceNames()) {
                    statReadOnlyHandlerPool.add(new AdvertiseStatHandler(dsn, config.getProps()));
                }
            } catch (DbException e) {
                GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());
                Utility.sleep(5000);
                System.exit(0);
            }

        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());

            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        //initialize the cache setting.
        // init queue thread
        eventProcessQueueThreadN = new QueueThreadN(config.getEventProcessQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                processQueuedEvent(obj);
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "eventProcessQueue"));

        advertiseCache = new AdvertiseUrlCache(config.getMemCachedConfig());
        appAdvertiseCache = new AppAdvertiseCache(config.getMemCachedConfig());

        // advertiseRedis = new AdvertiseRedis(config.getProps());
    }


    private void processQueuedEvent(Object event) {
        AdvertiseEvent advertiseEvent = null;

        if (event instanceof AdvertisePublishClickEvent) {
            //
            processAdvertisePublishClickEvent((AdvertisePublishClickEvent) event);
        } else if (event instanceof AdvertisePageViewEvent) {
            //
            processAdvertisePageViewEvent((AdvertisePageViewEvent) event);
        } else if (event instanceof AdvertiseUserRegisterEvent) {
            //
            processAdvertiseUserRegisterEvent((AdvertiseUserRegisterEvent) event);
        } else if (event instanceof UserEvent) {
            //
            processUserEvent((UserEvent) event);
        } else if (event instanceof AdvertiseDeviceClickEvent) {
            processClickEvent((AdvertiseDeviceClickEvent) event);
        } else if (event instanceof AdvertiseDeviceActivityEvent) {
            processActivityEvent((AdvertiseDeviceActivityEvent) event);
        } else {
            GAlerter.lab("In eventProcessQueueThreadN, there is a unknown obj.");
        }
    }

    private void processAdvertisePublishClickEvent(AdvertisePublishClickEvent srcEvent) {
        //
        AdvertiseEvent advertiseEvent = new AdvertiseEvent();

        advertiseEvent.setPublishId(srcEvent.getPublishId());
        advertiseEvent.setLocationCode(srcEvent.getLocationCode());

        advertiseEvent.setEventType(AdvertiseEventType.CLICK);
        advertiseEvent.setGlobalId(srcEvent.getGlobalId());
        advertiseEvent.setSessionId(srcEvent.getSessionId());

        advertiseEvent.setEventCount(1);

        advertiseEvent.setEventDate(srcEvent.getEventDate());
        advertiseEvent.setEventIp(srcEvent.getEventIp());

        //insert the click event.
        insertAdvertiseEvent(advertiseEvent);

        //check the location code. todo.
    }

    private void processAdvertisePageViewEvent(AdvertisePageViewEvent srcEvent) {
        //
        AdvertiseEvent advertiseEvent = new AdvertiseEvent();

        advertiseEvent.setPublishId(srcEvent.getPublishId());
        advertiseEvent.setLocationCode(srcEvent.getLocationCode());

        advertiseEvent.setEventType(AdvertiseEventType.VIEW);
        advertiseEvent.setGlobalId(srcEvent.getPageViewEvent().getGlobalId());
        advertiseEvent.setSessionId(srcEvent.getPageViewEvent().getSessionId());
        advertiseEvent.setUno(srcEvent.getPageViewEvent().getUno());

        advertiseEvent.setEventCount(1);
        advertiseEvent.setEventDesc(srcEvent.getPageViewEvent().getLocationUrl());

        advertiseEvent.setEventDate(srcEvent.getPageViewEvent().getEventDate());
        advertiseEvent.setEventIp(srcEvent.getPageViewEvent().getEventIp());

        //insert the click event.
        insertAdvertiseEvent(advertiseEvent);
    }

    private void processAdvertiseUserRegisterEvent(AdvertiseUserRegisterEvent srcEvent) {
        //
        AdvertiseUserPublishRelation relation = new AdvertiseUserPublishRelation();
        relation.setCreateIp(srcEvent.getEventIp());

        relation.setLocationCode(srcEvent.getLocationCode());
        relation.setPublishId(srcEvent.getPublishId());

        relation.setUno(srcEvent.getUno());

        try {
            writeAbleAccountHandler.insertUserPublishRelation(relation);
        } catch (DbException e) {
            GAlerter.lab("AdvertiseLogic processAdvertiseUserRegisterEvent insertUserPublishRelation error.", e);
        }
    }

    private void processUserEvent(UserEvent srcEvent) {
        //check the support setting.
        if (advertiseHotdeployConfig.supportUserEvent(srcEvent.getEventType())) {
            //get the relation
            AdvertiseUserPublishRelation relation = null;

            QueryExpress getExpress = new QueryExpress();
            getExpress.add(QueryCriterions.eq(AdvertiseUserPublishRelationField.UNO, srcEvent.getSrcUno()));

            try {
                relation = readonlyAccountHandlersPool.getHandler().getUserPublishRelation(getExpress);
            } catch (Exception e) {
                //
                GAlerter.lab("AdvertiseLogic processUserEvent getUserPublishRelation error.", e);
            }

            //if not from advertise, just ignore.
            if (relation != null) {
                //
                AdvertiseEvent advertiseEvent = new AdvertiseEvent();

                //
                advertiseEvent.setPublishId(relation.getPublishId());
                advertiseEvent.setLocationCode(relation.getLocationCode());

                //
                advertiseEvent.setEventType(advertiseHotdeployConfig.getAdvertiseEventType(srcEvent.getEventType()));

                //
                advertiseEvent.setUno(srcEvent.getSrcUno());

                advertiseEvent.setEventCount(1);

                advertiseEvent.setEventDate(srcEvent.getEventDate());
                advertiseEvent.setEventIp(srcEvent.getEventIp());

                //insert the click event.
                insertAdvertiseEvent(advertiseEvent);
            }
        }
    }

    private AdvertiseEvent insertAdvertiseEvent(AdvertiseEvent event) {
        //if the event is not null, insert into db.
        if (event != null) {
            try {
                event = writeAbleAccountHandler.insertEvent(event);
            } catch (Exception e) {
                //
                GAlerter.lab("AdvertiseLogic insertAdvertiseEvent error.", e);
            }
        }

        return event;
    }

    @Override
    public AdvertiseAgent createAgent(AdvertiseAgent agent) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createAgent, agent:" + agent);
        }

        return writeAbleAccountHandler.insertAgent(agent);
    }

    @Override
    public AdvertiseAgent getAgent(String agentId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getAgent, agentId:" + agentId);
        }

        //
        QueryExpress getExpress = new QueryExpress();
        getExpress.add(QueryCriterions.eq(AdvertiseAgentField.AGENTID, agentId));

        return readonlyAccountHandlersPool.getHandler().getAgent(getExpress);
    }

    @Override
    public boolean modifyAgent(UpdateExpress updateExpress, String agentId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyAgent, updateExpress:" + updateExpress + ", agentId:" + agentId);
        }

        //
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AdvertiseAgentField.AGENTID, agentId));

        return writeAbleAccountHandler.updateAgent(updateExpress, queryExpress) > 0;
    }

    @Override
    public PageRows<AdvertiseAgent> queryPageAgents(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryPageAgents, queryExpress:" + queryExpress + ", page:" + page);
        }

        return readonlyAccountHandlersPool.getHandler().queryAgents(queryExpress, page);
    }

    @Override
    public List<AdvertiseAgent> queryAgents(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryAgents, queryExpress:" + queryExpress);
        }

        return readonlyAccountHandlersPool.getHandler().queryAgents(queryExpress);
    }

    @Override
    public AdvertiseProject createProject(AdvertiseProject project) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createProject, project:" + project);
        }

        return writeAbleAccountHandler.insertProject(project);
    }

    @Override
    public AdvertiseProject getProject(String projectId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getProject, projectId:" + projectId);
        }

        //
        QueryExpress getExpress = new QueryExpress();
        getExpress.add(QueryCriterions.eq(AdvertiseProjectField.PROJECTID, projectId));

        return readonlyAccountHandlersPool.getHandler().getProject(getExpress);
    }

    @Override
    public boolean modifyProject(UpdateExpress updateExpress, String projectId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyProject, updateExpress:" + updateExpress);
        }

        //
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AdvertiseProjectField.PROJECTID, projectId));

        return writeAbleAccountHandler.updateProject(updateExpress, queryExpress) > 0;
    }

    @Override
    public PageRows<AdvertiseProject> queryPageProjects(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryPageProjects, queryExpress:" + queryExpress + ", page:" + page);
        }

        return readonlyAccountHandlersPool.getHandler().queryProjects(queryExpress, page);
    }

    @Override
    public List<AdvertiseProject> queryProjects(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryProjects, queryExpress:" + queryExpress);
        }

        return readonlyAccountHandlersPool.getHandler().queryProjects(queryExpress);
    }

    @Override
    public AdvertisePublish createPublish(AdvertisePublish publish) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createPublish, publish:" + publish);
        }


        return writeAbleAccountHandler.insertPublish(publish);
    }

    @Override
    public AdvertisePublish getPublish(String publishId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getPublish, publishId:" + publishId);
        }

        //
        QueryExpress getExpress = new QueryExpress();
        getExpress.add(QueryCriterions.eq(AdvertisePublishField.PUBLISHID, publishId));

        return readonlyAccountHandlersPool.getHandler().getPublish(getExpress);
    }

    @Override
    public boolean modifyPublish(UpdateExpress updateExpress, String publishId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyPublish, updateExpress:" + updateExpress + ", publishId:" + publishId);
        }

        //
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AdvertisePublishField.PUBLISHID, publishId));

        return writeAbleAccountHandler.updatePublish(updateExpress, queryExpress) > 0;
    }

    @Override
    public PageRows<AdvertisePublish> queryPagePublishs(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryPagePublishs, queryExpress:" + queryExpress + ", page:" + page);
        }

        return readonlyAccountHandlersPool.getHandler().queryPublishs(queryExpress, page);
    }

    @Override
    public PageRows<AdvertisePublish> queryPagePublishsByState(QueryExpress queryExpress, Pagination page, boolean state) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryPagePublishs, queryExpress:" + queryExpress + ", page:" + page);
        }

        PageRows<AdvertisePublish> pageRows = queryPagePublishs(queryExpress, page);

        if (state && !CollectionUtil.isEmpty(pageRows.getRows())) {
            for (AdvertisePublish publish : pageRows.getRows()) {
                publish.setAdvertiseAgent(getAgent(publish.getAgentId()));
                publish.setAdvertiseProject(getProject(publish.getProjectId()));
            }
        }

        return pageRows;
    }

    @Override
    public List<AdvertisePublish> queryPublishs(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryPublishs, queryExpress:" + queryExpress);
        }


        return readonlyAccountHandlersPool.getHandler().queryPublishs(queryExpress);
    }

    @Override
    public AdvertisePublishLocation createPublishLocation(AdvertisePublishLocation location) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createPublishLocation, location:" + location);
        }


        return writeAbleAccountHandler.insertPublishLocation(location);
    }

    @Override
    public AdvertisePublishLocation getPublishLocation(String publishId, String locationCode) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getPublishLocation, locationId:" + locationCode);
        }

        //
        QueryExpress getExpress = new QueryExpress();
        getExpress.add(QueryCriterions.eq(AdvertisePublishLocationField.PUBLISHID, publishId));
        getExpress.add(QueryCriterions.eq(AdvertisePublishLocationField.LOCATIONCODE, locationCode));

        return readonlyAccountHandlersPool.getHandler().getPublishLocation(getExpress);
    }

    @Override
    public boolean modifyPublishLocation(UpdateExpress updateExpress, String publishId, String locationCode) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyPublishLocation, updateExpress:" + updateExpress + ", publishId:" + publishId + ", locationId:" + locationCode);
        }

        //
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AdvertisePublishLocationField.PUBLISHID, publishId));
        queryExpress.add(QueryCriterions.eq(AdvertisePublishLocationField.LOCATIONCODE, locationCode));

        return writeAbleAccountHandler.updatePublishLocation(updateExpress, queryExpress) > 0;
    }

    @Override
    public PageRows<AdvertisePublishLocation> queryPagePublishLocations(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryPagePublishLocations, queryExpress:" + queryExpress + ", page:" + page);
        }


        return readonlyAccountHandlersPool.getHandler().queryPublishLocations(queryExpress, page);
    }

    @Override
    public List<AdvertisePublishLocation> queryPublishLocations(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryPublishLocations, queryExpress:" + queryExpress);
        }


        return readonlyAccountHandlersPool.getHandler().queryPublishLocations(queryExpress);
    }

    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("receiveEvent, event:" + event);
        }

        //
        eventProcessQueueThreadN.add(event);

        return true;
    }

    @Override
    public AdvertiseAppUrl insertAppUrl(AdvertiseAppUrl advertiseAppUrl) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to AdvertiseAppUrl, AdvertiseAppUrl:" + advertiseAppUrl);
        }
        advertiseAppUrl.setCode(AdvertiseIdGenerator.generateAppUrlId(System.currentTimeMillis()));
        return writeAbleAccountHandler.insertAppUrl(advertiseAppUrl);
    }

    @Override
    public AdvertiseAppUrl getAppUrl(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to AdvertiseAppUrl, QueryExpress:" + queryExpress);
        }
        return readonlyAccountHandlersPool.getHandler().getAdvertiseAppUrl(queryExpress);
    }

    @Override
    public AdvertiseAppUrl getAppUrlByCode(String code) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to AdvertiseAppUrl, code:" + code);
        }
        AdvertiseAppUrl advertiseAppUrl = advertiseCache.getAppUrl(code);
        if (advertiseAppUrl == null) {
            advertiseAppUrl = readonlyAccountHandlersPool.getHandler().getAdvertiseAppUrl(new QueryExpress().add(QueryCriterions.eq(AdvertiseAppUrlField.CODE, code)).add(QueryCriterions.eq(AdvertiseAppUrlField.REMOVESTATUS, ActStatus.UNACT.getCode())));
            if (advertiseAppUrl != null) {
                advertiseCache.putAppUrl(advertiseAppUrl);
            }
        }
        return advertiseAppUrl;
    }

    @Override
    public boolean modifyAppUrl(UpdateExpress updateExpress, QueryExpress queryExpress, String code) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to AdvertiseAppUrl, UpdateExpress:" + updateExpress + " QueryExpress=" + queryExpress);
        }
        boolean bool = writeAbleAccountHandler.modifyAdvertiseAppUrl(updateExpress, queryExpress);
        if (bool) {
            advertiseCache.deleteAppUrl(code);
        }
        return bool;
    }

    @Override
    public List<AdvertiseAppUrl> listAppUrl(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to AdvertiseAppUrl, QueryExpress:" + queryExpress);
        }
        return readonlyAccountHandlersPool.getHandler().listAdvertiseAppUrl(queryExpress);
    }

    @Override
    public PageRows<AdvertiseAppUrl> pageAppUrl(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to AdvertiseAppUrl, QueryExpress:" + queryExpress + " Pagination =" + pagination);
        }
        return readonlyAccountHandlersPool.getHandler().pageAdvertiseAppUrl(queryExpress, pagination);
    }


    @Override
    public AppAdvertise createAppAdvertise(AppAdvertise appAdvertise) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createAppAdvertise, AppAdvertise:" + appAdvertise);
        }
        return writeAbleAppAccountHandler.insertAppAdvertise(appAdvertise);
    }

    @Override
    public boolean modifyAppAdvertise(UpdateExpress updateExpress, long appAdvertiseId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createAppAdvertise, UpdateExpress:" + updateExpress + ",AppAdvertiseId=" + appAdvertiseId);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AppAdvertiseField.ADVERTISE_ID, appAdvertiseId));
        boolean bval = writeAbleAppAccountHandler.updateAdvertise(updateExpress, queryExpress);
        if (bval) {
            appAdvertiseCache.deleteAppAdvertiseById(appAdvertiseId);
        }
        return bval;
    }

    @Override
    public PageRows<AppAdvertise> queryAppAdvertise(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryAppAdvertise, QueryExpress:" + queryExpress + ",Pagination:" + pagination);
        }
        return readonlyAppHandlersPool.getHandler().pageQueryAdvertise(queryExpress, pagination);
    }

    @Override
    public AppAdvertise getAppAdvertise(long appAdvertiseId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getAppAdvertise, appAdvertiseId:" + appAdvertiseId);
        }
        AppAdvertise appAdvertise = appAdvertiseCache.getAppAdvertisById(appAdvertiseId);
        if (appAdvertise == null) {
            appAdvertise = readonlyAppHandlersPool.getHandler().getAppAdvertise(appAdvertiseId);
            if (appAdvertise != null) {
                appAdvertiseCache.putAppAdvertiseById(appAdvertise);
            }
        }
        return appAdvertise;
    }

    @Override
    public AppAdvertisePublish createAppAdertisePublish(AppAdvertisePublish publish) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createAppAdertisePublish, publish:" + publish);
        }
        appAdvertiseCache.deleteAppAdvertisePublish(publish.getAppkey(), publish.getPublishType().getCode(), publish.getChannel() == null ? "" : publish.getChannel().getCode());
        return writeAbleAppAccountHandler.insertAppAdvertisePublish(publish);
    }

    @Override
    public AppAdvertisePublish getAppAdvertisePublish(long publishId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getAppAdvertisePublish, publishId:" + publishId);
        }
        return readonlyAppHandlersPool.getHandler().getAppAdvertisePublish(publishId);
    }

    @Override
    public boolean modifyAppAdvertisePublish(UpdateExpress updateExpress, long publishId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyAppAdvertisePublish, publishId:" + publishId + ",UpdateExpress=" + updateExpress);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(AppAdvertisePublishField.PUBLISH_ID, publishId));
        AppAdvertisePublish publish = readonlyAppHandlersPool.getHandler().getAppAdvertisePublish(publishId);
        if (publish == null) {
            return false;
        }
        boolean bool = writeAbleAppAccountHandler.modifyAppAdvertisePublish(updateExpress, queryExpress, publishId);
        if (bool) {
            appAdvertiseCache.deleteAppAdvertisePublish(publish.getAppkey(), publish.getPublishType().getCode(), publish.getChannel() == null ? "" : publish.getChannel().getCode());
        }
        return bool;
    }

    @Override
    public PageRows<AppAdvertisePublish> queryAppAdvertisePublish(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryAppAdvertisePublish, QueryExpress:" + queryExpress + ",Pagination=" + pagination);
        }
        return readonlyAppHandlersPool.getHandler().pageQueryAppAdvertisePublish(queryExpress, pagination);
    }


    @Override
    public List<AppAdvertiseInfo> queryPublishingAdvertise(String appKey, AppAdvertisePublishType type, Date now) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryPublishingAdvertise, appKey:" + appKey + " now:" + now);
        }

        List<AppAdvertiseInfo> infoList = appAdvertiseCache.getAppAdvertises(appKey, type);
        if (infoList == null) {
            infoList = new ArrayList<AppAdvertiseInfo>();
            //query publish from db
            QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(AppAdvertisePublishField.APP_KEY, appKey))
                    .add(QueryCriterions.lt(AppAdvertisePublishField.START_TIME, now))
                    .add(QueryCriterions.gt(AppAdvertisePublishField.END_TIME, now))
                    .add(QueryCriterions.gt(AppAdvertisePublishField.PUBLISH_TYPE, type.getCode()))
                    .add(QueryCriterions.gt(AppAdvertisePublishField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            List<AppAdvertisePublish> publishList = readonlyAppHandlersPool.getHandler().queryAdvertisePublish(queryExpress);

            Long[] advertiseIds = new Long[publishList.size()];
            int i = 0;
            for (AppAdvertisePublish publish : publishList) {
                advertiseIds[i] = publish.getAdvertiseId();
                i++;
            }

            //query advertise from cache
            QueryExpress adExpress = new QueryExpress();
            adExpress.add(QueryCriterions.in(AppAdvertiseField.ADVERTISE_ID, advertiseIds));
            adExpress.add(QueryCriterions.eq(AppAdvertiseField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            List<AppAdvertise> adList = readonlyAppHandlersPool.getHandler().queryAdvertise(adExpress);
            Map<Long, AppAdvertise> map = new HashMap<Long, AppAdvertise>();
            for (AppAdvertise appAdvertise : adList) {
                map.put(appAdvertise.getPublishId(), appAdvertise);
            }

            for (AppAdvertisePublish publish : publishList) {
                AppAdvertise advertise = map.get(publish.getPublishId());
                if (advertise == null) {
                    continue;
                }

                AppAdvertiseInfo info = new AppAdvertiseInfo();
                info.setAppAdvertise(advertise);
                info.setStartDate(publish.getStartTime());
                info.setEndDate(publish.getEndTime());
                info.setPublishId(publish.getPublishId());
                info.setPublishParam(publish.getPublishParam());
                infoList.add(info);
            }
            //put to cache
            appAdvertiseCache.putAppAdvertises(appKey, type, infoList);
        }

        //遍历查找有效期的APP
        List<AppAdvertiseInfo> returnList = new ArrayList<AppAdvertiseInfo>();
        for (AppAdvertiseInfo info : infoList) {
            if (info.getStartDate().getTime() < now.getTime() && info.getEndDate().getTime() > now.getTime()) {
                returnList.add(info);
            }
        }

        return infoList;
    }

    @Override
    public List<AppAdvertisePublish> queryAppAdvertisePublishByCache(String appId, AppAdvertisePublishType publishType, String channel) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryAppAdvertisePublishByCache.appId:" + appId + ",publishType:" + publishType.getCode());
        }
        Date now = new Date();
        List<AppAdvertisePublish> list = appAdvertiseCache.getAppAdvertisePublish(appId, publishType.getCode(), channel);
        if (CollectionUtil.isEmpty(list)) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(AppAdvertisePublishField.APP_KEY, appId));
            queryExpress.add(QueryCriterions.eq(AppAdvertisePublishField.PUBLISH_TYPE, publishType.getCode()));
            queryExpress.add(QueryCriterions.eq(AppAdvertisePublishField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            queryExpress.add(QueryCriterions.leq(AppAdvertisePublishField.START_TIME, now));
            queryExpress.add(QueryCriterions.geq(AppAdvertisePublishField.END_TIME, now));
            queryExpress.add(QueryCriterions.eq(AppAdvertisePublishField.CHANNEL, channel));
            list = readonlyAppHandlersPool.getHandler().queryAdvertisePublish(queryExpress);
            if (!CollectionUtil.isEmpty(list)) {
                appAdvertiseCache.putAppAdvertisePublish(appId, publishType.getCode(), channel, list);
            }
        }

        //如果广告为空，去查询全部
        if (CollectionUtil.isEmpty(list)) {
            list = appAdvertiseCache.getAppAdvertisePublish(appId, publishType.getCode(), "");
            if (CollectionUtil.isEmpty(list)) {
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(AppAdvertisePublishField.APP_KEY, appId));
                queryExpress.add(QueryCriterions.eq(AppAdvertisePublishField.PUBLISH_TYPE, publishType.getCode()));
                queryExpress.add(QueryCriterions.eq(AppAdvertisePublishField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
                queryExpress.add(QueryCriterions.leq(AppAdvertisePublishField.START_TIME, now));
                queryExpress.add(QueryCriterions.geq(AppAdvertisePublishField.END_TIME, now));
                queryExpress.add(QueryCriterions.eq(AppAdvertisePublishField.CHANNEL, ""));
                list = readonlyAppHandlersPool.getHandler().queryAdvertisePublish(queryExpress);
                if (!CollectionUtil.isEmpty(list)) {
                    appAdvertiseCache.putAppAdvertisePublish(appId, publishType.getCode(), "", list);
                }
            }
        }

        return list;
    }

    @Override
    public Map<Long, AppAdvertise> queryAppAdvertiseByIdSet(Set<Long> advertiseIdSet, int platform) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryAppAdvertiseByIdSet.advertiseIdSet:" + advertiseIdSet + ",platform:" + platform);
        }
        Map<Long, AppAdvertise> returnMap = new HashMap<Long, AppAdvertise>();
        Set<Long> querySet = new HashSet<Long>();
        for (Long aid : advertiseIdSet) {
            AppAdvertise advertise = appAdvertiseCache.getAppAdvertisById(aid);
            if (advertise != null) {
                if (advertise.getAppPlatform().getCode() == platform) {
                    returnMap.put(aid, advertise);
                }
            } else {
                querySet.add(aid);
            }
        }
        if (!CollectionUtil.isEmpty(querySet)) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.in(AppAdvertiseField.ADVERTISE_ID, querySet.toArray()));
            queryExpress.add(QueryCriterions.eq(AppAdvertiseField.ADVERTISE_PLATFORM, platform));
            queryExpress.add(QueryCriterions.eq(AppAdvertiseField.REMOVE_STATUS, ActStatus.UNACT.getCode()));
            List<AppAdvertise> advertiseList = readonlyAppHandlersPool.getHandler().queryAdvertise(queryExpress);
            if (!CollectionUtil.isEmpty(advertiseList)) {
                for (AppAdvertise advertise : advertiseList) {
                    returnMap.put(advertise.getAdvertiseId(), advertise);
                }
            }
        }
        return returnMap;
    }

    @Override
    public AppAdvertisePv createAppAdvertisePvMongo(AppAdvertisePv appAdvertisePv) throws ServiceException {
        return writeAbleAppAccountHandler.insert(appAdvertisePv);
    }

    public AdvertiseAppUrlClickInfo reportAppUrlClick(AdvertiseAppUrlClickInfo info) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call mongo handler reportAdvertsieAppUrlClickClick." + info);
        }

        return statHandler.insert(info);
    }


    private void processClickEvent(AdvertiseDeviceClickEvent event) {

        advertiseRedis.putClickDevice(event.getDeviceId(), event.getAgentId());
        //todo write to db report clickDevice

//        //valid is click and jihuo ex: dianru
//        if (event.getStatus().equals(IntValidStatus.VALID)) {
//            AdvertiseDeviceActivityEvent adaEvent = new AdvertiseDeviceActivityEvent();
//            adaEvent.setAgentId(event.getAgentId());
//            adaEvent.setCreateIp(event.getCreateIp());
//            adaEvent.setCreateTime(event.getCreateTime());
//            adaEvent.setAppkey(event.getAppkey());
//            adaEvent.setThirdAppKey(event.getThirdAppKey());
//            adaEvent.setDeviceId(event.getDeviceId());
//            processActivityEvent(adaEvent);
//        }

    }

    private void processActivityEvent(AdvertiseDeviceActivityEvent event) {

        //1、设备已经存在
        if (advertiseRedis.isExistsActivityDevice(event.getAppkey(), event.getDeviceId())) {
            logger.info(this.getClass().getName() + " processActivityEvent failed,has activity. event.getDeviceId:" + event.getDeviceId());
            return;
        }

        //2、获取redis点击事件的idfa--->agent 看是否和激活事件的agent一样
        String agentId = advertiseRedis.getClickDevice(event.getDeviceId());
        if (StringUtil.isEmpty(agentId) || !agentId.equals(event.getAgentId())) {
            logger.info(this.getClass().getName() + " processActivityEvent failed,agent match wrong:" + event.getDeviceId() + " event.getAgentId:" + event.getAgentId() + " redis agent:" + agentId);
            return;
        }

        //todo write to db report actvitydevice

        advertiseRedis.pushActivityDevice(event.getAppkey(), event.getDeviceId());

        //todo send to agent callback
    }

    @Override
    public Set<String> checkActivityDevice(String appkey, Set<String> deviceIds) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " checkActivityDevice deviceIds:" + deviceIds);
        }

        Set<String> result = new HashSet<String>();
        //节省性能为空不做逻辑
        if (CollectionUtil.isEmpty(deviceIds)) {
            return result;
        }

        //只有一个值直接返回
        if (deviceIds.size() == 1 && advertiseRedis.isExistsActivityDevice(appkey, deviceIds.iterator().next())) {
            result.addAll(deviceIds);
            return result;
        }

        //有多个请求的时候
        Map<String, Set<String>> map = new HashMap<String, Set<String>>();
        for (String deviceId : deviceIds) {
            String key = advertiseRedis.getActivityDeviceKey(appkey, deviceId);

            if (!map.containsKey(key)) {
                map.put(key, new HashSet<String>());
            }
            map.get(key).add(deviceId);
        }

        //取交集
        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
            result.addAll(advertiseRedis.checkActivityDevice(entry.getKey(), entry.getValue().toArray(new String[]{})));
        }

        return result;
    }
    /**
     * todo tips
     *
     * 提供排重接口：
     * 在排重接口的时候判断如果是点入的，发送AdvertiseDeviceClickEvent并且status为valid
     * 排重接口调用checkActivityDevice方法。
     *
     * 提供点击之后的接口：
     * 发送AdvertiseDeviceClickEvent并且status为validing(点入不用调用这个接口)
     *
     * 在report接口增加如下逻辑：
     * 如果platform是IOS，如果有init事件，发送一个AdvertiseDeviceActivityEvent
     * 考虑在service处理完activity事件后发送回调地址通知广告商
     *
     * 俩张表。一张activitydevice表 一张Event表
     */
}
