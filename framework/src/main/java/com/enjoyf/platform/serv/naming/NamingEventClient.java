/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.naming;

import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.service.service.ServiceId;
import com.enjoyf.platform.service.service.ServiceRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * A class that represents a NamingEventClient object. This would be a client
 * of the server that wanted to recieve change events.
 */
class NamingEventClient {
    //
    private ConnThreadBase connThreadBase;

    /**
     * Maps a serviceType to a Set of ServiceRequest objects that match
     * that type.
     */
    private HashMap<String, Set<ServiceRequest>> serviceRequestsMap = new HashMap<String, Set<ServiceRequest>>();

    /**
     * Stores ServiceRequest objects which are the events
     * we want to send out.
     */
    private Set<ServiceRequest> serviceRequestEventSet = new HashSet<ServiceRequest>();

    //
    NamingEventClient(ConnThreadBase conn) {
        connThreadBase = conn;
    }

    public int hashCode() {
        return connThreadBase.hashCode();
    }

    public boolean equals(Object obj) {
        return connThreadBase.equals(obj);
    }

    boolean isAlive() {
        return connThreadBase.isAlive();
    }

    ConnThreadBase getConn() {
        return connThreadBase;
    }

    /**
     * Add an event to our internal container of events to send out.
     */
    synchronized boolean addEvent(ServiceId serviceId) {
        if (serviceRequestsMap.size() == 0) {
            return false;
        }

        boolean added = false;
        Set<ServiceRequest> set = serviceRequestsMap.get(serviceId.getServiceType());
        if (set != null) {
            added = true;
            serviceRequestEventSet.addAll(set);
        }

        return added;
    }


    synchronized ServiceRequest[] getAndClearEvents() {
        ServiceRequest[] events = new ServiceRequest[serviceRequestEventSet.size()];
        if (events.length == 0) {
            return events;
        }

        serviceRequestEventSet.toArray(events);
        serviceRequestEventSet.clear();

        return events;
    }


    synchronized void setServiceRequests(ServiceRequest[] serviceRequests) {
        //--
        // A client will always register for events passing in the full
        // set of events it is interested in, so we can clear out any
        // old ones.
        //--
        serviceRequestsMap.clear();

        //--
        // serviceRequestsMap will store a mapping between a serviceType and all the
        // ServiceRequests that match that type.
        //--
        for (ServiceRequest serviceRequest : serviceRequests) {
            Set<ServiceRequest> set = serviceRequestsMap.get(serviceRequest.getServiceType());
            if (set == null) {
                set = new HashSet<ServiceRequest>();

                serviceRequestsMap.put(serviceRequest.getServiceType(), set);
            }

            set.add(serviceRequest);
        }
    }

    public synchronized String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(connThreadBase);
        sb.append(": reqs = " + serviceRequestsMap);

        return new String(sb);
    }
}
