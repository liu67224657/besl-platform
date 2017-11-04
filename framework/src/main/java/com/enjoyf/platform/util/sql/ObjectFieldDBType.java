package com.enjoyf.platform.util.sql;

import com.enjoyf.platform.util.StringUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
@SuppressWarnings("serial")
public class ObjectFieldDBType implements Serializable {

    private static Map<String, ObjectFieldDBType> map = new HashMap<String, ObjectFieldDBType>();

    public static final ObjectFieldDBType STRING = new ObjectFieldDBType("string");
    public static final ObjectFieldDBType LONG = new ObjectFieldDBType("long");
    public static final ObjectFieldDBType INT = new ObjectFieldDBType("int");
    public static final ObjectFieldDBType BOOLEAN = new ObjectFieldDBType("boolean");
    public static final ObjectFieldDBType TIMESTAMP = new ObjectFieldDBType("timestamp");
    public static final ObjectFieldDBType DATE = new ObjectFieldDBType("date");
    public static final ObjectFieldDBType FLOAT = new ObjectFieldDBType("float");
    public static final ObjectFieldDBType DOUBLE = new ObjectFieldDBType("double");

    private String code;

    private ObjectFieldDBType(String c) {
        code = c.toLowerCase();

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int hashCode() {
        return code.hashCode();
    }

    public String toString() {
        return "QueryFieldType: code=" + code;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ObjectFieldDBType)) {
            return false;
        }

        return code.equalsIgnoreCase(((ObjectFieldDBType) obj).getCode());
    }

    public static ObjectFieldDBType getByCode(String c) {
        if (StringUtil.isEmpty(c)) {
            return null;
        }

        ObjectFieldDBType type = map.get(c.toLowerCase());
        return type;
    }

}
