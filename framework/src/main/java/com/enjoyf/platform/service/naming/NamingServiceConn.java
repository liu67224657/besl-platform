package com.enjoyf.platform.service.naming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.service.service.ConnChooserSticky;
import com.enjoyf.platform.service.service.EventConfig;
import com.enjoyf.platform.service.service.EventListener;
import com.enjoyf.platform.service.service.Greeter;
import com.enjoyf.platform.service.service.ReqProcessor;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConn;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.ServiceFinder;
import com.enjoyf.platform.service.service.ServiceFinderN;
import com.enjoyf.platform.service.service.ServiceFinderVirtual;
import com.enjoyf.platform.service.service.Syncher;

/**
 * This is an internal class to manage the connection to the naming server
 * Like the ServiceConn in com.enjoyf.platform.service.ServiceConn.
 */
public class NamingServiceConn {
    
    private static final Logger logger = LoggerFactory.getLogger(NamingServiceConn.class);
    
    private ReqProcessor reqProcessor = null;
    private Listener listener = null;
    private int timeoutFlag = 30 * 1000;

    private NamingServiceAddress namingServiceAddress;

    protected NamingServiceConn(NamingServiceAddress saddr, Listener l, ServiceAddress hint) {
        this(saddr, l, hint, true, "NamingServiceEvent");
    }

    protected NamingServiceConn(NamingServiceAddress saddr, Listener l, ServiceAddress hint, boolean useReconThread, String threadName) {
        this(saddr, l, hint, useReconThread, threadName, false);
    }

    protected NamingServiceConn(NamingServiceAddress saddr, Listener l, ServiceAddress hint, boolean useReconThread, String threadName, boolean inProgressOk) {
	if (logger.isDebugEnabled()) {
	    logger.debug("NamingServiceConn: " + hashCode() + ": Creating from: " + saddr);
	}
        namingServiceAddress = saddr;

        ServiceFinder finder = null;
        if (saddr.isVirtual()) {
            ServiceAddress vaddr = saddr.getVirtual();

            finder = new ServiceFinderVirtual(vaddr.getAddr(), vaddr.getPortInt());
        } else {
            finder = new ServiceFinderN(saddr.getAddresses());
        }

        p_init(finder, l, hint, useReconThread, threadName, inProgressOk);
    }

    private void p_init(ServiceFinder finder, Listener l, ServiceAddress hint, boolean useReconThread, String threadName, boolean inProgressOk) {
        listener = l;

        ConnChooserSticky chooser = new ConnChooserSticky(hint, timeoutFlag);

        chooser.setInProgressOk(inProgressOk);
        chooser.setServiceFinder(finder);

        reqProcessor = new ReqProcessor(chooser);

        //--
        // We establish a Greeter object so that when we lose the
        // conn, we synch up again. "Synching up" means registering for
        // events if we have been told to do so.
        //--
        reqProcessor.setGreeter(new MyGreeter());

        //--
        // Register an event listener so that we can receive change
        // events from the naming service.
        //--
        reqProcessor.setEventCfg(
                new EventConfig(
                        new EventListener() {
                            //the event is arrived.
                            public void eventArrived(RPacket rp, ServiceAddress sa) {
                                listener.eventArrived(rp);
                            }

                            public void connDown(ServiceAddress sa) {
                                p_connDown();
                            }
                        },
                        threadName
                )
        );

        //--
        // If we are firing up a recon thread, use that. Otherwise perform
        // the initial connect to the service.
        //--
        if (useReconThread) {
            reqProcessor.useConnThread(10 * 1000);
        }
    }

    /**
     * Issue an explicit connect call. Returns true if the connect succeeds.
     */
    public boolean connect() {
        return reqProcessor.connect();
    }

    protected NamingServiceAddress getServiceAddress() {
        return namingServiceAddress;
    }

    protected void close() {
        logger.info("NamingServiceConn: " + hashCode() + ": Shutting down: ");
        reqProcessor.close();
    }

    /**
     * Attempt to reconnect to the naming service as soon as we detect
     * a broken conn (but only if we are not dying).
     */
    private void p_connDown() {
	if (logger.isDebugEnabled()) {
	    logger.debug("NamingServiceConn.connDown: " + hashCode());
	}
    }

    public RPacket send(Request req) throws ServiceException {
        RPacket rp = null;

        try {
            rp = reqProcessor.process(req);
        }
        catch (ServiceException se) {
            if (se.equals(ServiceException.CONNECT)) {
                throw new ServiceException(ServiceException.CONNECT, "Could not connect to naming server: " + namingServiceAddress);
            } else if (se.equals(ServiceException.CONNECT_IN_PROGRESS)) {
                throw new ServiceException(ServiceException.CONNECT_IN_PROGRESS, "Could not connect to naming server. Connection in progress to: " + namingServiceAddress + " Usually means the connect() call is hung.");
            }

            throw se;
        }

        return rp;
    }

    class MyGreeter implements Greeter {
        public void greet(ServiceConn conn) throws ServiceException {
            listener.synchUp(conn);
        }

        public void setSyncher(Syncher syncher) {
        }
    }

    /**
     * Use this interface to communicate with the client object of
     * this class.
     */
    protected interface Listener {
        /**
         * Called when we need to synch up, ie, when the conn has
         * been established.
         */
        public void synchUp(ServiceConn sconn) throws ServiceException;

        /**
         * Called when an event has arrived from the server.
         */
        public void eventArrived(RPacket rp);
    }
}
