/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.naming;

import com.enjoyf.platform.util.FiveProps;

public class NamingServiceUtil {
    private static final int WAIT_TIME = 5 * 1000;

    /**
     * Creates a "std" naming service with all the desired decorators.
     */
    public static NamingService createStdNamingService(FiveProps envProps) {
        NamingService ns = null;

        ns = NamingServiceFactory.instance().createFromProps(envProps);
        ns = new NamingServiceAddressDecorator(ns, WAIT_TIME);
        ns = new NamingServiceThrottleDecorator(ns);

        return ns;
    }
}
