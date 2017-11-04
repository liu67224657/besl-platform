/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.log;

import org.apache.commons.logging.Log;

/**
 * Utility class that works in concert with CommonsBridgeLog to pass
 * GAlerterLogger-style alerts through the Commons-Logging API.
 * <p/>
 * This class can be used two ways - either wrap an existing Log
 * in an object of this type, or use the static methods that take
 * a Log as a parameter.
 */
public class AlertLog {
    /**
     * This prefix indicates that a log message is an alert.
     */
    protected static final String ALERT_PREFIX = "***ALERT***";

    /**
     * Note that the whitespace is important for readability.
     */
    protected static final String ALERT_TYPE_NOISE = " NOISE ";
    protected static final String ALERT_TYPE_BUG = " BUG ";
    protected static final String ALERT_TYPE_DEBUG = " DEBUG ";
    protected static final String ALERT_TYPE_CUSTOMER = " CUSTOMER ";

    /**
     * Separator between variable text and not.  This eventually gets
     * interpreted by the log analyzer.
     */
    protected static final String VAR_TEXT_SEPARATOR = "%var%";


    /**
     * Use this for alerts that happen occassionally, but aren't a
     * problem unless they continue happening.
     */
    public static void noise(Log log, Object message, Object variableData, Throwable t) {
        sendMessage(log, message, variableData, t, ALERT_TYPE_NOISE);
    }

    /**
     * Convenience method
     */
    public static void noise(Log log, Object message, Object variableData) {
        noise(log, message, variableData, null);
    }

    /**
     * Convenience method
     */
    public static void noise(Log log, Object message, Throwable t) {
        noise(log, message, null, t);
    }

    /**
     * Convenience method
     */
    public static void noise(Log log, Object message) {
        noise(log, message, null, null);
    }

    /**
     * These are probably due to some programming bug or config bug.
     * Sometimes other causes can end up generating these alerts (eg, a
     * user messing around with the url).
     */
    public static void bug(Log log, Object message, Object variableData, Throwable t) {
        sendMessage(log, message, variableData, t, ALERT_TYPE_BUG);
    }

    /**
     * Convenience method
     */
    public static void bug(Log log, Object message, Object variableData) {
        bug(log, message, variableData, null);
    }

    /**
     * Convenience method
     */
    public static void bug(Log log, Object message, Throwable t) {
        bug(log, message, null, t);
    }

    /**
     * Convenience method
     */
    public static void bug(Log log, Object message) {
        bug(log, message, null, null);
    }

    /**
     * These aren't so much alerts as they are messages that can be
     * used to debug various conditions.
     */
    public static void debug(Log log, Object message, Object variableData, Throwable t) {
        sendMessage(log, message, variableData, t, ALERT_TYPE_DEBUG);
    }

    /**
     * Convenience method
     */
    public static void debug(Log log, Object message, Object variableData) {
        debug(log, message, variableData, null);
    }

    /**
     * Convenience method
     */
    public static void debug(Log log, Object message, Throwable t) {
        debug(log, message, null, t);
    }

    /**
     * Convenience method
     */
    public static void debug(Log log, Object message) {
        debug(log, message, null, null);
    }

    /**
     * These aren't so much alerts as they are messages that are sent
     * to customer support.
     */
    public static void customer(Log log, Object message, Object variableData, Throwable t) {
        sendMessage(log, message, variableData, t, ALERT_TYPE_CUSTOMER);
    }

    /**
     * Convenience method
     */
    public static void customer(Log log, Object message, Object variableData) {
        customer(log, message, variableData, null);
    }

    /**
     * Convenience method
     */
    public static void customer(Log log, Object message, Throwable t) {
        customer(log, message, null, t);
    }

    /**
     * Convenience method
     */
    public static void customer(Log log, Object message) {
        customer(log, message, null, null);
    }

    /**
     * Packages the message appropriately and sends to log at level error().
     * This format is interpreted by the CommonsBridgeLog.
     */
    protected static void sendMessage(Log log, Object message, Object variableData, Throwable t, String type) {
        StringBuffer buf = new StringBuffer();
        buf.append(ALERT_PREFIX);
        buf.append(type);
        buf.append(message.toString());

        if (variableData != null) {
            buf.append(VAR_TEXT_SEPARATOR);
            buf.append(variableData.toString());
        }

        if (t == null) {
            log.error(buf.toString());
        } else {
            log.error(buf.toString(), t);
        }
    }
}
