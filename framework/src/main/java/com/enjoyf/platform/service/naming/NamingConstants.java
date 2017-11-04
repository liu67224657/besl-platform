/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.naming;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * NamingBackChannelConstants, some shared between client and server.
 */
public class NamingConstants {
    public static final byte REGISTER = 1;
    public static final byte UNREGISTER = 2;
    public static final byte PING = 3;
    public static final byte SYNCH = 4;
    public static final byte SYNCH_END = 5;
    public static final byte SRV_REGISTER = 6;
    public static final byte SRV_UNREGISTER = 7;
    public static final byte GET_SERVICE_TYPES = 8;
    public static final byte GET_SERVICE_NAMES = 9;
    public static final byte GET_SERVICE_IDS = 10;
    public static final byte GET_SERVICE_ADDRESS = 11;
    public static final byte STORE_OBJECT = 12;
    public static final byte GET_OBJECT = 13;
    public static final byte EVENT_REGISTER = 14;
    public static final byte GET_SERVICE_DATA = 15;

    /**
     * This is an event received from the server.
     */
    public static final byte EV_SERVICE_CHANGE = 100;

    /**
     * This event is received when the naming system decides
     * that the service should go away, primarily because it
     * clashes with a registration that has been deemed to be
     * the "true" registration.
     */
    public static final byte EV_SERVICE_DIE = 101;

    /**
     * This means that the naming server the client is connected to
     * is overloaded, and is telling the client to go and connect
     * elsewhere (and it gives it a hint as to how to do that as well).
     */
    public static final byte EV_REBALANCE = 102;

    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    static {
        transProfileContainer.put(new TransProfile(REGISTER, "REGISTER"));
        transProfileContainer.put(new TransProfile(UNREGISTER, "UNREGISTER"));
        transProfileContainer.put(new TransProfile(PING, "PING"));
        transProfileContainer.put(new TransProfile(GET_SERVICE_TYPES, "GET_SERVICE_TYPES"));
        transProfileContainer.put(new TransProfile(GET_SERVICE_NAMES, "GET_SERVICE_NAMES"));
        transProfileContainer.put(new TransProfile(GET_SERVICE_IDS, "GET_SERVICE_IDS"));
        transProfileContainer.put(new TransProfile(GET_SERVICE_ADDRESS, "GET_SERVICE_ADDRESS"));
        transProfileContainer.put(new TransProfile(GET_SERVICE_DATA, "GET_SERVICE_DATA"));
        transProfileContainer.put(new TransProfile(EVENT_REGISTER, "EVENT_REGISTER"));
    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
