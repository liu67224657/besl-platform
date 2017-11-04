package com.enjoyf.platform.service.service;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.util.Refresher;
import com.enjoyf.platform.util.log.GAlerter;

public class ServiceFinderVirtual implements ServiceFinder {
    private ServiceAddress[] serviceAddresses;
    private String virtualIp;
    private int virtualPort;
    private Refresher refresher;

    public ServiceFinderVirtual(String vIp, int port) {
        virtualIp = vIp;
        virtualPort = port;
        refresher = new Refresher(10 * 60 * 1000);
        p_virtualRefresh();
    }

    private void p_virtualRefresh() {
        ServiceAddress[] newAddresses = null;
        try {
            InetAddress[] iaddr = InetAddress.getAllByName(virtualIp);
            newAddresses = new ServiceAddress[iaddr.length];

            for (int i = 0; i < iaddr.length; i++) {
                newAddresses[i] = new ServiceAddress(iaddr[i].getHostAddress(), virtualPort);
            }
        }
        catch (Exception e) {
            //--
            // Only get wiggy if we could not do this the first time.
            //--
            if (serviceAddresses == null) {
                GAlerter.lab("ServiceFinderN.ctor: "
                        + "Got exception while initializing with virtual address: "
                        + e + " This is probably a very bad thing and should be"
                        + " reported! ");

                throw new RuntimeException("Could not initialize ServiceFinderN: " + e);
            }
        }
        if (newAddresses != null) {
            serviceAddresses = newAddresses;
        }
    }

    public List<ServiceData> getServiceData(boolean reallyNeedSome) {
        ArrayList<ServiceData> list = new ArrayList<ServiceData>();
        if (serviceAddresses == null) {
            return list;
        }

        if (!reallyNeedSome && !refresher.shouldRefresh()) {
            return null;
        }

        p_virtualRefresh();

        //--
        // Return the list of ServiceData objects corresponding to the
        // virtual addresses.
        //--
        for (int i = 0; i < serviceAddresses.length; i++) {
            list.add(new ServiceData(serviceAddresses[i]));
        }

        return list;
    }

    public String getId() {
        return serviceAddresses != null ? serviceAddresses[0].toString() : "UNKNOWN";
    }

    public void close() {
    }
}
