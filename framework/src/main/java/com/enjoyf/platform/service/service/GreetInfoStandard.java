/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import com.enjoyf.platform.io.WPacket;

/**
 * This concrete class defines a standard way of greeting a server.
 */
public class GreetInfoStandard implements GreetInfo {
    private Request initReq;
    private Request reconReq;

    /**
     * @param clientName This is the name of the client as a string.
     *                   It should be unique across the network.
     */
    public GreetInfoStandard(String clientName) {
        WPacket wp1 = new WPacket();
        wp1.writeSerializable(new HelloInfo(clientName, false));
        initReq = new Request(ServiceConstants.HELLO, wp1);

        WPacket wp2 = new WPacket();
        wp2.writeSerializable(new HelloInfo(clientName, true));
        reconReq = new Request(ServiceConstants.HELLO, wp2);
    }

    /**
     * Get the Request object to send when we initially connect to
     * a server.
     */
    public Request getInitHello() {
        return initReq;
    }

    /**
     * Get the Request object to send when we reconnect to a server.
     */
    public Request getReconHello() {
        return reconReq;
    }
}
