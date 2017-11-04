package com.enjoyf.platform.service.rate;

import java.io.Serializable;

import com.google.common.base.Strings;

/**
 * @author Yin Pengyi
 */
@SuppressWarnings("serial")
public class RateLimit implements Serializable {
	
    //check period, in secs
    private int duration;
    private int maxAllowed;

    /**
     * Constructs a RateLimit
     */
    public RateLimit() {
    }

    /**
     * Constructs a RateLimit
     *
     * @param durationSecs - duration of the limit window
     * @param max          - maximum number of attempts allowed during this duration
     */
    public RateLimit(int durationSecs, int max) {
        setDurationSecs(durationSecs);
        setMaxAllowed(max);
    }

    /**
     * Gets the duration of this period
     *
     * @return int - duration of the limit window in seconds
     */
    public int getDurationSecs() {
        return duration;
    }

    /**
     * Sets the duration of this period
     *
     * @param durationSecs - duration of the limit window in seconds
     */
    public void setDurationSecs(int durationSecs) {
        duration = durationSecs;
    }

    /**
     * Gets the maximum number of attempts allowed during this duration
     *
     * @return int - maximum number of attempts allowed during this duration
     */
    public int getMaxAllowed() {
        return maxAllowed;
    }

    /**
     * String representation
     *
     * @return String - string representation
     */
    public String toString() {
        return "RateLimit [" + maxAllowed + " attempts per " + duration + " secs]";
    }

    /**
     * Sets the duration of this period
     *
     * @param max - maximum number of attempts allowed during this duration
     */
    public void setMaxAllowed(int max) {
        if (max <= 0) {
            throw new IllegalArgumentException("maxAllowed <= 0");
        }
        maxAllowed = max;
    }

    /**
     * hashCode override
     */
    public int hashCode() {
        // this results in the something like 5 per 15 mins hashing to the same
        // value as 10 per 30 mins
        return duration + maxAllowed;
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

        // it's a RateKeyType, but it's not this one, so check it for equality
        RateLimit limit = (RateLimit) o;
        // this results in, for example, 5 per 15 mins being equivalent to 10 per 30 mins
        // because it's 1 attempt every 180 seconds
        return (duration / maxAllowed) == (limit.duration / limit.maxAllowed);
    }

    //the format is max/duration.
    public static RateLimit parse(final String rl) {
        if (Strings.isNullOrEmpty(rl)) {
            return new RateLimit();
        }

        String[] rls = rl.split("\\/");

        if (rls.length == 2) {
            int max = Integer.valueOf(rls[0]);
            int duration = Integer.valueOf(rls[1]);

            return new RateLimit(duration, max);
        } else {
            return new RateLimit();
        }
    }
}
