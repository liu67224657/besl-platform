/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.IOException;

/**
 * This is an abstract class for reading in packets from an input
 * stream. Derived classes should override the readFrom method to
 * fill in the packet.
 */
public abstract class PacketReaderBase {
    protected int maxPacketLength = 3 * 1024 * 1024;

    /**
     * Routine is called to read a packet in from the given
     * input stream.
     */
    public abstract RPacketBase readFrom(DataInputCustom din) throws IOException;

    /**
     * Set a max packet length. If a packet of length > maxPacketLength
     * is read in, an IOException will be thrown.
     */
    public void setMaxPacketLength(int maxLength) {
        maxPacketLength = maxLength;
    }
}
