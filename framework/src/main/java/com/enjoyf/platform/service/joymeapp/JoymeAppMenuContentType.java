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
public class JoymeAppMenuContentType implements Serializable {

    private static Map<Integer, JoymeAppMenuContentType> map = new HashMap<Integer, JoymeAppMenuContentType>();
    //默认
    public static final JoymeAppMenuContentType DEFAULT = new JoymeAppMenuContentType(0);
    //活动
    public static final JoymeAppMenuContentType HUODONG = new JoymeAppMenuContentType(1);
    //资讯
    public static final JoymeAppMenuContentType ZIXUN = new JoymeAppMenuContentType(2);
    //公告
    public static final JoymeAppMenuContentType GONGGAO = new JoymeAppMenuContentType(3);


    private int code;

    private JoymeAppMenuContentType(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "MenuContentType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((JoymeAppMenuContentType) o).code) return false;

        return true;
    }

    public static JoymeAppMenuContentType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<JoymeAppMenuContentType> getAll() {
        return map.values();
    }
}
