/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.thread;

/**
 * A singleton to manage a single thread pool. This thread pool
 * should be used by code that runs on the "client" side via
 * service service code.
 */
public class ThreadPoolUtil {
    private ThreadPool pool;

    private static ThreadPoolUtil instance = new ThreadPoolUtil();

    private ThreadPoolUtil() {
        pool = new ThreadPool(10);
    }

    public static ThreadPoolUtil instance() {
        return instance;
    }

    /**
     * Start up a thread for this Runnable.
     *
     * @param name The name to give to the thread.
     * @return Returns the Thread object in use.
     */
    public Thread startThread(Runnable runnable, String name) {
        return pool.startThread(runnable, name);
    }

    /**
     * Return the ThreadPool object.
     */
    public ThreadPool getPool() {
        return pool;
    }
}
