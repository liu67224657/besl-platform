package com.enjoyf.platform.service.social;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-6-5
 * Time: 上午9:50
 * To change this template use File | Settings | File Templates.
 */
public class SocialBlackType implements Serializable {
    public static final String ADD_BLACK = "1";//加入黑名单

    public static final String UN_BLACK = "0";//解除黑名单

    private static Map<String, SocialBlackType> map = new HashMap<String, SocialBlackType>();

    //
    private String code;

    public SocialBlackType(String c) {
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
        return "RelationType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof SocialBlackType)) {
            return false;
        }

        return code.equalsIgnoreCase(((SocialBlackType) obj).getCode());
    }

    public static SocialBlackType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<SocialBlackType> getAll() {
        return map.values();
    }
}
