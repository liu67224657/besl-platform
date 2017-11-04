/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>  ,zx
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class ImageSizeType implements Serializable {
    //icon, super small image
    public static final Integer SUPER_SMALL = 1;

    //small image
    public static final Integer SMALL = 2;

    //middling image
    public static final Integer MIDDLING = 4;

    //large image
    public static final Integer LARGE = 8;

    //original image
    public static final Integer ORIGINAL = 16;

    //
    private Integer superSmall = 0;
    private Integer small = 0;
    private Integer middling = 0;
    private Integer large = 0;
    private Integer original = 0;

    //
    private ImageSizeType(Integer v) {
        superSmall = SUPER_SMALL & v;
        small = SMALL & v;
        middling = MIDDLING & v;
        large = LARGE & v;
        original = ORIGINAL & v;
    }

    public Integer getValue() {
        return superSmall + small + middling + large + original;
    }

    public void isSuperSmall() {
        superSmall = SUPER_SMALL;
    }

    public void isSmall() {
        small = SMALL;
    }

    public void isMiddling() {
        middling = MIDDLING;
    }

    public void isLarge() {
        large = LARGE;
    }

    public void isOriginal() {
        original = ORIGINAL;
    }

    public boolean hasSuperSmall() {
        return (getValue() & SUPER_SMALL) > 0;
    }

    public boolean hasSmall() {
        return (getValue() & SMALL) > 0;
    }

    public boolean hasMiddling() {
        return (getValue() & MIDDLING) > 0;
    }

    public boolean hasLarge() {
        return (getValue() & LARGE) > 0;
    }

    public boolean hasOriginal() {
        return (getValue() & ORIGINAL) > 0;
    }

    @Override
    public int hashCode() {
        return getValue();
    }

    @Override
    public String toString() {
        return "ImageSizeType: value=" + getValue();
    }

    public static ImageSizeType getByValue(Integer v) {
        return new ImageSizeType(v);
    }

}
