package com.enjoyf.platform.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.io.TtlException;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.thread.DiePoolThread;

/**
 * A class used to perform a connection on a separate thread.
 */
public class ConnMaker {
	
	private static final Logger logger = LoggerFactory.getLogger(ConnMaker.class);
	
    private int timeoutTime = 30 * 1000;

    private boolean inProgressFlag = false;

    public ConnMaker(int timeout) {
        timeoutTime = timeout;
    }

    public boolean isInProgress() {
        return inProgressFlag;
    }

    public void setInProgress(boolean val) {
        inProgressFlag = val;
    }

    public String toString() {
        return "INPROGRESS=" + inProgressFlag;
    }

    synchronized void connect(ServiceConn sconn, EventConfig eventConfig, Greeter greeter) {
        inProgressFlag = true;
        //--
        // Create the thread performing the actual connection and
        // wait some amount of time for the connection to complete.
        //--
        new ConnectionThread(this, sconn, eventConfig, greeter).start();
        //--
        // Wait for the connect call to complete. It may not, in which
        // case we have a timeout.
        //--
        Utility.wait(this, timeoutTime);
    }

    private static class ConnectionThread extends DiePoolThread {
        private ServiceConn serviceConn;
        private EventConfig eventConfig;
        private Greeter greeter;
        private ConnMaker connMaker;

        public ConnectionThread(ConnMaker connMaker, ServiceConn conn, EventConfig eventConfig, Greeter greeter) {
            this.serviceConn = conn;
            this.eventConfig = eventConfig;
            this.greeter = greeter;
            this.connMaker = connMaker;
            setName("ConnMaker.ConnThread:" + hashCode());
        }

        public void run() {
            try {
            	if (logger.isDebugEnabled()) {
            		logger.debug("ConnMaker.connThread: " + serviceConn);
            	}
                serviceConn.setState(ServiceConn.IN_PROGRESS);

                try {
                    serviceConn.connect();
                } catch (TtlException e) {
                    logger.error("ConnMaker.connThread: connect error: ", e);
                    serviceConn.close();

                    return;
                }

                //--
                // Set up an event listener if so indicated. Do this
                // first, before we greet, in case greeting causes
                // events to be sent to us that we might miss.
                //--
                if (eventConfig != null) {
                    serviceConn.setEventListener(eventConfig.getListener(), eventConfig.getThreadName());
                }

                //--
                // Now greet the conn if so indicated.
                //--
                if (greeter != null) {
                    try {
                        greeter.greet(serviceConn);
                    } catch (ServiceException e) {
                        logger.error("ConnChooser: Could not greet: " + serviceConn, e);
                        serviceConn.close();

                        return;
                    }
                }

                serviceConn.setState(ServiceConn.CONNECTED);

                if (logger.isDebugEnabled()) {
                	logger.debug("ConnMaker.conThread: end = " + serviceConn);
                }
            } finally {
                connMaker.setInProgress(false);
                synchronized (connMaker) {
                    connMaker.notify();
                }
            }
        }
    }
}
