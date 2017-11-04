package com.enjoyf.webapps.joyme.weblogic.regcode;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:标签服务接口
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class RegcodeUsed implements Serializable {

    private static final long serialVersionUID = -1352788530328688045L;

    private static Map<String, RegcodeUsed> map = new HashMap<String, RegcodeUsed>();

    public static final RegcodeUsed USED_INIT = new RegcodeUsed("A");
    public static final RegcodeUsed USED_UNUSE = new RegcodeUsed("D");
    public static final RegcodeUsed USED_USED = new RegcodeUsed("I");
    private String code;

    public RegcodeUsed(String code) {
        this.code = code;
        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    public static Map<String, RegcodeUsed> getMap() {
        return map;
    }

    /**
     * get by code default null
     *
     * @param code
     * @return
     */
    public static RegcodeUsed getByCode(String code) {
        return map.containsKey(code) ? map.get(code) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegcodeUsed)) return false;

        RegcodeUsed obj = (RegcodeUsed) o;

        if (!code.equals(obj.getCode())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
