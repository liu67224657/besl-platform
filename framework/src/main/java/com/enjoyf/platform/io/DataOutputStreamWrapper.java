/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * A wrapper class around DataOutputStream that implements DataOutputCustom instead of DataOutput.
 */
public class DataOutputStreamWrapper implements DataOutputCustom {
    private DataOutputStream outputStream = null;

    /**
     * Constructor.  Takes an output stream to wrap around.
     *
     * @param output The output stream we're wrapping around.
     */
    public DataOutputStreamWrapper(DataOutputStream output) {
        outputStream = output;
    }

    /**
     * Flushes the output, if possible.  If the underlying object is blocking, this method may block.
     */
    public void flush() throws IOException {
        outputStream.flush();
    }

    /**
     * Writes the lower 8 bits of an int to the output.  See java.io.DataOutput.
     *
     * @param b The byte to write.
     * @throws IOException, If there is an I/O error.
     */
    public void write(int b) throws IOException {
        outputStream.write(b);
    }

    /**
     * Writes an array of bytes to the output.  See java.io.DataOutput.
     *
     * @param b The bytes to write.
     * @throws IOException, If there is an I/O error.
     */
    public void write(byte[] b) throws IOException {
        outputStream.write(b);
    }

    /**
     * Writes an array of bytes to the output.  See java.io.DataOutput.
     *
     * @param b   The bytes to write.
     * @param off The offset to begin writing at.
     * @param len The number of bytes to write.
     * @throws IOException, If there is an I/O error.
     */
    public void write(byte[] b, int off, int len) throws IOException {
        outputStream.write(b, off, len);
    }

    /**
     * Writes a boolean to the output.  See java.io.DataOutput.
     *
     * @param v The boolean to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeBoolean(boolean v) throws IOException {
        outputStream.writeBoolean(v);
    }

    /**
     * Writes the lower 8 bits to the output.  See java.io.DataOutput.
     *
     * @param v The byte to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeByte(int v) throws IOException {
        outputStream.writeByte(v);
    }

    /**
     * Writes a String to the output, one byte per char.  See java.io.DataOutput.
     *
     * @param s The String to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeBytes(String s) throws IOException {
        outputStream.writeBytes(s);
    }

    /**
     * Writes a char to the output.  See java.io.DataOutput.
     *
     * @param v The char to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeChar(int v) throws IOException {
        outputStream.writeChar(v);
    }

    /**
     * Writes a String to the output, char for char.  See java.io.DataOutput.
     *
     * @param s The String to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeChars(String s) throws IOException {
        outputStream.writeChars(s);
    }

    /**
     * Writes a double to the output.  See java.io.DataOutput.
     *
     * @param v The double to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeDouble(double v) throws IOException {
        outputStream.writeDouble(v);
    }

    /**
     * Writes a float to the output.  See java.io.DataOutput.
     *
     * @param v The float to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeFloat(float v) throws IOException {
        outputStream.writeFloat(v);
    }

    /**
     * Writes an int to the output.  See java.io.DataOutput.
     *
     * @param v The int to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeInt(int v) throws IOException {
        outputStream.writeInt(v);
    }

    /**
     * Writes a long to the output.  See java.io.DataOutput.
     *
     * @param v The long to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeLong(long v) throws IOException {
        outputStream.writeLong(v);
    }

    /**
     * Writes a short to the output as the lower two bytes of an int.  See java.io.DataOutput.
     *
     * @param v The short to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeShort(int v) throws IOException {
        outputStream.writeShort(v);
    }

    /**
     * Writes a UTF formatted string to the output.  See java.io.DataOutput.
     *
     * @param str The String to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeUTF(String str) throws IOException {
        outputStream.writeUTF(str);
    }
}

