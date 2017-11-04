/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.nio.channels.SocketChannel;

/**
 * A class to represent a registration request for a selection operation.
 */
public class NioRegistrationReq {
    private int op = 0;
    private ConnThreadBase conn = null;
    private SocketChannel channel = null;

    /**
     * Constructor
     *
     * @param op      The selection operation to register for.
     * @param conn    The ConnThreadBase managing the socket.
     * @param channel The SocketChannel we are physically registering.
     */
    public NioRegistrationReq(int op, ConnThreadBase conn, SocketChannel channel) {
        this.op = op;
        this.conn = conn;
        this.channel = channel;
    }

    /**
     * Returns the operation we are registering for (from SelectionKey).
     *
     * @return The int code of the selection operation we want to register for.
     */
    public int getOp() {
        return op;
    }

    /**
     * Returns the ConnThreadBase managing the socket we are registering (necessary
     * because we need to be able get the ConnThreadBase given an arbitrary SocketChannel).
     */
    public ConnThreadBase getConn() {
        return conn;
    }

    /**
     * Returns the SocketChannel we are physically trying to register.
     */
    public SocketChannel getSocketChannel() {
        return channel;
    }
}
