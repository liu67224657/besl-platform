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

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.io.ServiceAddressKey;
import com.enjoyf.platform.service.service.load.LoadMonitorDefFactory;

/**
 * A class to abstract the choosing of a certain ServiceConn object.
 * Derived classes should determine the scheme they will use to do this.
 * Specifically, the get() method should be implemented.
 * In all cases, a ServiceFinder object should be used.
 */
public abstract class ConnChooser {
	
	private static final Logger logger = LoggerFactory.getLogger(ConnChooser.class);
	
    //the address -> addresskey.
    private HashMap<ServiceAddress, ServiceAddressKey> serviceAddressKeysMap = new HashMap<ServiceAddress, ServiceAddressKey>();

    /**
     * Maps a ServiceAddressKey -> ConnInfo object.
     */
    private ConnCache connInfoCache;

    private ServiceFinder serviceFinder = null;
    private int timeoutTime = 30 * 1000;

    private boolean closedFlag = false;
    private ConnManager connManager;
    private EventConfig eventConfig;
    private Greeter greeter;

    private ConnLoadChecker connLoadChecker = new ConnLoadCheckerDef();

    private static final int CONN_TIMEOUT_DEFAULT = 30 * 1000;

    /**
     * @param timeout The timeout for transactions in msecs.
     * @param timeout How often to refresh the cache of
     *                services in msecs.
     */
    public ConnChooser(int timeout) {
        timeoutTime = timeout;
        connInfoCache = new ConnCache(timeoutTime);
        connManager = new ConnManager(CONN_TIMEOUT_DEFAULT);
    }

    /**
     * Ctor the ServiceConn objects with default timeouts.
     */
    public ConnChooser() {
        this(30 * 1000);
    }

    public void setGreeter(Greeter greeter) {
        this.greeter = greeter;
    }

    public void setConnLoadChecker(ConnLoadChecker checker) {
        connLoadChecker = checker;
    }

    public void setLoadMonitorFactory(LoadMonitorDefFactory factory) {
        //TODO: not implemented
    }

    /**
     * Retrive a ServiceConn object according to the policy
     * used by the derived class.
     */
    protected abstract ConnPick getPick(Request req);

    /**
     * Returns a List of all the ServiceConn objects that we currently
     * know about. We will connect to them if we haven't already.
     */
    public List<ServiceConn> getAll(Request req) {
        List<ServiceConn> list = p_getAll(req);

        //--
        // Refresh from the ServiceFinder, the 2nd arg is "reallyNeedSome",
        // which means if we don't currently have any query the NS
        // directly.
        //--
        refresh(req, list.size() == 0);

        //--
        // Read the conns again.
        //--
        return p_getAll(req);
    }

    public List<ServiceConn> p_getAll(Request req) {
        List<ServiceConn> goodConns = connInfoCache.getGoodConns();

        ArrayList<ServiceConn> retConns = new ArrayList<ServiceConn>();
        for (Iterator<ServiceConn> itr = goodConns.iterator(); itr.hasNext();) {
            ServiceConn sconn = itr.next();

            if (sconn.needsConnection()) {
                boolean gotIt = false;

                try {
                    performConnection(sconn);
                    gotIt = true;
                } catch (ServiceException se) {
                    logger.warn("ConnChooser.getAll: trying to connect to: " + sconn.getServiceAddress(), se);
                }

                if (gotIt) {
                    retConns.add(sconn);
                }
            } else {
                retConns.add(sconn);
            }

            //check eventCfg status
            // 1 conn is not needed to make AND
            // 2 there is eventCfg AND
            // 3 eventListener is not set for somehow reason
            if ((!sconn.needsConnection()) && eventConfig != null && (!sconn.isEventListenerSet())) {
                sconn.setEventListener(eventConfig.getListener(), eventConfig.getThreadName());
            }
        }

        if (logger.isDebugEnabled()) {
        	logger.debug("ConnChooser.getAll: returning " + retConns.size() + " connections");
        }
        return retConns;
    }

    /**
     * Return a ServiceConn object where the Request object is
     * used to help us choose.
     *
     * @throws ServiceException Thrown if could not establish the
     *                          connection.
     */
    public ServiceConn get(Request req) throws ServiceException {
        //--
        // As a convenience, we check to see if the ServiceAddress field
        // is set. If so, we know the user wants exactly the server
        // identified by the ServiceAddress so we just look for it
        // directly. Otherwise, we delegate to child classes.
        //--
        ConnInfo connInfo = null;

        if (req.getServiceAddress() == null) {
            refresh(req, false);

            connInfo = getImpl(req);
        } else {
            ServiceData sdata = new ServiceData(req.getServiceAddress());
            ServiceAddressKey key;

            try {
                key = new ServiceAddressKey(req.getServiceAddress());
            } catch (UnknownHostException uhe) {
                throw new ServiceException(ServiceException.UNKNOWN_HOST, "saddr = " + req.getServiceAddress());
            }

            if (key != null) {
                connInfo = connInfoCache.getImmediate(key, sdata);
            }
        }

        if (connInfo == null) {
            String sid = "";
            if (serviceFinder != null) {
                sid = serviceFinder.getId();
            }

            throw new ServiceException(ServiceException.CONNECT, sid + ":no conns available!", null, null, true);
        }

        ServiceConn sconn = connInfo.getServiceConn();
        performConnection(sconn);

        return sconn;
    }

    private void performConnection(ServiceConn sconn) throws ServiceException {
        ServiceAddressKey key = getKey(sconn.getServiceAddress());

        //GAlerterLogger.lm( "ConnChooser.get: Returning conn = " + sconn );
        //--
        // Have only one thread attempt a connection.
        //--
        synchronized (key) {
            if (sconn.needsConnection()) {
                makeConn(key, sconn);
            }
        }
    }

    /**
     * By default, connections are made on a separate thread and they
     * timeout after 30 secs. You can modify that timeout with this
     * call.
     */
    public void setConnTimeout(int timeoutMillis) {
        connManager.setTimeout(timeoutMillis);
    }

    private void makeConn(ServiceAddressKey key, ServiceConn sconn) throws ServiceException {
    	if (logger.isTraceEnabled()) {
    		logger.trace("ConnChooser: needs connection: " + sconn);
    	}

        if (connLoadChecker.isOverloaded(sconn.getServiceAddress())) {
            logger.warn("ConnChooser.makeConn: server is overloaded, rate of connections is exceeded to: " + sconn.getServiceAddress());
            throw new ServiceException(ServiceException.OVERLOADED, "Reached conn-making threshold, more info in server log file");
        }

        connLoadChecker.connAttempt(sconn.getServiceAddress());
        connManager.connect(key, sconn, eventConfig, greeter);
        connLoadChecker.connSucceeded(sconn.getServiceAddress());

        if (logger.isTraceEnabled()) {
        	logger.trace("ConnChooser: connected: " + sconn);
        }
    }

    /**
     * A utility method to store and retrieve ServiceAddressKey objects.
     * The only reason we do this is so that we can synch on the
     * ServiceAddressKey object, which means we don't want to build
     * a different object every time, but rather cache them.
     */
    private synchronized ServiceAddressKey getKey(ServiceAddress saddr) throws ServiceException {
        ServiceAddressKey key = serviceAddressKeysMap.get(saddr);

        if (key == null) {
            try {
                key = new ServiceAddressKey(saddr);
            } catch (UnknownHostException uhe) {
                throw new ServiceException(ServiceException.UNKNOWN_HOST, saddr.toString());
            }

            serviceAddressKeysMap.put(saddr, key);
        }

        return key;
    }

    /**
     * Set the service finder object to use. MUST BE SET if
     * the get() method is invoked.
     */
    public void setServiceFinder(ServiceFinder finder) {
        serviceFinder = finder;
    }

    /**
     * By calling this method, you indicate your interest in receiving
     * events. An event thread will be started when a connection gets
     * made to the underlying server.
     */
    public void setEventCfg(EventConfig eventConfig) {
        this.eventConfig = eventConfig;
    }

    public String getId() {
        return serviceFinder == null ? "UNKNOWN" : serviceFinder.getId();
    }

    public synchronized void close() {
        if (closedFlag) {
            return;
        }

        if (logger.isDebugEnabled()) {
        	logger.debug("ConnChooser.close:" + hashCode());
        }
        if (serviceFinder != null) {
            serviceFinder.close();
        }

        connInfoCache.close();
        closedFlag = true;
    }

    private void refresh(Request req, boolean reallyNeedSome) {
        if (serviceFinder == null) {
            throw new RuntimeException("ConnChooser.refresh: NEED TO SET A ServiceFinder object!");
        }

        //--
        // Clean out the cache of any bad conns.
        //--
        connInfoCache.removeBadConns();
        if (reallyNeedSome(req, connInfoCache)) {
            reallyNeedSome = true;
        }

        List<ServiceData> serviceDataList = serviceFinder.getServiceData(reallyNeedSome);
        if (serviceDataList == null) {
            return;
        }

        //--
        // Update the cache with any new arrivals.
        //--
        connInfoCache.update(serviceDataList);
    }

    /**
     * This func can be overriden by others to be more selective as
     * to what "really needs some" means.
     */
    protected boolean reallyNeedSome(Request req, ConnCache cache) {
        return cache.isEmpty();
    }

    /**
     * Utility method to remove a bad conn.
     */
    synchronized void remove(ServiceConn sconn) {
        if (sconn != null) {
            sconn.close();
        }

        connInfoCache.remove(sconn);
    }

    /**
     * Return an array of the current conns, will never be null but
     * may have a length of 0.
     */
    public ConnInfo[] getCurrentConns() {
        return connInfoCache.getCurrentConns();
    }

    public Map<Integer, ConnInfo> getCurrentPartitionConns() {
        return connInfoCache.getCurrentPartitionConns();
    }

    public Collection<ServiceData> getCurrentServiceData() {
        return connInfoCache.getCurrentServiceData();
    }

    private ConnInfo getImpl(Request req) {
        ConnPick pick = getPick(req);

        if (pick == null) {
            return null;
        }

        ConnInfo connToKill = pick.getConnToKill();
        if (connToKill != null) {
            connToKill.getServiceConn().close();
        }

        ServiceConn sconn = pick.getConnInfo().getServiceConn();
        if (sconn == null || !sconn.isAvailable()) {
            pick = getPick(req);
        }

        if (pick == null) {
            return null;
        }

        connToKill = pick.getConnToKill();
        if (connToKill != null) {
            connToKill.getServiceConn().close();
        }

        return pick.getConnInfo();
    }
}
