/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A dummy implementation that behaves as a NOP.
 */
public class HealthMonitorDummy implements HealthMonitor {
	
	private static final Logger logger = LoggerFactory.getLogger(HealthMonitorDummy.class);
    public void enter() {
    }

    public void exit() {
    }

    public void timeoutExit() {
    }

    public boolean isHealthy() {
    	if (logger.isTraceEnabled()) {
        	logger.trace("Dummy.isHealth()");
    	}
        return true;
    }

    public ChooseStats getChooseStats() {
        return new ChooseStats(100, 0, 0, 0);
    }
}
