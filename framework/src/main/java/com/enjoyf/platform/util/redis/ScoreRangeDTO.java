package com.enjoyf.platform.util.redis;

/**
 * @author <a href=mailto:ericliu@fivewh.com>ericliu</a>,Date:16/10/11
 */
public class ScoreRangeDTO {
    private double min = -1.0;
    private double max = -1.0;
    private double scoreflag;
    private int size = 10;
    private boolean hasnext;

    public ScoreRangeDTO(ScoreRange range) {
        this.max=range.getMax();
        this.min=range.getMin();
        this.scoreflag=range.getScoreflag();
        this.size=range.getSize();
        this.hasnext=range.isHasnext();
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

    public double getScoreflag() {
        return scoreflag;
    }

    public void setScoreflag(double scoreflag) {
        this.scoreflag = scoreflag;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isHasnext() {
        return hasnext;
    }

    public void setHasnext(boolean hasnext) {
        this.hasnext = hasnext;
    }
}
