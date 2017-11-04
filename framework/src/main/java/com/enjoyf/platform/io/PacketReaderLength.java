/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PacketReaderLength extends PacketReaderBase {

    public PacketReaderLength() {
    }

    public PacketReaderLength(int maxLength) {
        setMaxPacketLength(maxLength);
    }

    public RPacketBase readFrom(DataInputCustom din) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        synchronized (din) {
            int len = din.readShort();

            // check for max packet length
            if (len > maxPacketLength) {
                throw new PacketTooLargeException(len);
            }

            for (int i = 0; i < len; i++) {
                bos.write(din.readByte());
            }
        }

        return new RPacketBase(bos.toByteArray());
    }
}

