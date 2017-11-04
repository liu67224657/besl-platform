package com.enjoyf.platform.service.content;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guoliangnan on 14-1-23.
 */
public class ForignContentReplyLogType implements Serializable{
    private static Map<Integer, ForignContentReplyLogType> map = new HashMap<Integer, ForignContentReplyLogType>();

    public static final ForignContentReplyLogType AGREE = new ForignContentReplyLogType(1);      //支持
    public static final ForignContentReplyLogType DISAGGREE = new ForignContentReplyLogType(2);  //不支持

    private int code;

    public ForignContentReplyLogType(Integer code) {
        this.code = code;
        map.put(code,this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForignContentReplyLogType that = (ForignContentReplyLogType) o;

        if (code != (that.code)) return false;

        return true;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ForignContentReplyLogType: code=" + code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    public static ForignContentReplyLogType getByCode(int code){
        return map.get(code);
    }

    public static Collection<ForignContentReplyLogType> getAll(){
        return map.values();
    }
}
