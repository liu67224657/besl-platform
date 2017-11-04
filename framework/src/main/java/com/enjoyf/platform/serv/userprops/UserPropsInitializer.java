/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.userprops;

import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.userprops.UserPropsConstants;
import com.enjoyf.platform.util.FiveProps;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class UserPropsInitializer implements ServiceInitializer {
    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(UserPropsConstants.SERVICE_PREFIX, servProps);

        UserPropsConfig config = new UserPropsConfig(servProps);
        //GAlerterLogger.lh(config.toString());

        UserPropsLogic logic = new UserPropsLogic(config);
        server.setPacketProcessor(new UserPropsPacketDecoder(logic));

        return new ServiceConfig(servProps, UserPropsConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
