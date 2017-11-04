/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Utility class to represent the key of a ServiceAddress object.
 * In this case a key is just the InetAddres+port. The reason it's
 * not just in ServiceAddress is because we are ignoring the
 * token field, and are being more robust by using InetAddress instead
 * of a String. I think once the arena server goes away
 * we can clean up ServiceAddress (ie, the elimination of the token
 * field) and merge these two classes.
 */
public class ServiceAddressKey {
    private int port;
    private InetAddress ip;

    /**
     * Ctor this object from a ServiceAddress. Note that
     * the ip is converted into an InetAddress for more robust equality
     * comparisons. This does mean that you might get an
     * UnknownHostException.
     *
     * @throws UnknownHostException Thrown if the underlying java
     *                              libraries cannot resolve the ip within ServiceAddress.
     */
    public ServiceAddressKey(ServiceAddress saddr) throws UnknownHostException {
        port = saddr.getPortInt();
        ip = InetAddress.getByName(saddr.getAddr());
    }

    public int hashCode() {
        return port + ip.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        ServiceAddressKey k = (ServiceAddressKey) obj;
        
        return k.port == port && k.ip.equals(ip);
    }

    public String toString() {
        return ip.toString();
    }
}
