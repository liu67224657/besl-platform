/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.io.IOException;

import com.enjoyf.platform.io.DataInputCustom;
import com.enjoyf.platform.io.DataOutputCustom;
import com.enjoyf.platform.io.WPacketBase;

/**
 * This is the base interface for all classes representing a socket connection.
 * Its basic methods specify that we need to be able to read and write from/to the socket
 * we are connected to, and to register ConnDieListeners that are invoked when
 * the connection dies.
 * <p/>
 * The name of the class is for backwards compatibility, when ConnThreadBase was an
 * abstract class which extended Thread.  Since the thread-per-conn functionality is
 * no longer guaranteed to be in use, that functionality has been decoupled from ConnThreadBase.
 * This class should ideally be called ConnBase, but a rename was not done in order
 * to avoid some massive renaming in all the client code.
 */
public interface ConnThreadBase {
    /**
     * Call to kill this connection
     */
    public void die();

    /**
     * Returns true if the connection is alive; false otherwise.  If the connection is in
     * the logicProcess of dieing, this will return false.
     *
     * @return True if the connection is still active, false otherwise.
     */
    public boolean isAlive();

    /**
     * Returns true if the conn has been determined to be "bad", and is
     * not usable anymore. This doesn't mean it has been closed yet, it
     * could still be closed with the attendant invocation of the
     * ConnDieListeners.
     */
    public boolean isBad();

    /**
     * The initializer is called when the connection is established.  Threaded implementations
     * will likely want to start their threads in this call
     */
    public void start();

    /**
     * This method should be implemented to read something.
     * Note how it doesn't deal with any sort of an object,
     * the read method should forward whatever it reads to
     * some listener object.
     *
     * @return true if we want to keep this connection alive, false otherwise.
     */
    public boolean read();

    /**
     * In contrast, the write method does take the object that it
     * is going to write. This object can be anything and implementations
     * may expect a specific subclass of Object.
     *
     * @throws IOException thrown if there is any problem
     *                     with the write.
     */
    public void write(Object obj) throws IOException;

    /**
     * This method is provided for internal implementors of WriteStrategy
     * objects. Consider the 'write' method to be the logical write, and
     * this one to be the 'physical' write. While this method is public
     * due to interface limitations, it should be considered package
     * private.
     */
    public void writeInternal(WPacketBase wp) throws IOException;

    /**
     * Return a String form of the ip address.
     */
    public String getIp();

    /**
     * Returns the DataInputCustom this Conn uses for reading.
     * Primarily for allowing NIO mechanisms to fill the buffer with data,
     * Clients shouldn't be reading out of this object directly.
     *
     * @return The DataInput we read from.
     */
    public DataInputCustom getDataInputCustom();

    /**
     * Returns the DataOutputCustom this Conn uses for writing.
     * Primarily for allowing NIO mechanisms to get data from the buffer.
     * Clients shouldn't be writing to this object directly.
     *
     * @return The DataOutput we write to.
     */
    public DataOutputCustom getDataOutputCustom();

    /**
     * Add a listener object for when a connection dies.
     *
     * @param l The listener object to register.
     */
    public void addConnDieListener(ConnDieListener l);

    /**
     * Remove a previously registered listener object.
     *
     * @param l The previously registered listener object.
     *          The equals() method on the object will be used to
     *          find it in our container.
     */
    public void removeConnDieListener(ConnDieListener l);

    /**
     * Invokes the currently registered die listeners.
     * Should only be called by whatever thread is managing this conn,
     * or behavior could be unpredictable.
     */
    public void invokeDieListeners();
}
