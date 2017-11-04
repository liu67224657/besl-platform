/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import com.enjoyf.platform.io.ServiceAddress;

/**
 * An interface describing a load checker when making connections to
 * a particular server. We can detect on the client-side whether or not
 * the particular server is overloaded by monitoring timeouts and the like.
 * If it is, we want to avoid pounding that server by continuous requests
 * for connections; ie, we want to throttle the client back.
 */
public interface ConnLoadChecker {
    /**
     * Called by the connection logic to find out if the server we are
     * connecting to is overloaded.
     *
     * @param saddr The ServiceAddress of the server we are connecting to.
     */
    public boolean isOverloaded(ServiceAddress saddr);

    /**
     * Called prior to making a connecction attempt.
     *
     * @param saddr The ServiceAddress of the server we are connecting to.
     */
    public void connAttempt(ServiceAddress saddr);

    /**
     * Called after a connection is succesful.
     *
     * @param saddr The ServiceAddress of the server we are connecting to.
     */
    public void connSucceeded(ServiceAddress saddr);
}
