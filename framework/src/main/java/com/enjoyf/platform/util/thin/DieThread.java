package com.enjoyf.platform.util.thin;

/**
 * A thread class that adds dying semantics to a thread. So eg, derive
 * your thread class from this class, and in the run() method call
 * shouldDie() at reasonable intervals to find out if someone told
 * us to go away. Note that this is in lieu of using Thread.stop()
 * to kill a thread since this feature is deprecated at 1.2. <p>
 * <p/>
 * A kill() method is provided that will call stop() if the thread does
 * not die within a specified amount of time. Sometimes the VMs in
 * pesky browsers do not wake up threads as we would like.
 * <p/>
 * NOTE: THIS CLASS IS BUILT INTO MANY GAME APPLETS. If it is changed on /main/LATEST,
 * it will trigger a re-download of these applets. It should be changed only on the applet
 * development branch, which is released to production every few months or so. If you have
 * questions, please ask Jim Greer or Mike Riccio.
 */

public class DieThread extends Thread {
    /**
     * @see java.lang.Thread
     */
    public DieThread() {
    }

    /**
     * @see java.lang.Thread
     */
    public DieThread(Runnable runnable) {
        super(runnable);
    }

    /**
     * @see java.lang.Thread
     */
    public DieThread(Runnable runnable, String name) {
        super(runnable, name);
    }

    /**
     * @see java.lang.Thread
     */
    public DieThread(ThreadGroup group, Runnable target) {
        super(group, target);
    }

    /**
     * @see java.lang.Thread
     */
    public DieThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
    }

    /**
     * @see java.lang.Thread
     */
    public DieThread(ThreadGroup group, String name) {
        super(group, name);
    }

    /**
     * @see java.lang.Thread
     */
    public DieThread(String name) {
        super(name);
    }


    /**
     * die() tells us nicely to go away.  If the current thread isn't
     * this thread, then we interrupt this thread, in the hopes that
     * it will respond.  Sadly, this doesn't always work.
     */

    public void die() {
        if (m_die) {
            return;
        }

        m_die = true;

        if (Thread.currentThread() != this) {
            try {
                interrupt();        // wake up and die!
            }
            catch (Throwable t) {
                // might get a security exception; ignore
            }
        }
    }


    /**
     * kill() is a not-so-nice way of getting rid of a thread.
     * Some VMs don't support the "nice die" semantics very well, so
     * we must improvise. <p>
     * <p/>
     * In particular, Netscape 4 will not wake up a thread with
     * interrupt() as far as I can tell. So, we could either wait for
     * them to die naturally, or call stop(). If you wait for their
     * natural death, it causes odd browser behavior upon exit. Hence,
     * the lesser of two evils seems to be to call stop() if it does
     * not die fast enough.  If your thread usage dictates otherwise,
     * just call die().
     */

    public void kill() {
        die();

        // We never want to stop ourselves. Otherwise life becomes
        // very confusing (I know this).  We also don't stop a dead thread.

        if (Thread.currentThread() != this &&
                this.isAlive()) {
            try {
                // The thread is given a chance to wake up and off itself.
                // Originally, I had a sleep() call here. However, on NS4,
                // the sleep() call would not return if stop() had been
                // called on the current thread. That would be the case if
                // we were cleaning up a child thread in our cleanup logicProcess.
                // So, that's why this is now a yield().
                yield();
                if (isAlive()) {
                    stop();        // not dead? down comes the hammer.
                }
            }
            catch (ThreadDeath e) {
                throw (e);
            }
            catch (Throwable e) {
                // Could get Security or Interrupted.
                // In that case, we don't worry about it. What could
                // we do anyway?
            }
        }


    }


    public boolean shouldDie() {
        return m_die;
    }


    private boolean m_die = false;
}
