package com.enjoyf.platform.service.naming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.service.service.ServiceId;
import com.enjoyf.platform.service.service.ServiceInfo;
import com.enjoyf.platform.service.service.ServiceLoad;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * Use this to register a service object with the naming service.
 */
public class RegistrantNormal extends RegistrantDefault {
    
    private static final Logger logger = LoggerFactory.getLogger(RegistrantNormal.class);
    /**
     * This is the underlying connection and ping thread. Note
     * that while the RegistrantNormal object is long-lived, the
     * RegistrantConn object can come and go.
     */
    private RegistrantConn registrantConn;
    private RegistrantId registrantId;


    protected RegistrantNormal(ServiceInfo servInfo) {
        serviceInfo = servInfo;
        registrantId = new RegistrantId(ClientRegInfo.getLocalClientId());
    }

    public void start() {
        ServiceId id = serviceInfo.getServiceId();
        if (id == null || id.isBad()) {
            GAlerter.lab("RegistrantNormal.start: Bad ServiceId = " + id);

            throw new IllegalStateException("Bad ServiceId! " + id);
        }

        p_init(null);
    }

    private void p_init(ServiceAddress saddr) {
        registrantConn = new RegistrantConn(this);
        registrantConn.start(saddr);
    }

    void handleRebalance(long id, ServiceAddress saddr) {
        RegistrantConn conn = p_checkConn(id);
        if (conn == null) {
            return;
        }

        logger.debug("RegistrantNormal: Received rebalance event: " + saddr);

        conn.close();

        p_init(saddr);
    }

    void handleDie(long id, ClientRegInfo serviceInfo) {
        RegistrantConn conn = p_checkConn(id);
        if (conn == null) {
            return;
        }

        //--
        // Check to see if we are the same entity. If we are, we don't
        // kill ourselves.
        //--
        if (registrantId.equals(serviceInfo.getRegistrantId())) {
            logger.warn("RegistrantNormal.handleDie: NOT dying because we are the same object");

            return;
        }

        logger.warn("RegistrantNormal: Dying because of a duplicate registration. The one that won is: " + serviceInfo + " conn dying = " + conn);

        conn.close();
    }

    /**
     * Utility to see if the id of an event matches our current conn.
     * Events should only be handled by the conn receiving the event.
     */
    private RegistrantConn p_checkConn(long id) {
        RegistrantConn conn = registrantConn;

        if (conn.isClosed()) {
            logger.warn("RegistrantNormal.p_checkConn: conn is already closed conn = " + conn + " ignoring event. ");
            return null;
        }

        if (!conn.isMatch(id)) {
            logger.warn("RegistrantNormal.p_checkConn: mismatch in id's id = " + registrantConn.getId() + " event id = " + id + " ignoring event. ");
            return null;
        }

        return conn;
    }

    /**
     * Called to shut this object down.
     */
    public void stop() {
        registrantConn.close();
    }

    /**
     * Send an updated load to the naming server.
     */
    public void updateLoad(ServiceLoad load) {
        registrantConn.updateLoad(load);
    }

    /**
     * This method is here for test purposes only.
     */
    public void ping() {
        registrantConn.ping();
    }

    public String toString() {
        return serviceInfo.toString();
    }

    public RegistrantId getId() {
        return registrantId;
    }
}
