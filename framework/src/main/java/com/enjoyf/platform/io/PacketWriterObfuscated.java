/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.IOException;

/**
 * A class for writing obfuscated data into an output stream.
 */
public class PacketWriterObfuscated extends PacketWriterLength {

    public PacketWriterObfuscated() {
        super();
    }

    public void writeTo(DataOutputCustom dout, WPacketBase wp) throws IOException {
        WPacketBase ob = new WPacketBase();

        ob.write(obfuscate(wp.getOutBuffer()));

        super.writeTo(dout, ob);
    }

    private byte[] obfuscate(byte[] buffer) {
        for (int i = 0; i < buffer.length; i++) {
            byte b = (byte) (buffer[i] ^ 0xFF);
            buffer[i] = b;
        }

        return buffer;
    }
}
