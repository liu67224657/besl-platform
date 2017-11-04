package com.enjoyf.platform.serv.rate;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yin Pengyi
 */
class MasterTable {
	
	private static final Logger logger = LoggerFactory.getLogger(MasterTable.class);
	
	private Map<Period, PeriodTable> table = new ConcurrentHashMap<Period, PeriodTable>();

    /**
     * Constructs a MasterTable
     *
     * @param interval - how often to clean up this table
     *                 most likely the smallest Period's duration
     */
    public MasterTable(int interval) {
        new Thread(new CleanUp(table, interval)).start();
    }

    /**
     * override to ensure only Period and PeriodTable are in this table
     */
    public boolean containsValue(PeriodTable value) {
        return table.containsValue(value);
    }

    /**
     * override to ensure only Period and PeriodTable are in this table
     */
    public boolean containsKey(Object key) {
        return table.containsKey(key);
    }

    /**
     * override to ensure only Period and PeriodTable are in this table
     */
    public PeriodTable get(Object key) {
        return table.get(key);
    }

    /**
     * override to ensure only Period and PeriodTable are in this table
     */
    public PeriodTable put(Period key, PeriodTable value) {
        return table.put(key, value);
    }


    /**
     * Inner class to clean up the master table
     */
    class CleanUp implements Runnable {
        private int interval;
        private Map<Period, PeriodTable> table;

        /**
         * Constructs a CleanUp
         *
         * @param table    - master table
         * @param interval - how often to run; most likely the smallest Period's duration
         */
        CleanUp(Map<Period, PeriodTable> table, int interval) {
            this.table = table;
            this.interval = interval;
        }

        /**
         * run implementation
         * Removes the period table the master table at the end of the duration
         */

        public void run() {
            while (true) {
            	
                // go to sleep for the specified interval
                try { Thread.sleep(interval * 1000); } catch (InterruptedException e) { }
                if (logger.isDebugEnabled()) {
                	logger.debug("CleanUp waking up.  Starting MasterTable clean up (" + table.size() + ") elements");
                }
                
                synchronized (table) {
                    for (Iterator<Period> i = table.keySet().iterator(); i.hasNext();) {
                        // get the next period (key)
                        Period period = i.next();
                        if (logger.isDebugEnabled()) {
                        	logger.debug("CleanUp examining period: " + period);
                        }

                        if (!period.expired()) {
                        	continue;
                        }
                        
                        // if this period has expired iterate through period counts and remove
                        // the entries that have expired
                        PeriodTable periodTable = table.get(period);
                        for (Iterator<PeriodCount> counts = periodTable.values().iterator(); counts.hasNext();) {
                            PeriodCount periodCount = counts.next();
                            //check if period has expired
                            if (periodCount.getPeriod().expired()) {
                            	if (logger.isDebugEnabled()) {
                            		logger.debug("Cleanup remove count from period table with period :" + 
                            				periodCount.getPeriod());
                            	}
                                counts.remove();
                            }
                        }
                        //if period table is empty then remove if from master table
                        if (periodTable.isEmpty()) {
                        	if (logger.isDebugEnabled()) {
                            	logger.debug("CleanUp removing " + period + " from MasterTable");
                        	}
                            i.remove();
                        }
                    }
                }
            }
        }
    }
}

