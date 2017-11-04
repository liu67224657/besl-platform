package com.enjoyf.platform.util.thread;

import com.enjoyf.platform.util.Utility;

/**
 * A class to derive from instead of Thread when a thread
 * from a pool is wanted. Use this when converting a class that
 * currently extends Thread to extend this class instead.
 * <p/>
 * To use, derive from this class and call start().
 * <p/>
 * getBoardName()/setBoardName() methods are provided at this level for convenience.
 * <p/>
 * Access to the underlying Thread object can be performed via getThread()
 * or getThreadBalk(), but be careful when using these methods (see the
 * methods themselves). In general, avoid having to use the underlying
 * Thread object if you can; it can get tricky.
 */
public abstract class PoolThread implements Runnable {

    private String threadName = "anonymous";
    protected Thread thread = null;

    /**
     * Call start() as you normally would to start up a thread.
     */
    public void start() {
        thread = ThreadPoolUtil.instance().startThread(this, threadName);
    }

    /**
     * Set the name of the thread to use.
     */
    public void setName(String name) {
        if (name == null) {
            return;
        }
        //--
        // We need to get funky, depending on whether or not start()
        // has been called yet.
        //--
        if (thread != null) {
            thread.setName(name);
        }

        threadName = name;
    }

    public String getName() {
        return threadName;
    }

    /**
     * Return the actual Thread object in use.
     * WARNING! Do not call this until you have called start(). Even
     * then, the routine might return null unless you're calling this
     * method in the same thread as the start() method.
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * Return the actual Thread object in use.
     * <p/>
     * WARNING WARNING WARNING!
     * <p/>
     * YOU MUST CALL start() before calling this method! Otherwise
     * you'll end up in an infinite loop.
     * <p/>
     * If you have called start(), this call guarantees that the
     * returned object will not be null.
     * <p/>
     * This is the method you want to use if you are inside the run()
     * method of your PoolThread subclass and you need access to the
     * underlying Thread object. The reason for this is that
     * it's quite possible for start() to fire up the thread and get
     * swapped out before assigning the returned Thread object to the
     * thread member.
     * <p/>
     * Note that in any case where you've got the underlying Thread object,
     * it is an *extremely* bad idea to use that Thread object once your
     * run() method has exited. This typically means that you shouldn't
     * call getThread()/getThreadBalk() unless you are executing inside
     * the run() method.
     */
    public Thread getThreadBalk() {
        while (thread == null) {
            Utility.sleep(1);
        }
        return thread;
    }

    public boolean isAlive() {
        if (thread == null) {
            return false;
        }

        return thread.isAlive();
    }
}
