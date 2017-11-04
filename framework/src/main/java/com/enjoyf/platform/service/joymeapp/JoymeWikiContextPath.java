/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-2-15 下午1:13
 * Description: 元素类型
 */
public class JoymeWikiContextPath implements Serializable {
    private static Map<String, JoymeWikiContextPath> map = new HashMap<String, JoymeWikiContextPath>();

    public static final JoymeWikiContextPath WIKI = new JoymeWikiContextPath("wiki");    //PC
    public static final JoymeWikiContextPath MWIKI = new JoymeWikiContextPath("mwiki");  //M站

    private String code;

    private JoymeWikiContextPath(String c) {
        this.code = c;

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code.equalsIgnoreCase(((JoymeWikiContextPath) o).code)) return false;

        return true;
    }

    public static JoymeWikiContextPath getByCode(String c) {
        return map.get(c);
    }

    public static Collection<JoymeWikiContextPath> getAll() {
        return map.values();
    }
}
