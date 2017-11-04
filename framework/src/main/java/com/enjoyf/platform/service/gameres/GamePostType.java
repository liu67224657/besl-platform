package com.enjoyf.platform.service.gameres;

import com.google.common.base.Strings;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-11-20
 * Time: 下午4:57
 * To change this template use File | Settings | File Templates.
 */
public class GamePostType {
    private static Map<String, GamePostType> map = new LinkedHashMap<String, GamePostType>();

    public static final GamePostType HANDBOOK = new GamePostType("handbook");
    public static final GamePostType NEWS = new GamePostType("news");
    public static final GamePostType IMAGE = new GamePostType("image");
    public static final GamePostType VIDEO = new GamePostType("video");
    public static final GamePostType REVIEW = new GamePostType("review");
    public static final GamePostType NOTE = new GamePostType("note");

    //
    private String code;

    public GamePostType(String c) {
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
        return "GamePostType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof GamePostType)) {
            return false;
        }

        return code.equalsIgnoreCase(((GamePostType) obj).getCode());
    }

    public static GamePostType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<GamePostType> getAll() {
        return map.values();
    }
}
