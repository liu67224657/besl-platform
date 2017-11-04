/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.naming;

/**
 * NamingBackChannelConstants used in back-channel communication.
 */
public class NamingBackChannelConstants {
    /**
     * Registration over the back channel.
     */
    public static final byte BCH_REGISTER = 1;

    /**
     * Unregistration over the backchanel.
     */
    public static final byte BCH_UNREGISTER = 2;

    /**
     * Sent upon connection, a hello packet.
     */
    public static final byte BCH_HELLO = 3;

    /**
     * A ping msg.
     */
    public static final byte BCH_PING = 4;

    /**
     * Duplicate registration. A server has determined that another
     * server (the one receiving this msg) has a bogus registration.
     * The server receiving this event should proceed to get rid of
     * the registration.
     */
    public static final byte BCH_DUP_REGISTRATION = 5;

    public static final byte BCH_SYNCH = 6;

    /**
     * Sent when an NS received a ping from a service with a load. The
     * load is broadcast to the other naming servers.
     */
    public static final byte BCH_LOAD_UPDATE = 7;
}
