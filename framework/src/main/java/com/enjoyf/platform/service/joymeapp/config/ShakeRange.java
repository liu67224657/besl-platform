package com.enjoyf.platform.service.joymeapp.config;

import java.io.Serializable;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/3/23
 * Description:
 */
public class ShakeRange implements Serializable {
    private int min;
    private int max;

    public ShakeRange() {
    }

    public ShakeRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
