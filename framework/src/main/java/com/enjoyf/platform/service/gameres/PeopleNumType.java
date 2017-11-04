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
public class PeopleNumType implements Serializable{
    private static Map<Integer, PeopleNumType> map = new HashMap<Integer, PeopleNumType>();

    //1 - 10人
    public static final PeopleNumType ONE_TO_TEN = new PeopleNumType(1);
    //11 - 20人
    public static final PeopleNumType ELEVEN_TO_TWENTY = new PeopleNumType(2);
    //21 - 50人
    public static final PeopleNumType TWENTY_ONE_TO_FIFTY = new PeopleNumType(3);
    //51 - 100人
    public static final PeopleNumType FIFTY_ONE_TO_ONR_HUNDRED = new PeopleNumType(4);
    //101 - 200人
    public static final PeopleNumType ONE_HUNDRED_AND_ONE_TO_TWO_HUNDRED = new PeopleNumType(5);
    //>201
    public static final PeopleNumType MORE_TWO_HUNDRED = new PeopleNumType(6);

    private int value;

    public PeopleNumType(){

    }

    public PeopleNumType(int v) {
        this.value = v;
        map.put(v, this);
    }

    public int getValue(){
        return value;
    }

    public static PeopleNumType getByValue(int v){
        return new PeopleNumType(v);
    }

    public static Collection<PeopleNumType> getAll(){
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
        if(value != ((PeopleNumType)o).value)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PeopleNumType: value"+value;
    }
}
