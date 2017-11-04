/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.collection;

import com.enjoyf.platform.util.thread.DiePoolThread;
import com.enjoyf.platform.util.thread.RequestThread;
import com.enjoyf.platform.util.thread.RequestThreadManager;

/**
 * A class which encapsulats N consumer threads to logicProcess incoming
 * requests. Producer threads (one or more) will call add() to
 * submit an object for processing. One of the N consumer threads will
 * then be used to logicProcess the request. Note that the requests are
 * queued up, so the add() call will return immediately. One must
 * be aware of producer/consumer issues. If production is greater
 * than consumption, the queue will grow until memory runs out. Also,
 * since there are N consumer threads, the QueueListener object will
 * be called in a multi-threaded fashion.
 */
public class QueueThreadN extends DiePoolThread {
    Queue queue = null;
    QueueListener listener = null;
    RequestThreadManager threadManager;
    private long lastProcessTime = 0;

    /**
     * Create the object with the passed in listener and the default queue.
     *
     * @param threadPoolSize This is the number of threads that will
     *                       be used to logicProcess requests on. What this means is that the
     *                       QueueListener object may be invoked in a multi-threaded fashion.
     * @param l              The listener object for processing requests.
     */
    public QueueThreadN(int threadPoolSize, QueueListener l) {
        this(threadPoolSize, l, new QueueList());
    }

    public QueueThreadN(int threadPoolSize, QueueListener l, String infoId) {
        this(threadPoolSize, l, new QueueList(), infoId);
    }

    /**
     * The queue will call the listener whenever an object gets added
     * to the queue. The user must be aware of producer/consumer issues,
     * if the producer goes faster than the consumer, you will eventually
     * run out of memory.
     *
     * @param l      The listener object.
     * @param queue  The queue object to use.
     * @param infoId An identifier used for log/dbg purposes.
     */
    public QueueThreadN(int threadPoolSize, QueueListener l, Queue queue, String infoId) {
        listener = l;
        this.queue = queue;
        threadManager = new RequestThreadManager(threadPoolSize, infoId);
        threadManager.setPrefix("QT:" + infoId);
        setName("QueueThreadN:" + infoId + ":" + hashCode());
        start();
    }

    public QueueThreadN(int threadPoolSize, QueueListener l, Queue queue) {
        this(threadPoolSize, l, queue, "QueueThreadN");
    }

    /**
     * Set the name of the internal thread so one can distinguish
     * between different QueueThreadN objects in use.
     */
    public void setThreadName(String threadName) {
        setName("QueueThreadN:" + threadName + ":" + hashCode());
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
        return queue.size();
    }

    /**
     * Add an element to queue. The queue thread will be notified
     * that something is in the queue, and the listener will be
     * invoked with the stored object.
     */
    public synchronized void add(Object obj) {
        queue.add(obj);

        notify();
    }

    /*default*/
    synchronized Object get() {
        while (queue.size() == 0) {
            if (shouldDie()) {
                return null;
            }

            try {
                wait();
            }             catch (Exception e) {
            }
        }

        lastProcessTime = System.currentTimeMillis();
        return queue.get();
    }

    /**
     * Return the last time an object was pulled from the queue.
     */
    public long getLastProcessTime() {
        return lastProcessTime;
    }

    /**
     * Remove all elements from the queue.
     */
    public synchronized void clear() {
        queue.clear();
    }

    class MyProcessor implements RequestThread.Processor {
        private Object obj;

        MyProcessor(Object obj) {
            this.obj = obj;
        }

        public void process() {
            listener.process(obj);
        }
    }

    public void run() {
        while (!shouldDie()) {
            Object obj = get();

            if (shouldDie()) {
                break;
            }

            RequestThread requestThread = threadManager.getThreadBlock();
            
            MyProcessor processor = new MyProcessor(obj);
            requestThread.process(processor);
        }
    }
}
