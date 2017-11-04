/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.io.IOException;
import java.net.InetAddress;

import com.enjoyf.platform.io.DataInputCustom;
import com.enjoyf.platform.io.DataOutputCustom;

/**
 * A wrapper abstraction around a Socket-like object.  The abstraction
 * is to support both the java.io and java.nio schemes of doing socket
 * connections, since we may be accepting either object from our server.
 */

public interface SocketWrapper {
    /**
     * Close the underlying socket.
     *
     * @throws IOException Thrown if there is a problem with the
     *                     socket.
     */
    public void close() throws IOException;

    /**
     * Returns the DataInput for reading from this socket
     *
     * @return a DataInput object for reading from this socket
     */
    public DataInputCustom getDataInputCustom();

    /**
     * Returns the DataOutput for writing to this socket
     *
     * @return a DataOutput object for writing to this socket
     */
    public DataOutputCustom getDataOutputCustom();

    /**
     * @return The InetAddress for the underlying socket
     */
    public InetAddress getInetAddress();

    /**
     * Returns the LocalAddress for this SocketWrapper
     *
     * @return The LocalAddress for the underlying socket
     */
    public InetAddress getLocalAddress();

    /**
     * Returns the port for this SocketWrapper
     *
     * @return the port number for the underlying socket
     */
    public int getPort();


    /**
     * Returns the local port for this SocketWrapper (the listening port)
     *
     * @return the port number for the underlying socket
     */
    public int getLocalPort();

}
