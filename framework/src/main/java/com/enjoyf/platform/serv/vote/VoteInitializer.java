/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.vote;

import com.enjoyf.platform.serv.rpc.RPCServer;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.vote.VoteConstants;
import com.enjoyf.platform.service.vote.VoteService;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:ericliu@enjoyfound.com>eric liu</a>
 */
class VoteInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(VoteInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(VoteConstants.SERVICE_PREFIX, servProps);

        VoteConfig config = new VoteConfig(servProps);
        logger.info("VoteInitializer, config is " + config.toString());

        VoteLogic logic = new VoteLogic(config);

        try {
            PacketDecoder processor = new RPCServer<VoteService>(logic).createService(VoteService.class);
            server.setPacketProcessor(processor);
        } catch (InstantiationException e) {
            GAlerter.lab("Cannot create vote server", e);
            System.exit(1);
        } catch (IllegalAccessException e) {
            GAlerter.lab("Cannot create vote server", e);
            System.exit(2);
        }

        return new ServiceConfig(servProps, VoteConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
