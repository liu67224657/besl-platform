package com.enjoyf.platform.serv.naming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.service.naming.ClientRegInfo;
import com.enjoyf.platform.service.service.ServiceId;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.thin.DieThread;

/**
 * A class to store ClientRegInfo objects.
 */
class ServiceContainer {
    
    private static final Logger logger = LoggerFactory.getLogger(ServiceContainer.class);
    
    private static final int EXPIRE_INTERVAL = 30 * 1000;
    private static final int EXPIRE_TIMEOUT = 30 * 1000;

    /**
     * A container of service types.
     * The key = a String (serviceType)
     * The value is a HashMap of service names.
     */
    private HashMap<String, Map<String, String>> typeServicesMap = new HashMap<String, Map<String, String>>();

    /**
     * A container mapping a ServiceId -> ClientRegInfo.
     */
    private HashMap<ServiceId, ClientRegInfo> regInfosMap = new HashMap<ServiceId, ClientRegInfo>();

    /**
     * Used to store ServiceId objects which are rebalancing. Object
     * will be stored in here for some amount of time, and then
     * expired. The value object is a Long (the timestamp).
     */
    private HashMap<ServiceId, Long> rebalancersMap = new HashMap<ServiceId, Long>();
    private ExpireThread expireThread;

    ServiceContainer() {
        expireThread = new ExpireThread(EXPIRE_INTERVAL);
        expireThread.start();
    }

    synchronized void add(ClientRegInfo regInfo) {
        ServiceId serviceId = regInfo.getServiceId();
        //--
        // Be sure to remove the object from the rebalance Collection.
        //--
        rebalancersMap.remove(serviceId);

        Map<String, String> servicesMap = typeServicesMap.get(serviceId.getServiceType());

        if (servicesMap == null) {
            servicesMap = new HashMap<String, String>();
            typeServicesMap.put(serviceId.getServiceType(), servicesMap);
        }

        servicesMap.put(serviceId.getServiceName(), serviceId.getServiceName());
        regInfosMap.put(serviceId, regInfo);
    }


    synchronized void remove(ServiceId serviceId, boolean forRebalance) {
        if (forRebalance) {
            //--
            // We don't actually do anything, since we want to keep
            // this around for users of this object. So we just
            // store him off to the side for now and expire him later.
            //--
            ClientRegInfo regInfo = regInfosMap.get(serviceId);

            rebalancersMap.put(regInfo.getServiceId(), new Long(System.currentTimeMillis()));
            return;
        }

        Map<String, String> servicesMap = typeServicesMap.get(serviceId.getServiceType());
        if (servicesMap == null) {
            return;
        }

        servicesMap.remove(serviceId.getServiceName());
        if (servicesMap.size() == 0) {
            typeServicesMap.remove(serviceId.getServiceType());
        }

        regInfosMap.remove(serviceId);
        return;
    }


    synchronized Vector<String> getServiceNames(String serviceType) {
        Vector<String> out = new Vector<String>();
        Map<String, String> servicesMap = typeServicesMap.get(serviceType);
        if (servicesMap == null) {
            return out;
        }

        Iterator<String> itr = servicesMap.keySet().iterator();
        while (itr.hasNext()) {
            out.addElement((String) itr.next());
        }

        return out;
    }


    synchronized Vector<String> getServiceTypes() {
        Vector<String> out = new Vector<String>();
        Iterator<String> itr = typeServicesMap.keySet().iterator();
        while (itr.hasNext()) {
            out.addElement(itr.next());
        }

        return out;
    }


    synchronized Vector<ServiceId> getServiceIds() {
        Vector<ServiceId> out = new Vector<ServiceId>();

        Iterator<Map.Entry<String, Map<String, String>>> itr = typeServicesMap.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, Map<String, String>> entry = itr.next();
            String serviceType = entry.getKey();
            Map<String, String> servicesMap = entry.getValue();

            Iterator<String> itr1 = servicesMap.values().iterator();
            while (itr1.hasNext()) {
                out.add(new ServiceId(serviceType, itr1.next()));
            }
        }
        return out;
    }


    synchronized ServiceAddress getServiceAddress(ServiceId id) {
        ClientRegInfo regInfo = regInfosMap.get(id);
        if (regInfo == null) {
            return null;
        }

        return regInfo.getServiceAddress();
    }

    synchronized List<ServiceId> getServicesCopy() {
        ArrayList<ServiceId> l = new ArrayList<ServiceId>(regInfosMap.keySet());
        return l;
    }

    int size() {
        Iterator<Map<String, String>> itr = typeServicesMap.values().iterator();
        int count = 0;
        while (itr.hasNext()) {
            Map<String, String> map = itr.next();
            count += map.size();
        }
        return count;
    }

    /**
     * This routine is invoked to expire any rebalancers. In a snappy
     * system, we shouldn't get a lot of expirations.
     */
    private synchronized void expireRebalancers() {
        long curTime = System.currentTimeMillis();
        Iterator<Entry<ServiceId, Long>> itr = rebalancersMap.entrySet().iterator();
        int count = 0;
        while (itr.hasNext()) {
            Entry<ServiceId, Long> entry = itr.next();
            long t = ((Long) entry.getValue()).longValue();
            if (curTime - t > EXPIRE_TIMEOUT) {
                ServiceId id = (ServiceId) entry.getKey();
                logger.debug("ServiceContainer.expireRebalancers: "
                        + " Expiring: " + id);

                remove(id, false);
                count++;
            }
        }
        logger.debug("ServiceContainer.expireRebalancers: "
                + "Expired " + count + " entries.");
    }

    private class ExpireThread extends DieThread {
        private int expireCheckInterval = 30 * 1000;

        public ExpireThread(int checkInterval) {
            expireCheckInterval = checkInterval;
            setName("ServiceContainer.ExpireThread:" + getName());
            logger.debug("ServiceContainer.ExpireThread starting up: "
                    + "checkInterval = " + expireCheckInterval + " msecs.");
        }

        public void run() {
            while (!shouldDie()) {
                Utility.sleepExc(expireCheckInterval);
                if (shouldDie()) {
                    break;
                }

                expireRebalancers();
            }
        }
    }
}
