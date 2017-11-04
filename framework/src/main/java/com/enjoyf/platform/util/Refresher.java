package com.enjoyf.platform.util;

/**
 * A simple utility class to keep track of refreshing that might need
 * to happen.
 */
public class Refresher {
    private int intervalTime;
    private long lastRefreshTime;

    /**
     * @param interval How long between each intervalTime, in msecs.
     */
    public Refresher(int interval) {
        this(interval, System.currentTimeMillis());
    }

    /**
     * @param interval How long between each intervalTime in msecs.
     * @param lastTime The time to use as the "last time" this got
     *                 invoked. Eg, if 0, then calling shouldRefresh() will return true.
     *                 If System.currentTimeMillis(), then you'll have to wait 'intervalTime'
     *                 msecs before shouldRefresh() will return true. The value is in
     *                 msecs since the epoch.
     */
    public Refresher(int interval, long lastTime) {
        intervalTime = interval;
        lastRefreshTime = lastTime;
    }

    /**
     * Will return true if (currentTime - lastRefreshTime > intervalTime)
     */
    public synchronized boolean shouldRefresh() {
        long curTime = System.currentTimeMillis();
        if ((curTime - lastRefreshTime) > intervalTime) {
            lastRefreshTime = curTime;
            return true;
        }
        return false;
    }

    /**
     * Sets the object to be "refreshed" as of this moment.
     */
    public synchronized void refreshed() {
        lastRefreshTime = System.currentTimeMillis();
    }

    /**
     * Resets the refresher so that the next call to shouldRefresh()
     * will return 'true'.
     */
    public synchronized void reset() {
        lastRefreshTime = 0;
    }
}
