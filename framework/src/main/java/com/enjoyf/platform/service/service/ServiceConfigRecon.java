/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import com.enjoyf.platform.service.naming.NamingService;

/**
 * This class adds a client id to the cfg object.
 */
public class ServiceConfigRecon extends ServiceConfigNaming {
    private String clientName = "";

    /**
     * This ctor is used to explicitly specify the NamingService
     * object to be used.
     *
     * @param namingService A built and configured NamingService object.
     * @param req           The service request to use.
     */
    public ServiceConfigRecon(NamingService namingService, ServiceRequest req) {
        super(namingService, req);
    }

    /**
     * Set the name for this client. May or may not be important
     * depending on the service.
     */
    public void setClientName(String name) {
        clientName = name;
    }

    public String getClientName() {
        return clientName;
    }

    protected ReqProcessor allocReqProcessor(ConnChooser chooser, int timeout) {
        return new ReqProcessor(chooser, new ProcessStrategyOnce(), timeout);
    }
}
