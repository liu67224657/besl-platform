package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: ericliu
 * Date: 11-8-25
 * Time: 下午3:27
 * Desc: 游戏条目类型 游戏类型 话题类型
 */
public class ResourceDomain implements Serializable {

    private static Map<String, ResourceDomain> map = new HashMap<String, ResourceDomain>();
    private String code;

    public static final ResourceDomain GAME = new ResourceDomain("game");//
    public static final ResourceDomain GROUP = new ResourceDomain("group");//
    public static final ResourceDomain WIKI = new ResourceDomain("wiki");//

    public ResourceDomain(String c) {
        this.code = c.toLowerCase();
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public static ResourceDomain getByCode(String code) {
        if (Strings.isNullOrEmpty(code)) {
            return null;
        }
        return map.get(code.toLowerCase());
    }

    public static Collection<ResourceDomain> getAll() {
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
        if ((obj == null) || !(obj instanceof ResourceDomain)) {
            return false;
        }

        return code.equalsIgnoreCase(((ResourceDomain) obj).getCode());
    }

}
