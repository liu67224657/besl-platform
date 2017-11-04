package com.enjoyf.platform.webapps.common.dto.game;

import com.google.common.base.Strings;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-1-14
 * Time: 下午4:35
 * To change this template use File | Settings | File Templates.
 */
public class GameMenuType {
    private static Map<String, GameMenuType> map = new HashMap<String, GameMenuType>();

    public static final GameMenuType AP = new GameMenuType("ap");
    public static final GameMenuType DEF = new GameMenuType("def");

    //
    private String code;

    public GameMenuType(String c) {
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
        return "GameMenuType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof GameMenuType)) {
            return false;
        }

        return code.equalsIgnoreCase(((GameMenuType) obj).getCode());
    }

    public static GameMenuType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<GameMenuType> getAll() {
        return map.values();
    }
}
