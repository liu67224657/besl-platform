/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.collection;

import java.util.ArrayList;
import java.util.List;

import com.enjoyf.platform.util.Utility;

/**
 * A queue that allows multiple threads to add() to it, and multiple threads
 * to get() from it where the get() call will block if there is nothing
 * in the queue. You really want to use this when you have N producer
 * threads, and N consumer threads. Of course, you need to be careful
 * if production exceeds consumption. Depending on the underlying queue
 * implementation (eg, if it just keeps growing), you might run into
 * memory problems.
 */
public class MultiThreadQueue implements Queue {
    private Queue queue;

    /**
     * Pass in underlying implementation.
     *
     * @param queue Some implementation of the underlying queue to use
     *              can be a FIFO, LIFO, whatever queue.
     */
    public MultiThreadQueue(Queue queue) {
        this.queue = queue;
    }

    /**
     * Default ctor which uses an unbounded FIFO queue.
     */
    public MultiThreadQueue() {
        queue = new QueueList();
    }

    /**
     * Add an object to the queue.
     */
    public void add(Object obj) {
        synchronized (queue) {
            queue.add(obj);
            queue.notifyAll();
        }
    }

    /**
     * Retrieve an object from the queue. If no elements, it will block.
     */
    public Object get() {
        synchronized (queue) {
            while (queue.size() == 0) {
                try {
                    queue.wait();
                }
                catch (InterruptedException e) {
                }
            }
            return queue.get();
        }
    }

    /**
     * Retrieve an object from the queue. If no elements, it will block.
     * If the call times out, it will return a null.
     *
     * @param timeoutMillis The timeout in msecs.
     */
    public Object get(int timeoutMillis) {
        synchronized (queue) {
            if (queue.size() > 0) {
                return queue.get();
            }

            Utility.wait(queue, timeoutMillis);
            return queue.get();
        }
    }

    public int size() {
        return queue.size();
    }

    public void clear() {
        queue.clear();
    }

    /**
     * Retrieves any current events w/o blocking or waiting for any
     * incoming ones. The return List will never be null but it might
     * be empty.
     */
    public List getRemaining() {
        ArrayList list = new ArrayList();
        Object obj = null;
        while ((obj = queue.get()) != null) {
            list.add(obj);
        }

        return list;
    }
}
