/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.userprops;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class UserPropKey implements Serializable {
    private UserPropDomain domain;
    private String uno = null;
    private String key = "";
    private int idx = 0;

    public UserPropKey(UserPropDomain d, String u, String k) {
        domain = d;
        uno = u;
        key = k.toLowerCase();
    }

    public UserPropKey(UserPropDomain d, String u, String k, int i) {
        domain = d;
        uno = u;
        key = k.toLowerCase();
        idx = i;
    }


    public UserPropDomain getDomain() {
        return domain;
    }

    public String getUno() {
        return uno;
    }

    public String getKey() {
        return key;
    }

    public int getIdx() {
        return idx;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof UserPropKey) {
            UserPropKey tempKey = (UserPropKey) obj;

            return domain.equals(tempKey.getDomain())
                    && uno.equals(tempKey.getUno())
                    && key.equals(tempKey.getKey())
                    && idx == tempKey.getIdx();
        }

        return false;
    }
}
