/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.log;

/**
 * An abstract interface to be used by objects wishing to log alerts.
 */
public interface AlertImpl {
    /**
     * Log an alert msg with an optional Throwable as well.
     */
    public void log(Alert alert);

    /**
     * Flush the destination.
     */
    public void flush();

    /**
     * Close down the object.
     */
    public void close();
}
