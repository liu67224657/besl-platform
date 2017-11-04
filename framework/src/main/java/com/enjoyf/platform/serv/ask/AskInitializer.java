/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.ask;

import com.enjoyf.platform.serv.rpc.RPCServer;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.ask.AskConstants;
import com.enjoyf.platform.service.ask.AskService;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class AskInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(AskInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(AskConstants.SERVICE_PREFIX, servProps);

        AskConfig config = new AskConfig(servProps);
        logger.info("AskInitializer config is " + config.toString());

        AskLogic logic = new AskLogic(config);

        try {
            PacketDecoder processor = new RPCServer<AskService>(logic).createService(AskService.class);
            server.setPacketProcessor(processor);
        } catch (InstantiationException e) {
            GAlerter.lab("Cannot create message server", e);

            System.exit(1);
        } catch (IllegalAccessException e) {
            GAlerter.lab("Cannot create message server", e);
            System.exit(2);
        }

        return new ServiceConfig(servProps, AskConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
