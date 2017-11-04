/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.misc;

import com.enjoyf.platform.serv.rpc.RPCServer;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.misc.MiscConstants;
import com.enjoyf.platform.service.misc.MiscService;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class MiscInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(MiscInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(MiscConstants.SERVICE_PREFIX, servProps);

        MiscConfig config = new MiscConfig(servProps);
        logger.info("MiscInitializer, config is " + config.toString());

        MiscLogic logic = new MiscLogic(config);
        try {
            PacketDecoder processor = new RPCServer<MiscService>(logic).createService(MiscService.class);
            server.setPacketProcessor(processor);
        } catch (InstantiationException e) {
            GAlerter.lab("Cannot create server", e);

            System.exit(1);
        } catch (IllegalAccessException e) {
            GAlerter.lab("Cannot create server", e);
            System.exit(2);
        }

        return new ServiceConfig(servProps, MiscConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
