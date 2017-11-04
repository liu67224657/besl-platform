/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

/**
 * Implement this listener object to be notified whenever a ServerThread
 * makes a connection. NOTE: The listener will be called after the
 * ConnThreadBase object has been constructed, but before it has been
 * started.
 */
public interface ConnMadeListener {
    public abstract void notify(ConnThreadBase connThread);
}
