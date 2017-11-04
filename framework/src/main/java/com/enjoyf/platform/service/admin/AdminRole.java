package com.enjoyf.platform.service.admin;

import com.enjoyf.platform.props.AdminConfig;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class AdminRole implements Serializable {

    private String code;
    private String name;

    private Set<Privilege> privilages = new HashSet<Privilege>();

    public AdminRole(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Privilege> getPrivilages() {
        return privilages;
    }

    public void setPrivilages(Set<Privilege> privilages) {
        this.privilages = privilages;
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    public int hashCode() {
        return code.hashCode();
    }

    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof AdminRole)) {
            return false;
        }

        return code.equalsIgnoreCase(((AdminRole) obj).getCode());
    }

    public static AdminRole getByCode(String c) {
        return AdminConfig.get().getRolesMap().get(c);
    }
}
