/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import com.enjoyf.platform.io.UTF8InputStreamReader;

/**
 * Simple InputStream which converts from any arbitrary encoding to the
 * freaky Latin1-plus-Unicode-escapes encoding that
 * {@link java.util.Properties#load} expects to see.
 */
public class PropertiesEncodingInputStream extends InputStream {
    private Reader reader;
    private byte buffer[];
    private int bufferPos;

    /**
     * Constructs a new PropertiesEncodingInputStream on some underlying
     * InputStream, which is assumed to be in some specified encoding
     * (usually UTF-8).
     *
     * @param in       InputStream to wrap
     * @param encoding name of the InputStream's encoding
     * @throws UnsupportedEncodingException if the specified encoding is
     *                                      not supported
     */
    public PropertiesEncodingInputStream(InputStream in, String encoding)
            throws UnsupportedEncodingException {
        // Make a Reader for the specified encoding
        reader = UTF8InputStreamReader.makeInputStreamReader(in, encoding);

        // Initialize our temporary \\uxxxx conversion buffer.
        buffer = new byte[6];
        buffer[0] = '\\';
        buffer[1] = 'u';
        bufferPos = buffer.length;
    }

    public void close() throws IOException {
        reader.close();
    }

    /**
     * Reads a byte from the underlying InputStream, doing some magic
     * conversions at the same time.
     *
     * @return next byte from the InputStream, or -1 if EOF has been reached
     * @throws IOException if an error occurred
     */
    public int read() throws IOException {
        // Are we reading from the temporary conversion buffer?
        if (bufferPos < buffer.length) {
            return buffer[bufferPos++];
        }

        // Read a character (note a byte!) from the original InputStream,
        // discarding all Unicode byte order marks (0xfeff) in the logicProcess.
        // Some editing tools (e.g. Windows Notepad) prepend a BOM when
        // they create a UTF-8 encoded file.
        int ch;
        do {
            ch = reader.read();
        } while (ch == 0xfeff);

        // If the character is -1 (EOF), or is in the Latin1 range (\u0000 -
        // \u00ff), we can return it as-is, since Latin1 coincides with
        // Unicode for those code points.
        if (ch <= 0x00ff) {
            return ch;
        }

        // The character is not in the Latin1 repertoire, so we must encode
        // it as a \\uxxxx escape. Stuff the hex digits into the buffer.
        buffer[2] = toHexDigit((ch & 0xf000) >> 12);
        buffer[3] = toHexDigit((ch & 0x0f00) >> 8);
        buffer[4] = toHexDigit((ch & 0x00f0) >> 4);
        buffer[5] = toHexDigit(ch & 0x000f);
        bufferPos = 0;

        // Return the first character from the buffer. Subsequent read()
        // calls will also come from this buffer, until it's used up, at
        // which point we'll return to reading from the InputStream.
        return buffer[bufferPos++];
    }

    private byte toHexDigit(int nybble) {
        return (byte) (nybble + ((nybble <= 9) ? '0' : ('a' - 10)));
    }
}
