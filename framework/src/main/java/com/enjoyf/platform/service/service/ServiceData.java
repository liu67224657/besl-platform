package com.enjoyf.platform.service.service;

import com.enjoyf.platform.io.ServiceAddress;

/**
 * A utility class to aggregate a ServiceInfo object + a ServiceLoad object.
 */
@SuppressWarnings("serial")
public class ServiceData implements java.io.Serializable {
	
    private ServiceInfo serviceInfo;
    private ServiceLoad serviceLoad;

    public ServiceData() {
    }

    public ServiceData(ServiceAddress saddr) {
        serviceInfo = new ServiceInfo(saddr);
    }

    public ServiceData(ServiceInfo servInfo) {
        setServiceInfo(servInfo);
    }

    public ServiceData(ServiceInfo servInfo, ServiceLoad servLoad) {
        setServiceInfo(servInfo);
        setServiceLoad(servLoad);
    }

    public void setServiceInfo(ServiceInfo servInfo) {
        serviceInfo = servInfo;
    }

    public void setServiceLoad(ServiceLoad servLoad) {
        serviceLoad = servLoad;
    }

    public ServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public ServiceLoad getServiceLoad() {
        return serviceLoad;
    }

    public ServiceAddress getServiceAddress() {
        return serviceInfo.getServiceAddress();
    }

    public ServiceId getServiceId() {
        return serviceInfo.getServiceId();
    }

    @Override
    public String toString() {
        return "service=" + serviceInfo + ":" + "load=" + serviceLoad;
    }
}
