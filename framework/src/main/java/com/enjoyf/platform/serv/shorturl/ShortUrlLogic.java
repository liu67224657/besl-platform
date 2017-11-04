/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.shorturl;


import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.db.shorturl.ShortUrlHandler;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.system.ShortUrlSumIncreaseEvent;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.shorturl.ShortUrl;
import com.enjoyf.platform.service.shorturl.ShortUrlService;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueList;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
class ShortUrlLogic implements ShortUrlService {
    //
    private static final Logger logger = LoggerFactory.getLogger(ShortUrlLogic.class);

    //
    private ShortUrlConfig config;

    //the handler's
    private ShortUrlHandler writeAbleShortUrlHandler;
    private HandlerPool<ShortUrlHandler> readonlyShortUrlHandlersPool;

    //the event queue thread n.
    private QueueThreadN eventProcessQueueThreadN = null;

    //
    private ShortUrlCache shortUrlCache;

    ShortUrlLogic(ShortUrlConfig cfg) {
        config = cfg;

        //initialize the handler.
        try {
            //
            writeAbleShortUrlHandler = new ShortUrlHandler(config.getWriteableDataSourceName(), config.getProps());

            //
            readonlyShortUrlHandlersPool = new HandlerPool<ShortUrlHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyShortUrlHandlersPool.add(new ShortUrlHandler(dsn, config.getProps()));
            }
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());

            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        //the queue thread n initialize.
        eventProcessQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                if (obj instanceof Event) {
                    processQueuedEvent((Event) obj);
                } else {
                    GAlerter.lab("In eventProcessQueueThreadN, there is a unknown obj.");
                }
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "eventProcessQueue"));

        //initialize the cache setting.
        shortUrlCache = new ShortUrlCache(config.getMemDiskCacheConfig());
    }

    private void processQueuedEvent(Event event) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to processQueuedEvent:" + event);
        }

        //check the event type.
        if (event instanceof ShortUrlSumIncreaseEvent) {
            ShortUrlSumIncreaseEvent castEvent = (ShortUrlSumIncreaseEvent) event;

            //
            Map<ObjectField, Object> keyValues = new HashMap<ObjectField, Object>();
            keyValues.put(castEvent.getField(), castEvent.getCount());

            try {
                //
                writeAbleShortUrlHandler.updateShortUrl(castEvent.getUrlKey(), keyValues);
            } catch (Exception e) {
                //
                GAlerter.lab("ShortUrlLogic processQueuedEvent error.", e);
            }
        } else {
            logger.info("ShortUrlLogic discard the unknown event:" + event);
        }
    }

    @Override
    public ShortUrl generateUrl(String url, String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleShortUrlHandler to generateUrl, url is " + url);
        }

        //write the short url into the db.
        return writeAbleShortUrlHandler.generateUrl(parseUrl(uno, url));
    }

    @Override
    public Map<String, ShortUrl> generateUrls(Set<String> urls, String uno) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleShortUrlHandler to generateUrls, urls is " + urls);
        }

        //write the content into the db.
        Map<String, ShortUrl> returnValue = null;

        //parse the url to shorturl object
        List<ShortUrl> l = new ArrayList<ShortUrl>();
        for (String url : urls) {
            //
            l.add(parseUrl(uno, url));
        }

        //write to db.
        returnValue = writeAbleShortUrlHandler.generateUrls(l);

        return returnValue;
    }

    private ShortUrl parseUrl(String uno, String url) {
        ShortUrl shortUrl = new ShortUrl();

        //
        shortUrl.setInitUno(uno);
        shortUrl.setUrl(url);

        //todo, parse the url file type and protocol type.

        return shortUrl;
    }

    @Override
    public ShortUrl getUrl(String key) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyShortUrlHandlersPool to getUrl, key is " + key);
        }

        //get from cache
        ShortUrl returnValue = shortUrlCache.getShortUrl(key);

        //if not exist in cahce
        if (returnValue == null) {
            //get from db
            returnValue = readonlyShortUrlHandlersPool.getHandler().getUrl(key);

            //exist in db, then add to cache,
            //maybe the urlkey is not exist in db(just return null).
            if (returnValue != null) {
                shortUrlCache.putShortUrl(returnValue.getUrlKey(), returnValue);
            }
        }

        //
        return returnValue;
    }

    @Override
    public Map<String, ShortUrl> queryUrls(Set<String> keys) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the readonlyShortUrlHandlersPool to queryUrls, keys is " + keys);
        }

        Map<String, ShortUrl> returnValue = new HashMap<String, ShortUrl>();

        //遍历keys，如果在cache中有，就直接从cache中获取，并把key从key中移除，减少数据库查询的数据。
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext(); ) {
            ShortUrl url = shortUrlCache.getShortUrl(iterator.next());

            if (url != null) {
                returnValue.put(url.getUrlKey(), url);

                iterator.remove();
            }
        }

        //cache中没有的，从数据库中获取，并放到cache中。
        if (keys.size() > 0) {
            Map<String, ShortUrl> newQuery = readonlyShortUrlHandlersPool.getHandler().queryUrls(keys);

            returnValue.putAll(newQuery);

            for (ShortUrl url : newQuery.values()) {
                shortUrlCache.putShortUrl(url.getUrlKey(), url);
            }
        }

        //
        return returnValue;
    }

    @Override
    public boolean updateShortUrl(String key, Map<ObjectField, Object> keyValues) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the writeAbleShortUrlHandler to updateShortUrl, key is " + key);
        }

        return writeAbleShortUrlHandler.updateShortUrl(key, keyValues);
    }

    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Put the event to eventProcessQueueThreadN, event is " + event);
        }

        eventProcessQueueThreadN.add(event);

        return true;
    }
}
