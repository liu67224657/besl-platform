/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.serv.example;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.example.ExampleHandler;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.example.Example;
import com.enjoyf.platform.service.example.ExampleService;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.RangeRows;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class ExampleLogic implements ExampleService {
    //
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //
    private ExampleConfig config = null;

    //the handler's
    private ExampleHandler writeAbleHandler;
    private HandlerPool<ExampleHandler> readonlyHandlersPool;

    //
    private QueueThreadN eventProcessQueueThreadN = null;

    //
    private ExampleCache exampleCache;


    ExampleLogic(ExampleConfig cfg) {
        config = cfg;

        //initialize the handler.
        try {
            //
            readonlyHandlersPool = new HandlerPool<ExampleHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyHandlersPool.add(new ExampleHandler(dsn, config.getProps()));
            }
            writeAbleHandler = new ExampleHandler(config.getWriteableDataSourceName(), config.getProps());


        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());

            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
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
        exampleCache = new ExampleCache(config.getMemDiskCacheConfig());

        // init the quartz trigger.
        if (config.isOpenStatTrigger()) {
            try {
                ExampleQuartzCronTrigger cronTrigger = new ExampleQuartzCronTrigger(config, writeAbleHandler);

                cronTrigger.init();
                cronTrigger.start();
            } catch (Exception e) {
                GAlerter.lab("EventQuartzCronTrigger start error.", e);
            }
        }

        //start timer task to do something.
        Timer exampleTimer = new Timer();
        exampleTimer.scheduleAtFixedRate(new ExampleTimerTask(), 0, 1000l * config.getExampleTimerRunIntervalSecs());
    }

    private void processQueuedEvent(Event obj) {
        if (logger.isDebugEnabled()) {
            logger.debug("processQueuedEvent event :" + obj);
        }

        //todo
    }

    @Override
    public Example create(Example entry) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to insert, entry:" + entry);
        }

        return writeAbleHandler.insert(entry);
    }

    @Override
    public Example get(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to get, queryExpress:" + queryExpress);
        }

        return readonlyHandlersPool.getHandler().get(queryExpress);
    }

    @Override
    public PageRows<Example> query(QueryExpress queryExpress, Pagination page) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to query, queryExpress:" + queryExpress + ", page:" + page);
        }

        return readonlyHandlersPool.getHandler().query(queryExpress, page);
    }

    @Override
    public RangeRows<Example> query(QueryExpress queryExpress, Rangination range) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to query, queryExpress:" + queryExpress + ", range:" + range);
        }

        return readonlyHandlersPool.getHandler().query(queryExpress, range);
    }

    @Override
    public boolean modify(UpdateExpress updateExpress, QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call the handler to update, updateExpress:" + updateExpress + ", queryExpress:" + queryExpress);
        }

        return writeAbleHandler.update(updateExpress, queryExpress) > 0;
    }

    //
    class ExampleTimerTask extends TimerTask {
        //
        public void run() {
            logger.info("ExampleTimerTask starts task.");

            logger.info("ExampleTimerTask completed task.");
        }
    }
}
