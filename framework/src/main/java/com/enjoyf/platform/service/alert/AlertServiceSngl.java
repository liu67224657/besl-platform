package com.enjoyf.platform.service.alert;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.service.naming.NamingService;
import com.enjoyf.platform.service.naming.NamingServiceSngl;
import com.enjoyf.platform.service.service.ConfigureProps;
import com.enjoyf.platform.service.service.ConfigurePropsSngl;
import com.enjoyf.platform.service.service.ServiceConfigRecon;
import com.enjoyf.platform.service.service.ServiceRequest;

/**
 * A class to act as a singleton for AlertServices.
 */
public class AlertServiceSngl {
	
	private static final Logger logger = LoggerFactory.getLogger(AlertServiceSngl.class);
	
    private static AlertService instance = null;

    public static synchronized void set(AlertService service) {
        instance = service;
    }

    public static synchronized AlertService get() {
        if (instance == null) {
            create();
        }

        return instance;
    }

    private static void create() {
        ServiceRequest req = null;
        ServiceConfigRecon cfgRecon = null;

        //--
        // Alert instance needs some special config.
        //--
        ConfigureProps c = ConfigurePropsSngl.get();
        req = p_getServiceRequest(c, AlertConstants.SERVICE_TYPE, AlertConstants.SERVICE_SECTION);

        NamingService ns = NamingServiceSngl.get();
        cfgRecon = new ServiceConfigRecon(ns, req);
        InetAddress iaddr = null;
        String hostname = null;

        try {
            iaddr = InetAddress.getLocalHost();
        } catch (Exception e) {
            logger.error("ServiceInit.init: "
                    + "Geez, couldn't get an InetAddress for local host!"
                    + " exc = " + e);
            hostname = "UNKNOWN";
        }

        if (iaddr != null) {
            hostname = iaddr.getHostName();
        }

        cfgRecon.setClientName(hostname);
        cfgRecon.setTimeout(c.getServiceTimeout(AlertConstants.SERVICE_SECTION));

        instance = new AlertService(cfgRecon);
    }

    private static ServiceRequest p_getServiceRequest(ConfigureProps c, String serviceType, String serviceSection) {
        String tstr = c.getEntry(serviceSection, "name");

        return tstr == null ? new ServiceRequest(serviceType, ServiceRequest.ALL) : new ServiceRequest(serviceType, tstr);
    }
}
