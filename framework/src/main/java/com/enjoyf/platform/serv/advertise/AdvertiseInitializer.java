/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.advertise;

import com.enjoyf.platform.serv.rpc.RPCServer;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.advertise.AdvertiseConstants;
import com.enjoyf.platform.service.advertise.AdvertiseService;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class AdvertiseInitializer implements ServiceInitializer {
    //
    private static final Logger logger = LoggerFactory.getLogger(AdvertiseInitializer.class);

    //
    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(AdvertiseConstants.SERVICE_PREFIX, servProps);

        AdvertiseConfig config = new AdvertiseConfig(servProps);
        logger.info("AccountInitializer, config is " + config.toString());

        AdvertiseLogic logic = new AdvertiseLogic(config);
        try {
            PacketDecoder processor = new RPCServer<AdvertiseService>(logic).createService(AdvertiseService.class);

            server.setPacketProcessor(processor);
        } catch (InstantiationException e) {
            GAlerter.lab("Cannot create server", e);

            System.exit(1);
        } catch (IllegalAccessException e) {
            GAlerter.lab("Cannot create server", e);
            System.exit(2);
        }

        return new ServiceConfig(servProps, AdvertiseConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
