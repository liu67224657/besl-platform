package com.enjoyf.platform.service.service;

import java.util.Random;

/**
 * A class that uses the Request object to choose a server in a
 * partitioned scheme. The Request object identifies which partition
 * to use.
 */
public class ConnChooserPartition extends ConnChooser {
    //
    private Random rand = new Random();

    //
    private static final int MAX_RAND_TIMES = 3;

    /**
     * Ctor the ServiceConn objects with the passed in timeout.
     *
     * @param timeout Timeout for transactions in msecs.
     */
    public ConnChooserPartition(int timeout) {
        super(timeout);
    }

    /**
     * Ctor the ServiceConn objects with default timeouts.
     */
    public ConnChooserPartition() {
        this(30 * 1000);
    }

    /**
     * Return a ServiceConn object from our cache, using a
     * partition scheme.
     *
     * @return Returns the ServiceConn object. May return null
     *         if nothing found. This usually means that the ServiceFinder
     *         object could not find any services that matched our request.
     *         For partitionable objects, all possible servers must exist
     *         otherwise this will fail.
     */
    protected synchronized ConnPick getPick(Request req) {
        if (!req.isPartitionable()) {
            return randomPick(req);
        }

        ConnInfo[] conns = getCurrentConns();
        if (conns.length == 0) {
            return null;
        }

        int partition = req.getPartition();
        for (int i = 0; i < conns.length; i++) {
            if (p_check(conns[i], partition)) {
                return new ConnPick(conns[i]);
            }
        }

        return null;
    }

    /**
     * Well, what if I don't want partition for some specific requests? use this.
     * Note, this method does not enforce a lock, you must pay attention to the synchronization
     *
     * @param req
     * @return
     * @author Daniel
     */
    protected ConnPick randomPick(Request req) {
        //the all matched serviceData.
        ConnInfo[] conns = getCurrentConns();

        if (conns.length == 0) {
            return null;
        }

        //pick a conn random.
        int retry = 0;
        while (retry++ < MAX_RAND_TIMES) {
            //
            ConnInfo connInfo = conns[rand.nextInt(conns.length)];
            ServiceConn sconn = connInfo.getServiceConn();

            //
            boolean isHealthy = sconn.getMonitor().isHealthy();
            if (isHealthy) {
                return new ConnPick(connInfo);
            }
        }

        //
        return null;
    }

    /**
     * @param req       The req we are going to logicProcess.
     * @param connCache The cache of current conns to use.
     */
    protected boolean reallyNeedSome(Request req, ConnCache connCache) {

        if (!req.isPartitionable()) {
            return super.reallyNeedSome(req, connCache);
        }

        ConnInfo[] conns = connCache.getCurrentConns();
        for (int i = 0; i < conns.length; i++) {
            if (req.getPartition() == conns[i].getPartition()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if this is the connection we want for this
     * request.
     */
    private boolean p_check(ConnInfo connInfo, int partition) {
        return connInfo.getPartition() == partition;
    }
}
