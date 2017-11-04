/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.stats;

/**
 * Keeps track of averages over the last N minutes.
 */
public class StatsCollector {
    static private final int TIME_INTERVAL = 60 * 1000;

    private WindowAverager m_averager;
    private int m_count = 0;
    private int m_windowSize = 10;
    private long m_lastTime = System.currentTimeMillis();

    /**
     * Create the object with the default of a 10 minute window.
     */
    public StatsCollector() {
        this(10);
    }

    /**
     * Create the object with an N minute window.
     *
     * @param windowSize The window size in minutes.
     */
    public StatsCollector(int windowSize) {
        m_averager = new WindowAverager(windowSize);
        m_windowSize = windowSize;
        //--
        // This tells the underlying averager to 0 things out
        // if we have not received data in a while.
        //--
        m_averager.setPeriod(TIME_INTERVAL);
    }

    /**
     * Call this every time "something" happens.
     */
    public synchronized void add() {
        m_count++;

        long cur = System.currentTimeMillis();
        if ((cur - m_lastTime) > TIME_INTERVAL) {
            m_averager.add(m_count);
            m_count = 0;
            m_lastTime = cur;
        }
    }

    /**
     * Retrieve the average over the specified window.
     *
     * @param windowSize The window size in minutes. Any integer
     *                   up to the specified window size specified in the ctor can
     *                   be specified. Eg, if ctor'ed with 10, you can retrieve the
     *                   average over a 1 minute window, up to a 10 minute window.
     */
    public synchronized int getAverage(int windowSize) {
        if (windowSize < 1 || windowSize > m_windowSize) {
            throw new IllegalArgumentException("StatsCollector: "
                    + " Window out of range: " + windowSize);
        }

        return (int) m_averager.getAverage(windowSize);
    }
}
