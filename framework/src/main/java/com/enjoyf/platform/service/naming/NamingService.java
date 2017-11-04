package com.enjoyf.platform.service.naming;

import java.util.List;
import java.util.Vector;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.service.service.ServiceData;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.ServiceRequest;

/**
 * An interface that describes a Naming service. In summary,
 * a naming service provides a way to register services, and for
 * clients to find out what those services are.
 */
public interface NamingService {
    /**
     * Retrieve a Vector of service types (String objects).
     */
    public Vector getServiceTypes() throws ServiceException;

    /**
     * Return a vector of serviceNames for a particular serviceType.
     *
     * @return May return null if the serviceType is not found.
     */
    public Vector getServiceNames(String serviceType) throws ServiceException;

    /**
     * Return a Vector of ServiceId objects.
     */
    public Vector getServiceIds() throws ServiceException;

    /**
     * Return a Vector of ServiceId objects that correspond to a
     * particular service type.
     */
    public Vector getServiceIds(String serviceType) throws ServiceException;

    /**
     * Retrive a list of ServiceData objects.
     */
    public List<ServiceData> getServiceData(ServiceRequest sreq) throws ServiceException;

    /**
     * Return a ServiceAddress for a particular request.
     */
    public Vector<ServiceAddress> getServiceAddress(ServiceRequest sreq) throws ServiceException, NamingException;

    /**
     * Retrieve a single service address. Note that any metric
     * is acceptable. If a metric happens to return more than one
     * ServiceAddress, the first one is returned. A convenience method.
     */
    public ServiceAddress getOneServiceAddress(ServiceRequest req) throws ServiceException, NamingException;

    /**
     * Return the ServiceAddress for this particular naming service.
     */
    public NamingServiceAddress getServiceAddress();

    /**
     * Add a listener object that will be called whenever the specific
     * set of services changes.
     *
     * @param req The ServiceRequest object to listen to changes to.
     * @param l   The listener object called when things change.
     */
    public void addListener(ServiceRequest req, Listener l);

    public void removeListener(ServiceRequest req, Listener l);

    /**
     * A utility listener class.
     */
    public interface Listener {
        public void changed(ServiceRequest req);
    }

    /**
     * Register a service with the naming service. The call should
     * "succeed" even if the naming service is not up. It will try
     * and reconnect at certain intervals.
     */
    public void registerService(Registrant r);

    /**
     * De-register a service from the naming service.
     */
    public void unregisterService(Registrant r);
}
