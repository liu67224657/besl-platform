/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.naming;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.service.service.ServiceId;
import com.enjoyf.platform.service.service.ServiceInfo;

/**
 * The register info when a client registers to naming server.
 */
public class ClientRegInfo implements java.io.Serializable {
    private static int globalNextSeqNo = 1;

    private static long globalClientId = System.currentTimeMillis();

    //
    private ServiceInfo serviceInfo;
    private RegistrantId registrantId;

    /**
     * An identifier for the client.
     */
    private long clientId;

    /**
     * A sequence number, filled in every time this gets ctored.
     */
    private int seqNo = -1;

    /////////////////////////////////////////////////
    public ClientRegInfo(ServiceInfo servInfo) {
        serviceInfo = servInfo;
        seqNo = p_getNextSeqno();
        clientId = globalClientId;
    }

    public long getClientId() {
        return clientId;
    }

    public ServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public ServiceId getServiceId() {
        return serviceInfo.getServiceId();
    }

    public ServiceAddress getServiceAddress() {
        return serviceInfo.getServiceAddress();
    }

    public int getSeqno() {
        return seqNo;
    }

    public void setRegistrantId(RegistrantId id) {
        registrantId = id;
    }

    public RegistrantId getRegistrantId() {
        return registrantId;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("serviceInfo=");

        sb.append(serviceInfo);
        sb.append(":seqno=" + seqNo);
        sb.append(":globalClientId=" + clientId);
        sb.append(":regId=" + registrantId);

        return new String(sb);
    }

    /**
     * Just a convenience method to pretty-print some info as part of the
     * the thread name.
     */
    public String getThreadName() {
        StringBuffer sb = new StringBuffer(serviceInfo.getServiceId().toString());

        sb.append(":seqno=" + seqNo + ":globalClientId=" + clientId);

        return new String(sb);
    }

    public static long getLocalClientId() {
        return globalClientId;
    }

    private static synchronized int p_getNextSeqno() {
        return globalNextSeqNo++;
    }
}
