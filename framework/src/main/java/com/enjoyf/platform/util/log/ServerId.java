/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.log;

/**
 * A class to tag the current server with some id. This info is then
 * available to whoever wants it throughout the logicProcess. Can then
 * be used by the alerter.
 */
public class ServerId {
    private static String serverId = "anon:anon";

    public static void setId(String id) {
        serverId = id;
    }

    public static String getId() {
        return serverId;
    }
}
