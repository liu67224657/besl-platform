/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import java.util.HashMap;

import com.enjoyf.platform.util.Averager;

public class SlownessMonitor {
    private static final int ONE_MINUTE = 60 * 1000;

    /**
     * This object tracks the average response time.
     */
    private Averager m_averager;

    /**
     * This is the threashold, if the average exceeds this value we say
     * the object is processing transactions too slowly.
     */
    private int m_threshold;

    /**
     * This map track the current outstanding transactions.
     */
    private HashMap m_map = new HashMap();


    /**
     * @param threshold The threshold in msecs. If the average time
     *                  is slower than this, then it's considered unhealthy.
     */
    SlownessMonitor(int threshold) {
        m_threshold = threshold;
        /**
         * This object ctor wants the no. of minutes we are
         * going to be averaging over.
         */
        m_averager = new Averager(ONE_MINUTE);
    }

    synchronized void enter() {
        m_map.put(TransId.get(), new Long(System.currentTimeMillis()));
    }

    synchronized void exit() {
        exit(System.currentTimeMillis());
    }

    synchronized void exit(long curTime) {
        Long startTime = (Long) m_map.remove(TransId.get());
        if (startTime != null) {
            int diff = (int) (curTime - startTime.longValue());
            m_averager.add(diff, curTime);
        }
    }

    synchronized boolean isSlow() {
        int average = (int) m_averager.getAverage();
        return average > m_threshold;
    }

    /**
     * Utility class which uses a ThreadLocal to manage keys for tracking
     * the various transactions. Transactions execute on a thread, so we
     * can use a ThreadLocal to track a particular transaction.
     */
    static class TransId {
        private static int g_nextTransId = 0;

        private static ThreadLocal g_transId = new ThreadLocal() {
            protected synchronized Object initialValue() {
                return new Integer(g_nextTransId++);
            }
        };

        static Integer get() {
            return ((Integer) g_transId.get());
        }
    }
}
