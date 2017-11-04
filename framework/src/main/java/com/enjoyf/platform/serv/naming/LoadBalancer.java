package com.enjoyf.platform.serv.naming;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.thin.DieThread;

/**
 * This class is used to perform load balancing of some abstract
 * set of objects.
 */
class LoadBalancer {
    
    private static final Logger logger = LoggerFactory.getLogger(LoadBalancer.class);
    
    private int howOftenValue;
    private int rebalanceWaitTime;
    private Listener listener;
    private CheckThread checkThread;
    private boolean closedFlag = false;

    /**
     * Ctor the object.
     *
     * @param howOften      This is how often to run the thread that
     *                      check for an out-of-balance situation. In msecs.
     * @param rebalanceWait Once we have determined that we are
     *                      overloaded, how often to wait between each balance operation.
     *                      In msecs.
     * @param l             A listener object communicating with the business logic.
     */
    public LoadBalancer(int howOften, int rebalanceWait, Listener l) {
        howOftenValue = howOften;
        rebalanceWaitTime = rebalanceWait;
        listener = l;

        checkThread = new CheckThread();
        checkThread.start();
    }

    /**
     * Must call this routine to clean up all the resources this object
     * is using.
     */
    public synchronized void close() {
        if (closedFlag) {
            return;
        }

        checkThread.die();
        closedFlag = true;
    }

    protected void finalize() {
        close();
    }

    class CheckThread extends DieThread {
        CheckThread() {
            setName("LoadBalancer.CheckThread:" + getName());
        }

        public void run() {
            while (!shouldDie()) {
                Utility.sleep(howOftenValue);
                while (true) {
                    Loadable current = listener.getCurrent();
                    if (!listener.isOverloaded(current)) {
                        break;
                    }

                    GAlerter.lab("LoadBalancer.checkThread: Overloaded!");
                    //--
                    // If we couldn't balance anybody because all of
                    // the servers are overloaded, then don't do the
                    // quick wait, instead do the long wait.
                    //--
                    if (!p_doBalance()) {
                        break;
                    }

                    Utility.sleep(rebalanceWaitTime);
                }
            }
        }
    }

    /**
     * Utility routine to perform the balance operation.
     *
     * @return Returns true if we were able to balance somebody, false
     *         if all servers were determined to be overloaded.
     */
    private boolean p_doBalance() {
        //--
        // Loop through the list of other Loadable objects finding
        // those that are *not* overloaded.
        //--
        List<Loadable> candidates = new LinkedList<Loadable>();
        Collection<Loadable> c = listener.getOthers();
        Iterator<Loadable> itr = c.iterator();
        while (itr.hasNext()) {
            Loadable other = itr.next();
            if (!listener.isOverloaded(other)) {
                logger.warn("LoadBalancer.p_doBalance: "
                        + "Found rebalance candidate: " + other);
                candidates.add(other);
            }
        }
        //--
        // If we couldn't find any, we report that to the listener
        // and we are done.
        //--
        if (candidates.size() == 0) {
            listener.allOverloaded();
            return false;
        }
        //--
        // Now find the least loaded of the bunch and report to the
        // listener that we need to balance.
        //--
        itr = candidates.iterator();
        Loadable leastLoaded = (Loadable) itr.next();

        while (itr.hasNext()) {
            Loadable other = (Loadable) itr.next();
            if (listener.compare(other, leastLoaded) < 0) {
                leastLoaded = other;
            }
        }
        listener.balance(leastLoaded);
        return true;
    }

    public static interface Listener {
        /**
         * Return the Loadable object that corresponds to current
         * object we are checking.
         */
        public Loadable getCurrent();

        /**
         * Return a collection of all other Loadable objects that
         * we want to check against.
         */
        public Collection<Loadable> getOthers();

        /**
         * Return true if the Loadable object is overloaded.
         */
        public boolean isOverloaded(Loadable l);

        /**
         * Compare two loadable objects.
         *
         * @return Return -1 if l1 is the least loaded, 0 if they
         *         have the same load, 1 if l2 is least loaded.
         */
        public int compare(Loadable l1, Loadable l2);

        /**
         * Called whenever a balancing operation is required.
         * This means that the current object is overloaded.
         *
         * @param l This is the object that we should transfer the
         *          load to.
         */
        public void balance(Loadable l);

        /**
         * This method is called when all the objects are overloaded.
         */
        public void allOverloaded();
    }

    /**
     * Just a marking interface, nothing to implement.
     */
    public static interface Loadable {
    }
}
