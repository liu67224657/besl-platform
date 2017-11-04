/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util;

import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-2-15 上午10:49
 * Description:
 */
public class RangeRows<T> implements Serializable {
    //
    private Rangination range;

    private List<T> rows = new ArrayList<T>();

    //
    public RangeRows() {
    }

    public Rangination getRange() {
        return range;
    }

    public void setRange(Rangination range) {
        this.range = range;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
