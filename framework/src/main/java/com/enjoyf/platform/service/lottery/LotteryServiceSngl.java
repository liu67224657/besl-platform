package com.enjoyf.platform.service.lottery;

import com.enjoyf.platform.props.EnvConfig;
import com.enjoyf.platform.service.service.ConnChooserRR;
import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-24
 * Time: 上午11:48
 * To change this template use File | Settings | File Templates.
 */
public class LotteryServiceSngl {
    private static LotteryService service = null;


    /**
     * Gets the RateCheckService
     *
     * @return RateCheckService - the RateCheckService
     */
    public static synchronized LotteryService get() {
        if (service == null) {
            ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(
                    LotteryConstants.SERVICE_SECTION, LotteryConstants.SERVICE_TYPE);

            //set the conn chooser type.
            cfg.setConnChooser(new ConnChooserRR(EnvConfig.get().getRequestTimeoutMsecs()));

            service = new LotteryServiceImpl(cfg);
        }

        return service;
    }
}
