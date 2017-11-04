/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import com.enjoyf.platform.service.service.PerformanceDataCollector;

/**
 * To be implemented by objects that expose metrics.
 */
public interface PerfDataAccessor {
    PerformanceDataCollector getPerfDataCollector();
}
