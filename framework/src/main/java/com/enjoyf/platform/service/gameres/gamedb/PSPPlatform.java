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
public class PSPPlatform extends GamePlatform implements Serializable {
    private static Map<Integer, PSPPlatform> map = new HashMap<Integer, PSPPlatform>();

    public static final PSPPlatform PSP = new PSPPlatform(0, "PSP");
    public static final PSPPlatform PSV = new PSPPlatform(1, "PSV");
    public static final PSPPlatform NDS = new PSPPlatform(2, "NDS");
    public static final PSPPlatform DS3 = new PSPPlatform(3, "3DS");

    private int code;
    private String desc;

    public PSPPlatform(int code, String desc) {
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
        if (code != ((PSPPlatform) o).code)
            return false;
        return true;
    }

    public static PSPPlatform getByCode(int c) {
        return map.get(c);
    }

    public static Collection<PSPPlatform> getAll() {
        return map.values();
    }
}
