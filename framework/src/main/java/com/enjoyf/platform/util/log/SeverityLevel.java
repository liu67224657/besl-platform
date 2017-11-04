/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.log;

import java.util.HashMap;
import java.util.Map;

/**
 * Use slf4j, this class sucks
 */
@Deprecated
public class SeverityLevel {
    private static Map<Integer, SeverityLevel> severitiesByLevel = new HashMap<Integer, SeverityLevel>(7);
    private static Map<String, SeverityLevel> severitiesByName = new HashMap<String, SeverityLevel>(7);

    //public static members
    public static int LEVEL_NONE = 0;
    public static int LEVEL_ALERT = 1;
    public static int LEVEL_HIGH = 2;
    public static int LEVEL_MEDIUM = 3;
    public static int LEVEL_LOW = 4;
    public static int LEVEL_DEBUG = 5;
    public static int LEVEL_INFO = 6;

    public static SeverityLevel NONE = new SeverityLevel(LEVEL_NONE, "NONE", "None security");
    public static SeverityLevel ALERT = new SeverityLevel(LEVEL_ALERT, "ALERT", "Alert level");
    public static SeverityLevel HIGH = new SeverityLevel(LEVEL_HIGH, "HIGH", "High level");
    public static SeverityLevel MEDIUM = new SeverityLevel(LEVEL_MEDIUM, "MEDIUM", "Medium level");
    public static SeverityLevel LOW = new SeverityLevel(LEVEL_LOW, "LOW", "Low level");
    public static SeverityLevel DEBUG = new SeverityLevel(LEVEL_DEBUG, "DEBUG", "Debug level");
    public static SeverityLevel INFO = new SeverityLevel(LEVEL_INFO, "INFO", "Info level");

    //private members
    private int level;
    private String name;
    private String description;

    private SeverityLevel(int l, String n, String d) {
        level = l;
        name = n.toUpperCase();
        description = d;

        severitiesByLevel.put(level, this);
        severitiesByName.put(name, this);
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static SeverityLevel getByLevel(int l) {
        SeverityLevel severityLevel = severitiesByLevel.get(l);

        if (severityLevel == null) {
            severityLevel = NONE;
        }

        return severityLevel;
    }

    public static SeverityLevel getByName(String n) {
        SeverityLevel severityLevel = severitiesByName.get(n.toUpperCase());

        if (severityLevel == null) {
            severityLevel = NONE;
        }

        return severityLevel;
    }
}
