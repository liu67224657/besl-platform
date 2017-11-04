package com.enjoyf.platform.serv.gameres;

import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.gameres.GameResourceConstants;
import com.enjoyf.platform.util.FiveProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: zhaoxin
 * Date: 11-8-25
 * Time: 下午4:35
 * Desc:
 */
class GameResourceInitializer implements ServiceInitializer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * Perform any initialization required prior to starting things up
     * and return the ServiceConfig object to be used by framework for
     * starting up the service.
     */
    @Override
    public ServiceConfig init(FiveProps props) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(GameResourceConstants.SERVICE_PREFIX, props);

        GameResourceConfig config = new GameResourceConfig(props);

        logger.info("GameResourceConfig is " + config.toString());

        GameResourceLogic logic = new GameResourceLogic(config);
        server.setPacketProcessor(new GameResourcePacketDecoder(logic));

        return new ServiceConfig(props, GameResourceConstants.SERVICE_PREFIX, server);
    }

    /**
     * Invoked after the service has been started.
     */
    @Override
    public void postStart() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
