package com.enjoyf.platform.service.misc;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-3 下午3:53
 * Description:
 */
public class JoymeOperateType implements Serializable {
    private static Map<Integer, JoymeOperateType> map = new HashMap<Integer, JoymeOperateType>();

    public static final JoymeOperateType REFRESH_INDEX = new JoymeOperateType(1);

    private Integer code;

    private JoymeOperateType(int i) {
        code = i;

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
        return "JoymeOperateType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof JoymeOperateType)) {
            return false;
        }

        return code == ((JoymeOperateType) obj).getCode();
    }

    public static JoymeOperateType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<JoymeOperateType> getAll() {
        return map.values();
    }
}
