/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

/**
 * Class to represent hello info for a client that needs to
 * register its existence. This is a generic object that can be
 * used by many different servers. However, a specific server can
 * certainly use it's own derived class from this to add functionality.
 */
public class HelloInfo implements java.io.Serializable {
    private String name = "";
    private boolean reconnectFlag = false;

    public HelloInfo() {
    }

    /**
     * @param name      This is a unique name across the network. Eg, if a server
     *                  it could be the ip-address/port combo.
     * @param reconnect Set to true if the client thinks he is reconnecting
     *                  to the server (as opposed to establishing a brand new connection).
     */
    public HelloInfo(String name, boolean reconnect) {
        setName(name);
        setReconnect(reconnect);
    }

    /**
     * Retrieves the name of the client.
     */
    public String getName() {
        return name;
    }

    public boolean isReconnect() {
        return reconnectFlag;
    }

    /**
     * Set this to true to indicate a reconnection to the server.
     */
    public void setReconnect(boolean reconnect) {
        reconnectFlag = reconnect;
    }

    /**
     * Set the name.
     */
    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof HelloInfo)) {
            return false;
        }

        HelloInfo hinfo = (HelloInfo) obj;
        if (hinfo.reconnectFlag != reconnectFlag) {
            return false;
        }

        return hinfo.name.equals(name);
    }

    public int hashCode() {
        return name.hashCode();
    }

    public String toString() {
        return "name/recon = " + name + "/" + reconnectFlag;
    }
}
