/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.rate;

/**
 * @author Yin Pengyi
 */
class PeriodCount {
    private Period period;
    private Count count;

    public PeriodCount(Period period, Count count) {
        if (period == null) {
            throw new IllegalArgumentException("null period received");
        }

        if (count == null) {
            throw new IllegalArgumentException("null count received");
        }

        this.period = period;
        this.count = count;
    }

    public Period getPeriod() {
        return period;
    }

    public Count getCount() {
        return count;
    }
}
