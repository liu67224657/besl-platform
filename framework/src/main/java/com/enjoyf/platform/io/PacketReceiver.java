/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.DataInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.thread.PoolThread;

/**
 * Package private class.
 * <p/>
 * A class to encapsulate the reader thread for advttl. The Transactor
 * class will create one of these threads and be notified by it whenever
 * a packet has been read.
 *
 * @see Transactor
 */
public class PacketReceiver extends PoolThread {
	
	private static final Logger logger = LoggerFactory.getLogger(PacketReceiver.class);
	
    public boolean stopFlag = false;
    public DataInputStream inputStream;
    public PacketListener packetListener = null;
    public PacketReader packetReader;
    private int connectionId;
    private Connection connection;

    /**
     * The constructor for the reader thread. This constructor
     * will start the thread. The connection better be established
     * when calling this constructor, since it will immediately
     * begin running and try to read from the transactors connection.
     *
     * @see Connection
     */
    public PacketReceiver(Connection conn, PacketReader pr) {
        connection = conn;
        inputStream = conn.getInputStream();
        packetReader = pr;
        setName("PacketReceiver:" + this.connection.toString());
    }

    /**
     * Add a listener object. All listener objects should be
     * added prior to starting the thread. Note that there is
     * no way to remove them once added.
     *
     * @param l The listener object to add.
     * @see PacketListener
     */
    public void setPacketListener(PacketListener l) {
        packetListener = l;
    }

    /**
     * Tell the thread to stop running at it's first opportunity.
     */
    public synchronized void setStop() {
        stopFlag = true;
    }

    /**
     * Set the connection id.
     */
    public void setConnectionId(int connId) {
        connectionId = connId;
    }

    /**
     * The reader thread for advttl. This thread will just block
     * on a read call until a packet comes in.
     */
    public void run() {
        logger.debug("PacketReceiver.run(): Thread is starting");

        RPacketBase rp = null;
        try {
            while (!stopFlag) {
                rp = packetReader.readFrom(new DataInputStreamWrapper(inputStream));
                if (rp != null) {
                    rp.setSource(connection.getIp());
                }

                if (logger.isTraceEnabled()) {
                	logger.trace("PacketReceiver thread: packet read: " + rp);
                }
                //--
                // Notify the transactor that we have received a
                // packet. If here, we are not in error.
                //--

                p_packetNotify(rp);
            }
        }
        catch (Throwable e) {
            //--
            // This tells the transactor that
            // we are in error, and to do the "right
            // thing". This means disconnect, and
            // wake up all waiting threads.
            //--

            logger.error("ReaderThread: Throwable", e);
            p_errNotify();
        }
        
        logger.info("PacketReceiver.run(): Thread exiting");
    }

    public void p_errNotify() {
        if (packetListener == null) {
            logger.error("PacketReceiver.p_errNotify(): No listeners registered!");
            return;
        }
        
        packetListener.errorNotify(connection);
    }

    public void p_packetNotify(RPacketBase p) {
        if (packetListener == null) {
            logger.warn("PacketReceiver.p_packetNotify(): No listeners registered!");
            return;
        }
        packetListener.packetArrived(p);
    }
}
