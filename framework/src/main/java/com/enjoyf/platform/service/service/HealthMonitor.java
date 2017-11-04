/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

/**
 * Interface describing a HealthMonitor checker for a ServiceConn.
 */
public interface HealthMonitor {
    /**
     * Invoked when we first fire off a transacction.
     */
    public void enter();

    /**
     * Invoked when we exit the transaction.
     */
    public void exit();

    /**
     * Invoked when we exit the transaction due to a timeout.
     */
    public void timeoutExit();

    /**
     * Invoked to ask if the conn is healthy.
     */
    public boolean isHealthy();

    /**
     * Return stats for this object.
     */
    public ChooseStats getChooseStats();
}
