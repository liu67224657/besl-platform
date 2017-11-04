package com.enjoyf.platform.service.content.wall;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-3-13
 * Time: 下午2:05
 * To change this template use File | Settings | File Templates.
 */
public class WallContentType implements Serializable {
    private static Map<String, WallContentType> map = new HashMap<String, WallContentType>();

    public static final WallContentType TEXT = new WallContentType("text");
    public static final WallContentType IMAGE_H = new WallContentType("image_h");
    public static final WallContentType IMAGE_V = new WallContentType("image_v");
    public static final WallContentType VIDEO = new WallContentType("video");
    public static final WallContentType AUDIO = new WallContentType("audio");
    public static final WallContentType APP = new WallContentType("app");

    private String code;

    public WallContentType(String c) {
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
        return "WallContentType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof WallContentType)) {
            return false;
        }

        return code.equalsIgnoreCase(((WallContentType) obj).getCode());
    }

    public static WallContentType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<WallContentType> getAll() {
        return map.values();
    }
}
