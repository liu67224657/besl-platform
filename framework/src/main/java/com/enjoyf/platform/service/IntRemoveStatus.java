package com.enjoyf.platform.service;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zhitaoshi on 2015/7/24.
 */
public class IntRemoveStatus implements Serializable {

    private static Map<Integer, IntRemoveStatus> map = new LinkedHashMap<Integer, IntRemoveStatus>();

    public static IntRemoveStatus REMOVE = new IntRemoveStatus(0, "删除");//删除
    public static IntRemoveStatus USED = new IntRemoveStatus(1, "可用");//使用
    public static IntRemoveStatus REVIEW = new IntRemoveStatus(2, "预发布");//预发布

    public static IntRemoveStatus ING = new IntRemoveStatus(3, "进行中");//
    public static IntRemoveStatus ERROR = new IntRemoveStatus(4, "错误");//

    private int code;
    private String desc;

    public IntRemoveStatus(int c, String d) {
        this.code = c;
        this.desc = d;
        map.put(c, this);
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static IntRemoveStatus getByCode(int c) {
        return map.get(c);
    }

    public Collection<IntRemoveStatus> getAll() {
        return map.values();
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (code != ((IntRemoveStatus) o).code) return false;
        return true;
    }

}
