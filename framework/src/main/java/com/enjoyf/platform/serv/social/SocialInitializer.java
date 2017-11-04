/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.social;

import com.enjoyf.platform.serv.rpc.RPCServer;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.social.SocialConstants;
import com.enjoyf.platform.service.social.SocialService;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class SocialInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(SocialInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(SocialConstants.SERVICE_PREFIX, servProps);

        SocialConfig config = new SocialConfig(servProps);
        logger.info("SocialInitializer, config is " + config.toString());

        SocialLogic logic = new SocialLogic(config);

        try {
            PacketDecoder processor = new RPCServer<SocialService>(logic).createService(SocialService.class);
            server.setPacketProcessor(processor);
        } catch (InstantiationException e) {
            GAlerter.lab("Cannot create social server", e);

            System.exit(1);
        } catch (IllegalAccessException e) {
            GAlerter.lab("Cannot create social server", e);
            System.exit(2);
        }

        return new ServiceConfig(servProps, SocialConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
