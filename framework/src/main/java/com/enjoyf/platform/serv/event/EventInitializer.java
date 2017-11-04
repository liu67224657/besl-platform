/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.serv.event;

import com.enjoyf.platform.serv.management.ManagementServer;
import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.event.EventConstants;
import com.enjoyf.platform.util.FiveProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
class EventInitializer implements ServiceInitializer {

    private static final Logger logger = LoggerFactory.getLogger(EventInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(EventConstants.SERVICE_PREFIX, servProps);

        //start MBeans daemon
        ManagementServer mgtServer = new ManagementServer(server.getPort() + 30000);
        mgtServer.start();


        EventConfig config = new EventConfig(servProps);
        logger.info(config.toString());

        EventLogic logic = new EventLogic(config);
        ActivityLogic activityLogic = new ActivityLogic(config);
        server.setPacketProcessor(new EventPacketDecoder(logic, activityLogic));

        return new ServiceConfig(servProps, EventConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
