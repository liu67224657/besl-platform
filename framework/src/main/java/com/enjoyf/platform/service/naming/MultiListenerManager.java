package com.enjoyf.platform.service.naming;

import com.enjoyf.platform.service.naming.NamingService.Listener;
import com.enjoyf.platform.service.service.ServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to manage change event listeners.
 */
public class MultiListenerManager implements Listener {
    //
    private static final Logger logger = LoggerFactory.getLogger(MultiListenerManager.class);

    //service type -> namingservice.listener
    private Map<String, Listener> listenerMap = new HashMap<String, Listener>();

    //service type -> boolean
    private Map<String, Boolean> changedMap = new HashMap<String, Boolean>();

    //
    synchronized void add(ServiceRequest serviceRequest, Listener l) {
        listenerMap.put(serviceRequest.getServiceType(), l);
        changedMap.put(serviceRequest.getServiceType(), true);

        logger.info("MultiListenerManager.add: size = " + listenerMap.size() + " for " + serviceRequest);
    }

    synchronized void remove(ServiceRequest serviceRequest, Listener l) {
        listenerMap.remove(serviceRequest.getServiceType());
        changedMap.put(serviceRequest.getServiceType(), true);

        logger.info("MultiListenerManager.remove: size = " + listenerMap.size() + " for " + serviceRequest);
    }

    /**
     * Will return an array of the current ServiceRequest objects
     * we want to register for events, but only if they've changed
     * since the last time we made this call.
     */
    synchronized ServiceRequest[] getChangedRequests() {
        List<ServiceRequest> requestList = new ArrayList<ServiceRequest>();

        for (Map.Entry<String, Boolean> entry : changedMap.entrySet()) {
            if (entry.getValue()) {
                requestList.add(new ServiceRequest(entry.getKey(), ServiceRequest.ALL));
            }
        }

        return requestList.toArray(new ServiceRequest[requestList.size()]);
    }

    synchronized ServiceRequest[] getServiceRequests() {
        List<ServiceRequest> requestList = new ArrayList<ServiceRequest>();

        for (Map.Entry<String, Boolean> entry : changedMap.entrySet()) {
            requestList.add(new ServiceRequest(entry.getKey(), ServiceRequest.ALL));
        }

        return requestList.toArray(new ServiceRequest[requestList.size()]);
    }

    public void setChanged(ServiceRequest serviceRequest, boolean changed) {
        changedMap.put(serviceRequest.getServiceType(), changed);
    }

    public Listener getListener(ServiceRequest serviceRequest) {
        return listenerMap.get(serviceRequest.getServiceType());
    }

    @Override
    public void changed(ServiceRequest serviceRequest) {
        //
        changedMap.put(serviceRequest.getServiceType(), true);

        //
        Listener listener = listenerMap.get(serviceRequest.getServiceType());

        if (listener != null) {
            listener.changed(serviceRequest);
        }
    }
}
