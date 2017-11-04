package com.enjoyf.platform.serv.naming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.thin.DieThread;

/**
 * Just a thread to print out info about the server.
 */
class Monitor extends DieThread {
	
	private static final Logger logger = LoggerFactory.getLogger(Monitor.class);
	
    private Logic logic;

    /**
     * Create the thread object.
     */
    public Monitor(Logic logic) {
        setName("NamingServMonitor:" + getName());
        this.logic = logic;
    }

    public void run() {
        logger.info("Monitor thread running at: " +
                logic.getCfg().getMonInterval() + " msecs ");

        int count = 0;
        while (!shouldDie()) {
            Utility.sleep(logic.getCfg().getMonInterval());
            logger.info("ThreadCount=" + Thread.currentThread().getThreadGroup().activeCount() +
                    ": server info: " + logic.getServerInfo());
            count++;
            if (count % 5 == 0) {
                logic.dumpState();
            }
        }
    }
}
