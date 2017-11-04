/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

/**
 * Package private class. These objects are stored whenever a request
 * is made from the backend. When a packet comes in from the backend,
 * we compare it against one of these objects to see if we have a match.
 */
public class PendingResponse {
    //the thread id transt from the client and sent back by the server.
    private int threadId;

    //
    private RPacket responsePacket = null;

    //
    private boolean received = false;
    private boolean connDown = false;

    /**
     * Construct a pending response with the specified threadId.
     */
    public PendingResponse(int tid) {
        threadId = tid;
    }

    /**
     * Return the packet associated with this response. Only makes
     * sense when getReceived() returns true.
     */
    public RPacket getPacket() {
        return responsePacket;
    }

    /**
     * Called when a packet has been read in from the backend.
     * This changes the state of the PendingResponse to no longer
     * pending (a bit ugly this..).
     */
    public synchronized void responseReceived(RPacket p) {
        responsePacket = p;
        received = true;
    }

    /**
     * This will be called whenever the conn is being shut down, so
     * that threads waiting on this response know that there will be
     * no response.
     */
    public synchronized void connDown() {
        connDown = true;
    }

    public boolean isConnDown() {
        return connDown;
    }

    /**
     * Return whether or not we have received the response
     */
    public boolean getReceived() {
        return received;
    }

    /**
     * Return the threadId of this response.
     */
    public int getTid() {
        return threadId;
    }
}
