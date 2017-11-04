/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Garrison
 */
public class DataBaseType {
    private static Map<String, DataBaseType> typeMap = new HashMap<String, DataBaseType>();

    private static String TYPE_NAME_SQLSERVER = "sqlserver";
    private static String TYPE_NAME_ORACLE = "oracle";
    private static String TYPE_NAME_MYSQL = "mysql";
    private static String TYPE_NAME_CASSANDRA = "cassandra";

    public static DataBaseType DB_TYPE_SQLSERVER = new DataBaseType(TYPE_NAME_SQLSERVER, "SqlServer");
    public static DataBaseType DB_TYPE_ORACLE = new DataBaseType(TYPE_NAME_ORACLE, "Oracle");
    public static DataBaseType DB_TYPE_MYSQL = new DataBaseType(TYPE_NAME_MYSQL, "MySql");
    public static DataBaseType DB_TYPE_CASSANDRA = new DataBaseType(TYPE_NAME_CASSANDRA, "Cassandra");

    private String name;
    private String suffix;

    private DataBaseType(String name, String suffix) {
        this.name = name.toLowerCase();
        this.suffix = suffix;

        typeMap.put(this.name, this);
    }

    public static DataBaseType getDbType(String name) throws DbTypeException {
        DataBaseType dataBaseType = null;

        dataBaseType = typeMap.get(name);

        if (dataBaseType == null) {
            throw new DbTypeException(DbTypeException.DBTYPE_NOT_SUPPORT, "The type of database, " + name + ", is not supported.");
        }

        return dataBaseType;
    }

    public String getName() {
        return name;
    }

    public String getSuffix() {
        return suffix;
    }

    public String toString() {
        return "DataBaseType: name=" + name + ", suffix=" + suffix;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof DataBaseType) {
            return name.equalsIgnoreCase(((DataBaseType) obj).getName());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return name.hashCode();
    }
}
