/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.timeline;

import com.enjoyf.platform.serv.rpc.RPCServer;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.timeline.TimeLineConstants;
import com.enjoyf.platform.service.timeline.TimeLineService;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class TimeLineInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(TimeLineInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(TimeLineConstants.SERVICE_PREFIX, servProps);

        TimeLineConfig config = new TimeLineConfig(servProps);
        logger.info("TimeLineInitializer, config is " + config.toString());

        TimeLineLogic logic = new TimeLineLogic(config);

        try {
            PacketDecoder processor = new RPCServer<TimeLineService>(logic).createService(TimeLineService.class);
            server.setPacketProcessor(processor);
        } catch (InstantiationException e) {
            GAlerter.lab("Cannot create TimeLine server", e);

            System.exit(1);
        } catch (IllegalAccessException e) {
            GAlerter.lab("Cannot create TimeLine server", e);
            System.exit(2);
        }

        return new ServiceConfig(servProps, TimeLineConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
