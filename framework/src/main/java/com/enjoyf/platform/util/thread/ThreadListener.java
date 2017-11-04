/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.thread;

/**
 * ThreadListener interface should be implemented by those
 * who wish to receive notification from ThreadNotify objects.
 */
public interface ThreadListener {
    public void notify(ThreadNotifier n);
}
