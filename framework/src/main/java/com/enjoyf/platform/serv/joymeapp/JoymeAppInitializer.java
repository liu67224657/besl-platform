/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.joymeapp;

import com.enjoyf.platform.serv.rpc.RPCServer;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.joymeapp.JoymeAppConstants;
import com.enjoyf.platform.service.joymeapp.JoymeAppService;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class JoymeAppInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(JoymeAppInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(JoymeAppConstants.SERVICE_PREFIX, servProps);

        JoymeAppConfig config = new JoymeAppConfig(servProps);
        logger.info("ClientInitializer, config is " + config.toString());

        JoymeAppLogic logic = new JoymeAppLogic(config);

        try {
            PacketDecoder processor = new RPCServer<JoymeAppService>(logic).createService(JoymeAppService.class);
            server.setPacketProcessor(processor);
        } catch (InstantiationException e) {
            GAlerter.lab("Cannot create message server", e);

            System.exit(1);
        } catch (IllegalAccessException e) {
            GAlerter.lab("Cannot create message server", e);
            System.exit(2);
        }

        return new ServiceConfig(servProps, JoymeAppConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
