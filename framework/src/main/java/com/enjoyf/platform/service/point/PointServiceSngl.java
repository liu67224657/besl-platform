/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.point;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.serv.point.PointLogic;
import com.enjoyf.platform.service.service.ConnChooserRR;
import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;

/**
 * @author Yin Pengyi
 */
public class PointServiceSngl {
    private static PointService service = null;

    private PointServiceSngl() {
    }

    /**
     * Gets the RateCheckService
     *
     * @return RateCheckService - the RateCheckService
     */
    public static synchronized PointService get() {
        if (service == null) {
            ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(
                    PointConstants.SERVICE_SECTION, PointConstants.SERVICE_TYPE);

            //set the conn chooser type.
            cfg.setConnChooser(new ConnChooserRR(EnvConfig.get().getRequestTimeoutMsecs()));

            service = new PointServiceImpl(cfg);
        }

        return service;
    }
}