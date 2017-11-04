/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

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
public class InteractionType implements Serializable {
    private static Map<String, InteractionType> map = new HashMap<String, InteractionType>();

    public static InteractionType REPLY = new InteractionType("reply");
    public static InteractionType FAVORITE = new InteractionType("fav");

    private String code;

    private InteractionType(String c) {
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
        return "InteractionType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof InteractionType)) {
            return false;
        }

        return code.equalsIgnoreCase(((InteractionType) obj).getCode());
    }

    public static InteractionType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<InteractionType> getAll() {
        return map.values();
    }
}
