/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.IOException;

/**
 * An interface for writing data.  To be used in place of java.io.DataOutput.  If we
 * could've either extended DataOutput to add functions, or named this class DataOutput,
 * that would be ideal.  But we can't, hence the 'Custom'.
 */
public interface DataOutputCustom {
    /**
     * Flushes the output, if possible.  If the underlying object is blocking, this method may block.
     */
    public void flush() throws IOException;

    /**
     * Writes the lower 8 bits of an int to the output.  See java.io.DataOutput.
     *
     * @param b The byte to write.
     * @throws IOException, If there is an I/O error.
     */
    public void write(int b) throws IOException;

    /**
     * Writes an array of bytes to the output.  See java.io.DataOutput.
     *
     * @param b The bytes to write.
     * @throws IOException, If there is an I/O error.
     */
    public void write(byte[] b) throws IOException;

    /**
     * Writes an array of bytes to the output.  See java.io.DataOutput.
     *
     * @param b   The bytes to write.
     * @param off The offset to begin writing at.
     * @param len The number of bytes to write.
     * @throws IOException, If there is an I/O error.
     */
    public void write(byte[] b, int off, int len) throws IOException;

    /**
     * Writes a boolean to the output.  See java.io.DataOutput.
     *
     * @param v The boolean to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeBoolean(boolean v) throws IOException;

    /**
     * Writes the lower 8 bits to the output.  See java.io.DataOutput.
     *
     * @param v The byte to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeByte(int v) throws IOException;

    /**
     * Writes a String to the output, one byte per char.  See java.io.DataOutput.
     *
     * @param s The String to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeBytes(String s) throws IOException;

    /**
     * Writes a char to the output.  See java.io.DataOutput.
     *
     * @param v The char to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeChar(int v) throws IOException;

    /**
     * Writes a String to the output, char for char.  See java.io.DataOutput.
     *
     * @param s The String to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeChars(String s) throws IOException;

    /**
     * Writes a double to the output.  See java.io.DataOutput.
     *
     * @param v The double to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeDouble(double v) throws IOException;

    /**
     * Writes a float to the output.  See java.io.DataOutput.
     *
     * @param v The float to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeFloat(float v) throws IOException;

    /**
     * Writes an int to the output.  See java.io.DataOutput.
     *
     * @param v The int to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeInt(int v) throws IOException;

    /**
     * Writes a long to the output.  See java.io.DataOutput.
     *
     * @param v The long to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeLong(long v) throws IOException;

    /**
     * Writes a short to the output as the lower two bytes of an int.  See java.io.DataOutput.
     *
     * @param v The short to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeShort(int v) throws IOException;

    /**
     * Writes a UTF formatted string to the output.  See java.io.DataOutput.
     *
     * @param str The String to write.
     * @throws IOException, If there is an I/O error.
     */
    public void writeUTF(String str) throws IOException;
}

