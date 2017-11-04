package com.enjoyf.platform.serv.thrserver;

import java.net.InetAddress;

import com.enjoyf.platform.util.FiveProps;

public interface ServerSocketFactory {
    /**
     * Creates a ServerSocketWrapper from a port number.  What the ServerSocketWrapper
     * wraps around depends on the implementation of the factory.
     *
     * @param port  The port we want to bind our socket to.
     * @param props Unstructured props to be interpreted by the various
     *              implementations.
     */
    public ServerSocketWrapper createSocket(int port, FiveProps props);

    /**
     * Variant to specify an alternative IP.
     *
     * @param iaddr       The ip on which to listen on.
     * @param port        The port on which to listen on.
     * @param maxBlockers How many connect requests to allow to queue
     *                    up before the underlying jdk returns an error to the client.
     *                    This is primarily here because the java api which takes an
     *                    InetAddress also requires it.
     * @param props       Unstructured props to be interpreted by the various
     *                    implementations.
     */
    public ServerSocketWrapper createSocket(InetAddress iaddr, int port, int maxBlockers, FiveProps props);
}
