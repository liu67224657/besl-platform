/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.IOException;

public class PacketWriterStreaming extends PacketWriter {

    public void writeTo(DataOutputCustom dout, WPacketBase wp) throws IOException {
        synchronized (dout) {
            dout.write(wp.getOutBuffer());
            dout.flush();
        }
    }
}

