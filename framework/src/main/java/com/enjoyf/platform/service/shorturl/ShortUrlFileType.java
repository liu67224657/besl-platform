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
public class ShortUrlFileType implements Serializable {
    private static Map<String, ShortUrlFileType> map = new HashMap<String, ShortUrlFileType>();

    //
    public static final ShortUrlFileType HTML = new ShortUrlFileType("html");
    public static final ShortUrlFileType IMAGE = new ShortUrlFileType("image");
    public static final ShortUrlFileType SWF = new ShortUrlFileType("SWF");
    public static final ShortUrlFileType DOWNLOAD = new ShortUrlFileType("download");


    //
    private String code;

    public ShortUrlFileType(String c) {
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
        if ((obj == null) || !(obj instanceof ShortUrlFileType)) {
            return false;
        }

        return code.equalsIgnoreCase(((ShortUrlFileType) obj).getCode());
    }

    public static ShortUrlFileType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ShortUrlFileType> getAll() {
        return map.values();
    }
}
