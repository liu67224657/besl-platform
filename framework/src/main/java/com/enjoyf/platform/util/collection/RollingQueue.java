/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.collection;

/**
 * This is a strange class, and may not be very reusable.
 * <p/>
 * What it does is maintain a fixed size list of timestamped objects, and
 * pushes them towards the end of the queue. What's odd is that you only
 * ever insert an element at the very beginning. As time goes by, this
 * element moves along the queue, but the key thing is that this moving
 * happens only when the client invokes 'roll()'.
 */
public class RollingQueue {
    private Timestamped[] m_data;
    private int m_size;
    private int m_period;

    /**
     * @param size   The fixed size of the queue.
     * @param period The period in msecs between each slot. See the
     *               roll() method for more info.
     */
    public RollingQueue(int size, int period) {
        if (size <= 0) {
            throw new IllegalArgumentException("size<=0!");
        }

        if (period <= 0) {
            throw new IllegalArgumentException("period<=0!");
        }

        m_size = size;
        m_period = period;
        m_data = new Timestamped[size];
    }

    /**
     * Interface to be implemented by users of this queue.
     */
    public interface Timestamped {
        /**
         * Should return the timestamp of the object, in msecs since
         * the epoch.
         */
        public long getTimestamp();
    }

    /**
     * Sets an element to the top of the queue. Note that the top
     * element is simply replaced. If you want to make sure you've
     * rolled first, then call roll() before calling this method.
     *
     * @param obj The timestamped object. Note that you should never
     *            insert elements with timestamps < than any of the current elements
     *            in the queue.
     */
    public void set(Timestamped obj) {
        m_data[0] = obj;
    }

    /**
     * Retrieve the ith element. The reference is returned.
     *
     * @param idx If idx==0, it returns the top element, idx cannot
     *            be >= than the 'size' specified at construction time.
     * @throws IllegalArgumentException Thrown if idx<0 || idx>=size.
     */
    public Timestamped get(int idx) {
        if (idx < 0 || idx >= m_size) {
            throw new IllegalArgumentException("idx<0 || idx>=size");
        }

        return m_data[idx];
    }

    /**
     * Returns the top element, shorthand for get(0).
     */
    public Timestamped top() {
        return m_data[0];
    }

    /**
     * Ok, this is the method that moves objects from slot to slot.
     * All objects are examined to see if they should move down 1 or more
     * slots, or even completely off of the queue. Eg, if period==10, and
     * the timestamp of the first slot==20, and the currentTime==31, then
     * the object moves down one slot. In contrast, if the period==10,
     * and the timestamp of the first slot==20, and the currentTime==51,
     * then it moves down 3 slots. In the last example, if there were only
     * 3 slots, then the element would get removed from the queue.
     * The same holds for the other slots.
     * <p/>
     * NOTE: This operation is O(n), and it is only performed when this
     * method is called. This means that if elements are retrieved w/o
     * having recently called roll(), they may technically have expired
     * from the list, but you'll still get them.
     */
    public void roll(long curTime) {
        //--
        // Move the objects from slot to slot, if enough time has elapsed.
        //
        for (int i = m_size - 1; i >= 0; i--) {
            Timestamped data = m_data[i];
            if (data == null) {
                continue;
            }

            int diff = (int) (curTime - data.getTimestamp());
            int newSlot = diff / m_period;

            //--
            // Somehow we're going back in time, don't change anything.
            //--
            if (newSlot <= i) {
                continue;
            }

            //--
            // The data has fully expired from the queue, null it out.
            //--
            if (newSlot >= m_size) {
                m_data[i] = null;
                continue;
            }
            //--
            // If here, we're moving the data one or more slots down
            // clearing the previous data.
            //--
            m_data[newSlot] = m_data[i];
            m_data[i] = null;
        }
    }

    public String getData() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < m_size; i++) {
            sb.append(m_data[i] == null ? "null" : m_data[i].toString());
            sb.append(",");
        }
        return sb.toString();
    }
}
