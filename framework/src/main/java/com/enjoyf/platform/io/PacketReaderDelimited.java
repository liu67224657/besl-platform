/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A class for reading delimited data form an input stream.
 */
public class PacketReaderDelimited extends PacketReaderBase {
    protected int delimiter = Integer.MIN_VALUE;

    public PacketReaderDelimited(int delim) {
        delimiter = delim;
    }

    public PacketReaderDelimited(int maxPacketLength, int delim) {
        setMaxPacketLength(maxPacketLength);
        delimiter = delim;
    }

    public RPacketBase readFrom(DataInputCustom din) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len = 0;

        synchronized (din) {
            for (byte data = din.readByte(); data != delimiter; data = din.readByte()) {
                // REMOVED with conversion to DataInput.readByte from InputStream.read()
                // This is because bytes will be 0 to 255, and throw an exception on EOF
                // check for end of stream
                //if (data == -1) {
                //	throw new IOException("end of stream reached");
                //}

                // check for max packet length
                len++;
                if (len > maxPacketLength) {
                    throw new PacketTooLargeException(len);
                }

                bos.write(data);
            }
        }

        return new RPacketBase(bos.toByteArray());
    }
}
