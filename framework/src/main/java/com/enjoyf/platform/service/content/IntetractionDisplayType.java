/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-2-15 下午1:13
 * Description: 元素显示类型
 */
public class IntetractionDisplayType implements Serializable {
    //
    public static final int HOT = 1;
    public static final int NEW = 2;
    public static final int ESSENTIAL = 4;

    //
    private int value = 0;

    public IntetractionDisplayType() {
    }

    public IntetractionDisplayType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean isHot() {
        return (value & HOT) > 0;
    }

    public boolean isNew() {
        return (value & NEW) > 0;
    }

    public boolean isEssential() {
        return (value & ESSENTIAL) > 0;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "ViewLineItemDisplayType: value=" + value;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof IntetractionDisplayType)) {
            return false;
        }

        return value == ((IntetractionDisplayType) obj).getValue();
    }

    public static IntetractionDisplayType getByValue(int v) {
        return new IntetractionDisplayType(v);
    }
}
