package com.enjoyf.platform.service.shorturl;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
@SuppressWarnings("serial")
public class ShortUrlStatus implements Serializable {

    private static Map<String, ShortUrlStatus> map = new HashMap<String, ShortUrlStatus>();

    public static ShortUrlStatus VALID = new ShortUrlStatus("valid");
    public static ShortUrlStatus INVALID = new ShortUrlStatus("invalid");
    public static ShortUrlStatus BANNED = new ShortUrlStatus("banned");

    private String code;

    public ShortUrlStatus(String code) {
        this.code = code.toLowerCase();

        map.put(this.code, this);
    }

    public String getCode() {
        return code;
    }


    public static Map<String, ShortUrlStatus> getStatusMap() {
        return map;
    }

    public String toString() {
        return "ShortUrlStatus code:" + code;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof ShortUrlStatus) {
            return code.equalsIgnoreCase(((ShortUrlStatus) obj).getCode());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return code.hashCode();
    }

    public static ShortUrlStatus getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c);
    }

    public static Collection<ShortUrlStatus> getAll() {
        return map.values();
    }
}
