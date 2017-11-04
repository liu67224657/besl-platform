/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

/**
 * This interface defines how we synch up with a server.
 * This is essentially a callback invoked when a connection is
 * made to a server and we need to "synch up" with it. This usually
 * means sending it some Requests.
 */
public interface Syncher {
    /**
     * This is called by the framework logic to perform the synch up.
     * The business logic should then use the ServiceConn.send() method
     * to send synch requests to the server. Don't you dare call
     * the very same ReqProcessor object with requests while in
     * this routine, since you'll end up in an infinite loop.
     * <p/>
     * Note that the ServiceConn.send() can throw a ServiceException
     * so it's ok to just pass this along.
     *
     * @param sconn   The connection object to synch up to.
     * @param greeter The Greeter object used when greeting the conn.
     */
    public void synchUp(ServiceConn sconn, Greeter greeter) throws ServiceException;
}
