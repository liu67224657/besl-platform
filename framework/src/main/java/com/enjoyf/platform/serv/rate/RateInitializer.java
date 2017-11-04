package com.enjoyf.platform.serv.rate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.rate.RateConstants;
import com.enjoyf.platform.util.FiveProps;

/**
 * @author Yin Pengyi
 */
class RateInitializer implements ServiceInitializer {
	
	private static final Logger logger = LoggerFactory.getLogger(RateInitializer.class);
	
    public RateInitializer() {

    }

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(RateConstants.SERVICE_PREFIX, servProps);

        RateConfig config = new RateConfig(servProps);
        logger.info(config.toString());

        RateLogic logic = new RateLogic(config);
        
        server.setPacketProcessor(new RatePacketDecoder(logic));

        return new ServiceConfig(servProps, RateConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
    }
}
