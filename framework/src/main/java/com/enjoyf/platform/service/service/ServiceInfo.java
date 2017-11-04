/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

import com.enjoyf.platform.io.ServiceAddress;

/**
 * A class to contain the info for a service.
 */
public class ServiceInfo implements Serializable {
    //
    private ServiceId serviceId = new ServiceId();
    private ServiceAddress serviceAddress = new ServiceAddress();

    //
    private static final ServiceId ANON_SERVICE = new ServiceId("anon", "anon");

    /////////////////////////////////////////////////
    public ServiceInfo() {
    }

    public ServiceInfo(ServiceAddress saddr) {
        this(ANON_SERVICE, saddr);
    }

    public ServiceInfo(ServiceId id, ServiceAddress saddr) {
        serviceId = id;
        serviceAddress = saddr;
    }

    /**
     * Construct the object from a ServiceId and the service port.
     * The ip of the service is assumed to be the local host.
     */
    public ServiceInfo(ServiceId id, int port) {
        this(id, new ServiceAddress(port));
    }

    public ServiceId getServiceId() {
        return serviceId;
    }

    public ServiceAddress getServiceAddress() {
        return serviceAddress;
    }

    public int getPort() {
        return serviceAddress == null ? 0 : serviceAddress.getPortInt();
    }

    public void setServiceId(ServiceId serviceId) {
        this.serviceId = serviceId;
    }

    public void setServiceAddress(ServiceAddress serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(serviceId);
        String sval = serviceAddress == null ? "" : serviceAddress.deconstitute();
        out.writeObject(sval);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        serviceId = (ServiceId) in.readObject();
        String sval = (String) in.readObject();

        if (sval.length() == 0) {
            serviceAddress = null;
        } else {
            serviceAddress = new ServiceAddress();
            serviceAddress.reconstitute(sval);
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("id=" + serviceId + ":");
        sb.append("id=" + serviceAddress);

        return new String(sb);
    }
}
