package com.enjoyf.platform.service.billing;

import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;
import com.enjoyf.platform.service.service.rpc.RPCClient;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-1-10
 * Time: 下午1:59
 * To change this template use File | Settings | File Templates.
 */
public class BillingServiceSngl {
    private static BillingService instance;

    public static BillingService get() {
        if (instance == null) {
            synchronized (BillingServiceSngl.class) {
                if (instance == null) {
                    ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(BillingConstants.SERVICE_SECTION, BillingConstants.SERVICE_TYPE);

                    instance = new BillingServiceImpl(cfg);
                }
            }
        }

        return instance;
    }
}
