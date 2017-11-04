/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

/**
 * Generic packet class with a specific header. This is the basic packet
 * that is used by the service.
 */
public class WPacket extends WPacketBase {
    public PacketHeader header = new PacketHeader();

    public String toString() {
        StringBuffer sb = new StringBuffer(header.toString());

        sb.append(super.toString());
        
        return new String(sb);
    }

    /**
     * Write the string specified into the packet, null padding
     * according to length.
     *
     * @param s   The string to write.
     * @param len The no. of bytes to write out. This is *exactly*
     *            the number of bytes written.
     */
    public void writeFixedString(String s, int len) {
        writeStringWithoutNull(s);
        int l = len - s.length();
        if (l > 0) {
            byte[] b = new byte[l];
            for (int i = 0; i < l; i++) {
                b[i] = 0;
            }
            writeNx(b);
        }
    }

    public PacketHeader getHeader() {
        return header;
    }

    public void setType(byte type) {
        header.type = type;
    }

    public int getTid() {
        return header.tid;
    }

    public void setTid(int tid) {
        header.tid = tid;
    }

    public void setMagic(short magic) {
        header.magic = magic;
    }

    public void setMetricsType(byte metricsType) {
        header.magic = metricsType;
    }

    public byte getMetricsType() {
        return (byte) header.magic;
    }
}
