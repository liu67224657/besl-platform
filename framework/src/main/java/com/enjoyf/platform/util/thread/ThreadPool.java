package com.enjoyf.platform.util.thread;

import java.io.PrintStream;
import java.util.Vector;

import com.enjoyf.platform.util.RateLimitedAlert;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.log.StackTrace;


/**
 * Use Java Executors instead
 * A pool of threads that are reused as needed. <P>
 * <p/>
 * Using a thread pool is preferrable to creating new threads
 * on the fly because it is more efficient, and allows greater
 * control over and knowledge about the threads in your application. <P>
 */
@Deprecated
public class ThreadPool {

    private int initialSize;
    private int maxSize;
    private Vector idleThreads;
    private Vector runningThreads;
    private boolean disabled;
    private PrintStream out = System.err;
    private String alertPrefix;
    private RateLimitedAlert rateLimitedAlert;

    private static final int ALERT_RATE_LIMIT = (60 * 15);


    /**
     * Creates an unlimited-size thread pool. <P>
     *
     * @param initialSize The initial number of threads in the pool.
     */
    public ThreadPool(int initialSize) {
        this(initialSize, 0);
    }

    /**
     * Creates a limited-size thread pool. <P>
     *
     * @param initialSize The initial number of threads in the pool.
     * @param maxSize     The maximum number of threads in the pool,
     *                    or zero to have an unlimited size pool.
     *                    Once this number of threads is reached,
     *                    calls to {@link #startThread} will return
     *                    <TT>null</TT>.
     */
    public ThreadPool(int initialSize, int maxSize) {
        this.initialSize = initialSize;
        this.maxSize = maxSize;

        idleThreads = new Vector(initialSize + 10);
        runningThreads = new Vector(initialSize + 10);
        rateLimitedAlert = new RateLimitedAlert(ALERT_RATE_LIMIT);

        for (int i = 0; (i < initialSize); i++) {
            idleThreads.addElement(new ThreadPoolThread(this));
        }
    }

    /**
     * Finalizer -- disables the thread pool. <P>
     *
     * @see #disable
     */
    public void finalize() throws Throwable {
        disable();
        super.finalize();
    }

    /**
     * Disables the thread pool. <P>
     * All idle threads in the pool are stopped, and future
     * calls to {@link #startThread} will simply create a new
     * Thread rather than using the thread pool. <P>
     */
    public synchronized void disable() {
        disabled = true;

        ThreadPoolThread t[];
        synchronized (idleThreads) {
            t = getIdleThreads();
            idleThreads.removeAllElements();
        }
        for (int i = 0; (i < t.length); i++) {
            t[i].disable();
        }
        t = getRunningThreads();
        for (int i = 0; (i < t.length); i++) {
            t[i].disable();
        }
    }


    /**
     * Returns the print stream used for error logging. <P>
     * By default, <TT>System.err</TT> is used.
     *
     * @return The print stream used for error logging.
     */
    public final PrintStream getLoggingStream() {
        return (out);
    }

    /**
     * Alters the print stream used for error logging. <P>
     * By default, <TT>System.err</TT> is used.
     *
     * @param out The print stream used for error logging.
     */
    public final void setLoggingStream(PrintStream out) {
        this.out = out;
    }

    /**
     * Returns the prefix used for alerts. <P>
     * By default, no prefix is used.
     *
     * @return The prefix used for alerts.
     */
    public final String getAlertPrefix() {
        return (alertPrefix);
    }

    /**
     * Alters the prefix used for alerts. <P>
     * By default, no prefix is used.
     *
     * @param alertPrefix The prefix used for alerts.
     */
    public final void setAlertPrefix(String alertPrefix) {
        this.alertPrefix = alertPrefix;
    }


    /**
     * Starts a new unnamed thread in the thread pool. <P>
     * If the thread pool has been disabled, the thread is
     * created outside of the pool. <P>
     *
     * @param target The run method for the thread.
     * @return The thread, or <TT>null</TT> if the
     *         pool's size limit has been reached.
     */
    public final Thread startThread(Runnable target) {
        return (startThread(target, "unnamed"));
    }

    /**
     * Starts a new named thread in the thread pool. <P>
     * If the thread pool has been disabled, the thread is
     * created outside of the pool. <P>
     *
     * @param target The run method for the thread.
     * @param name   The name for the thread.
     * @return The thread, or <TT>null</TT> if the
     *         pool's size limit has been reached.
     */
    public synchronized Thread startThread(Runnable target, String name) {
        Thread thread;

        if (disabled) {
            thread = new Thread(target, name);
            thread.setDaemon(true);
            thread.start();
        } else {
            synchronized (idleThreads) {
                int count = idleThreads.size();
                if (count > 0) {
                    thread = (Thread) idleThreads.elementAt(count - 1);
                    idleThreads.removeElementAt(count - 1);
                    ((ThreadPoolThread) thread).setTarget(target, name);
                } else if (maxSize <= 0 || runningThreads.size() < maxSize) {
                    thread = new ThreadPoolThread(this, target, name);
                } else {
                    StringBuffer sb = new StringBuffer();
                    String prefix = alertPrefix;
                    if (prefix != null) {
                        sb.append(prefix);
                        sb.append(": ");
                    }
                    sb.append("[");
                    sb.append(Thread.currentThread().getName());
                    sb.append("]: ");
                    sb.append("ThreadPool at maximum size - ");
                    sb.append(name);
                    sb.append(" thread not created.");
                    String s = new String(sb);

                    out.println("\nERROR: " + s);

                    try {
                        rateLimitedAlert.alert(s);
                    } catch (ThreadDeath xx) {
                        throw (xx);
                    } catch (Throwable xx) {
                    }

                    return (null);
                }
            }

            runningThreads.addElement(thread);
        }

        return (thread);
    }


    /**
     * Returns the number of idle threads in the pool. <P>
     *
     * @return The number of idle threads in the pool.
     */
    public final int getIdleThreadCount() {
        return (idleThreads.size());
    }

    /**
     * Returns the list of idle threads in the pool. <P>
     *
     * @return The list of idle threads in the pool.
     */
    public ThreadPoolThread[] getIdleThreads() {
        synchronized (idleThreads) {
            ThreadPoolThread t[] = new ThreadPoolThread[idleThreads.size()];
            idleThreads.copyInto(t);
            return (t);
        }
    }

    /**
     * Returns the number of running threads in the pool. <P>
     *
     * @return The number of running threads in the pool.
     */
    public final int getRunningThreadCount() {
        return (runningThreads.size());
    }

    /**
     * Returns the list of running threads in the pool. <P>
     *
     * @return The list of running threads in the pool.
     */
    public ThreadPoolThread[] getRunningThreads() {
        synchronized (runningThreads) {
            ThreadPoolThread t[] = new ThreadPoolThread[runningThreads.size()];
            runningThreads.copyInto(t);
            return (t);
        }
    }


    /**
     * Invoked when a thread in the pool finishes execution. <P>
     * Internal use only. <P>
     *
     * @param thread The thread.
     */
    synchronized void threadStopped(ThreadPoolThread thread) {
        runningThreads.removeElement(thread);

        if (disabled) {
            thread.disable();
        } else {
            synchronized (idleThreads) {
                if (idleThreads.size() < initialSize) {
                    idleThreads.addElement(thread);
                } else {
                    thread.disable();
                }
            }
        }
    }

    /**
     * Utility method to remove a thread because it is no longer in use.
     */
    synchronized void removeThread(ThreadPoolThread thread) {
        runningThreads.removeElement(thread);
        idleThreads.removeElement(thread);
    }
}


/**
 * The threads created by the ThreadPool. <P>
 * <p/>
 * Internal use only. <P>
 *
 * @author Michael Riccio
 */
class ThreadPoolThread extends Thread {
    private ThreadPool threadPool;
    private Runnable target;
    private int id;
    private boolean enabled = true;

    private static int threadId = 0;


    /**
     * Creates and starts an idle thread. <P>
     *
     * @param threadPool The pool.
     */
    ThreadPoolThread(ThreadPool threadPool) {
        this.threadPool = threadPool;
        id = getNextId();
        setName("[Idle ThreadPool Thread " + id + "]");
        setDaemon(true);
        start();
    }

    /**
     * Creates and starts an active thread. <P>
     *
     * @param threadPool The pool.
     * @param target     The run method for the thread.
     * @param name       The name of the thread.
     */
    ThreadPoolThread(ThreadPool threadPool, Runnable target, String name) {
        super(name);
        setDaemon(true);
        this.threadPool = threadPool;
        this.target = target;
        id = getNextId();
        start();
    }


    /**
     * Instructs an idle thread to become active. <P>
     *
     * @param target The run method for the thread.
     * @param name   The name of the thread.
     */
    synchronized void setTarget(Runnable target, String name) {
        setName(name);
        this.target = target;
        notify();
    }


    /**
     * Disables the thread. <P>
     * The thread will terminate as soon as it is idle. <P>
     */
    synchronized void disable() {
        enabled = false;
        notify();
    }


    /**
     * The run method for the thread. <P>
     * Automatically catches and logs all exceptions. <P>
     */
    public void run() {
        try {
            runImpl();
        }
        finally {
            this.threadPool.removeThread(this);
        }
    }

    private void runImpl() {
        while (enabled) {
            synchronized (this) {
                while (enabled && target == null) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
            }

            Runnable t = target;
            if (enabled && t != null) {
                try {
                    t.run();
                } catch (ThreadDeath x) {
                    throw (x);
                } catch (Throwable x) {
                    Utility.sleep(10000);
                    PrintStream out = threadPool.getLoggingStream();
                    out.println("\nERROR: UNCAUGHT THREAD EXCEPTION: " + x);
                    x.printStackTrace(out);
                    if (t instanceof Dumpable) {
                        ((Dumpable) t).dump(out, "");
                    }

                    StringBuffer sb = new StringBuffer();
                    String prefix = threadPool.getAlertPrefix();
                    if (prefix != null) {
                        sb.append(prefix);
                        sb.append(": ");
                    }
                    sb.append("[");
                    sb.append(getName());
                    sb.append("]: ");
                    sb.append("UNCAUGHT THREAD EXCEPTION: ");
                    String stackTrace = StackTrace.getTrace(x);
                    stackTrace = stackTrace.trim();
                    sb.append(stackTrace);

                    try {
                        GAlerter.lab(new String(sb));
                    } catch (ThreadDeath xx) {
                        throw (xx);
                    } catch (Throwable xx) {
                    }
                }

                target = null;
                setName("[Idle ThreadPool Thread " + id + "]");
                threadPool.threadStopped(this);
            }
        }
    }

    private static synchronized int getNextId() {
        return (threadId++);
    }
}
