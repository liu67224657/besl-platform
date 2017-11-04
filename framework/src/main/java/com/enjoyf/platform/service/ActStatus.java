/**
 * (C) 2009 Fivewh platform platform.com
 */
package com.enjoyf.platform.service;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
@SuppressWarnings("serial")
public class ActStatus implements Serializable {

    private static Map<String, ActStatus> map = new LinkedHashMap<String, ActStatus>();

    //the initialize status   未审核n
    public static ActStatus UNACT = new ActStatus("n");      //初始状态
    //the acting         ing
    public static ActStatus ACTING = new ActStatus("ing");   //进行中状态
    //the act result. ok, reject error.  y
    public static ActStatus ACTED = new ActStatus("y");     //已操作状态
    public static ActStatus REJECTED = new ActStatus("rj");  //准备状态
    public static ActStatus ERROR = new ActStatus("er");    //错误

    private String code;

    private ActStatus(String code) {
        this.code = code.toLowerCase();

        map.put(this.code, this);
    }
    public ActStatus(){

    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ActStatus code:" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof ActStatus) {
            return code.equalsIgnoreCase(((ActStatus) obj).getCode());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    public static ActStatus getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ActStatus> getAll() {
        return map.values();
    }
}
