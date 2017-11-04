/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.net.ServerSocket;

/**
 * Encapsulates the common behavior across nio and blocking io.
 */
public abstract class ServerSocketWrapperDef implements ServerSocketWrapper {
    protected ServerSocket serverSocket = null;

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
