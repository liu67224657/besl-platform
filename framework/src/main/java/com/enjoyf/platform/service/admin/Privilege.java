package com.enjoyf.platform.service.admin;

import com.enjoyf.platform.props.AdminConfig;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;


public class Privilege implements Serializable {

    private String code;
    private String name;

    //
    public Privilege(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public int hashCode() {
        return code.hashCode();
    }

    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof Privilege)) {
            return false;
        }

        return code.equalsIgnoreCase(((Privilege) obj).getCode());
    }

    public static Privilege getByCode(String c) {
        return AdminConfig.get().getPrivilagesMap().get(c);
    }
}
