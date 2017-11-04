/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import java.util.ArrayList;
import java.util.List;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.util.Refresher;

public class ServiceFinderN implements ServiceFinder {
    private List<ServiceData> serviceDataList;
    private Refresher refresher;

    public ServiceFinderN(ServiceAddress[] addresses) {
        serviceDataList = new ArrayList<ServiceData>();

        for (int i = 0; i < addresses.length; i++) {
            serviceDataList.add(new ServiceData(addresses[i]));
        }

        refresher = new Refresher(10 * 60 * 1000);
    }

    public List<ServiceData> getServiceData(boolean reallyNeedSome) {
        //ArrayList<ServiceData> l = new ArrayList<ServiceData>();

        if (reallyNeedSome || refresher.shouldRefresh()) {
            return new ArrayList<ServiceData>(serviceDataList);
        }

        return null;
    }

    public String getId() {
        String id = "UNKNOWN";
        if (serviceDataList != null && serviceDataList.size() > 0) {
            id = serviceDataList.get(0).toString();
        }

        return id;
    }

    public void close() {
    }
}
