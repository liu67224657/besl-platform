/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.lottery;

import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.lottery.LotteryConstants;
import com.enjoyf.platform.util.FiveProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class LotteryInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(LotteryInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(LotteryConstants.SERVICE_PREFIX, servProps);

        LotteryConfig config = new LotteryConfig(servProps);
        logger.info("LotteryInitializer config is " + config.toString());

        LotteryLogic logic = new LotteryLogic(config);

        LotteryPacketDecoder packetDecoder = new LotteryPacketDecoder(logic);
        server.setPacketProcessor(packetDecoder);

        return new ServiceConfig(servProps, LotteryConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
