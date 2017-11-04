package com.enjoyf.platform.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * A class that uses the Request object to choose a server in a
 * partitioned scheme, if the current partition is not available, fail over to the next partition to serve it.
 * If you set the partitionFailoverNum to 1, it is equivalent to the traditional ConnChooserRR
 */
public class ConnChooserRingPartition extends ConnChooserPartition {

    private static final Logger logger = LoggerFactory.getLogger(ConnChooserRingPartition.class);

    private static final int DEFAULT_FAILOVER_NUM = 2;

    private int partitionFailoverNum = 2;
    private int partitionNum = 1;

    public ConnChooserRingPartition(int totalPartitions, int partitionsToFailover, int timeout) {
        super(timeout);

        this.partitionNum = totalPartitions;
        this.partitionFailoverNum = partitionsToFailover;

        if (partitionFailoverNum > partitionNum) {
            partitionFailoverNum = partitionNum;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("ConnChooserRingPartition params: partitionNum=" + partitionNum + ", failoverNum=" + partitionFailoverNum);
        }
    }

    /**
     * Ctor the ServiceConn objects with the passed in timeout.
     *
     * @param timeout Timeout for transactions in msecs.
     */
    public ConnChooserRingPartition(int pn, int timeout) {
        this(pn, DEFAULT_FAILOVER_NUM, timeout);
    }

    /**
     * Ctor the ServiceConn objects with default timeouts.
     */
    public ConnChooserRingPartition(int pn) {
        this(pn, DEFAULT_FAILOVER_NUM, 30 * 1000);
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

        //find the certain connection.
        ConnInfo connInfo = null;
        int partition = req.getPartition();
        int tryTimes = 0;

        Map<Integer, ConnInfo> validConns = getCurrentPartitionConns();
        int validConnNum = validConns.size();

        do {
            connInfo = validConns.get(partition);

            tryTimes++;
            partition++;

            //if we reach the end of the partition ring, go back to the first one.
            if (partition == partitionNum) {
                partition = 0;
            }
        } while (connInfo == null && tryTimes < partitionFailoverNum && tryTimes < validConnNum);

        //
        if (connInfo == null) {
            return null;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("ConnChooserRingPartition choose server " + connInfo.getPartition() + " for request:" + req.getPartition());
            }

            return new ConnPick(connInfo);
        }
    }

}
