/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.joymeappconfig;

import com.enjoyf.platform.serv.rpc.RPCServer;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigConstants;
import com.enjoyf.platform.service.joymeappconfig.JoymeAppConfigService;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class JoymeAppConfigInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(JoymeAppConfigInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(JoymeAppConfigConstants.SERVICE_PREFIX, servProps);

        JoymeAppConfigConfig config = new JoymeAppConfigConfig(servProps);
        logger.info("ClientInitializer, config is " + config.toString());

        JoymeAppConfigLogic logic = new JoymeAppConfigLogic(config);

        try {
            PacketDecoder processor = new RPCServer<JoymeAppConfigService>(logic).createService(JoymeAppConfigService.class);
            server.setPacketProcessor(processor);
        } catch (InstantiationException e) {
            GAlerter.lab("Cannot create message server", e);

            System.exit(1);
        } catch (IllegalAccessException e) {
            GAlerter.lab("Cannot create message server", e);
            System.exit(2);
        }

        return new ServiceConfig(servProps, JoymeAppConfigConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
