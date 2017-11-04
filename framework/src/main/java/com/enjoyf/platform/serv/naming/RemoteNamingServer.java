package com.enjoyf.platform.serv.naming;

import java.net.InetAddress;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * This class represents a naming server that connects to us, and will be
 * sending us msgs.
 */
class RemoteNamingServer {
    private static int curId = 1;

    /**
     * This is the service address used by the backchannel, ie, the
     * one the naming servers communicate on.
     */
    private ServiceAddress serviceAddress;

    /**
     * This is the service address of the remote ns that clients
     * use to connect to it.
     */
    private ServiceAddress clientServiceAddress;
    private int id;
    private long lastPingTime = 0;
    private LoadInfo loadInfo = new LoadInfo();

    public RemoteNamingServer(ConnThreadBase conn) {
        id = p_nextId();
    }

    public RemoteNamingServer() {
        this(null);
    }

    public int getId() {
        return id;
    }

    public void setServiceAddress(ServiceAddress saddr) {
        serviceAddress = saddr;
    }

    public ServiceAddress getServiceAddress() {
        return serviceAddress;
    }

    public void setClientAddress(ServiceAddress clientAddress) {
        clientServiceAddress = clientAddress;
    }

    public ServiceAddress getClientAddress() {
        return clientServiceAddress;
    }

    public void setConn(ConnThreadBase conn) {
    }

    public void ping(long serverTime, LoadInfo load) {
        lastPingTime = System.currentTimeMillis();
        long diff = Math.abs(lastPingTime - serverTime);
        if (diff > 60 * 1000) {
            p_alertTimeOff(diff);
        }

        loadInfo = load;
    }

    public void setLoadInfo(LoadInfo load) {
        loadInfo = load;
    }

    public LoadInfo getLoadInfo() {
        return loadInfo;
    }

    public int hashCode() {
        return id;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (obj.getClass() != getClass()) {
            return false;
        }

        RemoteNamingServer c = (RemoteNamingServer) obj;
        return c.id == id;
    }

    public boolean isInitialized() {
        return clientServiceAddress != null;
    }

    public String toString() {
        return "id/saddr/load = " + id + "/" + serviceAddress + "/" + loadInfo;
    }

    private static synchronized int p_nextId() {
        return curId++;
    }

    private void p_alertTimeOff(long diff) {
        String ip = "UNKNOWN";
        try {
            ip = InetAddress.getLocalHost().getHostName();
        }
        catch (Exception e) {
        }
        GAlerter.lan("WARNING! The naming server on: " + ip + " and the one on: "
                + serviceAddress.getAddr() + " are off by: " + diff / 1000 + " secs!!");
    }
}
