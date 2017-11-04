package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.service.JsonBinder;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-21 下午2:26
 * Description:
 */
public class TVPlatform extends GamePlatform implements Serializable {
    private static Map<Integer, TVPlatform> map = new HashMap<Integer, TVPlatform>();

    public static final TVPlatform XBOX360 = new TVPlatform(0, "XBOX360");
    public static final TVPlatform PS3 = new TVPlatform(1, "PS3");
    public static final TVPlatform PS4 = new TVPlatform(2, "PS4");
    public static final TVPlatform WII = new TVPlatform(3, "Wii");
    public static final TVPlatform WIIU = new TVPlatform(4, "WiiU");
    public static final TVPlatform XBOXONE = new TVPlatform(5, "XBOXONE");

    private int code;
    private String desc;

    public TVPlatform(int code, String desc) {
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
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass())
            return false;
        if (code != ((TVPlatform) o).code)
            return false;
        return true;
    }

    public static TVPlatform getByCode(int c) {
        return map.get(c);
    }

    public static Collection<TVPlatform> getAll() {
        return map.values();
    }
}
