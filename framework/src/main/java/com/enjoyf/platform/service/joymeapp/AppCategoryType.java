/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.joymeapp;

import java.io.Serializable;
import java.util.Collection;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>  ,zx
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class AppCategoryType implements Serializable {


    public static final int AWESOME = 1;

    public static final int BOOMING = 2;

    public static final int CLASSIC = 4;

    public static final int NEWGAME = 8;

    public static final int HOT = 16;

    public static final int ALL = 31;


    private int value = 0;

    //
    public AppCategoryType() {
    }

    private AppCategoryType(int v) {
        value = v;
    }

    public AppCategoryType has(int v) {
        value += v;

        return this;
    }

    public boolean hasAwesome() {
        return (value & AWESOME) > 0;
    }

    public boolean hasBooming() {
        return (value & BOOMING) > 0;
    }


    public boolean hasClassic() {
        return (value & CLASSIC) > 0;
    }


    public boolean hasNewGame() {
        return (value & NEWGAME) > 0;
    }

    public boolean hasHot() {
        return (value & HOT) > 0;
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
        return "ContentType: value=" + value;
    }

    public static AppCategoryType getByValue(Integer v) {
        return new AppCategoryType(v);
    }

    public Collection<AppCategoryType> getAll(){
        return null;
    }


}
