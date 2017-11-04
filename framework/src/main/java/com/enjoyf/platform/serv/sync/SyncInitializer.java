/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.sync;

import com.enjoyf.platform.serv.rpc.RPCServer;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.sync.SyncConstants;
import com.enjoyf.platform.service.sync.SyncService;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:ericliu@enjoyfound.com>eric liu</a>
 */
class SyncInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SyncInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(SyncConstants.SERVICE_PREFIX, servProps);

        SyncConfig config = new SyncConfig(servProps);
        logger.info("SyncInitializer, config is " + config.toString());

        SyncLogic logic = new SyncLogic(config);

        try {
            PacketDecoder processor = new RPCServer<SyncService>(logic).createService(SyncService.class);
            server.setPacketProcessor(processor);
        } catch (InstantiationException e) {
            GAlerter.lab("Cannot create oauth server", e);

            System.exit(1);
        } catch (IllegalAccessException e) {
            GAlerter.lab("Cannot create oauth server", e);
            System.exit(2);
        }

        return new ServiceConfig(servProps, SyncConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
