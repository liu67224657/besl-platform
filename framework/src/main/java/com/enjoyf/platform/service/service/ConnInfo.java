package com.enjoyf.platform.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class to contain the info for a service.
 */
public class ConnInfo {
    
	private static final Logger logger = LoggerFactory.getLogger(ConnInfo.class);
	
    private ServiceData serviceData;
    private ServiceConn serviceConn;

    public ConnInfo(ServiceData servData, ServiceConn conn) {
        serviceData = servData;
        serviceConn = conn;

        String serviceName = getServiceData().getServiceId().getServiceName();

        int ival = 0;
        try {
            ival = Integer.parseInt(serviceName.substring(serviceName.length() - 2));
        } catch (Exception e) {
            logger.info("ConnInfo.ctor: If this is a partitioned service "
                    + " Got a bogus serviceName = " + serviceName
                    + " The service type = "
                    + serviceData.getServiceId().getServiceType());
        }
        conn.setPartition(ival - 1);
    }

    public boolean shouldRemove() {
        return serviceConn.shouldRemove();
    }

    public ServiceData getServiceData() {
        return serviceData;
    }

    public ServiceConn getServiceConn() {
        return serviceConn;
    }

    public int getPartition() {
        return serviceConn.getPartition();
    }

    @Override
    public String toString() {
        return "serviceData=" + serviceData + ":serviceConn=" + serviceConn;
    }
}
