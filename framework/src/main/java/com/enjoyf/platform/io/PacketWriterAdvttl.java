/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Concrect class used to write packets in advttl protocol.
 */
public class PacketWriterAdvttl extends PacketWriter {
	
	private static final Logger logger = LoggerFactory.getLogger(PacketWriterAdvttl.class);
	
    /**
     * This method will write the passed in packet to the passed
     * in DataOutputStream. It will write it in advttl format.
     *
     * @param dout The DataOutputCustom to write the packet to.
     * @param wp   The packet to write.
     */
    public void writeTo(DataOutputCustom dout, WPacketBase wp) throws IOException {
    	if (logger.isTraceEnabled()) {
    		logger.trace("PacketWriterAdvttl: Sending a packet: " + wp);
    	}

        WPacket wpack = (WPacket) wp;

        synchronized (dout) {
            //--------------------------------------------------------
            // Write the header out.
            //--------------------------------------------------------
            PacketHeader hdr = wpack.getHeader();

            dout.writeByte(hdr.version);
            dout.writeByte(hdr.type);
            dout.writeShort(hdr.magic);
            hdr.length = PacketHeader.PACKET_HEADER_SIZE + wp.length();
            dout.writeInt(hdr.length);
            dout.writeInt(hdr.tid);

            //--------------------------------------------------------
            // Write the data portion.
            //--------------------------------------------------------
            dout.write(wp.getOutBuffer());

            // flush if possible
            dout.flush();
        }

        if (logger.isTraceEnabled()) {
        	logger.trace("PacketWriterAdvttl: Packet sent: " + wp);
        }
    }
}
