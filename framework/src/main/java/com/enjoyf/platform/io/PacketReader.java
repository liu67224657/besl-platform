/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.IOException;

/**
 * This is an abstract class for reading in packets from an input
 * stream. It is used by the PacketReceiver object to read in packets to deal
 * with different packet formats. Note that while it returns an
 * RPacketBase, it is really returning an RPacket.
 */
public abstract class PacketReader extends PacketReaderBase {

    /**
     * Routine is called to read a packet in from the given
     * input stream.
     */
    public abstract RPacketBase readFrom(DataInputCustom din) throws IOException;
}
