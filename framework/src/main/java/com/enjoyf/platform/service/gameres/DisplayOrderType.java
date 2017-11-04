package com.enjoyf.platform.service.gameres;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-20
 * Time: 下午4:49
 * To change this template use File | Settings | File Templates.
 */
public class DisplayOrderType implements Serializable{
    private static Map<Integer, DisplayOrderType> map = new HashMap<Integer, DisplayOrderType>();

    //发行时间
    public static final DisplayOrderType PUBLISH_DATE = new DisplayOrderType(1);
    //提交时间
    public static final DisplayOrderType CREATE_DATE = new DisplayOrderType(2);
    //关注度
    public static final DisplayOrderType FOCUS_NUM = new DisplayOrderType(3);

    private int value;

    public DisplayOrderType(){

    }

    public DisplayOrderType(int v) {
        this.value = v;
        map.put(v, this);
    }

    public int getValue(){
        return value;
    }

    public static DisplayOrderType getByValue(int v){
        return new DisplayOrderType(v);
    }

    public static Collection<DisplayOrderType> getAll(){
        return map.values();
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || this.getClass() != o.getClass())
            return false;
        if(value != ((DisplayOrderType)o).value)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PeopleNumType: value"+value;
    }
}
