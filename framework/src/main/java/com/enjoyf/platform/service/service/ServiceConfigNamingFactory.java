/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import java.util.HashMap;

import com.enjoyf.platform.service.naming.NamingService;
import com.enjoyf.platform.service.naming.NamingServiceSngl;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;

public class ServiceConfigNamingFactory {
    private static HashMap<String, ServiceConfigNaming> configMap = new HashMap<String, ServiceConfigNaming>();

    public synchronized static ServiceConfigNaming getDefaultServiceCfgNaming(String serviceSection, String serviceType) {
        String key = serviceSection + ":" + serviceType;
        if (configMap.containsKey(key)) {
            return configMap.get(key);
        }

        NamingService ns = NamingServiceSngl.get();
        FiveProps envProps = Props.instance().getEnvProps();
        ServiceConfigNaming cfg = ConfigureProps.getCfg(envProps, ns, serviceSection, serviceType);
        configMap.put(key, cfg);

        return cfg;
    }

    public synchronized static ServiceConfigNaming getDefaultServiceCfgNaming(String serviceSection, String serviceType, TransProfileContainer transProfileContainer) {
        ServiceConfigNaming cfg = getDefaultServiceCfgNaming(serviceSection, serviceType);

        if (transProfileContainer != null) {
            cfg.setTransProfileContainer(transProfileContainer);
        }

        return cfg;
    }

    public synchronized static ServiceConfigNaming getDefaultServiceCfgNaming(String serviceSection, String serviceType, ConnChooser chooser) {
        ServiceConfigNaming cfg = getDefaultServiceCfgNaming(serviceSection, serviceType);

        if (chooser != null) {
            cfg.setConnChooser(chooser);
        }

        return cfg;
    }
}
