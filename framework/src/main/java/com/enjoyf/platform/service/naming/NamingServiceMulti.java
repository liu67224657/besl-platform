package com.enjoyf.platform.service.naming;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConn;
import com.enjoyf.platform.service.service.ServiceData;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.ServiceId;
import com.enjoyf.platform.service.service.ServiceRequest;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * The service class for the naming service.
 */

public class NamingServiceMulti implements NamingService {
    //
    private static final Logger logger = LoggerFactory.getLogger(NamingServiceMulti.class);

    //
    private NamingServiceConn serviceConn;
    private NamingServiceAddress namingServiceAddress;

    //
    private MultiListenerManager listenerManager;

    //
    private Set<String> serviceTypeSet = new HashSet<String>();

    //service type -> set<service name>
    private Map<String, Set<String>> serviceNameMap = new HashMap<String, Set<String>>();

    /**
     * Ctor the object using the passed in config object.
     */
    public NamingServiceMulti(NamingServiceAddress namingServiceAddress, Set<String> serviceTypeSet) {
        //
        this.namingServiceAddress = namingServiceAddress;
        this.listenerManager = new MultiListenerManager();
        this.serviceTypeSet.addAll(serviceTypeSet);

        //
        init(null);
        initServiceTypes(this.serviceTypeSet);
    }

    public void addServiceType(String s) {
        //
        this.serviceTypeSet.add(s);

        //
        Set<String> set = new HashSet<String>();
        set.add(s);

        initServiceTypes(set);
    }

    private void init(ServiceAddress hint) {
        //
        serviceConn = new NamingServiceConn(
                namingServiceAddress,
                new NamingServiceConn.Listener() {
                    public void eventArrived(RPacket rp) {
                        eventProcess(rp);
                    }

                    public void synchUp(ServiceConn sconn) throws ServiceException {
                        p_synchUp(sconn);
                    }
                },
                hint
        );
    }

    private void initServiceTypes(Set<String> set) {
        int addedServiceTypeNum = 0;

        //
        for (String serviceType : set) {
            //
            if (logger.isDebugEnabled()) {
                logger.debug("NamingServiceMulti initServiceTypes loop, serviceType:" + serviceType);
            }

            //
            ServiceRequest serviceRequest = new ServiceRequest(serviceType, ServiceRequest.ALL);

            if (listenerManager.getListener(serviceRequest) == null) {
                NamingServiceTypeListener listener = new NamingServiceTypeListener(serviceRequest.getServiceType());

                listenerManager.add(serviceRequest, listener);

                //
                listenerManager.getListener(serviceRequest).changed(serviceRequest);

                addedServiceTypeNum++;
            } else {
                logger.info("Listener is exist, request:" + serviceRequest);
            }
        }

        //if there changed service type, check for update.
        if (addedServiceTypeNum > 0) {
            //
            if (logger.isDebugEnabled()) {
                logger.debug("NamingServiceMulti start to check for change.");
            }

            try {
                checkForChange();
            } catch (Exception e) {
                //
                logger.error("checkForChange error", e);
            }
        }
    }

    private void eventProcess(RPacket rp) {
        byte type = rp.getType();

        if (logger.isDebugEnabled()) {
            logger.debug("The event is coming, type:" + type);
        }

        switch (type) {
            case NamingConstants.EV_SERVICE_CHANGE:
                ServiceRequest[] reqs = (ServiceRequest[]) rp.readSerializable();

                //
                handleServiceChange(reqs);

                break;
            case NamingConstants.EV_REBALANCE:
                String tstr = rp.readString();

                ServiceAddress saddr = new ServiceAddress();
                saddr.reconstitute(tstr);

                //
                handleRebalance(saddr);

                break;
            default:
                GAlerter.lab("NamingServiceMulti.eventProcess: Got unknown event! " + type);
        }
    }

    /**
     * Invoked whenever we have had a change in the services (ie, an
     * event arrived notifying us that new services are available).
     */
    private void handleServiceChange(ServiceRequest[] serviceRequests) {
        if (logger.isDebugEnabled()) {
            logger.debug("NamingServiceMulti.handleServiceChange: " + Arrays.asList(serviceRequests));
        }

        for (int i = 0; i < serviceRequests.length; i++) {
            listenerManager.changed(serviceRequests[i]);
        }
    }

    private void handleRebalance(ServiceAddress saddr) {
        if (logger.isDebugEnabled()) {
            logger.debug("NamingServiceMulti: Received rebalance event: " + saddr);
        }

        serviceConn.close();

        init(saddr);
    }

    @Override
    public Vector<ServiceAddress> getServiceAddress(ServiceRequest sreq) throws ServiceException {
        return null;
    }

    @Override
    public ServiceAddress getOneServiceAddress(ServiceRequest req) throws ServiceException {
        return null;
    }

    public NamingServiceAddress getServiceAddress() {
        return serviceConn.getServiceAddress();
    }

    @Override
    public void addListener(ServiceRequest req, Listener l) {
    }

    @Override
    public void removeListener(ServiceRequest req, Listener l) {
    }

    @Override
    public void registerService(Registrant r) {
        //
    }

    @Override
    public void unregisterService(Registrant r) {
    }

    @Override
    public Vector<String> getServiceTypes() throws ServiceException {
        Vector<String> returnValue = new Vector<String>();

        for (Map.Entry<String, Set<String>> entry : serviceNameMap.entrySet()) {
            if (entry.getValue().size() > 0) {
                returnValue.add(entry.getKey());
            }
        }

        return returnValue;
    }

    public boolean isLive(String serviceType) {
        //
        Set<String> serviceNames = serviceNameMap.get(serviceType);

        return (serviceNames != null && serviceNames.size() > 0);
    }

    @Override
    public Vector getServiceIds() throws ServiceException {
        return null;
    }

    @Override
    public Vector<ServiceId> getServiceIds(String serviceType) throws ServiceException {
        return null;
    }

    @Override
    public List<ServiceData> getServiceData(ServiceRequest sreq) throws ServiceException {
        return null;
    }

    @Override
    public Vector getServiceNames(String serviceType) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("NamingServiceMulti.getServiceNames: " + serviceType);
        }

        WPacket wp = new WPacket();
        wp.writeSerializable(serviceType);

        Request req = new Request(NamingConstants.GET_SERVICE_NAMES, wp);

        RPacket rp = serviceConn.send(req);
        return (Vector) rp.readSerializable();
    }


    private void p_synchUp(ServiceConn sconn) throws ServiceException {
        Request req = getFullEventRequest();

        if (req != null) {
            logger.debug("NamingServiceNormal.p_synchUp: Sending EVENT_REGISTER");

            sconn.send(req);
        }
    }

    private synchronized void checkForChange() throws ServiceException {
        Request request = getFullEventRequest();

        if (request == null) {
            return;
        }

        logger.debug("NamingServiceNormal.checkForChange: Sending EVENT_REGISTER");

        serviceConn.send(request);
    }

    private Request getEventRequest() {
        ServiceRequest[] serviceRequests = listenerManager.getChangedRequests();
        if (serviceRequests == null || serviceRequests.length == 0) {
            return null;
        }

        return getRequest(serviceRequests);
    }

    private Request getFullEventRequest() {
        ServiceRequest[] serviceRequests = listenerManager.getServiceRequests();
        if (serviceRequests.length == 0) {
            return null;
        }

        return getRequest(serviceRequests);
    }

    private Request getRequest(ServiceRequest[] serviceRequests) {
        WPacket wp = new WPacket();
        wp.writeSerializable(serviceRequests);

        return new Request(NamingConstants.EVENT_REGISTER, wp);
    }

    //
    class NamingServiceTypeListener implements Listener {
        //
        private String serviceType;

        //
        NamingServiceTypeListener(String serviceType) {
            this.serviceType = serviceType;
        }

        @Override
        public void changed(ServiceRequest serviceRequest) {
            //
            if (!serviceType.equals(serviceRequest.getServiceType())) {
                return;
            }

            //
            try {
                Vector<String> serviceNames = getServiceNames(serviceRequest.getServiceType());
                synchronized (serviceNameMap) {
                    serviceNameMap.remove(serviceType);

                    Set<String> serviceNameSet = new HashSet<String>();
                    serviceNameSet.addAll(serviceNames);

                    serviceNameMap.put(serviceType, serviceNameSet);

                    listenerManager.setChanged(serviceRequest, false);
                }
            } catch (Exception e) {
                //
                GAlerter.lan("NamingServiceMulti getServiceNames error.", e);
            }
        }
    }
}
