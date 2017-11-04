/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.profile;

import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.profile.ProfileConstants;
import com.enjoyf.platform.util.FiveProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class ProfileInitializer implements ServiceInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ProfileInitializer.class);

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(ProfileConstants.SERVICE_PREFIX, servProps);

        ProfileConfig config = new ProfileConfig(servProps);
        logger.info("ProfileInitializer config is " + config.toString());

        ProfileLogic logic = new ProfileLogic(config);

        ProfilePacketDecoder packetDecoder = new ProfilePacketDecoder(logic);
        server.setPacketProcessor(packetDecoder);

        return new ServiceConfig(servProps, ProfileConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
