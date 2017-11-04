/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import java.util.ArrayList;
import java.util.List;

import com.enjoyf.platform.io.ServiceAddress;

/**
 * A trivial implementation to return a preset ServiceAddress.
 */
public class ServiceFinderOne implements ServiceFinder {
    private ServiceAddress serviceAddress;

    public ServiceFinderOne(ServiceAddress saddr) {
        serviceAddress = saddr;
    }

    /**
     * Return a List of ServiceData objects. In this implementation,
     * the list will only have one element.
     */
    public List<ServiceData> getServiceData(boolean reallyNeedSome) {
        ArrayList<ServiceData> l = new ArrayList<ServiceData>();
        l.add(new ServiceData(serviceAddress));

        return l;
    }

    public String getId() {
        return serviceAddress.toString();
    }

    public void close() {
    }
}
