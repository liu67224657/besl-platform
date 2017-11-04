package com.enjoyf.webapps.joyme.dto.joymeapp.socialclient;

import com.google.common.base.Strings;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-4-17
 * Time: 下午5:27
 * To change this template use File | Settings | File Templates.
 */
public class FocusType {

    private static Map<String, FocusType> map = new HashMap<String, FocusType>();

    //相互关注
    public static final FocusType ALL = new FocusType("2");

    //关注
    public static final FocusType FOLLOW = new FocusType("1");

    //没关注
    public static final FocusType UNFOCUS = new FocusType("0");

    private String code;

    public FocusType(String c) {
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
        if ((obj == null) || !(obj instanceof FocusType)) {
            return false;
        }

        return code.equalsIgnoreCase(((FocusType) obj).getCode());
    }

    public static FocusType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<FocusType> getAll() {
        return map.values();
    }
}
