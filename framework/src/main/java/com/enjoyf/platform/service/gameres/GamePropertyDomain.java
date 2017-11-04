package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-2-18
 * Time: 下午1:50
 * To change this template use File | Settings | File Templates.
 */
public class GamePropertyDomain implements Serializable {
    private static Map<String, GamePropertyDomain> map = new HashMap<String, GamePropertyDomain>();
    private String code;

    public static final GamePropertyDomain DOMAIN_CHANNEL = new GamePropertyDomain("channel");//获取渠道
    public static final GamePropertyDomain UPDATEINFO = new GamePropertyDomain("updateinfo");//最近更新

    public GamePropertyDomain(String c) {
        this.code = c.toLowerCase();
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public static GamePropertyDomain getByCode(String code) {
        return map.get(code.toLowerCase());
    }

    public static Collection<GamePropertyDomain> getAll() {
        return map.values();
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof GamePropertyDomain)) {
            return false;
        }

        return code.equalsIgnoreCase(((GamePropertyDomain) obj).getCode());
    }
}
