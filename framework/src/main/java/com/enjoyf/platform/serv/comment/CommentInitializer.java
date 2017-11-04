/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.comment;

import com.enjoyf.platform.serv.rpc.RPCServer;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.comment.CommentService;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class CommentInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(CommentInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(CommentConstants.SERVICE_PREFIX, servProps);

        CommentConfig config = new CommentConfig(servProps);
        logger.info("CommentInitializer config is " + config.toString());

        CommentLogic logic = new CommentLogic(config);

        try {
            PacketDecoder processor = new RPCServer<CommentService>(logic).createService(CommentService.class);
            server.setPacketProcessor(processor);
        } catch (InstantiationException e) {
            GAlerter.lab("Cannot create message server", e);

            System.exit(1);
        } catch (IllegalAccessException e) {
            GAlerter.lab("Cannot create message server", e);
            System.exit(2);
        }

        return new ServiceConfig(servProps, CommentConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
