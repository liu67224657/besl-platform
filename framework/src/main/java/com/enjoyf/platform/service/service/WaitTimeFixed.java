/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

/**
 * A concrete WaitTime that uses a simple throttle to keep incrementing
 * a value.
 */
public class WaitTimeFixed implements WaitTime {
    private double throttleValue;

    public WaitTimeFixed(double throttle) {
        throttleValue = throttle;
    }

    public long getNextWaitTime(long curWaitTime) {
        return (long) ((double) curWaitTime * throttleValue);
    }
}
