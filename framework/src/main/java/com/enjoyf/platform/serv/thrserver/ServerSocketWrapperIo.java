/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.KeepAlive;

//import java.nio.channels.ServerSocketChannel;
//import java.nio.channels.SocketChannel;

/**
 * The concrete implementation for blocking io.
 */

public class ServerSocketWrapperIo extends ServerSocketWrapperDef {
    /**
     * A property is used to determine whether or not we want keepalive
     * turned on for server-side sockets in the serv/thrserver framework.
     * Will default to true, so it needs to be disabled.
     */
    private boolean keepAliveFlag;

    /**
     * A property is used to determine whether there is tcp delay
     * (Nagle's Algorithm) is needed for network optimization.
     * Setting it on the server side seems sufficient enough to speed
     * up the response time for returning result.
     */
    private boolean tcpNoDelayFlag;

    ///////////////////////////////////////////////////////
    public ServerSocketWrapperIo(ServerSocket socket, FiveProps props) {
        tcpNoDelayFlag = props.getBoolean("server.tcpNoDelay", false);
        keepAliveFlag = props.getBoolean("server.keepalive", true);

        serverSocket = socket;
    }

    /**
     * Perform the accept() call on the ServerSocket object.
     *
     * @throws IOException if there is a network error.
     */
    public SocketWrapper accept() throws IOException {
        if (serverSocket == null) {
            throw new IllegalStateException("ServerSocketWrapperIo: ServerSocket not initialized!");
        }

        Socket sock = serverSocket.accept();
        if (keepAliveFlag) {
            KeepAlive.setKeepAlive(sock, true);
        }

        if (tcpNoDelayFlag) {
            sock.setTcpNoDelay(true);
        }

        return new SocketWrapperIo(sock);
    }
}
