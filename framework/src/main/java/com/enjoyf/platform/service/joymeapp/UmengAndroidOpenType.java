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
public class UmengAndroidOpenType implements Serializable {

    private static Map<Integer, UmengAndroidOpenType> map = new HashMap<Integer, UmengAndroidOpenType>();

    //打开应用
    public static final UmengAndroidOpenType APP = new UmengAndroidOpenType(0);
    //打开activity
    public static final UmengAndroidOpenType ACTIVITY = new UmengAndroidOpenType(1);
    //打开url
    public static final UmengAndroidOpenType URL = new UmengAndroidOpenType(2);
    //自定义
    public static final UmengAndroidOpenType CUSTOM = new UmengAndroidOpenType(3);

    private int code;

    private UmengAndroidOpenType(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "AppDisplayType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((UmengAndroidOpenType) o).code) return false;

        return true;
    }

    public static UmengAndroidOpenType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<UmengAndroidOpenType> getAll() {
        return map.values();
    }
}
