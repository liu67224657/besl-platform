package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-18
 * Time: 上午10:23
 * To change this template use File | Settings | File Templates.
 */
public class JoymeAppMenuDisplayType implements Serializable {
    private static Map<Integer, JoymeAppMenuDisplayType> map = new HashMap<Integer, JoymeAppMenuDisplayType>();

    public static final JoymeAppMenuDisplayType DEFAULT = new JoymeAppMenuDisplayType(0);   //默认

    public static final JoymeAppMenuDisplayType IMAGE_TEXT = new JoymeAppMenuDisplayType(1);     //图文类型

    public static final JoymeAppMenuDisplayType NEWS = new JoymeAppMenuDisplayType(2);    //新闻类型

    public static final JoymeAppMenuDisplayType TEXT = new JoymeAppMenuDisplayType(3);     //文字类型

    public static final JoymeAppMenuDisplayType IMAGE = new JoymeAppMenuDisplayType(4);     //图片类型

    public static final JoymeAppMenuDisplayType TAG = new JoymeAppMenuDisplayType(5);     //标签类型

    public static final JoymeAppMenuDisplayType ICON = new JoymeAppMenuDisplayType(6);     //图标类型

    private int code;

    private JoymeAppMenuDisplayType(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public String toString() {
        return "JoymeAppMenuDisplayType: code=" + code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((JoymeAppMenuDisplayType) o).code) return false;

        return true;
    }

    public static JoymeAppMenuDisplayType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<JoymeAppMenuDisplayType> getAll() {
        return map.values();
    }
}
