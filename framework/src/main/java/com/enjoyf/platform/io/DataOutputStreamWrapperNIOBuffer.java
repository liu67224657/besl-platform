package com.enjoyf.platform.io;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

/**
 * A class implementing DataOutputCustom used to read data out of nio channels.
 * Implementation-wise, it's a wrapper around a ByteBuffer that changes
 * it into a DataOutputCustom interface (so that it's compatible with our
 * IO classes, which may read from either InputStreams or Channels).
 * Has some additional functionality to throw proper exceptions, etc.
 * <p/>
 * The methods implementing DataOutputCustom and throwing an IOException will
 * throw a TooMuchDataException when the buffer doesn't have enough
 * data to fulfill the request.  This is really just translating a
 * BufferOverflowException into a propriatary exception for compilation
 * purposes (so older classes don't have to be modified to compile under
 * jdk's less than 1.4).  Normally, we'd just want to catch that
 * BufferOverflowExceptions in the caller.
 */
public class DataOutputStreamWrapperNIOBuffer implements DataOutputCustom {

    private int maxSize = 131072;
    private ByteBuffer buffer = null;

    /**
     * Construct this with a buffer size (in bytes).
     *
     * @param size The initial buffer size, in bytes.
     */
    public DataOutputStreamWrapperNIOBuffer(int size, int maxSize) {
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
    }

    /**
     * Construct this with a ByteBuffer.  Note that passing a ByteBuffer
     * to this object means that the buffer WILL be operated on, so don't
     * plan on using the ByteBuffer directly anymore.
     *
     * @param bbuff The buffer to use.
     */
    public DataOutputStreamWrapperNIOBuffer(ByteBuffer bbuff, int maxSize) {
        buffer = bbuff;
        if (buffer != null) {
            buffer.rewind();
        }

        int hardLimit = maxSize;
        if (hardLimit <= 0) {
            hardLimit = 1;
        }

        this.maxSize = hardLimit;
    }

    /**
     * Flushes the output, if possible.  If the underlying object is blocking, this method may block.
     */
    public void flush() throws IOException {
        // we can't force-flush something in nio, because we can't block.  do nothing.
    }


    /**
     * Writes as many available bytes as we can to the channel, then removes those bytes
     * from our buffer.  This is done by creating a new internal buffer of the same capacity,
     * and writing all the remaining data to that new buffer.
     *
     * @param channel The socketChannel to write to.
     * @return A buffer full of the requested bytes.
     */
    public void flushToChannel(ByteChannel channel) throws IOException {
        // Save the limit and position
        int position = buffer.position();

        if (position == 0) {
            // no data exists in the buffer, because the writing position is at the beginning.
            // do nothing.
            return;
        }

        // write whatever data we can to the channel
        buffer.flip();
        int bytesWritten = channel.write(buffer);

        // remove the written bytes from our internal buffer
        ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity());

        newBuffer.put(buffer);
        buffer = newBuffer;
        buffer.position(position - bytesWritten);
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
     * Implementing DataOutputCustom.
     */
    public void write(int b) throws IOException {
        try {
            expandIfNecessary(buffer, 1).put((byte) (0xff & b));
        }
        catch (BufferOverflowException e) {
            throw new TooMuchDataException(e.toString());
        }
    }

    /**
     * Implementing DataOutputCustom.
     */
    public void write(byte[] b) throws IOException {
        try {
            expandIfNecessary(buffer, b.length).put(b);
        }
        catch (BufferOverflowException e) {
            throw new TooMuchDataException(e.toString());
        }
    }

    /**
     * Implementing DataOutputCustom.
     */
    public void write(byte[] b, int off, int len)
            throws IOException {
        try {
            expandIfNecessary(buffer, len).put(b, off, len);
        }
        catch (BufferOverflowException e) {
            throw new TooMuchDataException(e.toString());
        }
    }

    /**
     * Implementing DataOutputCustom.
     */
    public void writeBoolean(boolean v) throws IOException {
        try {
            if (v) {
                expandIfNecessary(buffer, 1).put((byte) 1);
            } else {
                expandIfNecessary(buffer, 1).put((byte) 0);
            }
        }
        catch (BufferOverflowException e) {
            throw new TooMuchDataException(e.toString());
        }
    }

    /**
     * Implementing DataOutputCustom.
     */
    public void writeByte(int v) throws IOException {
        write(v);
    }

    /**
     * Implementing DataOutputCustom.
     */
    public void writeBytes(String s) throws IOException {
        try {
            // Write each character in the string as a single byte
            // (the lower-order byte of the char)
            expandIfNecessary(buffer, s.length());
            char c = 0;
            for (int i = 0; i < s.length(); i++) {
                c = s.charAt(i);
                buffer.put((byte) (0xff & c));
            }
        }
        catch (BufferOverflowException e) {
            throw new TooMuchDataException(e.toString());
        }
    }

    /**
     * Implementing DataOutputCustom.
     */
    public void writeChar(int v) throws IOException {
        try {
            expandIfNecessary(buffer, 2);
            buffer.put((byte) (0xff & (v >> 8)));
            buffer.put((byte) (0xff & v));
        }
        catch (BufferOverflowException e) {
            throw new TooMuchDataException(e.toString());
        }
    }

    /**
     * Implementing DataOutputCustom.
     */
    public void writeChars(String s) throws IOException {
        try {
            // Write each character in the string individually
            expandIfNecessary(buffer, 2 * s.length());
            char c = 0;
            for (int i = 0; i < s.length(); i++) {
                c = s.charAt(i);
                buffer.putChar(c);
            }
        }
        catch (BufferOverflowException e) {
            throw new TooMuchDataException(e.toString());
        }
    }

    /**
     * Implementing DataOutputCustom.
     */
    public void writeDouble(double v) throws IOException {
        try {
            expandIfNecessary(buffer, 8).putDouble(v);
        }
        catch (BufferOverflowException e) {
            throw new TooMuchDataException(e.toString());
        }
    }

    /**
     * Implementing DataOutputCustom.
     */
    public void writeFloat(float v) throws IOException {
        try {
            expandIfNecessary(buffer, 4).putFloat(v);
        }
        catch (BufferOverflowException e) {
            throw new TooMuchDataException(e.toString());
        }
    }

    /**
     * Implementing DataOutputCustom.
     */
    public void writeInt(int v) throws IOException {
        try {
            expandIfNecessary(buffer, 4).putInt(v);
        }
        catch (BufferOverflowException e) {
            throw new TooMuchDataException(e.toString());
        }
    }

    /**
     * Implementing DataOutputCustom.
     */
    public void writeLong(long v) throws IOException {
        try {
            expandIfNecessary(buffer, 8).putLong(v);
        }
        catch (BufferOverflowException e) {
            throw new TooMuchDataException(e.toString());
        }
    }

    /**
     * Implementing DataOutputCustom.
     */
    public void writeShort(int v) throws IOException {
        try {
            expandIfNecessary(buffer, 2);
            buffer.put((byte) (0xff & (v >> 8)));
            buffer.put((byte) (0xff & v));
        }
        catch (BufferOverflowException e) {
            throw new TooMuchDataException(e.toString());
        }
    }

    /**
     * Implementing DataOutputCustom.
     */
    public void writeUTF(String str) throws IOException {
        // this looks like a pain to implement.  since it's not being used anywhere, it's not
        // implemented here.  if you really need this function, feel free to write it.
        throw new RuntimeException("DataOutputStreamWrapperNIOBuffer.writeUTF() currently not implemented.  "
                + "Please implement or use another function.");
    }

    /**
     * Returns a new ByteBuffer, containing the same data and marks as the old buffer, but
     * with more capacity.
     *
     * @param oldBuffer   the old buffer to replicate
     * @param newCapacity the capacity of the new buffer
     */
    private ByteBuffer increaseCapacity(ByteBuffer oldBuffer, int newCapacity) throws BufferOverflowException {
        int position = oldBuffer.position();
        int limit = oldBuffer.limit();
        int newCap = newCapacity;

        if (newCap > getAbsoluteMaxBufferSize()) {
            newCap = getAbsoluteMaxBufferSize();
        }
        ByteBuffer newBuffer = ByteBuffer.allocate(newCap);
        oldBuffer.flip();
        newBuffer.put(oldBuffer);

        // reset our limits and positions
        oldBuffer.limit(limit);
        oldBuffer.position(position);
        newBuffer.position(position);

        return newBuffer;
    }

    /**
     * Returns a ByteBuffer that can accomodate the number of additional bytes specified.
     * If the original buffer is adequate, it returns the original buffer.  Otherwise
     * it returns a new buffer with an expanded capacity (unless we have already expanded
     * up to the hard limit on the buffer size), and sets buffer to that buffer.
     *
     * @param original    The original buffer to possibly expand
     * @param bytesNeeded The number of additional bytes needed.
     * @return A ByteBuffer that can hold bytesNeeded additional bytes
     */
    private ByteBuffer expandIfNecessary(ByteBuffer original, int bytesNeeded) {
        int newSize = original.position() + bytesNeeded;
        if (newSize <= original.limit()) {
            return original;
        }
        int newCap = original.capacity() * 2;
        if (newCap < newSize) {
            newCap = newSize + bytesNeeded;
        }

        buffer = increaseCapacity(original, newCap);

        return buffer;
    }

    public String printBuffer() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("DataOutputStreamWrapperNIOBuffer:\nposition=" + buffer.position() + ", limit=" + buffer.limit());
        return strbuf.toString();
    }

    public String printBufferFull() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("DataOutputStreamWrapperNIOBuffer:\nposition=" + buffer.position() + ", limit=" + buffer.limit() + "\ncontent=\n");

        for (int i = 0; i < buffer.position(); i++) {
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

