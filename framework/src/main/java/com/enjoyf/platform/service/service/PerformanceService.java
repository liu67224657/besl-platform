/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.service.naming.NamingService;

/**
 * This is the service class for accessing performance data for a
 * "normal" service.
 */
public class PerformanceService {
	
	private static final Logger logger = LoggerFactory.getLogger(PerformanceService.class);
    private ServiceId serviceId;
    protected ReqProcessor reqProcessor = null;

    /**
     * @param serviceId The specific service we are interested in.
     */
    public PerformanceService(NamingService namingService, ServiceId serviceId) {
        this.serviceId = serviceId;
        ServiceRequest req = new ServiceRequest(serviceId.getServiceType(), serviceId.getServiceName());
        if (logger.isTraceEnabled()) {
        	logger.trace("PerformanceService creation for server: " + req);
        }
        ServiceConfigNaming scfg = new ServiceConfigNaming(namingService, req);
        reqProcessor = scfg.getReqProcessor();
    }

    /**
     * Retrieve performance data for a service.
     *
     * @throws ServiceException Thrown if there is a connectivity
     *                          problem or a timeout.
     */
    public PerformanceData getPerfData() throws ServiceException {
    	if (logger.isTraceEnabled()) {
    		logger.trace("Service.getPerfData");
    	}
        WPacket wp = new WPacket();
        Request req = new Request(ServiceConstants.GET_PERF_DATA, wp);

        RPacket rp = reqProcessor.process(req);

        return (PerformanceData) rp.readSerializable();
    }

    public String toString() {
        return "PerformanceService:" + serviceId;
    }
}
