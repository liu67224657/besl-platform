/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;

import com.enjoyf.platform.util.HexUtil;
import com.enjoyf.platform.util.Refresher;
import com.enjoyf.platform.util.log.GAlerter;

/*
* Input packet base class. Note that it inherits from DataInputStream()
* so use that classes' methods to extract the contents of the
* packet (such as readInt(), readByte(), etc.).
*
* This class defines just a raw repository for info that will be
* sent over the wire. Derived classes should add behavior and/or
* headers.
*/
public class RPacketBase extends DataInputStream {
    private static Refresher refresher = new Refresher(60 * 1000, 0);
    /**
     * Holds the original byte array used to construct this packet.
     */
    private byte[] byteArray;
    /**
     * A string used to identify the source. Used for informational,
     * debug purposes. Typically set by whoever instantiates this object.
     * It may be left unset, however..
     */
    private String source = "UNSET";


    /**
     * Custom internal pointer. Used internally.
     */
    private Object internalPointer;


    /**
     * Construct an empty packet.
     */
    public RPacketBase() {
        super(new ByteArrayInputStream(new byte[0]));

        byteArray = null;
    }

    /**
     * Construct the packet from the passed in array.
     */
    public RPacketBase(byte[] barray) {
        super(new ByteArrayInputStream(barray, 0, barray.length));

        byteArray = barray;
    }

    /**
     * Since this class doesn't support serialization,
     * Here's a routine to read serializable objects.
     * Reading straight out of <in> might present a problem, in which case
     * we'll have to create a temporary ByteArrayInputStream object.
     */
    public Serializable readSerializable() {
        ObjectInputStream stream = null;
        Object result = null;

        try {
            stream = new ObjectInputStream(in);
            result = stream.readObject();
        } catch (InvalidClassException ice) {
            if (refresher.shouldRefresh()) {
                GAlerter.lab("source=" + source + ":serialization error: " + ice);
            }

            throw new SerializationException(ice);
        }
        //--
        // We catch these rare exceptions and convert them so that we
        // don't bug alert them later in the code.
        //--
        catch (StreamCorruptedException sce) {
            throw new StreamBadException("readSerializable: " + sce.toString(), sce);
        } catch (IOException e) {
            throw new RuntimeException("readSerializable():Caught IOException.", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("readSerializable():Caught ClassNotFoundException.", e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("readSerializable(): ERROR closing stream.", e);
                }
            }
        }

        return (Serializable) result;
    }

    /**
     * Read a string without throwing an exception that we
     * need to catch.
     *
     * @see DataInputStream
     * @see IOException
     */
    public String readString() {
        StringBuffer result = new StringBuffer();

        try {
            for (byte b = readByte(); b != 0; b = readByte()) {
                result.append((char) b);
            }
        } catch (IOException e) {
            throw new RuntimeException("RPacketBase.readString():source=" + source + " Caugh io exception : " + e);
        }

        return result.toString();
    }

    /**
     * Read a null-terminated stream of bytes in UTF-8 without throwing an
     * exception that we need to catch. Note the difference from readUTFNx;
     * this method exists to remain backwards-compatible with clients of
     * the roomserver while supporting the Unicode charset.
     *
     * @see DataInputStream
     * @see IOException
     */
    public String readStringUTF() {
        String s = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(255);

            byte b;
            while ((b = readByte()) != 0) {
                baos.write(b);
            }

            try {
                s = new String(baos.toByteArray(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                s = new String(baos.toByteArray());
            }
        } catch (IOException e) {
            throw new RuntimeException("RPacketBase.readStringUTF() Caught io exception : " + e);
        }

        return s;
    }

    /**
     * Read an integer without throwing an exception that we
     * need to catch.
     */
    public int readIntNx() {
        int s;

        try {
            s = readInt();
        } catch (IOException e) {
            throw new RuntimeException("RPacketBase.readIntNx() :source=" + source + " Caugh io exception : " + e);
        }
        return s;
    }

    public double readDoubleNx() {
        double s;

        try {
            s = readDouble();
        } catch (IOException e) {
            throw new RuntimeException("RPacketBase.readDoubleNx() :source=" + source + " Caugh io exception : " + e);
        }

        return s;
    }

    /**
     * Read a long without throwing an exception that we
     * need to catch.
     */
    public long readLongNx() {
        long s;

        try {
            s = readLong();
        }
        catch (IOException e) {
            throw new RuntimeException("RPacketBase.readLongNx() :source=" + source + " Caugh io exception : " + e);
        }

        return s;
    }

    /**
     * Read a boolean without throwing an exception that we
     * need to catch.
     */
    public boolean readBooleanNx() {
        boolean val;

        try {
            val = readBoolean();
        } catch (IOException e) {
            throw new RuntimeException("RPacketBase.readBooleanNx()" + ":source=" + source + " Caugh io exception : " + e);
        }

        return val;
    }

    public String readUTFNx() {
        String s;

        try {
            s = readUTF();
        } catch (IOException e) {
            throw new RuntimeException("RPacketBase.readUTFNx()" + ":source=" + source + " Caught io exception: " + e);
        }

        return s;
    }

    /**
     * Read a byte without throwing an exception that we
     * need to catch.
     */
    public byte readByteNx() {
        byte s;

        try {
            s = readByte();
        } catch (IOException e) {
            throw new RuntimeException("RPacketBase.readByteNx()" + ":source=" + source + " Caugh io exception : " + e);
        }

        return s;
    }

    /**
     * Read a short without throwing an exception that we
     * need to catch.
     */
    public short readShortNx() {
        short s;

        try {
            s = readShort();
        } catch (IOException e) {
            throw new RuntimeException("RPacketBase.readShortNx()" + ":source=" + source + " Caugh io exception : " + e);
        }

        return s;
    }

    public void readFullyNx(byte[] b) {
        try {
            readFully(b);
        } catch (IOException e) {
            throw new RuntimeException("RPacketBase.readFullyNx()" + ":source=" + source + " Caugh io exception : " + e);
        }
    }

    /**
     * Standard toString() conversion method.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (byteArray == null) {
            sb.append(": empty packet : ");
        } else {
            sb.append(HexUtil.toHex(byteArray));
        }

        return new String(sb);
    }

    /**
     * Returns the number of bytes left in the packet.
     */
    public int length() {
        if (byteArray == null) {
            return 0;
        } else {
            int len;

            try {
                len = available();
            } catch (IOException e) {
                len = 0;
            }

            return len;
        }
    }

    /**
     * Return the remaning bytes in a byte array.
     */
    public byte[] remaining() {
        byte[] array = new byte[length()];
        try {
            read(array);
        }
        catch (IOException e) {
            throw new RuntimeException(
                    "RPacketBase.remaining: "
                            + ":source=" + source
                            + "IO exception in read! " + e);
        }
        return array;
    }

    /**
     * Return the original buffer used to construct this packet.
     */
    public byte[] getOriginalBuffer() {
        return byteArray;
    }

    /**
     * Set an informational string that identifies the source of this
     * packet.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Retrieve the  an informational string which identifies the
     * source of this packet.
     */
    public String getSource() {
        return source;
    }


    public Object clone() {
        return new RPacketBase(byteArray);
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
