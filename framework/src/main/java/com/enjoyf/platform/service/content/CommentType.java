package com.enjoyf.platform.service.content;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guoliangnan on 14-1-23.
 */
public class CommentType implements Serializable{
    private static Map<Integer, CommentType> map = new HashMap<Integer, CommentType>();
    private Integer code;

    public static final CommentType LOCALSOURCE = new CommentType(0);
    public static final CommentType FOREIGNSOURCE = new CommentType(1);

    public CommentType(Integer code) {
        this.code = code;
        map.put(code,this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommentType that = (CommentType) o;

        if (!code.equals(that.code)) return false;

        return true;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "CommentDomain: code=" + code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    public static CommentType getByCode(Integer code){
        if(code == null){
            return null;
        }
        return map.get(code);
    }

    public static Collection<CommentType> getAll(){
        return map.values();
    }
}
