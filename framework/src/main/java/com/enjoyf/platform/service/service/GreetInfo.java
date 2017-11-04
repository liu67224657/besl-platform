/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

/**
 * This interface defines what kinds of packets to use
 * when reconnecting to a server. Used by GreeterDefault.
 */
public interface GreetInfo {
    /**
     * Get the Request object to send when we initially connect to
     * a server.
     */
    public Request getInitHello();

    /**
     * Get the Request object to send when we reconnect to a server.
     */
    public Request getReconHello();
}
