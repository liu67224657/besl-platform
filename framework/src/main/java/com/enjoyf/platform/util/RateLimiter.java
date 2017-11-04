/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util;

/**
 * Class used for rate limiting. It uses a simple algorithm of incrementing
 * a counter for each bad occurrence, and if the last increment puts us
 * over the limit, it will return true. Once the periodTime has expired, the
 * counter is reset.
 */
public class RateLimiter {
    private int periodTime;
    private int maxCount;
    private int curCount = 0;
    private long lastTime = 0;

    /**
     * @param period   The periodTime in msecs.
     * @param maxCount The max count during the periodTime.
     */
    public RateLimiter(int period, int maxCount) {
        periodTime = period;
        this.maxCount = maxCount;
    }

    /**
     * Calling this routine increments the current counter. If the
     * current counter exceeds the passed in maxCount during the
     * 'periodTime' interval, a 'true' is returned. The current counter
     * will be reset if the (current_time - last_reset_time > periodTime)
     *
     * @param incrementAmount Increments the counter by this amount.
     */
    public synchronized boolean isLimited(int incrementAmount) {
        long now = System.currentTimeMillis();
        if (now - lastTime > periodTime) {
            curCount = 0;
            lastTime = now;
        }
        curCount += incrementAmount;
        return curCount > maxCount;
    }

    /**
     * Asks if we are limited w/o incrementing the current counter.
     */
    public boolean isLimited() {
        return isLimited(0);
    }

    /**
     * Resets the limiter. This might be useful in cases where you want
     * isLimited() to return 'true' only once per time periodTime.
     */
    public synchronized void reset() {
        lastTime = System.currentTimeMillis();
        curCount = 0;
    }
}
