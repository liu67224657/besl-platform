/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.service.ConnChooserRingPartition;
import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;

/**
 * @author Yin Pengyi
 */
public class ContentServiceSngl {
    private static ContentService service = null;

    /**
     * Sets the RateCheckService for later retrieval
     *
     * @param service - the RateCheckService for this object
     */
    public static synchronized void set(ContentService service) {
        ContentServiceSngl.service = service;
    }

    /**
     * Gets the RateCheckService
     *
     * @return RateCheckService - the RateCheckService
     */
    public static synchronized ContentService get() {
        if (service == null) {
            ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(
                    ContentConstants.SERVICE_SECTION, ContentConstants.SERVICE_TYPE);

            //set the conn chooser type.
            cfg.setConnChooser(new ConnChooserRingPartition(
                    EnvConfig.get().getServicePartitionNum(ContentConstants.SERVICE_SECTION),
                    EnvConfig.get().getServicePartitionFailoverNum(ContentConstants.SERVICE_SECTION),
                    EnvConfig.get().getRequestTimeoutMsecs()));

            service = new ContentServiceImpl(cfg);
        }

        return service;
    }
}