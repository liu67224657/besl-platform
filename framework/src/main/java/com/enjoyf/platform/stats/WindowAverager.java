/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.stats;

/**
 * A class to keep a window of averages.
 */
public class WindowAverager {
    private long counts[];
    private int index;
    private int count;
    private int period = -1;
    private long lastTime;
    private boolean resetFlag = true;

    /**
     * Create the object with the specified window size.
     *
     * @param windowSize This specifies how many data elements
     *                   the object will maintain.
     */
    public WindowAverager(int windowSize) {
        counts = new long[windowSize];
    }

    /**
     * Specify the time interval in msecs between each data point.
     * This is to obsolete old data when calling getAverage(). Eg, if
     * period is 1 minute, and the window size is 10, and the last time
     * you called add() was more than 10 minutes ago, then getAverage()
     * will return 0.
     */
    public void setPeriod(int msecs) {
        period = msecs;
    }

    /**
     * Return the average over the specified window.
     *
     * @param windowSize The size of the window whose avg. we want.
     *                   Needs to be less than the windowSize specified at construction
     *                   time.
     */
    public synchronized final long getAverage(int windowSize) {
        if (period != -1 && (System.currentTimeMillis() - lastTime > counts.length * period)) {
            if (!resetFlag) {
                for (int i = 0; i < counts.length; i++) {
                    counts[i] = 0;
                }
                resetFlag = true;
            }
            return 0;
        }

        long total = 0;
        int len = counts.length;
        int offset = index + len - 1;

        if (windowSize > count) {
            windowSize = count;
        } else if (windowSize > len) {
            windowSize = len;
        }

        for (int i = 0; (i < windowSize); i++) {
            total += counts[(offset - i) % len];
        }

        double avg = (windowSize == 0) ? 0.0 : ((double) total) / ((double) windowSize);

        return ((long) (avg + 0.5));
    }

    /**
     * Add a data element to the next window slot. Data elements
     * should be added at whatever every T time interval the user
     * is interested in.
     *
     * @param stat The value to add. The current slot number is
     *             incremented (and will wrap around).
     */
    public synchronized void add(long stat) {
        resetFlag = false;
        lastTime = System.currentTimeMillis();
        counts[index] = stat;
        index = (index + 1) % counts.length;
        count++;
    }
}
