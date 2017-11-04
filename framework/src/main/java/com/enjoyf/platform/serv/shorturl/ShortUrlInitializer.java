/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.shorturl;

import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.shorturl.ShortUrlConstants;
import com.enjoyf.platform.util.FiveProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class ShortUrlInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ShortUrlInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(ShortUrlConstants.SERVICE_PREFIX, servProps);

        ShortUrlConfig config = new ShortUrlConfig(servProps);
        logger.info("ShortUrlInitializer, config is " + config.toString());

        ShortUrlLogic logic = new ShortUrlLogic(config);

        server.setPacketProcessor(new ShortUrlPacketDecoder(logic));
        return new ServiceConfig(servProps, ShortUrlConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
