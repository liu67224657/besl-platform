package com.enjoyf.platform.webapps.common.dto.game;

import com.google.common.base.Strings;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-11-20
 * Time: 下午4:57
 * To change this template use File | Settings | File Templates.
 */
public class GameDisplayType {
    private static Map<String, GameDisplayType> map = new LinkedHashMap<String, GameDisplayType>();

    public static final GameDisplayType LIST = new GameDisplayType("list");
    public static final GameDisplayType IMAGE = new GameDisplayType("image");
    public static final GameDisplayType VIDEO = new GameDisplayType("video");

    //
    private String code;

    public GameDisplayType(String c) {
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
        return "GameDisplayType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof GameDisplayType)) {
            return false;
        }

        return code.equalsIgnoreCase(((GameDisplayType) obj).getCode());
    }

    public static GameDisplayType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<GameDisplayType> getAll() {
        return map.values();
    }
}
