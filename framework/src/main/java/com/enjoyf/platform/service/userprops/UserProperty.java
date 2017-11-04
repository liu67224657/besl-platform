/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.userprops;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class UserProperty implements Serializable {

    private UserPropKey userPropKey = null;
    private String value = null;

    private Date initialDate;
    private Date modifyDate;

    public UserProperty() {

    }

    public UserPropKey getKey() {
        return userPropKey;
    }

    public void setUserPropKey(UserPropKey key) {
        userPropKey = key;
    }

    public String getValue() {
        return value;
    }

    public long getLongValue() {
        if (Strings.isNullOrEmpty(value)) {
            return 0;
        }

        return Long.parseLong(value);
    }

    public int getIntValue() {
        if (Strings.isNullOrEmpty(value)) {
            return 0;
        }

        return Integer.parseInt(value);
    }

    public float getFloatValue() {
        if (Strings.isNullOrEmpty(value)) {
            return 0;
        }

        return Float.parseFloat(value);
    }

    public double getDoubleValue() {
        if (Strings.isNullOrEmpty(value)) {
            return 0;
        }

        return Double.parseDouble(value);
    }

    public void setValue(String val) {
        value = val;
    }


    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date date) {
        initialDate = date;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date date) {
        modifyDate = date;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}