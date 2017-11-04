package com.enjoyf.platform.util.redis;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/18
 * Description:
 */
public class ScoreRangeRows<T> implements Serializable {
    private ScoreRange range;
    private List<T> rows = new ArrayList<T>();

    public ScoreRangeRows() {
    }

    public ScoreRange getRange() {
        return range;
    }

    public void setRange(ScoreRange range) {
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
