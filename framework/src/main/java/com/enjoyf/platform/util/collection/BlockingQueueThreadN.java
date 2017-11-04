/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.util.collection;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-1-20 下午5:42
 * Description:
 * A blocking queue. Objects are processed by a pool of threads. If
 * the queue is at a certain size, the add() call will block until the
 * queue drains below that size.
 */
public class BlockingQueueThreadN extends QueueThreadN {
    private Object lock = new Object();
    private int sizeLimit;

    /**
     * @param threadPoolSize How many threads to allocate for processing
     *                       of objects.
     * @param l              The QueueListener to use. All objects to be processed are
     *                       sent to this listener.
     * @param sizeLimit      The max size of the queue.
     */
    public BlockingQueueThreadN(int threadPoolSize, QueueListener l, int sizeLimit) {
        this(threadPoolSize, l, sizeLimit, "anonymous");
    }

    /**
     * @param threadPoolSize How many threads to allocate for processing
     *                       of objects.
     * @param l              The QueueListener to use. All objects to be processed are
     *                       sent to this listener.
     * @param sizeLimit      The max size of the queue.
     * @param infoId         A String to tag the names of the processing threads
     *                       (for debugging purposes).
     */
    public BlockingQueueThreadN(int threadPoolSize, QueueListener l, int sizeLimit, String infoId) {
        super(threadPoolSize, l, infoId);
        this.sizeLimit = sizeLimit;
    }

    /**
     * Add an object to the queue for processing. NOTE: if the queue has
     * reached 'sizeLimit', it will block until the queue drops down
     * below that size.
     */
    public void add(Object obj) {
        //--
        // Silently ignore any more objects for processing since this
        // object is dying.
        //--
        if (shouldDie()) {
            return;
        }

        synchronized (lock) {
            //--
            // Don't block if we are dying, just go ahead and empty
            // the queue.
            //--
            while (size() > sizeLimit && !shouldDie()) {
                try {
                    lock.wait();
                } catch (Exception e) {
                }
            }
        }

        super.add(obj);
    }

    Object get() {
        Object obj = super.get();

        //--
        // Let's notify the add() call if we are below the sizeLimit
        // in case it's waiting. It may not be.
        //--
        synchronized (lock) {
            if (size() < sizeLimit) {
                lock.notify();
            }
        }

        return obj;
    }

    /**
     * Kill this object and free up any data structures. Note that any
     * queued up requests may not be processed before this object
     * dies.
     */
    public void die() {
        super.die();

        synchronized (lock) {
            lock.notifyAll();
        }
    }
}
