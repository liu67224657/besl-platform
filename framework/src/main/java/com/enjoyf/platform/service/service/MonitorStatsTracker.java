/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import com.enjoyf.platform.stats.CounterDiscrete;

/**
 * Tracks stats for HealthMonitor objects.
 */
public class MonitorStatsTracker {
    /**
     * We create counters with 2 slots, and always read the previous
     * slot to get our data (ie, the info is 1 minute old).
     */
    private static final int ONE_MINUTE = 60 * 1000;
    private static final int NSLOTS = 2;

    private CounterDiscrete goodCounts = new CounterDiscrete(NSLOTS, ONE_MINUTE);
    private CounterDiscrete slowCounts = new CounterDiscrete(NSLOTS, ONE_MINUTE);
    private CounterDiscrete timeoutCounts = new CounterDiscrete(NSLOTS, ONE_MINUTE);
    private CounterDiscrete stuckCounts = new CounterDiscrete(NSLOTS, ONE_MINUTE);

    synchronized void ok() {
        goodCounts.add();
    }

    synchronized void slow() {
        slowCounts.add();
    }

    synchronized void stuck() {
        stuckCounts.add();
    }

    synchronized void timeout() {
        timeoutCounts.add();
    }

    public synchronized ChooseStats getData() {
        return new ChooseStats(
                goodCounts.getCount(1),
                slowCounts.getCount(1),
                stuckCounts.getCount(1),
                timeoutCounts.getCount(1));
    }
}
