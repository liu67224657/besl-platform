package com.enjoyf.platform.service.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.stats.SampleStats;

/**
 * A class that uses the current load to pick a conn. It also sticks to
 * that one conn.
 */
public class ConnChooserLoad extends ConnChooser {
	
	private static final Logger logger = LoggerFactory.getLogger(ConnChooserLoad.class);
	
    private long lastCheckTime = 0;
    private Random random = new Random(System.currentTimeMillis());
    private ConnInfo curConn = null;

    /**
     * When choosing a least-loaded conn, we don't just want to choose
     * the least loaded one. Since load updates only arrive with a
     * certain frequency, we can easily overload the least loaded
     * server. Instead, choose one at random from the X% least loaded
     * ones.
     */
    private int loadPercentage = 40;

    /**
     * Use this to sort by load.
     */
    private Comparator<ConnInfo> comparator = new ConnInfoComparator();

    /**
     * Ctor this object.
     *
     * @param timeout The timeout for transactions.
     */
    public ConnChooserLoad(int loadPercentage, int timeout) {
        super(timeout);

        this.loadPercentage = loadPercentage;
    }

    protected synchronized ConnPick getPick(Request req) {
        ConnPick pick = p_getImpl(req);

        //GAlerterLogger.lh( "ConnChooserLoad: pick = " + pick );
        return pick;
    }

    protected synchronized ConnPick p_getImpl(Request req) {
        ConnInfo[] conns = null;

        if (curConn != null) {
            conns = getCurrentConns();
            //--
            // This code makes the decision to try another. It will
            // only do so if we are currently overloaded.
            //--
            ConnInfo newConn = p_tryAnother(curConn, conns);
            if (newConn != null) {
                ConnPick pick = new ConnPick(newConn, curConn);
                curConn = newConn;

                return pick;
            } else {
                ServiceConn sconn = curConn.getServiceConn();

                if (sconn != null && sconn.isAvailable()) {
                    return new ConnPick(curConn);
                }
            }
        }

        //--
        // If here, we either never had a conn, or the current one has
        // been shut down. This means we must find another one.
        //--
        curConn = null;
        if (conns == null) {
            conns = getCurrentConns();
        }

        if (conns.length == 0) {
            return null;
        }

        ConnInfo connInfo = p_pickOne(conns);
        if (connInfo == null) {
            return null;
        }

        curConn = connInfo;

        return new ConnPick(connInfo);
    }

    private ConnInfo p_pickOne(ConnInfo[] conns) {
        //--
        // Make a copy of the ConnInfo array and sort it by load.
        //--
        ConnInfo[] temp = (ConnInfo[]) conns.clone();
        Arrays.sort(temp, comparator);

        //--
        // This bit tries to do the following: while we normally try to
        // find the least loaded servers and use those, if the load is
        // about the same on all of them, let's just randomize over all
        // of them.
        //--
        SampleStats stats = new SampleStats();
        for (int i = 0; i < temp.length; i++) {
            stats.add(temp[i].getServiceData().getServiceLoad().getCurLoad());
        }

        int loadPercentage = 100;
        for (int i = 0; i < temp.length; i++) {
            if (stats.isOutsideStddev(
                    temp[i].getServiceData().getServiceLoad().getCurLoad())) {
                loadPercentage = this.loadPercentage;
                break;
            }
        }

        //--
        // Pick a conn at random from the ones that satisfy the load
        // percentage.
        //--
        int maxIdx = (int) (((double) loadPercentage) / 100.0 * (double) temp.length);
        if (maxIdx == 0) {
            return temp[0];
        }

        int idx = Math.abs(random.nextInt()) % maxIdx;
        ConnInfo conn = temp[idx];

        // GAlerterLogger.lh( "ConnChooserLoad.p_pickOne: returning: " + conn );
        return conn;
    }

    /**
     * Returns a new ConnInfo object to use if we find that the current
     * one is overloaded AND there is one that is not.
     */
    private ConnInfo p_tryAnother(ConnInfo conn, ConnInfo[] conns) {
        //--
        // If our current conn is not overloaded, then we're fine.
        //--
        ServiceLoad load = conn.getServiceData().getServiceLoad();
        if (!load.isOverloaded()) {
            return null;
        }

        //--
        // If no alternatives, just exit.
        //--
        if (conns.length == 0) {
            return null;
        }

        logger.warn("ConnChooserLoad.p_tryAnother: overloaded = " + conn);
        //--
        // Don't try anything if it's only been a minute since the last
        // time.
        //--
        long curTime = System.currentTimeMillis();
        if (curTime - lastCheckTime < 60 * 1000) {
            return null;
        }

        lastCheckTime = curTime;
        //--
        // If here, our current conn is overloaded, let's see if we
        // can find one that is not.
        //--
        ConnInfo newConnInfo = p_pickOne(conns);
        if (!newConnInfo.getServiceData().getServiceLoad().canAcceptMoreLoad()) {
            return null;
        }

        logger.warn("ConnChooserLoad: chose a new conn because the current one " + conn + " is overloaded: " + newConnInfo);
        return newConnInfo;
    }

    ////////////////////////////////////////////////////////////////////
    private static class ConnInfoComparator implements Comparator<ConnInfo> {
        //
        public int compare(ConnInfo c1, ConnInfo c2) {
            ServiceLoad l1 = c1.getServiceData().getServiceLoad();
            ServiceLoad l2 = c2.getServiceData().getServiceLoad();
            return l1.getCurLoad() - l2.getCurLoad();
        }
    }
}
