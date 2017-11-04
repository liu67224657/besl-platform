/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;


public class ServiceConstants {
    /**
     * Error code returned from a request.
     */
    public static final byte OK = 1;

    /**
     * Error code returned from a request.
     */
    public static final byte NOTOK = 2;

    /**
     * A HELLO request to a server.
     */
    public static final byte HELLO = -128;

    /**
     * A synch request to a server.
     */
    public static final byte SYNCH = -127;

    /**
     * A msg indicating we are done synching.
     */
    public static final byte SYNCH_END = -126;

    /**
     * Transaction type for returning performance data. All
     * servers should support this type.
     */
    public static final byte GET_PERF_DATA = -125;

    /**
     * Causes a server to refresh its internal cache (if it has one)
     */
    public static final byte REFRESH_CACHE = -124;

    /**
     * Used for XA handling
     */
    public static final byte XA_INVOKE = -123;
}
