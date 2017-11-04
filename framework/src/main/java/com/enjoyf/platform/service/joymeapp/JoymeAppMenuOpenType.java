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
public class JoymeAppMenuOpenType implements Serializable {

    private static Map<Integer, JoymeAppMenuOpenType> map = new HashMap<Integer, JoymeAppMenuOpenType>();

    public static final JoymeAppMenuOpenType DEFAULT = new JoymeAppMenuOpenType(0);   //默认

    public static final JoymeAppMenuOpenType TOBE_OPEN = new JoymeAppMenuOpenType(1);  //即将开启
    public static final JoymeAppMenuOpenType OPENING = new JoymeAppMenuOpenType(2);  //进行中
    //
    private int code;

    private JoymeAppMenuOpenType(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "JoymeAppMenuModuleType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((JoymeAppMenuOpenType) o).code) return false;

        return true;
    }

    public static JoymeAppMenuOpenType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<JoymeAppMenuOpenType> getAll() {
        return map.values();
    }
}
