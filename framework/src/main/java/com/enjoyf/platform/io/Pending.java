/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.util.Enumeration;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Package private class. This is a container used to hold pending requests
 * for a Transactor object. The index into the container is typically the
 * tid. The object stored is a PendingResponse object.
 *
 * @see Transactor
 * @see PendingResponse
 */
public class Pending {
	
	private static final Logger logger = LoggerFactory.getLogger(Pending.class);
	
    //
    private Hashtable<Integer, PendingResponse> pendingResponsesMap = new Hashtable<Integer, PendingResponse>(100);

    /**
     * Return the contents of the container.
     */
    public Enumeration<PendingResponse> elements() {
        return pendingResponsesMap.elements();
    }

    /**
     * Add a pending response to the container.
     */
    public synchronized void add(PendingResponse pr) {
        pendingResponsesMap.put(pr.getTid(), pr);
    }

    /**
     * Erase a response from the container.
     */
    public synchronized void erase(int tid) {
        //----------------------------------------------------------
        // Remove the element pending response from the container,
        // and also remove it from the queue if it's a tid==0
        // packet.
        //----------------------------------------------------------
        pendingResponsesMap.remove(tid);
    }

    /**
     * Return a response from the container.
     *
     * @param tid This is the key of the response.
     */
    public synchronized PendingResponse get(int tid) {
        return pendingResponsesMap.get(tid);
    }

    /**
     * This routine gets called when a packet has been read in. If
     * there was a pending response, we update it accordingly, if
     * there wasn't we just add it in.
     *
     * @param rp The packet that has arrived.
     * @return The PendingResponse found or added.
     */
    public synchronized PendingResponse addResponse(RPacket rp) {
        //--------------------------------------------------------
        // If no request for this response, toss it. Probably
        // means the original request timed out.
        //--------------------------------------------------------
        PendingResponse pr = get(rp.header.tid);
        if (pr == null) {
        	if (logger.isDebugEnabled()) {
        		logger.debug("Pending.addResponse: Did not find a request for resp = " + rp.header.tid);
        	}
            return null;
        }

        pr.responseReceived(rp);
        return pr;
    }

    /**
     * Return the # of elements in the queue.
     */
    public int length() {
        return pendingResponsesMap.size();
    }
}
