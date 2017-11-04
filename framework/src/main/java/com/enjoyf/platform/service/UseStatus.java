package com.enjoyf.platform.service;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
@SuppressWarnings("serial")
public class UseStatus implements Serializable {
    private static Map<String, UseStatus> map = new HashMap<String, UseStatus>();

    public static UseStatus INIT = new UseStatus("init");
    public static UseStatus UNUSE = new UseStatus("unuse");
    public static UseStatus USED = new UseStatus("used");

    private String code;

    public UseStatus(String code) {
        this.code = code.toLowerCase();

        map.put(this.code, this);
    }

    public String getCode() {
        return code;
    }


    public static Map<String, UseStatus> getStatusMap() {
        return map;
    }

    public String toString() {
        return "UseStatus code:" + code;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof UseStatus) {
            return code.equalsIgnoreCase(((UseStatus) obj).getCode());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return code.hashCode();
    }

    public static UseStatus getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c);
    }

    public static Collection<UseStatus> getAll() {
        return map.values();
    }
}
