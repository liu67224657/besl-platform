package com.enjoyf.platform.serv.alert;

import com.enjoyf.platform.serv.thrserver.ConnDieListener;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.ServerThreadStandard;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.alert.AlertConstants;
import com.enjoyf.platform.util.FiveProps;

class AlertInitializer implements ServiceInitializer {

    public ServiceConfig init(FiveProps servProps) {
        ServerThreadStandard server = new ServerThreadStandard(AlertConstants.SERVICE_PREFIX, servProps);
        Config cfg = new Config(servProps);
        final Logic logic = new Logic(cfg);

        server.setPacketProcessor(new AlertPacketDecoder(logic));

        server.addConnDieListener(new ConnDieListener() {
            public void notify(ConnThreadBase conn) {
                logic.connDied(conn);
            }
        });

        return new ServiceConfig(servProps, AlertConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
    }
}
