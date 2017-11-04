/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import java.util.Random;

/**
 * A class that uses the Request object to choose a server in a such as manner as to direct requests to the same
 * server. This is different from {@link ConnChooserSticky} in that if the number of servers changes the algorithm
 * adjusts to find a server. The scheme takes the available connections, which are always returned in the same order
 * except when the number of servers changes, and selects the same server based on the <code>Request</code>
 * partition value. The partition value should be a hash value of something significant to the sender so that for the
 * same hash value expects to be routed to the same server. If the partition is actually a hash then users should be
 * randomly assigned to the servers. This strategy is called "tacky" since it is somewhat less sticky than
 * the "sticky" strategy.
 * <p/>
 * This scheme allows information about a users session to be cached on a server. The down side is that if there is
 * an addition or loss of a server the allocation of request hash codes to servers will change dramatically. If there
 * are n servers and another server is added then n/(n+1) of the client sessions will be reallocated. However, usually
 * the number of servers is constant and so requests with the same hash code/partition would arrive at the same server.
 * This has the advantage of being a lot simpler to configure than a strictly partitioned service and also means a
 * failure of one server doesn't lead to a partial failure of the system.
 * <p/>
 * The risk is that during system unstability there will be large numbers of reassignments resulting in heavy load
 * on the servers which in turn may lead to further instability. This is only a potential risk and may not
 * occur in actual usage.
 */
public class ConnChooserTacky extends ConnChooser {
    private Random random = new Random();

    /**
     * Ctor the ServiceConn objects with the passed in timeout.
     *
     * @param timeout Timeout for transactions in msecs.
     */
    public ConnChooserTacky(int timeout) {
        super(timeout);
    }

    /**
     * Ctor the ServiceConn objects with default timeouts.
     */
    public ConnChooserTacky() {
        this(30 * 1000);
    }

    /**
     * Return a ServiceConn object from our cache, using a
     * tacky partitioning scheme. The connections should always be returned in the same order unless there has been
     * a change in the number of connectoins. One is selected based on an index from the request partition modulo
     * the connection array length.
     *
     * @return Returns the ServiceConn object. May return null
     *         if nothing is found which generally means there are no servers of this type.
     */
    protected synchronized ConnPick getPick(Request req) {
        ConnInfo[] conns = getCurrentConns();

        //--
        // If the request is partitionable, we use that info to route
        // to a particular instance. If it isn't, we pick an instance at
        // random.
        //--
        if (conns.length == 0) {
            return null;
        } else if (req.isPartitionable()) {
            return new ConnPick(conns[req.getPartition() % conns.length]);
        } else {
            return new ConnPick(conns[Math.abs(random.nextInt()) % conns.length]);
        }
    }
}
