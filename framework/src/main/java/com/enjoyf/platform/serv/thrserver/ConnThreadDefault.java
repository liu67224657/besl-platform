package com.enjoyf.platform.serv.thrserver;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.io.DataInputCustom;
import com.enjoyf.platform.io.DataOutputCustom;
import com.enjoyf.platform.io.NotEnoughDataException;
import com.enjoyf.platform.io.PacketReaderBase;
import com.enjoyf.platform.io.PacketTooLargeException;
import com.enjoyf.platform.io.PacketWriterBase;
import com.enjoyf.platform.io.RPacketBase;
import com.enjoyf.platform.io.TooMuchDataException;
import com.enjoyf.platform.io.WPacketBase;
import com.enjoyf.platform.util.IpUtil;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * The default implementation of ConnThreadBase.  Can be used as a threaded or
 * non-threaded Conn, depending on whether a Thread member has been set.
 */
public class ConnThreadDefault implements ConnThreadBase {

    private static final Logger logger = LoggerFactory.getLogger(ConnThreadDefault.class);

    protected boolean dieFlag = false;
    private boolean badFlag = false;

    protected PacketHandler packetHandler = null;
    protected PacketReaderBase packetReader = null;
    protected PacketWriterBase packetWriter = null;
    protected SocketWrapper socketWrapper = null;
    private WriteStrategy writeStrategy = new WriteStrategyDef();

    protected String ip = null;
    protected String toString = "";

    //
    protected Thread watchThread = null;
    protected Vector<ConnDieListener> dieContainer = new Vector<ConnDieListener>(3);

    /**
     * Constructor.  Does not specify a thread for this conn.
     *
     * @param clientSocket The socket this conn is handling.
     * @param pw           The object to use for writing packets.
     * @param pr           The object to use for reading packets.
     * @param ph           The object to use for handling packets.
     */
    public ConnThreadDefault(SocketWrapper clientSocket, PacketWriterBase pw, PacketReaderBase pr, PacketHandler ph) {
        this(clientSocket, pw, pr, ph, null);
    }

    /**
     * Constructor.
     *
     * @param clientSocket The socket this conn is handling.
     * @param pw           The object to use for writing packets.
     * @param pr           The object to use for reading packets.
     * @param ph           The object to use for handling packets.
     * @param runThread    A thread to run for this connection.  Only necessary in thread-per-conn schemes.
     */
    public ConnThreadDefault(SocketWrapper clientSocket, PacketWriterBase pw, PacketReaderBase pr, PacketHandler ph, Thread runThread) {
        socketWrapper = clientSocket;
        packetWriter = pw;
        packetReader = pr;
        packetHandler = ph;
        watchThread = runThread;

        if (socketWrapper != null) {
            //--
            // Cache the output of toString().
            //--
            StringBuffer sb = new StringBuffer();
            sb.append(getIp());
            sb.append(":");
            sb.append(socketWrapper.getPort());
            toString = new String(sb);
        }
    }

    public void setWriteStrategy(WriteStrategy writeStrategy) {
        if (writeStrategy == null) {
            String errmsg = "ConnThreadDefault. WriteStrategy is null!";
            GAlerter.lab(errmsg);
            throw new IllegalArgumentException(errmsg);
        }
        this.writeStrategy = writeStrategy;
    }

    /**
     * Add a listener object for when a connection dies.
     *
     * @param l listener object to register.
     */
    public void addConnDieListener(ConnDieListener l) {
        dieContainer.addElement(l);
    }

    /**
     * Called when we want to kill this connection.
     */
    public synchronized void die() {
        if (dieFlag) {
            return;
        }

        // Set our flag so we don't call this more than once
        dieFlag = true;

        if (Thread.currentThread().equals(watchThread)) {
            return;
        }

        // close the socket - this should cause all further read() calls on this Connection to fail
        closeSocket();

        //if(watchThread != null) {
        //	watchThread.interrupt();
        //}
    }

    /**
     * Return a String form of the ip address.
     *
     * @return The ip address as a string.
     */
    public String getIp() {
        if (socketWrapper == null) {
            return "UNKNOWN";
        }

        if (ip != null) {
            return ip;
        }

        ip = IpUtil.cvtToString(socketWrapper.getInetAddress().getAddress());
        return ip;
    }

    /**
     * Return the source port of the underlying socket connection
     *
     * @return the source port of the underlying socket connection or -1 if no socket.
     */
    public int getPort() {
        if (socketWrapper == null) {
            return -1;
        } else {
            return socketWrapper.getPort();
        }
    }

    /**
     * Returns the DataInputCustom from which we read.
     * Be careful, as the input is live and any reads from it will
     * disrupt reads by this object.  We need to return this object,
     * because under the NIO scheme the input is really a buffer, and
     * we need some method to fill the buffer with data as we get it.
     *
     * @return The DataInputCustom this conn reads from.
     */
    public DataInputCustom getDataInputCustom() {
        if (socketWrapper != null) {
            return socketWrapper.getDataInputCustom();
        }

        return null;
    }

    /**
     * Returns the DataOutputCustom to which we write.
     * Be careful, as the output is live and any writes to it will
     * send data out to the client in an unpredictable fashion (as we
     * may be writing to it concurrently).  We need to return this object,
     * because under the NIO scheme the output is really a buffer, and
     * we need some method to get data out of the buffer as the socket
     * becomes available for writing.
     *
     * @return The DataOutputCustom we write to.
     */
    public DataOutputCustom getDataOutputCustom() {
        if (socketWrapper != null) {
            return socketWrapper.getDataOutputCustom();
        }

        return null;
    }


    public boolean isBad() {
        return badFlag;
    }

    /**
     * Returns true if the connection is alive; false otherwise.
     *
     * @return True if the connection is still active, false otherwise.
     */
    public boolean isAlive() {
        if (dieFlag) {
            return false;
        }

        return true;
    }

    /**
     * Reads an incoming packet from the socket, and handles it.
     * If a write packet is returned from the handling routine, it
     * will write it back out to the socket.
     * Note how it doesn't deal with any sort of an object,
     * the read method should forward whatever it reads to
     * its PacketHandler.
     *
     * @return Should return true if you want to keep the connection
     *         alive, and false, if you want the thread to exit.
     */
    public boolean read() {
        RPacketBase rp = null;
        DataInputCustom in = this.getDataInputCustom();

        // if we don't have a valid stream, return false to exit the thread.
        // this will happen if we attempt to use the connection after it
        // was closed but before
        if (in == null) {
            return false;
        }

        //read the packet from input stream.
        //step one.
        try {
            rp = packetReader.readFrom(in);
            in.setReturnPoint();
        } catch (NotEnoughDataException e) {
            // we get this in NIO mode, when there's not enough data for a full packet
            // in this case, we reset the reading point in the buffer, return true, and hope
            // that the next time we find data on our socket, we'll have enough for a packet.
            try {
                in.toReturnPoint();
            } catch (Exception e2) {
                //
            }

            return true;
        } catch (PacketTooLargeException pe) {
            logger.error(pe.toString() + " could be a bug, somebody trying to hack, or a bad link to a game from a 3rd party site");
            return false;
        } catch (IOException e) {
            GAlerter.lan("Error in ConnThreadDefault.read()", e);
            return false;
        } catch (Exception e) {
            logger.error("Error in ConnThreadDefault.read()", e);
            return false;
        }

        //handler the inout.
        //step two.
        WPacketBase wp = packetHandler.handle(this, rp);

        //write the result back to client.
        if (wp != null) {
            try {
                writeStrategy.write(this, wp);
            } catch (TooMuchDataException e) {
                logger.error("DataOutputStreamWrapperNIOBuffer filled to max, cannot write");
                return false;
            } catch (Exception e) {
                logger.error("ConnThreadDefault.read(): Exception writing response packet to " + this);
                return false;
            }
        }

        //return true if the process is ok.
        return true;
    }

    /**
     * Remove a previously registered listener object.
     *
     * @param l The previously registered listener object.
     *          The equals() method on the object will be used to
     *          find it in our container.
     */
    public void removeConnDieListener(ConnDieListener l) {
        dieContainer.removeElement(l);
    }

    /**
     * Sets the thread to run for this conn
     *
     * @param t A thread to start up when this conn starts.
     */
    public void setThread(Thread t) {
        watchThread = t;
    }

    /**
     * Signals that this conn is ready for operation
     */
    public void start() {
        // if our conn has a thread, we start it here.
        if (watchThread != null) {
            watchThread.start();
        }
    }

    /**
     * Returns the string form of the connection
     *
     * @return A display string for the connection.  Mostly for logging.
     */
    public String toString() {
        return toString;
    }

    public int hashCode() {
        return getIp().hashCode() * 100000 + getPort();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ConnThreadDefault)) {
            return false;
        }

        ConnThreadDefault conn = (ConnThreadDefault) obj;

        return getIp().equals(conn.getIp()) && getPort() == conn.getPort();
    }

    /**
     * The write method takes the object that it is going to write.
     * This object should really be a WPacketBase, but due to "historical"
     * reasons it's an Object.
     *
     * @param obj The object to write out to this connection.
     * @throws IOException thrown if there is any problem with the write.
     */
    public void write(Object obj) throws IOException {
        WPacketBase wp = (WPacketBase) obj;
        writeStrategy.write(this, wp);
    }

    /**
     * The strategy object will call back to this method
     * to perform the write. WriteStrategy implementations should
     * reside in this package so that the method is accessible.
     */
    public void writeInternal(WPacketBase wp) throws IOException {
        DataOutputCustom out = this.getDataOutputCustom();
        if (out == null) {
            badFlag = true;
            throw new IOException("No valid data output stream");
        }

        try {
            packetWriter.writeTo(out, wp);
            if (logger.isTraceEnabled()) {
                logger.trace("Sent a packet of length = " + wp.length() + "\npacket = " + wp);
            }
        } catch (IOException ioe) {
            badFlag = true;
            throw ioe;
        }
    }

    /**
     * Call to invoke all the registered die listeners.
     */
    public void invokeDieListeners() {
        // Call the registered listeners
        Enumeration<ConnDieListener> itr = dieContainer.elements();
        while (itr.hasMoreElements()) {
            ConnDieListener l = itr.nextElement();
            l.notify(this);
        }
    }

    /**
     * Called internally to close our socket
     */
    private void closeSocket() {
        if (socketWrapper != null) {
            try {
                socketWrapper.close();
            }
            catch (Exception e) {
                logger.error("Cannot close socket", e);
            }
        }
    }


    public static class RunThread extends Thread {
        private ConnThreadDefault connection = null;

        /**
         * Ctor with a ConnThreadBase to use
         */
        public RunThread(ConnThreadDefault conn) {
            connection = conn;
            setName("ConnThreadDefault:" + connection.getIp() + ":"
                    + (connection.socketWrapper != null ?
                    Integer.toString(connection.socketWrapper.getPort()) : "")
                    + ":" + connection.hashCode());
        }

        /**
         * Called when we want to kill this thread.
         */
        public void die() {
            // if we're being called by some other thread, give our thread a nudge.
            if (Thread.currentThread().equals(this)) {
                return;
            }

            this.interrupt();
        }

        /**
         * The body of the thread.
         */
        public void run() {
            if (logger.isTraceEnabled()) {
                logger.trace("RunThread.run(): Conn. established to client, listening for packets: " + connection);
            }

            boolean ok = true;

            try {
                while (ok) {
                    //------------------------------------------------
                    // Don't bother looping if our conn is dead
                    //------------------------------------------------
                    if (!connection.isAlive()) {
                        break;
                    }

                    //all the process logic is here.
                    //the thread just loop to read the input from the client.
                    ok = connection.read();
                }
            } catch (Throwable t) {
                // Log socket closing errors/exceptions to the alert service.
                if (!(t instanceof java.io.IOException)) {
                    GAlerter.lan("ConnThreadDefault.read: error: " + t, t);
                }
            } finally {
                // Note that we'll get here and close the socket
                // even if an exception is thrown.
                if (logger.isTraceEnabled()) {
                    logger.trace("RunThread.run: Connection thread exiting: " + this);
                }
                // Close the socket.
                connection.closeSocket();
                // Invoke the die listeners.
                connection.invokeDieListeners();
            }
        }
    }
}
