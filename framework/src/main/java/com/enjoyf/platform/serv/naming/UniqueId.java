/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.naming;

/**
 * This class represents a network-wide unique id that all registrations
 * are stamped with so that we can perform conflict resolution.
 */
class UniqueId implements java.io.Serializable {
    /**
     * This is the timestamp identifying a particular instantiation of
     * a server.
     */
    private long timestampId;

    /**
     * The server's ip
     */
    private int serverIp;

    /**
     * The server's port.
     */
    private int serverPort;

    /**
     * An incrementing integer to differentiate between registrations.
     */
    private int seqNo;

    UniqueId(long id, int ip, int port) {
        timestampId = id;
        serverIp = ip;
        serverPort = port;
    }

    void setSeqno(int seqno) {
        seqNo = seqno;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        UniqueId uid = (UniqueId) obj;
        if (timestampId != uid.timestampId) {
            return false;
        }

        if (serverIp != uid.serverIp) {
            return false;
        }

        if (serverPort != uid.serverPort) {
            return false;
        }

        return seqNo == uid.seqNo;
    }

    public int hashCode() {
        return (int) (timestampId + serverIp + serverPort + seqNo);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("id=" + timestampId + "|");
        sb.append("serverIp=" + serverIp + "|");
        sb.append("serverPort=" + serverPort + "|");
        sb.append("seqno=" + seqNo);
        return new String(sb);
    }
}
