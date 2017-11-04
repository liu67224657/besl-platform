package com.enjoyf.platform.service.naming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConn;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.ServiceLoad;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.thread.DiePoolThread;

/**
 * This class encapsulates the fact that a RegistrantConn may
 * re-initialize itself with a new ping thread and conn.
 * It's a wrapper class of NamingServiceConn.
 */
public class RegistrantConn {
    
    private static final Logger logger = LoggerFactory.getLogger(RegistrantConn.class);
    
    private boolean closedFlag = false;
    private static long curId;

    private long connId;

    private NamingServiceConn namingServiceConn;
    private PingThread pingThread;
    private RegistrantNormal registrantNormal;

    RegistrantConn(RegistrantNormal registrant) {
        registrantNormal = registrant;

        //--
        // Tag this object.
        //--
        connId = p_nextId();
    }

    void start(ServiceAddress hint) {
        p_init(hint);
    }

    long getId() {
        return connId;
    }

    boolean isMatch(long id) {
        return connId == id;
    }

    private void p_init(ServiceAddress hint) {
        logger.info("RegistrantConn.p_init: " + hashCode() + " serviceInfo = " + registrantNormal.getServiceInfo() + " hint = " + hint);

        //--
        // Note the last arg to the ctor.
        // We don't want to reconnect immediately upon lost conn. Let
        // the ping thread initiate this. This is for two reasons: 1)
        // we reduce the load on the ns during a restart since different
        // clients will connect at different times, 2) the ping thread
        // will compete with other threads to attempt reconnection. This
        // may cause more than one registration to be issued by this
        // object, which results in a DUP_REGISTRATION event from the ns.
        // This in turn causes this code to kill itself, but it shouldn't
        // so we'd need to manage the fact that this particular object
        // issued more than one registration.
        //--
        namingServiceConn = new NamingServiceConn(
                registrantNormal.getNamingServiceAddress(),
                new NamingServiceConn.Listener() {
                    public void synchUp(ServiceConn sconn) throws ServiceException {
                        p_synchUp(sconn);
                    }

                    public void eventArrived(RPacket rp) {
                        p_eventArrived(rp);
                    }
                },
                hint,
                false,
                registrantNormal.getServiceInfo().toString(),
                true
        );

        pingThread = new PingThread();

        //--
        // Start the ping thread which will initiate connection when the
        // ping is sent out.
        //--
        pingThread.start();

        logger.debug("RegistrantConn.p_init END: " + hashCode() + " serviceInfo = " + registrantNormal.getServiceInfo());
    }

    private void p_synchUp(ServiceConn sconn) throws ServiceException {
        ClientRegInfo clientRegInfo = new ClientRegInfo(registrantNormal.getServiceInfo());
        clientRegInfo.setRegistrantId(registrantNormal.getId());
        pingThread.setName("NamingServicePing_synch:" + clientRegInfo.getThreadName());

        logger.debug("RegistrantConn.p_synchUp: synching up.." + hashCode() + " clientRegInfo = " + clientRegInfo);

        WPacket wp = new WPacket();
        wp.writeSerializable(clientRegInfo);
        Request req = new Request(NamingConstants.REGISTER, wp);

        try {
            sconn.send(req);
        } catch (NamingException ne) {
            if (ne.equals(NamingException.REGISTRATION_DISALLOWED)) {
                logger.warn("SERVER WILL DIE!! Registration with " + clientRegInfo.getServiceInfo()
                        + " has been disallowed, probably because it was registering with a ServiceId that conflicts with an existing one.");

                Utility.sleep(1000);
                System.exit(1);
            }
            throw ne;
        }

        logger.debug("RegistrantConn.p_synchUp: synch up complete." + hashCode() + " clientRegInfo = " + clientRegInfo);
    }

    private void p_eventArrived(RPacket rp) {
        ClientRegInfo serviceInfo;
        String tstr;
        ServiceAddress saddr;

        byte type = rp.getType();

        logger.debug("RegistrantConn: Received event: " + type);

        switch (type) {
            case NamingConstants.EV_REBALANCE:
                tstr = rp.readString();
                saddr = new ServiceAddress();
                saddr.reconstitute(tstr);
                p_handleRebalance(saddr);
                break;
            case NamingConstants.EV_SERVICE_DIE:
                serviceInfo = (ClientRegInfo) rp.readSerializable();
                p_handleDie(serviceInfo);
        }
    }

    private void p_handleRebalance(ServiceAddress saddr) {
        registrantNormal.handleRebalance(connId, saddr);
    }

    private void p_handleDie(ClientRegInfo serviceInfo) {
        registrantNormal.handleDie(connId, serviceInfo);
    }

    /**
     * Called to shut this object down.
     */
    public synchronized void close() {
        if (closedFlag) {
            return;
        }

        if (pingThread != null) {
            pingThread.die();
        }

        if (namingServiceConn != null) {
            namingServiceConn.close();
        }

        closedFlag = true;
    }

    public synchronized boolean isClosed() {
        return closedFlag;
    }

    /**
     * Send an updated load to the naming server.
     */
    public void updateLoad(ServiceLoad load) {
        /**
         * Just piggyback on the ping.
         */
        WPacket wp = new WPacket();
        wp.writeSerializable(load);

        Request req = new Request(NamingConstants.PING, wp);

        p_pingImpl(req);
    }

    /**
     * This method is here for test purposes only.
     */
    public void ping() {
        p_pingImpl(p_getPingRequest());
    }

    private void p_pingImpl(Request req) {
        try {
            namingServiceConn.send(req);
        }
        catch (ServiceException e) {
            logger.error("RegistrantConn.ServiceThread: could not ping!", e);
        }
    }

    class PingThread extends DiePoolThread {
        PingThread() {
            setName("NamingServicePingInit:" + registrantNormal.getServiceInfo().getServiceId().toString() + ":" + hashCode());
        }

        public void run() {
            //
            while (!shouldDie()) {
                //--
                // Send a ping, which will perform the connect if we haven't
                // connected already.
                //--
                int sleepInterval = registrantNormal.getPingInterval();

                //
                try {
                    namingServiceConn.send(p_getPingRequest());
                } catch (ServiceException e) {
                    //--
                    // We really don't do much if we got some sort
                    // of exception. Other bits of the code will
                    // take care of doing the right thing.
                    //--
                    logger.error("RegistrantConn.ServiceThread: could not ping!", e);

                    //--
                    // Try every 15 seconds if we are down. This is how
                    // often to attempt the reconnect.
                    //--
                    sleepInterval = 15 * 1000;
                }


                if (shouldDie()) {
                    break;
                }

                //--
                // Note that we use Utility.sleepExc() instead of
                // Utility.sleep() since when somebody shuts this
                // object down, we'll get the exception and be able
                // to check whether or not we should die.
                //--
                try {
                    Utility.sleepExc(sleepInterval);
                } catch (Exception e) {
                    logger.error("RegistrantConn.Ping thread error.", e);
                }
            }

            logger.debug("RegistrantConn: PingThread is dying");
        }
    }

    private Request p_getPingRequest() {
        WPacket wp = new WPacket();

        wp.writeSerializable(registrantNormal.getServiceLoad());

        Request req = new Request(NamingConstants.PING, wp);

        return req;
    }

    public String toString() {
        return registrantNormal.getServiceInfo().toString() + ":" + connId;
    }

    private synchronized static long p_nextId() {
        return ++curId;
    }
}
