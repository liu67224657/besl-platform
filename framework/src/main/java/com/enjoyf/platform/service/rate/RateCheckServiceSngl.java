/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.rate;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.service.ConnChooserRingPartition;
import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;

/**
 * @author Yin Pengyi
 */
public class RateCheckServiceSngl {
    private static RateCheckService service = null;

    private RateCheckServiceSngl() {
    }

    /**
     * Sets the RateCheckService for later retrieval
     *
     * @param service - the RateCheckService for this object
     */
    public static synchronized void set(RateCheckService service) {
        RateCheckServiceSngl.service = service;
    }

    /**
     * Gets the RateCheckService
     *
     * @return RateCheckService - the RateCheckService
     */
    public static synchronized RateCheckService get() {
        if (service == null) {
            ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(
                    RateConstants.SERVICE_SECTION, RateConstants.SERVICE_TYPE);

            cfg.setConnChooser(new ConnChooserRingPartition(
                    EnvConfig.get().getServicePartitionNum(RateConstants.SERVICE_SECTION),
                    EnvConfig.get().getServicePartitionFailoverNum(RateConstants.SERVICE_SECTION),
                    EnvConfig.get().getRequestTimeoutMsecs()));

            service = new RateCheckService(cfg);
        }
        return service;
    }
}