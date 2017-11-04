/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.IOException;

/**
 * An interface for reading data.  To be used in place of java.io.DataInput.  If we
 * could've either extended DataInput to add functions, or named this class DataInput,
 * that would be ideal.  But we can't, hence the 'Custom'.
 */
public interface DataInputCustom {
    /**
     * Reads a boolean value from the input.  See java.io.DataInput.
     *
     * @return The boolean read.
     * @throws IOException, If there is an I/O error.
     */
    public boolean readBoolean() throws IOException;

    /**
     * Reads a byte from the input.  See java.io.DataInput.
     *
     * @return The byte read.
     * @throws IOException, If there is an I/O error.
     */
    public byte readByte() throws IOException;

    /**
     * Reads a char from the input.  See java.io.DataInput.
     *
     * @return The char read.
     * @throws IOException, If there is an I/O error.
     */
    public char readChar() throws IOException;

    /**
     * Reads a double from the input.  See java.io.DataInput.
     *
     * @return The double read.
     * @throws IOException, If there is an I/O error.
     */
    public double readDouble() throws IOException;

    /**
     * Reads a float from the input.  See java.io.DataInput.
     *
     * @return The float read.
     * @throws IOException, If there is an I/O error.
     */
    public float readFloat() throws IOException;

    /**
     * Read a byte array from the input.
     * <p/>
     * <br> The specialized method only read what is available in the
     * buffer if the buffer is not empty.
     * <p/>
     * <br> In the case that buffer is empty:
     * <p/>
     * IF NIO <B>IS</B> USED, it will throws NotEnoughDataException, just
     * like other methods.
     * <p/>
     * IF NIO <B>IS NOT</B> used, it will be blocked and wait until the
     * buffer has at least "something", and return the data
     *
     * @param b the byte array to read into
     * @return the number of data read.  it will be greater than 0 but
     *         equals or less then the size of the given array.
     */
    public int readAvailable(byte[] b) throws IOException;

    /**
     * Reads a byte array from the input.  See java.io.DataInput.
     *
     * @param b The byte array to read into.
     * @throws IOException, If there is an I/O error.
     */
    public void readFully(byte[] b) throws IOException;

    /**
     * Reads a byte array from the input.  See java.io.DataInput.
     *
     * @param b   The byte array to read into.
     * @param off The offset to begin reading at.
     * @param len The number of bytes to read.
     * @throws IOException, If there is an I/O error.
     */
    public void readFully(byte[] b, int off, int len) throws IOException;

    /**
     * Reads an int from the input.  See java.io.DataInput.
     *
     * @return The int read.
     * @throws IOException, If there is an I/O error.
     */
    public int readInt() throws IOException;

    /**
     * Reads a String from the input until a line terminator is reached.  See java.io.DataInput.
     *
     * @return The String read.
     * @throws IOException, If there is an I/O error.
     */
    public String readLine() throws IOException;

    /**
     * Reads a long from the input.  See java.io.DataInput.
     *
     * @return The long read.
     * @throws IOException, If there is an I/O error.
     */
    public long readLong() throws IOException;

    /**
     * Reads a short from the input.  See java.io.DataInput.
     *
     * @return The short read.
     * @throws IOException, If there is an I/O error.
     */
    public short readShort() throws IOException;

    /**
     * Reads an unsigned byte from the input.  See java.io.DataInput.
     *
     * @return The byte read, as an int.
     * @throws IOException, If there is an I/O error.
     */
    public int readUnsignedByte() throws IOException;

    /**
     * Reads an unsigned short from the input.  See java.io.DataInput.
     *
     * @return The short read, as an int.
     * @throws IOException, If there is an I/O error.
     */
    public int readUnsignedShort() throws IOException;

    /**
     * Reads a UTF formatted String from the input.  See java.io.DataInput.
     *
     * @return The String read.
     * @throws IOException, If there is an I/O error.
     */
    public String readUTF() throws IOException;

    /**
     * Sets the return point to the current reading position on the input.
     *
     * @throws IOException, If there is an I/O error.
     */
    public void setReturnPoint() throws IOException;

    /**
     * Skips n bytes on the input.  See java.io.DataInput.
     *
     * @param n The number of bytes to attempt to skip.
     * @return The actual number of bytes skipped.
     * @throws IOException, If there is an I/O error.
     */
    public int skipBytes(int n) throws IOException;

    /**
     * Resets the reading point on the input to the return point.
     *
     * @throws IOException, If there is an I/O error.
     */
    public void toReturnPoint() throws IOException;
}
