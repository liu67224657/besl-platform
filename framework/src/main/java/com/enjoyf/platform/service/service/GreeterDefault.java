/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.io.RPacket;

public class GreeterDefault implements Greeter {
	
	private Logger logger = LoggerFactory.getLogger(GreeterDefault.class);
	
    private GreetInfo greetInfo;
    private Syncher syncher = null;
//    private boolean needHelloFlag = true;
//    private boolean needInitHelloFlag = true;

    public GreeterDefault(GreetInfo greet) {
        greetInfo = greet;
    }

    public void setSyncher(Syncher syncher) {
        this.syncher = syncher;
    }

    public void greet(ServiceConn sconn) throws ServiceException {
        synchronized (sconn) {
            //--
            // Send the hello packet and synch up if necessary.
            //
            // We need to synch on the conn so we don't do this more
            // than once via multiple threads.
            //--
            p_checkHello(sconn);
            p_checkSynch(sconn);
        }
    }

    /**
     * Utility routine to check and see if we should synch up, and to
     * perform the synching if so.
     */
    protected void p_checkSynch(ServiceConn sconn) throws ServiceException {
        //--
        // We first ask the syncher if want to synch up, if not,
        // we just return.
        //--
        if (syncher == null) {
            return;
        }

        //--
        // Call the business logic to perform the synch up.
        //--
        syncher.synchUp(sconn, this);
    }

    /**
     * Send the hello packet if we have a brand new connection.
     */
    protected void p_checkHello(ServiceConn sconn) throws ServiceException {
        RPacket rp = null;
        if (sconn.needInitHello()) {
            sconn.setNeedInitHello(false);
            try {
                logger.debug("GreeterDefault: SENDING INIT HELLO");
                Request req = greetInfo.getInitHello();
                rp = sconn.process(req);
                logger.debug("GreeterDefault: HELLO SENT");
            }
            catch (ServiceException e) {
                if (e.getValue() == ServiceException.CONNECT) {
                    sconn.setNeedInitHello(true);
                }
                throw e;
            }
        } else if (sconn.needHello()) {
            sconn.setNeedHello(false);
            try {
                logger.debug("GreeterDefault: SENDING RECON HELLO");
                Request req = greetInfo.getReconHello();
                rp = sconn.process(req);
            }
            catch (ServiceException e) {
                if (e.getValue() == ServiceException.CONNECT) {
                    sconn.setNeedHello(true);
                }
                throw e;
            }
        }
        p_processHelloReturn(rp);
    }

    /**
     * This function is here for derived classes to implement.
     */
    protected void p_processHelloReturn(RPacket rp) {
    }
}
