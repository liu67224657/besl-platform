/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

/*
 * Input packet class that uses a specific PacketHeader. A general
 * purpose packet class.
 */

public class RPacketHeader extends RPacketBase {
    /**
     * The packet header.
     */
    public PacketHeader header = new PacketHeader();

    public RPacketHeader() {
    }

    /**
     * Construct the packet from the passed in array.
     */
    public RPacketHeader(byte[] barray) {
        super(barray);
    }

    public String toString() {
        return header.toString() + super.toString();
    }

    /**
     * Return the type of the packet.
     */
    public byte getType() {
        return header.type;
    }

    /**
     * Return the tid of this packet.
     */
    public int getTid() {
        return header.tid;
    }

    /**
     * Return the magic value.
     */
    public short getMagic() {
        return header.magic;
    }
}
