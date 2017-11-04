package com.enjoyf.platform.serv.naming;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.service.naming.ClientRegInfo;
import com.enjoyf.platform.service.service.ServiceId;
import com.enjoyf.platform.service.service.ServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

/**
 * A class to store RegistrantServerSide objects.
 */
class RegistrantContainer {

    private static final Logger logger = LoggerFactory.getLogger(RegistrantContainer.class);

    private Random rand = new Random(System.currentTimeMillis());

    /**
     * A map of ServiceId -> RegistrantServerSide.
     */
    private HashMap<ServiceId, RegistrantServerSide> regsOfServerSideMapByKey = new HashMap<ServiceId, RegistrantServerSide>();

    /**
     * A map of ConnThreadBase -> RegistrantServerSideLocal.
     */
    private HashMap<ConnThreadBase, RegistrantServerSideLocal> localRegsOfServerSideMapByConn = new HashMap<ConnThreadBase, RegistrantServerSideLocal>();

    private ServiceContainer clientRegInfoContainer = new ServiceContainer();

    /**
     * A Map of RemoteNamingServer.getId() -> Map of RegistrantServerSideRemote
     * objects.
     */
    private HashMap<Integer, Map<ServiceId, RegistrantServerSideRemote>> remoteRegsOfServerSideMap = new HashMap<Integer, Map<ServiceId, RegistrantServerSideRemote>>();

    private LinkedList<Listener> listenersList = new LinkedList<Listener>();

    RegistrantContainer() {
    }

    void addListener(Listener l) {
        listenersList.addLast(l);
    }

    /**
     * Return a copy of the local registrants.
     */
    synchronized Collection<RegistrantServerSideLocal> getLocalServicesCopy() {
        return new ArrayList<RegistrantServerSideLocal>(localRegsOfServerSideMapByConn.values());
    }

    /**
     * Return an iterator over local registrants.
     */
    Iterator<RegistrantServerSideLocal> getLocalServices() {
        return localRegsOfServerSideMapByConn.values().iterator();
    }

    /**
     * Returns a collection of ServiceId objects currently registered.
     */
    synchronized List<ServiceId> getServicesCopy() {
        return clientRegInfoContainer.getServicesCopy();
    }

    /**
     * Returns false if the service is already registered.
     */

    synchronized boolean add(RegistrantServerSide r) {
        ServiceId key = r.getServerRegInfo().getClientRegInfo().getServiceId();
        RegistrantServerSide oldRegistrantServerSide = regsOfServerSideMapByKey.get(key);
        if (oldRegistrantServerSide != null) {
            return false;
        }

        regsOfServerSideMapByKey.put(key, r);
        if (r instanceof RegistrantServerSideLocal) {
            RegistrantServerSideLocal lr = (RegistrantServerSideLocal) r;
            localRegsOfServerSideMapByConn.put(lr.getConn(), lr);
        } else {
            RegistrantServerSideRemote rr = (RegistrantServerSideRemote) r;
            RemoteNamingServer rns = rr.getRemoteNamingServer();
            Integer ikey = new Integer(rns.getId());

            Map<ServiceId, RegistrantServerSideRemote> map = remoteRegsOfServerSideMap.get(ikey);
            if (map == null) {
                map = new HashMap<ServiceId, RegistrantServerSideRemote>();
                remoteRegsOfServerSideMap.put(ikey, map);
            }
            map.put(key, rr);
        }

        ClientRegInfo clientRegInfo = r.getServerRegInfo().getClientRegInfo();
        clientRegInfoContainer.add(clientRegInfo);

        for (Listener l : listenersList) {
            l.add(clientRegInfo);
        }

        return true;
    }


    synchronized void remove(RegistrantServerSide r, boolean forRebalance) {
        ClientRegInfo clientRegInfo = r.getServerRegInfo().getClientRegInfo();
        ServiceId key = clientRegInfo.getServiceId();

        regsOfServerSideMapByKey.remove(key);

        if (r instanceof RegistrantServerSideLocal) {
            RegistrantServerSideLocal lr = (RegistrantServerSideLocal) r;
            localRegsOfServerSideMapByConn.remove(lr.getConn());
        } else {
            RegistrantServerSideRemote rr = (RegistrantServerSideRemote) r;
            RemoteNamingServer rns = rr.getRemoteNamingServer();
            Integer ikey = new Integer(rns.getId());
            Map<ServiceId, RegistrantServerSideRemote> map = remoteRegsOfServerSideMap.get(ikey);
            if (map == null) {
                logger.error("RegistrantContainer.remove: "
                        + "Trying to remove: " + r + " from remote regs "
                        + " but not found! ");
            } else {
                map.remove(key);
                if (map.size() == 0) {
                    remoteRegsOfServerSideMap.remove(ikey);
                }
            }
        }
        clientRegInfoContainer.remove(key, forRebalance);

        for (Listener l : listenersList) {
            l.remove(clientRegInfo);
        }
    }

    Iterator<RegistrantServerSideRemote> getRemoteRegs(RemoteNamingServer rns) {
        Integer ikey = new Integer(rns.getId());
        Map<ServiceId, RegistrantServerSideRemote> map = remoteRegsOfServerSideMap.get(ikey);
        if (map == null) {
            return (new LinkedList<RegistrantServerSideRemote>()).iterator();
        }

        return map.values().iterator();
    }

    synchronized Collection<RegistrantServerSideRemote> getRemoteRegsCopy(RemoteNamingServer rns) {
        Integer ikey = new Integer(rns.getId());
        Map<ServiceId, RegistrantServerSideRemote> map = remoteRegsOfServerSideMap.get(ikey);
        if (map == null) {
            return new ArrayList<RegistrantServerSideRemote>();
        }

        return new ArrayList<RegistrantServerSideRemote>(map.values());
    }


    synchronized ServiceAddress getServiceAddress(ServiceId id) {
        return clientRegInfoContainer.getServiceAddress(id);
    }


    synchronized Vector getServiceNames(String serviceType) {
        return clientRegInfoContainer.getServiceNames(serviceType);
    }


    synchronized Vector getServiceTypes() {
        return clientRegInfoContainer.getServiceTypes();
    }


    synchronized Vector getServiceIds() {
        return clientRegInfoContainer.getServiceIds();
    }


    synchronized RegistrantServerSide get(ServiceId id) {
        return regsOfServerSideMapByKey.get(id);
    }


    synchronized RegistrantServerSideLocal getByConn(ConnThreadBase conn) {
        return localRegsOfServerSideMapByConn.get(conn);
    }


    synchronized int size() {
        return regsOfServerSideMapByKey.size();
    }


    synchronized int localSize() {
        return localRegsOfServerSideMapByConn.size();
    }


    synchronized int remoteSize() {
        int count = 0;

        for (Map<ServiceId, RegistrantServerSideRemote> map : remoteRegsOfServerSideMap.values()) {
            count += map.size();
        }

        return count;
    }


    synchronized String containerInfo() {
        StringBuffer sb = new StringBuffer();

        sb.append("TotalServices=" + size() + ":");
        sb.append("LocalServices=" + localSize() + ":");
        sb.append("RemoteServices=" + remoteSize());

        return new String(sb);
    }


    synchronized LoadInfo getLoadInfo() {
        int services = localRegsOfServerSideMapByConn.size();
        int regClients = regsOfServerSideMapByKey.size() - services;
        return new LoadInfo(services, regClients);
    }


    synchronized RegistrantServerSideLocal getRandom() {
        //--
        // Ok, so it's not very random, we just return the first one
        // we find.
        //--
        Iterator<RegistrantServerSideLocal> itr = localRegsOfServerSideMapByConn.values().iterator();
        RegistrantServerSideLocal r = null;
        while (itr.hasNext()) {
            r = itr.next();

            if (r.isWell()) {
                break;
            }
        }

        return r;
    }

    /**
     * Retrieve a List of RegistrantServerSide objects given a ServiceRequest.
     */

    synchronized List<RegistrantServerSide> getList(ServiceRequest sreq) {
        ArrayList<RegistrantServerSide> list = new ArrayList<RegistrantServerSide>();

        String serviceType = sreq.getServiceType();
        String serviceName = "";

        if (sreq.isNameMetric()) {
            p_addRegistrant(serviceType, sreq.getServiceName(), list);
        } else if (sreq.isAllMetric()) {
            p_getAll(serviceType, list);
        } else if (sreq.isDefaultMetric()) {
            Vector v = getServiceNames(sreq.getServiceType());
            int size = v.size();
            if (size == 0) {
                return list;
            }

            //--
            // Choose a service at random.
            //--
            int pick = Math.abs(rand.nextInt()) % size;
            serviceName = (String) v.elementAt(pick);
            p_addRegistrant(serviceType, serviceName, list);
        }

        return list;
    }

    /**
     * Return a List of all current registrants.
     */
    synchronized List<RegistrantServerSide> getAllRegistrants() {
        return new ArrayList<RegistrantServerSide>(regsOfServerSideMapByKey.values());
    }

    /**
     * Utility routine to retrieve all RegistrantServerSide objects that satisfy
     * a serviceType.
     */
    private synchronized void p_getAll(String serviceType, List<RegistrantServerSide> list) {
        Vector tmp = getServiceNames(serviceType);
        if (tmp.size() == 0) {
            return;
        }

        Enumeration itr = tmp.elements();
        while (itr.hasMoreElements()) {
            String serviceName = (String) itr.nextElement();
            p_addRegistrant(serviceType, serviceName, list);
        }
    }

    /**
     * Utility method to add a RegistrantServerSide to a list we are building if
     * we find it.
     */
    private void p_addRegistrant(String serviceType, String serviceName, List list) {
        ServiceId serviceId = new ServiceId(serviceType, serviceName);

        RegistrantServerSide r = regsOfServerSideMapByKey.get(serviceId);
        if (r != null) {
            list.add(r);
        }

        return;
    }

    /**
     * This interface is to be implemented by listeners for when
     * the contents of this container changes.
     */
    static public interface Listener {
        /**
         * A registration was added.
         */
        public void add(ClientRegInfo regInfo);

        /**
         * A registration was removed.
         */
        public void remove(ClientRegInfo regInfo);
    }
}
