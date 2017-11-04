/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>  ,zx
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class ActivityGoodsCategory implements Serializable {
    private static Map<Integer, ActivityGoodsCategory> map = new HashMap<Integer, ActivityGoodsCategory>();

    public static final int CLASSIC = 1; //精品推荐

    public static final int HOT = 2;    //热卖实物

    public static final int NEW = 4; //最新上架

    public static final int SPECIAL = 8; //特价商品

    private int value = 0;

    //
    public ActivityGoodsCategory() {
    }

    private ActivityGoodsCategory(int v) {
        this.value = v;
        map.put(v, this);
    }

    public ActivityGoodsCategory has(int v) {
        value += v;
        return this;
    }

    public boolean hasClassic() {
        return (value & CLASSIC) > 0;
    }

    public boolean hasHot() {
        return (value & HOT) > 0;
    }


    public boolean hasNew() {
        return (value & NEW) > 0;
    }


    public boolean hasSpecial() {
        return (value & SPECIAL) > 0;
    }

    public int getValue() {
        return value;
    }


    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "ActivityGoodsCategory: value=" + value;
    }

    public static ActivityGoodsCategory getByValue(Integer v) {
        return new ActivityGoodsCategory(v);
    }

    public static Collection<ActivityGoodsCategory> getAll() {
        return map.values();
    }


}
