package com.enjoyf.platform.db.conn;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Strings;

/**
 * Shanghai operation platform of platform.com
 *
 * @author Yin Pengyi
 */
public class ConnPoolType {
	
    private static Map<String, ConnPoolType> typeMap = new HashMap<String, ConnPoolType>();

    public static String TYPE_NAME_BONECP = "bonecp";
    public static String TYPE_NAME_DBCP = "dbcp";
    public static String TYPE_NAME_C3P0 = "c3p0";
   public static String TYPE_NAME_PROXOOL = "proxool";

    public static ConnPoolType CONN_POOL_TYPE_BONECP = new ConnPoolType(TYPE_NAME_BONECP);
    public static ConnPoolType CONN_POOL_TYPE_DBCP = new ConnPoolType(TYPE_NAME_DBCP);
    public static ConnPoolType CONN_POOL_TYPE_C3P0 = new ConnPoolType(TYPE_NAME_C3P0);
    public static ConnPoolType CONN_POOL_TYPE_PROXOOL = new ConnPoolType(TYPE_NAME_PROXOOL);

    private String name;

    private ConnPoolType(String n) {
        name = n.toLowerCase();

        typeMap.put(name.toLowerCase(), this);
    }

    public static ConnPoolType getConnPoolType(String name) {
        if (Strings.isNullOrEmpty(name)) {
            return CONN_POOL_TYPE_DBCP;
        }

        ConnPoolType connPoolType = typeMap.get(name);

        if (connPoolType == null) {
            connPoolType = CONN_POOL_TYPE_DBCP;
        }

        return connPoolType;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ConnPoolType)) {
            return false;
        }

        return name.equals(((ConnPoolType) obj).getName());
    }

    public int hashCode() {
        return name.hashCode();
    }
}
