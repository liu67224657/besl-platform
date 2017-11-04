/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * A wrapper class around a ServerSocket-like object.  This is to support
 * both the java.io and java.nio schemes of doing socket connections,
 * since we may be using either a ServerSocket or a ServerSocketChannel.
 */

public interface ServerSocketWrapper {

    public ServerSocket getServerSocket();

    /**
     * Accept on the socket.
     *
     * @return Returns a SocketWrapper object representing the socket
     *         in question.
     * @throws IOException, If the accept() call fails.
     */
    public SocketWrapper accept() throws IOException;
}
