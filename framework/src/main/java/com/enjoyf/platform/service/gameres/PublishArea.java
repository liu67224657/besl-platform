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
public class PublishArea implements Serializable{

    //国内
    public static final int INTERNAL = 1;
    //亚洲
    public static final int ASIA = 2;
    //东南亚
    public static final int SOUTH_EAST_ASIA = 4;
    //日韩
    public static final int JAPAN_KOREA = 8;
    //北美
    public static final int NORTH_AMERICA = 16;
    //南美
    public static final int SOUTH_AMERICA = 32;
    //西亚
    public static final int WEST_ASIA = 64;
    //非洲
    public static final int AFRICAN = 128;
    //欧洲
    public static final int EUROPE = 256;

    private int value = 0;

    public PublishArea() {
    }

    public PublishArea(int v) {
        this.value = v;
    }

    public PublishArea has(int v){
        this.value += v;
        return this;
    }

    public int getValue(){
        return value;
    }

    public static PublishArea getByValue(int v){
        return new PublishArea(v);
    }

    public boolean hasInternal(){
        return (value & INTERNAL)>0;
    }

    public boolean hasAsia(){
        return (value & ASIA)>0;
    }

    public boolean hasSouthEastAsia(){
        return (value & SOUTH_EAST_ASIA)>0;
    }

    public boolean hasJapanKorea(){
        return (value & JAPAN_KOREA)>0;
    }

    public boolean hasNorthAmerica(){
        return (value & NORTH_AMERICA)>0;
    }

    public boolean hasSouthAmerica(){
        return (value & SOUTH_AMERICA)>0;
    }

    public boolean hasWestAsia(){
        return (value & WEST_ASIA)>0;
    }

    public boolean hasAfrican(){
        return (value & AFRICAN)>0;
    }

    public boolean hasEurope(){
        return (value & EUROPE)>0;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o){return true;}
        if(o == null || this.getClass() != o.getClass()){return false;}
        if(value != ((PublishArea)o).value){return false;}
        return true;
    }

    @Override
    public String toString() {
        return "PublishArea: value"+value;
    }
}
