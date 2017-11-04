/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * A class to encapsulate a registered service id.
 */
public class ServiceId implements Comparable, Serializable {
    private String serviceType = "";
    private String serviceName = "";

    public ServiceId() {
    }

    public ServiceId(String type, String name) {
        serviceType = type;
        serviceName = name;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceType(String type) {
        serviceType = type;
    }

    public void setServiceName(String name) {
        serviceName = name;
    }

    public int hashCode() {
        return serviceName.hashCode() + serviceType.hashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ServiceId)) {
            return false;
        }

        ServiceId sid = (ServiceId) obj;
        return serviceName.equals(sid.serviceName)                 && serviceType.equals(sid.serviceType);
    }

    public String toString() {
        return serviceType + ":" + serviceName;
    }

    public int compareTo(Object obj) {
        ServiceId sid = (ServiceId) obj;

        int cmp = serviceType.compareTo(sid.serviceType);
        if (cmp != 0) {
            return cmp;
        }

        return serviceName.compareTo(sid.serviceName);
    }

    public boolean reconstitute(String s) {
        boolean retval = false;
        StringTokenizer t = new StringTokenizer(s, ":");

        if (t.hasMoreTokens()) {
            serviceType = t.nextToken();
        }
        if (t.hasMoreTokens()) {
            serviceName = t.nextToken();
            retval = true;
        }

        return retval;
    }

    /**
     * Ask if this ServiceId is not set properly.
     */
    public boolean isBad() {
        if (serviceType == null || serviceType.length() == 0) {
            return true;
        }

        if (serviceName == null || serviceName.length() == 0) {
            return true;
        }

        return false;
    }
}
