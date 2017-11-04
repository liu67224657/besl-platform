/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.notice;

import com.enjoyf.platform.serv.rpc.RPCServer;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.notice.NoticeConstants;
import com.enjoyf.platform.service.notice.NoticeService;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class NoticeInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(NoticeInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(NoticeConstants.SERVICE_PREFIX, servProps);

        NoticeConfig config = new NoticeConfig(servProps);
        logger.info("NoticeInitializer, config is " + config.toString());

        NoticeLogic logic = new NoticeLogic(config);

        try {
            PacketDecoder processor = new RPCServer<NoticeService>(logic).createService(NoticeService.class);
            server.setPacketProcessor(processor);
        } catch (InstantiationException e) {
            GAlerter.lab("Cannot create message server", e);

            System.exit(1);
        } catch (IllegalAccessException e) {
            GAlerter.lab("Cannot create message server", e);
            System.exit(2);
        }

        return new ServiceConfig(servProps, NoticeConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
