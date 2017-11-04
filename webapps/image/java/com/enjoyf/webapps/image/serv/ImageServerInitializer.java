/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.webapps.image.serv;

import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.util.FiveProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class ImageServerInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ImageServerInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        //
        ImageServerConfig config = ImageServerConfig.get();
        logger.info("ImageServerInitializer: ImageServerConfig is " + config.toString());

        //the server.
        ServerThreadRequestPool server = new ServerThreadRequestPool(config.getServicePrefix(), servProps);

        //
        ImageServerLogic logic = new ImageServerLogic(config);
        server.setPacketProcessor(new ImageServerPacketDecoder(logic));

        //
        return new ServiceConfig(servProps, config.getServicePrefix(), server);
    }

    public void postStart() {
        //todo
    }
}
