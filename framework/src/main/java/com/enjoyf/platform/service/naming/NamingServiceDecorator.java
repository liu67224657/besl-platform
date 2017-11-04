/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.naming;

import java.util.List;
import java.util.Vector;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.service.service.ServiceData;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.ServiceRequest;

/**
 * A Decorator for the NamingService. This default decorator just
 * forwards the call to the NamingService.
 */
public class NamingServiceDecorator implements NamingService {
    protected NamingService namingService;

    protected NamingServiceDecorator(NamingService namingService) {
        this.namingService = namingService;
    }

    /**
     * Retrieve a Vector of service types (String objects).
     */
    public Vector getServiceTypes() throws ServiceException {
        return namingService.getServiceTypes();
    }

    /**
     * Return a vector of serviceNames for a particular serviceType.
     *
     * @return May return null if the serviceType is not found.
     */
    public Vector getServiceNames(String serviceType) throws ServiceException {
        return namingService.getServiceNames(serviceType);
    }

    /**
     * Return a Vector of ServiceId objects.
     */
    public Vector getServiceIds() throws ServiceException {
        return namingService.getServiceIds();
    }

    /**
     * Return a list of ServiceData objects.
     */
    public List<ServiceData> getServiceData(ServiceRequest sreq) throws ServiceException {
        return namingService.getServiceData(sreq);
    }

    /**
     * Return a Vector of ServiceId objects that correspond to a
     * particular service type.
     */
    public Vector getServiceIds(String serviceType) throws ServiceException {
        return namingService.getServiceIds(serviceType);
    }

    /**
     * Return a ServiceAddress for a particular request.
     */
    public Vector getServiceAddress(ServiceRequest sreq) throws ServiceException {
        return namingService.getServiceAddress(sreq);
    }

    /**
     * Retrieve a single service address. Note that any metric
     * is acceptable. If a metric happens to return more than one
     * ServiceAddress, the first one is returned. A convenience method.
     */
    public ServiceAddress getOneServiceAddress(ServiceRequest req) throws ServiceException {
        return namingService.getOneServiceAddress(req);
    }

    /**
     * Return the NamingServiceAddress for this particular naming service.
     */
    public NamingServiceAddress getServiceAddress() {
        return namingService.getServiceAddress();
    }

    /**
     * Add a listener object that will be called whenever the set of
     * services changes. This feature may or may not be provided
     * by underlying implementations.
     */
    public void addListener(ServiceRequest req, NamingService.Listener l) {
        namingService.addListener(req, l);
    }

    public void removeListener(ServiceRequest req, NamingService.Listener l) {
        namingService.removeListener(req, l);
    }

    /**
     * Register a service with the naming service. The call should
     * "succeed" even if the naming service is not up. It will try
     * and reconnect at certain intervals.
     */
    public void registerService(Registrant r) {
        namingService.registerService(r);
    }

    /**
     * De-register a service from the naming service.
     */
    public void unregisterService(Registrant r) {
        namingService.unregisterService(r);
    }
}
