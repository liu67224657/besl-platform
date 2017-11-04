/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.joymeapp;


import com.enjoyf.mcms.bean.DedeArchives;
import com.enjoyf.mcms.bean.DedeArctype;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.db.joymeapp.ArchiveHandler;
import com.enjoyf.platform.db.joymeapp.JoymeAppHandler;
import com.enjoyf.platform.db.joymeapp.JoymeAppStatsHandler;
import com.enjoyf.platform.db.wikiurl.WikiUrlHandler;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.serv.joymeapp.quartz.AppFavoritePublishQuartzCronTrigger;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.system.AppPushMessageDeviceRegisterEvent;
import com.enjoyf.platform.service.event.system.GameClientMiyouPostEvent;
import com.enjoyf.platform.service.event.system.JoymeClientLineModifyEvent;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTV;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTag;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagAppType;
import com.enjoyf.platform.service.joymeapp.anime.AnimeTagField;
import com.enjoyf.platform.service.joymeapp.config.*;
import com.enjoyf.platform.service.joymeapp.gameclient.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;
import com.mongodb.BasicDBObject;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author <a href=mailto:ericliu@staff.joyme.com>Eirc Liu</a>
 */

/**
 * The JoymeAppLogic class holds the core logic for the server.
 * This class is expected to change almost completely from
 * server to server. <p>
 * <p/>
 * JoymeAppLogic is called by JoymeAppPacketDecoder.
 */
public class JoymeAppLogic implements JoymeAppService {
    //
    private static final Logger logger = LoggerFactory.getLogger(JoymeAppLogic.class);

    private static final long DEFAULT_NECESSARY_COMPLETE_ID = -1l;

    //
    private JoymeAppConfig config;

    //////////////
    private QueueThreadN eventQueueThreadN = null;

    private JoymeAppHandler writeAbleHandler;
    private HandlerPool<JoymeAppHandler> readonlyHandlersPool;

    private JoymeAppStatsHandler statsWriteAbleHandler;
    private HandlerPool<JoymeAppStatsHandler> statsReadOnlyHandlerPool;

    private JoymeAppCache joymeAppCache;

    private ClientLineCache clientLineCache;

    private CmsArchiveCache archiveCache;

    private ArchiveHandler archiveWriteHandler;
    //private HandlerPool<ArchiveHandler> archiveReadOnlyHandlerPool;

    private WikiUrlHandler wikiUrlWriteAbleHandler;
    //private HandlerPool<WikiUrlHandler> wikiUrlReadonlyHandlerPool;


    private JoymeAppRedis joymeAppRedis;

    public JoymeAppLogic(JoymeAppConfig cfg) {
        this.config = cfg;

        eventQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                if (obj instanceof AppPushMessageDeviceRegisterEvent) {

                } else if (obj instanceof GameClientMiyouPostEvent) {
                    GameClientMiyouPostEvent gcMiyouPostEvent = (GameClientMiyouPostEvent) obj;
                    processGameClientMiyouPostEvent(gcMiyouPostEvent);
                } else if (obj instanceof JoymeClientLineModifyEvent) {
                    JoymeClientLineModifyEvent joymeClientLineModifyEvent = (JoymeClientLineModifyEvent) obj;
                    processJoymeClientLineItemModifyEvent(joymeClientLineModifyEvent);
                } else {
                    GAlerter.lab("In eventQueueThreadN, there is a unknown obj.");
                }
            }


        }, new FQueueQueue(config.getQueueDiskStorePath(), "eventProcessQueue"));

        //initialize the handler.
        try {
            writeAbleHandler = new JoymeAppHandler(config.getWriteableDataSourceName(), config.getProps());

            readonlyHandlersPool = new HandlerPool<JoymeAppHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyHandlersPool.add(new JoymeAppHandler(dsn, config.getProps()));
            }
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());
            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        try {
            wikiUrlWriteAbleHandler = new WikiUrlHandler(config.getWikiUrlWriteableDataSourceName(), config.getProps());

        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());
            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        try {
            statsWriteAbleHandler = new JoymeAppStatsHandler(config.getMongoDbWriteAbleDateSourceName(), config.getProps());

            statsReadOnlyHandlerPool = new HandlerPool<JoymeAppStatsHandler>();
            for (String dsn : config.getMongoDbReadonlyDataSourceNames()) {
                statsReadOnlyHandlerPool.add(new JoymeAppStatsHandler(dsn, config.getProps()));
            }
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());
            Utility.sleep(5000);
            System.exit(0);
        }

        //initialize the handler.
        try {
            archiveWriteHandler = new ArchiveHandler(config.getCmsWriteableDataSourceName(), config.getProps());

//            archiveReadOnlyHandlerPool = new HandlerPool<ArchiveHandler>();
//            for (String dsn : config.getCmsDbReadonlyDataSourceNames()) {
//                archiveReadOnlyHandlerPool.add(new ArchiveHandler(dsn, config.getProps()));
//            }
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());
            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        joymeAppCache = new JoymeAppCache(config.getMemCachedConfig());
        clientLineCache = new ClientLineCache(config.getMemCachedConfig());
        archiveCache = new CmsArchiveCache(config.getMemCachedConfig());


        joymeAppRedis = new JoymeAppRedis(config.getProps());

        if (cfg.isAppFavPublishTriggerOpen()) {
            try {
                AppFavoritePublishQuartzCronTrigger cronTrigger = new AppFavoritePublishQuartzCronTrigger(this, cfg);

                cronTrigger.init();
                cronTrigger.start();
            } catch (SchedulerException e) {
                GAlerter.lab("WikiStatsQuartzCronTrigger start error.", e);
            }
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 04:00:00");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date firDate = sdf2.parse(sdf.format(new Date()));
            Date beginDate = new Date();
            if (beginDate.getTime() > firDate.getTime()) {
                Calendar date = Calendar.getInstance();
                date.setTime(beginDate);
                date.set(Calendar.DATE, date.get(Calendar.DATE) + 1);
                firDate = sdf2.parse(sdf.format(date.getTime()));
            }
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        List<AnimeTag> animeTags = readonlyHandlersPool.getHandler().queryAnimeTag(new QueryExpress().add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.SYHB_QUANZI.getCode())));
                        for (AnimeTag animeTag : animeTags) {
                            int val = (int) (Math.random() * 20 + 30);
                            writeAbleHandler.modifyAnimeTag(new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, animeTag.getTag_id())), new UpdateExpress().increase(AnimeTagField.TOTAL_SUM, val));
                        }

                    } catch (DbException e) {
                        GAlerter.lab(" run anime tag total_sum" + this.getClass(), e);
                    }

                    GAlerter.latd(this.getClass() + "run anime tag total_sum end. spent:" + ((System.currentTimeMillis() - new Date().getTime()) / 1000l));
                }
            }, firDate, 1000 * 60 * 60 * 12);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        reloadTagDedearchivesTimerTask();
    }

    private void reloadTagDedearchivesTimerTask() {
        try {
            List<AnimeTag> list = queryAnimeTag(new QueryExpress()
                    .add(QueryCriterions.eq(AnimeTagField.APP_TYPE, AnimeTagAppType.SYHB.getCode()))
                    .add(QueryCriterions.eq(AnimeTagField.REMOVE_STATUS, ValidStatus.VALID.getCode())));
            if (!CollectionUtil.isEmpty(list)) {
                for (AnimeTag tag : list) {
                    PageRows<String> idSet = joymeAppRedis.getTagDedearchivesTimerTaskCache(tag.getTag_id(), null);
                    if (idSet != null && !CollectionUtil.isEmpty(idSet.getRows())) {
                        for (String id : idSet.getRows()) {
                            String timer = joymeAppRedis.getTagDedearchivesTimer(id);
                            if (!StringUtil.isEmpty(timer)) {
                                try {
                                    scheduleTagDedearchivesTimerTask(tag.getTag_id(), id, new Date(Long.valueOf(timer)));
                                } catch (NumberFormatException e) {
                                    GAlerter.lab("JoymeAppLogic reloadTagDedearchivesTimerTask occur Exception.e", e);
                                }
                            }
                        }
                    }
                }
            }


        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    private void processGameClientMiyouPostEvent(GameClientMiyouPostEvent gcMiyouPostEvent) {

        TagDedearchives archive = new TagDedearchives();
        long tagId = AnimeUtil.TAG_ID_MIYOU_ARTICLE;
        archive.setTagid(tagId);
        archive.setArchiveContentType(ArchiveContentType.MIYOU_COMMENT);
        archive.setArchiveRelationType(ArchiveRelationType.TAG_RELATION);
        archive.setDede_archives_id(gcMiyouPostEvent.getDirectId());
        archive.setId(AnimeUtil.genTagArchiveId(tagId, gcMiyouPostEvent.getDirectId()));
        archive.setDede_archives_pubdate(gcMiyouPostEvent.getCreateTime().getTime());
        archive.setProfileId(gcMiyouPostEvent.getProfileId());
        archive.setDisplay_tag(StringUtil.isEmpty(gcMiyouPostEvent.getGroupId()) ? 0l : Long.parseLong(gcMiyouPostEvent.getGroupId()));
        archive.setDisplay_order(Long.valueOf(Integer.MAX_VALUE - (int) (System.currentTimeMillis() / 1000)));

        try {
            createTagDedearchives(archive);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured error:", e);
        }

    }

    private void processJoymeClientLineItemModifyEvent(JoymeClientLineModifyEvent joymeClientLineEvent) {
        try {
            String code = joymeClientLineEvent.getCode();
            String gameDbId = joymeClientLineEvent.getGameDbId();
            ClientLineItem clientLineItemParam = joymeClientLineEvent.getClientLineItem();
            if (StringUtil.isEmpty(code) || StringUtil.isEmpty(gameDbId) || null == clientLineItemParam) {
                GAlerter.lab(this.getClass().getName() + " param null error:" + joymeClientLineEvent);
                return;
            }
            ClientLine clientLine = getClientLineByCode(joymeClientLineEvent.getCode());
            if (clientLine != null) {
                //todo 避免用1 2等变量 可以用itemByQueryDb等...另外logic层避免调用service接口..可以用this.getClientLineItem
                ClientLineItem clientLineItemDB = getClientLineItem(new QueryExpress()
                        .add(QueryCriterions.eq(ClientLineItemField.LINE_ID, clientLine.getLineId()))
                        .add(QueryCriterions.eq(ClientLineItemField.DIRECT_ID, String.valueOf(joymeClientLineEvent.getGameDbId()))));
                if (clientLineItemDB == null) {
                    clientLineItemParam.setLineId(clientLine.getLineId());
                    clientLineItemParam.setItemType(clientLine.getItemType());
                    createClientLineItem(clientLineItemParam);
                } else if (!clientLineItemDB.getValidStatus().equals(ValidStatus.VALID)) {
                    UpdateExpress updateExpress = new UpdateExpress();
                    updateExpress.set(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode());
                    modifyClientLineItem(updateExpress, clientLineItemDB.getItemId());
                }
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured error:", e);
        }

    }

    public List<AppContentVersionInfo> queryContentVersionInfoByAppKey(String appKey, long version) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandlersPool queryContentInfo appKey:" + appKey + " version:" + version);
        }

        List<AppContentVersionInfo> returnObj = new ArrayList<AppContentVersionInfo>();

        List<AppContentVersionInfo> appContentVersionInfoList = joymeAppCache.getAppContentVersionList(appKey);

        //put cache
        if (appContentVersionInfoList == null) {
            appContentVersionInfoList = readonlyHandlersPool.getHandler().queryContentVersionInfo(new QueryExpress()
                    .add(QueryCriterions.eq(AppContentVersionField.APPKEY, appKey))
                    .add(QueryCriterions.eq(AppContentVersionField.REMOVESTTAUS, ActStatus.UNACT.getCode())));
            Collections.sort(appContentVersionInfoList);

            joymeAppCache.putAppContentVersionList(appKey, appContentVersionInfoList);
        }

        long lastNecessaryCompleteVersion = DEFAULT_NECESSARY_COMPLETE_ID;
        for (AppContentVersionInfo versionInfo : appContentVersionInfoList) {
            if (versionInfo.getCurrent_version() > version) {
                returnObj.add(versionInfo);
            }

            //如果是完整包，并且是必须要下的，break并且返回
            if (versionInfo.getPackageType() == ContentPackageType.COMPLETE.getCode() && versionInfo.isNecessaryUpdate()) {
                lastNecessaryCompleteVersion = versionInfo.getCurrent_version();
                break;
            }
        }

        if (CollectionUtil.isEmpty(returnObj)) {
            return returnObj;
        }

        if (lastNecessaryCompleteVersion != DEFAULT_NECESSARY_COMPLETE_ID && version >= lastNecessaryCompleteVersion) {
            //计算用户现在的版本号，如果versionNum<最近的必下完整包，如果遇到不是必下完整包删掉，只下载补丁
            for (AppContentVersionInfo versionInfo : returnObj) {
                if (versionInfo.getPackageType() == ContentPackageType.COMPLETE.getCode() && !versionInfo.isNecessaryUpdate()) {
                    returnObj.remove(versionInfo);
                }
            }
        } else {
            //versionNum>最后的值，返回patch+最近的一个完整包
            List<AppContentVersionInfo> tempList = new ArrayList<AppContentVersionInfo>();
            for (AppContentVersionInfo versionInfo : returnObj) {
                tempList.add(versionInfo);
                if (versionInfo.getPackageType() == ContentPackageType.COMPLETE.getCode()) {
                    break;
                }
            }
            returnObj = tempList;
        }

        return returnObj;
    }


    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" receiveEvent. event:" + event);
        }

        eventQueueThreadN.add(event);
        return true;
    }


    ///////////////////////////////////////////////////////////////
    @Override
    public Map<Integer, Archive> queryArchiveMapByIds(Set<Integer> archiveIdSet) {

        Map<Integer, Archive> returnMap = new HashMap<Integer, Archive>();

        for (Integer archiveId : archiveIdSet) {
            Archive archive = null;
            try {
                archive = getArchiveById(archiveId);
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " queryArchiveMapByIds occured Excpetion.e:", e);
            }

            if (archive != null) {
                returnMap.put(archive.getArchiveId(), archive);
            }
        }

        return returnMap;
    }

    public Archive getArchiveById(Integer archiveId) throws Exception {
        String aid = String.valueOf(archiveId);
        Archive archive = archiveCache.getArchive(aid);

        if (archive == null) {
            archive = archiveWriteHandler.getArchive(aid);
            if (archive != null) {
                archiveCache.putArchive(archive);
            }
        }

        return archive;
    }

    @Override
    public DedeArctype getqueryDedeArctype(Integer archiveTypeId) throws Exception {

        return archiveWriteHandler.getqueryDedeArctype(archiveTypeId);
    }


    @Override
    public Map<Integer, DedeArctype> queryDedeArctypeMapByTypeId(Set<Integer> archiveTypeIds) throws Exception {
        if (!CollectionUtil.isEmpty(archiveTypeIds)) {
            Map<Integer, DedeArctype> dedeArcTypeMap = new HashMap<Integer, DedeArctype>();
            for (Integer archiveTypeId : archiveTypeIds) {
                DedeArctype dedeArctype = archiveWriteHandler.getqueryDedeArctype(archiveTypeId);
                dedeArcTypeMap.put(archiveTypeId, dedeArctype);
            }
            return dedeArcTypeMap;
        } else {
            return null;
        }
    }
    ///ClientLine

    @Override
    public ClientLine getClientLine(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler getClientLine:QueryExpress" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getClientLine(queryExpress);
    }

    @Override
    public ClientLine getClientLineByCode(String code) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler getClientLineByCode:code " + code);
        }

        ClientLine line = clientLineCache.getClientLine(code);
        if (line == null) {
            //todo remove query
            line = readonlyHandlersPool.getHandler().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.CODE, code)));
            if (line != null) {
                clientLineCache.putClientLine(line);
            }
        }

        return line;
    }


    //为了着迷玩霸2.0.3热门页接口新加
    @Override
    public boolean removeClientLineFromCache(String code) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler removeClientLineFromCache:code " + code);
        }
        return clientLineCache.removeClientLine(code);
    }


    @Override
    public List<ClientLine> queryClientLineList(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryClientLineList:queryExpress" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryClientLine(queryExpress);
    }

    @Override
    public List<ClientLine> queryClientLine(ClientLineType lineType, Integer platform) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic queryClientLine.lineType:" + lineType + ",platform:" + platform);
        }
        List<ClientLine> lineList = new ArrayList<ClientLine>();

        Map<String, ClientLine> lineMap = new HashMap<String, ClientLine>();

        Set<String> lineCodeSet = joymeAppRedis.getClientLineCodeList(lineType, platform);
        List<String> dbCodeList = new ArrayList<String>();
        if (!CollectionUtil.isEmpty(lineCodeSet)) {
            for (String lineCode : lineCodeSet) {
                if (!StringUtil.isEmpty(lineCode)) {
                    ClientLine clientLine = ClientLine.parse(joymeAppRedis.getClientLine(lineCode));
                    if (clientLine != null) {
                        lineMap.put(lineCode, clientLine);
                        joymeAppRedis.putClientLine(lineCode, clientLine);
                    } else {
                        dbCodeList.add(lineCode);
                    }
                }
            }
            if (!CollectionUtil.isEmpty(dbCodeList)) {
                List<ClientLine> dbList = readonlyHandlersPool.getHandler().queryClientLine(new QueryExpress()
                        .add(QueryCriterions.in(ClientLineField.CODE, dbCodeList.toArray()))
                        .add(QueryCriterions.eq(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode())));
                if (!CollectionUtil.isEmpty(dbList)) {
                    for (ClientLine clientLine : dbList) {
                        lineMap.put(clientLine.getCode(), clientLine);
                        joymeAppRedis.putClientLine(clientLine.getCode(), clientLine);
                    }
                }
            }
            for (String lineCode : lineCodeSet) {
                if (lineMap.containsKey(lineCode)) {
                    lineList.add(lineMap.get(lineCode));
                }
            }
        } else {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, lineType.getCode()));
            if (platform != null) {
                queryExpress.add(QueryCriterions.eq(ClientLineField.PLATFORM, platform));
            }
            queryExpress.add(QueryCriterions.eq(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode()));
            queryExpress.add(QuerySort.add(ClientLineField.DISPLAY_ORDER, QuerySortOrder.ASC));
            lineList = readonlyHandlersPool.getHandler().queryClientLine(queryExpress);
            if (!CollectionUtil.isEmpty(lineList)) {
                for (ClientLine line : lineList) {
                    joymeAppRedis.putClientLineCodeList(lineType.getCode(), platform, line);
                    joymeAppRedis.putClientLine(line.getCode(), line);
                }
            }
        }
        return lineList;
    }

    @Override
    public PageRows<ClientLine> queryClientLineByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryClientLineByPage:queryExpress" + queryExpress + ",pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryClientLineByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyClientLine(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler modifyClientLine:updateExpress" + updateExpress + ",queryExpress:" + queryExpress);
        }
        return writeAbleHandler.modifyClientLine(updateExpress, queryExpress);
    }

    @Override
    public boolean
    modifyClientLineByCache(UpdateExpress updateExpress, Map<String, Object> param) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler modifyClientLine:updateExpress" + updateExpress + ",param:" + param.toString());
        }
        long lineId = param.containsKey("lineid") ? (Long) param.get("lineid") : 0l;
        String lineCode = param.containsKey("linecode") ? (String) param.get("linecode") : "";
        double order = param.containsKey("incorder") ? (Double) param.get("incorder") : 0d;
        int lineType = param.containsKey("linetype") ? (Integer) param.get("linetype") : -1;
        int platform = param.containsKey("platform") ? (Integer) param.get("platform") : -1;
        //非 删除、恢复 操作 一定 不要 传 status参数，
        String status = param.containsKey("status") ? (String) param.get("status") : "";

        boolean bool = false;
        if (lineId > 0l || !StringUtil.isEmpty(lineCode)) {
            QueryExpress queryExpress = new QueryExpress();
            if (lineId > 0l) {
                queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId));
            }
            if (!StringUtil.isEmpty(lineCode)) {
                queryExpress.add(QueryCriterions.eq(ClientLineField.CODE, lineCode));
            }
            bool = writeAbleHandler.modifyClientLine(updateExpress, queryExpress);
            if (bool) {
                //删除， 清出  redis
                if (status.equals(ValidStatus.REMOVED.getCode()) && lineType > -1 && platform > -1) {
                    joymeAppRedis.removeClientLineCodeList(lineType, platform, lineCode);
                    //恢复， 放入  redis
                } else if (status.equals(ValidStatus.VALID.getCode()) && lineType > -1 && platform > -1) {
                    ClientLine newLine = writeAbleHandler.getClientLine(queryExpress);
                    joymeAppRedis.putClientLineCodeList(lineType, platform, newLine);
                    joymeAppRedis.putClientLine(lineCode, newLine);
                    //排序
                } else if (order != 0d && lineType > -1 && platform > -1) {
                    joymeAppRedis.sortClientLineCodeList(lineType, platform, order, lineCode);
                    //普通 修改 重新 set 覆盖
                } else {
                    ClientLine newLine = writeAbleHandler.getClientLine(queryExpress);
                    joymeAppRedis.putClientLine(lineCode, newLine);
                }
            }
        }
        return bool;
    }

    @Override
    public ClientLine createClientLine(ClientLine clientLine) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler createClientLine:ClientLine" + clientLine);
        }
        ClientLine insertLine = writeAbleHandler.createClientLine(clientLine);
        if (insertLine != null) {
            joymeAppRedis.putClientLineCodeList(insertLine.getLineType().getCode(), insertLine.getPlatform(), insertLine);
            joymeAppRedis.putClientLine(insertLine.getCode(), insertLine);
        }
        return insertLine;
    }

    @Override
    public ClientLineItem getClientLineItem(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler getClientLineItem:QueryExpress" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getClientLineItem(queryExpress);
    }

    @Override
    public ClientLineItem getClientLineItemByCache(String lineCode, Long itemId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic getClientLineItemByCache.lineCode:" + lineCode + ",itemId:" + itemId);
        }
        ClientLineItem item = joymeAppRedis.getClientItem(String.valueOf(itemId));
        if (item == null) {
            item = readonlyHandlersPool.getHandler().getClientLineItem(new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.ITEM_ID, itemId)));
            if (item != null) {
                joymeAppRedis.putClientItem(String.valueOf(itemId), item);
            }
        }
        return item;
    }

    @Override
    public List<ClientLineItem> queryClientLineItemList(String lineCode) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryClientLineItemList:lineCode" + lineCode);
        }
        ClientLine line = this.getClientLineByCode(lineCode);
        List<ClientLineItem> returnList = new ArrayList<ClientLineItem>();
        if (line == null) {
            return returnList;
        }

        returnList = queryItemsByLineIdFromCache(line.getLineId());
        return returnList;
    }

    @Override
    public List<ClientLineItem> queryClientLineItem(String lineCode, QuerySort querySort, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic queryClientLineItemList:lineCode" + lineCode);
        }
        if (querySort == null) {
            querySort = QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC);
        }
        List<ClientLineItem> returnList = new ArrayList<ClientLineItem>();
        Set<String> itemIds = joymeAppRedis.getClientItemIdList(lineCode, pagination);
        if (CollectionUtil.isEmpty(itemIds)) {
            ClientLine line = this.getClientLineByCode(lineCode);
            if (line == null) {
                return returnList;
            }
            PageRows<ClientLineItem> pageRows = queryClientLineItemByPage(new QueryExpress()
                    .add(QueryCriterions.eq(ClientLineItemField.LINE_ID, line.getLineId()))
                    .add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()))
                    .add(querySort), pagination);
            if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                for (ClientLineItem item : pageRows.getRows()) {
                    if (item != null) {
                        returnList.add(item);
                        joymeAppRedis.putClientItemIdList(lineCode, item);
                    }
                }
            }
        } else {
            List<Long> dbList = new ArrayList<Long>();
            for (String itemId : itemIds) {
                ClientLineItem item = joymeAppRedis.getClientItem(itemId);
                if (item != null) {
                    returnList.add(item);
                } else {
                    dbList.add(Long.valueOf(itemId));
                }
            }
            if (!CollectionUtil.isEmpty(dbList)) {
                List<ClientLineItem> itemList = readonlyHandlersPool.getHandler().queryClientLineItem(new QueryExpress()
                        .add(QueryCriterions.in(ClientLineItemField.ITEM_ID, dbList.toArray()))
                        .add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()))
                        .add(querySort));
                if (!CollectionUtil.isEmpty(itemList)) {
                    for (ClientLineItem item : itemList) {
                        if (item != null) {
                            returnList.add(item);
                            joymeAppRedis.putClientItem(String.valueOf(item.getItemId()), item);
                        } else {
                            joymeAppRedis.removeClientItemIdList(lineCode, item);
                        }
                    }
                }
            }
        }
        return returnList;
    }

    //为了着迷玩霸2.0.3热门页接口的轮播图，链接，自定义楼层 by tony 2015-04-08
    @Override
    public List<ClientLineItem> queryClientLineItemListForHotPage(String lineCode) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryClientLineItemList:lineCode" + lineCode);
        }
        ClientLine line = this.getClientLineByCode(lineCode);
        List<ClientLineItem> returnList = new ArrayList<ClientLineItem>();
        if (line == null) {
            return returnList;
        }

        returnList = queryItemsByLineIdFromCacheDisplayOrderASC(line.getLineId());
        return returnList;
    }


    // 根据code查询ClientLineCache中的自定义缓存 by tony 2015-04-16
    @Override
    public List queryClientLineCustomCache(String customCode, String platform) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryClientLineCustomCache:customCode-->" + customCode + platform);
        }

        List gameCategoryResultList = new ArrayList();
        if (!StringUtil.isEmpty(customCode) && customCode.equals("gamecategory")) {

            gameCategoryResultList = clientLineCache.getClientCustom(customCode + platform);
            if (!CollectionUtil.isEmpty(gameCategoryResultList)) {
                return gameCategoryResultList;
            }
            gameCategoryResultList = new ArrayList();  //重新赋值
            String[] emptyArray = new String[0];
            //游戏分类
            QueryExpress queryExpressForGameCategory = new QueryExpress();
            queryExpressForGameCategory.add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.HOT_PAGE_FIRST_CATEGORY.getCode()));
            queryExpressForGameCategory.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.GAMECLIENT.getCode()));
            queryExpressForGameCategory.add(QueryCriterions.eq(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode()));
            queryExpressForGameCategory.add(QuerySort.add(ClientLineField.DISPLAY_ORDER, QuerySortOrder.ASC));

            List<ClientLine> gameCategoryBaseList = readonlyHandlersPool.getHandler().queryClientLine(queryExpressForGameCategory);


            if (!CollectionUtil.isEmpty(gameCategoryBaseList)) {
                for (int i = 0; i < gameCategoryBaseList.size(); i++) {
                    String code = gameCategoryBaseList.get(i).getCode();

                    if (code.endsWith(String.valueOf(platform))) {         //lineCode如果以0结尾是ios,以1结尾是android
                        Map baseItem = new LinkedHashMap();
                        baseItem.put("name", gameCategoryBaseList.get(i).getLineName());
                        List subList = new ArrayList();
                        QueryExpress queryExpress = new QueryExpress();
                        queryExpress.add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.HOT_PAGE_SECOND_CATEGORY.getCode()));
                        queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.GAMECLIENT.getCode()));
                        queryExpress.add(QueryCriterions.eq(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode()));
                        queryExpress.add(QueryCriterions.eq(ClientLineField.LINE_INTRO, code));
                        queryExpress.add(QuerySort.add(ClientLineField.DISPLAY_ORDER, QuerySortOrder.ASC));
                        List<ClientLine> gameCategorySubList = readonlyHandlersPool.getHandler().queryClientLine(queryExpress);
                        if (!CollectionUtil.isEmpty(gameCategorySubList)) {

                            for (int j = 0; j < gameCategorySubList.size(); j++) {

                                HashMap itemMap = new LinkedHashMap();
                                itemMap.put("name", gameCategorySubList.get(j).getLineName());
                                List<ClientLineItem> itemList = queryClientLineItemListForHotPage(gameCategorySubList.get(j).getCode());
                                int size = 0;
                                long gameDbId = 0L;
                                if (!CollectionUtil.isEmpty(itemList)) {
                                    size = itemList.size();
                                    gameDbId = Long.valueOf(itemList.get(0).getDirectId());
                                }
                                itemMap.put("gameDbId", gameDbId);
                                itemMap.put("size", size);
                                if (size > 0) {
                                    itemMap.put("jt", "-2");     //9507   bug id
                                    itemMap.put("ji", "http://api." + WebappConfig.get().getDomain() + "/joymeapp/gameclient/webview/game/gamecategorylist?lineCode=" + gameCategorySubList.get(j).getCode());
                                } else {
                                    itemMap.put("jt", "-100");     //默认无动作
                                    itemMap.put("ji", "");
                                }
                                subList.add(itemMap);
                            }
                        }
                        if (!CollectionUtil.isEmpty(subList)) {
                            baseItem.put("body", subList);
                        } else {
                            baseItem.put("body", emptyArray);
                        }
                        if (gameCategoryResultList.size() < 3) {
                            gameCategoryResultList.add(baseItem);
                        } else {
                            break; //跳出最外层循环     //只取前3个父分类
                        }
                    }
                }
            }

            if (!CollectionUtil.isEmpty(gameCategoryResultList)) {
                clientLineCache.putClientCustom(customCode + platform, gameCategoryResultList);
            }
        }


        return gameCategoryResultList;
    }

    //根据code  删除ClientLineCache中的自定义缓存 by tony 2015-04-16
    @Override
    public boolean removeClientLineCustomCache(String customCode, String platform) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler removeClientLineCustomCache:customCode" + customCode + platform);
        }
        return clientLineCache.removeClientCustom(customCode + platform);
    }


    //为了着迷玩霸2.0.3热门页接口的今日推荐 by tony 2015-04-08
    @Override
    public List<ClientLineItem> queryClientLineItemListForWanBaHotPageTodayRec(String lineCode) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryClientLineItemListForWanBaHotPageTodayRec:lineCode" + lineCode);
        }
        ClientLine line = this.getClientLineByCode(lineCode);
        Long lineId = line.getLineId();
        List<ClientLineItem> returnList = new ArrayList<ClientLineItem>();
        if (line == null) {
            return returnList;
        }

        returnList = clientLineCache.getClientItem(line.getLineId());

        if (CollectionUtil.isEmpty(returnList)) {
            QueryExpress queryExpressForTodayRecommend = new QueryExpress();
            queryExpressForTodayRecommend.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, lineId));
            queryExpressForTodayRecommend.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date tomorrow = calendar.getTime();

            //只取今天的，不取未来的
            queryExpressForTodayRecommend.add(QueryCriterions.lt(ClientLineItemField.ITEM_CREATE_DATE, tomorrow));
            queryExpressForTodayRecommend.add(QueryCriterions.geq(ClientLineItemField.ITEM_CREATE_DATE, DateUtil.ignoreTime(new Date())));
            queryExpressForTodayRecommend.add(QuerySort.add(ClientLineItemField.ITEM_CREATE_DATE, QuerySortOrder.DESC));

            returnList = readonlyHandlersPool.getHandler().queryClientLineItem(queryExpressForTodayRecommend);

            //如果后台没有配置今天的数据，就查询最近的数据，最多12条
            if (CollectionUtil.isEmpty(returnList)) {
                queryExpressForTodayRecommend = new QueryExpress();
                queryExpressForTodayRecommend.add(QueryCriterions.eq(ClientLineItemField.LINE_ID, lineId));
                queryExpressForTodayRecommend.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()));
                queryExpressForTodayRecommend.add(QueryCriterions.lt(ClientLineItemField.ITEM_CREATE_DATE, DateUtil.ignoreTime(new Date())));
                queryExpressForTodayRecommend.add(QuerySort.add(ClientLineItemField.ITEM_CREATE_DATE, QuerySortOrder.DESC));
                Pagination pagination = new Pagination(12, 1, 12);
                PageRows<ClientLineItem> itemPageRows = readonlyHandlersPool.getHandler().queryClientLineItemByPage(queryExpressForTodayRecommend, pagination);
                if (itemPageRows != null && !CollectionUtil.isEmpty(itemPageRows.getRows())) {
                    returnList = itemPageRows.getRows();
                    //如果今天没有配数据，只显示最近一天的数据
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dateStr = sdf.format(returnList.get(0).getItemCreateDate());

                    String dateTemp;
                    for (int i = 1; i < returnList.size(); ) {
                        dateTemp = sdf.format(returnList.get(i).getItemCreateDate());
                        if (!dateTemp.equals(dateStr)) {
                            returnList.remove(i);
                        } else {
                            i += 1;
                        }
                    }
                }
            }

            if (returnList != null) {
                clientLineCache.putClientItem(lineId, returnList);
            }
        }

        return returnList;
    }


    @Override
    public List<ClientLineItem> queryClientLineItemByQueryExpress(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryClientLineItemByQueryExpress:QueryExpress" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryClientLineItem(queryExpress);

    }

    @Override
    public PageRows<ClientLineItem> queryClientLineItemByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler createClientLine:QueryExpress" + queryExpress + "Pagination " + pagination);
        }
        return readonlyHandlersPool.getHandler().queryClientLineItemByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyClientLineItem(UpdateExpress updateExpress, long itemId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler modifyClientLineItem:UpdateExpress:" + updateExpress + " itemId:" + itemId);
        }

        QueryExpress express = new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.ITEM_ID, itemId));
        boolean bval = writeAbleHandler.modifyClientLineItem(updateExpress, express);
        if (bval) {
            ClientLineItem item = writeAbleHandler.getClientLineItem(express);
            if (item != null) {
                removeItemCacheByLineId(item.getLineId());
            }
        }
        return bval;
    }

    @Override
    public ClientLineItem createClientLineItem(ClientLineItem clientLineitem) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler createClientLine:ClientLineItem" + clientLineitem);
        }

        removeItemCacheByLineId(clientLineitem.getLineId());

        return writeAbleHandler.createClientLineItem(clientLineitem);
    }

    public PageRows<ClientLineItem> queryItemsByLineCode(String lineCode, String flag, Pagination pagination) throws ServiceException {
        if (StringUtil.isEmpty(flag)) {
            return queryItemsByCache(lineCode, pagination);
        } else {
            return queryItemsByDb(lineCode, pagination);
        }
    }

    private List<ClientLineItem> queryItemsByLineIdFromCache(long lineid) throws ServiceException {
        List<ClientLineItem> returnList = clientLineCache.getClientItem(lineid);
        if (CollectionUtil.isEmpty(returnList)) {
            returnList = readonlyHandlersPool.getHandler().queryClientLineItem(new QueryExpress()
                    .add(QueryCriterions.eq(ClientLineItemField.LINE_ID, lineid))
                    .add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()))
                    .add(QuerySort.add(ClientLineItemField.STATE_DATE, QuerySortOrder.DESC)));
            if (!CollectionUtil.isEmpty(returnList)) {
                clientLineCache.putClientItem(lineid, returnList);
            }
        }
        return returnList;
    }

    private List<ClientLineItem> queryItemsByLineIdFromCacheDisplayOrderASC(long lineid) throws ServiceException {
        List<ClientLineItem> returnList = new ArrayList<ClientLineItem>();

        //todo
        returnList = clientLineCache.getClientItem(lineid);
        if (CollectionUtil.isEmpty(returnList)) {
            returnList = readonlyHandlersPool.getHandler().queryClientLineItem(new QueryExpress()
                    .add(QueryCriterions.eq(ClientLineItemField.LINE_ID, lineid))
                    .add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()))
                    .add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC)));
            if (!CollectionUtil.isEmpty(returnList)) {
                clientLineCache.putClientItem(lineid, returnList);
            }
        }
        return returnList;
    }


    private PageRows<ClientLineItem> queryItemsByCache(String lineCode, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryItemsByCache:lineCode:" + lineCode + " pagination:" + pagination);
        }
        PageRows<ClientLineItem> pageRows = new PageRows<ClientLineItem>();
        ClientLine line = getClientLineByCode(lineCode);
        if (line == null) {
            return pageRows;
        }

        List<ClientLineItem> items = clientLineCache.getClientItem(line.getLineId());

        if (CollectionUtil.isEmpty(items)) {

            if (line != null) {

                QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.LINE_ID, line.getLineId()));
//                        .add(QueryCriterions.leq(ClientLineItemField.ITEM_ID, clientLineFlag.getMaxItemId()))

                ClientLineFlag clientLineFlag = readonlyHandlersPool.getHandler().getClientLineFlag(new QueryExpress()
                        .add(QueryCriterions.eq(ClientLineFlagField.LINE_ID, line.getLineId()))
                        .add(QueryCriterions.eq(ClientLineFlagField.FLAG_TYPE, ClientLineFlagType.CLIENT_LINE.getCode()))
                        .add(QueryCriterions.eq(ClientLineFlagField.VALID_STATUS, ValidStatus.VALID.getCode())));
                if (clientLineFlag != null) {
                    queryExpress.add(QueryCriterions.leq(ClientLineItemField.ITEM_ID, clientLineFlag.getMaxItemId()));
                }

                queryExpress.add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()))
                        .add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC));

                //
                items = readonlyHandlersPool.getHandler().queryClientLineItem(queryExpress);
                clientLineCache.putClientItem(line.getLineId(), items);
            }
        }

        if (CollectionUtil.isEmpty(items)) {
            return pageRows;
        }

        pagination.setTotalRows(items.size());
        List<ClientLineItem> returnObj = new ArrayList<ClientLineItem>();
        for (int i = pagination.getStartRowIdx(); i < items.size() && i <= pagination.getEndRowIdx(); i++) {
            ClientLineItem item = items.get(i);
            returnObj.add(item);
        }

        pageRows.setPage(pagination);
        pageRows.setRows(returnObj);

        return pageRows;
    }

    private PageRows<ClientLineItem> queryItemsByDb(String lineCode, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryItemsByDb:lineCode:" + lineCode + " pagination:" + pagination);
        }

        ClientLine line = readonlyHandlersPool.getHandler().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.CODE, lineCode))
                .add(QueryCriterions.eq(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode())));

        if (line == null) {
            return null;
        }

        PageRows<ClientLineItem> pageRows = readonlyHandlersPool.getHandler().queryClientLineItemByPage(new QueryExpress().add(QueryCriterions.eq(ClientLineItemField.LINE_ID, line.getLineId()))
                .add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()))
                .add(QuerySort.add(ClientLineItemField.DISPLAY_ORDER, QuerySortOrder.ASC)), pagination);
        return pageRows;
    }


    @Override
    public List<ActivityTopMenu> queryClientTopMenuCache(Integer platform, Integer categroy, String flag, String appKey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryActivityTopMenuCache:platform:" + platform + " category:" + categroy);
        }
        if (StringUtil.isEmpty(flag)) {
            return queryClientTopMenuCache(platform, categroy, appKey);
        } else {
            return queryClientTopMenuDB(platform, categroy);
        }
    }

    private List<ActivityTopMenu> queryClientTopMenuCache(Integer platform, Integer categroy, String appKey) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryClientTopMenuCache:platform:" + platform);
        }
        List<ActivityTopMenu> clientTopMenuList = joymeAppCache.getClientTopMenu(platform);
        if (CollectionUtil.isEmpty(clientTopMenuList)) {
            String flagPlatform = String.valueOf(platform);
            ClientLineFlag clientLineFlag = readonlyHandlersPool.getHandler().getClientLineFlag(new QueryExpress()
                    .add(QueryCriterions.eq(ClientLineFlagField.LINE_CODE, appKey))
                    .add(QueryCriterions.eq(ClientLineFlagField.VALID_STATUS, ValidStatus.VALID.getCode()))
                    .add(QueryCriterions.eq(ClientLineFlagField.LINE_ID, Long.parseLong(flagPlatform)))
                    .add(QueryCriterions.eq(ClientLineFlagField.FLAG_TYPE, ClientLineFlagType.TOP_MENU.getCode())));
            if (clientLineFlag == null) {
                return null;
            }
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.leq(ActivityTopMenuField.ACTIVITY_MENU_ID, clientLineFlag.getMaxItemId()));
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.PLATFORM, platform));
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.CATEGORY, categroy));
            queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.VALID_STATUS, ValidStatus.VALID.getCode()));
            queryExpress.add(QuerySort.add(ActivityTopMenuField.DISPLAY_ORDER, QuerySortOrder.ASC));
            clientTopMenuList = readonlyHandlersPool.getHandler().queryActivityTopMenu(queryExpress);
            joymeAppCache.putClientTopMenu(platform, clientTopMenuList);
        }
        return clientTopMenuList;
    }

    private List<ActivityTopMenu> queryClientTopMenuDB(Integer platform, Integer categroy) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryClientTopMenuDB:platform:" + platform);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.PLATFORM, platform));
        queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.CATEGORY, categroy));
        queryExpress.add(QueryCriterions.eq(ActivityTopMenuField.VALID_STATUS, ValidStatus.VALID.getCode()));
        queryExpress.add(QuerySort.add(ActivityTopMenuField.DISPLAY_ORDER, QuerySortOrder.ASC));
        List<ActivityTopMenu> clientTopMenuList = readonlyHandlersPool.getHandler().queryActivityTopMenu(queryExpress);
        return clientTopMenuList;
    }

    @Override
    public ClientLineFlag createClientLineFlag(ClientLineFlag flag) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler createClientLineFlag.flag:" + flag);
        }
        return writeAbleHandler.createClientLineFlag(flag);
    }

    @Override
    public ClientLineFlag getClientLineFlag(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler getClientLineFlag.queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getClientLineFlag(queryExpress);
    }

    @Override
    public PageRows<ClientLineFlag> queryClientLineFlagByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler queryClientLineFlagByPage.queryExpress:" + queryExpress + ",pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryClientLineFlagByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyClientLineFlag(UpdateExpress updateExpress, Long flagId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call handler modifyClientLineFlag.flagId:" + flagId + ",updateExpress:" + updateExpress);
        }
        ClientLineFlag clientLineFlag = readonlyHandlersPool.getHandler().getClientLineFlag(new QueryExpress().add(QueryCriterions.eq(ClientLineFlagField.FLAG_ID, flagId)));
        if (clientLineFlag != null) {
            removeItemCacheByLineId(clientLineFlag.getLineId());
            String platform = String.valueOf(clientLineFlag.getLineId());
            joymeAppCache.deleteClientTopMenu(Integer.parseInt(platform));
        }
        return writeAbleHandler.modifyClientLineFlag(updateExpress, new QueryExpress().add(QueryCriterions.eq(ClientLineFlagField.FLAG_ID, flagId)));
    }

    private boolean removeItemCacheByLineId(long lineId) throws DbException {
        ClientLine line = readonlyHandlersPool.getHandler().getClientLine(new QueryExpress().add(QueryCriterions.eq(ClientLineField.LINE_ID, lineId))
                .add(QueryCriterions.eq(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode())));

        if (line != null) {
            return clientLineCache.removeClientItem(lineId);
        }
        return false;
    }

    @Override
    public boolean modifyDedeArchivePubdateById(DedeArchives dedeArchives) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getAccountVirtual:DedeArchives " + dedeArchives);
        }
        return archiveWriteHandler.modifyDedeArchivePubdateById(dedeArchives);
    }


    @Override
    public List<SocialShare> querySocialShare(QueryExpress queryExpress, String appkey, int platform, SocialShareType socialShareType, long activityid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialShare.queryExpress: " + queryExpress);
        }

        List<SocialShare> returnList = joymeAppCache.getSocialShare(appkey, platform, socialShareType.getCode(), activityid);

        if (CollectionUtil.isEmpty(returnList)) {
            returnList = readonlyHandlersPool.getHandler().querySocialShare(queryExpress);
            List<String> nodefault = new ArrayList<String>();
            for (SocialShare socialShare : returnList) {
                //找出不是默认有哪些平台
                if (socialShare.getActivityid() != -1L) {
                    nodefault.add(socialShare.getSharedomain().getCode());
                }
            }

            List<SocialShare> shareList = new ArrayList<SocialShare>();
            for (SocialShare socialShare : returnList) {
                if (nodefault.contains(socialShare.getSharedomain().getCode()) && socialShare.getActivityid() == -1L) {
                    continue;
                }
                shareList.add(socialShare);
            }

            if (returnList != null && returnList.size() > 0) {
                joymeAppCache.putSocialShare(appkey, platform, socialShareType.getCode(), activityid, shareList);
            }
            returnList = shareList;
        }
        return returnList;
    }

    @Override
    public SocialShare createSocialShare(SocialShare socialShare, String appkey, int platform, SocialShareType socialShareType, long activityid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " createSocialShare.socialShare: " + socialShare);
        }

        joymeAppCache.deleteSocialShare(appkey, platform, socialShareType.getCode(), activityid);

        return writeAbleHandler.insertSocialShare(socialShare);
    }

    @Override
    public boolean modifySocialShare(QueryExpress queryExpress, UpdateExpress updateExpress, String appkey, int platform, SocialShareType socialShareType, long activityid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifySocialShare.queryExpress: " + queryExpress);
        }

        boolean ok = writeAbleHandler.updateSocialShare(queryExpress, updateExpress);
        if (ok) {
            //如果更新默认活动
            if (socialShareType.equals(SocialShareType.SOCIAL_ACTIVITY_TYPE) && activityid == -1L) {
                List<SocialShare> shareList = readonlyHandlersPool.getHandler().querySocialShare(new QueryExpress().add(QueryCriterions.eq(SocialShareField.SHARE_TYPE, SocialShareType.SOCIAL_ACTIVITY_TYPE.getCode())));
                for (SocialShare share : shareList) {
                    joymeAppCache.deleteSocialShare(appkey, platform, socialShareType.getCode(), share.getActivityid());
                }
            } else {
                joymeAppCache.deleteSocialShare(appkey, platform, socialShareType.getCode(), activityid);
            }
        }
        return ok;
    }

    @Override
    public PageRows<SocialShare> querySocialShareByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " querySocialShareByPage.queryExpress: " + queryExpress + ",pagination=" + pagination);
        }
        return readonlyHandlersPool.getHandler().querySocialShare(queryExpress, pagination);
    }

    @Override
    public SocialShare getSocialShare(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getSocialShare.queryExpress: " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getSocialShare(queryExpress);
    }


    @Override
    public MobileGameArticle createMobileGameArticle(MobileGameArticle mobileGameArticle) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " createMobileGameArticle MobileGameArticle" + mobileGameArticle);
        }
        return writeAbleHandler.insertMobileGameArticle(mobileGameArticle);
    }

    @Override
    public MobileGameArticle getMobileGameArticle(QueryExpress queryExpresse) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getMobileGameArticle QueryExpress" + queryExpresse);
        }
        return readonlyHandlersPool.getHandler().getMobileGameArticle(queryExpresse);
    }

    @Override
    public List<MobileGameArticle> queryMobileGameArticleByList(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryMobileGameArticleByList QueryExpress" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryMobileGameArticles(queryExpress);
    }

    @Override
    public PageRows<MobileGameArticle> queryMobileGameArticleByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryMobileGameArticleByPage QueryExpress" + queryExpress + " Pagination" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryMobileGameArticleByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyMobileGameArticle(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifyMobileGameArticle QueryExpress" + queryExpress + " UpdateExpress" + updateExpress);
        }
        return writeAbleHandler.updateMobileGameArticle(queryExpress, updateExpress);
    }

    @Override
    public AnimeIndex getAnimeIndex(BasicDBObject express) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getAnimeIndex.BasicDBObject: " + express);
        }
        return statsReadOnlyHandlerPool.getHandler().getAnimeIndex(express);
    }

    @Override
    public List<AnimeIndex> queryAnimeIndex(MongoQueryExpress mongoQueryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryAnimeIndex.MongoQueryExpress: " + mongoQueryExpress);
        }
        return statsReadOnlyHandlerPool.getHandler().queryAnimeIndex(mongoQueryExpress);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean modifyAnimeIndex(BasicDBObject queryObject, BasicDBObject updateObjec) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifyAnimeIndex.BasicDBObject: " + queryObject + "  updateBasicDBObject" + updateObjec);
        }
        return statsWriteAbleHandler.modify(queryObject, updateObjec);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public AnimeIndex createAnimeIndex(AnimeIndex animeIndex) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " createAnimeIndex.AnimeIndex: " + animeIndex);
        }
        return statsWriteAbleHandler.insertAnimeIndex(animeIndex);
    }

    @Override
    public PageRows<AnimeIndex> queryAnimeIndexByPage(MongoQueryExpress mongoQueryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryAnimeIndexByPage.MongoQueryExpress: " + mongoQueryExpress + " Paginatio:" + pagination);
        }
        return statsReadOnlyHandlerPool.getHandler().queryAniIndexByPage(mongoQueryExpress, pagination);
    }

    @Override
    public AnimeSpecial getAnimeSpecial(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getAnimeSpecial.queryExpress: " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getAnimeSpecial(queryExpress);
    }

    @Override
    public AnimeSpecial createAnimeSpecial(AnimeSpecial animeSpecial) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " createAnimeSpecial.createAnimeSpecial: " + animeSpecial);
        }
        return writeAbleHandler.createAnimeSpecial(animeSpecial);
    }

    @Override
    public PageRows<AnimeSpecial> queryAnimeSpecialByPage(QueryExpress queyrExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryAnimeSpecialByPage.queryExpress: " + queyrExpress + " pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryAnimeSpecial(queyrExpress, pagination);
    }

    @Override
    public boolean modifyAnimeSpecial(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifyAnimeSpecial.queryExpress: " + queryExpress + " updateExpress:" + updateExpress);
        }
        return writeAbleHandler.modifyAnimeSpecial(queryExpress, updateExpress);
    }

    @Override
    public AnimeTag createAnimeTag(AnimeTag animeTag) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " createAnimeTag.AnimeTag:" + animeTag);
        }
        return writeAbleHandler.createAnimeTag(animeTag);
    }

    @Override
    public List<AnimeSpecialItem> queryAnimeSpecialItem(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryAnimeSpecialItem.queryExpress: " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryAnimeSpecialItem(queryExpress);
    }

    @Override
    public AnimeTag getAnimeTag(Long tagid, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getAnimeTag.QueryExpress:" + queryExpress);
        }
        AnimeTag animeTag = joymeAppCache.getAnimeTagtByTagid(tagid);
        if (animeTag == null) {
            animeTag = readonlyHandlersPool.getHandler().getanimeTag(queryExpress);
            if (animeTag != null) {
                joymeAppCache.putAnimeTag(tagid, animeTag);
            }
        }
        return animeTag;
    }

    @Override
    public AnimeSpecialItem getAnimeSpecialItem(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getAnimeSpecialItem.queryExpress: " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getAnimeSpecialitem(queryExpress);
    }

    @Override
    public List<AnimeTag> queryAnimeTag(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryAnimeTag.QueryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryAnimeTag(queryExpress);
    }

    @Override
    public AnimeSpecialItem createAnimeSpecialItem(AnimeSpecialItem animeSpecial) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " createAnimeSpecialItem.AnimeSpecial: " + animeSpecial);
        }
        return writeAbleHandler.createAnimeSpecialItem(animeSpecial);
    }

    @Override
    public PageRows<AnimeTag> queryAnimeTagByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryAnimeTagByPage.QueryExpress:" + queryExpress + ",Pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryAnimeTagByPage(queryExpress, pagination);
    }

    @Override
    public PageRows<AnimeSpecialItem> queryAnimeSpecialItemByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryAnimeSpecialItemByPage.QueryExpress: " + queryExpress + " Pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryAnimeSpecialItem(queryExpress, pagination);
    }

    @Override
    public boolean modifyAnimeTag(Long tagid, QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifyAnimeTag.QueryExpress:" + queryExpress + ",UpdateExpress:" + updateExpress);
        }
        boolean bval = writeAbleHandler.modifyAnimeTag(queryExpress, updateExpress);
        if (bval) {
            joymeAppCache.deleteAnimeTagByTagid(tagid);
        }
        return bval;
    }

    @Override
    public boolean modifyAnimeSpecialItem(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifyAnimeSpecialItem.QueryExpress: " + queryExpress + " UpdateExpress:" + updateExpress);
        }
        return writeAbleHandler.modifyAnimeSpecialItem(queryExpress, updateExpress);
    }

    ///anime tv
    @Override
    public AnimeTV createAnimeTV(AnimeTV animeTV) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " createAnimeTV.AnimeTV:" + animeTV);
        }
        return writeAbleHandler.createAnimeTV(animeTV);
    }

    @Override
    public AnimeTV getAnimeTV(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getAnimeTV.QueryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getAnimeTV(queryExpress);
    }

    @Override
    public List<AnimeTV> queryAnimeTV(QueryExpress queryExpress, Long tagid, String flag) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryAnimeTV.QueryExpress:" + queryExpress);
        }
        List<AnimeTV> animeTVList = null;
        if (StringUtil.isEmpty(flag) || !flag.equals("true")) {
            animeTVList = joymeAppCache.getAnimeTVListByTagid(tagid);
            if (CollectionUtil.isEmpty(animeTVList)) {
                animeTVList = readonlyHandlersPool.getHandler().queryAnimeTV(queryExpress);
                if (!CollectionUtil.isEmpty(animeTVList)) {
                    joymeAppCache.putAnimeTVList(tagid, animeTVList);
                }
            }
        } else {
            animeTVList = readonlyHandlersPool.getHandler().queryAnimeTV(queryExpress);
        }

        return animeTVList;
    }

    @Override
    public PageRows<AnimeTV> queryAnimeTVByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryAnimeTVByPage.QueryExpress:" + queryExpress + ",Pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryAnimeTVByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyAnimeTV(QueryExpress queryExpress, UpdateExpress updateExpress, List<Long> tags) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " modifyAnimeTV.QueryExpress:" + queryExpress + ",UpdateExpress:" + updateExpress);
        }

        if (!CollectionUtil.isEmpty(tags)) {
            for (Long tag : tags) {
                joymeAppCache.deleteAnimeTVListByTagid(tag);
            }
        }


        return writeAbleHandler.modifyAnimeTV(queryExpress, updateExpress);
    }


    @Override
    public TagDedearchives createTagDedearchives(TagDedearchives tagDedearchives) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler createTagDedearchives:,TagDedearchives " + tagDedearchives);
        }
        TagDedearchives tag = writeAbleHandler.createTagDedearchives(tagDedearchives);
        if (tag.getArchiveRelationType().equals(ArchiveRelationType.TAG_RELATION) && tag != null) {
            QueryExpress qu = new QueryExpress();
            qu.add(QueryCriterions.eq(AnimeTagField.TAG_ID, tag.getTagid()));
            AnimeTag animeTag = getAnimeTag(tag.getTagid(), qu);
            if (animeTag == null) {
                return tag;
            }

            int pageSize = 0;
            if (animeTag.getTotal_sum() == null) {
                pageSize = 1;
            } else {
                pageSize = animeTag.getTotal_sum() / 10 + 1;
            }
            for (int i = 0; i < pageSize; i++) {
                joymeAppCache.deleteTagDedearchivesListByPage(tag.getTagid(), 0, i);
                joymeAppCache.deleteTagDedearchivesListByPage(tag.getTagid(), 1, i);
            }
        }
        return tag;
    }

    @Override
    public TagDedearchives getTagDedearchives(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandler getTagDedearchives:,QueryExpress " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getTagDedearchives(queryExpress);
    }

    @Override
    public List<TagDedearchives> queryTagDedearchives(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandler queryTagDedearchives:,QueryExpress " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryTagDedearchives(queryExpress);
    }

    @Override
    public PageRows<TagDedearchives> queryTagDedearchivesByPage(Boolean isTools, Long tagid, Integer platform, QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandler queryTagDedearchivesByPage:,QueryExpress " + queryExpress + ",Pagination " + pagination);
        }
        PageRows<TagDedearchives> pageRows = new PageRows<TagDedearchives>();
        if (!isTools) {

            QueryExpress qu = new QueryExpress();
            qu.add(QueryCriterions.eq(AnimeTagField.TAG_ID, tagid));
            AnimeTag animeTag = getAnimeTag(tagid, qu);

            List<TagDedearchives> list = joymeAppCache.getTagDedearchivesListByPage(tagid, platform, pagination.getCurPage());
            if (list == null) {
                pageRows = readonlyHandlersPool.getHandler().queryTagDedearchivesByPage(queryExpress, pagination);
                if (!CollectionUtil.isEmpty(pageRows.getRows())) {
                    joymeAppCache.putTagDedearchivesList(tagid, platform, pagination.getCurPage(), pageRows.getRows());
                }
            } else {
                pagination.setTotalRows(animeTag.getTotal_sum());
                pageRows.setPage(pagination);
                pageRows.setRows(list);
            }
        } else {
            pageRows = readonlyHandlersPool.getHandler().queryTagDedearchivesByPage(queryExpress, pagination);
        }
        return pageRows;
    }

    @Override
    public boolean modifyTagDedearchives(Long tagid, String dede_archives_id, QueryExpress queryExpress, UpdateExpress updateExpress, ArchiveRelationType archiveRelationType) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler getTagDedearchives:,QueryExpress " + queryExpress + ",UpdateExpress " + updateExpress);
        }
        if (archiveRelationType.equals(ArchiveRelationType.TAG_RELATION)) {
            QueryExpress qu = new QueryExpress();
            qu.add(QueryCriterions.eq(AnimeTagField.TAG_ID, tagid));
            AnimeTag animeTag = getAnimeTag(tagid, qu);

            int pageSize = 0;
            if (animeTag.getTotal_sum() == null) {
                pageSize = 1;
            } else {
                pageSize = animeTag.getTotal_sum() / 10 + 1;
            }
            for (int i = 0; i < pageSize; i++) {
                joymeAppCache.deleteTagDedearchivesListByPage(tagid, 0, i);
                joymeAppCache.deleteTagDedearchivesListByPage(tagid, 1, i);
            }
        }
        boolean bval = writeAbleHandler.modifyTagDedearchives(queryExpress, updateExpress);
        if (bval) {
            joymeAppCache.removeTagDedearchivesCache(tagid, archiveRelationType, dede_archives_id);
            archiveCache.removeArchive(dede_archives_id);
        }
        return bval;
    }

    @Override
    public PageRows<TagDedearchiveCheat> queryTagDedearchiveCheat(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryTagDedearchiveCheat.QueryExpress:" + queryExpress + ",Pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryTagDedearchiveCheatByPage(queryExpress, pagination);
    }

    @Override
    public TagDedearchiveCheat createTagDedearchiveCheat(TagDedearchiveCheat cheat) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " createTagDedearchiveCheat.TagDedearchiveCheat:" + cheat);
        }
        cheat = writeAbleHandler.createTagDedearchiveCheat(cheat);
        if (cheat != null) {
            joymeAppCache.putTagDedearchiveCheat(cheat.getDede_archives_id(), cheat);
        }
        return cheat;
    }

    @Override
    public TagDedearchiveCheat getTagDedearchiveCheat(Long dede_archives_id) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandler getTagDedearchiveCheat:,dede_archives_id " + dede_archives_id);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(TagDedearchiveCheatFiled.DEDE_ARCHIVES_ID, dede_archives_id));
        TagDedearchiveCheat cheat = joymeAppCache.getTagDedearchiveCheat(dede_archives_id);
        if (cheat == null) {
            cheat = readonlyHandlersPool.getHandler().getTagDedearchiveCheat(queryExpress);
            if (cheat != null) {
                joymeAppCache.putTagDedearchiveCheat(cheat.getDede_archives_id(), cheat);
            } else {
                //如果文章不存在，就创建
                try {
                    Archive archive = getArchiveById(dede_archives_id.intValue());
                    if (archive != null) {
                        TagDedearchiveCheat cheat1 = new TagDedearchiveCheat();
                        cheat1.setDede_archives_id(Long.valueOf(dede_archives_id));
                        cheat1.setRead_num(0);
                        cheat1.setAgree_num(0);
                        cheat1.setAgree_percent(0.0);
                        cheat1.setCheating_time(new Date());
                        cheat = createTagDedearchiveCheat(cheat1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return cheat;
    }

    @Override
    public Map<Long, TagDedearchiveCheat> getMapTagDedearchiveCheat(Set<Long> dedeIds) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call readonlyHandler getTagDedearchiveCheat:,dedeIds " + dedeIds);
        }
        Map<Long, TagDedearchiveCheat> returnMap = new HashMap<Long, TagDedearchiveCheat>();
        for (Long id : dedeIds) {
            TagDedearchiveCheat cheat = getTagDedearchiveCheat(id);
            returnMap.put(id, cheat);
        }
        return returnMap;
    }

    @Override
    public boolean modifyTagDedearchiveCheat(Long dede_archives_id, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call writeHandler modifyTagDedearchiveCheat:,dede_archives_id " + dede_archives_id + ",UpdateExpress " + updateExpress);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(TagDedearchiveCheatFiled.DEDE_ARCHIVES_ID, dede_archives_id));
        boolean bval = writeAbleHandler.modifyTagDedearchiveCheat(queryExpress, updateExpress);
        if (bval) {
            joymeAppCache.deleteTagDedearchiveCheat(dede_archives_id);
        }
        return bval;
    }


    @Override
    public JoymeWiki getJoymeWiki(String wikiKey, JoymeWikiContextPath contextPath) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic getJoymeWiki.wikiKey:" + wikiKey + ",contextPath:" + contextPath);
        }
        JoymeWiki joymeWiki = joymeAppRedis.getJoymeWiki(contextPath, wikiKey);
        if (joymeWiki == null) {
            joymeWiki = wikiUrlWriteAbleHandler.getJoymeWiki(new QueryExpress().add(QueryCriterions.eq(JoymeWikiField.WIKI_KEY, wikiKey))
                    .add(QueryCriterions.eq(JoymeWikiField.WIKI_CONTEXT_PATH, contextPath.getCode())));
            if (joymeWiki != null) {
                joymeAppRedis.putJoymeWiki(contextPath, wikiKey, joymeWiki);
            }
        }
        return joymeWiki;
    }

    @Override
    public PageRows<JoymeWiki> queryJoymeWikiByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic queryJoymeWikiByPage.queryExpress:" + queryExpress.toString() + ",pagination:" + pagination.toString());
        }
        return wikiUrlWriteAbleHandler.queryJoymeWikiByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyJoymeWiki(String wikiKey, JoymeWikiContextPath contextPath, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic modifyJoymeWiki.wikiKey:" + wikiKey + ",contextPath:" + contextPath.getCode());
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(JoymeWikiField.WIKI_KEY, wikiKey));
        queryExpress.add(QueryCriterions.eq(JoymeWikiField.WIKI_CONTEXT_PATH, contextPath.getCode()));
        boolean bool = wikiUrlWriteAbleHandler.modifyJoymeWiki(updateExpress, queryExpress);
        if (bool) {
            JoymeWiki joymeWiki = wikiUrlWriteAbleHandler.getJoymeWiki(queryExpress);
            if (joymeWiki != null) {
                joymeAppRedis.putJoymeWiki(contextPath, wikiKey, joymeWiki);
            }
        }
        return bool;
    }

    @Override
    public Map<String, JoymeWiki> queryJoymeWikiByWikiKeySet(Set<String> wikiKeySet, JoymeWikiContextPath contextPath) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic queryJoymeWikiByWikiKeySet.wikiKeySet:" + wikiKeySet.toArray().toString());
        }
        if (CollectionUtil.isEmpty(wikiKeySet)) {
            return null;
        }
        Set<String> dbSet = new HashSet<String>();
        Map<String, JoymeWiki> map = new HashMap<String, JoymeWiki>();
        for (String wikiKey : wikiKeySet) {
            if (StringUtil.isEmpty(wikiKey)) {
                continue;
            }
            //todo game
            JoymeWiki joymeWiki = joymeAppRedis.getJoymeWiki(contextPath, wikiKey);
            if (joymeWiki != null) {
                map.put(wikiKey, joymeWiki);
            } else {
                dbSet.add(wikiKey);
            }
        }
        if (!CollectionUtil.isEmpty(dbSet)) {
            List<JoymeWiki> wikiList = wikiUrlWriteAbleHandler.queryJoymeWiki(new QueryExpress()
                    .add(QueryCriterions.in(JoymeWikiField.WIKI_KEY, dbSet.toArray()))
                    .add(QueryCriterions.eq(JoymeWikiField.WIKI_CONTEXT_PATH, contextPath.getCode())));
            if (!CollectionUtil.isEmpty(wikiList)) {
                for (JoymeWiki joymeWiki : wikiList) {
                    map.put(joymeWiki.getWikiKey(), joymeWiki);
                }
            }
        }
        return map;
    }

    @Override
    public void putGameIdByWeight(String code, String gameId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic putGameIdByWeight.code:" + code + ",gameid:" + gameId);
        }
        joymeAppRedis.putGameIdByWeight(code, gameId);
    }

    @Override
    public Set<String> getGameIdByWeight(String code) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic getGameIdByWeight.code:" + code);
        }
        Set<String> idSet = joymeAppRedis.getGameIdByWeight(code);
        if (CollectionUtil.isEmpty(idSet)) {
            ClientLine clientLine = getClientLineByCode(code);
            PageRows<ClientLineItem> itemList = readonlyHandlersPool.getHandler().queryClientLineItemByPage(new QueryExpress()
                    .add(QueryCriterions.eq(ClientLineItemField.LINE_ID, clientLine.getLineId()))
                    .add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()))
                    .add(QuerySort.add(ClientLineItemField.ITEM_ID, QuerySortOrder.DESC)), new Pagination(5, 1, 5));
            if (itemList != null && !CollectionUtil.isEmpty(itemList.getRows())) {
                idSet = new HashSet<String>();
                for (ClientLineItem item : itemList.getRows()) {
                    idSet.add(item.getDirectId());
                }
            }

        } else if (idSet.size() < 5) {
            ClientLine clientLine = getClientLineByCode(code);
            PageRows<ClientLineItem> itemList = readonlyHandlersPool.getHandler().queryClientLineItemByPage(new QueryExpress()
                    .add(QueryCriterions.eq(ClientLineItemField.LINE_ID, clientLine.getLineId()))
                    .add(QueryCriterions.eq(ClientLineItemField.VALID_STATUS, ValidStatus.VALID.getCode()))
                    .add(QuerySort.add(ClientLineItemField.ITEM_ID, QuerySortOrder.DESC)), new Pagination(5, 1, 5));
            if (itemList != null && !CollectionUtil.isEmpty(itemList.getRows())) {
                for (ClientLineItem item : itemList.getRows()) {
                    if (idSet.contains(item.getDirectId())) {
                        continue;
                    }
                    idSet.add(item.getDirectId());
                    if (idSet.size() >= 5) {
                        break;
                    }
                }
            }
        }
        return idSet;
    }

    @Override
    public boolean removeGameIdByWeight(String code) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic removeGameIdByWeight.code:" + code);
        }
        return joymeAppRedis.delGameIdByWeight(code);
    }

    @Override
    public List<Integer> queryPostNum(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic queryPostNum.code:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryPostNum(queryExpress);
    }

    private static final String VALID_TYPE = "1";

    @Override
    public String validateInfo(String key, String type) throws ServiceException {
        int value = 0;
        if (StringUtil.isEmpty(key) || StringUtil.isEmpty(type)) {
            return "";
        }
        if (VALID_TYPE.equals(type)) {
            value = joymeAppCache.getValidateInfo(key);
            if (value == 0) {
                joymeAppCache.putValidateInfo(key, 1);
            } else {
                joymeAppCache.putValidateInfo(key, value + 1);
            }

        } else {
            value = joymeAppCache.getValidateInfo(key);
        }
        if (value < 3) {
            return null;
        }
        return "true";
    }

    @Override
    public List<ImgDTO> picInfoListCache(String commentId, List<String> pics) throws ServiceException {
        if (CollectionUtil.isEmpty(pics)) {
            return null;
        }
        List<ImgDTO> returnPics = null;
        try {
            returnPics = joymeAppCache.getPicInfoList(commentId);
            if (CollectionUtil.isEmpty(returnPics)) {
                returnPics = new ArrayList<ImgDTO>();
                for (String pic : pics) {
                    Image image = Image.getInstance(pic);
                    if (image != null) {
                        int width = (int) image.getWidth();
                        int height = (int) image.getHeight();
                        ImgDTO imgDTO = new ImgDTO();
                        imgDTO.setHeight(height);
                        imgDTO.setWidth(width);
                        imgDTO.setPic(pic);
                        returnPics.add(imgDTO);
                    }
                }
                joymeAppCache.putPicInfoList(commentId, returnPics);
            }

        } catch (BadElementException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return returnPics;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PageRows<Long> queryTagDedeArchivesByDistinct(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic queryTagDedeArchivesByDistinct");
        }
        return readonlyHandlersPool.getHandler().queryTagDedeArchivesByDistinct(queryExpress, page);
    }

    @Override
    public void addTagDedearchivesTimerTask(Long tagid, Set<String> idSet, Date publishDate) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic addTagDedearchivesTimerTask:" + idSet);
        }
        joymeAppRedis.putTagDedearchivesTimerTaskCache(tagid, idSet, publishDate);
        if (publishDate != null && tagid != null && !CollectionUtil.isEmpty(idSet)) {
            for (String id : idSet) {
                scheduleTagDedearchivesTimerTask(tagid, id, publishDate);
            }
        }
    }

    @Override
    public PageRows<TagDedearchives> queryTagDedearchivesTimerList(Long tagid, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic queryTagDedearchivesTimerList:" + tagid);
        }
        PageRows<TagDedearchives> pageRows = null;
        List<TagDedearchives> list = null;
        PageRows<String> idRows = joymeAppRedis.getTagDedearchivesTimerTaskCache(tagid, pagination);
        if (idRows != null && !CollectionUtil.isEmpty(idRows.getRows())) {
            list = new ArrayList<TagDedearchives>();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
            for (String id : idRows.getRows()) {
                TagDedearchives tagDedearchives = getTagDedearchives(new QueryExpress().add(QueryCriterions.eq(TagDedearchivesFiled.ID, id)));
                if (tagDedearchives != null) {
                    String timer = joymeAppRedis.getTagDedearchivesTimer(id);
                    if (!StringUtil.isEmpty(timer) && Long.valueOf(timer) > 0l) {
                        Date date = new Date(Long.valueOf(timer));
                        tagDedearchives.setTimerDate(df.format(date));
                    }
                    list.add(tagDedearchives);
                }
            }
            pageRows = new PageRows<TagDedearchives>();
            pageRows.setRows(list);
            pageRows.setPage(idRows.getPage());
        }

        return pageRows;
    }

    @Override
    public boolean delTagDedearchivesTimerTaskCache(Long tagid, Set<String> idSet, Date publishDate) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic delTagDedearchivesTimerTaskCache:" + tagid);
        }
        joymeAppRedis.removeTagDedearchivesTimerTaskCache(tagid, idSet, publishDate);
        return true;
    }

    @Override
    public TagDedearchives getTagDedearchivesByCache(long gameId, ArchiveRelationType gameRelation, String archiveId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic getTagDedearchivesByCache:" + gameId + "," + gameRelation.getCode() + "," + archiveId);
        }
        TagDedearchives tagDedearchives = joymeAppCache.getTagDedearchivesCache(gameId, gameRelation, archiveId);
        if (tagDedearchives == null) {
            tagDedearchives = getTagDedearchives(new QueryExpress()
                    .add(QueryCriterions.eq(TagDedearchivesFiled.TAGID, gameId))
                    .add(QueryCriterions.eq(TagDedearchivesFiled.ARCHIVE_RELATION_TYPE, gameRelation.getCode()))
                    .add(QueryCriterions.eq(TagDedearchivesFiled.DEDE_ARCHIVES_ID, archiveId)));
            if (tagDedearchives != null) {
                joymeAppCache.putTagDedearchivesCache(gameId, gameRelation, archiveId, tagDedearchives);
            }
        }
        return tagDedearchives;
    }

    @Override
    public List<TagDedearchives> queryTagDedearchivesByCache(long gameId, ArchiveRelationType gameRelation, List<String> archiveIds) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic getTagDedearchivesByCache:" + gameRelation.getCode() + "," + archiveIds.toString());
        }
        List<TagDedearchives> list = new ArrayList<TagDedearchives>();
        if (!CollectionUtil.isEmpty(archiveIds)) {
            for (String archiveId : archiveIds) {
                TagDedearchives tagDedearchives = joymeAppCache.getTagDedearchivesCache(gameId, gameRelation, archiveId);
                if (tagDedearchives == null) {
                    tagDedearchives = getTagDedearchives(new QueryExpress()
                            .add(QueryCriterions.eq(TagDedearchivesFiled.TAGID, gameId))
                            .add(QueryCriterions.eq(TagDedearchivesFiled.ARCHIVE_RELATION_TYPE, gameRelation.getCode()))
                            .add(QueryCriterions.eq(TagDedearchivesFiled.DEDE_ARCHIVES_ID, archiveId)));
                    if (tagDedearchives != null) {
                        joymeAppCache.putTagDedearchivesCache(gameId, gameRelation, archiveId, tagDedearchives);
                    }
                }
                list.add(tagDedearchives);
            }
        }

        return list;
    }

    private void scheduleTagDedearchivesTimerTask(Long tagid, String id, Date publishDate) {
        //<=当前时间，直接发布
        if (publishDate.getTime() <= System.currentTimeMillis()) {
            try {
                QueryExpress queryExpress = new QueryExpress();
                queryExpress.add(QueryCriterions.eq(TagDedearchivesFiled.ID, id));
                TagDedearchives tagDedearchives = getTagDedearchives(queryExpress);
                if (tagDedearchives != null) {
                    UpdateExpress updateExpress = new UpdateExpress();
                    updateExpress.set(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.VALID.getCode());
                    boolean bool = modifyTagDedearchives(tagid, tagDedearchives.getDede_archives_id(), queryExpress, updateExpress, ArchiveRelationType.TAG_RELATION);
                    if (bool) {
                        UpdateExpress up = new UpdateExpress();
                        up.increase(AnimeTagField.TOTAL_SUM, 1);
                        modifyAnimeTag(tagid, new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, tagid)), up);
                    }
                }
            } catch (Exception e) {
                GAlerter.lab("JoymeAppLogic scheduleTagDedearchivesTimerTask occur Exception.e", e);
            }
        } else {
            TagDedearchivesTimerTask timerTask = new TagDedearchivesTimerTask(tagid, id, publishDate);
            Timer timer = new Timer();
            timer.schedule(timerTask, publishDate);
        }
    }

    class TagDedearchivesTimerTask extends TimerTask {

        private Long tagid;
        private String id;
        private Date publishDate;

        public TagDedearchivesTimerTask(Long tagid, String id, Date publishDate) {
            this.tagid = tagid;
            this.id = id;
            this.publishDate = publishDate;
        }

        @Override
        public void run() {
            try {
                if (String.valueOf(publishDate.getTime()).equals(joymeAppRedis.getTagDedearchivesTimer(id))) {
                    System.out.println("====tag dede archives timer task:" + tagid + "," + id + "," + publishDate);
                    TagDedearchives tagDedearchives = getTagDedearchives(new QueryExpress().add(QueryCriterions.eq(TagDedearchivesFiled.ID, id)));
                    if (tagDedearchives != null) {
                        boolean bool = modifyTagDedearchives(tagDedearchives.getTagid(), tagDedearchives.getDede_archives_id(),
                                new QueryExpress().add(QueryCriterions.eq(TagDedearchivesFiled.ID, id)),
                                new UpdateExpress().set(TagDedearchivesFiled.REMOVE_STATUS, ValidStatus.VALID.getCode())
                                        .set(TagDedearchivesFiled.DISPLAY_ORDER, System.currentTimeMillis()), ArchiveRelationType.TAG_RELATION);
                        if (bool) {
                            UpdateExpress up = new UpdateExpress();
                            up.increase(AnimeTagField.TOTAL_SUM, 1);
                            modifyAnimeTag(tagDedearchives.getTagid(), new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, tagDedearchives.getTagid())), up);
                        }
                    }
                    Set<String> idSet = new HashSet<String>();
                    idSet.add(id);
                    joymeAppRedis.removeTagDedearchivesTimerTaskCache(tagid, idSet, publishDate);
                }
            } catch (ServiceException e) {
                GAlerter.lab("JoymeAppLogic scheduleTagDedearchivesTimerTask occur Exception.e", e);
            }
        }
    }

    @Override
    public Map<Long, AnimeTag> queryAnimetags(Set<Long> tagIds) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("JoymeAppLogic queryAnimetags,tagIds:" + tagIds);
        }
        Map<Long, AnimeTag> result = new HashMap<Long, AnimeTag>();
        if (CollectionUtil.isEmpty(tagIds)) {
            return result;
        }

        for (Long id : tagIds) {
            AnimeTag animeTag = getAnimeTag(id, new QueryExpress().add(QueryCriterions.eq(AnimeTagField.TAG_ID, id)));
            if (animeTag != null) {
                result.put(id, animeTag);
            }
        }
        return result;
    }
}

