/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import com.enjoyf.platform.util.HexUtil;

/**
 * The fundamental class representing a packet of data (a transaction)
 * to be sent to a backend server.
 */
public class WPacketBase extends DataOutputStream {
    /**
     * Custom internal pointer. Used internally.
     */
    private Object internalPointer;


    /**
     * Construct an empty packet.
     */
    public WPacketBase() {
        super(new ByteArrayOutputStream());
    }

    /**
     * Since this class doesn't support serialization,
     * Here's a routine to write serializable objects.
     * Writing straight into <out> might present a problem, in which case
     * we'll have to create a temporary ByteArrayOutputStream object.
     *
     * @param obj object to write
     */
    public void writeSerializable(Serializable obj) {
        ObjectOutputStream stream = null;

        try {
            stream = new ObjectOutputStream(out);
            stream.writeObject(obj);
        } catch (IOException e) {
            throw new RuntimeException("WPacketBase.writeSerializable(): Caught IOException: " + e);
        } finally {
            if (stream != null) {
                try {
                    stream.flush();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("WPacketBase.writeSerializable(): ERROR closing stream - " + e);
                }
            }
        }
    }

    /**
     * A routine to write an array of bytes out. Won't throw
     * an exception.
     */
    public void writeNx(byte[] buf) {
        try {
            super.write(buf, 0, buf.length);
        } catch (IOException e) {
            throw new RuntimeException("WPacketBase.writeNx(): IO exception writing: " + e);
        }
    }

    /**
     * Writes the string to the packet. Adds a null at the end, ie
     * writes out a C string. Has the side effect of increasing the
     * packet length by one byte more than intended.
     *
     * @param s String to print
     */
    public final void writeString(String s) {
        writeStringInternal(s, true);
    }

    /**
     * Writes the string to the packet, without adding a null
     * char at the end.
     *
     * @param s String to print
     */
    public final void writeStringWithoutNull(String s) {
        writeStringInternal(s, false);
    }

    /**
     * Writes the string as a null-terminated stream of bytes in UTF-8.
     *
     * @param s String to write to stream
     */
    public final void writeStringUTF(String s) {
        try {
            byte[] buf;

            try {
                buf = s.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                buf = s.getBytes();
            }

            write(buf);
            writeByte(0);
        } catch (IOException e) {
            throw new RuntimeException("WPacketBase.writeStringUTF: got a IOException which should never happen!");
        }
    }

    /**
     * Internal writeString Implementation
     *
     * @param s  String to write to stream
     * @param nl If true, print a null at end of string, ie treat
     *           it as a C-string. Has the side effect of increasing the length
     *           of the packet by one byte more than what was meant.
     */
    private final void writeStringInternal(String s, boolean nl) {
        //--------------------------------------------------------
        // For strings, we can't use the default java string
        // encoding, we need to use C encoding where we add
        // a null at the end of the guy.
        //--------------------------------------------------------

        try {
            writeBytes(s);

            if (nl) {
                writeByte(0);
            }
        } catch (IOException e) {
            throw new RuntimeException("WPacketBase.writeString: got a IOException which should never happen!");
        }
    }

    /**
     * Normally, one would use the writeByte() routine of
     * DataOutputStream directly, but that method throws an
     * IO exception which must be caught. This routine does that
     * for us. Since our ultimate destination is a
     * ByteArrayOutputStream, we should not get any exceptions.
     */
    public void writeByteNx(byte val) {
        try {
            writeByte(val);
        } catch (IOException e) {
            throw new RuntimeException("WPacketBase.writeByteNx: Unexpected IO exc. " + e);
        }
    }

    /**
     * Write a short without worrying about an exception.
     */
    public void writeShortNx(short val) {
        try {
            writeShort(val);
        } catch (IOException e) {
            throw new RuntimeException("WPacketBase.writeShortNx: Unexpected IO exc. " + e);
        }
    }

    /**
     * Write an int without worrying about an exception.
     */
    public void writeIntNx(int val) {
        try {
            writeInt(val);
        } catch (IOException e) {
            throw new RuntimeException("WPacketBase.writeIntNx: Unexpected IO exc. " + e);
        }
    }

    /**
     * Write a long without worrying about an exception.
     */
    public void writeLongNx(long val) {
        try {
            writeLong(val);
        } catch (IOException e) {
            throw new RuntimeException("WPacketBase.writeLongNx: nexpected IO exc. " + e);
        }
    }

    /**
     * Write a long without worrying about an exception.
     */
    public void writeDoubleNx(double val) {
        try {
            writeDouble(val);
        } catch (IOException e) {
            throw new RuntimeException("WPacketBase.writeDoubleNx: Unexpected IO exc. " + e);
        }
    }

    /**
     * Write a boolean without worrying about an exception.
     */
    public void writeBooleanNx(boolean val) {
        try {
            writeBoolean(val);
        } catch (IOException e) {
            throw new RuntimeException("WPacketBase.writeBooleanNx: Unexpected IO exc. " + e);
        }
    }

    public void writeUTFNx(String s) {
        try {
            writeUTF(s);
        } catch (IOException e) {
            throw new RuntimeException("WPacketBase.writeUTFNx: Unexpected IO exc. " + e);
        }
    }

    /**
     * Return the output buffer as a byte array. Note that the
     * returned array is allocated, and the bytes copied into it
     * from this object.
     */
    public byte[] getOutBuffer() {
        return ((ByteArrayOutputStream) out).toByteArray();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(HexUtil.toHex(getOutBuffer()));

        return new String(sb);
    }

    /**
     * Return the number of bytes currently in this packet.
     */
    public int length() {
        return ((ByteArrayOutputStream) out).size();
    }


    /**
     * Sets the custom internal pointer. Used internally
     * for tracking of custom information
     *
     * @return
     */
    public Object getIPtr() {
        return internalPointer;
    }


    /**
     * Sets the custom internal pointer. Used internally
     * for tracking of custom information
     *
     * @param iPtr
     */
    public void setIPtr(Object iPtr) {
        this.internalPointer = iPtr;
    }
}
