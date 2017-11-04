package com.enjoyf.platform.service.joymeapp;

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
public class AppPlatform implements Serializable {
    private static Map<Integer, AppPlatform> map = new HashMap<Integer, AppPlatform>();

    public static final AppPlatform IOS = new AppPlatform(0);
    public static final AppPlatform ANDROID = new AppPlatform(1);
    public static final AppPlatform WEB = new AppPlatform(2);  //pc          推送时，表示ios 企业版
    public static final AppPlatform CLIENT = new AppPlatform(3);    //在礼包中心表示没有选择平台

    public static final AppPlatform ALL = new AppPlatform(4); //礼包中心表示平台是IOS和安卓双平台


    private int code;

    public AppPlatform(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "AppPlatform: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AppPlatform) o).code) return false;

        return true;
    }

    public static AppPlatform getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AppPlatform> getAll() {
        return map.values();
    }
}
