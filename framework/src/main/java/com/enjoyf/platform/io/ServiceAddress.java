/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.enjoyf.platform.util.thin.Settings;

/**
 * A class for encapsulating a TEN service address. This is typically
 * the port, the ip address, and the token.
 */
@SuppressWarnings("serial")
public class ServiceAddress implements Cloneable, Serializable {
    
    //the port of the service
    private int portInt;
    private String portValue = null;

    //the address
    private String address = "";

    /**
     * Constructs an unset object.
     */
    public ServiceAddress() {
        portInt = 0;
    }

    /**
     * Construct an object from a host and a port.
     *
     * @param host Host name.
     * @param port The port number.
     */
    public ServiceAddress(String host, int port) {
        set(host, port);
    }

    /**
     * Construct this object using the localhost as the ip address.
     *
     * @param port The port to use.
     */
    public ServiceAddress(int port) {
        String ipaddr = "";
        try {
            ipaddr = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            //
        }

        set(ipaddr, port);
    }

    /**
     * Set the address.
     *
     * @param host The host name.
     * @param port The port as a string.
     */
    public void set(String host, String port) {
        try {
            portInt = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            portInt = 0;
        }

        set(host, portInt);
    }

    /**
     * Set the inet address from a string (a hostname).
     */
    public void set(String host, int port) {
        address = host;
        portInt = port;

        if (portInt < 0) {
            portInt &= 0xffff;
        }
    }

    /**
     * Set the ip address.
     */
    public void setAddr(String host) {
        address = host;
    }

    /**
     * Set the port.
     */
    public void setPort(int port) {
        portInt = port;
        if (portInt < 0) {
            portInt &= 0xffff;
        }

        portValue = Integer.toString(portInt);
    }

    /**
     * Return the port as a string.
     */
    public String getPort() {
        if (portValue == null) {
            portValue = Integer.toString(portInt);
        }

        return portValue;
    }

    /**
     * Return the port as an integer.
     */
    public int getPortInt() {
        return portInt;
    }

    /**
     * Return the InetAddress object.
     *
     * @throws UnknownHostException Thrown because the host was
     *                              not understood.
     */
    public InetAddress getInetAddress() throws UnknownHostException {
        return InetAddress.getByName(address);
    }

    /**
     * Return the inet address as a string.
     */
    public String getAddr() {
        return address;
    }

    /**
     * Set this object from a string. The passed in string is converted
     * to a Settings object, so the string better be in that format.
     *
     * @return Returns false if the 'addr' or 'host' entries are null,
     *         or if the 'token' entry does not exist.
     */
    public boolean reconstitute(String s) {
        if (s == null || s.length() == 0) {
            return false;
        }

        Settings settings = new Settings();
        settings.reconstitute(s);

        String addr = settings.getValue("address");
        if (addr == null || addr.length() == 0) {
            return false;
        }

        String port = settings.getValue("port");
        if (port == null || port.length() == 0) {
            return false;
        }

        set(addr, port);

        return true;
    }

    /**
     * Convert this object to a string.
     */
    public String deconstitute() {
        Settings settings = new Settings();

        settings.add("port", getPort());
        settings.add("address", address);

        return settings.deconstitute();
    }

    public String toString() {
        return deconstitute();
    }

    public int hashCode() {
        return portInt + address.hashCode();
    }

    /**
     * The equals method.
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        ServiceAddress saddr = (ServiceAddress) obj;

        return saddr.portInt == portInt && saddr.address.equals(address);
    }
}
