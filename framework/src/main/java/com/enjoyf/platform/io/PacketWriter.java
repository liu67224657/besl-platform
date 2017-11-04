/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.IOException;

/**
 * Abstract class to be used when writing a std WPacket out. Since
 * we are dealing with multiple protocols, certain destinations may
 * require munged packets. Concrete classes derived from this one
 * deal with implementing that.
 */
public abstract class PacketWriter extends PacketWriterBase {
    /**
     * This method is called to write the packet out to the output
     * stream.
     *
     * @param dout The DataOutputCustom to write to.
     * @param wp   The packet to write.
     * @throws IOException Thrown whenever there is a problem
     *                     writing to the output stream.
     */
    public abstract void writeTo(DataOutputCustom dout, WPacketBase wp) throws IOException;
}
