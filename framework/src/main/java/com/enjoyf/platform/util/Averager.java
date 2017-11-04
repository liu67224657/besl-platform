/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util;

/**
 * Utility to track an average over time. It is not a moving average, rather
 * a discrete average over the last period specified in the ctor.
 * Eg, if period==1 minute, and the object gets ctor'ed at
 * 03:04:00, all counters reset at 03:05:00. Note that this means that
 * if no data points are added via add() over the last 'period' msecs, then
 * getAverage() will return 0.
 */
public class Averager {
    private long m_lastTime;
    private int m_period;
    private int m_count;
    private double m_sum;

    /**
     * @param period The period in msecs.
     */
    public Averager(int period) {
        m_period = period;
        m_lastTime = System.currentTimeMillis();
    }

    /**
     * Add a value to be averaged.
     */
    public void add(int value) {
        add(value, System.currentTimeMillis());
    }

    /**
     * This method takes the current time for optimization reasons.
     */
    public void add(int value, long curTime) {
        p_checkReset(curTime);
        m_count++;
        m_sum += value;
    }

    /**
     * Returns the average over the last 'period' msecs.
     *
     * @param curTime This is an optimized call in that we need the
     *                current time, which the caller might have.
     */
    public double getAverage(long curTime) {
        //--
        // We check for a reset here so that if no values are added, we
        // eventually return 0.
        //--
        p_checkReset(curTime);
        if (m_count == 0) {
            return 0;
        }

        return m_sum / (double) m_count;
    }

    /**
     * Return the average over the last 'period' msecs.
     */
    public double getAverage() {
        return getAverage(System.currentTimeMillis());
    }

    private void p_checkReset(long curTime) {
        if (curTime - m_lastTime > m_period) {
            m_count = 0;
            m_sum = 0;
            m_lastTime = curTime;
        }
    }
}
