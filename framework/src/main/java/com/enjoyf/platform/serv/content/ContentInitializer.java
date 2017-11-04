/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.content;

import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.content.ContentConstants;
import com.enjoyf.platform.util.FiveProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class ContentInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ContentInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(ContentConstants.SERVICE_PREFIX, servProps);

        ContentConfig config = new ContentConfig(servProps);

        logger.info("ContentInitializer: ContentConfig is " + config.toString());

        ContentLogic logic = new ContentLogic(config);
        server.setPacketProcessor(new ContentPacketDecoder(logic));

        return new ServiceConfig(servProps, ContentConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
