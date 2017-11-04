package com.enjoyf.platform.serv.vote;


import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.db.vote.VoteHandler;
import com.enjoyf.platform.db.vote.VoteHandlerMongo;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.system.VoteEvent;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.vote.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryCriterions;
import com.enjoyf.platform.util.sql.mongodb.MongoQueryExpress;
import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-9-19
 * Time: 上午10:01
 * To change this template use File | Settings | File Templates.
 */
class VoteLogic implements VoteService {
    private static final Logger logger = LoggerFactory.getLogger(VoteLogic.class);

    private VoteConfig voteConfig;

    private VoteHandler writeHandler;

    private HandlerPool<VoteHandler> readonlyHandlersPool;

    private QueueThreadN eventProcessorQueue;

    private VoteHandlerMongo writeHandlerMongo;
    private HandlerPool<VoteHandlerMongo> readonlyHandlersPoolMongo;

    // the vote cate(ehcache)
    private VoteCache voteCache;

    private VoteRedis voteRedis;


    VoteLogic(VoteConfig voteConfig) {
        this.voteConfig = voteConfig;
        try {
            writeHandler = new VoteHandler(this.voteConfig.getWriteableDataSourceName(), this.voteConfig.getProps());

            readonlyHandlersPool = new HandlerPool<VoteHandler>();
            for (String readDsn : this.voteConfig.getReadonlyDataSourceNames()) {
                readonlyHandlersPool.add(new VoteHandler(readDsn, this.voteConfig.getProps()));
            }

            writeHandlerMongo = new VoteHandlerMongo(this.voteConfig.getMongoDbWriteAbleDateSourceName(), this.voteConfig.getProps());
            readonlyHandlersPoolMongo = new HandlerPool<VoteHandlerMongo>();
            for (String dsn : this.voteConfig.getMongoDbReadonlyDataSourceNames()) {
                readonlyHandlersPoolMongo.add(new VoteHandlerMongo(dsn, this.voteConfig.getProps()));
            }
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());

            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        voteCache = new VoteCache(voteConfig.getMemCacheConfig());
        voteRedis = new VoteRedis(voteConfig.getProps());


        eventProcessorQueue = new QueueThreadN(voteConfig.getEventQueueThreadNum(), new QueueListener() {
            @Override
            public void process(Object obj) {
                if (obj instanceof VoteEvent) {
                    //投票操作
                    processVoteEvent((VoteEvent) obj);
                } else {
                    GAlerter.lab("In eventProcessQueueThreadN, there is a unknown obj.");
                }
            }
        }, new FQueueQueue(voteConfig.getQueueDiskStorePath(), "eventProcessQueue"));
    }


    @Override
    public Vote postVote(String subjectId, Vote vote) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleHandler to post vote, vote is " + vote);
        }

        Vote returnValue = writeHandler.postVote(vote);

        if (returnValue != null) {
            voteCache.putVote(returnValue.getVoteSubject().getSubjectId(), returnValue);
        }

        return returnValue;
    }

    @Override
    public Vote getVote(String subjectId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Coll the readonlyHandlersPool to getVote, subjectId is " + subjectId);
        }

        // get from cache
        Vote returnValue = voteCache.getVote(subjectId);

        if (returnValue == null) {
            returnValue = writeHandler.getVote(subjectId);
            // put to cache
            voteCache.putVote(subjectId, returnValue);
        }

        return returnValue;
    }

    @Override
    public boolean partVote(String subjectId, String voteUno, String voteIp, VoteDomain voteDomain, VoteRecordSet recordSet) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyHandlersPool to partVote , subjectId is " + subjectId + ", voteUno is " + voteUno + ", recordSet:" + recordSet.toJsonStr());
        }

        //
        voteCache.removeVote(subjectId);

        return writeHandler.partVote(subjectId, voteUno, voteIp, voteDomain, recordSet);
    }

    @Override
    public VoteUserRecord getVoteUserRecord(String voteUno, String subjectId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyHandlersPool to getVoteUserRecord ,  voteUno is " + voteUno + ", subjectId is " + subjectId);
        }

        return readonlyHandlersPool.getHandler().getVoteUserRecord(voteUno, subjectId);
    }


    @Override
    public Map<String, VoteUserRecord> queryVoteUserRecordBySubjectId(String subjectId, Set<String> voteUnoSet) throws ServiceException {
        Map<String, VoteUserRecord> returnValue = new HashMap<String, VoteUserRecord>();

        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyHandlersPool to queryVoteUserRecordBySubjectId , subjectId is " + subjectId);
        }


        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(VoteSubjectRecordField.SUBJECTID, subjectId));

        QueryCriterions[] voteUnoCriterionsArray = new QueryCriterions[voteUnoSet.size()];
        int index = 0;
        for (String voteUno : voteUnoSet) {
            voteUnoCriterionsArray[index] = QueryCriterions.eq(VoteSubjectRecordField.VOTEUNO, voteUno);
            index++;
        }

        queryExpress.add(QueryCriterions.or(voteUnoCriterionsArray));

        queryExpress.add(QuerySort.add(VoteSubjectRecordField.SUBJECTRECORDID, QuerySortOrder.DESC));

        List<VoteSubjectRecord> subjectRecordList = readonlyHandlersPool.getHandler().queryVoteSubjectRecords(subjectId, queryExpress);

        for (VoteSubjectRecord subjectRecord : subjectRecordList) {
            VoteRecord voteRecord = new VoteRecord();
            voteRecord.setOptionId(subjectRecord.getVoteOption());
            voteRecord.setOptionValue(subjectRecord.getVoteOptionValue());

            if (returnValue.get(subjectRecord.getVoteUno()) != null) {
                returnValue.get(subjectRecord.getVoteUno()).getRecordSet().add(voteRecord);
            } else {
                VoteUserRecord voteUserRecord = new VoteUserRecord();
                voteUserRecord.setSubjectId(subjectId);
                voteUserRecord.setVoteUno(subjectRecord.getVoteUno());
                voteUserRecord.setVoteDate(subjectRecord.getVoteDate());

                VoteRecordSet voteRecordSet = new VoteRecordSet();
                voteRecordSet.add(voteRecord);
                voteUserRecord.setRecordSet(voteRecordSet);

                returnValue.put(subjectRecord.getVoteUno(), voteUserRecord);
            }
        }

        return returnValue;
    }

    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" receiveEvent.event: " + event);
        }

        eventProcessorQueue.add(event);
        return true;
    }

    @Override
    public WikiVote getWikiVote(String url, String pic, String name, String num, Long articleId, String keyWords, Integer voteSum) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " getWikiVote.url:" + url);
        }
        WikiVote wikiVote = voteCache.getWikiVote(articleId);
        if (wikiVote == null) {
            wikiVote = readonlyHandlersPoolMongo.getHandler().getWikiVote(new MongoQueryExpress().add(MongoQueryCriterions.eq(WikiVoteField.ARTICLE_ID, articleId)));
            if (wikiVote == null) {
                wikiVote = new WikiVote();
                wikiVote.setArticleId(articleId);
                wikiVote.setUrl(url);
                wikiVote.setPic(pic);
                wikiVote.setName(name);
                wikiVote.setNoStr(num);
                wikiVote.setKeyWords(keyWords);
                wikiVote.setVotesSum(voteSum);
                wikiVote.setCreateDate(new Date());

                wikiVote = writeHandlerMongo.createWikiVote(wikiVote);
            }

            if (wikiVote != null) {
                voteCache.putWikiVote(articleId, wikiVote);
            }
        }
        return wikiVote;
    }

    @Override
    public boolean incWikiVote(String url, Long articleId, BasicDBObject query, BasicDBObject update) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " incWikiVote.url:" + url);
        }
        boolean bool = writeHandlerMongo.updateWikiVote(query, update);
        if (bool) {
            voteCache.removeWikiVote(articleId);
        }
        return bool;
    }

    @Override
    public List<WikiVote> queryWikiVotes(Set<Long> idSet) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryWikiVotes.idSet:" + idSet.toArray().toString());
        }
        List<WikiVote> voteList = new ArrayList<WikiVote>();
        for (Long id : idSet) {
            if (id > 0l) {
                WikiVote wikiVote = voteCache.getWikiVote(id);
                if (wikiVote == null) {
                    wikiVote = readonlyHandlersPoolMongo.getHandler().getWikiVote(new MongoQueryExpress().add(MongoQueryCriterions.eq(WikiVoteField.ARTICLE_ID, id)));
                }
                if (wikiVote != null) {
                    voteCache.putWikiVote(id, wikiVote);
                    voteList.add(wikiVote);
                }
            }
        }
        return voteList;
    }

    @Override
    public List<WikiVote> queryWikiVoteByPage(MongoQueryExpress mongoQueryExpress, Pagination pagination) throws DbException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " queryWikiVoteByPage.mongoQueryExpress:" + mongoQueryExpress);
        }
        return readonlyHandlersPoolMongo.getHandler().queryWikiVoteByPage(mongoQueryExpress, pagination);
    }

    /**
     * 查询投票数量
     *
     * @param keySets keySe
     * @return
     * @throws ServiceException
     */
    @Override
    public Map<String, Integer> queryVoteNum(Set<String> keySets) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " .keySets:" + keySets.toArray());
        }
        Map<String, Integer> map = new HashMap<String, Integer>();
        if (CollectionUtil.isEmpty(keySets)) {
            return map;
        }
        for (String key : keySets) {
            int value = voteRedis.getVoteTitle(key);
            map.put(key, value);
        }
        return map;
    }

    @Override
    public boolean getVoteHistory(String profileId, String key) throws ServiceException {
        return voteRedis.getVoteHistory(profileId, key);
    }

    @Override
    public VoteUserRecord insertVoteUserRecord(VoteUserRecord voteUserRecord) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " .insertVoteUserRecord:" + voteUserRecord);
        }
        return writeHandler.insertVoteUserRecord(voteUserRecord);
    }

    //处理事件
    private void processQueuedEvent(Event event) {
        //
    }

    //投票操作
    private void processVoteEvent(VoteEvent voteEvent) {
        String key = voteEvent.getKey();
        voteRedis.incrVote(key);
    }

}
