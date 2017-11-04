/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.naming;

import com.enjoyf.platform.serv.thrserver.ConnDieListener;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServerWrap;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.util.FiveProps;

/**
 * This is the naming server.
 */
class Initializer implements ServiceInitializer {
    public ServiceConfig init(FiveProps servProps) {
        int port = servProps.getInt("server.port", 7500);
        int maxReqThreads = servProps.getInt("server.maxReqThreads", 100);
        ServerThreadRequestPool server = new ServerThreadRequestPool(port, maxReqThreads, servProps);

        ServerWrap serverWrap = new ServerWrap(server);
        serverWrap.setExitOnDie(true);
        ServiceConfig serviceConfig = new ServiceConfig(serverWrap);

        Config cfg = new Config(servProps);
        final Logic logic = new Logic(cfg);

        Monitor mon = new Monitor(logic);
        mon.start();

        Vulture v = new Vulture(logic);
        v.start();

        //--
        // Register a conn died listener so we know when
        // we've lost a connection.
        //--
        server.addConnDieListener(new ConnDieListener() {
            public void notify(ConnThreadBase conn) {
                logic.connDied(conn);
            }
        }
        );

        server.setPacketProcessor(new NamingPacketDecoder(logic));
        return serviceConfig;
    }

    public void postStart() {
    }
}
