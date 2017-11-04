package com.enjoyf.platform.serv.tools;

import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.tools.ToolsConstants;
import com.enjoyf.platform.util.FiveProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 下午2:19
 * Desc:
 */
public class ToolsInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ToolsInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(ToolsConstants.SERVICE_PREFIX, servProps);

        ToolsConfig config = new ToolsConfig(servProps);
        logger.info("ToolsInitializer, config is " + config.toString());

        ToolsLogic logic = new ToolsLogic(config);

        server.setPacketProcessor(new ToolsPacketDecoder(logic));
        return new ServiceConfig(servProps, ToolsConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
