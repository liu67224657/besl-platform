package com.enjoyf.platform.service.gameres.gamedb;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
@SuppressWarnings("serial")
public class GameDbStatus implements Serializable {

    private static Map<String, GameDbStatus> map = new HashMap<String, GameDbStatus>();

    public static GameDbStatus VALID = new GameDbStatus("valid");
    public static GameDbStatus INVALID = new GameDbStatus("invalid");
    public static GameDbStatus NOTINVALID = new GameDbStatus("notvalid");
    public static GameDbStatus REMOVED = new GameDbStatus("removed");

    private String code;

    public GameDbStatus(String code) {
        this.code = code.toLowerCase();

        map.put(this.code, this);
    }

    public String getCode() {
        return code;
    }


    public static Map<String, GameDbStatus> getStatusMap() {
        return map;
    }

    public String toString() {
        return "ValidStatus code:" + code;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof GameDbStatus) {
            return code.equalsIgnoreCase(((GameDbStatus) obj).getCode());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return code.hashCode();
    }

    public static GameDbStatus getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c);
    }

    public static Collection<GameDbStatus> getAll() {
        return map.values();
    }
}
