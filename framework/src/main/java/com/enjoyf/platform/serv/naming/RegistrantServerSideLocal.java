package com.enjoyf.platform.serv.naming;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.service.naming.ClientRegInfo;
import com.enjoyf.platform.service.naming.NamingConstants;
import com.enjoyf.platform.service.service.ServiceLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that represents a RegistrantServerSide that is "local", meaning it
 * is registered with this service.(the registrant is from this NS)
 */
class RegistrantServerSideLocal extends RegistrantServerSide {

    private static final Logger logger = LoggerFactory.getLogger(RegistrantServerSideLocal.class);

    private static final int WELL = 1;
    private static final int DEAD = 2;

    private int m_state;
    private long m_lastPingTime = System.currentTimeMillis();
    private ConnThreadBase m_conn;
    private boolean m_rebalancing = false;

    RegistrantServerSideLocal(ConnThreadBase conn, ServerRegInfo regInfo) {
        super(regInfo);
        m_state = WELL;
        m_conn = conn;
    }

    ConnThreadBase getConn() {
        return m_conn;
    }

    boolean isLocal() {
        return true;
    }

    /**
     * Ask if this client should die.
     *
     * @param connTimeout This is the connection timeout.
     */
    boolean shouldDie(int connTimeout) {
        long cur = System.currentTimeMillis();
        int diff = (int) (cur - m_lastPingTime);
        if (diff > connTimeout) {
            logger.debug("Client.shouldDie: "
                    + getServerRegInfo().getClientRegInfo().getServiceId()
                    + " diff " + diff);
            return true;
        }
        return false;
    }

    void ping(ServiceLoad load) {
        m_lastPingTime = System.currentTimeMillis();
        setLoad(load);
    }


    synchronized void sendDie(ClientRegInfo regInfo) {
        if (m_state == DEAD) {
            return;
        }

        logger.info("RegistrantServerSideLocal.sendDie: Sending die to: " + m_conn);

        WPacket wp = new WPacket();
        wp.setType(NamingConstants.EV_SERVICE_DIE);
        wp.writeSerializable(regInfo);
        try {
            m_conn.write(wp);
        } catch (Exception e) {
            logger.error("RegistrantServerSideLocal.sendDie: Caught exception: ", e);
        }
        m_state = DEAD;
    }


    synchronized void sendRebalance(ServiceAddress saddr) {
        //--
        // We might have been declared unwell, in which case we
        // don't do anything.
        //--
        if (!isWell()) {
            return;
        }

        logger.info("RegistrantServerSideLocal.sendRebalance: Telling: " + getServerRegInfo()
                + " to rebalance.");

        m_rebalancing = true;
        WPacket wp = new WPacket();
        wp.setType(NamingConstants.EV_REBALANCE);
        wp.writeString(saddr.deconstitute());
        try {
            m_conn.write(wp);
        } catch (Exception e) {
            logger.error("RegistrantServerSideLocal.sendRebalance: Caught exception: ", e);
        }
    }


    synchronized void die() {
        if (m_state == DEAD) {
            return;
        }

        m_state = DEAD;
        m_conn.die();
    }

    boolean isDead() {
        return m_state == DEAD;
    }

    boolean isRebalancing() {
        return m_rebalancing;
    }

    public boolean isWell() {
        return m_state == WELL;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(super.toString());
        sb.append(":conn=" + m_conn);
        sb.append(":state=" + m_state);
        return new String(sb);
    }
}
