package com.enjoyf.platform.serv.billing;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.billing.BillingHandler;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.serv.billing.quatz.FailedDepositLogQuartzCronTrigger;
import com.enjoyf.platform.service.IntValidStatus;
import com.enjoyf.platform.service.billing.BillingService;
import com.enjoyf.platform.service.billing.DepositLog;
import com.enjoyf.platform.service.billing.DepositLogField;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.event.EventDispatchServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-1-10
 * Time: 下午3:54
 * To change this template use File | Settings | File Templates.
 */
public class BillingLogic implements BillingService {
    private static final Logger logger = LoggerFactory.getLogger(BillingLogic.class);

    //props
    private BillingConfig config;

    //the handler's
    private BillingHandler writeAbleHandler;
    private HandlerPool<BillingHandler> readonlyHandlersPool;

    //queue thread pool
    private QueueThreadN eventProcessQueueThreadN = null;

    private DepositLogCache cache;
    private BillingRedis billingRedis;

    BillingLogic(BillingConfig cfg) {
        config = cfg;

        //initialize the handler.
        try {
            //
            writeAbleHandler = new BillingHandler(config.getWriteableDataSourceName(), config.getProps());
            readonlyHandlersPool = new HandlerPool<BillingHandler>();

            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyHandlersPool.add(new BillingHandler(dsn, config.getProps()));
            }
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());

            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        //thread setting.
        eventProcessQueueThreadN = new QueueThreadN(config.getEventQueueThreadNum(), new QueueListener() {
            public void process(Object obj) {
                if (obj instanceof Event) {
                    //receive event process
                    processQueuedEvent((Event) obj);
                } else {
                    GAlerter.lab("In eventProcessQueueThreadN, there is a unknown obj.");
                }
            }
        }, new FQueueQueue(config.getQueueDiskStorePath(), "eventProcessorQueue"));

        cache = new DepositLogCache(config.getMemCachedConfig());

        billingRedis = new BillingRedis(config.getProps());

        //处理失败的充值log
        if (config.isFailedDepositLogTriggerIsOpen()) {
            try {
                FailedDepositLogQuartzCronTrigger cronTrigger = new FailedDepositLogQuartzCronTrigger(this, cfg);
                cronTrigger.init();
                cronTrigger.start();

                GAlerter.lab(this.getClass().getName() + " start process failed deposit log cron trigger");
            } catch (Exception e) {
                GAlerter.lab("FailedDepositLogQuartzCronTrigger start error.", e);
            }
        }
    }


    @Override
    public DepositLog createDepositLog(DepositLog log) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call hanlder createDepositLog.log: " + log);
        }

        return writeAbleHandler.insertDepositLog(log);
    }


    public boolean increaseSyncLogTimes(int increaseTimes, String errorMsg, String logid) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call hanlder increaseSyncLogTimes.increaseTimes:" + increaseTimes + " logid: " + logid);
        }
        UpdateExpress updateExpress = new UpdateExpress();
        updateExpress.increase(DepositLogField.SYNCTIMES, increaseTimes);
        updateExpress.set(DepositLogField.SYNC_STATUS, IntValidStatus.UNVALID.getCode());
        updateExpress.set(DepositLogField.ERRORMSG, errorMsg);
        return writeAbleHandler.modifyDepositLog(updateExpress, logid);
    }

    public String getFailedDepositLog() {
        if (logger.isDebugEnabled()) {
            logger.debug("call hanlder getFailedDepositLogs.");
        }
        return billingRedis.popFailedDepositLog();
    }

    public boolean pushFailedDepositLog(String logId) {
        if (logger.isDebugEnabled()) {
            logger.debug("call hanlder getFailedDepositLogs.logId:" + logId);
        }
        billingRedis.pushFailedDepositLog(logId);
        return true;
    }

    public boolean pushFailedDepositLog(String... logIds) {
        if (logger.isDebugEnabled()) {
            logger.debug("call hanlder getFailedDepositLogs.logIds:" + logIds);
        }
        billingRedis.pushFailedDepositLog(logIds);
        return true;
    }

    public long getFailedDepositLogLength() {
        if (logger.isDebugEnabled()) {
            logger.debug("call hanlder getFailedDepositLogLength.");
        }

        return billingRedis.getFailedDepositLogLength();
    }

    public DepositLog getDepositLog(String logId) throws DbException {
        if (logger.isDebugEnabled()) {
            logger.debug("call hanlder getDepositLog.logId:" + logId);
        }
        return writeAbleHandler.getDepositLog(logId);
    }


    @Override
    public boolean modifyDepositLog(IntValidStatus syncStatus, String errorMsg, String logId) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call hanlder createDepositLog.logId: " + logId + " syncStatus:" + syncStatus);
        }
        boolean result;
        try {
            if (cache.isLockLog(logId)) {
                GAlerter.lan(this.getClass().getName() + " modifyDepositLog log is lock: " + logId);
                return false;
            }

            cache.lockLog(logId);

            DepositLog depositLog = writeAbleHandler.getDepositLog(logId);
            if (depositLog == null) {
                GAlerter.lan(this.getClass().getName() + " depositLog not exists: " + logId);
                return false;
            }

            result = writeAbleHandler.modifyDepositLog(new UpdateExpress()
                    .set(DepositLogField.SYNC_STATUS, syncStatus.getCode())
                    .set(DepositLogField.ERRORMSG, errorMsg)
                    , logId);

            //log 同步成功
            if (result && IntValidStatus.VALID.equals(depositLog.getStatus()) && IntValidStatus.VALID.equals(syncStatus)) {
                result = writeAbleHandler.modifyBalance(depositLog.getAmount(), depositLog.getAppKey(), depositLog.getProfileId(), depositLog.getZoneKey());


            } else if (result && IntValidStatus.VALID.equals(depositLog.getStatus()) && IntValidStatus.UNVALID.equals(syncStatus)) {
                //
                pushFailedDepositLog(depositLog.getLogId());
            }
        } finally {
            cache.unlock(logId);
        }

        return result;
    }

    @Override
    public PageRows<DepositLog> queryDepositLogQueryExpressPage(QueryExpress queryExpress, Pagination pagination) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("call hanlder queryDepositLogList.queryExpress: " + queryExpress);
        }
        return readonlyHandlersPool.getHandler().queryDepositLogQueryExpress(queryExpress, pagination);
    }


    /**
     * 根据sql语句查数据
     *
     * @param sql
     * @return
     * @throws com.enjoyf.platform.service.service.ServiceException
     */
    @Override
    public String queryBySql(String sql) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call billingHandler queryBySql:sql" + sql);
        }
        return readonlyHandlersPool.getHandler().queryStatBySql(sql);
    }

    private void processQueuedEvent(Event event) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to processQueuedEvent:" + event);
        }


    }


    @Override
    public boolean receiveEvent(Event event) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("The event is recieved, event:" + event);
        }

        eventProcessQueueThreadN.add(event);

        return true;
    }

    @Override
    public boolean checkReceipt(String receipt) throws ServiceException {
        return billingRedis.checkReceipt(receipt);
    }

    @Override
    public boolean setReceipt(String receipt) throws ServiceException {
        billingRedis.setReceipt(receipt);
        return true;
    }

}
