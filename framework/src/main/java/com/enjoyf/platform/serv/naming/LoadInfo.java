/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.naming;

/**
 * Represents the load on the naming server.
 */
class LoadInfo implements java.io.Serializable, LoadBalancer.Loadable {
    /**
     * Number of services.
     */
    private int servicesNum = 0;

    /**
     * Number of regular clients.
     */
    private int regClientsNum = 0;

    LoadInfo() {
    }

    LoadInfo(int numRegClients, int numServices) {
        regClientsNum = numRegClients;
        servicesNum = numServices;
    }

    public void setNumServices(int numServices) {
        servicesNum = numServices;
    }

    public void setNumRegClients(int numRegClients) {
        regClientsNum = numRegClients;
    }

    public int getNumServices() {
        return servicesNum;
    }

    public int getNumRegClients() {
        return regClientsNum;
    }

    public int getLoad() {
        return servicesNum + regClientsNum;
    }

    public String toString() {
        return "service=" + servicesNum + ":clients=" + regClientsNum;
    }

    /**
     * An interface used to decouple the business logic from other users.
     */
    interface Getter {
        public LoadInfo getLoadInfo();
    }
}
