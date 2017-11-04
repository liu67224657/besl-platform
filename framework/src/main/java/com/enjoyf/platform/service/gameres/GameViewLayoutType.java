package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: ericliu
 * Date: 11-8-25
 * Time: 下午3:27
 * Desc: 用于表示游戏条目模板的类型由于要用于json对象的解析所以setCode和 默认构造方法为私有
 */
public class GameViewLayoutType implements Serializable {

    private static Map<String, GameViewLayoutType> map = new HashMap<String, GameViewLayoutType>();
    private String code;

    public static final GameViewLayoutType HEADICON = new GameViewLayoutType("headicon");
    public static final GameViewLayoutType GAMEDESC = new GameViewLayoutType("gamedesc");
    public static final GameViewLayoutType UPDATEINFO = new GameViewLayoutType("updateinfo");
    public static final GameViewLayoutType IMAGE = new GameViewLayoutType("image");
    public static final GameViewLayoutType VIDEO = new GameViewLayoutType("video");
    public static final GameViewLayoutType ARTICLE = new GameViewLayoutType("article");
    public static final GameViewLayoutType BAIKE = new GameViewLayoutType("baike");

    private GameViewLayoutType() {
    }

    public GameViewLayoutType(String c) {
        this.code = c.toLowerCase();
        map.put(code, this);
    }

    private void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static GameViewLayoutType getByCode(String code) {
        if (Strings.isNullOrEmpty(code)) {
            return null;
        }
        return map.get(code.toLowerCase());
    }

    public static Collection<GameViewLayoutType> getAll() {
        return map.values();
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
        if ((obj == null) || !(obj instanceof GameViewLayoutType)) {
            return false;
        }

        return code.equalsIgnoreCase(((GameViewLayoutType) obj).getCode());
    }

}
