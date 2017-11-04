/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.serv.example;

import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.example.ExampleConstants;
import com.enjoyf.platform.util.FiveProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
class ExampleInitializer implements ServiceInitializer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * Perform any initialization required prior to starting things up
     * and return the ServiceConfig object to be used by framework for
     * starting up the service.
     */
    @Override
    public ServiceConfig init(FiveProps props) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(ExampleConstants.SERVICE_PREFIX, props);

        ExampleConfig config = new ExampleConfig(props);

        logger.info("GameResourceInitializer: GameResourceConfig is " + config.toString());

        ExampleLogic logic = new ExampleLogic(config);
        server.setPacketProcessor(new ExamplePacketDecoder(logic));

        return new ServiceConfig(props, ExampleConstants.SERVICE_PREFIX, server);
    }

    /**
     * Invoked after the service has been started.
     */
    @Override
    public void postStart() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
