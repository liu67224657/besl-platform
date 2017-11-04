/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.advertise;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-6-25 下午5:51
 * Description:
 */
public class AdvertiseEventType implements Serializable {
    //
    private static Map<String, AdvertiseEventType> map = new HashMap<String, AdvertiseEventType>();

    //user click the url from the agent web site
    public static final AdvertiseEventType CLICK = new AdvertiseEventType("click");

    //read the page in our website.
    public static final AdvertiseEventType VIEW = new AdvertiseEventType("view");

    //
    public static final AdvertiseEventType POST = new AdvertiseEventType("post");
    public static final AdvertiseEventType REPLY = new AdvertiseEventType("reply");
    public static final AdvertiseEventType LOGIN = new AdvertiseEventType("login");

    //
    private String code;

    public AdvertiseEventType(String c) {
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
        return "AdvertiseEventType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof AdvertiseEventType)) {
            return false;
        }

        return code.equalsIgnoreCase(((AdvertiseEventType) obj).getCode());
    }

    public static AdvertiseEventType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<AdvertiseEventType> getAll() {
        return map.values();
    }
}
