/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

/**
 * This is a class similar to java.net.InetAddress, except that it
 * adds functionality. Primarily, it encapsulates the net representation
 * of an ipaddress (currently it is 4-bytes, but it could change in the
 * future).
 */
public class IpAddress {
    private String address;
    private int intAddress;
    private boolean isNullFlag = false;

    /**
     * Construct an ip address from its integer represantion.
     */
    public IpAddress(int ip) {
        intAddress = ip;
        address = convertToString(ip);
    }

    /**
     * Construct an IpAddress object from a string rep.
     * If the object could not be constructed, then it is
     * set to NULL.
     *
     * @param host The string representation (eg "10.10.2.100")
     *             of the hostname.
     * @throws RuntimeException Will throw this exception if it
     *                          is not in numerical dotted notation.
     */
    public IpAddress(String host) {
        address = host;
        intAddress = convertToInt(address);
    }

    /**
     * This is a utility routine to convert a raw ip address in
     * 32-bit form into a string consisting of the dot notation.
     */
    public static String cvtToString(int addr) {
        return convertToString(addr);
    }

    /**
     * Return the address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Return the integer address.
     */
    public int getIntVal() {
        return intAddress;
    }

    /**
     * Return true if the address is NULL
     */
    public boolean isNull() {
        return isNullFlag;
    }

    /**
     * Can set an address to null if so wanted.
     * Note that there is no way to unset this once done.
     */
    public void setNull() {
        isNullFlag = true;
    }

    /**
     * The std comparison operator. Will be smart enough to
     * take into account NULL addresses.
     */
    public boolean equals(IpAddress a) {
        //--------------------------------------------------------
        // First check all the null conditions.
        //--------------------------------------------------------

        if (isNullFlag) {
            if (a.isNullFlag) {
                return true;
            } else {
                return false;
            }
        } else if (a.isNullFlag) {
            return false;
        }

        //--------------------------------------------------------
        // If here, we know we have two valid addresses, so
        // compare their bit patterns.
        //--------------------------------------------------------

        if (intAddress == a.intAddress) {
            return true;
        }

        return false;
    }

    /**
     * Mask this address with the passed in address ( AND operation).
     *
     * @param mask The mask to use.
     */
    public void mask(IpAddress mask) {
        //--------------------------------------------------------
        // If our object is null, masking ain't gonna change jack.
        //--------------------------------------------------------

        if (isNull()) {
            return;
        }

        //--------------------------------------------------------
        // If the mask is null, we're going to end up with a null
        // object.
        //--------------------------------------------------------

        if (mask.isNull()) {
            setNull();
            return;
        }
        //--------------------------------------------------------
        // If here, we know that both addresses are valid. So
        // iterate over each byte, and'ing them.
        //--------------------------------------------------------

        intAddress &= mask.intAddress;
        address = convertToString(intAddress);
        isNullFlag = false;
    }

    /**
     * Return the InetAddress of this ip address.
     *
     * @throws UnknownHostException Thrown if we could not
     *                              understand the ip address/name.
     */
    public InetAddress getInetAddress() throws UnknownHostException {
        return InetAddress.getByName(address);
    }

    public String toString() {
        return new String(address);
    }

    private int convertToInt(String a) {
        StringTokenizer t = new StringTokenizer(a, ".");
        byte[] barr = new byte[4];
        int i = 0;
        while (t.hasMoreElements()) {
            String s = (String) t.nextElement();
            int addr;
            try {
                addr = Integer.parseInt(s);
            }
            catch (NumberFormatException e) {
                throw new RuntimeException(
                        "Not in dotted notation!");
            }
            barr[i] = (byte) addr;
            i++;
        }
        int addr = (barr[0] << 24)
                | ((barr[1] << 16) & 0x00ff0000)
                | ((barr[2] << 8) & 0x0000ff00)
                | (barr[3] & 0xff);
        return addr;
    }

    private static String convertToString(int val) {
        int t1 = (val >> 24) & 0xff;
        int t2 = (val >> 16) & 0xff;
        int t3 = (val >> 8) & 0xff;
        int t4 = val & 0xff;
        StringBuffer sb = new StringBuffer();
        sb.append(Integer.toString(t1));
        sb.append(".");
        sb.append(Integer.toString(t2));
        sb.append(".");
        sb.append(Integer.toString(t3));
        sb.append(".");
        sb.append(Integer.toString(t4));
        return new String(sb);
    }


}
