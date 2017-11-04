/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A manager of request threads. This class instantiates a number
 * of thread objects to use.
 */
public class RequestThreadManager {

    private static final Logger logger = LoggerFactory.getLogger(RequestThreadManager.class);

    /**
     * Container for request threads that are available.
     */
    private Vector<RequestThread> availables = new Vector<RequestThread>();
    /**
     * Container for request threads that are in use.
     */
    private ConcurrentMap<Integer, RequestThread> inUse = new ConcurrentHashMap<Integer, RequestThread>();

    private int curCount = 0;
    private int maxCount;
    private String prefix = null;
    /**
     * Some sort of tag to identify who is using this object.
     */
    private String infoId = "NONE";

    /**
     * Allocate some fixed number of threads.
     */
    public RequestThreadManager(int n) {
        this(n, "NONE");
    }

    public RequestThreadManager(int n, String infoId) {
        maxCount = n;
        this.infoId = infoId;
    }

    public String getInfoId() {
        return infoId;
    }

    /**
     * Returns true if all threads are in use.
     */
    public synchronized boolean isFull() {
        return inUse.size() >= maxCount;
    }

    /**
     * Set a prefix to use when creating request threads.
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Get a thread, but if there are none available right now,
     * block until there is one.
     */
    public synchronized RequestThread getThreadBlock() {
        RequestThread rt = null;
        while (rt == null) {
            rt = getThread();
            if (rt == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("RequestThreadManager.getThreadBlock: " + " waiting for a thread..");
                }

                try {
                    this.wait();
                } catch (InterruptedException e) {
                    //
                }
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("RequestThreadManager.getThreadBlock: " + "Returning thread: " + rt.getName());
        }

        return rt;
    }

    /**
     * Find a thread that's not busy.
     */
    public synchronized RequestThread getThread() {
        if (logger.isDebugEnabled()) {
            logger.debug("RequestThreadManager.getThread(): getting a thread");
        }

        RequestThread rt = null;
        synchronized (availables) {
            if (availables.size() == 0) {
                p_allocThread();
            }

            if (availables.size() == 0) {
                return null;
            }

            rt = availables.elementAt(0);
            availables.removeElementAt(0);
            inUse.put(rt.getId(), rt);
        }

        return rt;
    }

    /**
     * Return a thread to pool of unused threads.
     */
    public synchronized void returnThread(RequestThread rt) {
        inUse.remove(rt.getId());
        availables.addElement(rt);

        if (logger.isDebugEnabled()) {
            logger.debug("RequestThreadManager.returnThread: " + "Thread returned: " + rt.getName());
        }

        this.notify();
    }

    private void p_allocThread() {
        if (curCount >= maxCount) {
            return;
        }

        RequestThread rt = new RequestThread(this, prefix);
        curCount++;
        availables.addElement(rt);
    }

    /**
     * Kill off any threads that this container might possess.
     */
    public synchronized void die() {
        if (logger.isDebugEnabled()) {
            logger.debug("RequestThreadManager.die(): ENTER");
        }

        synchronized (availables) {
            p_shutDownThreads(availables);
            availables.removeAllElements();
        }

        synchronized (inUse) {
            p_shutDownThreads(inUse.values());
            inUse.clear();
        }
    }

    private void p_shutDownThreads(Collection<RequestThread> threads) {
        for (RequestThread rt : threads) {
            rt.die();

            synchronized (rt) {//why synchronize on rt ????
                rt.notify();
            }
        }
    }

    protected void finalize() {
        if (logger.isDebugEnabled()) {
            logger.debug("RequestThreadManager.finalize()");
        }

        die();
    }
}
