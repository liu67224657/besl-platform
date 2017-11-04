package com.enjoyf.platform.serv.management;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import com.enjoyf.platform.util.log.GAlerter;

/**
 * You need to specify the jmx port from command line, e.g. -Dcom.sun.management.jmxremote.port=7878
 * Otherwise you can only connect to the MBeans locally
 * @author Daniel
 */
public class ManagementServer extends Thread {
	
	private JMXConnectorServer cs;
	private int port;
	
	public ManagementServer(int port) {
		this.setDaemon(true);
		this.port = port;
	}
	
	public void run() {
    	try {
    		LocateRegistry.createRegistry(port);
    		
        	Log4jManagementMBean mbean = new Log4jManagement();
        	MBeanServer server = ManagementFactory.getPlatformMBeanServer();
			server.registerMBean(mbean, new ObjectName("com.enjoyf.platform.management:name=ManagementMBean"));
			
			JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://0.0.0.0/jndi/rmi://localhost:" + port +
					"/management"); 
			                                        
			JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, server); 
			cs.start(); 
		} catch (Exception e) {
			GAlerter.lab("Cannot start management server", e);
		}
	}
	
	public void shutdown() {
		try {
			cs.stop();
		} catch (IOException e) {
			GAlerter.lab("Cannot stop management server", e);
		}
	}
}
