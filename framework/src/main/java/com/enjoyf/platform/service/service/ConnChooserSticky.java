package com.enjoyf.platform.service.service;

import java.net.UnknownHostException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.io.ServiceAddressKey;

/**
 * A class that sticks to one conn when it comes to choosing one.
 * Of course, if the conn gets broken for whatever reason, then i
 * will choose another.
 */
public class ConnChooserSticky extends ConnChooser {
	
	private static final Logger logger = LoggerFactory.getLogger(ConnChooserSticky.class);
	
    private Random random = new Random(System.currentTimeMillis());

    private ConnInfo curConn = null;
    private ServiceAddress hintServiceAddress = null;
    private boolean inProgressOkFlag = false;

    /**
     * Ctor this object.
     *
     * @param hint    If not null, a conn matching this ServiceAddress
     *                will be returned (if found). If not found, or hint is null,
     *                then some conn is randomly selected.
     * @param timeout The timeout for transactions.
     */
    public ConnChooserSticky(ServiceAddress hint, int timeout) {
        super(timeout);
        hintServiceAddress = hint;
    }

    public ConnChooserSticky(ServiceAddress hint) {
        this(hint, 30 * 1000);
    }

    /**
     * Set this to true if we consider a conn that is in progress
     * a valid conn to return.
     */
    public void setInProgressOk(boolean val) {
        inProgressOkFlag = val;
    }

    protected synchronized ConnPick getPick(Request req) {
        if (curConn != null) {
            ServiceConn sconn = curConn.getServiceConn();
            if (sconn != null && sconn.isAvailable()) {
                return new ConnPick(curConn);
            }

            if (sconn != null && inProgressOkFlag && sconn.isInProgress()) {
            	if (logger.isTraceEnabled()) {
            		logger.trace("Sticky: Returning IN PROGRESS conn: " + curConn);
            	}
                return new ConnPick(curConn);
            }
        }

        curConn = null;
        ConnInfo[] conns = getCurrentConns();
        if (conns.length == 0) {
            return null;
        }

        if (logger.isTraceEnabled()) {
        	logger.trace("STicky: Current conn length: " + conns.length);
        }
        //--
        // If we have a hint, choose that conn.
        //--
        if (hintServiceAddress != null) {
        	if (logger.isTraceEnabled()) {
        		logger.trace("ConnChooserSticky.getPick: using hint: " + hintServiceAddress);
        	}

            ServiceAddressKey key1 = null;
            try {
                key1 = new ServiceAddressKey(hintServiceAddress);
            } catch (UnknownHostException uhe) {
               	logger.warn("ConnChooserSticky.getPick: UnknownHostException for: " + hintServiceAddress);
            }

            if (key1 != null) {
                hintServiceAddress = null;
                
                for (int i = 0; i < conns.length; i++) {
                    ServiceAddressKey key2 = null;
                    try {
                        key2 = new ServiceAddressKey(conns[i].getServiceData().getServiceAddress());
                    } catch (UnknownHostException uhe) {
                        logger.warn("ConnChooserSticky.getPick: UnknownHostException for: " + conns[i].getServiceData().getServiceAddress());
                    }

                    if (key2 == null) {
                        continue;
                    }

                    if (key1.equals(key2)) {
                        curConn = conns[i];
                        break;
                    }
                }
            }
        }

        if (curConn == null) {
            //--
            // Just pick one at random.
            //--
            int idx = Math.abs(random.nextInt()) % conns.length;
            curConn = conns[idx];
            if (logger.isTraceEnabled()) {
            	logger.trace("ConnChooserSticky.getPick: picking at random: " + curConn);
            }
        }

        return new ConnPick(curConn);
    }
}
