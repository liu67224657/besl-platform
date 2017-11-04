package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-11 上午10:01
 * Description:
 */
public class ClientLineFlagType implements Serializable {
    private static Map<Integer, ClientLineFlagType> map = new HashMap<Integer, ClientLineFlagType>();
    public static final ClientLineFlagType CLIENT_LINE = new ClientLineFlagType(1);
    public static final ClientLineFlagType TOP_MENU = new ClientLineFlagType(2);

    private int code;

    private ClientLineFlagType(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ClientLineFlagType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((ClientLineFlagType) o).code) return false;

        return true;
    }

    public static ClientLineFlagType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ClientLineFlagType> getAll() {
        return map.values();
    }
}
