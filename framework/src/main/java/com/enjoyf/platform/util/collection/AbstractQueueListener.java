/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.collection;

import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Use this to register a listener with a QueueThread object.
 */
public abstract class AbstractQueueListener implements QueueListener {
    private static Logger logger = LoggerFactory.getLogger(AbstractQueueListener.class);

    /**
     * This is called by the QueueThread object whenever it finds
     * an object in its internal queue.
     */
    public void process(Object obj) {
        try {
            processObj(obj);
        } catch (Exception e) {
            logger.error(this.getClass().getName() + " process errror.e:,e");
        }
    }

    public abstract void processObj(Object obj);
}

