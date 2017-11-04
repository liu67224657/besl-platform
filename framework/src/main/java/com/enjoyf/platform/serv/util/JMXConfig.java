/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.util;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.enjoyf.platform.util.FiveProps;

public class JMXConfig {
    public static Log LOG = LogFactory.getLog(JMXConfig.class);

    public static void config(FiveProps props) {
        if (props != null) {
            if (props.getBoolean("jvm.jmx.enable", false)) {
                int registryPort = props.getInt("jvm.jmx.rmi.registry.port");
                int remotePort = props.getInt("jvm.jmx.rmi.remote.port");
                String serviceName = props.get("service.NAME");

                // start rmi registry
                try {
                    LocateRegistry.createRegistry(registryPort);
                } catch (RemoteException e) {
                    LOG.info("unable to create rmi registry on port " + registryPort);
                }

                // create jmx service url
                JMXServiceURL url = null;
                try {
                    url = new JMXServiceURL("service:jmx:rmi://localhost:" + remotePort +
                            "/jndi/rmi://localhost:" + registryPort + "/" + serviceName);
                } catch (MalformedURLException e) {
                    LOG.error("error creating jmx service url", e);
                    return;
                }

                // start the jmx connector server
                JMXConnectorServer server = null;
                try {
                    server = JMXConnectorServerFactory.newJMXConnectorServer(url, null,
                            ManagementFactory.getPlatformMBeanServer());
                    server.start();
                    LOG.info("started jmx server, jmx url = " + url);
                } catch (IOException e) {
                    LOG.error("error starting jmx connector server", e);
                }
            }
        } else {
            LOG.error("null props - can't load config");
        }
    }
}
