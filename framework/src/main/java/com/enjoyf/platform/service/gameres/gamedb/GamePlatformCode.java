package com.enjoyf.platform.service.gameres.gamedb;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-21 下午2:26
 * Description:
 */
public class GamePlatformCode implements Serializable {
      private static Map<Integer, GamePlatformCode> map = new HashMap<Integer, GamePlatformCode>();

    public static final GamePlatformCode IOS = new GamePlatformCode(0);
    public static final GamePlatformCode ANDROID = new GamePlatformCode(1);


    private int code;

    public GamePlatformCode(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "GamePlatform: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if (code != ((GamePlatformCode) o).code) return false;

        return true;
    }

    public static GamePlatformCode getByCode(int c) {
        return map.get(c);
    }

    public static Collection<GamePlatformCode> getAll() {
        return map.values();
    }
}
