/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import com.enjoyf.platform.service.naming.NamingService;
import com.enjoyf.platform.service.naming.NamingServiceAddressDecorator;
import com.enjoyf.platform.service.naming.NamingServiceSngl;
import com.enjoyf.platform.service.naming.NamingServiceThrottleDecorator;

/**
 * Singleton to initialize services.
 */
public class ServiceInit {
    protected static ServiceInit instance = new ServiceInit();
    private boolean initialized = false;

    /**
     * Retrieve the singleton instance.
     */
    public static ServiceInit instance() {
        return instance;
    }


    /**
     * Initialize from the property file specifed in -Dcom.platform.env.
     */
    public synchronized void init() {
        initialized = true;
    }


    /**
     * Initialize only once no matter how many times this method is called.
     */
    public synchronized void initOnceOnly() {
        if (!initialized) {
            init();
        }
    }

    /**
     * Have we initialized?
     */
    public synchronized boolean isInitialized() {
        return initialized;
    }


    /**
     * Init from a FiveProps object.
     *
     * @param namingService The naming service we are using.
     */
    public void init(NamingService namingService) {
        NamingService temp = new NamingServiceAddressDecorator(namingService, 5000);
        NamingService nsFinal = new NamingServiceThrottleDecorator(temp);
        NamingServiceSngl.set(nsFinal);

        init();
    }

    protected ServiceInit() {
    }
}
