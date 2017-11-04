/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.naming;

import java.util.Enumeration;
import java.util.Hashtable;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.ServiceRequest;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * Use this class to cache responses to ServiceRequests. The
 * Requests must use the NAME metric.
 */
public class ServiceAddressCache {
    private NamingService namingService;

    private int refreshInterval;
    private long lastRefreshTime = 0;
    private Hashtable<Key, ServiceAddress> cache = new Hashtable<Key, ServiceAddress>(250);

    /**
     * Use a default reresh interval.
     */
    public ServiceAddressCache(NamingService namingService) {
        this(namingService, 10 * 60 * 1000);
    }

    /**
     * @param namingService   The NamingService to use.
     * @param refreshInterval The cache refresh interval in msecs.
     */
    public ServiceAddressCache(NamingService namingService, int refreshInterval) {
        this.namingService = namingService;
        this.refreshInterval = refreshInterval;
    }

    public synchronized ServiceAddress getServiceAddress(ServiceRequest req) throws ServiceException {
        Key key = new Key(req);
        try {
            p_checkRefresh();
        } catch (ServiceException e) {
            GAlerter.lab("ServiceAddressCache: Could not refresh: " + e);
        }

        ServiceAddress saddr = cache.get(key);
        if (saddr != null) {
            return saddr;
        }

        try {
            saddr = namingService.getOneServiceAddress(req);
        } catch (ServiceException e) {
            GAlerter.lab("ServiceAddressCache.getRoomServerAddress: Could not get service addr: " + req + " exc " + e);

            throw e;
        }

        cache.put(key, saddr);

        return saddr;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    private void p_checkRefresh() throws ServiceException {
        long curTime = System.currentTimeMillis();

        if (curTime - lastRefreshTime < refreshInterval) {
            return;
        }

        //--
        // Refresh our cache of ServiceAddress objects.
        //--
        Enumeration<Key> itr = cache.keys();
        while (itr.hasMoreElements()) {
            Key key = itr.nextElement();
            ServiceAddress saddr = null;
            try {
                saddr = namingService.getOneServiceAddress(key.getReq());
            } catch (NamingException e) {
                //--
                // If it was a request denied or svc not found error,
                // then we clear the cache of this entry, since it's
                // clearly no good. We then continue looking at other
                // keys. Any other kind of error is probably a monitor
                // down error, so we abort the refresh and leave the
                // cache alone.
                //--
                if (e.equals(NamingException.REQUEST_DENIED) || e.equals(NamingException.SVC_NOT_REGISTERED)) {
                    cache.remove(key);

                    continue;
                }

                throw e;
            }

            cache.put(key, saddr);
        }

        lastRefreshTime = System.currentTimeMillis();
    }

    /**
     * A helper class to store elements of this type in our cache.
     */
    static class Key {
        private ServiceRequest serviceReq;

        /**
         * Ctor an object from a ServiceRequest.
         */
        Key(ServiceRequest req) {
            if (!req.isNameMetric()) {
                throw new RuntimeException("ServiceAddressCache: must use a ServiceRequest object with a NAME metric");
            }

            serviceReq = req;
        }

        ServiceRequest getReq() {
            return serviceReq;
        }

        public int hashCode() {
            return serviceReq.getServiceType().hashCode() + serviceReq.getServiceName().hashCode();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Key)) {
                return false;
            }

            Key key = (Key) obj;

            return (serviceReq.getServiceType().equals(key.getReq().getServiceType()) && serviceReq.getServiceName().equals(key.getReq().getServiceName()));
        }
    }
}
