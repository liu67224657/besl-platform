package com.enjoyf.platform.serv.thrserver;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.Refresher;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThreadN;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * This class handles requests via a thread pool. Requests are queued up
 * if no current threads are available and are processed in the order
 * they were received.
 */
public class RequestHandlerQueued implements RequestHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerQueued.class);
    
    private QueueThreadN queueThreadN;
    private int timeoutMillis;
    private int maxQueueSize;
    private Refresher refresher = new Refresher(10 * 1000);

    /**
     * @param nthreads     The number of threads to use.
     * @param timeout      The timeout in msecs when we consider that
     *                     the handler is overloaded if no requests have been pulled off of
     *                     the queue within this time.
     * @param queueMaxSize In any event, regardless of the previous
     *                     setting, we don't want to let the queue get too big. This
     *                     sets a max size on the queue that if exceeded, will return true
     *                     from isOverloaded().
     * @param tag          A tag used in the thread name.
     *                     object overloaded. See isOverloaded() for more info.
     */
    public RequestHandlerQueued(int nthreads, int timeout, int queueMaxSize, String tag) {
        timeoutMillis = timeout;
        //--
        // Set and sanity check the max queue size.
        //--
        maxQueueSize = queueMaxSize;
        if (maxQueueSize < 10) {
            maxQueueSize = 10;
        }

        //--
        // A sanity check to make sure the timeout is at least a minute.
        //--
        if (timeoutMillis < 60 * 1000) {
            timeoutMillis = 60 * 1000;
        }

        queueThreadN = new QueueThreadN(
                nthreads,
                new QueueListener() {
                    public void process(Object obj) {
                        p_process((ProcRequestPack) obj);
                    }
                },
                tag
        );
    }

    private void p_process(ProcRequestPack requestPack) {
        try {
            requestPack.process();
        }
        catch (IOException ioe) {
            logger.warn("RequestHandlerQueued: i/o exception = " + ioe);
        }
        catch (Exception e) {
            GAlerter.lab("RequestHandlerQueued: exception = " + e);
        }
        catch (Error error) {
            GAlerter.lab("RequestHandlerQueued: error = " + error, error);
        }
    }

    /**
     * This will return true when we consider this object to be overloaded.
     * The following are the conditions checked:
     * If the object has not processed anything in the last T milliseconds
     * (T is passed in to the ctor), and there are objects to be processed
     * in the queue, then we consider it to be overloaded.
     * Also, if the queue is simply getting backed up, we will also
     * return true (usually if we exceed 1000 requests in the queue).
     * This latter condition typically indicates that the servers are just
     * getting too much work to do and we need to increase capacity.
     */
    public boolean isOverloaded() {
        int size = queueThreadN.size();
        //--
        // We just arbitrarily choose 10 as the limit on the queue.
        // This deals with the condition where some server is
        // frozen and is not processing any more requests.
        //--
        if (size > 10 && (System.currentTimeMillis() - queueThreadN.getLastProcessTime()) > timeoutMillis) {
            return true;
        }

        //--
        // We don't want the queue to grow indefinitely, since we will
        // run out of memory
        //--
        if (size > maxQueueSize) {
            if (refresher.shouldRefresh()) {
                GAlerter.lan("RequestHandlerQueued: " + " Server incoming queue is full! size=" + size);
            }
            return true;
        }
        return false;
    }

    public void handle(ProcRequestPack request) {
        queueThreadN.add(request);
    }

    public String toString() {
        return "maxQueueSize=" + maxQueueSize + ":timeoutMillis=" + timeoutMillis;
    }
}
