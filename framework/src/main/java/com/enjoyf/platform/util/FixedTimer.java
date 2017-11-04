package com.enjoyf.platform.util;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.thread.DiePoolThread;

/**
 * A class to provide a timer that wakes up at a specific time.
 */
public class FixedTimer {
    
    private static final Logger logger = LoggerFactory.getLogger(FixedTimer.class);
    
    private Listener listener;

    private int sleepTime;
    private int startTime;
    private boolean recurring;
    private int period;

    private DiePoolThread thread;

    /**
     * An interface to be implemented by clients that is invoked when the
     * timer pops.
     */
    public interface Listener {
        public void timerExpired(FixedTimer t);
    }

    /**
     * @param startTime This is the start time of the timer relative
     *                  to the day. It is in HHMM form. So if 2230 for example, the
     *                  timer will pop at 22:30 of the current day *unless* we're already
     *                  past that. In which case it will pop the next day.
     * @param l         The listener to be invoked when the timer pops.
     *                  <p/>
     *                  NOTE: Must call start() once the timer has been constructed
     *                  and configured.
     */
    public FixedTimer(int startTime, Listener l) {
        this(startTime, 24 * 3600 * 1000, l);
    }

    /**
     * @param startTime This is the start time of the timer relative
     *                  to the day. It is in HHMM form. So if 2230 for example, the
     *                  timer will pop at 22:30 of the current day *unless* we're already
     *                  past that. In that case, it will pop at time T, where
     *                  T = 22:30 + N*period AND T > currentTime
     *                  N is some integer chosen so that T > currentTime.
     * @param l         The listener to be invoked when the timer pops.
     *                  <p/>
     *                  NOTE: Must call start() once the timer has been constructed
     *                  and configured.
     */
    public FixedTimer(int startTime, int period, Listener l) {
        listener = l;

        int hours = (startTime / 100) * 100;
        this.startTime = ((startTime / 100) * 3600 + (startTime - hours) * 60) * 1000;
        this.period = period;

        Calendar c = Calendar.getInstance();

        int msecs = (c.get(Calendar.HOUR_OF_DAY) * 3600
                + c.get(Calendar.MINUTE) * 60
                + c.get(Calendar.SECOND)) * 1000
                + c.get(Calendar.MILLISECOND);

        int start = this.startTime;

        logger.debug("FixedTimer: input start = " + startTime);
        logger.debug("FixedTimer: start/msecs/period = " + start + "/" + msecs + "/" + period);
        while (start < msecs) {
            start += this.period;
        }

        sleepTime = start - msecs;
        logger.debug("FixedTimer: sleepTime = " + sleepTime);
        thread = new MyThread();
    }

    /**
     * Set the name of the internal thread. Useful for debugging when
     * looking at stack traces.
     */
    public void setName(String name) {
        if (thread != null) {
            thread.setName(name);
        }
    }

    /**
     * Must call this after this object has been constructed and
     * configured.
     */
    public void start() {
        thread.start();
    }

    public boolean isAlive() {
        return thread.isAlive();
    }

    /**
     * Makes the timer execute indefinitely, at the frequency indicated
     * by the period.
     */
    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    /**
     * For a recurring timer, the object will live indefinitely unless
     * this method is called to stop processing and clean up resources.
     */
    public void die() {
        thread.die();
    }

    class MyThread extends DiePoolThread {
        MyThread() {
            MyThread.this.setName("FixedTimer:" + hashCode());
        }

        public void run() {
            logger.debug("Fixed timer thread starting..");
            try {
                p_run();
            }
            finally {
                logger.debug("FixedTimer thread dying");
            }
        }

        private void p_run() {
            while (!shouldDie()) {
                while (true) {
                    long t1 = System.currentTimeMillis();
                    Utility.sleepExc(sleepTime);
                    if (shouldDie()) {
                        break;
                    }
                    long t2 = System.currentTimeMillis();
                    int diff = (int) (t2 - t1);
                    if (diff > sleepTime - 1000) {
                        break;
                    }

                    sleepTime -= diff;
                }
                if (shouldDie()) {
                    break;
                }

                long t1 = System.currentTimeMillis();
                try {
                    listener.timerExpired(FixedTimer.this);
                }
                catch (Exception e) {
                    GAlerter.lab("FixedTimer.timerExpired caught exc: " + e, e);
                }
                long t2 = System.currentTimeMillis();

                if (!recurring) {
                    break;
                }

                sleepTime = period - (int) (t2 - t1);
                if (sleepTime < 0) {
                    logger.debug("FixedTimer: Timer went negative, "
                            + "resetting sleep time to period: " + period);
                    sleepTime = period;
                }
            }
        }
    }
}
