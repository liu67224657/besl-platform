/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.ServiceAddress;

/**
 * Interface to be implemented by event listeners for ServiceConn
 * objects.
 */
public interface EventListener {
    //the event is arrived.
    public void eventArrived(RPacket rp, ServiceAddress sa);

    /**
     * If this method is called, it means that the conn
     * processing events is down.
     */
    public void connDown(ServiceAddress sa);
}
