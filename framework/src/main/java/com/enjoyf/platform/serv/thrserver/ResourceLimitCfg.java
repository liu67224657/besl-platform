/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

public class ResourceLimitCfg {
    private int maxFailedRequestsPerPeriod;
    private int period;

    /**
     * @param maxFailedRequestsPerPeriod This is the max number of
     *                                   failed requests per period. Ie, requests that failed due to
     *                                   resource limitation.
     * @param period                     This is the period in msecs during which we
     *                                   track requests.
     */
    public ResourceLimitCfg(int maxFailedRequestsPerPeriod, int period) {
        this.maxFailedRequestsPerPeriod = maxFailedRequestsPerPeriod;
        this.period = period;
    }

    /**
     * Use this ctor to create an object that tells the server framework
     * that this feature is not enabled.
     */
    public ResourceLimitCfg() {
        maxFailedRequestsPerPeriod = 0;
        period = 0;
    }

    public int getMaxFailedRequestsPerPeriod() {
        return maxFailedRequestsPerPeriod;
    }

    public int getPeriod() {
        return period;
    }

    /**
     * Returns true if we have enabled resource limit trapping.
     */
    public boolean isEnabled() {
        return period != 0 && maxFailedRequestsPerPeriod != 0;
    }

    public String toString() {
        return "period=" + period + ":maxRequestsPerPeriod="
                + maxFailedRequestsPerPeriod;
    }
}
