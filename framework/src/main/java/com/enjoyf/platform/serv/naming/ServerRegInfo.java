/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.naming;

import com.enjoyf.platform.service.naming.ClientRegInfo;

/**
 * This class encapsulates a com.enjoyf.platform.service.naming.ClientRegInfo object
 * and adds all the attributes that we need to manage this regisration
 * on the server side.
 */
class ServerRegInfo implements java.io.Serializable {
    private ClientRegInfo clientRegInfo;
    private long timestamp;
    private String m_ip;
    private UniqueId m_uniqueId;

    ServerRegInfo(ClientRegInfo regInfo) {
        clientRegInfo = regInfo;
        timestamp = System.currentTimeMillis();
    }

    void setUniqueId(UniqueId uid) {
        m_uniqueId = uid;
    }

    UniqueId getUniqueId() {
        return m_uniqueId;
    }

    void setIp(String ip) {
        m_ip = ip;
    }

    String getIp() {
        return m_ip;
    }

    /**
     * Compare two registrations to see which is the "valid" one.
     * Returns -1 if info1 is the valid one, and 1 if info2 is the
     * valid one.
     */
    static int compare(ServerRegInfo info1, ServerRegInfo info2) {
        if (info1.m_ip.equals(info2.m_ip) && info1.clientRegInfo.getClientId() == info2.clientRegInfo.getClientId()) {
            //--
            // If here, we have a pretty good guess that this is coming
            // from the same logicProcess, so compare the sequence number.
            //--
            return info1.clientRegInfo.getSeqno() < info2.clientRegInfo.getSeqno() ? 1 : -1;
        }
        //--
        // If here, then all we can do is use the timestamp.
        //--
        return info1.timestamp < info2.timestamp ? 1 : -1;
    }

    long getTimestamp() {
        return timestamp;
    }

    ClientRegInfo getClientRegInfo() {
        return clientRegInfo;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("regInfo=" + clientRegInfo);
        sb.append(":timestamp = " + timestamp);
        sb.append(":ip = " + m_ip);
        sb.append(":unid = " + m_uniqueId);
        return new String(sb);
    }
}
