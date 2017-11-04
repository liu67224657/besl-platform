package com.enjoyf.platform.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
@SuppressWarnings("serial")
@JsonIgnoreProperties(value = {"map","VALID","INVALID","REMOVED"})
public class ValidStatus implements Serializable {

    private static Map<String, ValidStatus> map = new HashMap<String, ValidStatus>();

    public static ValidStatus VALID = new ValidStatus("valid");
    public static ValidStatus INVALID = new ValidStatus("invalid");
    public static ValidStatus REMOVED = new ValidStatus("removed");

    private String code;

    public ValidStatus() {
    }

    public ValidStatus(String code) {
        this.code = code.toLowerCase();

        map.put(this.code, this);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static Map<String, ValidStatus> getStatusMap() {
        return map;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof ValidStatus) {
            return code.equalsIgnoreCase(((ValidStatus) obj).getCode());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return code.hashCode();
    }

    public static ValidStatus getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c);
    }

    public static Collection<ValidStatus> getAll() {
        return map.values();
    }
}
