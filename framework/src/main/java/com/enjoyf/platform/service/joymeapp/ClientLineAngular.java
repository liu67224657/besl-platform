package com.enjoyf.platform.service.joymeapp;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-3-25
 * Time: 下午5:05
 * ClientItem 角度类型
 */
public class ClientLineAngular implements Serializable {
    private static Map<Integer, ClientLineAngular> map = new HashMap<Integer, ClientLineAngular>();

    private int code;

    //专题
    public static final ClientLineAngular focus = new ClientLineAngular(0);

    public ClientLineAngular() {
    }

    public ClientLineAngular(int code) {
        this.code = code;
        map.put(code, this);
    }

    public void setCode(int code) {
        this.code = code;
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
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        if (code != ((ClientLineAngular) o).code) return false;

        return true;
    }
    public static ClientLineAngular getByCode(int c) {
        return map.get(c);
    }

    public static Collection<ClientLineAngular> getAll() {
        return map.values();
    }
}
