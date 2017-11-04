package com.enjoyf.platform.tools.naming;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.service.naming.NamingService;
import com.enjoyf.platform.service.service.ServiceId;
import com.enjoyf.platform.service.service.ServiceInfo;
import com.enjoyf.platform.service.service.ServiceRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

class State {
    private NamingService namingService;

    //
    private HashSet services = new HashSet();
    private HashMap byType = new HashMap();
    private HashMap serviceMap = new HashMap();

    /**
     * A list of ServiceInfo objects.
     */
    private Set serviceInfos = new TreeSet(new Comparator() {
        public int compare(Object o1, Object o2) {
            ServiceId id1 = ((ServiceInfo) o1).getServiceId();
            ServiceId id2 = ((ServiceInfo) o2).getServiceId();
            return id1.compareTo(id2);
        }
    });

    State(NamingService ns) {
        namingService = ns;
    }

    /**
     * Refresh all services, perhaps we get an error in which case we
     * return it.
     */
    String refresh() {
        //--
        // Clear out the cache of service maps.
        //--
        serviceMap.clear();
        serviceInfos.clear();

        Vector v = null;
        try {
            v = namingService.getServiceIds();
        } catch (Exception e) {
            //GLogger.lh("Caught an error: " + e);
            return "error: " + e;
        }

        services.clear();
        byType.clear();

        for (Iterator itr = v.iterator(); itr.hasNext(); ) {
            ServiceId sid = (ServiceId) itr.next();
            services.add(sid);
            List list = (List) byType.get(sid.getServiceType());
            if (list == null) {
                list = new ArrayList();
                byType.put(sid.getServiceType(), list);
            }
            list.add(sid);
        }

        return null;
    }

    HashSet getAll() {
        p_checkRefresh();

        return services;
    }

    Collection getAllByType(String serviceType) {
        p_checkRefresh();

        ArrayList output = new ArrayList();
        for (Iterator itr = byType.keySet().iterator(); itr.hasNext(); ) {
            String type = (String) itr.next();
            if (type.startsWith(serviceType))
                output.addAll((Collection) byType.get(type));
        }
        return output;
    }

    /**
     * Returns a sorted set of ServiceInfo objects
     */
    Set getAllServiceInfos() {
        p_checkRefresh();

        if (serviceInfos.size() > 0)
            return serviceInfos;

        HashSet set = getAll();
        for (Iterator itr = set.iterator(); itr.hasNext(); ) {
            ServiceId id = (ServiceId) itr.next();
            ServiceRequest req = new ServiceRequest(
                    id.getServiceType(), id.getServiceName());
            ServiceAddress saddr = getServiceAddress(req);
            ServiceInfo sinfo = new ServiceInfo(id, saddr);
            serviceInfos.add(sinfo);
        }
        return serviceInfos;
    }

    private void p_checkRefresh() {
        if (services.size() == 0)
            refresh();
    }

    /**
     * Retrieves a Collection of ServiceId objects that match loosely
     * on both serviceType and serviceName.
     *
     * @param serviceType The exact serviceType that is wanted.
     * @param serviceName The inexact serviceName.
     */
    Collection getAllWithName(String serviceType, String serviceName) {
        p_checkRefresh();
        ArrayList output = new ArrayList();
        Collection c = getAllByType(serviceType);
        for (Iterator itr = c.iterator(); itr.hasNext(); ) {
            ServiceId sid = (ServiceId) itr.next();
            if (sid.getServiceName().startsWith(serviceName))
                output.add(sid);
        }
        return output;
    }

    ServiceAddress getServiceAddress(ServiceRequest req) {
        ServiceAddress saddr = null;
        saddr = (ServiceAddress) serviceMap.get(req);
        if (saddr != null)
            return saddr;

        try {
            saddr = namingService.getOneServiceAddress(req);
        } catch (Exception e) {
            //GL.lh("ERROR! Looking for: " + req);
        }
        if (saddr != null)
            serviceMap.put(req, saddr);

        return saddr;
    }
}
