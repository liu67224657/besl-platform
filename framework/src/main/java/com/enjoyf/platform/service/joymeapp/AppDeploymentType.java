package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-12-11 上午10:01
 * Description:
 */
public class AppDeploymentType implements Serializable {

    private static Map<Integer, AppDeploymentType> map = new HashMap<Integer, AppDeploymentType>();

    //证书
    public static final AppDeploymentType CERTIFICATE = new AppDeploymentType(1);
    //版本更新
    public static final AppDeploymentType VERSION = new AppDeploymentType(2);
    //论坛
    public static final AppDeploymentType BBS = new AppDeploymentType(3);
    //
    private int code;

    private AppDeploymentType(int c) {
        this.code = c;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "AppDisplayType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((AppDeploymentType) o).code) return false;

        return true;
    }

    public static AppDeploymentType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<AppDeploymentType> getAll() {
        return map.values();
    }
}
