/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.naming;

import java.util.LinkedList;
import java.util.StringTokenizer;

import com.enjoyf.platform.io.ServiceAddress;

import com.google.common.base.Joiner;

/**
 * Utility class to encapsulate a ServiceAddress for the naming
 * service.
 */
public class NamingServiceAddress {
    /**
     * We store what is really two different kinds of objects in here.
     * One could reasonably argue that there should be a class hierarchy
     * here..
     */
    private ServiceAddress[] serviceAddresses;
    private ServiceAddress virtualServiceAddress;

    /**
     * Use an array of different addresses.
     */
    public void set(ServiceAddress[] addresses) {
        serviceAddresses = addresses;
    }

    /**
     * Use a single virtual address.
     */
    public void setVirtual(ServiceAddress saddr) {
        virtualServiceAddress = saddr;
    }

    /**
     * Set this object from a property.
     *
     * @param addrProperty The property containing one or more hostnames.
     *                     Each hostname can have a ':<port>' appended to it.
     * @param defPort      This would be the port property. Used as the
     *                     default port. Can be null or empty, in which case we default to
     *                     7500.
     */
    public void setFromProp(String addrProperty, int defPort) {
        //
        if (addrProperty == null || addrProperty.length() == 0) {
            throw new RuntimeException("NamingServiceAddress.setFromProp: Null or empty property!");
        }

        //
        int port = defPort;

        //
        LinkedList<ServiceAddress> addressList = new LinkedList<ServiceAddress>();
        StringTokenizer t = new StringTokenizer(addrProperty, " \t");

        while (t.hasMoreTokens()) {
            String addrAndPort = t.nextToken();
            StringTokenizer t2 = new StringTokenizer(addrAndPort, ":");
            String addr = t2.nextToken();

            port = defPort;
            if (t2.hasMoreTokens()) {
                try {
                    port = Integer.parseInt(t2.nextToken());
                }
                catch (Exception e) {
                    //
                }
            }

            addressList.addLast(new ServiceAddress(addr, port));
        }

        if (addressList.size() == 0) {
            throw new RuntimeException("NamingServiceAddress.setFromProp: Need something in the property! " + addrProperty);
        }

        if (addressList.size() > 1) {
            serviceAddresses = new ServiceAddress[addressList.size()];

            addressList.toArray(serviceAddresses);
        } else {
            virtualServiceAddress = addressList.getFirst();
        }
    }

    public boolean isVirtual() {
        return virtualServiceAddress != null;
    }

    public ServiceAddress getVirtual() {
        return virtualServiceAddress;
    }

    public ServiceAddress[] getAddresses() {
        return serviceAddresses;
    }

    @Override
    public String toString() {
        if (isVirtual()) {
            return virtualServiceAddress.toString();
        } else {
            return Joiner.on(":").join(serviceAddresses);
        }
    }
}
