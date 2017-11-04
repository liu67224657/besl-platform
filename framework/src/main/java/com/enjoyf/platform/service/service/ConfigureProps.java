package com.enjoyf.platform.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.service.naming.NamingService;
import com.enjoyf.platform.service.naming.NamingServiceSngl;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;

@Deprecated
public class ConfigureProps {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigureProps.class);

	private FiveProps fiveProps;

    /////////////////////////////////////////////////
    public ConfigureProps(FiveProps props) {
        fiveProps = props;
    }

    public ServiceConfigNaming getCfg(NamingService ns, String serviceSection, String serviceType) {
        return ConfigureProps.getCfg(Props.instance().getEnvProps(), NamingServiceSngl.get(), serviceSection, serviceType);
    }

    public String getEntry(String section, String key) {
        return ConfigureProps.getEntry(fiveProps, section, key);
    }

    public static String getEntry(FiveProps props, String section, String key) {
        return props.get(section + "." + key);
    }

    public static ServiceConfigNaming getCfg(FiveProps props, NamingService namingService, String serviceSection, String serviceType) {
        int timeout = 0;

        //In the env configure file,
        // we can special a service to serve for typed service.
        //ie: userserver.name=userserver01
        String name = props.get(serviceSection + ".name");
        timeout = props.getInt(serviceSection + ".timeout");
        ServiceRequest req = new ServiceRequest(serviceType, ServiceRequest.ALL);


        if (name != null && name.length() > 0) {
            req.useName(name);
        }

        ServiceConfigNaming cfg = new ServiceConfigNaming(namingService, req);
        if (timeout < 5000 && timeout != 0) {
            logger.info("ConfigureProps.getCfg: WARNING service timeout is set to: " + timeout / 1000 + " seconds.");
        }

        if (timeout != 0) {
            cfg.setTimeout(timeout);
        }

        return cfg;
    }

    public int getServiceTimeout(String service) {
        String tstr = getEntry(service, "timeout");
        int timeout = 30;
        if (tstr == null) {
            try {
                timeout = Integer.parseInt(tstr);
            }
            catch (Exception e) {
            }
        }
        timeout *= 1000;
        return timeout;
    }
}
