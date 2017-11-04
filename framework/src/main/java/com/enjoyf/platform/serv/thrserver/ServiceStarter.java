package com.enjoyf.platform.serv.thrserver;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.service.naming.Registrant;
import com.enjoyf.platform.service.service.PerformanceDataCollector;
import com.enjoyf.platform.service.service.ServiceId;
import com.enjoyf.platform.util.Utility;

/**
 * Singleton class to startup services.
 */
public class ServiceStarter {
    
    private static final Logger logger = LoggerFactory.getLogger(ServiceStarter.class);
    
    private static ServiceStarter instance = new ServiceStarter();

    private ServerDieListener dieListener = new MyDieListener();

    public static ServiceStarter instance() {
        return instance;
    }

    private class MyDieListener implements ServerDieListener {
        public void notify(ServerThread server) {
            logger.info("ServiceStarter: GOT A DIE CALL: " + server);
            //--
            // Wait for cleanup code to execute before force-killing this
            // logicProcess.
            //--
            Utility.sleep(3 * 1000);
            System.exit(1);
        }
    }

    /**
     * Fires up a set of related services. Once this method is inovked,
     * the services will all be listening for transactions.
     */
    public void start(ServiceConfig config) {
        //--
        // Fire up all the ServerThread objects.
        //--
        for (Iterator itr = config.getServiceWraps().iterator(); itr.hasNext();) {
            ServerWrap serverWrap = (ServerWrap) itr.next();
            //--
            // Enable metrics for this service.
            //--
            if (config.areMetricsEnabled()) {
                p_enableMetrics(serverWrap);
            }
            
            ServerThread server = serverWrap.getServer();
            if (serverWrap.shouldExitOnDie() && server != null) {
                server.addServerDieListener(dieListener);
            }

            logger.info("ServiceStarter: Starting server: " + serverWrap);
            serverWrap.start();
        }
    }

    private void p_enableMetrics(ServerWrap serverWrap) {
        ServerThread server = serverWrap.getServer();

        Registrant registrant = serverWrap.getRegistrant();
        if (registrant == null) {
            return;
        }

        //--
        // A bit ugly, since the object we want is a ServerThreadDefault.
        //--
        if (!(server instanceof ServerThreadDefault)) {
            return;
        }

        //--
        // What the MetricsWriter wants is the ServiceId + the
        // PerformanceDataCollector. We get the ServiceId from the Registrant,
        // and the PerformanceDataCollector from the PacketProcessor.
        //--
        ServerThreadDefault defServer = (ServerThreadDefault) server;
        PacketProcessor packetProcessor = defServer.getPacketProcessor();

        //--
        // A PacketProcessor may or may not be tagged with
        // the interface for returning metrics data (ie, implementing
        // the PerfDataAccessor interface). If not, we just return.
        //--
        if (!(packetProcessor instanceof PerfDataAccessor)) {
            return;
        }

        PerformanceDataCollector collector =
                ((PerfDataAccessor) packetProcessor).getPerfDataCollector();

        ServiceId serviceId = registrant.getServiceInfo().getServiceId();
        //MetricsWriter.instance().registerEntry(serviceId, collector);
    }
}
