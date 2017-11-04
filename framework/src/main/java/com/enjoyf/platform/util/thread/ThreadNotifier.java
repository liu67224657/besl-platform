/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.thread;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.enjoyf.platform.util.FixedTimer;

/**
 * ThreadNotifier objects are threads capable of notifying
 * ThreadListener objects at specified time intervals.
 */
public class ThreadNotifier {
    private Collection<ThreadListener> listeners = new Vector<ThreadListener>();
    private final Object listenLock = new Object();
    private FixedTimer fixedTimer;

    public ThreadNotifier(int sleep) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        init(hour * 100 + min - 1, sleep);
    }

    /**
     * Use this static ctor to create a ThreadNotifier that uses a
     * FixedTimer object.
     *
     * @param startTime The time we wish to pop the timer. It's an integer
     *                  in HHMM form (eg, 2300 means it pops at 11pm).
     * @param period    How often to pop the timer from the start time. This
     *                  is in msecs. So if 24*3600*1000, it pops once a day.
     */
    public ThreadNotifier(int startTime, int period) {
        init(startTime, period);
    }

    private void init(int startTime, int period) {
        fixedTimer = new FixedTimer(startTime, period,
                new FixedTimer.Listener() {
                    public void timerExpired(FixedTimer ft) {
                        notifyListeners();
                    }
                });

        fixedTimer.setRecurring(true);
    }

    public ThreadNotifier() {
        this(0);
    }

    public void start() {
        fixedTimer.start();
    }

    public boolean isAlive() {
        return fixedTimer.isAlive();
    }

    public void setName(String name) {
        fixedTimer.setName(name);
    }

    public void attachListener(ThreadListener l) {
        synchronized (listenLock) {
            listeners.add(l);
        }
    }

    public void detachListener(ThreadListener l) {
        synchronized (listenLock) {
            listeners.remove(l);
        }
    }

    public void clearListeners() {
        synchronized (listenLock) {
            listeners.clear();
        }
    }

    public void die() {
        fixedTimer.die();
    }

    private void notifyListeners() {
        Collection<ThreadListener> listeners = null;

        synchronized (listenLock) {
            listeners = new Vector<ThreadListener>(this.listeners);
        }

        for (Iterator<ThreadListener> i = listeners.iterator(); i.hasNext();) {
            i.next().notify(this);
        }
    }
}
