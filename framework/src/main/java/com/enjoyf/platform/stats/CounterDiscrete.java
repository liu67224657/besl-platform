package com.enjoyf.platform.stats;


/**
 * Class to "count" the # of times something happens over a particular
 * time period. Counts are kept in slots, and are migrated to "older"
 * slots as time goes by.
 * <p/>
 * Eg, if you create the object with slots==5, and period=60*1000, and
 * you call add() 10 times in the first minute, and 5 times in the 2nd
 * minute, then getCount(0) will return 5, and getCount(1) will return 10.
 */
public class CounterDiscrete {
    private int period;
    private long[] slots;
    private long lastTime;

    private static long bugCount;

    /**
     * @param slots  How many slots to track.
     * @param period in msecs.
     */
    public CounterDiscrete(int slots, int period) {
        if (slots <= 0) {
            throw new IllegalArgumentException("slots OOB!");
        }
        if (period < 1000) {
            throw new IllegalArgumentException("Period must be >= 1000");
        }

        lastTime = System.currentTimeMillis();
        this.slots = new long[slots];
        this.period = period;
    }

    /**
     * Increment the count of something happening at this moment in time.
     * If the current slot tracking the count has expired, it is moved
     * to its appropriate slot (or expired altogether from the queue).
     * This may be O(n) (n='slots' in ctor) if the top element has expired.
     */
    public synchronized void add() {
        long curTime = System.currentTimeMillis();
        p_roll(curTime);
        slots[0]++;
    }

    private void p_roll(long curTime) {
        long diff = curTime - lastTime;
        if (diff < period) {
            return;
        }

        int newIdx = -99;
        int slotShift = -99;
        try {
            slotShift = (int) diff / period;
            for (int i = slots.length - 1; i >= 0; i--) {
                newIdx = i + slotShift;
                if (newIdx < slots.length) {
                    slots[newIdx] = slots[i];
                }

                slots[i] = 0;
            }
        }
        catch (Exception e) {
            //--
            // We're getting an ArrayIndexOutOfBounds exception that is
            // a jvm bug. A small test case was put together and submitted
            // to sun, although haven't heard back. The small test case
            // took several hours to reproduce the problem. This problem
            // happens a lot for us in prod code.
            //
            // Note that it's not a big deal to run into this exception
            // for this particular class.  The "roll" operation fails this
            // time, and will be performed next time around.
            //--
            p_dealWithException();
        }
        lastTime = curTime;
    }

    /**
     * Retrieve the count at a particular slot, 0 being the "current" slot.
     *
     * @throws IllegalArgumentException If the slotIdx is out of bounds.
     */
    public synchronized long getCount(int slotIdx) {
        if (slotIdx >= slots.length || slotIdx < 0) {
            throw new IllegalArgumentException("slotIdx OOB!");
        }

        p_roll(System.currentTimeMillis());
        return slots[slotIdx];
    }

    /**
     * Return the average over the last N slots.
     *
     * @throws IllegalArgumentException Thrown if nslots exceeds the
     *                                  # of slots the object was created with.
     */
    public synchronized int getAverage(int nslots) {
        if (nslots > slots.length || nslots <= 0) {
            throw new IllegalArgumentException(
                    "nslots must be <= # of slots!");
        }
        long sum = 0;
        for (int i = 0; i < nslots; i++) {
            sum += slots[i];
        }

        return (int) (sum / nslots);
    }

    /**
     * A utility method to deal with the bug we are running into.
     * In order to not over-report (since many objects per jvm are
     * created), we use static attributes and make sure to synch on the
     * method as many threads could be in this code at the same time.
     */
    private synchronized static void p_dealWithException() {
        if (bugCount < 0) {
            bugCount = 0;
        }
    }
}
