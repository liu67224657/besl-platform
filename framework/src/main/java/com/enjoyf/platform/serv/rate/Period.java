package com.enjoyf.platform.serv.rate;

import java.util.Date;

/**
 * @author Yin Pengyi
 */
class Period {
    private int beginTime = (int) (System.currentTimeMillis() / 1000);
    private int duration;

    /**
     * Constructs a period
     *
     * @param durationSecs - duration of this period in seconds
     */
    public Period(int durationSecs) {
        setDuration(durationSecs);
    }

    /**
     * Gets the duration of this period
     *
     * @return int - when this period began
     */
    public int getBeginTimeSecs() {
        return beginTime;
    }

    /**
     * Gets the duration of this period
     *
     * @return int - duration of this period in seconds
     */
    public int getDurationSecs() {
        return duration;
    }

    /**
     * Sets the duration of this period
     *
     * @param durationSecs - duration of this period in seconds
     */
    public void setDuration(int durationSecs) {
        duration = durationSecs;
    }


    public boolean expired() {
        // get the current time and the duration of this period
        long now = System.currentTimeMillis() / 1000;

        // calculate the time elapsed since this period began
        long timeElapsed = now - beginTime;

        // if this period has expired return true
        if (timeElapsed >= duration) {
            return true;
        }

        return false;
    }


    /**
     * String representation
     *
     * @return String - representation of this key
     */
    public String toString() {
        return "begin=" + new Date(beginTime) + ", " + duration + " second intervals";
    }

    /**
     * hashCode override
     */
    public int hashCode() {
        return duration;
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
        Period period = (Period) o;
        return duration == period.duration;
    }
}
