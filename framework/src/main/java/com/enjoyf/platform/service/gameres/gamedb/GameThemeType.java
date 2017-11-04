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
public class GameThemeType implements Serializable {
    private static Map<Integer, GameThemeType> map = new HashMap<Integer, GameThemeType>();

    public static final GameThemeType LISHI = new GameThemeType(1, "历史");
    public static final GameThemeType WUXIA = new GameThemeType(2, "武侠");
    public static final GameThemeType QIHUAN = new GameThemeType(3, "奇幻");
    public static final GameThemeType XIANDAI = new GameThemeType(4, "现代");
    public static final GameThemeType KEHUAN = new GameThemeType(5, "科幻");
    public static final GameThemeType DONGMAN = new GameThemeType(6, "动漫");
    public static final GameThemeType ZHANZHENG = new GameThemeType(7, "战争");
    public static final GameThemeType SANGUO = new GameThemeType(8, "三国");
    public static final GameThemeType QITA = new GameThemeType(9, "其他");

    private int code = 0;
    private String name;

    public GameThemeType(int c, String n) {
        this.code = c;
        this.name = n;
        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static GameThemeType getByCode(int code) {
        return map.get(code);
    }

    public static Collection<GameThemeType> getAll() {
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
        if (code != ((GameThemeType) o).code)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }
}
