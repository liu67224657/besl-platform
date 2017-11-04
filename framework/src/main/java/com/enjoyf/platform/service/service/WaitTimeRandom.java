/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import java.util.Random;

/**
 * A WaitTime implementation that randomizes.
 */
public class WaitTimeRandom implements WaitTime {
    private double throttleValue;
    private int secondsRange;
    private Random random = new Random();
    private int maxWaitTime;

    /**
     * @param throttle how much to throttle the passed in waitTime. As
     *                 getNextWaitTime() is called, it will keep increasing the returned
     *                 value.
     * @param rang     The value returned is actually going to be
     *                 +- a random number generated within this seconds range. Eg,
     *                 If the wait time computed is 10 seconds, and secondsRange=5,
     *                 then the value returned will range from 5-15 seconds (or
     *                 5000-15000).
     * @param maxWait  This is the max amount of time to wait.
     */
    public WaitTimeRandom(double throttle, int rang, int maxWait) {
        throttleValue = throttle;
        secondsRange = rang;
        maxWaitTime = maxWait;
    }

    /**
     * Returns the next wait time in msecs.
     */
    public long getNextWaitTime(long curWaitTime) {
        long retval = (long) ((double) curWaitTime * throttleValue);
        int val = (Math.abs(random.nextInt()) % secondsRange) + 1;

        //--
        // Convert the random integer gotten from secondsRange to msecs
        // and then subtract half it's value so we can get a +- range.
        //--
        val *= 1000;
        val -= secondsRange / 2;
        retval += val;

        //--
        // Never return less than what got passed in.
        //--
        if (retval < curWaitTime) {
            retval = curWaitTime;
        }

        //--
        // Never return more than the max.
        //--
        if (maxWaitTime > 0 && retval > maxWaitTime) {
            retval = maxWaitTime;
        }

        return retval;
    }
}
