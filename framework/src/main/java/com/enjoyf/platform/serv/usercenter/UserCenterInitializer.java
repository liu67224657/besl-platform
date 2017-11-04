/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.usercenter;

import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.usercenter.UserCenterConstants;
import com.enjoyf.platform.util.FiveProps;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class UserCenterInitializer implements ServiceInitializer {
    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(UserCenterConstants.SERVICE_PREFIX, servProps);

        UserCenterConfig config = new UserCenterConfig(servProps);
        //GAlerterLogger.lh(config.toString());

        UserCenterLogic logic = new UserCenterLogic(config);
        server.setPacketProcessor(new UserCenterPacketDecoder(logic));

        return new ServiceConfig(servProps, UserCenterConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
