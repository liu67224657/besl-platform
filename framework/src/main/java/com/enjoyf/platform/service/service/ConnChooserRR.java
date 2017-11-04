package com.enjoyf.platform.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.Refresher;

/**
 * A class that uses round-robin to choose a ServiceConn object.
 */
public class ConnChooserRR extends ConnChooser {

    private static final Logger logger = LoggerFactory.getLogger(ConnChooserRR.class);

    private Refresher refresher = new Refresher(60 * 1000);
    private int curItem = 0;

    /**
     * Ctor the ServiceConn objects with the passed in timeout.
     *
     * @param timeout Timeout for transactions in msecs.
     */
    public ConnChooserRR(int timeout) {
        super(timeout);
    }

    /**
     * Ctor the ServiceConn objects with default timeouts.
     */
    public ConnChooserRR() {
        super(30 * 1000);
    }

    /**
     * Return a ServiceConn object from our cache, using a
     * round-robin scheme.
     *
     * @param req For this object, the input request object
     *            is not used in determining the conn to use.
     * @return Returns the ConnPick object. May return null if nothing
     *         is found.
     */
    protected synchronized ConnPick getPick(Request req) {
        ConnInfo[] conns = getCurrentConns();

        if (conns.length == 0) {
            return null;
        }

        if (refresher.shouldRefresh()) {
            p_dumpStats(conns);
        }

        int i = 0;
        ServiceConn sconn;
        ConnInfo connInfo;
        boolean isHealthy = true;
        do {
            if (curItem >= conns.length) {
                curItem = 0;
            }
            connInfo = conns[curItem];
            sconn = connInfo.getServiceConn();
            curItem++;
            i++;
            isHealthy = sconn.getMonitor().isHealthy();
        }
        while (i < conns.length && !isHealthy);

        if (logger.isTraceEnabled()) {
            logger.trace("CHOOSING: " + connInfo.getServiceConn().toString() );
        }
        return new ConnPick(connInfo);
    }

    private void p_dumpStats(ConnInfo[] connInfoArray) {
        boolean clean = true;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < connInfoArray.length; i++) {
            ServiceConn sconn = connInfoArray[i].getServiceConn();
            ChooseStats chooseStats = sconn.getChooseStats();
            if (!chooseStats.isClean()) {
                clean = false;
            }
            sb.append("CHOOSE_STATS:sconn=" + sconn + ":stats=[" + chooseStats + "]\n");
        }

        // Print out to the debug alert file if we have a problem, otherwise
        // just print to local log file.
        if (!clean) {
            if (logger.isDebugEnabled()) {
                logger.debug(sb.toString());
            }
        } else {
            logger.info(sb.toString());
        }
    }
}
