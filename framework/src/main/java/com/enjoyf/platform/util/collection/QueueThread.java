/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.collection;

import java.util.Collection;
import java.util.Iterator;

import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.thread.DiePoolThread;

/**
 * A class to implement a queue which processes objects on its
 * own thread. The default queue used is a FIFO queue which has
 * no size limitation, but a caller can use any object implementing
 * the com.enjoyf.platform.util.collection.Queue interface.
 */
public class QueueThread extends DiePoolThread {
    private Queue threadQueue = null;
    private QueueListener queueListener = null;

    /**
     * Create the object with the passed in listener and the default queue.
     */
    public QueueThread(QueueListener l) {
        this(l, new QueueList(), "QueueThread");
    }

    public QueueThread(QueueListener l, String threadName) {
        this(l, new QueueList(), threadName);
    }

    public QueueThread(QueueListener l, Queue queue) {
        this(l, queue, "QueueThread");
    }

    /**
     * The queue will call the listener whenever an object gets added
     * to the queue. The user must be aware of producer/consumer issues,
     * if the producer goes faster than the consumer, you will eventually
     * run out of memory.
     *
     * @param l     The listener object.
     * @param queue The queue object to use.
     */
    public QueueThread(QueueListener l, Queue queue, String threadName) {
        setName(threadName + ":" + hashCode());
        queueListener = l;
        threadQueue = queue;
        start();
    }

    public void die() {
        super.die();
        synchronized (this) {
            //--
            // Wake up the thread in case it's sitting in a wait.
            //--
            this.notify();
        }
    }

    public synchronized int size() {
        return threadQueue.size();
    }

    /**
     * Add an element to queue. The queue thread will be notified
     * that something is in the queue, and the listener will be
     * invoked with the stored object.
     */
    public synchronized void add(Object obj) {
        threadQueue.add(obj);
        notify();
    }

    public synchronized void addAll(Collection collection) {
        //--
        // Admittedly ugly, but the Queue interface is in a 1.0.2 dir
        //--
        if (threadQueue instanceof QueueList) {
            QueueList ql = (QueueList) threadQueue;
            ql.addAll(collection);
        } else {
            Iterator itr = collection.iterator();
            while (itr.hasNext()) {
                threadQueue.add(itr.next());
            }
        }
        notify();
    }

    /*default*/
    synchronized Object get() {
        while (threadQueue.size() == 0) {
            if (shouldDie()) {
                return null;
            }

            try {
                wait();
            } catch (Exception e) {
            }
        }
        return threadQueue.get();
    }

    /**
     * Remove all elements from the queue.
     */
    public synchronized void clear() {
        threadQueue.clear();
    }

    public void run() {
        while (!shouldDie()) {
            Object obj = get();
            if (shouldDie()) {
                break;
            }

            try {
                queueListener.process(obj);
            } catch (Exception e) {
                GAlerter.lab("QueueThread: exception caught: " + e, e);
            }
        }
    }
}
