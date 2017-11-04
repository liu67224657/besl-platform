/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.IOException;

/*
* Input packet class that extends RPacketHeader by adding some timing
* stuff.
*/

public class RPacket extends RPacketHeader {
    /**
     * The arrival time of the packet.
     */
    private long arrivalTime = -1;

    /**
     * The deltaTime between waiting for the packet and actually getting the
     * packet.
     */
    private int deltaTime = -1;

    public RPacket() {
    }

    /**
     * Construct the packet from the passed in array.
     */
    public RPacket(byte[] barray) {
        super(barray);
    }

    /**
     * Reads in a string of a fixed size.
     *
     * @param n The size of the string to read in. It is expected
     *          that there is at least n bytes left in the packet,
     *          or else 0 bytes left in the packet.
     */
    public String readFixedString(int n) {
        if (length() < n) {
            return "";
        }

        byte[] barray = new byte[n];
        try {
            read(barray);
        } catch (IOException e) {
            // Should never get this
        }

        //--------------------------------------------------------
        // Note that we are creating a string from an array of
        // bytes using the default character encoding.
        // This assumes 8-bit chars so we'll get bit by this
        // if the default ever changes to unicode or something.
        //--------------------------------------------------------

        // JDK1.1.1: return new String(barray);
        return new String(barray, 0);
    }

    /**
     * Set the deltaTime for the packet (time between the waiting
     * for a packet and the arrival of the packet).
     */
    public void setDeltaTime(int delta) {
        deltaTime = delta;
    }

    /**
     * Set the arrival time of the packet
     *
     * @param t
     */
    public void setArrivalTime(long t) {
        arrivalTime = t;
    }

    /**
     * Return the deltaTime (time between the waiting for a packet and
     * the arrival of a packet). In msecs.
     */
    public int getDeltaTime() {
        return deltaTime;
    }

    /**
     * Return the arrival time of the packet (in msecs).
     */
    public long getArrivalTime() {
        return arrivalTime;
    }

    public byte getMetricsType() {
        return header.magic != 0 ? (byte) header.magic : getType();
    }

}