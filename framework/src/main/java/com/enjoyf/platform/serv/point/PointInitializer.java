/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.point;

import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.point.PointConstants;
import com.enjoyf.platform.service.profile.ProfileConstants;
import com.enjoyf.platform.util.FiveProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class PointInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(PointInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(PointConstants.SERVICE_PREFIX, servProps);

        PointConfig config = new PointConfig(servProps);
        logger.info("PointInitializer config is " + config.toString());

        PointLogic logic = new PointLogic(config);

        PointPacketDecoder packetDecoder = new PointPacketDecoder(logic);
        server.setPacketProcessor(packetDecoder);

        return new ServiceConfig(servProps, PointConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
