package com.enjoyf.platform.serv.rate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.service.rate.Rate;
import com.enjoyf.platform.service.rate.RateLimit;
import com.enjoyf.platform.service.rate.RateService;
import com.enjoyf.platform.service.rate.key.RateKey;
import com.enjoyf.platform.service.service.ServiceException;

/**
 * @author Yin Pengyi
 */
class RateLogic implements RateService {
	
	private static final Logger logger = LoggerFactory.getLogger(RateLogic.class);
	
    private RateConfig config;
    private MasterTable masterTable;

    RateLogic(RateConfig cfg) {
        config = cfg;
        logger.info("RateCheckServer.config: " + config);
        masterTable = new MasterTable(config.getIntervalSecs());
    }


    public Rate getRate(RateKey key) throws ServiceException {
        return getRate(key.getDomain().getLimit(), key);
    }

    public Rate getRate(RateLimit limit, RateKey key) throws ServiceException {
        
    	Rate rate = new Rate();
    	Period period = new Period(limit.getDurationSecs());
        PeriodCount count = null;

        // if doesn't period table exist, create period table with new period
        if (masterTable.containsKey(period)) {
            PeriodTable periodTable = masterTable.get(period);

            // if key doesn't exist insert key with count of 1
            if (periodTable.containsKey(key)) {
                count = periodTable.get(key);

                // if count.getCount() is more than limit.getMaxAllowed(), rate is exceeded
                if (count.getCount().getCount() > limit.getMaxAllowed()) {
                    rate.setExceeded(true);
                }
            }
        }

        if (logger.isDebugEnabled()) {
        	logger.debug("Key=" + key + ", Limit=" + limit + ", Count=" + count + ", Rate=" + rate);
        }

        return rate;
    }

    public Rate recordCount(RateKey key) throws ServiceException {
        return recordCount(key.getDomain().getLimit(), key);
    }

    public Rate recordCount(RateLimit limit, RateKey key) throws ServiceException {
        Rate rate = new Rate();

        PeriodCount periodCount = null;

        //generate the period for the key in master table.
        Period period = new Period(limit.getDurationSecs());

        // if doesn't period table exist, create period table with new period
        if (!masterTable.containsKey(period)) {
            masterTable.put(period, new PeriodTable());
        }

        //the got the period table
        PeriodTable periodTable = masterTable.get(period);

        // if key doesn't exist insert key with count of 0
        if (!periodTable.containsKey(key)) {
            periodTable.put(key, new PeriodCount(period, new Count()));
        } else {
            periodCount = periodTable.get(key);
            if (periodCount.getPeriod().expired()) {
                periodTable.remove(key);
                periodTable.put(key, new PeriodCount(period, new Count()));
            }
        }

        periodCount = periodTable.get(key);
        periodCount.getCount().increment();

        // if count.getCount() is more than limit.getMaxAllowed(), rate is exceeded
        if (periodCount.getCount().getCount() > limit.getMaxAllowed()) {
            rate.setExceeded(true);
        }

        if (logger.isDebugEnabled()) {
        	logger.debug("Key=" + key + ", Limit=" + limit + ", Count=" + periodCount + ", Rate=" + rate);
        }
        return rate;
    }
}
