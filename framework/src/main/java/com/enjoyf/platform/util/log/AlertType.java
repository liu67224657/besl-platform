package com.enjoyf.platform.util.log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * An enum to define the various types of alerts in the system.
 */
@SuppressWarnings("serial")
public class AlertType implements java.io.Serializable {
    /**
     * Every site has an integer code which is also stored in the db.
     */
    private int type;

    /**
     * Every alert type has a String description.
     */
    private String name;

    private static Map<Integer, AlertType> alertTypesMap = new HashMap<Integer, AlertType>();

    /**
     * We don't have a valid alert type.
     */
    public static final AlertType UNKNOWN = new AlertType(0, "UNKNOWN");

    /**
     * Use this for alerts that happen occassionally, but aren't a
     * problem unless they continue happening.
     */
    public static final AlertType NOISE = new AlertType(1, "NOISE");

    /**
     * These are probably due to some programming bug or config bug.
     * Sometimes other causes can end up generating these alerts (eg, a
     * user messing around with the url).
     */
    public static final AlertType BUG = new AlertType(2, "BUG");

    /**
     * These aren't so much alerts as they are messages that can be
     * used to debug various conditions.
     */
    public static final AlertType DEBUG = new AlertType(3, "DEBUG");

    /**
     * The alert needs to be send out to some one.
     */
    public static final AlertType EMAIL = new AlertType(9, "EMAIL");

    public static final AlertType CLIENT_BUG = new AlertType(10, "CLIENT_BUG");

    public static final AlertType TIMER_DEBUG = new AlertType(11, "TIMER_DEBUG");


    private AlertType(int code, String sval) {
        type = code;
        name = sval;

        alertTypesMap.put(type, this);
    }

    public static Iterator<AlertType> iterator() {
        return alertTypesMap.values().iterator();
    }

    public int hashCode() {
        return type;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof AlertType)) {
            return false;
        }

        AlertType type = (AlertType) obj;

        return this.type == type.type;
    }

    public String toString() {
        return name;
    }
}
