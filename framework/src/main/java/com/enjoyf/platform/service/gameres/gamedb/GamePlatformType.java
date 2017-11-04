package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.service.JsonBinder;
import net.sf.json.JSONObject;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-21 下午2:26
 * Description:
 */
public class GamePlatformType implements Serializable {
    private static Map<Integer, GamePlatformType> map = new HashMap<Integer, GamePlatformType>();

    public static final GamePlatformType DEFAULT = new GamePlatformType(0, "");
    public static final GamePlatformType MOBILE = new GamePlatformType(1, "手机游戏");
    public static final GamePlatformType PC = new GamePlatformType(2, "电脑游戏");
    public static final GamePlatformType PSP = new GamePlatformType(3, "掌机游戏");
    public static final GamePlatformType TV = new GamePlatformType(4, "电视游戏");

    private int code;
    private String desc;

    public GamePlatformType(int code, String desc) {
        this.code = code;
        this.desc = desc;
        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = JSONObject.fromObject(this);
        return jsonObject.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass())
            return false;
        if (code != ((GamePlatformType) o).code)
            return false;
        return true;
    }

    public static GamePlatformType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<GamePlatformType> getAll() {
        return map.values();
    }
}
