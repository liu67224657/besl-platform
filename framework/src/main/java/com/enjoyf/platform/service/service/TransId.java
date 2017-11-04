/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

/**
 * Defines an id for entries in PerformanceDataCollector.
 * <p/>
 * Implementing classes MUST provide a valid hashCode() & equals()
 * method.
 */
public interface TransId {
    /**
     * Returns a name for the transaction that can be used to identify it
     * in the logs and metrics. Typically something in upper-case, such
     * as GET_ACCOUNT.
     */
    public String getMetricsName();
}
