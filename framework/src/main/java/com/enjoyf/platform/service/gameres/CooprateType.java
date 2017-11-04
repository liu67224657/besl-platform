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
public class CooprateType implements Serializable {

    //独代
    public static final int EXCLUSIVE = 1;
    //分成
    public static final int BENEFIT = 2;

    private int value = 0;

    public CooprateType() {
    }

    public CooprateType(int v) {
        this.value = v;
    }

    public CooprateType has(int v) {
        this.value += v;
        return this;
    }

    public int getValue() {
        return value;
    }

    public static CooprateType getByValue(int v) {
        return new CooprateType(v);
    }

    public boolean hasExclusive() {
        return (value & EXCLUSIVE) > 0;
    }

    public boolean hasBenefit() {
        return (value & BENEFIT) > 0;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass())
            return false;
        if (value != ((CooprateType) o).value)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CooprateType: value" + value;
    }
}
