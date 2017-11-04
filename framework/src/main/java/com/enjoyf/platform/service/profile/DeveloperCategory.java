package com.enjoyf.platform.service.profile;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-12-16
 * Time: 上午10:00
 * To change this template use File | Settings | File Templates.
 */
public class DeveloperCategory implements Serializable{
    private static Map<Integer, DeveloperCategory> map = new HashMap<Integer, DeveloperCategory>();

    public static final DeveloperCategory PERSONALDEV = new DeveloperCategory(0);  //个人
    public static final DeveloperCategory INVESTOR = new DeveloperCategory(1); //机构
    public static final DeveloperCategory DISTRIBUTOR = new DeveloperCategory(2);  //发行商
    public static final DeveloperCategory ORGDEV = new DeveloperCategory(3);  //开发商
    public static final DeveloperCategory OPERATER = new DeveloperCategory(4);  //运营商

    private int code;

    public DeveloperCategory(int c) {
        code = c;
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
        return "DeveloperCategory: code=" + code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (code != ((DeveloperCategory) o).code) return false;
        return true;
    }

    public static DeveloperCategory getByCode(int c) {
        return map.get(c);
    }

    public static Collection<DeveloperCategory> getAll() {
        return map.values();
    }
}
