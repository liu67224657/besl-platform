/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-2-15 下午6:17
 * Description:
 */
public class Rangination implements Serializable {
    //the start and the end index. from 0 to total - 1;
    private int start;
    private int end;

    private int total = 0;

    //
    public Rangination() {
        calculate();
    }

    public Rangination(int total) {
        this.total = total;

        calculate();
    }

    public Rangination(int start, int end) {
        this.start = start;
        this.end = end;
        this.total = end;

        calculate();
    }

    public Rangination(int start, int end, int total) {
        this.start = start;
        this.end = end;
        this.total = total;

        calculate();
    }

    public void setStart(int start) {
        this.start = start;

        calculate();
    }

    public void setEnd(int end) {
        this.end = end;

        calculate();
    }

    public void setTotal(int total) {
        this.total = total;

        calculate();
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getTotal() {
        return total;
    }

    public int getSize() {
        return end - start + 1;
    }

    public boolean hasNext() {
        return total > end + 1;
    }

    public int leavings() {
        return total - end + 1;
    }

    private void calculate() {
        //
        total = total >= 0 ? total : 0;
        start = start >= 0 ? start : 0;
        end = end >= 0 ? end : 0;

        //
        if (start > total) {
            start = total;
        }

        if (end > total) {
            end = total;
        }

        if (start > end) {
            end = start;
        }
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
