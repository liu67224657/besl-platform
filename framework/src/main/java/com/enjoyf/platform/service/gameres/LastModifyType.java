package com.enjoyf.platform.service.gameres;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-20
 * Time: 下午4:50
 * To change this template use File | Settings | File Templates.
 */
public class LastModifyType implements Serializable{
    private static Map<Integer, LastModifyType> map= new HashMap<Integer, LastModifyType>();
    //joyme修改
    private static final LastModifyType JOYME = new LastModifyType(1);
    //发布人修改
    private static final LastModifyType OWNER = new LastModifyType(2);

    private int code;

    public LastModifyType(int code) {
        this.code = code;
        map.put(code, this);
    }

    public int getCode(){
        return code;
    }

    public static LastModifyType getByCode(int code){
        return map.get(code);
    }

    public static Collection<LastModifyType> getAll(){
        return map.values();
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || this.getClass() != o.getClass())
            return false;
        if(code != ((LastModifyType)o).code)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LastModifyType: code"+code;
    }
}
