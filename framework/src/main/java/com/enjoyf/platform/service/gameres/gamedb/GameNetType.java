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
public class GameNetType implements Serializable {
    private static Map<Integer, GameNetType> map = new HashMap<Integer, GameNetType>();
    public static final GameNetType DANJI = new GameNetType(1, "单机");
    public static final GameNetType WANGYOU = new GameNetType(2, "网游");

    private int code = 0;
    private String name;

    public GameNetType(int c, String n) {
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

    public static GameNetType getByCode(int code) {
        return map.get(code);
    }

    public static Collection<GameNetType> getAll() {
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
        if (code != ((GameNetType) o).code)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }
}
