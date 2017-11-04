package com.enjoyf.platform.util.thread;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * A thread used to logicProcess an incoming request. How
 * the incoming request is handled is up to the 'Processor'
 * object that is used. This is an abstract interface which can be
 * implemented in any number of ways.
 */
public class RequestThread extends PoolThread {

    private static final Logger logger = LoggerFactory.getLogger(RequestThread.class);

    private RequestThreadManager requestThreadManager;
    private boolean readyFlag = false;
    private boolean dieFlag = false;
    private Integer threadId;
    private Processor requestProcessor = null;
    private static int count = 1;

    /**
     * This interface is to be implemented by callers.
     */
    public static interface Processor {
        public abstract void process() throws IOException;
    }

    /**
     * Construct the object. The start() method will be called
     * by this ctor.
     *
     * @param rtm This is the owning request thread manager object.
     */
    public RequestThread(RequestThreadManager rtm, String prefix) {
        if (prefix == null) {
            prefix = "RequestThread";
        }

        requestThreadManager = rtm;
        threadId = new Integer(getNextId());
        setName(prefix + ":" + threadId.intValue());

        if (logger.isDebugEnabled()) {
            logger.debug("RequestThread.ctor: Creating thread = " + getName());
        }
        start();
    }

    /**
     * Retrieve the integer id for this request thread.
     */
    public Integer getId() {
        return threadId;
    }

    public void process(Processor processor) {
        //--------------------------------------------------------
        // Wait until the thread is ready before proceeding.
        // "Ready" means that the thread has started up and
        // is waiting on a condition.
        //--------------------------------------------------------

        if (logger.isDebugEnabled()) {
            logger.debug("RequestThread.logicProcess: ENTER");
        }

        while (!p_isReady()) {
            Utility.sleep(5);
        }

        requestProcessor = processor;
        synchronized (this) {
            if (logger.isDebugEnabled()) {
                logger.debug("RequestThread.logicProcess: " + "Signalling thread:  " + this.getName());
            }

            this.notify();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("RequestThread.logicProcess: EXIT");
        }

    }

    public synchronized void die() {
        dieFlag = true;
    }

    public void run() {
        //--------------------------------------------------------
        // The thread will run forever until the die() method
        // is explicitly invoked. The thread will wait on a
        // condition to tell it to proceed.
        //--------------------------------------------------------

        for (; ;) {
            if (dieFlag) {
                break;
            }

            synchronized (this) {
                p_setReady(true);

                if (logger.isDebugEnabled()) {
                    logger.debug("RequestThread.run: just before wait()");
                }

                try {
                    wait();
                }
                catch (InterruptedException e) {
                }

                if (dieFlag) {
                    break;
                }

                p_setReady(false);
            }

            try {
                requestProcessor.process();
            }
            catch (IOException ioe) {
                logger.error("RequestThread.run: Exc. while processing" + " a request", ioe);
            }
            catch (Exception e) {
                GAlerter.lab("RequestThread.run: exception: " + e, e);
            }
            catch (Error e) {
                GAlerter.lab("RequestThread.run: ERROR: " + e, e);
            }

            requestThreadManager.returnThread(this);
        }
    }

    private void p_setReady(boolean val) {
        readyFlag = val;
    }

    private synchronized boolean p_isReady() {
        return readyFlag;
    }

    /**
     * Use this to retrieve and id.
     */
    protected static synchronized int getNextId() {
        return count++;
    }
}
