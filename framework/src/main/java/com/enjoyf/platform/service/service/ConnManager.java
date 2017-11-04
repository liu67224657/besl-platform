/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import java.util.HashMap;

import com.enjoyf.platform.io.ServiceAddressKey;
import com.enjoyf.platform.util.Utility;

/**
 * A class to manage the *establishment* of a connection to a ServiceConn
 * object. The class should have the following features:
 * <p/>
 * - The conn should be made on a separate thread.
 * - A conn is identified by a ServiceAddressKey, essentially an ip/port.
 * - Only one conn to one ServiceAddressKey should be attempted at
 * any given moment in time.
 * - A thread attempting to make a connection when one is already in
 * progress should simply get an error, perhaps the thread should
 * wait a bit (a few seconds) for the conn to be ready. If not ready
 * a CONNECT_IN_PROGRESS should be thrown.
 */
public class ConnManager {
    /**
     * How long to wait for a conn to be established when some other
     * thread has initiated the connection.
     */
    private static final int BALK_PERIOD = 1000;

    /**
     * Maps a ServiceAddressKey to a ConnMaker object used to
     * make the connection to a ServiceConn.
     */
    private HashMap<ServiceAddressKey, ConnMaker> connMakersMap = new HashMap<ServiceAddressKey, ConnMaker>();

    private int timeoutTime = 30 * 1000;

    /**
     * Ctor this object. Set the timeout.
     */
    public ConnManager(int timeout) {
        timeoutTime = timeout;
    }

    public void setTimeout(int timeout) {
        timeoutTime = timeout;
    }

    /**
     * The idea is that multiple threads may be calling into this
     * function to perform the connect. We must manage the fact
     * that only one can actually perform the connect.
     */
    void connect(ServiceAddressKey key, ServiceConn sconn, EventConfig eventConfig, Greeter greeter) throws ServiceException {
        synchronized (key) {
            //--
            // Retrieve the ConnMaker object to use. If it's currently
            // making a connection to this ServiceAddress then wait a bit
            // and check again.
            //--
            ConnMaker connMaker = getConnMaker(key);
            if (connMaker.isInProgress()) {
                Utility.sleep(BALK_PERIOD);

                if (connMaker.isInProgress()) {
                    throw new ServiceException(ServiceException.CONNECT_IN_PROGRESS);
                }
            }

            //--
            // Make the connection. The ConnMaker object will perform
            // the connection on a separate thread.
            //--
            connMaker.connect(sconn, eventConfig, greeter);

            //
            if (connMaker.isInProgress()) {
                throw new ServiceException(ServiceException.CONNECT_IN_PROGRESS);
            }

            if (!sconn.isConnected()) {
                throw new ServiceException(ServiceException.CONNECT);
            }
        }
    }

    private synchronized ConnMaker getConnMaker(ServiceAddressKey key) {
        //--
        // ConnMaker objects will persist throughout the lifetime of the
        // this object, which is encapsulated by a ConnChooser object.
        //--
        ConnMaker connMaker = connMakersMap.get(key);
        if (connMaker == null) {
            connMaker = new ConnMaker(timeoutTime);

            connMakersMap.put(key, connMaker);
        }

        return connMaker;
    }
}
