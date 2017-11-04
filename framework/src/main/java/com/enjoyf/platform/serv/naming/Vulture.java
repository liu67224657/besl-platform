package com.enjoyf.platform.serv.naming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.thin.DieThread;

/**
 * A thread that reaps any registrations that have not pinged during
 * the timeout period.
 */
class Vulture extends DieThread {
    
    private static final Logger logger = LoggerFactory.getLogger(Vulture.class);
    
    private Logic logic;

    /**
     * Create the thread object.
     */
    public Vulture(Logic l) {
        setName("VultureThread:" + getName());
        logic = l;
    }

    public void run() {
        logger.info("Vulture thread running at: "
                + logic.getCfg().getVultureInterval() + " msecs ");

        while (!shouldDie()) {
            Utility.sleep(logic.getCfg().getVultureInterval());
            logic.reapConns();
        }
    }
}
