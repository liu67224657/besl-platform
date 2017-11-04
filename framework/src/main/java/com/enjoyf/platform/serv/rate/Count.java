/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.rate;

/**
 * @author Yin Pengyi
 */
class Count {
    private long count = 0;

    /**
     * Constructs a Count
     */
    public Count() {
    }

    /**
     * Constructs a Count
     *
     * @param count - current count
     */
    public Count(long count) {
        setCount(count);
    }

    /**
     * Gets the current count
     *
     * @return long - current count
     */
    public long getCount() {
        return count;
    }

    /**
     * Sets the current count
     *
     * @param count - current count
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * Increments the current count by 1
     */
    public void increment() {
        count++;
    }

    /**
     * String representation
     *
     * @return String - string representation
     */
    public String toString() {
        return String.valueOf(count);
    }

    /**
     * equals override
     *
     * @param o - object to compare this to
     */
    public boolean equals(Object o) {
        // quick tests to get us out of here
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        // it's a RateKey, but it's not this one, so check it for equality
        Count count = (Count) o;
        return this.count == count.count;
    }
}
