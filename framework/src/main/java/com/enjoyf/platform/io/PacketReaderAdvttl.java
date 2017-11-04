/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.IOException;

/**
 * Concrete PacketReader class to read in advttl packets.
 */
public class PacketReaderAdvttl extends PacketReader {

    public PacketReaderAdvttl() {
        setMaxPacketLength(10 * 1024 * 1024);
    }

    public PacketReaderAdvttl(int maxLength) {
        setMaxPacketLength(maxLength);
    }

    public RPacketBase readFrom(DataInputCustom din) throws IOException {
        RPacket packet = null;

        synchronized (din) {
            PacketHeader header = new PacketHeader();

            //------------------------------------------------
            // Read the header in.
            //------------------------------------------------
            header.version = din.readByte();
            header.type = din.readByte();
            header.magic = din.readShort();
            header.length = din.readInt();
            header.tid = din.readInt();

            int len = header.length - PacketHeader.PACKET_HEADER_SIZE;

            //--------------------------------------------------
            // Just a length check to see if something is
            // screwed up.
            //--------------------------------------------------
            if (len > maxPacketLength || len < 0) {
                throw new PacketTooLargeException(len);
            }

            //--------------------------------------------------
            // Careful with 0 length packets. Some packets
            // have no data in them so we don't want to
            // post a read.
            //--------------------------------------------------
            if (len == 0) {
                packet = new RPacket();
            } else {
                byte[] barray = new byte[len];
                din.readFully(barray);
                packet = new RPacket(barray);
            }

            packet.header = header;
        }

        return packet;
    }
}

