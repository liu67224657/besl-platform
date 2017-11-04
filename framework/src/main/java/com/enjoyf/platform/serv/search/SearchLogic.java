/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.search;


import com.enjoyf.platform.serv.search.lucene.GiftIndexer;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.ActivityField;
import com.enjoyf.platform.service.content.ActivityType;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.point.ActivityGoods;
import com.enjoyf.platform.service.point.ActivityGoodsField;
import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.search.SearchGiftCriteria;
import com.enjoyf.platform.service.search.SearchGiftIndexEntry;
import com.enjoyf.platform.service.search.SearchGiftResultEntry;
import com.enjoyf.platform.service.search.SearchService;
import com.enjoyf.platform.service.search.redis.RedisServer;
import com.enjoyf.platform.service.search.solr.ProfileSolrServer;
import com.enjoyf.platform.service.search.solr.SolrServer;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import org.apache.lucene.queryParser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>,zx
 */

/**
 * The UserPropsLogic class holds the core logic for the server.
 * This class is expected to change almost completely from
 * server to server. <p>
 * <p/>
 * UserPropsLogic is called by UserPropsPacketDecoder.
 */
class SearchLogic implements SearchService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private SearchConfig config;

    private GiftIndexer giftIndexer;



    private SolrServer solrServer;
    private ProfileSolrServer profileSolrServer;
    private RedisServer redisServer;

    private QueueThreadN eventProcessQueueThreadN = null;

    SearchLogic(SearchConfig cfg) {
        config = cfg;

        //todo
        giftIndexer = new GiftIndexer(cfg.getGiftIndexDir());

        // todo
        solrServer = new SolrServer(config.getSolrServerUri(), config.getSolrAnalysisUri());
        profileSolrServer = new ProfileSolrServer(config.getSolrServerUri(), config.getSolrAnalysisUri());

        redisServer = new RedisServer(config.getRedisServerHost(), config.getRedisServerPort());

        eventProcessQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {

            public void process(Object obj) {
                if (obj instanceof Event) {
                    processQueuedEvent((Event) obj);
                } else {
                    GAlerter.lab(this.getClass().getName() + " eventProcessQueueThreadN, there is a unknown obj." + obj);
                }
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "eventProcessQueue"));

//        Timer indexTimer = new Timer();
//        indexTimer.scheduleAtFixedRate(new ContentIndexTask(), 15 * 1000, config.getTaskIndexStep());
//
//        Timer blogIndexTimer = new Timer();
//        blogIndexTimer.scheduleAtFixedRate(new ProfileIndexTask(), 15 * 1000, config.getTaskIndexStep());
//
//        Timer groupIndexTimer = new Timer();
//        groupIndexTimer.scheduleAtFixedRate(new GroupIndexTask(), 15 * 1000, config.getTaskIndexStep());
//
//        Timer gameIndexTimer = new Timer();
//        gameIndexTimer.scheduleAtFixedRate(new GameIndexTask(), 15 * 1000, config.getTaskIndexStep());

//        Timer gameDbIndexTimer = new Timer();
//        gameDbIndexTimer.scheduleAtFixedRate(new GameDbIndexTask(), 15 * 1000, config.getTaskIndexStep());

        Timer giftIndexTimer = new Timer();
        giftIndexTimer.scheduleAtFixedRate(new GiftIndexTask(), 15 * 1000, config.getTaskIndexStep());
        //
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
    }

    /**
     * 搜索礼包
     *
     * @param criteria
     * @param page
     * @return
     * @throws ServiceException
     */
    @Override
    public PageRows<SearchGiftResultEntry> searchGiftByText(SearchGiftCriteria criteria, Pagination page) throws ServiceException {
        PageRows<SearchGiftResultEntry> rePageRows = new PageRows<SearchGiftResultEntry>();
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlySearchHandlersPool to SearchGiftCriteria, criteria is " + criteria);
        }

        try {
            List<Map<String, String>> queryList = new ArrayList<Map<String, String>>();
            for (String key : criteria.getKeys()) {
                Map<String, String> map = new HashMap<String, String>();
                map.put(SearchGiftIndexEntry.IDX_KEY_GTNAME, key);
                queryList.add(map);
            }

            rePageRows = giftIndexer.search(queryList, page);

        } catch (ParseException e) {
            logger.error("searchBlogsByText error.", e);
        } catch (IOException e) {
            logger.error("searchBlogsByText error.", e);
        }
        return rePageRows;
    }



    class GiftIndexTask extends TimerTask {
        public void run() {
            if (logger.isDebugEnabled()) {
                logger.debug("GiftIndexTask start to reload.");
            }
            try {
                createGiftIndex();
            } catch (IOException e) {
                GAlerter.lab("There is createGiftIndex create error." + this.getClass(), e);
            } catch (ServiceException e) {
                GAlerter.lab("GiftIndexTask  error." + this.getClass(), e);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("GiftIndexTask finish to reload.");
            }
        }
    }

    private void createGiftIndex() throws IOException, ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("create the giftinfo index");
        }
        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.eq(ActivityGoodsField.ACTIVITY_TYPE, ActivityType.EXCHANGE_GOODS.getCode()))
                .add(QueryCriterions.eq(ActivityGoodsField.REMOVE_STATUS, ActStatus.ACTED.getCode()))
                .add(QuerySort.add(ActivityField.DISPLAY_ORDER, QuerySortOrder.ASC));
        List<ActivityGoods> listActivity = PointServiceSngl.get().listActivityGoods(queryExpress);

        List<SearchGiftIndexEntry> indexEntryList = new ArrayList<SearchGiftIndexEntry>();
        for (ActivityGoods activity : listActivity) {

            SearchGiftIndexEntry entry = new SearchGiftIndexEntry();
            entry.setaId(String.valueOf(activity.getActivityGoodsId()));
            entry.setName(activity.getActivitySubject());
            indexEntryList.add(entry);
        }
        giftIndexer.index(indexEntryList);


    }

}
