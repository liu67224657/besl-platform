package com.enjoyf.platform.util.collection;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.thread.DiePoolThread;

/**
 * USE MapMaker or EHCache
 * Class to manage a collection of objects which may expire
 * over time. The key of the object is supplied by the client
 * and is also expected to be an object.
 */
@Deprecated
public class ObjMgrBase<K, T> {
    private boolean die = false;

    private long expTimeout = 1000 * 10 * 60;
    private boolean resetTime = true;

    private ExpireThread expireThread;
    private Map<K, Element> container;

    /**
     * Construct an object manager.
     */
    public ObjMgrBase() {
        this(5 * 60, 15 * 3600, 100, true);
    }

    /**
     * Construct an object mgr with a size hint.
     */
    public ObjMgrBase(int size) {
        this(5 * 60, 15 * 3600, size, true);
    }

    /**
     * Construct an object manager.
     *
     * @param expireInterval The interval at which to run the
     *                       expire thread (in seconds).
     * @param expireTimeout  The timeout for the stored objects
     *                       (in seconds).
     */
    public ObjMgrBase(int expireInterval, int expireTimeout) {
        this(expireInterval, expireTimeout, 100, true);
    }

    /**
     * Construct an object manager.
     *
     * @param expireInterval The interval at which to run the
     *                       expire thread (in seconds).
     * @param expireTimeout  The timeout for the stored objects
     *                       (in seconds).
     * @param size           The initial size of the container.
     */
    public ObjMgrBase(int expireInterval, int expireTimeout, int size) {
        this(expireInterval, expireTimeout, size, true);
    }

    /**
     * Construct an object manager.
     *
     * @param expireInterval The interval at which to run the
     *                       expire thread (in seconds).
     * @param expireTimeout  The timeout for the stored objects
     *                       (in seconds).
     * @param resetTime      Flag to control whether to reset the time
     *                       when object is accessed.
     */
    public ObjMgrBase(int expireInterval, int expireTimeout, boolean resetTime) {
        this(expireInterval, expireTimeout, 100, resetTime);
    }

    /**
     * Construct an object manager.
     *
     * @param expireInterval The interval at which to run the
     *                       expire thread (secs).
     * @param expireTimeout  The timeout for the stored objects (secs).
     * @param size           The initial size of the container.
     * @param resetTime      Flag to control whether to reset the time
     *                       when accessed.
     */
    public ObjMgrBase(int expireInterval, int expireTimeout, int size, boolean resetTime) {

        this.container = new HashMap<K, Element>();
        this.expTimeout = expireTimeout * 1000;
        this.resetTime = resetTime;

        this.expireThread = new ExpireThread(expireInterval * 1000);
        this.expireThread.start();
    }

    /**
     * Set a thread id. Call this so that log stmts print out with
     * something meaningful.
     */
    public void setThreadId(String id) {
        if (expireThread != null) {
            expireThread.setName("ExpireThread:" + id + ":" + expireThread.hashCode());
        }
    }

    /**
     * Retrieve the count of elements.
     */
    public int getCount() {
        return container.size();
    }

    /**
     * Add an element to the container.
     */
    public synchronized void put(K key, T obj) {
        container.put(key, new Element(key, obj));
    }

    /**
     * Add an element to the container w/ specific timeout
     *
     * @param key     is the key of the object
     * @param obj     is the object to be stored
     * @param timeout is the timeout for the object (in secs)
     */
    public synchronized void put(K key, T obj, int timeout) {
        container.put(key, new Element(key, obj, timeout * 1000));
    }

    /**
     * Need to call this to clean things up in a timely manner.
     */
    public void die() {
        if (die) {
            return;
        }

        die = true;
        expireThread.die();
    }

    protected void finalize() {
        die();
    }

    public synchronized T get(K key) {
        Element e = container.get(key);
        if (e == null) {
            return null;
        }

        if (resetTime) {
            e.reset();
        }

        return e.data;
    }

    /**
     * Removes an object from the container.
     *
     * @param key The key of the object.
     * @return Returns the contained object. Null if it did not exist.
     */
    public synchronized Object remove(K key) {
        Element e = container.remove(key);
        if (e == null) {
            return null;
        }

        return e.data;
    }

    /**
     * Utility class. This is a thread used to expire objects.
     */
    class ExpireThread extends DiePoolThread {
        private int interval;

        ExpireThread(int interval) {
            this.interval = interval;

            setName("ObjMgrBase.ExpireThread:" + hashCode());
        }

        public void run() {
            while (!shouldDie()) {
                Utility.sleep(interval);

                if (shouldDie()) {
                    break;
                }

                try {
                    doExpire();
                } catch (Exception e) {
                    GAlerter.lab("ObjMgrBase: Unexpected exception: " + e, e);
                }
            }
        }

        private void doExpire() {
            synchronized (ObjMgrBase.this) {

                long curTime = System.currentTimeMillis();

                int count = 0;
                for (Iterator<Element> itr = container.values().iterator(); itr.hasNext(); ) {
                    Element e = itr.next();

                    if (e.shouldExpire(curTime, expTimeout)) {
                        count++;
                        itr.remove();
                    }
                }
            }
        }
    }

    /**
     * Utility class. Objects of this type are what is stored in our
     * container.
     */
    class Element {
        private K key;
        private T data;
        private long timestamp;
        private int timeout; // individual timeout for the object

        public Element(K key, T obj) {
            this(key, obj, 0);
        }

        public Element(K key, T obj, int individualTimeout) {
            this.key = key;
            this.data = obj;
            this.timeout = individualTimeout;

            reset();
        }

        public void reset() {
            this.timestamp = System.currentTimeMillis();
        }

        public boolean shouldExpire(long t, long timeout) {
            if (this.timeout == 0) {
                return (t - timestamp > timeout);
            } else {
                return (t - timestamp > this.timeout);
            }
        }
    }
}
