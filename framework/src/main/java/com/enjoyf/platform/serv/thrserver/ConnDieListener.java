/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

/**
 * Implement this listener object to be notified whenever a ConnThreadBase
 * object is going to die (ie, the thread is going to exit). Register
 * the listener with the ConnThreadBase object, or you can register a
 * listener with the containing server object as well.
 */
public interface ConnDieListener {
    public abstract void notify(ConnThreadBase ts);
}
