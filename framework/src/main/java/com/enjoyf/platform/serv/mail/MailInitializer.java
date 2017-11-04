/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.mail;

import com.enjoyf.platform.serv.thrserver.ServerThreadRequestPool;
import com.enjoyf.platform.serv.thrserver.ServiceConfig;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.mail.MailConstants;
import com.enjoyf.platform.util.FiveProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yin Pengyi
 */
class MailInitializer implements ServiceInitializer {
    //
    private static final Logger logger = LoggerFactory.getLogger(MailInitializer.class);

    //
    public ServiceConfig init(FiveProps servProps) {
        ServerThreadRequestPool server = new ServerThreadRequestPool(MailConstants.SERVICE_PREFIX, servProps);

        MailConfig config = new MailConfig(servProps);
        MailLogic logic = new MailLogic(config);

        logger.info("Mail server's config is " + config);

        server.setPacketProcessor(new MailPacketDecoder(logic));
        server.setMaxPacketLength(1024 * 1024 * 10);

        return new ServiceConfig(servProps, MailConstants.SERVICE_PREFIX, server);
    }

    public void postStart() {
    }
}