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
public class GameDBRelationType implements Serializable {
    private static Map<Integer, GameDBRelationType> map = new HashMap<Integer, GameDBRelationType>();
    public static final GameDBRelationType NEWS = new GameDBRelationType(1);
    public static final GameDBRelationType DETAIL = new GameDBRelationType(2);
    public static final GameDBRelationType GIFT = new GameDBRelationType(3);
    public static final GameDBRelationType LINK = new GameDBRelationType(4);

    private int code;

    public GameDBRelationType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "GameDbRelationType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((GameDBRelationType) o).code) return false;

        return true;
    }

    public static GameDBRelationType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<GameDBRelationType> getAll() {
        return map.values();
    }

}
