/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.rate;

import java.io.Serializable;

/**
 * Encapsulation of the concept of a rate that can be exceeded
 *
 * @author Yin Pengyi
 */

public class Rate implements Serializable {
    private boolean exceeded;

    /**
     * Constructs a Rate
     */
    public Rate() {
    }

    /**
     * Constructs a Rate
     *
     * @param exceeded - whether this rate has been exceeded
     */
    public Rate(boolean exceeded) {
        setExceeded(exceeded);
    }

    /**
     * Whether this rate has been exceeded
     *
     * @return boolean - true if rate has been exceeded
     */
    public boolean isExceeded() {
        return exceeded;
    }

    /**
     * Sets whether this rate has been exceeded
     *
     * @param exceeded - true if rate has been exceeded
     */
    public void setExceeded(boolean exceeded) {
        this.exceeded = exceeded;
    }

    /**
     * String representation
     *
     * @return String - string representation
     */
    public String toString() {
        return exceeded ? "Rate Exceeded" : "Rate Not Exceeded";
    }
}