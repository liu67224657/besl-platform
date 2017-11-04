package com.enjoyf.platform.serv.billing;

import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.billing.BillingConstants;
import com.enjoyf.platform.util.FiveProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: EricLiu
 * Date: 12-1-10
 * Time: 下午2:50
 * To change this template use File | Settings | File Templates.
 */
public class BillingInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(BillingInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(BillingConstants.SERVICE_PREFIX, servProps);

        BillingConfig config = new BillingConfig(servProps);
        logger.info("BillingInitializer, config is " + config.toString());

        BillingLogic logic = new BillingLogic(config);
        BillingPacketDecoder packetDecoder = new BillingPacketDecoder(logic);
        server.setPacketProcessor(packetDecoder);

        return new ServiceConfig(servProps, BillingConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
