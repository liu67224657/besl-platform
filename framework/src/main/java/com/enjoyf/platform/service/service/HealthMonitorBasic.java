/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

/**
 * This is somewhat complicated implementation that checks conns to
 * see if they are stuck, or are getting too many timeouts, or are too
 * "slow".
 */
public class HealthMonitorBasic implements HealthMonitor {
    /**
     * Tracks the current # of outstanding transactions.
     */
    private int curCount = 0;
    /**
     * Tracks the last time a transaction completed.
     */
    private long lastTime = 0;
    /**
     * The time to let pass before we say that we are "stuck".
     */
    private int timeoutTime;

    /**
     * We use a SlownessMonitor to find out if a conn is processing
     * transactions slowly.
     */
    private SlownessMonitor slownessMonitor = new SlownessMonitor(5 * 1000);

    /**
     * Tracks timeouts and decides whether or not the conn is "healthy".
     */
    private TimeoutMonitor timeoutMonitor = new TimeoutMonitor();

    /**
     * Tracks stats.
     */
    private MonitorStatsTracker statsTracker = new MonitorStatsTracker();

    /**
     * @param timeout The timeout in msecs after which we consider a conn
     *                in "stuck" mode if transactions are not being processed.
     */
    public HealthMonitorBasic(int timeout) {
        timeoutTime = timeout;
    }

    public synchronized void enter() {
        if (lastTime == 0) {
            lastTime = System.currentTimeMillis();
        }

        slownessMonitor.enter();
        curCount++;
    }

    public synchronized void timeoutExit() {
        slownessMonitor.exit();
        timeoutMonitor.timeoutExit();
        curCount--;
    }

    public synchronized void exit() {
        long curTime = System.currentTimeMillis();
        curCount--;
        lastTime = curTime;
        slownessMonitor.exit(curTime);
        timeoutMonitor.exit();
    }

    public synchronized boolean isHealthy() {
        long curTime = System.currentTimeMillis();
        //--
        // We check various things to see if we are healthy.
        // - timeouts. If we have been receiving timeouts, we mark the conn
        // as unhealthy.
        // - stuck. This means that socket writes are "stuck" and not
        // returning. Typically due to blocking at the network layer.
        // - slow. This means transactions are executing slowly (>5 secs).
        //
        // We use a MonitorStatsTracker object to keep track of what kind
        // of conditions we are running into.
        //--
        boolean timeout = !timeoutMonitor.isHealthy(curTime, lastTime);
        boolean stuck = !p_checkStuckTransactions(curTime);
        boolean slow = p_isSlow();

        if (timeout) {
            statsTracker.timeout();
        } else if (stuck) {
            statsTracker.stuck();
        } else if (slow) {
            statsTracker.slow();
        } else {
            statsTracker.ok();
        }

        return !timeout && !stuck && !slow;
    }

    private boolean p_isSlow() {
        return slownessMonitor.isSlow();
    }

    private boolean p_checkStuckTransactions(long curTime) {
        long diff = curTime - lastTime;
        return curCount <= 0 || lastTime == 0 || diff <= timeoutTime;
    }

    public ChooseStats getChooseStats() {
        return statsTracker.getData();
    }

    /**
     * Class to monitor timeouts, ie, when a service call receives a
     * ServiceException.TIMEOUT. It decides whether we should declare
     * the connection "unhealthy".
     */
    static class TimeoutMonitor {
        /**
         * This is how long we initially wait to retry a conn if we find
         * it to be bad.
         */
        private static final int BASE_TIMEOUT_EXPIRATION = 30 * 1000;
        /**
         * Max timeouts in a row before we consider this unhealthy.
         */
        private static final int MAX_TIMEOUTS = 5;
        /**
         * If we are in the timeout state, we want to retry at certain intervals
         * to see if the conn is good again. It starts at a certain value and
         * increments as we encounter more and more timeouts.
         */
        private long m_timeoutExpiration = BASE_TIMEOUT_EXPIRATION;
        /**
         * As we try to use a conn, fail, and retry again, we extend the
         * the time before trying again by this multiplier.
         */
        private static final double TIMEOUT_EXPIRATION_MULTIPLIER = 1.5;

        /**
         * Tracks how many TIMEOUT's we have seen in a row.
         */
        private int m_curTimeoutCount;
        /**
         * If we are in the "timeout state", it means we've received too
         * many timeouts and are marking the conn as unhealthy for some
         * amount of time.
         */
        private boolean m_healthy = true;

        /**
         * Invoked when a service called exited with a timeout.
         */
        void timeoutExit() {
            m_curTimeoutCount++;

            /**
             * If our count exceeds a static value, we mark the conn
             * as unhealthy.
             */
            if (m_curTimeoutCount > MAX_TIMEOUTS && m_healthy) {
                m_healthy = false;
            }
        }

        /**
         * Will return true if we are healthy.
         */
        boolean isHealthy(long curTime, long lastTime) {
            if (m_healthy) {
                return true;
            }

            long diff = curTime - lastTime;
            if (diff > m_timeoutExpiration) {
                //--
                // If we waited long enough, let's mark the conn as healthy
                // again so that we can try again. At the same time, we
                // bump up the interval we wait the next time around if
                // we continue to have timeouts. Note that if we don't
                // have timeouts, the exit() call will be invoked and we
                // will reset this interval to the base value.
                //--
                m_healthy = true;
                m_timeoutExpiration *= TIMEOUT_EXPIRATION_MULTIPLIER;
            }
            return m_healthy;
        }

        long getTimeoutExpiration() {
            return m_timeoutExpiration;
        }

        /**
         * Invoked when a transaction completed w/o a timeout.
         */
        void exit() {
            m_curTimeoutCount = 0;
            //--
            // If we had a transaction that completed w/o a timeout, then
            // we mark the conn as good again as well as reset the interval
            // that we mark the conn as unhealthy if we run into the
            // timeout condition again.
            //--
            m_healthy = true;
            m_timeoutExpiration = BASE_TIMEOUT_EXPIRATION;
        }
    }
}
