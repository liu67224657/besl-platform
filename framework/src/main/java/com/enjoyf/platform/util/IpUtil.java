package com.enjoyf.platform.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilility class for ip address manipulation.
 */
public class IpUtil {

	private static final Logger logger = LoggerFactory.getLogger(IpUtil.class);
	
    /**
     * Return an int representing the ip address.
     *
     * @param saddr Can be numeric-dotted notation, or the name of
     *              a host. If the latter, InetAddress is used to perform the
     *              conversion (which means a dns hit).
     */
    public static int cvtToInt(String saddr) throws UnknownHostException {
        IpAddress ipaddr = null;
        try {
            ipaddr = new IpAddress(saddr);
        } catch (Exception e) {
            InetAddress addr = InetAddress.getByName(saddr);
            byte[] barr = addr.getAddress();
            return Utility.cvtInt(barr, 0);
        }
        return ipaddr.getIntVal();
    }


    public static long cvtToLong(String saddr) throws UnknownHostException {
        IpAddress ipaddr = null;
        try {
            ipaddr = new IpAddress(saddr);
        } catch (Exception e) {
            InetAddress addr = InetAddress.getByName(saddr);
            byte[] barr = addr.getAddress();
            return Utility.cvtInt(barr, 0) & 0xFFFFFFFFL;   //十六进制
        }
        return ipaddr.getIntVal() & 0xFFFFFFFFL;
    }

    /**
     * Convert the raw IP address bytes (returned from
     * InetAddress.getAddress()) into an ip address String in
     * numeric-dotted form.
     */
    public static String cvtToString(byte[] addr) {
        return cvtToString(Utility.cvtInt(addr, 0));
    }

    /**
     * Convert an integer representation of an ip address into
     * numeric-dotted form.
     */
    public static String cvtToString(int addr) {
        int t1 = (addr >> 24) & 0xff;
        int t2 = (addr >> 16) & 0xff;
        int t3 = (addr >> 8) & 0xff;
        int t4 = addr & 0xff;
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

    /**
     * Compares two ip's, no matter if they are in numeric dotted
     * notation, or alpha, or whatever. If InetAddress throws an
     * exception while trying to decode the string, a false is
     * returned.
     */
    public static boolean compare(String ip1, String ip2) {
        if (ip1.equals(ip2)) {
            return true;
        }

        try {
            InetAddress iaddr1 = InetAddress.getByName(ip1);
            InetAddress iaddr2 = InetAddress.getByName(ip2);
            return iaddr1.equals(iaddr2);
        }
        catch (Exception e) {
            logger.error("IpUtil.compare: Exception comparing: " + ip1 + "/" + ip2, e);
            return false;
        }
    }

    /**
     * Returns the local host name. If there are any errors, a null is
     * returned.
     */
    public static String getLocalHostName() {
        InetAddress iaddr = null;
        try {
            iaddr = InetAddress.getLocalHost();
        }
        catch (Exception e) {
            logger.error("IpUtil: Error retrieving local host: ", e);
            return null;
        }
        return iaddr.getHostName();
    }

    /**
     * Returns the base part of the host name, stripping out the domain.
     */
    public static String getBaseLocalHostName() {
        String hostName = getLocalHostName();
        if (hostName == null) {
            return null;
        }

        int idx = hostName.indexOf(".");
        return idx > 0 ? hostName.substring(0, idx) : hostName;
    }

    public static long aToN(String addr) {
        long returnVlaue = 0;

        String[] as = addr.split("\\.");

        if (as.length == 4) {
            returnVlaue = Long.parseLong(as[0]) * 256 * 256 * 256 + Long.parseLong(as[0]) * 256 * 256 + Long.parseLong(as[0]) * 256 + Long.parseLong(as[3]);
        }

        return returnVlaue;
    }

    public static String nToa() {
        return "";
    }
}
