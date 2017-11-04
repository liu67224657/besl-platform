/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.io.ServiceAddressKey;
import com.enjoyf.platform.service.service.load.LoadMonitorDefFactory;
import com.enjoyf.platform.util.Utility;

/**
 * A class to help manage the ConnInfo objects.
 */
public class ConnCache {
	
	private static final Logger logger = LoggerFactory.getLogger(ConnCache.class);
	
    /**
     * Store the ConnInfo objects in use.
     * A map of ServiceAddressKey -> ConnInfo objects.
     */
    private HashMap<ServiceAddressKey, ConnInfo> connInfosMap = new HashMap<ServiceAddressKey, ConnInfo>();

    /**
     * Store the current known services. This is a map of
     * ServiceAddressKey -> ServiceData.
     */
    private HashMap<ServiceAddressKey, ServiceData> serviceDatasMap = new HashMap<ServiceAddressKey, ServiceData>();

    /**
     * A load monitor object. This is optional and is used to
     * monitor the load of a conn.
     */
    private LoadMonitorDefFactory loadMonitorFactory;

    private int timeoutTime;

    ///////////////////////////////////////////////////////////
    ConnCache(int timeout) {
        timeoutTime = timeout;
    }

    void setLoadMonitorFactory(LoadMonitorDefFactory factory) {
        loadMonitorFactory = factory;
    }

    /**
     * Use this routine when you know exactly which ServiceAddress is
     * wanted.
     */
    public synchronized ConnInfo getImmediate(ServiceAddressKey key, ServiceData sdata) {
        ConnInfo connInfo = connInfosMap.get(key);

        if (connInfo == null || !connInfo.getServiceConn().isAvailable()) {
            connInfo = createConnInfo(sdata);

            connInfosMap.put(key, connInfo);
        } else {
            //--
            // Update the ServiceLoad if we have a valid object.
            //--
            connInfo.getServiceData().setServiceLoad(sdata.getServiceLoad());
        }

        return connInfo;
    }

    /**
     * Remove a bad conn from our cache.
     */
    public synchronized void remove(ServiceConn sconn) {
        ServiceAddressKey key = null;
        try {
            key = new ServiceAddressKey(sconn.getServiceAddress());
        }
        catch (UnknownHostException uhe) {
        	logger.error("Could not remove conn: " + sconn.getServiceAddress() + " due to UnknownHostException ");
            return;
        }

        //--
        // Look for the conn by its key because we should only ever have
        // one conn per ServiceAddress. When removing however, we need
        // to make sure the ServiceConn object passed in is really
        // the same one that we have in this cache. Many threads may
        // have encountered the bad conn and all will try to remove it.
        //--
        ConnInfo connInfo = connInfosMap.get(key);
        if (connInfo == null) {
            return;
        }

        if (Utility.equals(connInfo.getServiceConn(), sconn)) {
        	logger.error("ConnCache.remove: Removing a bad conn: " + sconn);

            connInfosMap.remove(key);
        }

        log("remove.exit");
    }

    /**
     * This can be called to remove any bad conns from our container.
     */
    public synchronized void removeBadConns() {
        //
        for (Iterator<Map.Entry<ServiceAddressKey, ConnInfo>> itr = connInfosMap.entrySet().iterator(); itr.hasNext();)
        {
            Map.Entry<ServiceAddressKey, ConnInfo> entry = itr.next();
            ConnInfo connInfo = entry.getValue();

            if (connInfo.shouldRemove()) {
            	logger.warn("ConnCache.removeBadConns: removing: " + connInfo);

                itr.remove();
            }
        }

        log("removeBadConns.exit");
    }

    /**
     * This is called when the naming server informs us that it
     * has new services available.
     *
     * @param serviceDataList A List of ServiceData objects.
     */
    public synchronized void update(List<ServiceData> serviceDataList) {
    	if (logger.isTraceEnabled()) {
    		logger.trace("ConnCache.update: " + serviceDataList);
    	}

        Iterator<ServiceData> itr = serviceDataList.iterator();
        while (itr.hasNext()) {
            ServiceData sdata = itr.next();
            ServiceAddressKey key = null;

            try {
                key = new ServiceAddressKey(sdata.getServiceAddress());
            } catch (UnknownHostException uhe) {
            	logger.error("Could not update cache with: " + sdata.getServiceAddress() + " due to UnknownHostException");
                continue;
            }

            //
            serviceDatasMap.put(key, sdata);

            //
            ConnInfo connInfo = connInfosMap.get(key);
            if (connInfo != null) {
                //--
                // If here, we already have a connection that we
                // think is valid for this address, so just update
                // it's load info.
                //--
                connInfo.getServiceData().setServiceLoad(sdata.getServiceLoad());
            }
        }

        log("update.exit");
    }

    private ConnInfo createConnInfo(ServiceData sdata) {
        ServiceConn sconn = new ServiceConn(sdata.getServiceAddress(), sdata.getServiceId(), timeoutTime);

        if (loadMonitorFactory != null) {
            sconn.setLoadMonitor(loadMonitorFactory.create());
        }

        ConnInfo connInfo = new ConnInfo(sdata, sconn);
        connInfo.getServiceData().setServiceLoad(sdata.getServiceLoad());

        if (logger.isTraceEnabled()) {
        	logger.trace("ConnCache: Created new conn: " + connInfo);
        }

        log("createConnInfo.exit");

        return connInfo;
    }

    public synchronized Collection<ServiceData> getCurrentServiceData() {
        return serviceDatasMap.values();
    }

    public synchronized ConnInfo[] getCurrentConns() {
        //--
        // Update the container. Remove old, bad conns and create new
        // ones if we have them.
        //--
        update();

        ConnInfo[] array = new ConnInfo[connInfosMap.size()];
        connInfosMap.values().toArray(array);

        return array;
    }

    //only used for partion service.
    public synchronized Map<Integer, ConnInfo> getCurrentPartitionConns() {
        //--
        // Update the container. Remove old, bad conns and create new
        // ones if we have them.
        //--
        update();

        Map<Integer, ConnInfo> partitionConns = new HashMap<Integer, ConnInfo>();

        for (ConnInfo info : connInfosMap.values()) {
            partitionConns.put(info.getPartition(), info);
        }

        return partitionConns;
    }

    /**
     * Returns a list of ServiceConn objects that are "good" meaning
     * they can be connected to or are already connected.
     */
    public synchronized List<ServiceConn> getGoodConns() {
        //--
        // Need to update with any recently arrived ServiceData.
        //--
        update();

        ArrayList<ServiceConn> list = new ArrayList<ServiceConn>();
        for (Iterator<ConnInfo> itr = connInfosMap.values().iterator(); itr.hasNext();) {
            ConnInfo connInfo = itr.next();

            if (connInfo == null || connInfo.shouldRemove()) {
                continue;
            }

            list.add(connInfo.getServiceConn());
        }

        if (logger.isTraceEnabled()) {
        	logger.trace("ConnCache.getGoodConns: returning: " + list);
        }
        return list;
    }

    /**
     * Close all the open conns.
     */
    public synchronized void close() {
        for (Iterator<ConnInfo> itr = connInfosMap.values().iterator(); itr.hasNext();) {
            ServiceConn sconn = (itr.next()).getServiceConn();

            sconn.close();
        }
    }

    /**
     * Ask if it's empty of any conns or potential
     * conns.
     */
    public synchronized boolean isEmpty() {
        return connInfosMap.size() == 0 && serviceDatasMap.size() == 0;
    }

    /**
     * This routine is used to to update our ConnInfo container
     * given any ServiceData that we have recently received.
     */
    private void update() {
        log("update.enter");

        if (serviceDatasMap.size() == 0) {
            return;
        }

        removeBadConns();

        for (Iterator<Map.Entry<ServiceAddressKey, ServiceData>> itr = serviceDatasMap.entrySet().iterator(); itr.hasNext();)
        {
            Map.Entry<ServiceAddressKey, ServiceData> entry = itr.next();

            ServiceAddressKey key = entry.getKey();
            ServiceData sdata = entry.getValue();

            ConnInfo connInfo = connInfosMap.get(key);

            if (connInfo == null) {
                connInfosMap.put(key, createConnInfo(sdata));
                itr.remove();
            }
        }

        log("update.exit");
    }

    private void log(String func) {
    	if (logger.isTraceEnabled()) {
    		logger.trace("ConnCache." + func + ": conns=" + connInfosMap + ":serviceDatasMap=" + serviceDatasMap);
    	}
    }
}
