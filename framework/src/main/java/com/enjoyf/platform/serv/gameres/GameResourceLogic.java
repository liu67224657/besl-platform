package com.enjoyf.platform.serv.gameres;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.gameres.GameDBHandler;
import com.enjoyf.platform.db.gameres.GameResourceHandler;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.serv.gameres.quartz.WikiStatsLogic;
import com.enjoyf.platform.serv.joymeapp.JoymeAppRedis;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.system.GameAgreeEvent;
import com.enjoyf.platform.service.event.system.GameDBModifyTimeEvent;
import com.enjoyf.platform.service.event.system.GameDBSumIncreaseEvent;
import com.enjoyf.platform.service.event.system.GameIncrCollectionCacheEvent;
import com.enjoyf.platform.service.event.system.GameSumIncreaseEvent;
import com.enjoyf.platform.service.gameres.*;
import com.enjoyf.platform.service.gameres.gamedb.*;
import com.enjoyf.platform.service.gameres.gamedb.collection.GameArchivesDTO;
import com.enjoyf.platform.service.gameres.gamedb.collection.GameCollectionDTO;
import com.enjoyf.platform.service.gameres.privilege.*;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveContentType;
import com.enjoyf.platform.service.joymeapp.gameclient.ArchiveRelationType;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchives;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.*;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.enjoyf.platform.util.sql.mongodb.MongoSort;
import com.enjoyf.platform.util.sql.mongodb.MongoSortOrder;
import com.mongodb.BasicDBObject;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Author: zhaoxin
 * Date: 11-8-25
 * Time: 下午4:35
 * Desc:
 */
public class GameResourceLogic implements GameResourceService {
    //
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //
    private GameResourceConfig config = null;

    //the handler's
    private GameResourceHandler writeAbleHandler;
    private HandlerPool<GameResourceHandler> readonlyHandlersPool;

    private GameDBHandler writeAbleGameDbHandler;
    private HandlerPool<GameDBHandler> readonlyGameDbHandler;

    private GameResourceCache gameResourceCache;

    private GroupPrivilegeCache groupPrivilegeCache;
    //
    private QueueThreadN eventProcessQueueThreadN = null;

    private WikiStatsLogic wikiStatsLogic;

    private GameResourceRedis gameResourceRedis;
    private JoymeAppRedis joymeAppRedis;

    //
    GameResourceLogic(GameResourceConfig cfg) {
        config = cfg;

        //initialize the handler.
        try {
            //
            readonlyHandlersPool = new HandlerPool<GameResourceHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyHandlersPool.add(new GameResourceHandler(dsn, config.getProps()));
            }

            writeAbleHandler = new GameResourceHandler(config.getWriteableDataSourceName(), config.getProps());
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());
            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        try {
            //
            readonlyGameDbHandler = new HandlerPool<GameDBHandler>();
            for (String dsn : config.getMongoDbReadonlyDataSourceNames()) {
                readonlyGameDbHandler.add(new GameDBHandler(dsn, config.getProps()));
            }

            writeAbleGameDbHandler = new GameDBHandler(config.getMongoDbWriteAbleDateSourceName(), config.getProps());
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());
            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

//        wikiStatsLogic = new WikiStatsLogic(writeAbleHandler);

        //todo
        if (cfg.isOpenStatTrigger()) {
            try {
                WikiStatsQuartzCronTrigger cronTrigger = new WikiStatsQuartzCronTrigger(wikiStatsLogic, cfg);

                cronTrigger.init();
                cronTrigger.start();
            } catch (SchedulerException e) {
                GAlerter.lab("WikiStatsQuartzCronTrigger start error.", e);
            }
        }


        //initialize the event
        eventProcessQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                if (obj instanceof Event) {
                    processQueuedEvent((Event) obj);
                } else {
                    GAlerter.lab("In eventProcessQueueThreadN, there is a unknown obj.");
                }
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "eventProcessQueue"));

        //initialize the caches.
        gameResourceCache = new GameResourceCache(this);

        groupPrivilegeCache = new GroupPrivilegeCache(config.getCachedConfig());

        gameResourceRedis = new GameResourceRedis(config.getProps());
        joymeAppRedis = new JoymeAppRedis(config.getProps());
    }

    private void processQueuedEvent(Event obj) {
        if (logger.isDebugEnabled()) {
            logger.debug("processQueuedEvent event :" + obj);
        }


        if (obj instanceof GameSumIncreaseEvent) {


        } else if (obj instanceof GameDBModifyTimeEvent) {
            GameDBModifyTimeEvent event = (GameDBModifyTimeEvent) obj;
            BasicDBObject query = new BasicDBObject();
            query.put(GameDBField.ID.getColumn(), event.getGamedbFileId());
            BasicDBObject update = new BasicDBObject();
            try {
                GameDB gameDB = getGameDB(query, false);
                GameDBModifyTimeFieldJson modifyTimeFieldJson = event.getModifyTimeFieldJson();
                GameDBModifyTimeFieldJson modifyTime = gameDB.getModifyTime() == null ? new GameDBModifyTimeFieldJson() : gameDB.getModifyTime();
                if (modifyTimeFieldJson.getLastModifyTime() != 0) {
                    modifyTime.setLastModifyTime(modifyTimeFieldJson.getLastModifyTime());
                }
                if (modifyTimeFieldJson.getGiftlastModifyTime() != 0) {
                    modifyTime.setGiftlastModifyTime(modifyTimeFieldJson.getGiftlastModifyTime());
                }

                if (modifyTimeFieldJson.getRedMessageTime() > 0l) {
                    modifyTime.setRedMessageTime(modifyTimeFieldJson.getRedMessageTime());
                    modifyTime.setRedMessageType(modifyTimeFieldJson.getRedMessageType());
                    modifyTime.setRedMessageText(modifyTimeFieldJson.getRedMessageText());
                }

                update.put(GameDBField.MODIFY_TIME_JSON.getColumn(), modifyTime.toJson());
                updateGameDB(query, update);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        } else if (obj instanceof GameDBSumIncreaseEvent) {
            GameDBSumIncreaseEvent event = (GameDBSumIncreaseEvent) obj;

            if (event.getGameDBField() == null) {
                GAlerter.lan(this.getClass().getName() + "event.getGameDBField()==null event:" + event);
                return;
            }
            BasicDBObject update = new BasicDBObject().append("$inc", new BasicDBObject(event.getGameDBField().getColumn(), event.getIncreateValue()));
            BasicDBObject query = new BasicDBObject();
            query.put(GameDBField.ID.getColumn(), event.getGameId());
            try {
                updateGameDB(query, update);
            } catch (Exception e) {
                GAlerter.lab(this.getClass().getName() + " occured Excepton.e:", e);
            }

        } else if (obj instanceof GameAgreeEvent) {
            GameAgreeEvent gameAgreeEvent = (GameAgreeEvent) obj;
            if (gameAgreeEvent.getGamedbId() == 0 || StringUtil.isEmpty(gameAgreeEvent.getKey())) {
                GAlerter.lan(this.getClass().getName() + "event.GameAgreeEvent param is null event:" + gameAgreeEvent);
                return;
            }
            try {
                BasicDBObject query = new BasicDBObject();
                query.put(GameDBField.ID.getColumn(), gameAgreeEvent.getGamedbId());
                GameDB gameDb = getGameDB(query, false);
                if (gameDb != null) {
                    CommentAndAgree commentAndAgree = gameDb.getCommentAndAgree();
                    String key = gameAgreeEvent.getKey();
                    int agree = 0;
                    if (key.equals(commentAndAgree.getComment1())) {
                        agree = Integer.parseInt(commentAndAgree.getAgree1());
                        commentAndAgree.setAgree1(String.valueOf(agree + 1));
                    } else if (key.equals(commentAndAgree.getComment2())) {
                        agree = Integer.parseInt(commentAndAgree.getAgree2());
                        commentAndAgree.setAgree2(String.valueOf(agree + 1));
                    } else if (key.equals(commentAndAgree.getComment3())) {
                        agree = Integer.parseInt(commentAndAgree.getAgree3());
                        commentAndAgree.setAgree3(String.valueOf(agree + 1));
                    }
                    BasicDBObject update = new BasicDBObject();
                    update.append("comment_and_agree", commentAndAgree.toJson());
                    updateGameDB(query, update);
                    groupPrivilegeCache.removeGameDBCache(gameAgreeEvent.getGamedbId());
                }
            } catch (ServiceException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
		} else if (obj instanceof GameIncrCollectionCacheEvent) {
			GameIncrCollectionCacheEvent gameIncrCollectionCacheEvent = (GameIncrCollectionCacheEvent) obj;
			if (StringUtil.isEmpty(gameIncrCollectionCacheEvent.getCode())|| gameIncrCollectionCacheEvent.getGameDbId()<=0l) {
				GAlerter.lan(this.getClass().getName() + "event.GameIncrCollectionCacheEvent param is null event:"+ gameIncrCollectionCacheEvent);
				return;
			}
			try {
                BasicDBObject query = new BasicDBObject();
                query.put(GameDBField.ID.getColumn(), gameIncrCollectionCacheEvent.getGameDbId());
                GameDB gameDb = getGameDB(query, false);
                if (null != gameDb) {
                	incrGameCollectionListCache(gameIncrCollectionCacheEvent.getCode(), 1,gameDb);
				}
				
			} catch (ServiceException e) {
				GAlerter.lab(this.getClass().getName() + " occured Excepton.e:", e);
			}
		}

    }

    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("receiveEvent event :" + event);
        }

        eventProcessQueueThreadN.add(event);

        return true;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public GameResource createGameResource(GameResource gameResource) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createGameResource, gameResource:" + gameResource);
        }

        return writeAbleHandler.insertBameResource(gameResource);
    }

    @Override
    public GameResource getGameResource(QueryExpress getExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getGameResource, getExpress:" + getExpress);
        }

        GameResource gameResource = readonlyHandlersPool.getHandler().getGameResource(getExpress);
        if (gameResource != null) {
            gameResource = buildGameResource(gameResource);
        }

        return gameResource;
    }

    @Override
    public List<GameResource> queryGameResources(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryGameResources, queryExpress:" + queryExpress);
        }

        List<GameResource> gameResourceList = readonlyHandlersPool.getHandler().queryGameResources(queryExpress);
        if (!CollectionUtil.isEmpty(gameResourceList)) {
            gameResourceList = getGameResources(gameResourceList);
        }

        return gameResourceList;
    }

    @Override
    public PageRows<GameResource> queryGameResourceByPage(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryGameResourceByPage, queryExpress:" + queryExpress + ", page:" + page);
        }

        PageRows<GameResource> gameResourcePageRows = readonlyHandlersPool.getHandler().queryGameResourcesByPage(queryExpress, page);
        if (!CollectionUtil.isEmpty(gameResourcePageRows.getRows())) {
            List<GameResource> gameResourceList = getGameResources(gameResourcePageRows.getRows());
            gameResourcePageRows.setRows(gameResourceList);
        }

        return gameResourcePageRows;
    }

    @Override
    public List<GameResource> queryGameResourcesBySynonyms(String synonyms) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryGameResourcesBySynonyms, synonyms:" + synonyms);
        }

        List<GameResource> gameResourceList = readonlyHandlersPool.getHandler().queryBySynonyms(synonyms);
        if (!CollectionUtil.isEmpty(gameResourceList)) {
            gameResourceList = getGameResources(gameResourceList);
        }

        return gameResourceList;
    }

    @Override
    public Map<String, List<GameResource>> queryGameResourceMapBySynonymses(Set<String> synonymses) throws ServiceException {
        Map<String, List<GameResource>> returnValue = new HashMap<String, List<GameResource>>();

        //
        for (String synonyms : synonymses) {
            returnValue.put(synonyms, this.queryGameResourcesBySynonyms(synonyms));
        }

        return returnValue;
    }

    @Override
    public boolean modifyGameResource(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyGameResource, updateExpress:" + updateExpress + ", queryExpress:" + queryExpress);
        }

        return writeAbleHandler.updateGameResource(updateExpress, queryExpress) > 0;
    }

    @Override
    public boolean deleteGameResource(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to deleteGameResource, queryExpress: " + queryExpress);
        }

        return writeAbleHandler.deleteGameResource(queryExpress) > 0;
    }

    @Override
    public GameRelation createRelation(GameRelation gameRelation) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createRelation, gameRelation: " + gameRelation);
        }

        return writeAbleHandler.insertGameRelation(gameRelation);
    }

    @Override
    public boolean modifyGameRelation(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyRelation, updateExpress: " + updateExpress + ";queryExpress: " + queryExpress);
        }

        return writeAbleHandler.updateGameRelation(updateExpress, queryExpress);
    }

    @Override
    public List<GameResource> queryGameResourceByRelationValue(String relationValue, GameRelationType gameRelationType) throws ServiceException {
        List<GameResource> gameResourceList = new ArrayList<GameResource>();

        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyRelation, relationValue: " + relationValue + ";gameRelationType: " + gameRelationType);
        }

        QueryExpress queryRelationExpress = new QueryExpress();
        queryRelationExpress.add(QueryCriterions.eq(GameRelationField.RELATIONVALUE, relationValue));
        queryRelationExpress.add(QueryCriterions.eq(GameRelationField.RELATIONTYPE, gameRelationType.getCode()));

        Map<Long, List<GameRelation>> gameRelationList = readonlyHandlersPool.getHandler().queryGameRelationByMap(queryRelationExpress);
        for (Long gId : gameRelationList.keySet()) {
            GameResource gameResource = getGameResource(new QueryExpress().add(QueryCriterions.eq(GameResourceField.RESOURCEID, gId)));
            if (gameResource != null) {
                gameResourceList.add(gameResource);
            }
        }

        return gameResourceList;
    }

    @Override
    public GameRelation getGameRelation(QueryExpress queryExpress) throws DbException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getRelation, queryExpress: " + queryExpress);
        }

        return readonlyHandlersPool.getHandler().getGameRelation(queryExpress);
    }

    @Override
    public List<GameRelation> queryGameRelation(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryGameRelation, queryExpress: " + queryExpress);
        }

        return readonlyHandlersPool.getHandler().queryGameRelation(queryExpress);
    }

    @Override
    public List<GameResource> queryGameResourceByRelationQueryExpress(QueryExpress queryExpress) throws ServiceException {
        List<GameResource> gameResourceList = new ArrayList<GameResource>();

        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryGameResourceByRelationQueryExpress queryExpress is " + queryExpress);
        }

        Map<Long, List<GameRelation>> gameRelationList = readonlyHandlersPool.getHandler().queryGameRelationByMap(queryExpress);
        for (Long gId : gameRelationList.keySet()) {
            GameResource gameResource = getGameResource(new QueryExpress().add(QueryCriterions.eq(GameResourceField.RESOURCEID, gId)));
            if (gameResource != null) {
                gameResourceList.add(gameResource);
            }
        }

        return gameResourceList;
    }

    @Override
    public Map<Long, GameResource> searchGameResourceByWord(String text, Pagination pagination) throws ServiceException {

        Map<Long, GameResource> returnMap = new HashMap<Long, GameResource>();

        Map<Long, GameResource> gameResourceMap = gameResourceCache.getCacheMap();
        for (GameResource gameResource : gameResourceMap.values()) {
            if (gameResource.getResourceName().contains(text)
                    || PinYinUtil.getAllFirstLetter(gameResource.getResourceName()).contains(text)
                    || PinYinUtil.getAllLetters(gameResource.getResourceName()).contains(text)) {
                returnMap.put(gameResource.getResourceId(), gameResource);

                if (returnMap.size() > pagination.getPageSize()) {
                    break;
                }
            }
        }

        return returnMap;
    }

    @Override
    public GameProperty createGameProperty(GameProperty gameProperty) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createGameProperty gameProperty :" + gameProperty);
        }

        return writeAbleHandler.insertGameProperty(gameProperty);
    }

    @Override
    public List<GameProperty> batchCreateGameProperts(List<GameProperty> gamePropertyList) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to batchCreateGameProperts gamePropertyList.size :" + gamePropertyList);
        }

        return writeAbleHandler.insertGameProperties(gamePropertyList);
    }

    @Override
    public boolean modifyGameProperty(List<GameProperty> gamePropertyList, long resourceId, GamePropertyDomain gamePropertyDomain) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyGameProperty gamePropertyList.size :" + gamePropertyList);
        }
        for (GameProperty gameProperty : gamePropertyList) {
            gameProperty.setResourceId(resourceId);
        }

        return writeAbleHandler.updateGameProperties(gamePropertyList, resourceId, gamePropertyDomain);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public GameLayout createGameLayout(GameLayout gameLayout) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createGameLayout gameLayout :" + gameLayout);
        }

        return writeAbleHandler.insertGameLayout(gameLayout);
    }

    @Override
    public boolean modifyGameLayout(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyGameLayout updateExpress :" + updateExpress + " queryExpress:" + queryExpress);
        }

        return writeAbleHandler.updateGameLayout(updateExpress, queryExpress);
    }

    @Override
    public GameLayout getGameLayout(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getGameLayout queryExpress:" + queryExpress);
        }

        return writeAbleHandler.getGameLayout(queryExpress);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public GamePrivacy createGamePrivacy(GamePrivacy privacy) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createGamePrivacy privacy:" + privacy);
        }

        return writeAbleHandler.insertGamePrivacy(privacy);
    }

    @Override
    public GamePrivacy getGamePrivacy(QueryExpress getExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getGamePrivacy privacy:" + getExpress);
        }

        return readonlyHandlersPool.getHandler().getGamePrivacy(getExpress);
    }

    @Override
    public List<GamePrivacy> queryGamePrivacies(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryGamePrivacies queryExpress:" + queryExpress);
        }

        return readonlyHandlersPool.getHandler().queryGamePrivacy(queryExpress);
    }

    @Override
    public boolean modifyGamePrivacy(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryGamePrivacies updateExpress:" + updateExpress + " queryExpress: " + queryExpress);
        }

        return writeAbleHandler.updateGamePrivacy(updateExpress, queryExpress);
    }

    @Override
    public boolean deleteGamePrivacy(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to deleteCategoryPrivacy queryExpress:" + queryExpress);
        }

        return writeAbleHandler.deleteGamePrivacy(queryExpress);
    }


    @Override
    public PageRows<WikiResource> queryWikiResourceByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryWikiResource queryExpress:" + queryExpress + " pagination: " + pagination);
        }

        return readonlyHandlersPool.getHandler().queryWikiResource(queryExpress, pagination);
    }

    @Override
    public List<WikiResource> queryWikiResource(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryWikiResource queryExpress:" + queryExpress);
        }

        return readonlyHandlersPool.getHandler().queryWikiResource(queryExpress);
    }

    @Override
    public WikiResource getWikiResource(QueryExpress getExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getWikiResource getExpress:" + getExpress);
        }

        return readonlyHandlersPool.getHandler().getWikiResource(getExpress);
    }

    @Override
    public boolean modifyWikiResource(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyWikiResource updateExpress:" + updateExpress + " queryExpress: " + queryExpress);
        }

        return writeAbleHandler.updateWikiResource(updateExpress, queryExpress);
    }

    @Override
    public WikiResource createWikiResource(WikiResource wikiResource) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyWikiResource wikiResource:" + wikiResource);
        }

        return writeAbleHandler.insertWikiResource(wikiResource);
    }

    public boolean statWiki(long resourceId) throws ServiceException {

        WikiResource wikiResource = readonlyHandlersPool.getHandler().getWikiResource(new QueryExpress().add(QueryCriterions.eq(WikiResourceField.RESOURCEID, resourceId)));

        if (wikiResource == null) {
            return false;
        }

        try {
            return wikiStatsLogic.statWiki(wikiResource);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Excpeiton.e:", e);
            return false;
        }
    }

    @Override
    public NewRelease createNewGameInfo(NewRelease newRelease, List<Long> newGameTagIdList, long cityId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createNewGameInfo,newRelease:" + newRelease);
        }
        NewRelease info = writeAbleHandler.insertNewGameInfo(newRelease);
        if (info == null) {
            return null;
        }
        NewReleaseTagRelation newTagRelation = null;
        for (Long id : newGameTagIdList) {
            newTagRelation = new NewReleaseTagRelation();
            newTagRelation.setNewGameInfoId(info.getNewReleaseId());
            newTagRelation.setNewGameTagId(id);
            writeAbleHandler.insertNewTagRelation(newTagRelation);
        }

        CityRelation cityRelation = new CityRelation();
        cityRelation.setCityId(cityId);
        cityRelation.setNewGameInfoId(info.getNewReleaseId());
        writeAbleHandler.insertCityRelation(cityRelation);

        return info;
    }

    @Override
    public boolean modifyNewGameInfo(long newGameInfoId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyNewGameInfo,newGameInfoId:" + newGameInfoId + ",updateExpress:" + updateExpress);
        }
        QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(NewReleaseField.NEW_RELEASE_ID, newGameInfoId));
        return writeAbleHandler.updateNewGameInfo(queryExpress, updateExpress);
    }

    @Override
    public NewRelease getNewGameInfo(long newGameInfoId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getNewGameInfo,newGameInfoId:" + newGameInfoId);
        }
        QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(NewReleaseField.NEW_RELEASE_ID, newGameInfoId));
        return readonlyHandlersPool.getHandler().getNewGameInfo(queryExpress);
    }

    @Override
    public List<NewRelease> queryNewGameInfo(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryNewGameInfo,queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryNewGameInfo(queryExpress);
    }

    @Override
    public PageRows<NewRelease> queryNewGameInfoByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryNewGameInfo,queryExpress:" + queryExpress + ",pagination" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryNewGameInfoByPage(queryExpress, pagination);
    }

    @Override
    public NewReleaseTag createNewGameTag(NewReleaseTag newGameTag) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createNewGameTag,newGameTag:" + newGameTag);
        }
        return writeAbleHandler.insertNewGameTag(newGameTag);
    }

    @Override
    public boolean modifyNewGameTag(long newGameTagId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyNewGameTag,newGameTagId:" + newGameTagId + ",updateExpress" + updateExpress);
        }
        QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(NewReleaseTagField.NEW_RELEASE_TAG_ID, newGameTagId));
        return writeAbleHandler.updateNewGameTag(queryExpress, updateExpress);
    }

    @Override
    public NewReleaseTag getNewGameTag(long newGameTagId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getNewGameTag,newGameTagId:" + newGameTagId);
        }
        QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(NewReleaseTagField.NEW_RELEASE_TAG_ID, newGameTagId));
        return readonlyHandlersPool.getHandler().getNewGameTag(queryExpress);
    }

    @Override
    public List<NewReleaseTag> queryNewGameTag(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryNewGameTag,queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryNewGameTag(queryExpress);
    }

    @Override
    public PageRows<NewReleaseTag> queryNewGameTagByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryNewGameTagByPage,queryExpress:" + queryExpress + ",pagination" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryNewGameTagByPage(queryExpress, pagination);
    }

    @Override
    public List<NewReleaseTagRelation> queryNewTagRelation(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryNewTagRelation,queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryNewTagRelation(queryExpress);
    }

    @Override
    public City createCity(City city) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createCity,city:" + city);
        }
        return writeAbleHandler.createCity(city);
    }

    @Override
    public boolean modifyCity(long cityId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyCity,cityId:" + cityId + ",updateExpress" + updateExpress);
        }
        QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(CityField.CITY_ID, cityId));
        return writeAbleHandler.updateCity(queryExpress, updateExpress);
    }

    @Override
    public List<City> queryCity(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryCity,queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryCity(queryExpress);
    }

    @Override
    public List<CityRelation> queryCityRelation(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryCityRelation,queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryCityRelation(queryExpress);
    }

    @Override
    public boolean modifyCityRelation(long cityRelationId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyCityRelateon,cityRelationId:" + cityRelationId);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(CityRelationField.CITY_RELATION_ID, cityRelationId));
        return writeAbleHandler.modifyCityRelation(queryExpress, updateExpress);
    }

    @Override
    public NewReleaseTagRelation createTagRelation(NewReleaseTagRelation tagRelation) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createTagRelation,tagRelation:" + tagRelation);
        }
        return writeAbleHandler.createTagRelation(tagRelation);
    }

    @Override
    public boolean modifyTagRelation(long newTagRelationId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyTagRelation,newTagRelationId:" + newTagRelationId);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(NewReleaseTagRelationField.TAG_RELATION_ID, newTagRelationId));
        return writeAbleHandler.modifyTagRelation(updateExpress, queryExpress);
    }

    /////////////////////////////////////////////////////////////////////


    @Override
    public GroupUser createGroupUser(GroupUser groupUser) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createGroupUser ,groupUser:" + groupUser);
        }
        return writeAbleHandler.insertGroupUser(groupUser);
    }

    public GroupUser getGroupUser(String uno, long groupId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getGroupUser ,uno:" + uno + " groupId:" + groupId);
        }
        return readonlyHandlersPool.getHandler().getGroupUserByGroupIdUno(uno, groupId);
    }

    public PageRows<GroupUser> queryGroupUser(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryGroupUser ,queryExpress:" + queryExpress + " pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryGroupUser(queryExpress, pagination);
    }

    public boolean modifyGroupUser(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyGroupUser ,updateExpress:" + updateExpress + " queryExpress:" + queryExpress);
        }
        return writeAbleHandler.updateGroupUser(updateExpress, queryExpress);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * group privilege
     *
     * @return
     * @throws DbException
     */

    @Override
    public Role createRole(Role role) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createRole,role:" + role);
        }
        Role returnRole = writeAbleHandler.insertRole(role);
//        if (returnRole != null) {
//            groupPrivilegeCache.removeRoleByGroup(returnRole.getGroupId());
//        }
        return returnRole;
    }

    @Override
    public Role getRole(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getRole,QueryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getRole(queryExpress);
    }

    @Override
    public List<Role> queryRole(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryRole,queryExpress:" + queryExpress);
        }
//        List<Role> list = groupPrivilegeCache.getRoleByGroup(groupId);
//        if (CollectionUtil.isEmpty(list)) {
//            QueryExpress queryExpress = new QueryExpress();
//            queryExpress.add(QueryCriterions.eq(RoleField.GROUP_ID, groupId));
//            list = readonlyHandlersPool.getHandler().queryRole(queryExpress);
//            if (!CollectionUtil.isEmpty(list)) {
//                groupPrivilegeCache.putRoleByGroup(groupId, list);
//            }
//        }
        return readonlyHandlersPool.getHandler().queryRole(queryExpress);
    }

    @Override
    public PageRows<Role> queryRoleByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryRoleByPage,queryExpress:" + queryExpress + ",pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryRoleByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyRole(long roleId, long groupId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyRole,roleId:" + roleId + ",updateExpress:" + updateExpress);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(RoleField.ROLE_ID, roleId));
        boolean bool = writeAbleHandler.updateRole(updateExpress, queryExpress);
//        if (bool) {
//            groupPrivilegeCache.removeRoleByGroup(groupId);
//        }
        return bool;
    }

    @Override
    public PrivilegeRoleRelation createPrivilegeRoleRelation(PrivilegeRoleRelation relation) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createPrivilegeRoleRelation,relation:" + relation);
        }
        PrivilegeRoleRelation privilegeRoleRelation = writeAbleHandler.insertPrivilegeRoleRelation(relation);
        if (privilegeRoleRelation != null) {
            groupPrivilegeCache.removeRelationByRole(privilegeRoleRelation.getRoleId());
        }
        return privilegeRoleRelation;
    }

    @Override
    public PrivilegeRoleRelation getPrivilegeRoleRelation(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getPrivilegeRoleRelation,queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getPrivilegeRoleRelation(queryExpress);
    }

    @Override
    public List<PrivilegeRoleRelation> queryPrivilegeRoleRelation(long roleId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryPrivilegeRoleRelation,roleId:" + roleId);
        }
        List<PrivilegeRoleRelation> list = groupPrivilegeCache.getRelationByRole(roleId);
        if (CollectionUtil.isEmpty(list)) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(PrivilegeRoleRelationField.ROLE_ID, roleId));
            list = readonlyHandlersPool.getHandler().queryPrivilegeRoleRelation(queryExpress);
            if (!CollectionUtil.isEmpty(list)) {
                groupPrivilegeCache.putRelationByRole(roleId, list);
            }
        }
        return list;
    }

    @Override
    public PageRows<PrivilegeRoleRelation> queryPrivilegeRoleRelationByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryPrivilegeRoleRelation,queryExpress:" + queryExpress + ",pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryPrivilegeRoleRelationByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyPrivilegeRoleRelation(long relationId, long roleId, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyPrivilegeRoleRelation,relationId:" + relationId + ",updateExpress:" + updateExpress);
        }
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(PrivilegeRoleRelationField.RELATION_ID, relationId));
        Boolean bool = writeAbleHandler.updatePrivilegeRoleRelation(updateExpress, queryExpress);
        if (bool) {
            groupPrivilegeCache.removeRelationByRole(roleId);
        }
        return bool;
    }

    @Override
    public GroupPrivilege createGroupPrivilege(GroupPrivilege groupPrivilege) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createGroupPrivilege,groupPrivilege:" + groupPrivilege);
        }
        return writeAbleHandler.insertGroupPrivilege(groupPrivilege);
    }

    @Override
    public GroupPrivilege getGroupPrivilege(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getGroupPrivilege,queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getGroupPrivilege(queryExpress);
    }

    @Override
    public List<GroupPrivilege> queryGroupPrivilege(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryGroupPrivilege,queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryGroupPrivilege(queryExpress);
    }

    @Override
    public PageRows<GroupPrivilege> queryGroupPrivilegeByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryGroupPrivilegeByPage,queryExpress:" + queryExpress + ",pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryGroupPrivilegeByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyGroupPrivilege(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyGroupPrivilege,queryExpress:" + queryExpress + ",updateExpress:" + updateExpress);
        }
        return writeAbleHandler.updateGroupPrivilege(updateExpress, queryExpress);
    }

    @Override
    public GroupProfile createGroupProfile(GroupProfile groupProfile) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createGroupProfile,groupProfile:" + groupProfile);
        }
        return writeAbleHandler.insertGroupProfile(groupProfile);
    }

    @Override
    public GroupProfile getGroupProfile(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getGroupProfile,queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getGroupProfile(queryExpress);
    }

    @Override
    public List<GroupProfile> queryGroupProfile(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryGroupProfile,queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryGroupProfile(queryExpress);
    }

    @Override
    public PageRows<GroupProfile> queryGroupProfileByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryGroupProfileByPage,queryExpress:" + queryExpress + ",pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryGroupProfileByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyGroupProfile(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyGroupProfile,QueryExpress:" + queryExpress + ",updateExpress:" + updateExpress);
        }
        return writeAbleHandler.modifyGroupProfile(updateExpress, queryExpress);
    }

    @Override
    public GroupProfilePrivilege createGroupProfilePrivilege(GroupProfilePrivilege groupProfilePrivilege) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to createGroupProfilePrivilege,groupProfilePrivilege:" + groupProfilePrivilege);
        }
        return writeAbleHandler.insertGroupProfilePrivilege(groupProfilePrivilege);
    }

    @Override
    public GroupProfilePrivilege getGroupProfilePrivilege(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getGroupProfilePrivilege,queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().getGroupProfilePrivilege(queryExpress);
    }

    @Override
    public List<GroupProfilePrivilege> queryGroupProfilePrivilege(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryGroupProfilePrivilege,queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryGroupProfilePrivilege(queryExpress);
    }

    @Override
    public PageRows<GroupProfilePrivilege> queryGroupProfilePrivilegeByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryGroupProfilePrivilegeByPage,queryExpress:" + queryExpress + ",pagination:" + pagination);
        }
        return readonlyHandlersPool.getHandler().queryGroupProfilePrivilegeByPage(queryExpress, pagination);
    }

    @Override
    public boolean modifyGroupProfilePrivilege(QueryExpress queryExpress, UpdateExpress updateExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to modifyGroupProfilePrivilege,queryExpress:" + queryExpress + ",updateExpress:" + updateExpress);
        }
        return writeAbleHandler.updateGroupProfilePrivilege(updateExpress, queryExpress);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public GameDB createGameDb(GameDB gameDb) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call writeAbleGameDbHandler to insertGameDb,gameDb:" + gameDb);
        }
        return writeAbleGameDbHandler.insertGameDB(gameDb);
    }

    @Override
    public int countGameDB(MongoQueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call writeAbleGameDbHandler to countGameDB,BasicDBObject:" + queryExpress);
        }
        return readonlyGameDbHandler.getHandler().countGameDB(queryExpress);
    }


    @Override
    public List<GameDB> queryGameDB(MongoQueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call writeAbleGameDbHandler to queryGameDB,BasicDBObject:" + queryExpress);
        }
        return readonlyGameDbHandler.getHandler().query(queryExpress);
    }


    @Override
    public List<GameDB> queryGameDBByCache() throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call writeAbleGameDbHandler queryGameDBByCache ");
        }
        List<GameDB> lists = groupPrivilegeCache.getGameDBList();
        if (CollectionUtil.isEmpty(lists)) {
            PageRows<GameDB> pageRows = readonlyGameDbHandler.getHandler().queryGameDbByPage(new MongoQueryExpress().add(MongoQueryCriterions.eq(GameDBField.VALIDSTATUS, ValidStatus.VALID.getCode())), new Pagination(70));
            if (pageRows != null) {
                if (!CollectionUtil.isEmpty(pageRows.getRows())) {
                    groupPrivilegeCache.putGameDBList(pageRows.getRows());
                    return pageRows.getRows();
                }
            }
        }
        return lists;  //To change body of implemented methods use File | Settings | File Templx`ates.
    }

    @Override
    public List<GameDB> queryGameDBByCategory(String categoryId, MongoQueryExpress mongoQueryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call writeAbleGameDbHandler to queryGameDBByCategory,categoryId:" + categoryId + "  mongoQueryExpress" + mongoQueryExpress);
        }
        List<GameDB> lists = groupPrivilegeCache.getGameCategoryList(categoryId);
        if (lists == null) {
            List<GameDB> gameDBs = readonlyGameDbHandler.getHandler().query(mongoQueryExpress);
            if (!CollectionUtil.isEmpty(gameDBs)) {
                groupPrivilegeCache.putGameCategoryCache(categoryId, gameDBs);
                return gameDBs;
            }
        }
        return lists;
    }

    @Override
    public PageRows<GameDB> queryGameDbByPage(MongoQueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call writeAbleGameDbHandler to queryGameDB,MongoQueryExpress:" + queryExpress + "Pagination: " + pagination);
        }
        return readonlyGameDbHandler.getHandler().queryGameDbByPage(queryExpress, pagination);
    }

    @Override
    public GameDB getGameDB(BasicDBObject basicDBObject, boolean isTools) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call writeAbleGameDbHandler to getGameDB,BasicDBObject:" + basicDBObject);
        }
        Object object = basicDBObject.get("_id");
        if (isTools) {
            return readonlyGameDbHandler.getHandler().get(basicDBObject);
        } else {
            GameDB gameDb = null;
            if (object != null) {
                gameDb = groupPrivilegeCache.getGameDBCache((Long) object);
            }
            if (gameDb == null) {
                gameDb = readonlyGameDbHandler.getHandler().get(basicDBObject);
                if (gameDb != null) {
                    groupPrivilegeCache.putGameDBCache(gameDb.getGameDbId(), gameDb);
                }
            }
            return gameDb;
        }
    }

    @Override
    public boolean updateGameDB(BasicDBObject query, BasicDBObject update) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call writeAbleGameDbHandler to getGameDB,BasicDBObjectQuery:" + query + " BasicDBObjectUpdate:" + update);
        }
        boolean bool = writeAbleGameDbHandler.update(query, update);
        if (bool) {
            groupPrivilegeCache.removeGameDBCache((Long) query.get(GameDBField.ID.getColumn()));
            groupPrivilegeCache.removeGameDBByAnotherName(update.get(GameDBField.ANOTHERNAME.getColumn()));
            gameResourceRedis.removeGameCollectionDTO((Long) query.get(GameDBField.ID.getColumn()));
        }
        return bool;
    }

    @Override
    public Map<Long, GameDB> queryGameDBSet(Set<Long> setLong) throws ServiceException {
        Map<Long, GameDB> returnMap = new LinkedHashMap<Long, GameDB>();
        if (CollectionUtil.isEmpty(setLong)) {
            return returnMap;
        }
        for (Long id : setLong) {
            GameDB gamedb = groupPrivilegeCache.getGameDBCache(id);
            if (gamedb == null) {
                gamedb = readonlyGameDbHandler.getHandler().get(new BasicDBObject("_id", id));
                if (gamedb != null) {
                    groupPrivilegeCache.putGameDBCache(gamedb.getGameDbId(), gamedb);
                    returnMap.put(id, gamedb);
                }
            } else {
                returnMap.put(id, gamedb);
            }
        }
        return returnMap;
    }

    @Override
    public List<GameDBChannel> queryGameDbChannel() throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to queryGameDbChannelEntry:");
        }
        List<GameDBChannel> channelList = new ArrayList<GameDBChannel>();
        Collection<AppChannelType> collection = AppChannelType.getAll();
        for (AppChannelType channelType : collection) {
            GameDBChannel channel = new GameDBChannel();
            channel.setChannelId(channelType.getId());
            channel.setChannelCode(channelType.getCode());
            channel.setChannelName(channelType.getName());
            channelList.add(channel);
        }

        //
        Collections.sort(channelList, new Comparator<GameDBChannel>() {
            @Override
            public int compare(GameDBChannel o1, GameDBChannel o2) {
                long jt1 = o1.getChannelId();
                long jt2 = o2.getChannelId();
                return jt1 > jt2 ? 1 : (o1 == o2 ? 0 : -1);
            }
        });
        return channelList;
    }


    @Override
    public GameDBChannel getGameDbChannel(String channelCode) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to getGameDbChannel,channelCode:" + channelCode);
        }
        AppChannelType appChannelType = AppChannelType.getByCode(channelCode);
        GameDBChannel channel = null;
        if (appChannelType != null) {
            channel = new GameDBChannel();
            channel.setChannelId(appChannelType.getId());
            channel.setChannelCode(appChannelType.getCode());
            channel.setChannelName(appChannelType.getName());
        }
        return channel;
    }

    @Override
    public List<GamePropertyInfo> createGamePropertyInfo(List<GamePropertyInfo> gamePropertyInfoList) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call writeAbleHandler to insertGamePropertyInfo,gamePropertyInfoList:" + gamePropertyInfoList);
        }
        return writeAbleHandler.insertGamePropertyInfo(gamePropertyInfoList);
    }

    @Override
    public List<GameDB> queryGamePropertyInfo(List<GamePropertyInfo> paramList) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call writeAbleHandler to queryGamePropertyInfo,paramList:" + paramList);
        }
        List<GameDB> returnList = new LinkedList<GameDB>();
        Set<Long> idSet = new HashSet<Long>();
        for (GamePropertyInfo info : paramList) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(GamePropertyInfoField.KEY_NAME, info.getKeyName()));
            queryExpress.add(QueryCriterions.eq(GamePropertyInfoField.VALUE_TYPE, info.getGamePropertyValueType().getValue()));
            if (GamePropertyValueType.INTEGER.equals(info.getGamePropertyValueType())) {
                queryExpress.add(QueryCriterions.eq(GamePropertyInfoField.INT_VALUE, info.getIntValue()));
            } else if (GamePropertyValueType.DATE.equals(info.getGamePropertyValueType())) {
                queryExpress.add(QueryCriterions.eq(GamePropertyInfoField.DATE_VALUE, info.getDateValue()));
            } else if (GamePropertyValueType.STRING.equals(info.getGamePropertyValueType())) {
                queryExpress.add(QueryCriterions.eq(GamePropertyInfoField.STRING_VALUE, info.getStringValue()));
            } else if (GamePropertyValueType.BOOLEAN.equals(info.getGamePropertyValueType())) {
                queryExpress.add(QueryCriterions.eq(GamePropertyInfoField.BOOLEAN_VALUE, info.getBooleanValue()));
            } else if (GamePropertyValueType.DEFAULT.equals(info.getGamePropertyValueType())) {
                continue;
            }
            List<GamePropertyInfo> infoList = queryGamePropertyInfoByQuery(queryExpress);
            for (GamePropertyInfo info2 : infoList) {
                idSet.add(info2.getKeyId());
            }
        }
        for (Long id : idSet) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(GamePropertyInfoField.KEY_ID, id));
            GameDB gameDB = buildGameDB(queryExpress);
            returnList.add(gameDB);
        }
        return returnList;
    }

    @Override
    public PageRows<GameDB> queryGamePropertyInfoByPage(List<GamePropertyInfo> paramList, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call writeAbleHandler to queryGamePropertyInfo,paramList:" + paramList);
        }
        PageRows<GameDB> pageRows = new PageRows<GameDB>();
        List<GameDB> returnList = new LinkedList<GameDB>();
        Set<Long> idSet = new HashSet<Long>();
        for (GamePropertyInfo info : paramList) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(GamePropertyInfoField.KEY_NAME, info.getKeyName()));
            queryExpress.add(QueryCriterions.eq(GamePropertyInfoField.VALUE_TYPE, info.getGamePropertyValueType()));
            if (GamePropertyValueType.INTEGER.equals(info.getGamePropertyValueType())) {
                queryExpress.add(QueryCriterions.eq(GamePropertyInfoField.INT_VALUE, info.getIntValue()));
            } else if (GamePropertyValueType.DATE.equals(info.getGamePropertyValueType())) {
                queryExpress.add(QueryCriterions.eq(GamePropertyInfoField.DATE_VALUE, info.getDateValue()));
            } else if (GamePropertyValueType.STRING.equals(info.getGamePropertyValueType())) {
                queryExpress.add(QueryCriterions.eq(GamePropertyInfoField.STRING_VALUE, info.getStringValue()));
            } else if (GamePropertyValueType.BOOLEAN.equals(info.getGamePropertyValueType())) {
                queryExpress.add(QueryCriterions.eq(GamePropertyInfoField.BOOLEAN_VALUE, info.getBooleanValue()));
            } else if (GamePropertyValueType.DEFAULT.equals(info.getGamePropertyValueType())) {
                continue;
            }
            PageRows<GamePropertyInfo> infoPageRows = queryGamePropertyInfoByPage(queryExpress, pagination);
            pageRows.setPage(infoPageRows.getPage());
            for (GamePropertyInfo info2 : infoPageRows.getRows()) {
                idSet.add(info2.getKeyId());
            }
        }
        for (Long id : idSet) {
            QueryExpress queryExpress = new QueryExpress();
            queryExpress.add(QueryCriterions.eq(GamePropertyInfoField.KEY_ID, id));
            GameDB gameDB = buildGameDB(queryExpress);
            returnList.add(gameDB);
        }
        pageRows.setRows(returnList);
        return pageRows;
    }

    private List<GamePropertyInfo> queryGamePropertyInfoByQuery(QueryExpress queryExpress) throws DbException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call readonlyHandlersPool to queryGamePropertyInfo,queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryGamePropertyInfo(queryExpress);
    }

    private PageRows<GamePropertyInfo> queryGamePropertyInfoByPage(QueryExpress queryExpress, Pagination pagination) throws DbException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call readonlyHandlersPool to queryGamePropertyInfo,queryExpress:" + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryGamePropertyInfoByPage(queryExpress, pagination);
    }

    private GameDB buildGameDB(QueryExpress queryExpress) throws DbException {
        List<GamePropertyInfo> list = queryGamePropertyInfoByQuery(queryExpress);
        Map<String, Object> map = new HashMap<String, Object>();
        Map<GameDBChannel, GameDBChannelInfo> map2 = new HashMap<GameDBChannel, GameDBChannelInfo>();
        Map<GamePlatformCode, GamePlatformInfo> map3 = new HashMap<GamePlatformCode, GamePlatformInfo>();
        Set<GameDBChannelInfo> set = new HashSet<GameDBChannelInfo>();
        for (GamePropertyInfo info : list) {
            if (GamePropertyKeyNameCode.CHANNEL.equals(info.getKeyNameCode().getValue())) {
                GameDBChannelInfo channelInfo = new GameDBChannelInfo();
                GameDBChannel channel = new GameDBChannel();
                channel.setChannelId(Long.parseLong(info.getKeyName()));
                map2.put(channel, channelInfo);
                map.put(info.getKeyNameCode().getValue(), map2);
                set.add(channelInfo);
            } else if (GamePropertyKeyNameCode.PLATFORM.equals(info.getKeyNameCode().getValue())) {
                GamePlatformInfo platformInfo = new GamePlatformInfo();
                platformInfo.setDownloadUrl(info.getStringValue());
                platformInfo.setAppPlatform(AppPlatform.getByCode(Integer.parseInt(info.getKeyName())));
                map3.put(GamePlatformCode.getByCode(Integer.parseInt(info.getKeyName())), platformInfo);
                map.put(info.getKeyNameCode().getValue(), map3);
            } else {
                map.put(info.getKeyNameCode().getValue(), info.getIntValue() == null ? (info.getStringValue() == null ? (info.getDateValue() == null ? (info.getBooleanValue() == null ? null : info.getBooleanValue()) : info.getDateValue()) : info.getStringValue()) : info.getIntValue());
            }
        }
        GameDB gameDB = new GameDB();
        gameDB.setGameName((String) map.get(GamePropertyKeyNameCode.NAME.getValue()));
        gameDB.setGameIcon((String) map.get(GamePropertyKeyNameCode.ICON.getValue()));
        gameDB.setGameDeveloper((String) map.get(GamePropertyKeyNameCode.DEVELOPER.getValue()));
        //TODO
        //gameDB.setChannelInfoSet(set);
        //gameDB.setGameDBChannelInfoMap((Map<GameDBChannel, GameDBChannelInfo>) map.get(GamePropertyKeyNameCode.CHANNEL.getValue()));
        //gameDB.setPlatformInfoMap((Map<AppPlatform, GamePlatformInfo>) map.get(GamePropertyKeyNameCode.PLATFORM.getValue()));
        return gameDB;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    private GameResource buildGameResource(GameResource gameResource) throws DbException {
        QueryExpress queryRelationExpress = new QueryExpress();
        queryRelationExpress.add(QueryCriterions.eq(GameRelationField.RESOURCEID, gameResource.getResourceId()));
        queryRelationExpress.add(QueryCriterions.eq(GameRelationField.VALIDSTATUS, ValidStatus.VALID.getCode()));

        Map<Long, List<GameRelation>> gameRelationMap = readonlyHandlersPool.getHandler().queryGameRelationByMap(queryRelationExpress);

        Map<Long, List<GameProperty>> gamePropertyMap = readonlyHandlersPool.getHandler().queryGameProperties(new QueryExpress()
                .add(QueryCriterions.eq(GameRelationField.RESOURCEID, gameResource.getResourceId())));


        if (gameRelationMap.containsKey(gameResource.getResourceId())) {
            gameResource.setGameRelationSet(buildGameRelationSet(gameResource.getResourceId(), gameRelationMap.get(gameResource.getResourceId())));
        }
        if (gamePropertyMap.containsKey(gameResource.getResourceId())) {
            gameResource.setGameProperties(buildGameProperties(gameResource.getResourceId(), gamePropertyMap.get(gameResource.getResourceId())));
        }

        return gameResource;
    }

    private List<GameResource> getGameResources(List<GameResource> gameResourceList) throws DbException {
        Set<Long> gameResourceid = new HashSet<Long>();
        for (GameResource gameResource : gameResourceList) {
            gameResourceid.add(gameResource.getResourceId());
        }

        QueryExpress queryRelationExpress = new QueryExpress();
        queryRelationExpress.add(QueryCriterions.in(GameRelationField.RESOURCEID, gameResourceid.toArray()));
        queryRelationExpress.add(QueryCriterions.eq(GameRelationField.VALIDSTATUS, ValidStatus.VALID.getCode()));
        Map<Long, List<GameRelation>> gameRelationMap = readonlyHandlersPool.getHandler().queryGameRelationByMap(queryRelationExpress);

        Map<Long, List<GameProperty>> gamePropertyMap = readonlyHandlersPool.getHandler().queryGameProperties(new QueryExpress()
                .add(QueryCriterions.in(GameRelationField.RESOURCEID, gameResourceid.toArray())));

        for (GameResource gameResource : gameResourceList) {
            if (gameRelationMap.containsKey(gameResource.getResourceId())) {
                gameResource.setGameRelationSet(buildGameRelationSet(gameResource.getResourceId(), gameRelationMap.get(gameResource.getResourceId())));
            }
            if (gamePropertyMap.containsKey(gameResource.getResourceId())) {
                gameResource.setGameProperties(buildGameProperties(gameResource.getResourceId(), gamePropertyMap.get(gameResource.getResourceId())));
            }
        }


        return gameResourceList;
    }

    private GameRelationSet buildGameRelationSet(long gameResourceId, List<GameRelation> relationList) {
        GameRelationSet returnObj = new GameRelationSet();
        returnObj.setGameResourceId(gameResourceId);

        Collections.sort(relationList);
        for (GameRelation gameRelation : relationList) {
            if (gameRelation.getGameRelationType().equals(GameRelationType.GAME_RELATION_TYPE_BOARD)) {
                returnObj.setBoardRelation(gameRelation);
            } else if (gameRelation.getGameRelationType().equals(GameRelationType.GAME_RELATION_TYPE_BAIKE)) {
                returnObj.setBaikeRelation(gameRelation);
            } else if (gameRelation.getGameRelationType().equals(GameRelationType.GAME_RELATION_TYPE_COVER)) {
                returnObj.setCoverRelation(gameRelation);
            } else if (gameRelation.getGameRelationType().equals(GameRelationType.GAME_RELATION_TYPE_INVITE)) {
                returnObj.setInviteRelation(gameRelation);
            } else if (gameRelation.getGameRelationType().equals(GameRelationType.GAME_RELATION_TYPE_RELATED_GAME)) {
                returnObj.getGameMap().put(gameRelation.getRelationId(), gameRelation);
            } else if (gameRelation.getGameRelationType().equals(GameRelationType.GAME_RELATION_TYPE_RELATED_GROUP)) {
                returnObj.getGroupMap().put(gameRelation.getRelationId(), gameRelation);
            } else if (gameRelation.getGameRelationType().equals(GameRelationType.GAME_RELATION_TYPE_ARTICLE)) {
                returnObj.getArticleMap().put(gameRelation.getRelationId(), gameRelation);
            } else if (gameRelation.getGameRelationType().equals(GameRelationType.GAME_RELATION_TYPE_MENU)) {
                returnObj.setMenuRelation(gameRelation);
            } else if (gameRelation.getGameRelationType().equals(GameRelationType.GAME_RELATION_TYPE_DOWNLOAD)) {
                returnObj.setDownLoadRelation(gameRelation);
            } else if (gameRelation.getGameRelationType().equals(GameRelationType.GAME_RELATION_TYPE_GROUPCONTENT)) {
                returnObj.setGroupContnetRelation(gameRelation);
            } else if (gameRelation.getGameRelationType().equals(GameRelationType.GAME_RELATION_TYPE_TALENT)) {
                returnObj.setTalentRelation(gameRelation);
            } else if (gameRelation.getGameRelationType().equals(GameRelationType.GAME_RELATION_TYPE_CONTRIBUTE)) {
                returnObj.setContributeRelation(gameRelation);
            } else if (gameRelation.getGameRelationType().equals(GameRelationType.GAME_RELATION_TYPE_HEADIMAGE)) {
                returnObj.setHeadImageRelation(gameRelation);
            }
        }
        returnObj.addGameRelation(relationList);

        return returnObj;
    }

    private GameProperties buildGameProperties(long gameResourceId, List<GameProperty> gamePropertyList) {
        GameProperties returnObj = new GameProperties();
        returnObj.setResourceId(gameResourceId);

        Collections.sort(gamePropertyList);
        for (GameProperty gameProperty : gamePropertyList) {
            if (gameProperty.getGamePropertyDomain().equals(GamePropertyDomain.DOMAIN_CHANNEL)) {
                if (returnObj.getChannels() == null) {
                    returnObj.setChannels(new ArrayList<GameProperty>());
                }

                returnObj.getChannels().add(gameProperty);
            } else if (gameProperty.getGamePropertyDomain().equals(GamePropertyDomain.UPDATEINFO)) {
                returnObj.setUpdateInfo(gameProperty);
            }
        }

        return returnObj;
    }


    @Override
    public GameDBRelation createGameDbRelation(GameDBRelation gameDBRelation) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call readonlyHandlersPool to createGameDbRelation,gameDBRelation:" + gameDBRelation);
        }

        gameDBRelation = writeAbleGameDbHandler.insertGameDbRelation(gameDBRelation);

        groupPrivilegeCache.removeRelations(gameDBRelation.getGamedbId());

        return gameDBRelation;
    }

    @Override
    public List<GameDBRelation> queryGameDBRelationbyGameDbId(long gamedbId, boolean fromCache) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call readonlyHandlersPool to queryGameDBRelationbyGameDbId,gamedbId:" + gamedbId + " fromCache:" + fromCache);
        }

        List<GameDBRelation> resultList = null;
        if (fromCache) {
            resultList = groupPrivilegeCache.getRelations(gamedbId);
        } else {
            return readonlyGameDbHandler.getHandler().queryGameDBRelation(new QueryExpress()
                    .add(QueryCriterions.eq(GameDBRelationField.GAMEDBID, gamedbId))
                    .add(QuerySort.add(GameDBRelationField.DISPLAYORDER, QuerySortOrder.ASC)));
        }

        if (CollectionUtil.isEmpty(resultList)) {
            resultList = readonlyGameDbHandler.getHandler().queryGameDBRelation(new QueryExpress()
                    .add(QueryCriterions.eq(GameDBRelationField.GAMEDBID, gamedbId))
                    .add(QueryCriterions.eq(GameDBRelationField.VALIDSTATUS, IntValidStatus.VALID.getCode()))
                    .add(QuerySort.add(GameDBRelationField.DISPLAYORDER, QuerySortOrder.ASC)));
            if (!CollectionUtil.isEmpty(resultList)) {
                groupPrivilegeCache.putRelations(gamedbId, resultList);
            }
        }

        return resultList;

    }

    @Override
    public boolean updateGameDbRelation(UpdateExpress updateExpress, long realitonId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call readonlyHandlersPool to updateGameDbRelation,updateExpress:" + updateExpress + " realitonId:" + realitonId);
        }

        QueryExpress queryExpress = new QueryExpress().add(QueryCriterions.eq(GameDBRelationField.RELATIONID, realitonId));
        GameDBRelation gameDBRelation = writeAbleGameDbHandler.getGameDbRelation(queryExpress);

        if (gameDBRelation == null) {
            return false;
        }

        boolean result = writeAbleGameDbHandler.updateGameDBRelation(updateExpress, queryExpress);

        if (result) {
            groupPrivilegeCache.removeRelations(gameDBRelation.getGamedbId());
        }

        return result;
    }


    @Override
    public GameDBRelation getGameDbRelation(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call readonlyHandlersPool to updateGameDbRelation,gameDBRelation:" + queryExpress);
        }

        GameDBRelation gameDBRelation = writeAbleGameDbHandler.getGameDbRelation(queryExpress);
        return gameDBRelation;
    }

    @Override
    public GameBrand createGameBrand(GameBrand gameBrand) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("GameResourceLogic createGameBrand.gameBrand:" + gameBrand.toJson());
        }
        return gameResourceRedis.putGameBrandList(gameBrand);
    }

    @Override
    public List<GameBrand> queryGameBrand() throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("GameResourceLogic queryGameBrand");
        }
        return gameResourceRedis.getGameBrandList();
    }

    @Override
    public void modifyGameBrand(Integer brandId, GameBrand gameBrand) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("GameResourceLogic modifyGameBrand.brandId:" + brandId);
        }
        gameResourceRedis.setGameBrand(brandId, gameBrand);
    }

    @Override
    public void putGameCollectionListCache(String lineCode, int displayOrder, GameDB gameDB) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("GameResourceLogic putGameCollectionListCache.lineCode:" + lineCode + ",gameDB:" + gameDB.getGameDbId());
        }
        gameResourceRedis.putGameCollectionListCache(lineCode, displayOrder, gameDB);
    }

    @Override
    public void incrGameCollectionListCache(String lineCode, int incScore, GameDB gameDB) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("GameResourceLogic incrGameCollectionListCache.lineCode:" + lineCode + ",gameDB:" + gameDB.getGameDbId());
        }
        if(lineCode.equals("collection_game_hot_major")){
            //这里千万不要清缓存，因为每次访问页面都会走这里
            BasicDBObject queryDBObject = new BasicDBObject();
            queryDBObject.put(GameDBField.ID.getColumn(), gameDB.getGameDbId());
            BasicDBObject updateDBObject = new BasicDBObject();
            updateDBObject.append("$inc", new BasicDBObject().append(GameDBField.PVSUM.getColumn(), 1));
            writeAbleGameDbHandler.update(queryDBObject, updateDBObject);
        }
        gameResourceRedis.incrGameCollectionListCache(lineCode, incScore, gameDB);
    }

    @Override
    public boolean removeGameCollectionListCache(String lineCode) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("GameResourceLogic removeGameCollectionListCache.lineCode:" + lineCode);
        }
        return gameResourceRedis.removeGameCollectionListCache(lineCode);
    }

    @Override
    public Map<String, List<GameCollectionDTO>> getGameCollectionListCache(ClientLineType clientLineType, AppPlatform platform, Set<String> lineCodeSet, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("GameResourceLogic getGameCollectionListCache.clientLineType:" + clientLineType + ",platform:" + platform.getCode() + ",lineCodeSet:" + lineCodeSet);
        }
        Map<String, List<GameCollectionDTO>> map = new HashMap<String, List<GameCollectionDTO>>();
        if (!CollectionUtil.isEmpty(lineCodeSet)) {
            for (String lineCode : lineCodeSet) {
                if (StringUtil.isEmpty(lineCode)) {
                    continue;
                }
                ClientLine clientLine = ClientLine.parse(joymeAppRedis.getClientLine(lineCode));
                if (clientLine != null) {
                    List<GameCollectionDTO> gameList = gameResourceRedis.getGameCollectionListCache(lineCode, this, page);
                    if (ClientItemType.GAME_HOT_CATEGORY.equals(clientLine.getItemType())) {
                        map.put("category_" + clientLine.getLine_desc(), gameList);
                    } else {
                        map.put(String.valueOf(clientLine.getItemType().getCode()), gameList);
                    }
                }
            }
        } else {
            Set<String> codeSet = joymeAppRedis.getClientLineCodeList(clientLineType, platform.getCode());
            if (!CollectionUtil.isEmpty(codeSet)) {
                for (String lineCode : codeSet) {
                    if (StringUtil.isEmpty(lineCode)) {
                        continue;
                    }
                    ClientLine clientLine = ClientLine.parse(joymeAppRedis.getClientLine(lineCode));
                    if (clientLine != null) {
                        List<GameCollectionDTO> gameList = gameResourceRedis.getGameCollectionListCache(lineCode, this, page);
                        if (ClientItemType.GAME_HOT_CATEGORY.equals(clientLine.getItemType())) {
                            map.put("category_" + clientLine.getLine_desc(), gameList);
                        } else {
                            map.put(String.valueOf(clientLine.getItemType().getCode()), gameList);
                        }
                    }
                }
            }
        }
        return map;
    }

    @Override
    public GameDB getGameDBByAnotherName(String anotherName) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("GameResourceLogic getGameDBByAnotherName.anotherName:" + anotherName);
        }
        GameDB gameDB = groupPrivilegeCache.getGameDBByAnotherName(anotherName);
        if (gameDB == null) {
            gameDB = readonlyGameDbHandler.getHandler().get(new BasicDBObject("anothername", anotherName));
            if (gameDB != null) {
                groupPrivilegeCache.putGameDBByAnotherName(anotherName, gameDB);
            }
        }
        return gameDB;
    }

    @Override
    public PageRows<GameArchivesDTO> queryGameArchivesByCache(long gameDbId, ArchiveRelationType archiveRelationType, ArchiveContentType archiveContentType, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("GameResourceLogic queryGameArchivesByCache.gameDbId:" + gameDbId + ",archiveRelationType:" + archiveRelationType.getCode() + ",archiveContentType:" + archiveContentType.getCode());
        }
        return gameResourceRedis.getGameArchivesByCache(gameDbId, archiveRelationType.getCode(), archiveContentType.getCode(), pagination);
    }

    @Override
    public void putGameArchivesByCache(Long gameId, ArchiveRelationType archiveRelationType, ArchiveContentType archiveContentType, TagDedearchives tagDedearchives) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("GameResourceLogic putGameArchivesByCache.gameDbId:" + gameId + ",archiveRelationType:" + archiveRelationType.getCode() + ",archiveContentType:" + archiveContentType.getCode() + ",tagDedearchives:" + tagDedearchives.getDede_archives_id());
        }
        BasicDBObject queryDBObject = new BasicDBObject();
        queryDBObject.put(GameDBField.ID.getColumn(), gameId);
        BasicDBObject updateDBObject = new BasicDBObject();
        if(archiveContentType.equals(ArchiveContentType.GUIDE_ARCHIVE)){
            updateDBObject.append("$inc", new BasicDBObject().append(GameDBField.GUIDESUM.getColumn(), 1));
        }else if(archiveContentType.equals(ArchiveContentType.NEWS_ARCHIVE)){
            updateDBObject.append("$inc", new BasicDBObject().append(GameDBField.NEWSSUM.getColumn(), 1));
        }else if(archiveContentType.equals(ArchiveContentType.VIDEO_ARCHIVE)){
            updateDBObject.append("$inc", new BasicDBObject().append(GameDBField.VIDEOSUM.getColumn(), 1));
        }
        updateGameDB(queryDBObject, updateDBObject);
        gameResourceRedis.putGameArchivesByCache(gameId, archiveRelationType.getCode(), archiveContentType.getCode(), tagDedearchives);
    }

    @Override
    public boolean removeGameArchivesByCache(Long gameId, ArchiveRelationType archiveRelationType, ArchiveContentType archiveContentType, int archiveId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("GameResourceLogic removeGameArchivesByCache.gameDbId:" + gameId + ",archiveRelationType:" + archiveRelationType.getCode() + ",archiveContentType:" + archiveContentType.getCode() + ",tagDedearchives:" + archiveId);
        }
        boolean bool = gameResourceRedis.removeGameArchivesByCache(gameId, archiveRelationType.getCode(), archiveContentType.getCode(), archiveId);
        if(bool){
            BasicDBObject queryDBObject = new BasicDBObject();
            queryDBObject.put(GameDBField.ID.getColumn(), gameId);
            BasicDBObject updateDBObject = new BasicDBObject();
            if(archiveContentType.equals(ArchiveContentType.GUIDE_ARCHIVE)){
                updateDBObject.append("$inc", new BasicDBObject().append(GameDBField.GUIDESUM.getColumn(), -1));
            }else if(archiveContentType.equals(ArchiveContentType.NEWS_ARCHIVE)){
                updateDBObject.append("$inc", new BasicDBObject().append(GameDBField.NEWSSUM.getColumn(), -1));
            }else if(archiveContentType.equals(ArchiveContentType.VIDEO_ARCHIVE)){
                updateDBObject.append("$inc", new BasicDBObject().append(GameDBField.VIDEOSUM.getColumn(), -1));
            }
            updateGameDB(queryDBObject, updateDBObject);
        }
        return bool;
    }

    @Override
    public int getUserLikeGame(long uid, long gameId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("GameResourceLogic getUserLikeGame.uid:" + uid + ",gameId:" + gameId);
        }
        return gameResourceRedis.getUserLikeGame(uid, gameId);
    }

    @Override
    public boolean incUserLikeGame(long uid, long gameId, String anotherName, String column) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("GameResourceLogic incUserLikeGame.uid:" + uid + ",gameId:" + gameId);
        }
        BasicDBObject queryDBObject = new BasicDBObject();
        queryDBObject.put(GameDBField.ID.getColumn(), gameId);

        BasicDBObject updateDBObject = new BasicDBObject();
        updateDBObject.append("$inc", new BasicDBObject().append(column, 1));
        boolean bool = writeAbleGameDbHandler.update(queryDBObject, updateDBObject);
        if(bool){
            groupPrivilegeCache.removeGameDBCache(gameId);
            gameResourceRedis.removeGameCollectionDTO(gameId);
            groupPrivilegeCache.removeGameDBByAnotherName(anotherName);
            //gameResourceRedis.incUserLikeGame(uid, gameId);
        }
        return bool;
    }

    @Override
    public GameOrdered getGameOrdered(BasicDBObject basicDBObject) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("GameResourceLogic getGameOrdered:" + basicDBObject.toString());
        }
        return readonlyGameDbHandler.getHandler().getGameOrdered(basicDBObject);
    }

    @Override
    public GameOrdered createGameOrdered(GameOrdered gameOrdered) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("GameResourceLogic createGameOrdered:" + gameOrdered.toString());
        }
        return writeAbleGameDbHandler.insertGameOrdered(gameOrdered);
    }

    /**
     * 游戏分类信息存入map中
     * @param gameDB
     * @param isNew
     */
    public void addGameFilterMap(GameDB gameDB){
    	Map<String, String> map=getGameFilterMap(gameDB);
    	if (null!= map) {
    		//按筛选条件，存入对应的包含的其他条件数据
        	for (String redisKey : map.keySet()) {
        		String prefix=redisKey.substring(0, 2);
        		
    			Map<String, String> filterMap=gameResourceRedis.getGameFilterGroup(redisKey);
    			if (null==filterMap) {
    				Map<String, String> newDateMap=new HashMap<String, String>();
    				Set<String> removeKeys=new HashSet<String>();
    				//删除同级数据
    				for (String key:map.keySet()) {
    					if (key.startsWith(prefix)) {
    						removeKeys.add(key);
    					}
    				}
    				newDateMap.putAll(map);
    				for (String key : removeKeys) {
    					newDateMap.remove(key);
    				}
    				gameResourceRedis.addGameFilterGroup(redisKey, newDateMap);
    			}else {
    				for (String key:map.keySet()) {
    					if (!key.startsWith(prefix)) {
    						if (filterMap.containsKey(key)) {
        						int value=null==filterMap.get(key)?0:Integer.valueOf(filterMap.get(key));
        						filterMap.put(key, String.valueOf(value+1));			
    						}else {
    							filterMap.put(key, "1");
    						}	
    					}
    				}
    				gameResourceRedis.addGameFilterGroup(redisKey, filterMap);
    			}
    		}
		}
    }

    /**
     * 更新缓存中包含某一游戏类型信息的map数据
     * @param gameDB
     */
	public void deleteGameFilterMap(GameDB gameDB) {
		Map<String, String> map = getGameFilterMap(gameDB);
		if (null != map) {
			for (String redisKey : map.keySet()) {
				Map<String, String> filterMap = gameResourceRedis.getGameFilterGroup(redisKey);
				if (null == filterMap) {
					continue;
				} else {
					for (String key : map.keySet()) {
						if (!key.equals(redisKey)) {
							if (filterMap.containsKey(key)) {
								int value = null == filterMap.get(key) ? 0 : Integer.valueOf(filterMap.get(key));
								if (value <= 1) {
									gameResourceRedis.delGameFilterGroup(redisKey, key);
								}else {
									gameResourceRedis.updateGameFilterGroup(redisKey, key, String.valueOf(value-1));
								}
							} else {
								continue;
							}
						}
					}
				}
			}
		}
	}
    
	/**
	 * 拆分出gameDB中游戏分类信息
	 * @param gameDB
	 * @return
	 */
    private Map<String, String> getGameFilterMap(GameDB gameDB){
    	if (null == gameDB) {
			return null;
		}
    	Set<GameCategoryType> gameCategoryTypes=gameDB.getCategoryTypeSet();
    	GameNetType gameNetType=gameDB.getGameNetType();
    	Set<GameLanguageType> gameLanguageTypes=gameDB.getLanguageTypeSet();
    	Map<String, Set<GamePlatform>> gamePlatforMap=gameDB.getPlatformMap();
    	Set<GamePlatformType> gamePlatformTypes=gameDB.getPlatformTypeSet();
    	Set<GameThemeType> gameThemeTypes=gameDB.getThemeTypeSet();
    	//拆分出所有的筛选条件存入map中,初始状态置为1
    	Map<String, String> map=new HashMap<String, String>();
    	for (GameCategoryType gameCategoryType : gameCategoryTypes) {
			map.put(GameFilterGroupType.CATEGORY_TYPE.getType()+"_"+gameCategoryType.getCode(), "1");
		}
    	if (null!=gameNetType) {
    		map.put(GameFilterGroupType.NET_TYPE.getType()+"_"+gameNetType.getCode(), "1");
		}
    	for (GameLanguageType gameLanguageType : gameLanguageTypes) {
    		if (null!=gameLanguageType) {
    			map.put(GameFilterGroupType.LANGUAGE_TYPE.getType()+"_"+gameLanguageType.getCode(), "1");
			}
		}
    	for (String platformKey : gamePlatforMap.keySet()) {
			Set<GamePlatform> gamePlatforms=gamePlatforMap.get(platformKey);
			for (GamePlatform gamePlatform : gamePlatforms) {
				if (null!=gamePlatform) {
					map.put(GameFilterGroupType.PLATFORM_DEVICE.getType()+"_"+platformKey+"_"+gamePlatform.getCode(), "1");
				}
			}
		}
    	for (GamePlatformType gamePlatformType : gamePlatformTypes) {
			map.put(GameFilterGroupType.PLATFORM_TYPE.getType()+"_"+gamePlatformType.getCode(), "1");
		}
    	for (GameThemeType gameThemeType : gameThemeTypes) {
			map.put(GameFilterGroupType.THEME_TYPE.getType()+"_"+gameThemeType.getCode(), "1");
		}
    	return map;
    }
    
   
	@Override
	public Map<String, String> getGameFilterGroup(List<String> keys) throws ServiceException {
		if (CollectionUtil.isEmpty(keys)) {
			return null;
		}
		Map<String, String> mergeMap=null;
		for (String key : keys) {
			String prefix=key.substring(0, 2);
			Map<String, String> map = gameResourceRedis.getGameFilterGroup(key);
			if (null==mergeMap) {
				mergeMap=map;
			}else {
				mergeMap=mergeMap(map, mergeMap, prefix);
			}
		}
		return mergeMap;

	}
	
	private Map<String, String> mergeMap(Map<String, String> originMap,Map<String, String> mergeMap,String prefix){
		Map<String, String> newMergeMap=new HashMap<String, String>();
		for (String mergeKey : originMap.keySet()) {
			if (mergeMap.containsKey(mergeKey) && !mergeKey.startsWith(prefix)) {
				newMergeMap.put(mergeKey, originMap.get(mergeKey));
			}		
		}
		return newMergeMap;
	}

	@Override
	public void addGameFilterGroup(List<GameDB> games) throws ServiceException {
        if (CollectionUtil.isEmpty(games)) {
			return;
		}else {
			for (GameDB gameDB : games) {
				addGameFilterMap(gameDB);
			}
		} 
	}

}
