/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import java.util.HashMap;

import com.enjoyf.platform.io.ServiceAddress;

/**
 * An implementation of ConnLoadChecker that throttles back over time.
 */
public class ConnLoadCheckerStd implements ConnLoadChecker {
    /**
     * A Map of ServiceAddress->internal Data objects.
     */
    private HashMap m_map = new HashMap();
    private int m_initWaitTime;
    private WaitTime m_waitTime;

    /**
     * This object requires the initial wait time in case of a connect
     * failure, as well as a throttle multiplier for throttling back
     * as we keep getting failures.
     *
     * @param initWaitTime       The initial wait time in msecs.
     * @param throttleMultiplier A multiplier for subsequent wait times.
     *                           Eg, if initWaitTime=10000, and throttleMultiplier=1.5, it will wait
     *                           10 secs after the initial failure. After the 2nd failure, it will
     *                           wait 15 secs, etc. NOTE: THIS VALUE MUST BE BETWEEN 1 and 4.
     *                           This is to avoid potentially strange behavior.
     * @throws IllegalArgumentException Thrown if initWaitTime <= 0. Also
     *                                  if assert( 1.0 <= initWaitTime <= 4.0 ) fails.
     */
    public ConnLoadCheckerStd(int initWaitTime, double throttleMultiplier) {
        if (initWaitTime <= 0) {
            throw new IllegalArgumentException("initWaitTime<=0!");
        }

        if (throttleMultiplier < 1.0 || throttleMultiplier > 4.0) {
            throw new IllegalArgumentException("throttleMultiplier must be between 1 and 4!");
        }

        m_initWaitTime = initWaitTime;
        m_waitTime = new WaitTimeFixed(throttleMultiplier);
    }

    /**
     * A ctor that takes an abstract WaitTime computation object.
     */
    public ConnLoadCheckerStd(int initWaitTime, WaitTime waitTime) {
        m_initWaitTime = initWaitTime;
        m_waitTime = waitTime;
    }

    public synchronized boolean isOverloaded(ServiceAddress saddr) {
        Data data = (Data) m_map.get(saddr);
        if (data == null) {
            return false;
        }

        return data.shouldWait();
    }

    public synchronized void connAttempt(ServiceAddress saddr) {
        boolean isNew = false;

        Data data = (Data) m_map.get(saddr);
        if (data == null) {
            data = new Data(saddr, m_initWaitTime);
            m_map.put(saddr, data);
            isNew = true;
        }
        data.setSucceeded(false);
        data.setLastConnAttempt(System.currentTimeMillis());
        //--
        // Only throttle back after the first initial wait.
        //--
        if (!isNew) {
            data.throttleBack(m_waitTime);
        }
    }

    /**
     * This is mostly for information purposes.
     */
    public synchronized int getCurrentWaitTime(ServiceAddress saddr) {
        Data data = (Data) m_map.get(saddr);
        if (data == null) {
            return 0;
        }

        return data.getCurrentWaitTime();
    }

    public synchronized void connSucceeded(ServiceAddress saddr) {
        Data data = (Data) m_map.get(saddr);
        if (data != null) {
            data.resetOnSuccess(m_initWaitTime);
        }
    }

    /**
     * Need to track some data per ServiceAddress.
     */
    private static class Data {
        private ServiceAddress m_saddr;
        private long m_lastConnAttempt;
        private boolean m_succeeded = false;
        private int m_currentWaitTime;

        Data(ServiceAddress saddr, int initWaitTime) {
            m_saddr = saddr;
            m_currentWaitTime = initWaitTime;
        }

        boolean hasSucceeded() {
            return m_succeeded;
        }

        void setSucceeded(boolean succeeded) {
            m_succeeded = succeeded;
        }

        void setLastConnAttempt(long lastConnAttempt) {
            m_lastConnAttempt = lastConnAttempt;
        }

        boolean shouldWait() {
            if (m_succeeded) {
                return false;
            }

            return System.currentTimeMillis() - m_lastConnAttempt < m_currentWaitTime;
        }

        int getCurrentWaitTime() {
            return m_currentWaitTime;
        }

        void throttleBack(WaitTime waitTime) {
            //--
            // Keep doubling the wait time if we keep failing.
            //--
            m_currentWaitTime = (int) waitTime.getNextWaitTime(m_currentWaitTime);
        }

        void resetOnSuccess(int initialWaitTime) {
            m_currentWaitTime = initialWaitTime;
            m_succeeded = true;
        }
    }

    public String toString() {
        return "initWaitTime=" + m_initWaitTime + ":waitTime=" + m_waitTime;
    }
}
