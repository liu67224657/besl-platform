/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.naming;

import java.util.Hashtable;
import java.util.Vector;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.ServiceRequest;

/**
 * This decorator will slow down requests for ServiceAddress objects
 * when either the naming service is down, or the service requested
 * is not registered.
 */
public class NamingServiceAddressDecorator extends NamingServiceDecorator {
    private Hashtable errorCache = new Hashtable();
    private int checkInterval = 5 * 1000;
    private int sleepInterval = 0 * 1000;

    /////////////////////////////////////
    static class Entry {
        ServiceException exception;
        long time = 0;

        Entry(ServiceException e) {
            exception = e;
            time = System.currentTimeMillis();
        }
    }

    /**
     * Create the decorator.
     *
     * @param namingService The naming service to use (note that it could
     *                      be another "decorated" NamingService object).
     * @param checkInterval The interval in msecs, to check to see if
     *                      things are "up". This means that if a failure is encountered,
     *                      further calls will return the error for checkInterval msecs.
     * @param sleepInterval This is how long to sleep when making a
     *                      request, in msecs, per thread. NOTE: due to the higher
     *                      level service layers, settings this to non-zero will block
     *                      all threads accessing the same ServiceRequest. So it should
     *                      be left at 0, unless you want a backup of threads.
     */
    public NamingServiceAddressDecorator(NamingService namingService, int checkInterval, int sleepInterval) {
        super(namingService);
        this.checkInterval = checkInterval;
        this.sleepInterval = sleepInterval;
    }

    public NamingServiceAddressDecorator(NamingService namingService, int checkInterval) {
        this(namingService, checkInterval, 0 * 1000);
    }

    public NamingServiceAddressDecorator(NamingService namingService) {
        this(namingService, 5 * 1000, 0 * 1000);
    }

    private synchronized void p_checkCache(ServiceRequest req) throws ServiceException {
        Entry entry = null;
        entry = (Entry) errorCache.get(req);
        if (entry == null) {
            return;
        }

        if (!p_inErrorMode(entry, req)) {
            return;
        }

        if (sleepInterval > 0) {
            try {
                wait(sleepInterval);
            }
            catch (InterruptedException e) {
            }

            if (!p_inErrorMode(entry, req)) {
                return;
            }
        }

        if (entry != null) {
            throw entry.exception;
        }
    }

    /**
     * Utility routine which returns true if we are still in
     * "error mode".
     */
    private boolean p_inErrorMode(Entry entry, ServiceRequest req) {
        long curTime = System.currentTimeMillis();
        if ((curTime - entry.time) > checkInterval) {
            errorCache.remove(req);
            return false;
        }

        return true;
    }

    private synchronized void p_gotError(ServiceRequest req, ServiceException e) {
        Entry entry = new Entry(e);
        errorCache.put(req, entry);
    }

    /**
     * Return a ServiceAddress for a particular request.
     */
    public Vector getServiceAddress(ServiceRequest sreq) throws ServiceException, NamingException {
        Vector v = new Vector();

        p_checkCache(sreq);
        try {
            v = namingService.getServiceAddress(sreq);
        } catch (ServiceException e) {
            p_gotError(sreq, e);
            throw e;
        }

        return v;
    }

    /**
     * Retrieve a single service address. Note that any metric
     * is acceptable. If a metric happens to return more than one
     * ServiceAddress, the first one is returned. A convenience method.
     */
    public ServiceAddress getOneServiceAddress(ServiceRequest req) throws ServiceException, NamingException {
        ServiceAddress saddr = null;

        p_checkCache(req);

        try {
            saddr = namingService.getOneServiceAddress(req);
        } catch (ServiceException e) {
            p_gotError(req, e);
            throw e;
        }

        return saddr;
    }
}
