package com.enjoyf.platform.io;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.collection.CircularQueue;

/**
 * Transaction layer to be used by anybody wanting to access the backend.
 * This interface provides a couple of ways of sending packets, and 3
 * ways of receiving them.
 * <p/>
 * syncRequest()	- Sends a packet and waits for a response. Multiple
 * threads can all be executing this function at
 * the same time.
 * sendAndForget() - Sends a packet without waiting for a response.
 * getEvent()	- Call this routine in a thread to listen for
 * backend generated events. The routine will return
 * with a received packet. Dispatch the packet and
 * call the routine again in a loop.
 * addTypeListener()/react().
 * - Call the first to register listeners for particular
 * packet types, and the second in a thread to wait
 * forever, dispatching any received packets to the
 * appropriate listener.
 * <p/>
 * The order of processing for a received packet is:
 * <p/>
 * - If the packet's type matches that of some registered listener,
 * then the callback is invoked in the react() thread. Nothing
 * more is done with this packet.
 * <p/>
 * - If the packet has a tid of 0, it is assumed to be a backend
 * generated event and will be returned via a getEvent() call.
 * If no getEvent() call is in progress, the events queue up
 * in a circular queue. Nothing more is done with this packet.
 * <p/>
 * - Finally, an attempt is made to match the tid of the received
 * packet with the tid of a sent packet. If successful, then
 * the syncRequest() call for the sent packet returns with the
 * received packet.
 */
public class Transactor implements PacketListener {
	
	private static final Logger logger = LoggerFactory.getLogger(Transactor.class);
    private Hashtable<Integer, TypePacketListener> packetTypeListeners = new Hashtable<Integer, TypePacketListener>();
    private CircularQueue eventPackets = null;
    private CircularQueue typedPackets = null;

    //--
    // Dummy objects for condition vars.
    //--
    private Object eventCondition = new PendingResponse(0);
    private Object reactCondition = new PendingResponse(0);

    private Pending pending = null;
    private int nextTid = 1;

    //
    private Connection connection = null;
    private PacketReceiver packetReceiver = null;
    private PacketReader packetReader;
    private PacketWriter packetWriter;

    /**
     * Under most conditions, use "normal" reader threads. A client
     * could override the reader factory by calling setReaderFactory()
     */
    private PacketReceiverFactory packetReceiverFactory = new PacketReceiverNormalFactory();
    private String receiverName = null;

    private static boolean keepAliveFlag = false;

    /**
     * Set to true when the connection has been shutdown.
     */
    private boolean shutdownFlag = false;

    static {
        keepAliveFlag = Props.instance().getEnvProps().getBoolean("transactor.keepalive", false);
    }


    /**
     * Constructs a Transactor object. While you can create a Transactor
     * object directly, it is probably best to use one of the factory
     * classes.
     *
     * @param pr A PacketReader object. This object implements
     *           the reading semantics. Typically this means
     *           protocol munging.
     * @param pw A PacketWriter object. This is used to write a
     *           packet. It implements the writing semantics.
     *           Typically this means protocol munging.
     */
    public Transactor(PacketReader pr, PacketWriter pw) {
        packetReader = pr;
        packetWriter = pw;
    }

    /**
     * Set a name for the ReaderThread.
     */
    public void setReceiverName(String rdrName) {
        receiverName = rdrName;
    }

    /**
     * Set the reader factory to use with this transactor. Normall,
     * this is encapsulated behavior, but we are providing this
     * hook for "special" reader threads ( ok, this is to deal with
     * the goddam latency problems).
     */
    public void setReaderFactory(PacketReceiverFactory rf) {
        packetReceiverFactory = rf;
    }

    /**
     * Connect to a server. NOTE: If already connected, it will
     * simply return.
     *
     * @param saddr - The address to connect to.
     * @return Will always return true.
     * @throws TtlException Thrown if there is some sort of
     *                      connect problem. Typically either CONNECT or CONNECT_IN_PROGRESS.
     *                      Note that if the latter, the object should not be destroyed but
     *                      rather connect() called again. If the former, the object should
     *                      be destroyed and a new one created.
     */
    public synchronized boolean connect(ServiceAddress saddr) throws TtlException {
        if (logger.isTraceEnabled()) {
        	logger.trace("Transactor.connect: LOCK Connecting to transactor: " + hashCode());
        }
        connection = new Connection(saddr, keepAliveFlag);
        connection.connect();

        nextTid = 1;
        pending = new Pending();
        typedPackets = new CircularQueue(100);
        eventPackets = new CircularQueue(8000);
        eventPackets.setAlertOnOverflow(true, "TransactorEventQueue");

        packetReceiver = packetReceiverFactory.allocate(connection, packetReader);

        if (receiverName != null) {
            packetReceiver.setName("ReaderThread:" + receiverName);
        }

        packetReceiver.setPacketListener(this);
        packetReceiver.start();

        if (logger.isTraceEnabled()) {
        	logger.trace("Transactor.connect: fully connected: " + hashCode());
        }
        return true;
    }

    /**
     * Disconnect from the underlying server.
     * Careful. This routine may be called by the PacketReceiver thread.
     */
    public synchronized void disconnect() {
        if (shutdownFlag) {
            return;
        }

        if (connection == null) {
            return;
        }

        connection.close();
        Connection tempConn = connection;
        if (logger.isTraceEnabled()) {
        	logger.trace("Transactor.disconnect(): " + "Have started disconnecting from the transactor: " + tempConn.toString());
        }

        notifyWaiters();

        postDisconnect();
        if (logger.isTraceEnabled()) {
        	logger.trace("Transactor.disconnect: After notifying everybody, " + "have finished disconnecting." + tempConn.toString());
        }

        shutdownFlag = true;
    }

    /**
     * This routine is called to notify any waiters on this
     * transactor object.
     */
    public synchronized void notifyWaiters() {
        //--------------------------------------------------------
        // First thing is to notify any react or getEvent
        // threads in progress.
        //--------------------------------------------------------

        synchronized (eventCondition) {
            eventCondition.notify();
        }

        synchronized (reactCondition) {
            reactCondition.notify();
        }

        if (pending == null) {
            return;
        }

        //----------------------------------------------------------
        // Ok, now we have to iterate over all the pending
        // responses, and do a notifyAll() on each of them.
        // This is to wake up any threads that might be waiting
        // for us.
        //----------------------------------------------------------

        PendingResponse pr;
        synchronized (pending) {
            Enumeration<PendingResponse> itr = pending.elements();

            while (itr.hasMoreElements()) {
                try {
                    pr = itr.nextElement();
                } catch (NoSuchElementException e) {
                    continue;
                }

                if (pr != null) {
                    synchronized (pr) {
                        pr.connDown();
                        pr.notifyAll();
                    }
                }
            }
        }
    }

    /**
     * @return true if we are connected.
     */
    public synchronized boolean isConnected() {
        return connection != null && connection.isConnected();
    }

    /**
     * Issue a blocking request.
     *
     * @param type - The type of the transaction.
     * @param p    - The packet to send.
     * @return The packet returned by the backend.
     * @throws TtlException Thrown on no connection.
     */
    public RPacket syncRequest(byte type, WPacket p) throws TtlException {
        return syncRequest(type, p, -1);
    }

    /**
     * Similar to previous, except that a timeout may be specified.
     *
     * @param type    - The type of the transaction.
     * @param p       - The packet to send.
     * @param timeout - The timeout to use (msecs).
     * @return The packet returned by the backend.
     * @throws TtlException Thrown on timeout or no connection.
     */
    public RPacket syncRequest(byte type, WPacket p, int timeout) throws TtlException {
        //put into the pending and send the packet to server.
        PendingResponse pr = asyncRequest(type, p);

        //wait...
        RPacket rp = waitResponse(pr, timeout);

        return rp;
    }

    /**
     * Register a listener object for a packet of a particular
     * type. More than one listener object may be registered.
     * Events will only be received by calling react(). That is,
     * after registering your type listeners, call react() in
     * some thread. Whenever an event comes in that satisfies
     * any of the listened-to types, then that object will
     * be called.
     *
     * @param type The type of packet we are interested in.
     * @param l    The listener object.
     * @see TypePacketListener
     */
    public void addTypeListener(byte type, TypePacketListener l) {
        packetTypeListeners.put((int) type, l);
    }

    /**
     * Call this routine in a thread to receive packets that
     * satisfy any listeners registered with addTypeListener.
     *
     * @throws TtlException Thrown if not connected when
     *                      initially entered, as well as
     *                      when we detect a lost connection.
     */
    public void react() throws TtlException {
        while (true) {
            if (!isConnected()) {
                throw new TtlException(TtlException.CONNECT, "TransAdvttl.react() ");
            }
            //------------------------------------------------
            // Ok, this next bit of code is a bit tricky.
            // The deal is that we want to examing the
            // container of typed packets that has arrived
            // as well as waiting on the condition var, all
            // while holding the lock. Otherwise we run
            // into a race condition where we examine the
            // container of typed packets, find nothing,
            // context-switch, a typed packet arrives in
            // the other thread which issues the notify,
            // this thread begins executing again and will
            // miss the notify.
            //
            // The problem is then that we DO NOT want to
            // hold any locks while calling the listener
            // callback. Who knows what client code will do,
            // and it might lead to a deadlock if we hold
            // locks (which in fact, did happen).
            //
            // So we do this funky thing where we look for
            // a packet, if none, we drop right into the
            // wait on the condition, all while holding
            // the lock. If we do have a packet, we exit
            // the lock-block, and dispatch the packet.
            //------------------------------------------------

            RPacket rp = null;
            synchronized (reactCondition) {
                rp = (RPacket) typedPackets.get();
                if (rp == null) {
                    waitOnCondition(reactCondition, -1);
                    continue;
                }
            }

            if (rp != null) {
                dispatchTypedPacket(rp);
            }
        }
    }

    /**
     * Send a packet without expecting a reply.
     *
     * @param type - The type of the transaction.
     * @param p    - The packet to send.
     * @throws TtlException Thrown if we don't have a connection.
     */
    public void sendAndForget(byte type, WPacketBase p) throws TtlException {
        PendingResponse pr = asyncRequest(type, p);

        pending.erase(pr.getTid());
    }

    /**
     * Send a response packet. No reply is expected and no
     * manipulation of the packet is performed, it is just sent along.
     *
     * @param rp   The packet we are responding to.
     * @param type The type of the packet to send.
     * @param p    The packet to send.
     * @throws TtlException Thrown if there was a timeout or connect
     *                      problem.
     */
    public synchronized void sendResponse(RPacket rp, byte type, WPacket p) throws TtlException {
        if (!isConnected()) {
            throw new TtlException(TtlException.CONNECT);
        }

        p.setTid(rp.getTid());
        p.header.type = type;
        p.header.length = PacketHeader.PACKET_HEADER_SIZE + p.size();

        try {
            packetWriter.writeTo(new DataOutputStreamWrapper(connection.getOutputStream()), p);
        }
        catch (IOException e) {
            disconnect();

            throw new TtlException(TtlException.CONNECT, "caught ioe in asyncRequest", e);
        }
    }

    /**
     * Get a packet generated by the backend. Routine will block
     * forever until something is seen.
     *
     * @throws TtlException Thrown if there is a connection problem.
     */
    public RPacket getEvent() throws TtlException {
        return getEvent(-1);
    }

    /**
     * Get a packet generated by the backend. Routine will block
     * forever until something is seen.
     *
     * @param timeout The timeout in msecs.
     * @throws TtlException Thrown on timeout or no connection.
     */
    public RPacket getEvent(int timeout) throws TtlException {
        if (!isConnected()) {
            throw new TtlException(TtlException.CONNECT);
        }

        //--------------------------------------------------------
        // See if we have any event packets waiting. If not,
        // then wait for some. Need to synchronize on
        // eventCondition so that we avoid a race:
        //	We call getEventPacket() and find that
        //	there isn't anything. The reader thread then
        //	begins executing, stores a packet, and then
        //	does a notify. This thread picks up again, and
        //	does the wait. It will miss the notify in this
        //	case.
        //--------------------------------------------------------

        RPacket rp = null;
        synchronized (eventCondition) {
            rp = getEventPacket();
            if (rp == null) {
                waitOnCondition(eventCondition, timeout);
                rp = getEventPacket();
            }
        }

        if (!isConnected()) {
            throw new TtlException(TtlException.CONNECT);
        }

        //--------------------------------------------------------
        // If we still don't have an event packet, then we must've
        // timed out. If the timeout was -1, then something
        // weird is going on; for robustness, just call this
        // routine again.
        //--------------------------------------------------------

        if (rp == null) {
            if (timeout != -1) {
                throw new TtlException(TtlException.TIMEOUT);
            } else {
            	logger.warn("Transactor.getEvent(): Hmm, did not find a packet after doing a wait(-1). ");
                rp = getEvent(-1);
            }
        }

        return rp;
    }

    /**
     * Retrieve the current response queue length.
     */
    public int respQueueLength() {
        if (pending == null) {
            return 0;
        }

        return pending.length();
    }

    /**
     * Return the # of packets in the event queue. These are events
     * generated by the backend. The queue has a max size of 100,
     * so events will be lost if not retrieved rapidly enough.
     */
    public int eventQueueLength() {
        if (eventPackets == null) {
            return 0;
        }

        return eventPackets.size();
    }

    /**
     * This is an interface method that is implemented by this class.
     * It is public because of language requirements, BUT IT SHOULD
     * NEVER BE CALLED BY A CLIENT!
     */
    public void packetArrived(RPacketBase p) {
        RPacket rp = (RPacket) p;

        //--------------------------------------------------------
        // Our processing order is as follows:
        //	If there is a listener for a packet of a
        //	particular type, then that is the guy who
        //	receives the packet, and NOBODY ELSE.
        //
        //	Next, if the tid of the packet is 0, then it is
        //	considered a backend generated event and it goes
        //	into the event queue.
        //
        //	Lastly, it goes into the pending queue.
        //--------------------------------------------------------

        TypePacketListener l = packetTypeListeners.get((int) (rp.getType()));
        if (l != null) {
            typedPackets.add(rp);

            synchronized (reactCondition) {
                reactCondition.notify();
            }

            return;
        }

        if (rp.header.tid == 0) {
            eventPackets.add(rp);

            synchronized (eventCondition) {
                eventCondition.notify();
            }

            return;
        }

        //--------------------------------------------------------
        // Add the received packet to the container.
        // We then retrieve the response object and
        // notify anybody that might be waiting on it. Note
        // that there might not be a request for this response,
        // so we just return in that case.
        //--------------------------------------------------------

        PendingResponse pr = pending.addResponse(rp);
        if (pr == null) {
            return;
        }

        synchronized (pr) {
            pr.notify();
        }
    }

    /**
     * This routine is called by the errorNotify() routine which is
     * called by the reader thread whenever it encounters an error.
     * One race condition we need to avoid is the fact that somebody
     * else may have called disconnect() on this object, so all that
     * work has already been done AND it already started a new
     * connect() call which means a new Connection object has been
     * instantiated. If the PacketReceiver thread then kicks in and calls
     * disconnect(), it will blow away a newly established conn
     * which it should not. So we check to see if the conn used
     * when the reader thread was established is the same as the
     * current conn, in which case we proceed with the disconnect.
     */
    public synchronized void disconnect(Connection conn) {
        if (connection == null) {
            return;
        }

        if (connection.getId() != conn.getId()) {
            return;
        }

        disconnect();
        connection = null;
    }

    public boolean isShutdown() {
        return shutdownFlag;
    }

    /**
     * This function is part of an implemented interface. It is
     * public due to language requirements, BUT IT SHOULD NEVER BE
     * CALLED BY A CLIENT! It is normally called by the reader thread
     * when it notices that the connection has gone bad.
     */
    public void errorNotify(Connection conn) {
        disconnect(conn);
    }

    //------------------------------------------------------------------
    // PRIVATE/PROTECTED SECTION
    //------------------------------------------------------------------
    /**
     * Routine returns an event packet that is sitting in
     * the queue.
     */
    private RPacket getEventPacket() {
        RPacket rp = null;
        synchronized (eventPackets) {
            rp = (RPacket) eventPackets.get();
        }

        return rp;
    }

    /**
     * Utility routine to wait on a condition var with a timeout.
     */
    private void waitOnCondition(Object c, int timeout) {
        synchronized (c) {
            if (timeout == -1) {
                Utility.wait(c);
            } else {
                Utility.wait(c, timeout);
            }
        }
    }

    /**
     * Called after disconnection to clean up data structures.
     */
    private void postDisconnect() {
        //--------------------------------------------------------
        // Normally we would tell the thread to just stop, but
        // in this case we are setting a flag telling it to
        // stop when it can. The reason for this is we want to
        // use this code on the server side within the netscape
        // enterprise server, and it does *not* support
        // Thread.stop()!
        //--------------------------------------------------------

        //pending = null;
        stopReadThread();
    }

    /**
     * Utility routine to stop a read thread. Note that we have
     * to get funky choosing whether or not we are going to do
     * a hard or soft stop. See the RateConfig class for more info.
     */
    private void stopReadThread() {
        if (packetReceiver == null) {
            return;
        }

        packetReceiver.setStop();
        packetReceiver = null;
    }

    /**
     * Retrieve the next tid.
     */
    synchronized private int getNextTid() {
        return nextTid++;
    }

    /**
     * Send a request asynchronously.
     *
     * @param type - The type of transaction to send.
     * @param p    - The packet to send.
     * @return The pending response object.
     */
    private synchronized PendingResponse asyncRequest(byte type, WPacketBase p) throws TtlException {
        if (!isConnected()) {
            throw new TtlException(TtlException.CONNECT);
        }

        PendingResponse pr = new PendingResponse(getNextTid());
        pending.add(pr);

        if (p instanceof WPacket) {
            WPacket wp = (WPacket) p;
            wp.header.type = type;
            wp.header.tid = pr.getTid();
            wp.header.length = PacketHeader.PACKET_HEADER_SIZE + p.size();
        }

        try {
            packetWriter.writeTo(new DataOutputStreamWrapper(connection.getOutputStream()), p);
        }
        catch (IOException e) {
            disconnect();
            throw new TtlException(TtlException.CONNECT, "caught ioe in asyncRequest", e);
        }

        return pr;
    }

    /**
     * Wait for a response from an async request with a timeout.
     * We have to be careful with responses sent by the
     * backend before we actually start waiting on the thread.
     *
     * @param pr      The pending response to wait on.
     * @param timeout The timeout in msecs.
     * @return Returns the packet read in.
     * @throws TtlException Thrown on timeout or no connection.
     */
    private RPacket waitResponse(PendingResponse pr, int timeout) throws TtlException {
        RPacket p = null;

        //--------------------------------------------------------
        // We want to synch right away on the pending response.
        // This is so that the packet receive routine won't
        // issue the notify *after* we make the pr.getReceived()
        // call, but *before* we do the wait call. Prevents
        // a subtle race.
        //--------------------------------------------------------
        synchronized (pr) {
            //------------------------------------------------
            // First check to see if the conn is down, and
            // throw an exceptio if it is. Next, check to
            // see if we have the response and return it if so.
            // Otherwise we do a wait for the response.
            //------------------------------------------------
            if (pr.isConnDown()) {
                logger.debug("WaitResponse.PR.isConnDown");
                throw new TtlException(TtlException.CONNECT);
            }

            if (pr.getReceived()) {
                pending.erase(pr.getTid());

                return pr.getPacket();
            }

            waitOnCondition(pr, timeout);
        }

        //--------------------------------------------------------
        // Careful, the following needs to be *outside* the above
        // synchronized block, since isConnected() locks the
        // transactor, and pending.erase() locks pending.
        //
        // Our locking rules are that the  order of locking
        // should be: Transactor->Pending->PendingResponse.
        //--------------------------------------------------------
        if (!isConnected()) {
            logger.debug("WaitResponse.isConnected");

            throw new TtlException(TtlException.CONNECT);
        }

        if (pr.getReceived()) {
            p = pr.getPacket();

            pending.erase(pr.getTid());
        }

        //----------------------------------------------------------
        // If we are here and p is null, we must have timed out.
        // Note that this means that the caller will *never* get
        // a null packet back. Make sure we remove the request
        // from our queue.
        //----------------------------------------------------------
        if (p == null && timeout != -1) {
            pending.erase(pr.getTid());

            throw new TtlException(TtlException.TIMEOUT);
        }

        if (p == null) {
            logger.debug("TransAdvttl.waitResponse(): Strange, got a null packet but timeout was -1");

            p = waitResponse(pr, -1);
        }

        return p;
    }

    /**
     * Utility routine to dispatch a typed packet.
     */
    private void dispatchTypedPacket(RPacket rp) {
        TypePacketListener l = packetTypeListeners.get(new Integer(rp.getType()));

        if (l == null) {
            logger.warn("hmm, got a packet of type = " + rp.getType() + " stored in type queue, but found no listener for it!");
        } else {
            l.typedPacketArrived(rp);
        }
    }


    public String toString() {
        Connection conn = connection;
        if (conn == null) {
            return "NOT CONNECTED";
        }

        return conn.toString();
    }
}
