package com.enjoyf.platform.service.gameres.gamedb;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/12/26
 * Description:
 */
public class RedMessageType implements Serializable {
    private static Map<Integer, RedMessageType> map = new HashMap<Integer, RedMessageType>();
    public static final RedMessageType DEFAULT = new RedMessageType(0); //默认
    public static final RedMessageType LITTLE_RED_DOT = new RedMessageType(1); //小红点
    public static final RedMessageType NUMBER = new RedMessageType(2);//数字
    public static final RedMessageType WORD = new RedMessageType(3);//自定义汉字
    public static final RedMessageType GIFT = new RedMessageType(7);//礼
    public static final RedMessageType LIVE = new RedMessageType(6);//活
    public static final RedMessageType NEW = new RedMessageType(5);//新
    public static final RedMessageType ATTACK = new RedMessageType(4);//攻

    private int code;

    public RedMessageType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "RedMessageType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((RedMessageType) o).code) return false;

        return true;
    }

    public static RedMessageType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<RedMessageType> getAll() {
        return map.values();
    }

}
