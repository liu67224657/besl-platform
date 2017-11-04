package com.enjoyf.platform.service.viewline;

import com.google.common.base.Strings;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-1-4
 * Time: 下午5:36
 * To change this template use File | Settings | File Templates.
 */
public class GameDeviceFilter implements ViewLineItemFilter{
    private static Map<String, GameDeviceFilter> map = new HashMap<String, GameDeviceFilter>();

    private String code;

    private GameDeviceFilter(String code) {
        this.code = code.toLowerCase();
        map.put(code, this);
    }

    public static GameDeviceFilter getByCode(String code) {
        if (Strings.isNullOrEmpty(code)) {
            return null;
        }
        return map.get(code.toLowerCase());
    }

    public static Collection<GameDeviceFilter> getAll() {
        return map.values();
    }

    public String getCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameDeviceFilter that = (GameDeviceFilter) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "GameDeviceFilter: code=" + code;
    }
}
