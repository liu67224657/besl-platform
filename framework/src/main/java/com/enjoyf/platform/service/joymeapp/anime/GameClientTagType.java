package com.enjoyf.platform.service.joymeapp.anime;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 手游画报标签类型 0-默认标签 1-web标签
 * Created by zhimingli on 2016/7/7 0007.
 */
public class GameClientTagType implements Serializable {

    private static Map<String, GameClientTagType> map = new HashMap<String, GameClientTagType>();

    public static GameClientTagType DEFAULT = new GameClientTagType("0");
    public static GameClientTagType WEB = new GameClientTagType("1");
    public static GameClientTagType ACTIVITY = new GameClientTagType("2");

    private String code;

    public GameClientTagType() {
    }

    public GameClientTagType(String code) {
        this.code = code.toLowerCase();

        map.put(this.code, this);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static Map<String, GameClientTagType> getStatusMap() {
        return map;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof GameClientTagType) {
            return code.equalsIgnoreCase(((GameClientTagType) obj).getCode());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return code.hashCode();
    }

    public static GameClientTagType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c);
    }

    public static Collection<GameClientTagType> getAll() {
        return map.values();
    }
}
