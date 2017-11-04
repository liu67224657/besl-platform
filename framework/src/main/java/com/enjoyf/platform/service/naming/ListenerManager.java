package com.enjoyf.platform.service.naming;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.service.naming.NamingService.Listener;
import com.enjoyf.platform.service.service.ServiceRequest;

/**
 * Utility class to manage change event listeners.
 */
public class ListenerManager {
    
    private static final Logger logger = LoggerFactory.getLogger(ListenerManager.class);
    
    private Map<ServiceRequest, LinkedList<NamingService.Listener>> map = new HashMap<ServiceRequest, LinkedList<NamingService.Listener>>();
    private boolean changed = false;

    synchronized void add(ServiceRequest req, NamingService.Listener l) {
        LinkedList<Listener> list = map.get(req);
        if (list == null) {
            list = new LinkedList<Listener>();

            map.put(req, list);
        }

        list.addLast(l);

        logger.debug("ListenerManager.add: size = " + list.size() + " for " + req);

        changed = true;
    }

    synchronized void remove(ServiceRequest req, NamingService.Listener l) {
        LinkedList<Listener> list = map.get(req);
        if (list == null) {
            return;
        }

        list.remove(l);

        changed = true;
    }

    /**
     * Will return an array of the current ServiceRequest objects
     * we want to register for events, but only if they've changed
     * since the last time we made this call.
     */
    synchronized ServiceRequest[] getChangedRequests() {
        if (!changed) {
            return null;
        }

        changed = false;

        return getServiceRequests();
    }

    /**
     * Will return an array of the current ServiceRequest objects
     * we want to register for events.
     */
    synchronized ServiceRequest[] getServiceRequests() {
        return map.keySet().toArray(new ServiceRequest[map.size()]);
    }

    /**
     * This routine returns all the registered listeners for changes to
     * the passed in array of ServiceRequest objects.
     */
    synchronized NamingService.Listener[] getListeners(ServiceRequest req) {
        LinkedList<NamingService.Listener> l = map.get(req);
        if (l == null) {
            return null;
        }

        return l.toArray(new NamingService.Listener[l.size()]);
    }
}
