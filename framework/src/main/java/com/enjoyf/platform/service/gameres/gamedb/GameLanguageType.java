package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.service.JsonBinder;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-20
 * Time: 下午4:50
 * To change this template use File | Settings | File Templates.
 */
public class GameLanguageType implements Serializable {
    private static Map<Integer, GameLanguageType> map = new HashMap<Integer, GameLanguageType>();
    public static final GameLanguageType CHINESE = new GameLanguageType(1, "中文");
    public static final GameLanguageType ENGLISH = new GameLanguageType(2, "英文");
    public static final GameLanguageType JAPANESE = new GameLanguageType(3, "日文");
    public static final GameLanguageType QITA = new GameLanguageType(4, "其他");

    private int code = 0;
    private String name;

    public GameLanguageType(int c, String n) {
        this.code = c;
        this.name = n;
        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    public String getName(){
        return name;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static GameLanguageType getByCode(int code) {
        return map.get(code);
    }

    public static Collection<GameLanguageType> getAll() {
        return map.values();
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass())
            return false;
        if (code != ((GameLanguageType) o).code)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }
}
