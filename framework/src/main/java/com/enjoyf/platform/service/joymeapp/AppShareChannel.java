package com.enjoyf.platform.service.joymeapp;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-27
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
public class AppShareChannel implements Serializable {
    private static Map<String, AppShareChannel> map = new HashMap<String, AppShareChannel>();

	public static final AppShareChannel DEFAULT = new AppShareChannel("def");

    public static final AppShareChannel SINAWEIBO = new AppShareChannel("sinaweibo");
    public static final AppShareChannel QQ = new AppShareChannel("qq");
    public static final AppShareChannel QQPLUS = new AppShareChannel("qqplus");
    public static final AppShareChannel QQWEIBO = new AppShareChannel("qqweibo");
    public static final AppShareChannel WEIXIN = new AppShareChannel("weixin");
    public static final AppShareChannel WEIXINPENGYOUQUAN = new AppShareChannel("weixinpengyouquan");


    private String code;

    public AppShareChannel(String c) {
        code = c.toLowerCase();
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "AppPlatform: code=" + code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return code.equalsIgnoreCase(((AppShareChannel) o).getCode());
    }

    public static AppShareChannel getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }
        return map.get(c.toLowerCase());
    }

    public static Collection<AppShareChannel> getAll() {
        return map.values();
    }
}
