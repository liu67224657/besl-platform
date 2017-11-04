/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.timeline;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>  ,zx
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class ItemSpreadType implements Serializable {
    //一句话
    public static final int DEF = 1;

    //
    public static final int FAV = 2;

    //全部
    public static final int ALL = 3;

    private Integer value = 0;

    //
    public ItemSpreadType() {
    }

    private ItemSpreadType(int v) {
        value = v;
    }

    public ItemSpreadType has(int v) {
        value += v;

        return this;
    }

    public Integer getValue() {
        return value;
    }

    public boolean hasDef() {
        return (value & DEF) > 0;
    }

    public boolean onlyDefault() {
        return value == DEF;
    }

    public boolean hasFav() {
        return (value & FAV) > 0;
    }

    public boolean onlyFav() {
        return value == FAV;
    }


    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "ContentType: value=" + value;
    }

    public static ItemSpreadType getByValue(Integer v) {
        return new ItemSpreadType(v);
    }

}
