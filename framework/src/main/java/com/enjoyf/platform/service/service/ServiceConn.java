/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.io.PacketReaderAdvttl;
import com.enjoyf.platform.io.PacketWriterAdvttl;
import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.io.Transactor;
import com.enjoyf.platform.io.TtlException;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.io.WPacketBase;
import com.enjoyf.platform.service.service.load.LoadMonitorDef;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.thread.DiePoolThread;

/**
 * This class acts as a wrapper around the Transactor object for
 * managing transactions.
 */
public class ServiceConn {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceConn.class);
    //
    private ServiceId serviceId;

    /**
     * State value: Indicates that the object is ready to be connected.
     * This is the initial connState.
     */
    static final int READY = 0;

    /**
     * State value: Indicates that the connection is in progress.
     */
    static final int IN_PROGRESS = 1;

    /**
     * State value: Indicates that the connection is complete.
     */
    static final int CONNECTED = 2;

    /**
     * State value: Indicates that the conn is closed and should no
     * longer be used for operations.
     */
    static final int CLOSED = 3;

    //////
    private Transactor transactor;
    private ServiceAddress serviceAddr;

    //
    private int timeoutTime = 10 * 1000;
    private boolean needHelloFlag = true;
    private boolean needInitHelloFlag = true;
    private EventThread eventThread = null;
    private LoadMonitorDef loadMonitor = new LoadMonitorDef();
    private int partitionValue = 0;
    private HealthMonitor healthMonitor;

    /**
     * Indicates the connState of the connection. Note that the STD is
     * quite simple: READY->IN_PROGRESS->CONNECTED->CLOSED. There is
     * no going back once you've moved off of a connState.
     */
    private volatile int connState = READY;
    /**
     * Keep track of id's used to label event threads.
     */
    private static volatile AtomicInteger nextId = new AtomicInteger(1);

    private static final int STD_TIMEOUT = 30 * 1000;

    /**
     * Keeps track of how many transactions this object has processed.
     */
    private int transactionCount;

    /**
     * Construct a service object. Note that you'll need to
     * call connect() before you can use it.
     *
     * @param saddr   The ip/port of the serving host.
     * @param servId  The ServiceId, for information purposes.
     * @param timeout The timeoutTime in msecs for issuing
     *                a transaction.
     */
    ServiceConn(ServiceAddress saddr, ServiceId servId, int timeout) {
        serviceAddr = saddr;
        serviceId = servId;
        timeoutTime = timeout;

        //--
        // See if we've turned on health checks.
        //--
        boolean useHealthChecks = Props.instance().getEnvProps().getBoolean("service.useHealthChecks", false);
        if (logger.isTraceEnabled()) {
        	logger.trace("Using health checks: " + useHealthChecks);
        }
        healthMonitor = useHealthChecks ? (HealthMonitor) (new HealthMonitorBasic(STD_TIMEOUT)) : (HealthMonitor) (new HealthMonitorDummy());
        transactor = new Transactor(new PacketReaderAdvttl(), new PacketWriterAdvttl());
        if (logger.isTraceEnabled()) {
        	logger.trace("ServiceConn.ctor: " + this);
        }
    }

    /**
     * Consult the other constructor.
     */
    ServiceConn(ServiceAddress saddr, ServiceId serviceId) {
        this(saddr, serviceId, 30000);
    }

    /**
     * Make the connection to the underlying object. May take a while.
     */
    public synchronized void connect() throws TtlException {
    	if (logger.isTraceEnabled()) {
    		logger.trace("ServiceConn.conn: connect: " + this);
    	}
        //--
        // Attempt the connection. If we get any sort of exception this
        // object is hosed and can no longer be used so set the connState
        // accordingly.
        //--
        try {
            transactor.connect(serviceAddr);
        }
        catch (TtlException te) {
            close();
            throw te;
        }
        if (logger.isTraceEnabled()) {
        	logger.trace("ServiceConn.connect: Done: " + this);
        }
    }

    /**
     * Shut this service down. It i probably a good idea to
     * call this routine to clean things up when you want it done
     * as opposed to letting the gc do it.
     */
    public void close() {
        synchronized (this) {
            if (connState == CLOSED) {
                return;
            }

            connState = CLOSED;
        }

        if (logger.isTraceEnabled()) {
        	logger.trace("ServiceConn: closing: " + this);
        }
        
        if (transactor != null) {
            transactor.disconnect();
        }

        if (eventThread != null) {
            eventThread.die();
        }
    }

    /**
     * Returns true if this object is no longer usable.
     */
    public boolean shouldRemove() {
        return connState == CLOSED || (transactor != null && transactor.isShutdown());
    }

    /**
     * This is used internally by the package in order to control the
     * fact that we try to connect on a separate thread. The ConnManager
     * object needs to have control over the connState of a connection
     * to avoid race-conditions. In particular, the CONNECTED and
     * IN_PROGRESS states are managed by an external object.
     */
    public void setState(int val) {
        connState = val;
    }

    /**
     * Returns true if the connection is in progress.
     */
    public boolean isInProgress() {
        return connState == IN_PROGRESS;
    }

    public boolean isConnected() {
        return connState == CONNECTED;
    }

    /**
     * Returns true if the object is available for operations, meaning
     * it can either be connected to or it has been connected.
     */
    public boolean isAvailable() {
        return connState == CONNECTED || connState == READY;
    }

    /**
     * Returns true if we need to connect up for the first time.
     */
    public boolean needsConnection() {
        return connState == READY;
    }

    /**
     * Return the service address of this class.
     */
    public ServiceAddress getServiceAddress() {
        return serviceAddr;
    }

    public void setLoadMonitor(LoadMonitorDef loadMonitor) {
        if (loadMonitor != null) {
            this.loadMonitor = loadMonitor;
        }
    }

    /**
     * A utility routine to handle exceptions.
     */
    public RPacket send(Request req) throws ServiceException {
        if (loadMonitor.isOverloaded()) {
            throw new ServiceException(ServiceException.OVERLOADED);
        }

        RPacket rp = null;
        try {
            rp = process(req);
        } catch (ServiceException e) {
            if (e.getValue() == ServiceException.TIMEOUT) {
                loadMonitor.event();
            }
            throw e;
        }

        //--------------------------------------------------------
        // Set the service address used to logicProcess the request
        // in case the caller needs to know this info.
        //--------------------------------------------------------
        req.setServiceAddress(getServiceAddress());

        return rp;
    }

    /**
     * This method is used by service/fplayer/LocationServiceImpl, should
     * change to get rid of its use.
     */
    public RPacket process(Request req) throws ServiceException {
        //report the monitor enter event.
        healthMonitor.enter();

        //count the trans times.
        transactionCount++;

        boolean gotTimeout = false;

        //--
        // Pass the metrics type in the packet, overloading one of the
        // existing unused fields in the packet object.
        //--
        if (req.getMetricsType() != 0) {
        	req.getPacket().setMetricsType(req.getMetricsType());
        }

        try {
            RPacket rp = null;

            if (!req.isBlocking()) {
                processNonBlock(req.getPacket(), req.getType());
            } else {
                rp = process(req.getPacket(), req.getType(), req.isTimeoutSet() ? req.getTimeout() : timeoutTime);
            }

            return rp;
        } catch (ServiceException se) {
            if (se.getValue() == ServiceException.TIMEOUT) {
                gotTimeout = true;
            }

            throw se;
        } finally {
            if (gotTimeout) {
                healthMonitor.timeoutExit();
            } else {
                healthMonitor.exit();
            }
        }
    }

    public HealthMonitor getMonitor() {
        return healthMonitor;
    }

    /**
     * Utility routine to do common processing of a request.
     * The protocol for any request is:
     * errcode(byte): This will either be OK or NOTOK. If NOTOK
     * then a ServiceException object follows which is read in. If OK,
     * then it is up to the caller to decode the packet.
     */
    private RPacket process(WPacket wp, byte code, int timeout) throws ServiceException {
        //--------------------------------------------------------
        // Ok, issue the request to the transctor.
        //--------------------------------------------------------
        RPacket rp = null;
        try {
            rp = transactor.syncRequest(code, wp, timeout);
        } catch (TtlException e) {
            if (e.isTimeout()) {
                GAlerter.lan("ServiceConn.logicProcess:TIMEOUT talking to: " + serviceId + ":" + serviceAddr + ":tid=" + wp.getTid());
                throw new ServiceException(ServiceException.TIMEOUT, e);
            } else {
                logger.warn("ServiceConn.logicProcess: " + e + " talking to: " + serviceAddr, e);
                close();
                throw new ServiceException(ServiceException.CONNECT, e);
            }
        }

        //if the request is not successful, throw the service exception.
        byte errcode = rp.readByteNx();
        if (errcode != ServiceConstants.OK) {
            ServiceException e = (ServiceException) rp.readSerializable();
            throw e;
        }

        return rp;
    }

    /**
     * Utility routine to send off a request. No reply is expected.
     */
    private void processNonBlock(WPacketBase wp, byte code) throws ServiceException {
        //--------------------------------------------------------
        // Ok, issue the request to the transctor.
        //--------------------------------------------------------
        try {
            transactor.sendAndForget(code, wp);
        } catch (TtlException e) {
            logger.warn("ServiceConn.logicProcess: " + e + " talking to: " + serviceAddr, e);
            close();

            throw new ServiceException(ServiceException.CONNECT);
        }

        return;
    }

    public ChooseStats getChooseStats() {
        return healthMonitor.getChooseStats();
    }

    public int getCount() {
        return transactionCount;
    }

    /**
     * Call this method to create an event thread which will
     * receive events and send them to the passed in event listener.
     */
    public void setEventListener(EventListener l, String name) {
        eventThread = new EventThread(l, name);
        eventThread.start();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(hashCode() + ":");
        sb.append(serviceAddr + ":");
        sb.append("connState=" + getStateStr() + ":");
        sb.append("partitionValue=" + partitionValue);

        return new String(sb);
    }

    private String getStateStr() {
        String retval = "UNKNOWN";

        switch (connState) {
            case READY:
                retval = "READY";
                break;
            case IN_PROGRESS:
                retval = "IN_PROGRESS";
                break;
            case CONNECTED:
                retval = "CONNECTED";
                break;
            case CLOSED:
                retval = "CLOSED";
                break;
        }

        return retval;
    }

    /**
     * check event listener status set or unset
     *
     * @return true if set. false otherwise.
     */
    public boolean isEventListenerSet() {
        return eventThread != null;
    }

    /**
     * Utility class to logicProcess events.
     */
    private class EventThread extends DiePoolThread {
        private EventListener eventListener;

        EventThread(EventListener l, String name) {
            eventListener = l;

            setName("EventThread:" + ((name == null) ? "anon" : name) + ":" + p_getNextId());
        }

        public void run() {
            logger.trace("ServiceConn.EventThread starting..");

            try {
                while (!shouldDie()) {
                    RPacket rp = transactor.getEvent();

                    eventListener.eventArrived(rp, serviceAddr);
                }
            } catch (TtlException e) {
                logger.warn("ServiceConn.EventThread died: ", e);
                close();

                eventListener.connDown(serviceAddr);
            }
        }
    }

    /**
     * Not all services are partitioned, but those that are (ie, those
     * that use ConnChooserPartition) can set the partitionValue so that the
     * business logic (primarily the synch-up logic) can figure out
     * which partitionValue it needs to synch up to.
     */
    void setPartition(int partition) {
        this.partitionValue = partition;
    }

    /**
     * Retrieve the partitionValue if this ServiceConn was allocated via
     * ConnChooserPartition.
     */
    public int getPartition() {
        return partitionValue;
    }

    boolean needInitHello() {
        return needInitHelloFlag;
    }

    boolean needHello() {
        return needHelloFlag;
    }

    void setNeedInitHello(boolean val) {
        needInitHelloFlag = val;
    }

    void setNeedHello(boolean val) {
        needHelloFlag = val;
    }

    /**
     * A utility routine to return an incrementing id to differentiate
     * between event threads.
     */
    private static String p_getNextId() {
        return Integer.toString(nextId.incrementAndGet());
    }
}
