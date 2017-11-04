/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.thread;

/**
 * A class that adds dying semantics to a Thread, and that uses
 * a pool thread. Use this if you're used to using DieThread and
 * want to convert to using threads from a pool.
 * <p/>
 * So eg, derive class from this class, and in the run() method call
 * shouldDie() at reasonable intervals to find out if someone told
 * us to go away.
 */

public abstract class DiePoolThread extends PoolThread {
    private boolean dieFlag = false;

    /**
     * die() tells us nicely to go away.  If the current thread isn't
     * this thread, then we interrupt this thread, in the hopes that
     * it will respond.  Sadly, this doesn't always work.
     */
    public void die() {
        die(false);
    }

    /**
     * A version of die() that takes an arg telling it to use the
     * deprecated Thread.stop() call on the underlying thread. This
     * is to work around a bug in the jvm.
     */
    public void die(boolean hardStop) {
        if (dieFlag) {
            return;
        }

        dieFlag = true;

        if (thread != null && Thread.currentThread() != thread) {
            try {
                thread.interrupt();        // wake up and die!
                if (hardStop) {
                    thread.stop();
                }
            }
            catch (Throwable t) {
                // might get a security exception; ignore
            }
        }
    }

    public boolean shouldDie() {
        return dieFlag;
    }
}
