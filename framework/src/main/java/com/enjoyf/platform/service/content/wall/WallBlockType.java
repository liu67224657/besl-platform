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
 * Time: 下午2:11
 * To change this template use File | Settings | File Templates.
 */
public class WallBlockType implements Serializable {
    private static Map<String, WallBlockType> map = new HashMap<String, WallBlockType>();

    public static final WallBlockType BLOCK_A = new WallBlockType("block_a");
    public static final WallBlockType BLOCK_B = new WallBlockType("block_b");
    public static final WallBlockType BLOCK_C = new WallBlockType("block_c");
    public static final WallBlockType BLOCK_VIDEO = new WallBlockType("block_video");


    private String code;

    public WallBlockType(String c) {
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
        return "WallBlockType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof WallBlockType)) {
            return false;
        }

        return code.equalsIgnoreCase(((WallBlockType) obj).getCode());
    }

    public static WallBlockType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<WallBlockType> getAll() {
        return map.values();
    }

}
