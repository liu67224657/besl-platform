/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.IOException;

/**
 * A class for writing delimited data into an output stream.
 */
public class PacketWriterDelimited extends PacketWriterBase {
    protected int delimiter = Integer.MIN_VALUE;

    public PacketWriterDelimited(int delim) {
        delimiter = delim;
    }

    public void writeTo(DataOutputCustom dout, WPacketBase wp) throws IOException {
        synchronized (dout) {
            dout.write(wp.getOutBuffer());
            dout.write(delimiter);
            dout.flush();
        }
    }
}
