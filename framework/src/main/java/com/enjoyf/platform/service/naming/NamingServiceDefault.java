package com.enjoyf.platform.service.naming;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.ServiceId;
import com.enjoyf.platform.service.service.ServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;
import java.util.Vector;

/**
 * An abstract class to add utility/convenience methods that are shared
 * by derived classes.
 */
public abstract class NamingServiceDefault implements NamingService {
    //
    private static final Logger logger = LoggerFactory.getLogger(NamingServiceDefault.class);

    //
    protected ListenerManager listenerManager = new ListenerManager();

    /**
     * Retrieve service id objects for a particular service type.
     */
    public Vector<ServiceId> getServiceIds(String serviceType) throws ServiceException {
        Vector<ServiceId> out = new Vector<ServiceId>();
        Vector v = getServiceNames(serviceType);

        Enumeration itr = v.elements();
        while (itr.hasMoreElements()) {
            String sname = (String) itr.nextElement();
            out.addElement(new ServiceId(serviceType, sname));
        }

        return out;
    }

    public void registerService(Registrant r) {
        r.setNamingServiceAddress(getServiceAddress());
        r.start();
    }

    public void unregisterService(Registrant r) {
        r.stop();
    }

    public void addListener(ServiceRequest req, NamingService.Listener l) {
        if (logger.isDebugEnabled()) {
            logger.debug(hashCode() + ":NamingServiceDefault.addListener: Adding for: " + req);
        }
        listenerManager.add(req, l);
    }

    public void removeListener(ServiceRequest req, NamingService.Listener l) {
        if (logger.isDebugEnabled()) {
            logger.debug("NamingServiceDefault.removeListener: Removing for: " + req);
        }
        listenerManager.remove(req, l);
    }

    public ServiceAddress getOneServiceAddress(ServiceRequest req) throws ServiceException {
        Vector<ServiceAddress> v = getServiceAddress(req);
        if (v.size() == 0) {
            throw new NamingException(NamingException.SVC_NOT_REGISTERED, "req = " + req.toString());
        }

        return v.elementAt(0);
    }
}
