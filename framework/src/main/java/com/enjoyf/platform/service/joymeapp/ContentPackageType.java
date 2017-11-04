package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-27
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
public class ContentPackageType implements Serializable {
    private static Map<Integer, ContentPackageType> map = new HashMap<Integer, ContentPackageType>();

    public static final ContentPackageType PATCH = new ContentPackageType(0);
    public static final ContentPackageType COMPLETE = new ContentPackageType(1);


    private int code;

    public ContentPackageType(int code) {
        this.code = code;

        map.put(code, this);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ContentPackageType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((ContentPackageType) o).code) return false;

        return true;
    }

    public static ContentPackageType getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ContentPackageType> getAll() {
        return map.values();
    }
}
