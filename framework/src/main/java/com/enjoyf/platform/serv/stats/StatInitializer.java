package com.enjoyf.platform.serv.stats;

import com.enjoyf.platform.serv.rpc.RPCServer;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.stats.StatConstants;
import com.enjoyf.platform.service.stats.StatService;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel
 */
class StatInitializer implements ServiceInitializer {

    private static final Logger logger = LoggerFactory.getLogger(StatInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(StatConstants.SERVICE_PREFIX, servProps);

        StatConfig config = new StatConfig(servProps);
        logger.info(config.toString());

        StatLogic logic = new StatLogic(config);
        try {
            PacketDecoder processor = new RPCServer<StatService>(logic).createService(StatService.class);
            server.setPacketProcessor(processor);
        } catch (InstantiationException e) {
            GAlerter.lab("Cannot create stats server", e);
            System.exit(1);
        } catch (IllegalAccessException e) {
            GAlerter.lab("Cannot create stats server", e);
            System.exit(2);
        }
        return new ServiceConfig(servProps, StatConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
