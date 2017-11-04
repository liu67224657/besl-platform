/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * A class implementing DataInputCustom used to read data out of nio channels.
 * Implementation-wise, it's a wrapper around a ByteBuffer that changes
 * it into a DataInputCustom interface (so that it's compatible with our
 * IO classes, which may read from either InputStreams or Channels).
 * Has some additional functionality to throw proper exceptions, etc.
 * <p/>
 * The methods implementing DataInputCustom and throwing an IOException will
 * throw a NotEnoughDataException when the buffer doesn't have enough
 * data to fulfill the request.  This is really just translating a
 * BufferUnderflowException into a propriatary exception for compilation
 * purposes (so older classes don't have to be modified to compile under
 * jdk's less than 1.4).  Normally, we'd just want to catch that
 * BufferUnderflowExceptions in the caller.
 */
public class DataInputStreamWrapperNIOBuffer implements DataInputCustom {
    private static int EOF = -1;

    private int maxSize = 131072;
    private int returnPoint = 0;
    private ByteBuffer buffer = null;

    /**
     * Construct this with a buffer size (in bytes).
     *
     * @param size    The initial buffer size, in bytes.
     * @param maxSize The absolute maximum size this buffer may grow to.
     *                If we overflow this buffer when it is already at its max size, it will throw an exception.
     */
    public DataInputStreamWrapperNIOBuffer(int size, int maxSize) {
        int allocSize = size;
        if (allocSize <= 0) {
            allocSize = 1;
        }
        int hardLimit = maxSize;
        if (hardLimit <= 0) {
            hardLimit = 1;
        }
        this.maxSize = hardLimit;

        buffer = ByteBuffer.allocate(allocSize);
        buffer.rewind();
        buffer.flip();
        returnPoint = buffer.position();
    }

    /**
     * Construct this with a ByteBuffer.  Note that passing a ByteBuffer
     * to this object means that the buffer WILL be operated on, so don't
     * plan on using the ByteBuffer directly anymore.
     * Use this constructor when using an existing buffer already containing data.
     * The limit should be set to the end of the data.
     *
     * @param bbuff The buffer to use.  The limit must be set appropriately.
     */
    public DataInputStreamWrapperNIOBuffer(ByteBuffer bbuff, int maxSize) {
        buffer = bbuff;
        if (buffer != null) {
            buffer.rewind();
            returnPoint = buffer.position();
        }

        int hardLimit = maxSize;
        if (hardLimit <= 0) {
            hardLimit = 1;
        }
        this.maxSize = hardLimit;
    }

    /**
     * Appends the bytes from a ByteBuffer to the end of our buffer.
     *
     * @param src The ByteBuffer to append bytes to
     */
    public void put(ByteBuffer src)
            throws IOException {
        if (src == null) {
            return;
        }

        try {
            int limit = buffer.limit();

            // check to see if we need to resize our buffer
            int newLimit = limit + src.remaining();
            if (newLimit > buffer.capacity()) {
                // a straight append will overflow this buffer; expand/flush the buffer
                int requiredCapacity = buffer.remaining() + src.remaining();
                int newCapacity = 2 * requiredCapacity;
                synchronized (buffer) {
                    buffer = p_resizeBuffer(buffer, requiredCapacity, newCapacity);
                }
            }

            // append the contents of our buffer, now that we've expanded it
            p_appendBuffer(buffer, src);
        }
        catch (BufferOverflowException e) {
            // if we still overflowed the buffer, then it has exceeded the hard limit and cannot
            // be expanded further.
            throw new TooMuchDataException("Cannot increase buffer size beyond absolute max of " +
                    getAbsoluteMaxBufferSize());
        }
    }

    /**
     * Resets the reading point on the buffer to the return point.
     */
    public void toReturnPoint()
            throws IOException {
        synchronized (buffer) {
            buffer.position(returnPoint);
        }
    }

    /**
     * Sets the return point to the current position on the buffer.
     */
    public void setReturnPoint()
            throws IOException {
        synchronized (buffer) {
            returnPoint = buffer.position();
        }
    }

    /**
     * Since the input buffer can be of varying size, we need to put a reasonable cap on our
     * capacity (in bytes).  This returns that cap.
     *
     * @return The largest buffer size we should be allowed to have, period.
     */
    public int getAbsoluteMaxBufferSize() {
        return maxSize;
    }

    /**
     * Implementing DataInputCustom.
     */
    public boolean readBoolean()
            throws IOException {
        try {
            byte b = buffer.get();
            if (b == 0) {
                return false;
            } else {
                return true;
            }
        }
        catch (BufferUnderflowException e) {
            throw new NotEnoughDataException(e.toString());
        }
    }

    /**
     * Implementing DataInputCustom.
     */
    public byte readByte()
            throws IOException {
        try {
            return buffer.get();
        }
        catch (BufferUnderflowException e) {
            throw new NotEnoughDataException(e.toString());
        }
    }

    /**
     * Implementing DataInputCustom.
     */
    public char readChar()
            throws IOException {
        try {
            return buffer.getChar();
        }
        catch (BufferUnderflowException e) {
            throw new NotEnoughDataException(e.toString());
        }
    }

    /**
     * Implementing DataInputCustom.
     */
    public double readDouble()
            throws IOException {
        try {
            return buffer.getDouble();
        }
        catch (BufferUnderflowException e) {
            throw new NotEnoughDataException(e.toString());
        }
    }

    /**
     * Implementing DataInputCustom.
     */
    public float readFloat()
            throws IOException {
        try {
            return buffer.getFloat();
        }
        catch (BufferUnderflowException e) {
            throw new NotEnoughDataException(e.toString());
        }
    }

    /* Please see DataInputCustom */
    /* This is not tested as NIO is not available as the time of implementation */
    public int readAvailable(byte[] b) throws IOException {
        if (b == null || b.length == 0) {
            throw new IllegalArgumentException("Destination byte array is null or of size of zero");
        }
        int numAvailable = buffer.remaining();

        if (numAvailable == 0) {
            throw new NotEnoughDataException("No data is in the buffer");
        }

        buffer.get(b, 0, numAvailable);

        return numAvailable;
    }

    /**
     * Implementing DataInputCustom.
     */
    public void readFully(byte[] b)
            throws IOException {
        try {
            buffer.get(b);
        }
        catch (BufferUnderflowException e) {
            throw new NotEnoughDataException(e.toString());
        }
    }

    /**
     * Implementing DataInputCustom.
     */
    public void readFully(byte[] b, int off, int len)
            throws IOException {
        try {
            buffer.get(b, off, len);
        }
        catch (BufferUnderflowException e) {
            throw new NotEnoughDataException(e.toString());
        }
    }

    /**
     * Implementing DataInputCustom.
     */
    public int readInt()
            throws IOException {
        try {
            return buffer.getInt();
        }
        catch (BufferUnderflowException e) {
            throw new NotEnoughDataException(e.toString());
        }
    }

    /**
     * Implementing DataInputCustom.
     */
    public String readLine()
            throws IOException {
        try {
            // read bytes one at a time, until a line terminator is reached.  Note that
            // since we are casting bytes to chars, this does not support all unicode characters,
            // according to the DataInput API.
            StringBuffer strbuf = new StringBuffer();
            byte b = 0;
            char c = Character.UNASSIGNED;
            boolean isFirst = true;
            while (c != Character.LINE_SEPARATOR && c != Character.LETTER_NUMBER && c != EOF) {
                b = buffer.get();
                c = (char) b;
                if (isFirst) {
                    if (c == EOF) {
                        // if the first character is an EOF, we return null
                        return null;
                    }
                } else {
                    isFirst = false;
                }

                strbuf.append(c);
            }

            // read the next character temporarily, and if it's a LINE_SEPARATOR, discard it also
            if (c == Character.LETTER_NUMBER) {
                buffer.mark();
                b = buffer.get();
                c = (char) b;
                if (c != Character.LINE_SEPARATOR) {
                    // oops, the previous character was the end of the line
                    buffer.reset();
                }
            }

            return strbuf.toString();
        }
        catch (BufferUnderflowException e) {
            throw new NotEnoughDataException(e.toString());
        }
    }

    /**
     * Implementing DataInputCustom.
     */
    public long readLong()
            throws IOException {
        try {
            return buffer.getLong();
        }
        catch (BufferUnderflowException e) {
            throw new NotEnoughDataException(e.toString());
        }
    }

    /**
     * Implementing DataInputCustom.
     */
    public short readShort()
            throws IOException {
        try {
            return buffer.getShort();
        }
        catch (BufferUnderflowException e) {
            throw new NotEnoughDataException(e.toString());
        }
    }

    /**
     * Implementing DataInputCustom.
     */
    public int readUnsignedByte()
            throws IOException {
        try {
            byte b = buffer.get();
            return (0x00000000 | (0x000000ff & b));
        }
        catch (BufferUnderflowException e) {
            throw new NotEnoughDataException(e.toString());
        }
    }

    /**
     * Implementing DataInputCustom.
     */
    public int readUnsignedShort()
            throws IOException {
        try {
            byte a = buffer.get();
            byte b = buffer.get();
            return (((a & 0xff) << 8) | (b & 0xff));
        }
        catch (BufferUnderflowException e) {
            throw new NotEnoughDataException(e.toString());
        }
    }

    /**
     * Implementing DataInputCustom.
     */
    public String readUTF()
            throws IOException {
        // this looks like a pain to implement.  since it's not being used anywhere, it's not
        // implemented here.  if you really need this function, feel free to write it.
        throw new RuntimeException("DataInputStreamWrapperNIOBuffer.readUTF() currently not implemented.  "
                + "Please implement or use another function.");
    }

    /**
     * Implementing DataInputCustom.  Note that this function does NOT throw NotEnoughDataException.  It simply
     * skips as many bytes as it can (up until the number of bytes asked for), and returns that number.
     */
    public int skipBytes(int n)
            throws IOException {
        int retval = 0;
        for (int k = 0; k < n; k++) {
            try {
                buffer.get();
                retval++;
            }
            catch (BufferUnderflowException e) {
                return retval;
            }
        }

        return retval;
    }

    /**
     * Returns a new resized ByteBuffer containing all the unread data of the old ByteBuffer.
     * Bytes which have already been read are not copied into the new buffer, so the
     * starting position of the new buffer is always zero, and the new limit is the old limit
     * minus the old position.
     *
     * @param oldBuffer   the old buffer to replicate.
     * @param newCapacity the capacity of the new buffer
     */
    private ByteBuffer p_resizeBuffer(ByteBuffer oldBuffer, int requiredCapacity, int newCapacity)
            throws BufferOverflowException {
        int position = oldBuffer.position();
        int limit = oldBuffer.limit();
        int newCap = oldBuffer.capacity();

        if (requiredCapacity >= newCap) {
            newCap = newCapacity;
            if (newCap > getAbsoluteMaxBufferSize()) {
                newCap = getAbsoluteMaxBufferSize();
            }
        }
        ByteBuffer newBuffer = ByteBuffer.allocate(newCap);
        newBuffer.put(oldBuffer);

        // reset our limits and positions
        oldBuffer.limit(limit);
        oldBuffer.position(position);
        newBuffer.limit(limit - position);
        newBuffer.position(0);

        // reset our return point to 0 since we've changed positions
        // client calls to change the return point must be synched around the buffer, so that
        // they don't use an old return point.
        returnPoint = 0;

        return newBuffer;
    }

    /**
     * Appends the data from a source ByteBuffer to a destination ByteBuffer,
     * updating the destination's limit but not its position.
     */
    private void p_appendBuffer(ByteBuffer dest, ByteBuffer src) {
        int destPos = dest.position();
        int destLim = dest.limit();
        int srcBytes = src.limit() - src.position();

        dest.position(destLim);
        dest.limit(destLim + srcBytes);
        dest.put(src);
        dest.position(destPos);
    }

    public String printBuffer() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("DataInputStreamWrapperNIOBuffer:\nposition=" + buffer.position()
                + "\nlimit=" + buffer.limit());
        return strbuf.toString();
    }

    public String printBufferFull() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("DataInputStreamWrapperNIOBuffer:\nposition=" + buffer.position()
                + "\nlimit=" + buffer.limit() + "\ncontent=\n");

        for (int i = buffer.position(); i < buffer.limit(); i++) {
            strbuf.append(i);
            strbuf.append(":");
            strbuf.append(buffer.get(i));
            strbuf.append(" ");
        }
        return strbuf.toString();
    }

    public String toString() {
        return printBuffer();
    }
}

