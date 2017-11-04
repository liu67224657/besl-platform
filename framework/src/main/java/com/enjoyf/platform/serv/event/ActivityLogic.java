package com.enjoyf.platform.serv.event;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.event.ActivityHandler;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.service.event.Activity;
import com.enjoyf.platform.service.event.ActivityAwardLog;
import com.enjoyf.platform.service.event.ActivityField;
import com.enjoyf.platform.service.event.ActivityService;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: ericliu
 * Date: 14-8-5
 * Time: 下午5:24
 * To change this template use File | Settings | File Templates.
 */
public class ActivityLogic implements ActivityService {


    //
    private static final Logger logger = LoggerFactory.getLogger(EventLogic.class);

    //
    private EventConfig config;

    //
    private QueueThreadN eventProcessQueueThreadN = null;

    private ActivityHandler wriateAbleHandler = null;

    private HandlerPool<ActivityHandler> readonlyHandlersPool = null;

    private ActivityCache activityCache = null;

    ActivityLogic(EventConfig cfg) {
        config = cfg;

        // init queue thread
        eventProcessQueueThreadN = new QueueThreadN(config.getEventProcessQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {

            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "eventProcessQueue"));

        try {
            wriateAbleHandler = new ActivityHandler(config.getWriteableDataSourceName(), config.getProps());

            readonlyHandlersPool = new HandlerPool<ActivityHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyHandlersPool.add(new ActivityHandler(dsn, config.getProps()));
            }

        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());

            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        activityCache = new ActivityCache(cfg.getMemCachedConfig());

    }

    @Override
    public Activity crateActivity(Activity activity) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call write handler crateActivity:" + activity);
        }

        return wriateAbleHandler.insertActivity(activity);
    }

    @Override
    public boolean modifyActivity(UpdateExpress updateExpress, long activityId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call write handler modifyActivity.updateExpress" + updateExpress + " ,activityId:" + activityId);
        }

        boolean bVal = wriateAbleHandler.modifyActivity(updateExpress, new QueryExpress().add(QueryCriterions.eq(ActivityField.ACTIVITY_ID, activityId)));

        if (bVal) {
            activityCache.deleteActivity(activityId);
        }

        return bVal;
    }

    @Override
    public PageRows<Activity> queryByPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call write handler queryByPage.queryExpress: " + queryExpress + " ,pagination:" + pagination);
        }

        return readonlyHandlersPool.getHandler().queryActivity(queryExpress, pagination);
    }

    @Override
    public Activity getActivity(long activityId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call write handler queryByPage.activityId:" + activityId);
        }

        Activity activity = activityCache.getActivity(activityId);
        if (activity == null) {
            activity = readonlyHandlersPool.getHandler().getActivity(new QueryExpress().add(QueryCriterions.eq(ActivityField.ACTIVITY_ID, activityId)));
           if(activity!=null){
               activityCache.putActivity(activity);
           }
        }

        return activity;
    }

    @Override
    public ActivityAwardLog createAwardLog(ActivityAwardLog log) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call write handler createAwardLog.log:" + log);
        }

        log=wriateAbleHandler.insertAwardLog(log);

        modifyActivity(new UpdateExpress().increase(ActivityField.RESTAMOUNT,-1),
                log.getActivityId());

        return log;
    }

    @Override
    public List<ActivityAwardLog> queryAwardLog(QueryExpress queryExpress) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call write handler createAwardLog.queryExpress:" + queryExpress);
        }

        return wriateAbleHandler.queryAwardLog(queryExpress);
    }
}
