/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

public class PacketTooLargeException extends java.io.IOException {
    private int packetSize = -1;

    public PacketTooLargeException(int size, String msg) {
        super(msg);

        packetSize = size;
    }

    public PacketTooLargeException(int size) {
        this(size, "Packet too large!");
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(super.toString());

        sb.append(":packetSize=" + packetSize);

        return new String(sb);
    }
}
