package com.enjoyf.platform.serv.stats;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.handler.HandlerPool;
import com.enjoyf.platform.db.stats.StatHandler;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.stats.*;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.collection.FQueueQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * The UserLogic class holds the core logic for the server. This class is
 * expected to change almost completely from server to server. <p/> <p/>
 * UserLogic is called by ExamplePacketDecoder.
 */
public class StatLogic implements StatService {
    //
    private static final Logger logger = LoggerFactory.getLogger(StatLogic.class);

    private StatConfig config;

    //
    private QueueThreadN statItemProcessQueueThreadN = null;
    private StatHandler statHandler;
    private HandlerPool<StatHandler> readonlyHandlersPool;


    StatLogic(StatConfig cfg) {
        config = cfg;

        try {
            statHandler = new StatHandler(config.getWriteableDataSourceName(), config.getProps());
            readonlyHandlersPool = new HandlerPool<StatHandler>();
            for (String dsn : config.getReadonlyDataSourceNames()) {
                readonlyHandlersPool.add(new StatHandler(dsn, config.getProps()));
            }
        } catch (DbException e) {
            GAlerter.lab("There isn't database connection pool in the configure." + this.getClass());

            // sleep 5 seconds for the system to send out the alert.
            Utility.sleep(5000);
            System.exit(0);
        }

        //init queue thread
        statItemProcessQueueThreadN = new QueueThreadN(
                config.getStatItemProcessQueueThreadNum(),
                new QueueListener() {
                    public void process(Object obj) {
                        if (obj instanceof StatItem) {

                            processQueuedStatItem((StatItem) obj);
                        } else {
                            GAlerter.lab("In statItemProcessQueueThreadN, there is a unknown obj.");
                        }
                    }
                },
                new FQueueQueue(config.getQueueDiskStorePath(), "statItemProcessQueue")
        );


    }


    private void processQueuedStatItem(StatItem item) {
        if (logger.isDebugEnabled()) {
            logger.debug("Call handler to saveItem:" + item);
        }

        if (item.getReportDate() == null) {
            item.setReportDate(new Date());
        }

        try {
            statHandler.saveItem(item);
        } catch (DbException e) {
            GAlerter.lab("StatLogic processQueuedStatItem error, exception:", e);
        }
    }

    // report the stat result item, it's reported by other buz server.
    public boolean reportStat(StatItem item) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Put the item to statItemProcessQueueThreadN, item:" + item);
        }

        item.setStatDate(item.getDateType().getStartDateByType(item.getStatDate()));
        statItemProcessQueueThreadN.add(item);
        return true;
    }


    public boolean reportStatBatch(List<StatItem> items) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call statHandler to write StatItem:" + items.size());
        }

        for (StatItem item : items) {
            item.setStatDate(item.getDateType().getStartDateByType(item.getStatDate()));
            statItemProcessQueueThreadN.add(item);
        }

        return true;
    }

    @Override
    public StatItem getStatItem(StatDomain domain, StatSection section, StatDateType dateType, Date statDate) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call statHandler to getStatItem, domain:" + domain + ", section:" +
                    section + ", dateType:" + dateType + ", statDate:" + statDate);
        }
        return statHandler.getStatsItem(domain, section, dateType, statDate);
    }

    // query the statitme in time scope.
    public List<StatItem> queryStatItems(StatDomain domain, StatSection section, StatDateType dateType, Date from, Date to) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call statHandler to queryStatItemsByDomainPeriod, domain:" + domain + ", section:" +
                    section + ", dateType:" + dateType + ", from:" + from + ", to:" + to);
        }
        return statHandler.query(domain, section, dateType, from, to);
    }

    // query the selected domain's section items in a certain stat date.
    public Map<StatSection, StatItem> queryStatItemsByDomain(StatDomain domain, StatDateType dateType, Date statDate) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call statHandler to queryStatItemsByDomainPeriod, domain:" + domain + ", dateType:" +
                    dateType + ", statDate:" + statDate);
        }
        return statHandler.query(domain, dateType, statDate);
    }

    @Override
    public PageStatSectionItems queryPageStatSectionItemsByDomain(StatDomain domain, StatDateType dateType, Date statDate, Pagination p) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call statHandler to queryStatItemsByDomainPeriod, domain:" + domain + ", dateType:" +
                    dateType + ", statDate:" + statDate + ", p:" + p);
        }

        PageStatSectionItems returnValue = new PageStatSectionItems();

        returnValue.setStatItems(statHandler.query(domain, dateType, statDate, p));
        returnValue.setPage(p);

        return returnValue;
    }

    //query the selected domain's section items in a certain period stat date.
    public Map<Date, Map<StatSection, StatItem>> queryStatItemsByDomainPeriod(StatDomain domain, StatDateType dateType, Date startDate, Date endDate)
            throws ServiceException {
        Map<Date, Map<StatSection, StatItem>> returnValues = new LinkedHashMap<Date, Map<StatSection, StatItem>>();

        for (Date d : dateType.getStatDates(startDate, endDate)) {
            returnValues.put(d, queryStatItemsByDomain(domain, dateType, d));
        }

        return returnValues;
    }

    //recieve the player event
    public boolean receiveEvent(Event event) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug(" receiveEvent. event:" + event);
        }

        statItemProcessQueueThreadN.add(event);
        return true;
    }

    @Override
    public List<StatItem> queryStatItemsByQuery(String advertiseId, StatDomainDefault domain, StatSectionDefault section, StatDateType dateType, Date from, Date to) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("Call statHandler to queryStatItemsByDomainName,publishId:" + advertiseId);
        }
        return statHandler.query(advertiseId, domain, section, dateType, from, to);
    }


}
