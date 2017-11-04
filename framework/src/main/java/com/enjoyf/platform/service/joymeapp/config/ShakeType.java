package com.enjoyf.platform.service.joymeapp.config;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/3/19
 * Description:
 */
public class ShakeType implements Serializable {
    private static Map<Integer, ShakeType> map = new HashMap<Integer, ShakeType>();

    public static final ShakeType GAME = new ShakeType(1, "游戏");
    public static final ShakeType POINT = new ShakeType(2, "礼包");


    private int code;
    private String value;

    private ShakeType(int c, String value) {
        this.code = c;
        this.value = value;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ShakeType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((ShakeType) o).code) return false;

        return true;
    }

    public static Collection<ShakeType> getAll() {
        return map.values();
    }

    public static ShakeType getByCode(int c) {
        return map.get(c);
    }

}
