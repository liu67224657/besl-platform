package com.enjoyf.platform.util.redis;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/18
 * Description:
 */
@JsonIgnoreProperties(value = {"min", "max", "containStart", "containEnd", "limit"})
public class ScoreRange implements Serializable {
    private double min = -1.0;
    private double max = -1.0;
    private boolean isDesc = true;
    private boolean isFirstPage;
    private double scoreflag;
    private boolean hasnext;
    private int limit = 0;

    private int size = 10;

    public ScoreRange() {
    }

    public ScoreRange(double min, double max) {
        this(min, max, 10, true);
    }

    public ScoreRange(double min, double max, int size) {
        this(min, max, size, true);
    }

    public ScoreRange(double min, double max, int size, boolean isDesc) {
        this.min = min;
        this.max = max;
        this.size = size;
        this.isDesc = isDesc;

        isFirstPage=isDesc?max<0:min<0;
    }


    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getScoreflag() {
        return scoreflag;
    }

    public void setScoreflag(double scoreflag) {
        this.scoreflag = scoreflag;
    }

    public boolean isHasnext() {
        return hasnext;
    }

    public void setHasnext(boolean hasnext) {
        this.hasnext = hasnext;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isDesc() {
        return isDesc;
    }

    public void setIsDesc(boolean isDesc) {
        this.isDesc = isDesc;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
