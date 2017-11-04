/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.service.naming.NamingService;
import com.enjoyf.platform.service.naming.NamingServiceSngl;
import com.enjoyf.platform.util.RandomRange;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.thread.DiePoolThread;

/**
 * A class used to find a service by using the monitor.
 */
public class ServiceFinderNaming implements ServiceFinder, NamingService.Listener {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceFinderNaming.class);
	
    private ServiceRequest serviceRequest;
    private NamingService namingService;
    /**
     * A List of ServiceData objects.
     */
    private List<ServiceData> serviceDataList = null;
    private boolean changedFlag = false;
    private boolean activatedFlag = false;
    private long lastUpdateTime = 0;
    private RandomRange randomRange;
    private RefreshThread refreshThread;
    private boolean registerForEventsFlag = true;

    public ServiceFinderNaming(NamingService service, ServiceRequest req) {
        namingService = service;
        serviceRequest = req;
    }

    /**
     * By default, this is set to true. This means whenever the state
     * of the particular service changes within the naming service,
     * an event is sent from it. We can turn this feature off by
     * making this call.
     */
    public void setRegisterForEvents(boolean registerForEvents) {
        registerForEventsFlag = registerForEvents;
    }

    public ServiceFinderNaming(ServiceRequest req) {
        this(NamingServiceSngl.get(), req);
    }

    public synchronized void changed(ServiceRequest req) {
    	if (logger.isDebugEnabled()) {
    		logger.debug("ServiceFinderNaming.changed:" + hashCode() + ", Received change event: " + req);
    	}
    	
        //--
        // This is an optimization. If this object has not been used,
        // don't bother querying.
        //--
        if (!activatedFlag) {
            return;
        }

        //--
        // If we got a null back it means we couldn't talk to the ns
        // for whatever reason, so just keep whatever we have.
        //--
        List<ServiceData> servDataList = p_getServiceData(true);
        if (serviceDataList != null) {
            serviceDataList = servDataList;
            changedFlag = true;
        }
    }

    /**
     * Return a List of ServiceData objects that satisfies
     * the ServiceRequest (note that there may only be one,
     * depending on the type of ServiceRequest).
     *
     * @return Return a List of ServiceData objects. May return
     *         null indicating that nothing's changed since the last
     *         time this method was invoked.
     */
    public synchronized List<ServiceData> getServiceData(boolean reallyNeedSome) {
        //--
        // The first time we try to use this object, register
        // ourselves as an event listener.
        //--
        if (!activatedFlag && registerForEventsFlag) {
            namingService.addListener(serviceRequest, this);
        }

        activatedFlag = true;

        //--
        // Only want to fire this thread up once we've become active.
        //--
        if (p_wantRefreshThread() && refreshThread == null) {
            refreshThread = new RefreshThread();
            refreshThread.start();
        }
        if (!reallyNeedSome && !changedFlag) {
            return null;
        }

        if (changedFlag) {
            changedFlag = false;
            return serviceDataList;
        }

        //--
        // If here, it means we really need some addresses and we
        // don't currently have any.
        if (logger.isDebugEnabled()) { 
			logger.debug("ServiceFinderNaming.getServiceData:" + hashCode()
                + ": Could not find any "
                + " recent events, need to query the ns directly ");
		}

        serviceDataList = p_getServiceData(false);
        return serviceDataList;
    }

    private synchronized void p_updateList() {
        serviceDataList = p_getServiceData(true);
        changedFlag = true;
    }

    private List<ServiceData> p_getServiceData(boolean bypassTimeCheck) {
        //--
        // If it's only been a short while, and we have something, just
        // return that.
        //--
        if (!bypassTimeCheck && serviceDataList != null && System.currentTimeMillis() - lastUpdateTime < 10 * 1000) {
            return serviceDataList;
        }

        List<ServiceData> l = null;
        try {
            l = namingService.getServiceData(serviceRequest);
        } catch (ServiceException e) {
        	logger.warn("ServiceFinderNaming.getServiceData:" + hashCode()
                    + ":Failed getting addresses for: " + serviceRequest + ": exc = " + e);
            return null;
        }

        lastUpdateTime = System.currentTimeMillis();

        if (logger.isDebugEnabled()) {
        	logger.debug("ServiceFinderNaming.p_getServiceData:" + hashCode() + ":Got back: " + l);
        }
        
        return l;
    }

    /**
     * If this method is invoked, it means that we will fire up
     * a thread that performs a periodic query for the services.
     * This is done in order to retrieve the load from the naming
     * server.
     *
     * @param frequency How often (in msecs) to query the naming
     *                  service.
     * @param offset    If we want to randomize the frequency, this
     *                  value can be set to non-zero. In this case the interval will
     *                  range from frequency-offset to frequency+offset. In msecs.
     * @throws IllegalArgumentException Thrown if frequency-offset is
     *                                  less than 0.
     */
    public void useRefreshThread(int frequency, int offset) {
        randomRange = new RandomRange(frequency - offset, frequency + offset);
    }

    private boolean p_wantRefreshThread() {
        return randomRange != null;
    }

    private class RefreshThread extends DiePoolThread {
        RefreshThread() {
            setName("RefreshThread:" + hashCode());
        }

        public void run() {
        	if (logger.isDebugEnabled()) {
        		logger.debug("ServiceFinderNaming.RefreshThread:"
                    + ServiceFinderNaming.this.hashCode() + ": start");
        	}
            while (!shouldDie()) {
                try {
                    Utility.sleep(randomRange.nextInt());
                    p_updateList();
                }
                catch (Exception e) {
                    logger.warn("ServiceFinderNaming.RefreshThread:"
                            + hashCode()
                            + ": UNEXPECTED EXCEPTION: " + e);
                }
            }
        }
    }

    public String getId() {
        return serviceRequest.toString();
    }

    public void close() {
    	if (logger.isDebugEnabled()) {
    		logger.debug("ServiceFinderNaming.close:" + hashCode() + ":" + getId());
    	}

        if (refreshThread != null) {
            refreshThread.die();
        }
        if (activatedFlag) {
            namingService.removeListener(serviceRequest, this);
        }
    }
}
