package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 游戏权限对象
 * User: ericliu
 * Date: 13-4-25
 * Time: 上午9:16
 * To change this template use File | Settings | File Templates.
 */
public class GamePrivacyLevel implements Serializable {
    //
    private static Map<String, GamePrivacyLevel> map = new LinkedHashMap<String, GamePrivacyLevel>();

    //分类
    public static final GamePrivacyLevel ADMIN = new GamePrivacyLevel("admin");

    //
    private String code;

    private GamePrivacyLevel(String c) {
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
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        return !((obj == null) || !(obj instanceof GamePrivacyLevel)) && code.equalsIgnoreCase(((GamePrivacyLevel) obj).getCode());
    }

    public static GamePrivacyLevel getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<GamePrivacyLevel> getAll() {
        return map.values();
    }

}
