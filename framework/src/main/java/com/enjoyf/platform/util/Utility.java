/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * Static class providing miscellaneous utility methods.
 */
public class Utility {
    private static final String DEFAULT_TO_STRING = "NULL";
    private static final int DEFAULT_HASH_CODE = 0;
    private static final double SIZE_GB = 1024 * 1024 * 1024D;
	private static final double SIZE_MB = 1024 * 1024D;
	private static final double SIZE_KB = 1024D;
	private static final ThreadLocal<NumberFormat> format = new ThreadLocal<NumberFormat>(){
		protected NumberFormat initialValue() {
			return new DecimalFormat("0.#");
		};
	};

    private static final long SLEEP_GRACE_TIME = 10L;

    /**
     * Returns a byte array representing an int.
     */
    public static byte[] cvtToByte(int val) {
        byte[] barray = new byte[4];
        cvtToByte(val, barray, 0);
        return barray;
    }

    /**
     * Convert an int to a byte array.
     *
     * @param val    The value to convert.
     * @param barray The output byte array.
     * @param pos    The starting position in the output byte array. NOTE
     *               the byte array must be at least pos+4 long.
     */
    public static void cvtToByte(int val, byte[] barray, int pos) {
        barray[pos] = (byte) (val >> 24);
        barray[pos + 1] = (byte) ((val >> 16) & 0xff);
        barray[pos + 2] = (byte) ((val >> 8) & 0xff);
        barray[pos + 3] = (byte) (val & 0xff);
    }

    /**
     * Convert a long into a byte array.
     *
     * @param val
     * @return
     */
    public static byte[] cvtToByte(long val) {
        byte[] barray = new byte[8];
        int i1 = (int) (val >> 32);
        int i2 = (int) (val & 0xffffffff);
        cvtToByte(i1, barray, 0);
        cvtToByte(i2, barray, 4);
        return barray;
    }

    public static void cvtToByte(long val, byte[] barray, int pos) {
        int i1 = (int) (val >> 32);
        int i2 = (int) (val & 0xffffffff);
        cvtToByte(i1, barray, pos);
        cvtToByte(i2, barray, pos + 4);
    }

    /**
     * Convert a byte array containing a long into a long.
     */
    public static long cvtLong(byte[] barray) {
        return cvtLong(barray, 0);
    }

    public static long cvtLong(byte[] barray, int pos) {
        int i1 = cvtInt(barray, pos);
        int i2 = cvtInt(barray, pos + 4);
        return (((long) i1) << 32) | ((long) i2 & 0xffffffffL);
    }

    /**
     * Convert to an integer from a byte array.
     *
     * @param barray The byte array to read.
     * @param pos    The starting position of the byte array.
     */
    public static int cvtInt(byte[] barray, int pos) {
        int val = (barray[pos] << 24)
                | ((barray[pos + 1] << 16) & 0x00ff0000)
                | ((barray[pos + 2] << 8) & 0x0000ff00)
                | (barray[pos + 3] & 0xff);
        return val;
    }

    /**
     * Compares two objects for equality, dealing with the case
     * where one or both of them might be null. So if both are null,
     * the function returns true. If only one is null, the function
     * returns false. If both are non-null, then the equals()
     * method is invoked on one of them and the 2nd object is
     * passed in. This means that the equals() method better
     * be well-defined and reflexive.
     */
    public static boolean equals(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return true;
        }

        if (o1 == null && o2 != null) {
            return false;
        }

        if (o2 == null && o1 != null) {
            return false;
        }

        return o1.equals(o2);
    }

    /**
     * Null-checked string representation, defaults to "NULL".
     */
    public static String toString(Object o) {
        return toString(o, DEFAULT_TO_STRING);
    }

    /**
     * Null-checked string representation.
     */
    public static String toString(Object o, String d) {
        return (o == null ? d : o.toString());
    }

    /**
     * Null-checked hash code, defaults to 0.
     */
    public static int hashCode(Object o) {
        return hashCode(o, DEFAULT_HASH_CODE);
    }

    /**
     * Null-checked hash code.
     */
    public static int hashCode(Object o, int d) {

        return (o == null ? d : o.hashCode());
    }

    /**
     * Sleeps and catches the InterruptedException. If it does
     * catch the InterruptedException, it will keep sleeping
     * until it's time to wake up.
     */

    public static void sleep(long msecs) {
        sleep(msecs, false);
    }

    /**
     * Sleep and return if it gets an InterruptedException.
     */
    public static void sleepExc(long msecs) {
        sleep(msecs, true);
    }

    /**
     * Sleep for msecs.
     *
     * @param msecs           Sleep interval.
     * @param exitOnInterrupt If true, it will exit if it catches an
     *                        InterruptedException. If false, it will keep sleeping for the
     *                        specified interval.
     */
    public static void sleep(long msecs, boolean exitOnInterrupt) {
        if (msecs <= 0) {
            return;
        }

        long wakeTime = System.currentTimeMillis() + msecs - SLEEP_GRACE_TIME;

        while (msecs > 0L) {
            try {
                Thread.sleep(msecs);
            }
            catch (InterruptedException e) {
                if (exitOnInterrupt) {
                    return;
                }
            }

            long newMsecs = wakeTime + SLEEP_GRACE_TIME - System.currentTimeMillis();
            if (newMsecs > 0L) {
                Thread.yield(); //what is this?
            }
            msecs = newMsecs;
        }
    }

    /**
     * Parses an int containted in a string. If there is a format
     * exception, then it uses 'default' as the value.
     *
     * @param s      The string containing a number.
     * @param defval The default value to use in case there is
     *               a format exception.
     */
    public static int parseInt(String s, int defval) {
        int val = 0;
        try {
            val = Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            val = defval;
        }
        return val;
    }

    /**
     * Routine copies one byte array to another. If the length of the
     * destination array is less than the source, then only enough
     * bytes are copied over (ie, no ArrayIndexOutOfBound problem).
     *
     * @param destination The destination array.
     * @param source      The source array.
     */
    public static void copy(byte[] destination, byte[] source) {
        if (destination == null || source == null) {
            return;
        }

        int len = source.length;
        if (len > destination.length) {
            len = destination.length;
        }

        for (int i = 0; i < len; i++) {
            destination[i] = source[i];
        }
    }

    /**
     * Performs a byte-by-byte comparison of two byte arrays.
     * Will take into account null arguments.
     *
     * @param a1 A byte array to compare.
     * @param a2 The other byte array to compare.
     * @return Returns true if the arrays are identical.
     */
    public static boolean equals(byte[] a1, byte[] a2) {
        if (a1 == null) {
            if (a2 == null) {
                return true;
            } else {
                return false;
            }
        } else if (a2 == null) {
            return false;
        }

        if (a1.length != a2.length) {
            return false;
        }

        for (int i = 0; i < a1.length; i++) {
            if (a1[i] != a2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Clones a byte array.
     *
     * @param b The byte array to clone.
     * @return Will return a brand new array. If the input
     *         array is null, then null is returned.
     */
    public static byte[] clone(byte[] b) {
        if (b == null) {
            return null;
        }

        byte[] barray = new byte[b.length];
        Utility.copy(barray, b);
        return barray;
    }

    /**
     * A wait() method to get around a bug in the jvm. Sometimes a wait()
     * call will get interrupted() for no good reason. This routine will
     * catch the interrupt and reissue the wait() call. This means of
     * course means that you can't rely on interrupt() to cause the
     * wait() to terminate.
     * <p/>
     * NOTE! It is assumed that a synchronize(obj) has been performed by the
     * caller prior to calling this routine.
     */
    public static void wait(Object obj) {
        boolean interrupted = true;
        while (interrupted) {
            interrupted = false;
            try {
                obj.wait();
            }
            catch (InterruptedException e) {
                interrupted = true;
            }
        }
    }

    public static void wait(Object obj, int timeout) {
        boolean interrupted = true;
        long startTime = System.currentTimeMillis();
        int sleepTimeout = timeout;

        while (interrupted) {
            interrupted = false;
            try {
                obj.wait(sleepTimeout);
            }
            catch (InterruptedException e) {
                interrupted = true;
                long now = System.currentTimeMillis();
                sleepTimeout -= now - startTime;
                startTime = now;
                if (sleepTimeout < 0) {
                    interrupted = false;
                }
            }
        }
    }

    /*
     * Returns true if 'value' is a valid integer >= min and <= max.
     * Otherwise returns false;
     *
     * @param value			A String value
     * @param min			The minimum valid int value
     * @param max			The maximum valid int value
     *
     * @return boolean		true if the value is a valid int in the range.  Otherwise false (including NFE)
     */
    public static boolean isNumberRangeValid(String value, int min, int max) {
        try {
            int number = Integer.parseInt(value);
            return isNumberRangeValid(number, min, max);
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    /*
     * Returns true if 'value' is a valid integer >= min and <= max.
     * Otherwise returns false;
     *
     * @param value			int value to check
     * @param min			The minimum valid int value
     * @param max			The maximum valid int value
     *
     * @return boolean		true if the value is a valid int in the range.  Otherwise false (including NFE)
     */
    public static boolean isNumberRangeValid(int value, int min, int max) {
        if (value < min || value > max) {
            return false;
        }

        return true;
    }

    /**
     * Returns a <tt>Boolean</tt> instance representing the specified
     * <tt>boolean</tt> value.  If the specified <tt>boolean</tt> value
     * is <tt>true</tt>, this method returns <tt>Boolean.TRUE</tt>;
     * if it is <tt>false</tt>, this method returns <tt>Boolean.FALSE</tt>.
     * If a new <tt>Boolean</tt> instance is not required, this method
     * should generally be used in preference to the constructor
     * {@link Boolean#Boolean(boolean)}, as this method is likely to to yield
     * significantly better space and time performance.
     * <p>Imported from the JDK 1.4 implementation -- SDC.
     *
     * @param b a boolean value.
     * @return a <tt>Boolean</tt> instance representing <tt>b</tt>.
     */
    public static Boolean valueOf(boolean b) {
        return (b ? Boolean.TRUE : Boolean.FALSE);
    }


    /**
     * Get the serialized form of obj.
     *
     * @param obj Object to serialize
     * @return serialized form of the object
     */
    public static byte[] getBytes(Serializable obj) {
        return getBytes(obj, 0);
    }

    /**
     * Get the serialized from of obj, and compress it.
     *
     * @param obj              Object to serialize
     * @param compressionLevel compression level to use
     * @return serialized form of the object
     */
    public static byte[] getBytes(Serializable obj, int compressionLevel) {
        byte[] result = null;

        ObjectOutputStream objStream = null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        try {
            OutputStream stream = new DeflaterOutputStream(byteStream, new Deflater(compressionLevel));
            objStream = new ObjectOutputStream(stream);
            objStream.writeObject(obj);
        }
        catch (Exception e) {
        }
        finally {
            if (objStream != null) {
                try {
                    objStream.flush();
                    objStream.close();
                }
                catch (Exception e) {
                }
            }
        }

        if (byteStream.size() > 0) {
            result = byteStream.toByteArray();
        }

        return result;
    }

    /**
     * Get the object from the specificed serailized form
     *
     * @param data Serialized object data
     * @return Object
     */
    public static Serializable getObject(byte[] data) {
        Serializable result = null;

        ObjectInputStream objStream = null;
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);

        try {
            InputStream stream = new InflaterInputStream(byteStream);
            objStream = new ObjectInputStream(stream);
            result = (Serializable) objStream.readObject();
        }
        catch (Exception e) {
        }
        finally {
            if (objStream != null) {
                try {
                    objStream.close();
                }
                catch (Exception e) {
                }
            }
        }

        return result;
    }


    /**
     * Inflate (uncompress) the specified array.
     *
     * @param data
     * @return
     */
    public static byte[] inflate(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream stream = new ByteArrayOutputStream(data.length);

        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                stream.write(buffer, 0, count);
            }
        }
        finally {
            stream.flush();
            stream.close();
        }

        return stream.toByteArray();
    }


    /**
     * Deflate (compress) the specified array.
     *
     * @param data
     * @param level
     * @return
     * @throws IOException
     */
    public static byte[] deflate(byte[] data, int level) throws IOException {
        return deflate(data, 0, data.length, level);
    }

    /**
     * Deflate (compress) the specified array.
     *
     * @param data
     * @param level
     * @return
     */
    public static byte[] deflate(byte[] data, int start, int length, int level) throws IOException {
        Deflater deflater = new Deflater(level);
        deflater.setInput(data, start, length);
        deflater.finish();
        ByteArrayOutputStream stream = new ByteArrayOutputStream(data.length);

        byte[] buffer = new byte[1024];
        try {
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer);
                stream.write(buffer, 0, count);
            }
        }
        finally {
            stream.flush();
            stream.close();
        }

        return stream.toByteArray();
    }
    
    public static String getReadableFileSize(long size){
    	String filesize = "";
    	if (size > SIZE_GB) {
			filesize = format.get().format(size / SIZE_GB) + " GB";
		} else if (size > SIZE_MB) {
			filesize = format.get().format(size / SIZE_MB) + " MB";
		} else if (size > SIZE_KB) {
			filesize = format.get().format(size / SIZE_KB) + " KB";
		} else {
			filesize = format.get().format(size / SIZE_KB) + " B";
		}
    	return filesize;
    }
}
