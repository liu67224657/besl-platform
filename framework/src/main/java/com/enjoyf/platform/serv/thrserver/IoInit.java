/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import com.enjoyf.platform.util.FiveProps;

/**
 * An interface to provide io initialization to the server framework.
 * It's an abstraction, in order to differentiate between underlying
 * implementation (NIO vs blocking i/o).
 */
public interface IoInit {
    /**
     * Call this method first, before calling startup().
     */
    public void init(FiveProps servProps);

    /**
     * Call this to create the correct socket factory.
     */
    public ServerSocketFactory makeServerSocketFactory();

    /**
     * Call this on the startup of a ServerThread object.
     */
    public void startup();

    /**
     * Call this when we need to open a conn.
     */
    public void openConn(ConnThreadBase conn, SocketWrapper socket);
}
