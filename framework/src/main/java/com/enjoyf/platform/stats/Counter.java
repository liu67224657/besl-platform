/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.stats;

import com.enjoyf.platform.util.collection.RollingQueue;

/**
 * This class is used to track the count of "something" over time.
 * It supports the notion that the counts exist for some amount of time
 * over a particular period. It also supports N counts, one per period.
 * <p/>
 * It is intended that this class be used for small values of N since most
 * operations are O(n).
 * <p/>
 * Example:
 * <p/>
 * Assume you ctor this object with slots=10, and period=60*1000 (1 minute).
 * If at time T1 you call add(), the first slot will have a count of 1,
 * and calling getAverage(1) will return 1. Calling getAverage(n) with n>=2
 * will return 0, since you'll be doing 1/n.
 * <p/>
 * If at time T1+61 secs you call add() again, then slot(0) will have a
 * count of 1, and slot 1 will have a count of 1 since the old value at slot
 * 0 has expired and been moved down a slot. Calling getAverage(1) will
 * return 1, and calling getAverage(2) will return 1 (1+1/2).
 * Calling getAverage(n) with n>=3 will return 0.
 */
public class Counter {
    private RollingQueue queue;
    private int period;
    private int slots;

    /**
     * @param slots  How many slots to track.
     * @param period in msecs.
     */
    public Counter(int slots, int period) {
        this.slots = slots;
        this.period = period;
        queue = new RollingQueue(slots, period);
    }

    /**
     * Increment the count of something happening at this moment in time.
     * If the current object tracking the count has expired, it is moved
     * to its appropriate slot (or expired altogether from the queue).
     * This may be O(n) (n='slots' in ctor) if the top element has expired.
     */
    public synchronized void add() {
        add(1);
    }

    /**
     * Increment the count of something happening at this moment in time.
     * If the current object tracking the count has expired, it is moved
     * to its appropriate slot (or expired altogether from the queue).
     * This may be O(n) (n='slots' in ctor) if the top element has expired.
     *
     * @param count The amount to increment by.
     */
    public synchronized void add(long count) {
        long curTime = System.currentTimeMillis();
        Data data;
        do {
            data = (Data) queue.top();
            if (data == null) {
                data = new Data(curTime);
                queue.set(data);
            } else if (data.hasExpired(curTime, period)) {
                queue.roll(curTime);
                data = null;
            }
        } while (data == null);
        data.add(count);
    }

    /**
     * Returns the average value over the previous 'nslots'. Note that this
     * includes empty slots. So if you have one slot with data, and 4 slots
     * w/o, and you call getAverage(5), then the average will be the count
     * in the one slot divided by 5.
     * This operation is O(n) (n='slots' in ctor).
     *
     * @param nslots The number of slots to average over.
     * @throws IllegalArgumentException if nslots>slots
     */
    public synchronized int getAverage(int nslots) {
        if (nslots <= 0 || nslots > slots) {
            throw new IllegalArgumentException(
                    "nslots <=0 || nslots > slots: nslots=" + nslots);
        }

        long curTime = System.currentTimeMillis();
        //--
        // Roll the queue to make sure data is up to date.
        //--
        queue.roll(curTime);

        //--
        // Just compute the average over the window size.
        //--
        long sum = 0;
        long count = 0;
        for (int i = 0; i < nslots; i++) {
            Data data = (Data) queue.get(i);
            sum += data == null ? 0 : data.getCount();
            count++;
        }
        return (int) (count == 0 ? 0 : sum / count);
    }

    /**
     * Return a printable version of the state of the queue, for debugging.
     */
    public String getData() {
        return queue.getData();
    }

    static class Data implements RollingQueue.Timestamped {
        private long m_timestamp;
        private long m_count;

        Data(long timestamp) {
            m_timestamp = timestamp;
        }

        void add(long count) {
            m_count += count;
        }

        void add() {
            add(1);
        }

        public long getTimestamp() {
            return m_timestamp;
        }

        public boolean hasExpired(long curTime, int period) {
            return (int) (curTime - m_timestamp) > period;
        }

        long getCount() {
            return m_count;
        }

        public String toString() {
            return "count=" + m_count + ":ts=" + m_timestamp;
        }
    }
}
