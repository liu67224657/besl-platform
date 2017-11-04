package com.enjoyf.platform.serv.naming;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.io.ServiceAddressKey;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.IpUtil;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.log.GAlerter;

import com.google.common.base.Joiner;

/**
 * This class is used to configure the server. It is typically a specific
 * class for the server.
 */

class Config {
	
	private static final Logger logger = LoggerFactory.getLogger(Config.class);
	
    /**
     * How often to send out a change event, if in fact
     * things have changed.
     */
    private String ip;
    private int port;

    private int changeEventInterval;
    private int monInterval;
    private int vultureInterval;
    private int connTimeout;
    private int backChannelPort;
    private int backChannelPing;
    private int backChannelTimeout;
    private int loadCheckInterval;
    private int loadCheckWait;
    private int maxLoad;

    private int rateLimitMaxCount;
    private int rateLimitPeriod;
    private int startWaitTime;
    private int eventCutOffTime;

    private ServiceAddress[] namingServices;

    private IpConfig ipConfig = null;

    Config() {
        init();
    }

    /**
     * This method will construct the object from the passed in
     * props object.
     */

    Config(FiveProps props) {
        init();

        if (props == null) {
            return;
        }

        logger.info("Config. Configuring with props: " + props);

        int ival = 0;
        ival = props.getInt("server.monInterval");
        if (ival != 0) {
            monInterval = ival * 1000;
        }

        ival = props.getInt("server.vultureInterval");
        if (ival != 0) {
            vultureInterval = ival * 1000;
        }

        ival = props.getInt("server.connTimeout");
        if (ival != 0) {
            connTimeout = ival * 1000;
        }

        ival = props.getInt("server.changeEventInterval", -1);
        if (ival != -1) {
            changeEventInterval = ival * 1000;
        }

        ival = props.getInt("server.backChannelPort");
        if (ival != 0) {
            backChannelPort = ival;
        }

        ival = props.getInt("server.backChannelPing");
        if (ival != 0) {
            backChannelPing = ival * 1000;
        }

        ival = props.getInt("server.backChannelTimeout");
        if (ival != 0) {
            backChannelTimeout = ival * 1000;
        }

        ival = props.getInt("server.loadCheckInterval");
        if (ival != 0) {
            loadCheckInterval = ival * 1000;
        }

        ival = props.getInt("server.loadCheckWait");
        if (ival != 0) {
            loadCheckWait = ival;
        }

        ival = props.getInt("server.maxLoad");
        if (ival != 0) {
            maxLoad = ival;
        }

        ival = props.getInt("server.port");
        if (ival != 0) {
            port = ival;
        }

        ival = props.getInt("server.rateLimitPeriod");
        if (ival != 0) {
            rateLimitPeriod = ival;
        }

        ival = props.getInt("server.rateLimitMaxCount");
        if (ival != 0) {
            rateLimitMaxCount = ival;
        }

        ival = props.getInt("server.startWaitTime", -1);
        if (ival != -1) {
            startWaitTime = ival * 1000;
        }

        ival = props.getInt("server.eventCutOffTime");
        if (ival != 0) {
            eventCutOffTime = ival * 1000;
        }

        int defPort = backChannelPort;

        String sval = props.get("server.namingServices");
        if (sval != null) {
            try {
                Map map = new HashMap();
                StringTokenizer t1 = new StringTokenizer(sval, " \t");
                while (t1.hasMoreTokens()) {
                    String tok = t1.nextToken();
                    StringTokenizer t2 = new StringTokenizer(tok, ":");
                    String ip = t2.nextToken();

                    int port = defPort;
                    if (t2.hasMoreTokens()) {
                        port = Integer.parseInt(t2.nextToken());
                    }

                    p_addAll(map, ip, port);
                }

                namingServices = new ServiceAddress[map.size()];
                namingServices = (ServiceAddress[]) map.values().toArray(namingServices);
            }
            catch (Exception e) {
                GAlerter.lab("NamingService.Config: Malformed 'namingServices' entry: " + sval);
            }
        }

        ipConfig = new IpConfig(props);
    }

    int getStartWaitTime() {
        return startWaitTime;
    }

    int getEventCutOffTime() {
        return eventCutOffTime;
    }

    /**
     * Get the ip of this server.
     */
    String getIp() {
        return ip;
    }

    /**
     * Set the vulture thread interval in MSECS. This is how often
     * to run the vulture thread.
     */
    void setVultureInterval(int vultureInterval) {
        this.vultureInterval = vultureInterval;
    }

    int getVultureInterval() {
        return vultureInterval;
    }

    ServiceAddress[] getNamingServices() {
        return namingServices;
    }

    /**
     * How long to wait before we declare a client dead (in terms of
     * not having received a ping from it).
     */
    void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    int getConnTimeout() {
        return connTimeout;
    }

    /**
     * Set the monitor thread interval in MSECS.
     */
    void setMonInterval(int monInterval) {
        this.monInterval = monInterval;
    }

    /**
     * Retrieve the monitor thread interval in MSECS.
     */
    int getMonInterval() {
        return monInterval;
    }

    void setChangeEventInterval(int changeEventInterval) {
        this.changeEventInterval = changeEventInterval;
    }

    /**
     * Retrieve the monitor thread interval in MSECS.
     */
    int getChangeEventInterval() {
        return changeEventInterval;
    }

    void setBackChannelPort(int port) {
        backChannelPort = port;
    }

    int getBackChannelPort() {
        return backChannelPort;
    }

    int getBackChannelPing() {
        return backChannelPing;
    }

    int getBackChannelTimeout() {
        return backChannelTimeout;
    }

    int getLoadCheckInterval() {
        return loadCheckInterval;
    }

    int getLoadCheckWait() {
        return loadCheckWait;
    }

    int getPort() {
        return port;
    }

    int getMaxLoad() {
        return maxLoad;
    }

    int getRateLimitPeriod() {
        return rateLimitPeriod;
    }

    int getRateLimitMaxCount() {
        return rateLimitMaxCount;
    }

    boolean isDupeCheckingEnabled() {
        return ipConfig.isDupeCheckingEnabled();
    }

    boolean isAllowed(String ip) {
        return ipConfig.isAllowed(ip);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("port=" + port + ":");
        sb.append("monInterval=" + monInterval + ":");
        sb.append("vultureInterval=" + vultureInterval + ":");
        sb.append("connTimeout=" + connTimeout + ":");
        sb.append("changeEventInterval=" + changeEventInterval + ":");
        sb.append("namingServices=" + Joiner.on(":").join(namingServices));
        sb.append("backChannelPing=" + backChannelPing + ":");
        sb.append("backChannelTimeout=" + backChannelTimeout + ":");
        sb.append("backChannelPort=" + backChannelPort + ":");
        sb.append("loadCheckInterval=" + loadCheckInterval + ":");
        sb.append("loadCheckWait=" + loadCheckWait + ":");
        sb.append("maxLoad=" + maxLoad + ":");
        sb.append("rateLimitPeriod=" + rateLimitPeriod + ":");
        sb.append("rateLimitMaxCount=" + rateLimitMaxCount + ":");
        sb.append("eventCutOffTime=" + eventCutOffTime + ":");
        sb.append("startWaitTime=" + startWaitTime + ":");
        sb.append("ipConfig=" + ipConfig + ":");

        return new String(sb);
    }

    /**
     * Add all addresses given by this ip. The ip could be some sort
     * of round-robin virtual address, so use getAllByName.
     */
    private void p_addAll(Map map, String ip, int port) {
        try {
            InetAddress[] all = InetAddress.getAllByName(ip);
            for (int i = 0; i < all.length; i++) {
                //--
                // Only add it to list if it isn't us.
                //--
                logger.info("Comparing: " + all[i].getHostAddress() + " with: " + this.ip);
                if (!IpUtil.compare(all[i].getHostAddress(), this.ip) || port != backChannelPort) {
                    ServiceAddress saddr = new ServiceAddress(all[i].getHostAddress(), port);

                    ServiceAddressKey key = null;
                    try {
                        key = new ServiceAddressKey(saddr);
                    } catch (UnknownHostException uhe) {
                        GAlerter.lab("Caught UnknownHostException during "
                                + " config for ip: " + saddr
                                + " Process will die and will need a restart");
                        Utility.sleep(5000);
                        System.exit(1);
                    }
                    map.put(key, saddr);
                }
            }
        }
        catch (Exception e) {
            GAlerter.lab("NamingServer.Config: Could not resolve: " + ip
                    + " exc = " + e);
        }
    }

    /**
     * Put default settings here.
     */
    private void init() {
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        }
        catch (Exception e) {
            GAlerter.lab("NamingServer.Config: " + "Could not get my own host name!!");
            ip = "unknown";
        }

        monInterval = 60 * 1000;
        vultureInterval = 60 * 1000;
        connTimeout = 60 * 1000;
        changeEventInterval = 60 * 1000;
        namingServices = new ServiceAddress[0];
        backChannelPort = 7600;
        backChannelPing = 60 * 1000;
        backChannelTimeout = 180 * 1000;
        loadCheckInterval = 60 * 1000;
        loadCheckWait = 1000;
        maxLoad = 1000;
        port = 7500;
        rateLimitPeriod = 60 * 1000;
        rateLimitMaxCount = 300;
        eventCutOffTime = 10 * 1000;
        startWaitTime = 5 * 60 * 1000;
    }
}
