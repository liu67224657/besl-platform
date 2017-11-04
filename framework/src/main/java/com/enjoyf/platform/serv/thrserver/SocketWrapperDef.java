/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import com.enjoyf.platform.io.DataInputCustom;
import com.enjoyf.platform.io.DataOutputCustom;

/**
 * An abstract class implementing common behavior across both nio/io.
 */

public abstract class SocketWrapperDef implements SocketWrapper {
    protected int port = 0;
    protected int localPort = 0;
    protected DataInputCustom in = null;
    protected DataOutputCustom out = null;
    protected InetAddress inetAddress = null;
    protected InetAddress localAddress = null;
    protected Socket socket = null;

    /**
     * Close the underlying socket.
     *
     * @throws IOException if the underlying socket throws an IOException
     */
    public void close() throws IOException {
        Socket sock = p_getSocket();

        if (sock == null) {
            return;
        }

        sock.close();
    }

    /**
     * Returns the DataInput for reading from this socket
     *
     * @return a DataInput object for reading from this socket
     */
    public DataInputCustom getDataInputCustom() {
        return in;
    }

    /**
     * Returns the DataOutput for writing to this socket
     *
     * @return a DataOutput object for writing to this socket
     */
    public DataOutputCustom getDataOutputCustom() {
        return out;
    }

    /**
     * Returns the InetAddress for this SocketWrapper
     *
     * @return The InetAddress for the underlying socket
     */
    public InetAddress getInetAddress() {
        return inetAddress;
    }

    /**
     * Returns the LocalAddress for this SocketWrapper
     *
     * @return The LocalAddress for the underlying socket
     */
    public InetAddress getLocalAddress() {
        return localAddress;
    }

    /**
     * Returns the port for this SocketWrapper
     *
     * @return the port number for the underlying socket
     */
    public int getPort() {
        return port;
    }


    /**
     * Returns the local port for this SocketWrapper
     *
     * @return the local port number for the underlying socket
     */
    public int getLocalPort() {
        return localPort;
    }


    /**
     * Returns the Socket this object is wrapped around, or null if this
     * object is not wrapped around a socket (io or nio implementations
     * of server code should explicitly expect the wrapper to be around
     * a certain object).
     *
     * @return The Socket we are wrapped around
     */
    private Socket p_getSocket() {
        return socket;
    }

    /**
     * Set the various address-type member vars from a Socket.
     */
    protected void p_setAddresses(Socket socket) {
        if (socket == null) {
            return;
        }

        port = socket.getPort();
        inetAddress = socket.getInetAddress();
        localAddress = socket.getLocalAddress();
        localPort = socket.getLocalPort();
    }
}
