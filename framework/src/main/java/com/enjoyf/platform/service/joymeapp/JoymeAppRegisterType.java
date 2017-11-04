/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.joymeapp;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class JoymeAppRegisterType implements Serializable {
    private static Map<String, JoymeAppRegisterType> map = new HashMap<String, JoymeAppRegisterType>();

    //注册
    public static JoymeAppRegisterType DEFAULT = new JoymeAppRegisterType("default");
    //忘记密码 修改
    public static JoymeAppRegisterType FORGOT = new JoymeAppRegisterType("forgot");

    private String code;

    private JoymeAppRegisterType(String c) {
        code = c.toLowerCase();

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
        return "InteractionType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof JoymeAppRegisterType)) {
            return false;
        }

        return code.equalsIgnoreCase(((JoymeAppRegisterType) obj).getCode());
    }

    public static JoymeAppRegisterType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<JoymeAppRegisterType> getAll() {
        return map.values();
    }
}
