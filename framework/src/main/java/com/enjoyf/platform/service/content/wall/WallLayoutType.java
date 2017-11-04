package com.enjoyf.platform.service.content.wall;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-3-8
 * Time: 下午2:08
 * To change this template use File | Settings | File Templates.
 */
public class WallLayoutType implements Serializable {
    private static Map<String, WallLayoutType> map = new HashMap<String, WallLayoutType>();

    public static final WallLayoutType LAYOUT1 = new WallLayoutType("layout1");
    public static final WallLayoutType LAYOUT2 = new WallLayoutType("layout2");
    public static final WallLayoutType MVLAYOUT = new WallLayoutType("mvlayout");

    private String code;

    public WallLayoutType(String c) {
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
        return "WallLayoutType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof WallBlockType)) {
            return false;
        }

        return code.equalsIgnoreCase(((WallBlockType) obj).getCode());
    }

    public static WallLayoutType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<WallLayoutType> getAll() {
        return map.values();
    }

}
