/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.IOException;

/**
 * A class for reading obfuscated data form an input stream.
 */
public class PacketReaderObfuscated extends PacketReaderLength {
    
    public PacketReaderObfuscated(int maxLength) {
        super(maxLength);
    }

    public RPacketBase readFrom(DataInputCustom dis) throws IOException {
        RPacketBase rp = super.readFrom(dis);

        return (new RPacketBase(unobfuscate(rp.getOriginalBuffer())));
    }

    private byte[] unobfuscate(byte[] buffer) {
        for (int i = 0; i < buffer.length; i++) {
            byte b = (byte) (buffer[i] ^ 0xFF);
            buffer[i] = b;
        }

        return buffer;
    }
}
