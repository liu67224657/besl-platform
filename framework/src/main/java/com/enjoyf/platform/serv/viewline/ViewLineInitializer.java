package com.enjoyf.platform.serv.viewline;

import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.viewline.ViewLineConstants;
import com.enjoyf.platform.util.FiveProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 下午2:19
 * Desc:
 */
public class ViewLineInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ViewLineInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(ViewLineConstants.SERVICE_PREFIX, servProps);

        ViewLineConfig config = new ViewLineConfig(servProps);
        logger.info("ViewLineInitializer, config is " + config.toString());

        ViewLineLogic logic = new ViewLineLogic(config);

        server.setPacketProcessor(new ViewLinePacketDecoder(logic));
        return new ServiceConfig(servProps, ViewLineConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
