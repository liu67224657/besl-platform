package com.enjoyf.platform.service.naming;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.service.service.Request;
import com.enjoyf.platform.service.service.ServiceConn;
import com.enjoyf.platform.service.service.ServiceData;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.ServiceRequest;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * The service class for the naming service.
 */

public class NamingServiceNormal extends NamingServiceDefault {
    //
    private static final Logger logger = LoggerFactory.getLogger(NamingServiceNormal.class);

    //
    private NamingServiceConn serviceConn;
    private NamingServiceAddress serviceAddress;

    /**
     * Ctor the object using the passed in config object.
     */
    public NamingServiceNormal(NamingServiceAddress namingService) {
        serviceAddress = namingService;

        init(null);
    }

    private void init(ServiceAddress hint) {
        serviceConn = new NamingServiceConn(
                serviceAddress,
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

    private void eventProcess(RPacket rp) {
        ServiceRequest[] reqs = null;
        ServiceAddress saddr = null;
        String tstr = null;

        byte type = rp.getType();

        switch (type) {
            case NamingConstants.EV_SERVICE_CHANGE:
                reqs = (ServiceRequest[]) rp.readSerializable();
                p_handleServiceChange(reqs);

                break;
            case NamingConstants.EV_REBALANCE:
                tstr = rp.readString();
                saddr = new ServiceAddress();
                saddr.reconstitute(tstr);
                p_handleRebalance(saddr);

                break;
            default:
                GAlerter.lab("NamingServiceNormal.eventProcess: Got unknown event! " + type);
        }
    }

    /**
     * Invoked whenever we have had a change in the services (ie, an
     * event arrived notifying us that new services are available).
     */
    private void p_handleServiceChange(ServiceRequest[] reqs) {
        if (logger.isDebugEnabled()) {
            logger.debug("NamingServiceNormal.p_handleServiceChange: " + Arrays.asList(reqs));
        }
        for (int i = 0; i < reqs.length; i++) {
            NamingService.Listener[] listeners = listenerManager.getListeners(reqs[i]);

            for (int j = 0; j < listeners.length; j++) {
                listeners[j].changed(reqs[i]);
            }
        }
    }

    private void p_handleRebalance(ServiceAddress saddr) {
        if (logger.isDebugEnabled()) {
            logger.debug("NamingServiceNormal: Received rebalance event: " + saddr);
        }
        serviceConn.close();

        init(saddr);
    }

    public Vector<ServiceAddress> getServiceAddress(ServiceRequest sreq) throws ServiceException, NamingException {
        if (logger.isDebugEnabled()) {
            logger.debug("NamingService.getRoomServerAddress: " + sreq);
        }
        p_checkForChange();

        WPacket wp = new WPacket();

        wp.writeSerializable(sreq);
        Request req = new Request(NamingConstants.GET_SERVICE_ADDRESS, wp);

        RPacket rp = serviceConn.send(req);
        int count = rp.readIntNx();
        Vector<ServiceAddress> vout = new Vector<ServiceAddress>(count);
        for (int i = 0; i < count; i++) {
            String tmp = rp.readString();
            ServiceAddress saddr = new ServiceAddress();
            saddr.reconstitute(tmp);
            vout.addElement(saddr);
        }

        return vout;
    }

    public NamingServiceAddress getServiceAddress() {
        return serviceConn.getServiceAddress();
    }

    public Vector getServiceTypes() throws ServiceException {
        p_checkForChange();

        WPacket wp = new WPacket();

        Request req = new Request(NamingConstants.GET_SERVICE_TYPES, wp);

        RPacket rp = serviceConn.send(req);
        return (Vector) rp.readSerializable();
    }

    public Vector getServiceNames(String serviceType) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("NamingService.getServiceNames: " + serviceType);
        }
        p_checkForChange();

        WPacket wp = new WPacket();
        wp.writeSerializable(serviceType);

        Request req = new Request(NamingConstants.GET_SERVICE_NAMES, wp);

        RPacket rp = serviceConn.send(req);
        return (Vector) rp.readSerializable();
    }

    public Vector getServiceIds() throws ServiceException {
        p_checkForChange();

        WPacket wp = new WPacket();

        Request req = new Request(NamingConstants.GET_SERVICE_IDS, wp);

        RPacket rp = serviceConn.send(req);
        return (Vector) rp.readSerializable();
    }

    @SuppressWarnings("unchecked")
    public List<ServiceData> getServiceData(ServiceRequest sreq) throws ServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("NamingService.getServiceData: " + sreq);
        }
        p_checkForChange();

        WPacket wp = new WPacket();
        wp.writeSerializable(sreq);
        Request req = new Request(NamingConstants.GET_SERVICE_DATA, wp);

        RPacket rp = serviceConn.send(req);
        return (List<ServiceData>) rp.readSerializable();
    }

    public void storeObject(String key, Serializable obj) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(key);
        wp.writeSerializable(obj);
        Request req = new Request(NamingConstants.STORE_OBJECT, wp);
        serviceConn.send(req);
    }

    public Serializable getObject(String key) throws ServiceException {
        WPacket wp = new WPacket();
        wp.writeSerializable(key);
        Request req = new Request(NamingConstants.GET_OBJECT, wp);

        RPacket rp = serviceConn.send(req);
        return rp.readSerializable();
    }

    private void p_synchUp(ServiceConn sconn) throws ServiceException {
        Request req = p_getFullEventRequest();

        if (req != null) {
            logger.debug("NamingServiceNormal.p_synchUp: Sending EVENT_REGISTER");
            sconn.send(req);
        }
    }

    private synchronized void p_checkForChange() throws ServiceException {
        Request req = p_getEventRequest();

        if (req == null) {
            return;
        }

        logger.debug("NamingServiceNormal.p_checkForChange: Sending EVENT_REGISTER");
        serviceConn.send(req);
    }

    private Request p_getEventRequest() {
        ServiceRequest[] sreqs = listenerManager.getChangedRequests();
        if (sreqs == null || sreqs.length == 0) {
            return null;
        }

        return p_getRequest(sreqs);
    }

    private Request p_getFullEventRequest() {
        ServiceRequest[] sreqs = listenerManager.getServiceRequests();
        if (sreqs.length == 0) {
            return null;
        }
        return p_getRequest(sreqs);
    }

    private Request p_getRequest(ServiceRequest[] sreqs) {
        WPacket wp = new WPacket();
        wp.writeSerializable(sreqs);
        return new Request(NamingConstants.EVENT_REGISTER, wp);
    }
}
