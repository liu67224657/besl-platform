/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * A wrapper class around DataInputStream that implements DataInputCustom instead of DataInput.
 */
public class DataInputStreamWrapper implements DataInputCustom {
    private DataInputStream inputStream = null;

    /**
     * Constructor.  Takes an input stream to wrap around.
     *
     * @param input The input stream we're wrapping around.
     */
    public DataInputStreamWrapper(DataInputStream input) {
        inputStream = input;
    }

    /**
     * Reads a boolean value from the input.  See java.io.DataInput.
     *
     * @return The boolean read.
     * @throws IOException, If there is an I/O error.
     */
    public boolean readBoolean() throws IOException {
        return inputStream.readBoolean();
    }

    /**
     * Reads a byte from the input.  See java.io.DataInput.
     *
     * @return The byte read.
     * @throws IOException, If there is an I/O error.
     */
    public byte readByte() throws IOException {
        return inputStream.readByte();
    }

    /**
     * Reads a char from the input.  See java.io.DataInput.
     *
     * @return The char read.
     * @throws IOException, If there is an I/O error.
     */
    public char readChar() throws IOException {
        return inputStream.readChar();
    }

    /**
     * Reads a double from the input.  See java.io.DataInput.
     *
     * @return The double read.
     * @throws IOException, If there is an I/O error.
     */
    public double readDouble() throws IOException {
        return inputStream.readDouble();
    }

    /**
     * Reads a float from the input.  See java.io.DataInput.
     *
     * @return The float read.
     * @throws IOException, If there is an I/O error.
     */
    public float readFloat() throws IOException {
        return inputStream.readFloat();
    }

    /* Please see DataInputCustom */
    public int readAvailable(byte[] b) throws IOException {
        if (b == null || b.length == 0) {
            throw new IllegalArgumentException("Destination byte array is null or of size of zero");
        }

        int numAvailable = inputStream.available();
        int offset = 0;

        if (numAvailable == 0) {    // nothing available, then block for the first byte
            b[offset] = inputStream.readByte(); // block until the first byte is returned
            offset++;

            numAvailable = inputStream.available();    // check if there are more available
        }

        int byteRead;

        // get the min. value between number of available remaining array
        // space and available bytes in buffer
        if (b.length - offset < numAvailable) {
            byteRead = b.length - offset;
        } else {
            byteRead = numAvailable;
        }

        inputStream.readFully(b, offset, byteRead);

        return byteRead + offset;
    }


    /**
     * Reads a byte array from the input.  See java.io.DataInput.
     *
     * @param b The byte array to read into.
     * @throws IOException, If there is an I/O error.
     */
    public void readFully(byte[] b) throws IOException {
        inputStream.readFully(b);
    }

    /**
     * Reads a byte array from the input.  See java.io.DataInput.
     *
     * @param b   The byte array to read into.
     * @param off The offset to begin reading at.
     * @param len The number of bytes to read.
     * @throws IOException, If there is an I/O error.
     */
    public void readFully(byte[] b, int off, int len) throws IOException {
        inputStream.readFully(b, off, len);
    }

    /**
     * Reads an int from the input.  See java.io.DataInput.
     *
     * @return The int read.
     * @throws IOException, If there is an I/O error.
     */
    public int readInt() throws IOException {
        return inputStream.readInt();
    }

    /**
     * Reads a String from the input until a line terminator is reached.  See java.io.DataInput.
     *
     * @return The String read.
     * @throws IOException, If there is an I/O error.
     */
    public String readLine() throws IOException {
        // the read line method in DataInputStream is deprecated.  If someone needs this function, they
        // need to find another way to implement it.
        //return inputStream.readLine();
        throw new RuntimeException("Method not implemented in DataInputStreamWrapper, "
                + "because the method is deprecated as of jdk1.1 "
                + "in the underlying DataInputStream.");
    }

    /**
     * Reads a long from the input.  See java.io.DataInput.
     *
     * @return The long read.
     * @throws IOException, If there is an I/O error.
     */
    public long readLong() throws IOException {
        return inputStream.readLong();
    }

    /**
     * Reads a short from the input.  See java.io.DataInput.
     *
     * @return The short read.
     * @throws IOException, If there is an I/O error.
     */
    public short readShort() throws IOException {
        return inputStream.readShort();
    }

    /**
     * Reads an unsigned byte from the input.  See java.io.DataInput.
     *
     * @return The byte read, as an int.
     * @throws IOException, If there is an I/O error.
     */
    public int readUnsignedByte() throws IOException {
        return inputStream.readUnsignedByte();
    }

    /**
     * Reads an unsigned short from the input.  See java.io.DataInput.
     *
     * @return The short read, as an int.
     * @throws IOException, If there is an I/O error.
     */
    public int readUnsignedShort() throws IOException {
        return inputStream.readUnsignedShort();
    }

    /**
     * Reads a UTF formatted String from the input.  See java.io.DataInput.
     *
     * @return The String read.
     * @throws IOException, If there is an I/O error.
     */
    public String readUTF() throws IOException {
        return inputStream.readUTF();
    }

    /**
     * Sets the return point to the current reading position on the input.
     *
     * @throws IOException, If there is an I/O error.
     */
    public void setReturnPoint() throws IOException {
        // in this case we do nothing; this is an NIO thing.
    }

    /**
     * Skips n bytes on the input.  See java.io.DataInput.
     *
     * @param n The number of bytes to attempt to skip.
     * @return The actual number of bytes skipped.
     * @throws IOException, If there is an I/O error.
     */
    public int skipBytes(int n) throws IOException {
        return inputStream.skipBytes(n);
    }

    /**
     * Resets the reading point on the input to the last place marked.
     *
     * @throws IOException, If there is an I/O error.
     */
    public void toReturnPoint() throws IOException {
        // in this case we do nothing;  this is an NIO thing.
    }
}

