/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import com.enjoyf.platform.stats.WindowAverager;

/**
 * Collects data for java servers.
 */
public class TransDataCollector {
    static private final int TIME_INTERVAL = 60 * 1000;

    private WindowAverager respTimeAverager;
    private WindowAverager throughputAverager;

    private long transTime = 0;
    private int transCount = 0;

    private int maxTransTime = 0;
    private int minTransTime = Integer.MAX_VALUE;

    private long startTime = System.currentTimeMillis();
    private long lastTime = System.currentTimeMillis();

    private long transTotal = 0;

    public TransDataCollector() {
        respTimeAverager = new WindowAverager(10);
        throughputAverager = new WindowAverager(10);
    }

    /**
     * Call this every time a transaction executes.
     *
     * @param executionTime Execution time of the transaction in msecs.
     */
    public synchronized void incTrans(int executionTime) {
        transTotal++;
        transCount++;
        transTime += executionTime;

        if (executionTime > maxTransTime) {
            maxTransTime = executionTime;
        }

        if (executionTime < minTransTime) {
            minTransTime = executionTime;
        }

        long cur = System.currentTimeMillis();
        if ((cur - lastTime) > TIME_INTERVAL) {
            respTimeAverager.add(transTime / transCount);
            throughputAverager.add(transCount);
            transTime = 0;
            transCount = 0;
            lastTime = cur;
        }
    }

    public long getTotal() {
        return transTotal;
    }

    public int getMinTransTime() {
        return minTransTime;
    }

    public int getMaxTransTime() {
        return maxTransTime;
    }

    /**
     * Retrieve the avg time for this transaction.
     */
    public synchronized int getRespTimeAvg(int windowSize) {
        return (int) respTimeAverager.getAverage(windowSize);
    }

    public synchronized int getThroughputAvg(int windowSize) {
        return (int) throughputAverager.getAverage(windowSize);
    }
}
