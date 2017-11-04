/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.naming;

import com.enjoyf.platform.props.EnvConfig;

/**
 * A class to act as a global holder of a NamingService object.
 */
public class NamingServiceSngl {
    private static NamingService instance = null;

    public static synchronized void set(NamingService service) {
        instance = service;
    }

    public static synchronized NamingService get() {
        if (instance == null) {
            create();
        }
        return instance;
    }

    private static void create() {
        NamingService ns = NamingServiceFactory.instance().createFromProps(EnvConfig.get().getProps());
        NamingService temp = new NamingServiceAddressDecorator(ns, 5000);

        instance = new NamingServiceThrottleDecorator(temp);
    }
}
