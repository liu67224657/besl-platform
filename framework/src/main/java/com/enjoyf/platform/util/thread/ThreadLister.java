/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.thread;

/**
 * Generic class to list Threads in the VM.
 */
public class ThreadLister {
    protected ThreadLister() {
    }

    /**
     * List all Threads and ThreadGroups in the current JVM.
     *
     * @return A String list of the threads.
     */
    public static String list() {
        ThreadGroup g = Thread.currentThread().getThreadGroup();

        while (g.getParent() != null) {
            g = g.getParent();
        }
        return listGroup(g, "");
    }

    /**
     * Lists everything in the threadGroup
     *
     * @param g      The ThreadGroup to list
     * @param indent Indention to use for printing the information
     * @return String representation of this ThreadGroup
     */
    public static String listGroup(ThreadGroup g, String indent) {
        if (g == null) {
            return "";
        }

        int numThreads = g.activeCount();
        int numGroups = g.activeGroupCount();
        Thread[] threads = new Thread[numThreads];
        ThreadGroup[] groups = new ThreadGroup[numGroups];

        g.enumerate(threads, false);
        g.enumerate(groups, false);

        StringBuffer s = new StringBuffer();
        s.append(indent
                + "Thread Group: " + g.getName()
                + " Max Priority: " + g.getMaxPriority()
                + (g.isDaemon() ? "Daemon" : "")
                + "\n");

        for (int i = 0; i < numThreads; i++) {
            s.append(listThread(threads[i], indent + "  "));
        }
        for (int i = 0; i < numGroups; i++) {
            s.append(listGroup(groups[i], indent + "  "));
        }

        s.append("\n");
        return s.toString();
    }

    /**
     * Lists everything in the Thread
     *
     * @param t      The Thread to list
     * @param indent Indention to use for printing the information
     * @return String representation of this Thread
     */
    public static String listThread(Thread t, String indent) {
        if (t == null) {
            return "";
        }

        StringBuffer s = new StringBuffer();
        s.append(indent
                + "Thread: " + t.getName()
                + " Priority: " + t.getPriority()
                + (t.isDaemon() ? " Daemon" : "")
                + (t.isAlive() ? " Alive" : " Not Alive")
                + "\n");

        return s.toString();
    }

    public static Thread[] getCurrentThreads() {
        ThreadGroup g = Thread.currentThread().getThreadGroup();

        while (g.getParent() != null) {
            g = g.getParent();
        }
        int numThreads = g.activeCount();
        Thread[] threads = new Thread[numThreads];
        int realCount = g.enumerate(threads, true);
        Thread[] realThreads = threads;
        if (realCount != numThreads) {
            realThreads = new Thread[realCount];
            System.arraycopy(threads, 0, realThreads, 0, realCount);
        }
        return realThreads;
    }
}
