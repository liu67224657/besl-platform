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
public class GameToolsFilter implements ViewLineItemFilter{
    private static Map<String, GameToolsFilter> map = new HashMap<String, GameToolsFilter>();

    private String code;

    private GameToolsFilter(String code) {
        this.code = code.toLowerCase();
        map.put(code, this);
    }

    public static GameToolsFilter getByCode(String code) {
        if (Strings.isNullOrEmpty(code)) {
            return null;
        }
        return map.get(code.toLowerCase());
    }

    public static Collection<GameToolsFilter> getAll() {
        return map.values();
    }

    public String getCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameToolsFilter that = (GameToolsFilter) o;

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
