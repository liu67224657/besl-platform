package com.enjoyf.platform.service.misc;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-14
 * Time: 下午5:12
 * To change this template use File | Settings | File Templates.
 */
public class GamePropValueType implements Serializable {
    private static Map<Integer, GamePropValueType> map = new HashMap<Integer, GamePropValueType>();

    public static final GamePropValueType STRING_VALUE = new GamePropValueType(1);
    public static final GamePropValueType NUM_VALUE = new GamePropValueType(2);
    public static final GamePropValueType DATE_VALUE = new GamePropValueType(3);

    private Integer code;

    private GamePropValueType(int i) {
        code = i;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public String toString() {
        return "GamePropValueType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof GamePropValueType)) {
            return false;
        }

        return code == ((GamePropValueType) obj).getCode();
    }

    public static GamePropValueType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<GamePropValueType> getAll() {
        return map.values();
    }
}
