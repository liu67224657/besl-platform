/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.alert;

import java.util.Enumeration;
import java.util.Hashtable;

import com.enjoyf.platform.serv.thrserver.ConnThreadBase;

/**
 * A class to contain a bunch of Client objects. We keep track of the
 * client objects simply to print out a string identifying the client
 * with each alert msg. Note that this class doesn't add much value
 * over a hashtable, except compile-time type-checking.
 */
class ClientContainer {
    /**
     * A container of Client objects. Key=conn, Value=Client.
     */
    private Hashtable clientsTable = new Hashtable(100);

    ClientContainer() {
    }

    int size() {
        return clientsTable.size();
    }

    /**
     * Return an enumeration over all clients.
     */
    Enumeration elements() {
        return clientsTable.elements();
    }

    public synchronized void add(ConnThreadBase conn, Client client) {
        clientsTable.put(conn, client);
    }

    public void remove(ConnThreadBase conn) {
        clientsTable.remove(conn);
    }

    public Client get(ConnThreadBase conn) {
        return (Client) clientsTable.get(conn);
    }
}