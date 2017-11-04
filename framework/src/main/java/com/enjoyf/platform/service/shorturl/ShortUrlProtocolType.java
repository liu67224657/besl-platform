/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.shorturl;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class ShortUrlProtocolType implements Serializable {
    private static Map<String, ShortUrlProtocolType> map = new HashMap<String, ShortUrlProtocolType>();

    //
    public static final ShortUrlProtocolType HTTP = new ShortUrlProtocolType("http");
    public static final ShortUrlProtocolType HTTPS = new ShortUrlProtocolType("https");

    //电驴
    public static final ShortUrlProtocolType ED2K = new ShortUrlProtocolType("ed2k");

    //
    private String code;

    public ShortUrlProtocolType(String c) {
        code = c.toLowerCase();

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "ShortUrlProtocolType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ShortUrlProtocolType)) {
            return false;
        }

        return code.equalsIgnoreCase(((ShortUrlProtocolType) obj).getCode());
    }

    public static ShortUrlProtocolType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ShortUrlProtocolType> getAll() {
        return map.values();
    }
}
