package com.enjoyf.platform.serv.thrserver;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.io.WPacketBase;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.collection.ObjMgrBase;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThread;
import com.enjoyf.platform.util.thin.DieThread;

/**
 * Utility class to send WPacket objects over ConnThreadBase objects
 * on a separate thread. One or more send threads can be used.
 * The class will check for wedges as well.
 */
public class QueuedSender {
    
    private static final Logger logger = LoggerFactory.getLogger(QueuedSender.class);
    
    private SendContext[] senders;
    private ObjMgrBase wedgedConns = null;
    private boolean alertWedgesFlag = false;
    private String wedgeTag = "anon";

    /**
     * Ctor the object. One thread is used to send out all messages.
     *
     * @param wedgeCheckInterval Wedge checking is performed on a
     *                           separate thread, so this is how often to wake up and check
     *                           for a wedged conn, in msecs. If 0, no wedge checking is done.
     * @param wedgeTimeout       This is the timeout for a send, in msecs.
     */
    public QueuedSender(int wedgeCheckInterval, int wedgeTimeout) {
        this(wedgeCheckInterval, wedgeTimeout, 1);
    }

    /**
     * @param wedgeCheckInterval Wedge checking is performed on a
     *                           separate thread, so this is how often to wake up and check
     *                           for a wedged conn, in msecs. If 0, no wedge checking is done.
     * @param wedgeTimeout       This is the timeout for a send, in msecs.
     * @param nthreads           The number of send threads to use. Note that
     *                           all msgs destined for a particular conn go out on the same thread.
     */
    public QueuedSender(int wedgeCheckInterval, int wedgeTimeout, int nthreads) {
        //--
        // Store conns that are wedged so that we don't  try to send
        // to them. We really don't know when we should get rid of
        // them, so use an obj mgr to expire the contents after
        // an hour.
        //--
        wedgedConns = new ObjMgrBase(30 * 60, 60 * 60);

        senders = new SendContext[nthreads];
        for (int i = 0; i < nthreads; i++) {
            senders[i] = new SendContext(wedgeTimeout);
        }

        // checker needs to start after the SendContext objects are created
        if (wedgeCheckInterval != 0) {
            WedgeChecker checker = new WedgeChecker(wedgeCheckInterval);
            checker.start();
        }

    }

    /**
     * Set a wedge tag so that one can distinguish between different
     * wedge messages coming from different objects.
     */
    public void setWedgeTag(String tag) {
        wedgeTag = tag;
    }

    /**
     * If true is passed in, wedges are sent to the DEBUG alert bucket.
     */
    public void alertWedges(boolean alertWedges) {
        alertWedgesFlag = alertWedges;
    }

    /**
     * Send out a message on an internal thread.
     */
    public void send(Message msg) {
        if (msg.getConn() == null) {
            return;
        }

        //--
        // Use the hashcode to make sure all msgs destined for one
        // particular conn end up on the same sender.
        //--
        int idx = Math.abs(msg.getConn().hashCode()) % senders.length;
        senders[idx].send(msg);
    }

    /**
     * Return the total number of objects in the queue.
     */
    public int size() {
        int totSize = 0;
        for (int i = 0; i < senders.length; i++) {
            totSize += senders[i].size();
        }

        return totSize;
    }

    public String getSizeStr() {
        StringBuffer sb = new StringBuffer();
        int totSize = 0;
        for (int i = 0; i < senders.length; i++) {
            sb.append(senders[i].size() + "/");
            totSize += senders[i].size();
        }
        sb.append("totSize=" + totSize);
        return new String(sb);
    }

    /**
     * This interface can be implemented by callers that want to be
     * more specific about what kind of Message object they'd like to
     * use.
     */
    interface Message {
        public ConnThreadBase getConn();

        public WPacketBase getPacket();
    }

    /**
     * This is a default implementation of the Message interface which
     * is just an encapsulator of a conn and a packet.
     */
    public static class MessageDef implements Message {
        private ConnThreadBase connThreadBase;
        private WPacketBase wPacket;

        public MessageDef(ConnThreadBase conn, WPacketBase wp) {
            connThreadBase = conn;
            wPacket = wp;
        }

        public ConnThreadBase getConn() {
            return connThreadBase;
        }

        public WPacketBase getPacket() {
            return wPacket;
        }

        public String toString() {
            return connThreadBase.toString();
        }
    }

    boolean p_isWedgedConn(ConnThreadBase conn) {
        return wedgedConns.get(conn) != null;
    }

    class SendContext {
        private long sendTime = 0;
        private boolean sendingFlag = false;
        private int wedgeTimeout = 30 * 1000;
        private ConnThreadBase curConn = null;
        QueueThread queueThread;

        public SendContext(int wedgeTimeout) {
            this.wedgeTimeout = wedgeTimeout;
            queueThread = new QueueThread(
                    new QueueListener() {
                        public void process(Object obj) {
                            p_process((Message) obj);
                        }
                    }
            );
        }

        int size() {
            return queueThread.size();
        }

        void send(Message msg) {
            queueThread.add(msg);
        }

        private void p_process(Message msg) {
            ConnThreadBase conn = msg.getConn();
            //--
            // Just return if we have determined that the conn is bad.
            //--
            if (!conn.isAlive() || conn.isBad()) {
                return;
            }

            if (p_isWedgedConn(conn)) {
                if (logger.isTraceEnabled()) {
                    logger.trace("QueuedSender.logicProcess: Tossing message "
                        + "because destination got wedged: " + msg);
                }
                return;
            }
            sendTime = System.currentTimeMillis();
            curConn = conn;
            sendingFlag = true;
            try {
                conn.writeInternal(msg.getPacket());
            }
            catch (IOException ioe) {
                logger.warn("QueuedSender.p_sendNx: conn=" + conn
                        + ":exc = " + ioe + " msg = " + msg);
            }
            catch (Exception e) {
                logger.error("QueuedSender.p_sendNx: conn=" + conn
                        + ":exc = " + e + " msg = " + msg);
            }
            sendingFlag = false;
            curConn = null;
        }


        synchronized ConnThreadBase checkWedge() {
            if (!p_isWedged()) {
                return null;
            }

            //--
            // The synch on the queue guarantees that no new msgs will
            // arrive at logicProcess().
            //--

            ConnThreadBase conn = null;

            synchronized (queueThread) {
                //--
                // A quick check to see if the request actually completed.
                //--
                conn = curConn;
                if (conn == null) {
                    return null;
                }

                String msg = "QueuedSender: FOUND A WEDGE! " + conn.toString();
                if (alertWedgesFlag) {
                    logger.trace(wedgeTag + ":" + msg);
                } else {
                    logger.trace(msg);
                }

                wedgedConns.put(conn, conn);
                logger.debug("QueuedSender.Number of conns in wedge list = "
                        + wedgedConns.getCount());
            }
            return conn;
        }

        private boolean p_isWedged() {
            return sendingFlag &&
                    (System.currentTimeMillis() - sendTime) > wedgeTimeout;
        }
    }

    class WedgeChecker extends DieThread {
        private int m_wedgeCheckInterval;

        public WedgeChecker(int wedgeCheckInterval) {
            setName("WedgeChecker:" + getName());
            m_wedgeCheckInterval = wedgeCheckInterval;
        }

        public void run() {
            while (!shouldDie()) {
                Utility.sleepExc(m_wedgeCheckInterval);
                p_checkWedge();
            }
        }

        private void p_checkWedge() {
            //--
            // Loop through all the SendContext objects looking for
            // wedges.
            //--
            for (int i = 0; i < senders.length; i++) {
                ConnThreadBase conn = senders[i].checkWedge();
                if (conn != null) {
                    logger.warn("QueuedSender.WedgeChecker:p_checkWedge: "
                            + " Killing conn because of wedge: " + conn);
                    conn.die();
                }
            }
        }
    }
}
