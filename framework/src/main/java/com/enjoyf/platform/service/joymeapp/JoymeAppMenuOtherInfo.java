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
public class JoymeAppMenuOtherInfo implements Serializable {
    private static Map<String, JoymeAppMenuOtherInfo> map = new HashMap<String, JoymeAppMenuOtherInfo>();

    public static final JoymeAppMenuOtherInfo QQUN = new JoymeAppMenuOtherInfo("qqun");   //默认

    public static final JoymeAppMenuOtherInfo TIEBA = new JoymeAppMenuOtherInfo("tieba");     //图文类型

    public static final JoymeAppMenuOtherInfo LUNTAN = new JoymeAppMenuOtherInfo("luntan");    //新闻类型

    private String code;

    private JoymeAppMenuOtherInfo(String c) {
        this.code = c;
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "JoymeAppMenuOtherInfo: code=" + code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return code.equalsIgnoreCase(((AppChannelType) o).getCode());
    }

    public static JoymeAppMenuOtherInfo getByCode(String c) {
        return map.get(c);
    }

    public static Collection<JoymeAppMenuOtherInfo> getAll() {
        return map.values();
    }
}
