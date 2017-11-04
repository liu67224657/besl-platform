package com.enjoyf.platform.serv.util;

/**
 * CopyRight 2007 Fivewh.com
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * The ServerStartup class provides a hook at server startup time
 * for generic startup processing for arbitrary servers.  Startup
 * time is loosely defined as a time between configuration and
 * actual transaction processing.<p>
 * <p/>
 * Each server must contain a call to startup() at least once during
 * the startup period.  It is OK to call startup more than once; only
 * the first instance will have any effect.<p>
 */
public class ServerStartup {
	
	private static final Logger logger = LoggerFactory.getLogger(ServerStartup.class);

    /**
     * Private singleton instance.
     */
    private static ServerStartup instance = new ServerStartup();

    private boolean didStartup = false;
    private FiveProps envProps = null;


    /**
     * Private singleton constructor
     */
    private ServerStartup() {
    }


    /**
     * This method must be called at least once during server
     * startup time.
     */
    public synchronized void startup() {
        if (didStartup) {
            return;
        }

        logger.info("ServerStartup processing");
        didStartup = true;
        envProps = Props.instance().getEnvProps();

        processStartup();
    }


    /**
     * Singleton accessor
     */
    public static ServerStartup instance() {
        return instance;
    }


    /**
     * Do generic startup processing.
     */
    private void processStartup() {

        startJVMStatsThread();
    }

    /**
     * Starts the thread that will log JVM memory stats.
     */
    private void startJVMStatsThread() {
        int statsPeriod = envProps.getInt("jvm.statsThread.period", 0);
        if (statsPeriod == 0) {
            return;
        }

        //--
        // The prop is in minutes.
        //--
        statsPeriod *= 60 * 1000;

        //--
        // Fire off an object that will periodically collect jvm stats.
        //--
        JVMStatsCollector collector = new JVMStatsCollector(statsPeriod);
        collector.start();
    }

    private void abort(String msg) {
        GAlerter.lab(msg + " - aborting");
        Utility.sleep(5 * 1000);        // allow alert propagation
        System.exit(1);
    }
}
