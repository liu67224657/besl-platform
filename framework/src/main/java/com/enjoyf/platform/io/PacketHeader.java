/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

/**
 * Package private class for use by WPacket and RPacket. This is the header
 * that gets put into every packet.
 *
 * @see WPacket , RPacket, Transactor
 */
public class PacketHeader implements Cloneable {
    public static final int PACKET_HEADER_SIZE = 12;

    public byte version = 1;
    public byte type = 0;
    public short magic = 0;
    public int length = PACKET_HEADER_SIZE;
    public int tid = 0;

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("version\t= " + version + "\n");
        sb.append("type\t= " + type + "\n");
        sb.append("magic\t= " + magic + "\n");
        sb.append("length\t= " + length + "\n");
        sb.append("tid\t= " + tid + "\n");
        String s = new String(sb);
        
        return s;
    }

    public Object clone() {
        PacketHeader ph = new PacketHeader();

        ph.version = version;
        ph.type = type;
        ph.magic = magic;
        ph.length = length;
        ph.tid = tid;

        return ph;
    }
}
