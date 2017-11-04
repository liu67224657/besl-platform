/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.message;

import com.enjoyf.platform.serv.rpc.RPCServer;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.message.MessageConstants;
import com.enjoyf.platform.service.message.MessageService;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class MessageInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(MessageInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(MessageConstants.SERVICE_PREFIX, servProps);

        MessageConfig config = new MessageConfig(servProps);
        logger.info("MessageInitializer, config is " + config.toString());

        MessageLogic logic = new MessageLogic(config);

        try {
            PacketDecoder processor = new RPCServer<MessageService>(logic).createService(MessageService.class);
            server.setPacketProcessor(processor);
        } catch (InstantiationException e) {
            GAlerter.lab("Cannot create message server", e);

            System.exit(1);
        } catch (IllegalAccessException e) {
            GAlerter.lab("Cannot create message server", e);
            System.exit(2);
        }

        return new ServiceConfig(servProps, MessageConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
