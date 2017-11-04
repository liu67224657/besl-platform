/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.naming;

import com.enjoyf.platform.service.service.ServiceInfo;
import com.enjoyf.platform.service.service.ServiceLoad;
import com.enjoyf.platform.service.service.ServiceLoadNull;

/**
 * Abstract class providing some functionality for derived classes.
 */
public abstract class RegistrantDefault implements Registrant {
    protected ServiceInfo serviceInfo;

    protected NamingServiceAddress namingServiceAddress;

    protected int pingInterval = 60 * 1000;
    protected int reconnectWaitTime = 5 * 1000;
    protected Registrant.LoadListener loadListener = null;

    protected static final ServiceLoad defaultLoad = new ServiceLoadNull();

    /**
     * Initialize the object with the registration info.
     */
    public void init(ServiceInfo servInfo) {
        serviceInfo = servInfo;
    }

    public void setNamingServiceAddress(NamingServiceAddress namingService) {
        namingServiceAddress = namingService;
    }

    NamingServiceAddress getNamingServiceAddress() {
        return namingServiceAddress;
    }

    public void setPingInterval(int secs) {
        pingInterval = secs * 1000;
    }

    int getPingInterval() {
        return pingInterval;
    }

    public void setWaitBetweenReconnects(int secs) {
        reconnectWaitTime = secs * 1000;
    }

    /**
     * Retrieve the registration info.
     */
    public ServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public void setLoadListener(Registrant.LoadListener l) {
        loadListener = l;
    }

    /**
     * Return the ServiceLoad object to use in the next ping.
     */
    protected ServiceLoad getServiceLoad() {
        return loadListener == null ? defaultLoad : loadListener.get();
    }
}
