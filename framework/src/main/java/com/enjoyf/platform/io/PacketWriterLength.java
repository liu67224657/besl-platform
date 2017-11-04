/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.IOException;

public class PacketWriterLength extends PacketWriterBase {

    public PacketWriterLength() {
    }

    public void writeTo(DataOutputCustom dout, WPacketBase wp) throws IOException {
        synchronized (dout) {
            byte[] ba = wp.getOutBuffer();

            dout.writeShort(ba.length);
            dout.write(ba);
            dout.flush();
        }
    }
}

