/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.Utility;

/**
 * This class is a thread which collects jvm stats over time and writes
 * them to the esm service.
 */
public class JVMStatsCollector extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(JVMStatsCollector.class);
    private int statsPeriod;

    /**
     * @param statsPeriod How often to collect jvm stats in msecs. It should
     *                    be on the order of minutes.
     */
    JVMStatsCollector(int statsPeriod) {
        setName("JVM-Stats-Thread");
        this.statsPeriod = statsPeriod;
        if (this.statsPeriod <= 0) {
            logger.info("No JVM stats will be collected as period is 0");
            return;
        }
        if (this.statsPeriod <= 60 * 1000) {
            logger.info("Resetting jvm stats collection to 1 minute");
            this.statsPeriod = 60 * 1000;
        }
    }

    public void run() {
        logger.info("Starting up JVM stats thread with a period of: "
                + statsPeriod + " msecs");

        JVMStats jvmStats = new JVMStats();

        while (true) {
            Utility.sleep(statsPeriod);
            jvmStats.logAllStats();
        }
    }


}
