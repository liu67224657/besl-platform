package com.enjoyf.platform.service.gameres;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-11-20
 * Time: 下午2:36
 * To change this template use File | Settings | File Templates.
 */
public class GamePropertyValueType implements Serializable {

    private static Map<Integer, GamePropertyValueType> map = new HashMap<Integer, GamePropertyValueType>();
    //value 为 null
    public static final GamePropertyValueType DEFAULT = new GamePropertyValueType(0);
    //
    public static final GamePropertyValueType INTEGER = new GamePropertyValueType(1);
    //
    public static final GamePropertyValueType STRING = new GamePropertyValueType(2);
    //
    public static final GamePropertyValueType DATE = new GamePropertyValueType(3);
    //
    public static final GamePropertyValueType BOOLEAN = new GamePropertyValueType(4);
    //
    private int value;

    public GamePropertyValueType() {

    }

    public GamePropertyValueType(int v) {
        this.value = v;
        map.put(v, this);
    }

    public int getValue() {
        return value;
    }

    public static GamePropertyValueType getByValue(int v) {
        return new GamePropertyValueType(v);
    }

    public static Collection<GamePropertyValueType> getAll() {
        return map.values();
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass())
            return false;
        if (value != ((GamePropertyValueType) o).value)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GamePropertyValueType.value:" + value;
    }
}
