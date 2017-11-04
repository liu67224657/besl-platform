package com.enjoyf.platform.service.gameres.gamedb;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.util.CollectionUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-11-21 下午2:26
 * Description:
 */
public class PCPlatform extends GamePlatform implements Serializable {
    private static Map<Integer, PCPlatform> map = new HashMap<Integer, PCPlatform>();

    public static final PCPlatform PC = new PCPlatform(0, "PC");

    private int code;
    private String desc;

    public PCPlatform(int code, String desc) {
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
        if (code != ((PCPlatform) o).code)
            return false;
        return true;
    }

    public static PCPlatform getByCode(int c) {
        return map.get(c);
    }

    public static Collection<PCPlatform> getAll() {
        return map.values();
    }
}
