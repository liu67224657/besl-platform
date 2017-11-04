/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.oauth;

import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.oauth.OAuthConstants;
import com.enjoyf.platform.util.FiveProps;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class OAuthInitializer implements ServiceInitializer {

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(OAuthConstants.SERVICE_PREFIX, servProps);

        OAuthConfig config = new OAuthConfig(servProps);

        OAuthLogic logic = new OAuthLogic(config);
        server.setPacketProcessor(new OAuthPacketDecoder(logic));

        return new ServiceConfig(servProps, OAuthConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
        //todo
    }
}
