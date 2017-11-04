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
public class GamePicType implements Serializable {
    private static Map<Integer, GamePicType> map = new HashMap<Integer, GamePicType>();
    public static final GamePicType VERTICAL = new GamePicType(1, "不旋转");
    public static final GamePicType TRANSVERSE = new GamePicType(0, "旋转");

    private int code = 0;
    private String name;

    public GamePicType(int c, String n) {
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

    public static GamePicType getByCode(int code) {
        return map.get(code);
    }

    public static Collection<GamePicType> getAll() {
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
        if (code != ((GamePicType) o).code)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }
}
