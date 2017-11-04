/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.collection;

/**
 * Use this to register a listener with a QueueThread object.
 */
public interface QueueListener {
    /**
     * This is called by the QueueThread object whenever it finds
     * an object in its internal queue.
     */
    public void process(Object obj);
}

