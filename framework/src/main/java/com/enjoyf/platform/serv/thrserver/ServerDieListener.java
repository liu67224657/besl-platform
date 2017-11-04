/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

/**
 * Implement this listener object to be notified whenever a ServerThread
 * object is going to die (ie, the thread is going to exit).
 */
public interface ServerDieListener {
    public abstract void notify(ServerThread st);
}
