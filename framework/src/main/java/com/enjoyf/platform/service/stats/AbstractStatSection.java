/**
 * (C) 2010 Fivewh platform platform.com
 */
package com.enjoyf.platform.service.stats;

import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public abstract class AbstractStatSection implements StatSection {
    private String code;

    public AbstractStatSection(String c) {
        code = c.toLowerCase();
    }

    public String getCode() {
        return code;
    }

    public int hashCode() {
        return code.hashCode();
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof StatSection)) {
            return false;
        }

        return code.equalsIgnoreCase(((StatSection) obj).getCode());
    }
}
