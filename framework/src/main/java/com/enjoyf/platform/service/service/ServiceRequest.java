/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

/**
 * Use this to request a ServiceAddress from a NamingService.
 */
public class ServiceRequest implements java.io.Serializable {
    /**
     * Retrieve a single ServiceAddress, the method used to choose
     * it is up to the server.
     */
    public static final int DEFAULT = 1;

    /**
     * Return a ServiceAddress, identifying it by name.
     */
    public static final int NAME = 2;

    /**
     * Return all service addresses for the given service type.
     */
    public static final int ALL = 3;

    ///////////////////////////////////////////
    private int metricValue = DEFAULT;
    private String serviceType;
    private String serviceName;

    /**
     * This form of the request means that any server of the specified
     * serviceType will satisfy the request.
     */
    public ServiceRequest(String type) {
        serviceType = type;
        metricValue = DEFAULT;
    }

    /**
     * Ctor the object with the given metricValue.
     */
    public ServiceRequest(String type, int metric) {
        serviceType = type;
        metricValue = metric;
    }

    /**
     * This form of the request means that a specific server is request,
     * by serviceType and serviceName.
     */
    public ServiceRequest(String type, String serviceName) {
        serviceType = type;
        useName(serviceName);
    }

    /**
     * Use this method to specify the explicit server wanted.
     */
    public void useName(String name) {
        serviceName = name;
        metricValue = NAME;
    }

    /**
     * Retrieve the serviceType of this request.
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Retrieve the serviceName. Will be null if this was not
     * specified.
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Returns DEFAULT or NAME, the latter when a serviceName was
     * specified in the request.
     */
    public int getMetric() {
        return metricValue;
    }

    /**
     * Returns true if we are using the NAME metricValue.
     */
    public boolean isNameMetric() {
        return metricValue == NAME;
    }

    public boolean isAllMetric() {
        return metricValue == ALL;
    }

    /**
     * Set the metricValue to use.
     */
    public void setMetric(int metric) {
        this.metricValue = metric;
    }

    /**
     * Returns true if we are using the DEFAULT metricValue.
     */
    public boolean isDefaultMetric() {
        return metricValue == DEFAULT;
    }

    /**
     * Return the ServiceId representing this ServiceRequest ONLY IF
     * the metricValue used is NAME. If not, you'll get back NULL.
     */
    public ServiceId getServiceId() {
        return metricValue == NAME ? new ServiceId(serviceType, serviceName) : null;
    }

    /**
     * Returns true if the passed in ServiceId is "satisified" by this
     * particular ServiceRequest.
     * Eg, if ServiceId == "juserserver/juserserver01"
     * and this == "jusersserver/ANY", then it would be satisified, but
     * not if this == "juserserver/juserserver02".
     */
    public boolean isSatisfied(ServiceId serviceId) {
        if (!serviceId.getServiceType().equals(serviceType)) {
            return false;
        }

        if (metricValue != NAME) {
            return true;
        }

        //--
        // If here, we are looking for a specific service type.
        //--

        return serviceId.getServiceName().equals(serviceName);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(serviceType);
        sb.append("/");
        if (metricValue == DEFAULT) {
            sb.append("DEFAULT");
        } else if (metricValue == ALL) {
            sb.append("ALL");
        } else if (metricValue == NAME) {
            sb.append(serviceName);
        }

        return new String(sb);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (obj.getClass() != getClass()) {
            return false;
        }

        ServiceRequest req = (ServiceRequest) obj;

        if (metricValue != req.metricValue) {
            return false;
        }

        if (!serviceType.equals(req.serviceType)) {
            return false;
        }

        if (metricValue == NAME) {
            if (!serviceName.equals(req.serviceName)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int val = serviceType == null ? 0 : serviceType.hashCode();
        val += metricValue;
        if (metricValue == NAME) {
            val += serviceName == null ? 0 : serviceName.hashCode();
        }

        return val;
    }
}
