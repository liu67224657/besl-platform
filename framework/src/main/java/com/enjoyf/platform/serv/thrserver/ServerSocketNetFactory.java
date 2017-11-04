/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;

public class ServerSocketNetFactory implements ServerSocketFactory {
    /**
     * Creates a ServerSocketWrapper containing a ServerSocket bound to the specified port.
     *
     * @param port The port we want to bind our socket to.
     */
    public ServerSocketWrapper createSocket(int port, FiveProps props) {
        //ServerSocket s = null;

        return p_createSocket(port, null, 0, props);
    }

    private ServerSocketWrapper p_createSocket(int port, InetAddress iaddr, int maxBlockers, FiveProps props) {
        ServerSocket s = null;
        
        try {
            if (iaddr != null) {
                s = new ServerSocket(port, maxBlockers, iaddr);
            } else {
                s = new ServerSocket(port);
            }
        }
        catch (IOException e) {
            GAlerter.lab("ServerSocketNetFactory.createSocket(): Exception creating listen socket: ", e);
        }
        catch (IllegalArgumentException e) {
            GAlerter.lab("ServerSocketNetFactory.creatSocket(): Exception creating listen socket: ", e);
        }

        return new ServerSocketWrapperIo(s, props);
    }

    /**
     * Variant to specify an alternative IP.
     *
     * @param iaddr       The ip on which to listen on.
     * @param port        The port on which to listen on.
     * @param maxBlockers How many connect requests to allow to queue
     *                    up before the underlying jdk returns an error to the client.
     *                    This is primarily here because the java api which takes an
     *                    InetAddress also requires it.
     */
    public ServerSocketWrapper createSocket(InetAddress iaddr, int port, int maxBlockers, FiveProps props) {
        return p_createSocket(port, iaddr, maxBlockers, props);
    }
}
