/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.thread;

public class ThreadMonitorConfig {
    private int listThreadsMultiple = 1;
    private boolean listThreads = false;
    private int interval = 60;

    /**
     * Set the interval at which we want to display thread info.
     * Normally, thread info is just the # of threads.
     */
    public void setInterval(int secs) {
        interval = secs * 1000;
    }

    public int getInterval() {
        return interval / 1000;
    }

    /**
     * Use this to display all of threads (one log line per thread).
     */
    public void setListThreads(boolean val) {
        listThreads = val;
    }

    public boolean getListThreads() {
        return listThreads;
    }

    /**
     * This only applies of setListThreads(true) has been called.
     * The following value is the multiple of the interval at which
     * full thread info is displayed. Eg, if 1, full thread info is
     * displayed every getInterval() seconds. If 10, it is displayed
     * every getInterval()*10 seconds.
     */
    public void setListThreadsMultiple(int val) {
        if (val <= 0) {
            val = 1;
        }
        listThreadsMultiple = val;
    }

    public int getListThreadsMultiple() {
        return listThreadsMultiple;
    }
}
