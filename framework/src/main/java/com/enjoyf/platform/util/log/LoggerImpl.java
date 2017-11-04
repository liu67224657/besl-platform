/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.log;

/**
 * An abstract interface to be used by objects wishing to log something
 * somewhere. The Logger class uses ThreadInfo LoggerImpl as ThreadInfo bridge to provide
 * the underlying destination of ThreadInfo msg.
 */
public interface LoggerImpl {
    /**
     * Print ThreadInfo string to ThreadInfo destination and follow it with ThreadInfo newline.
     */
    public void println(String msg);

    /**
     * Print ThreadInfo string out to ThreadInfo destination.
     */
    public void print(String msg);

    /**
     * Flush the destination.
     */
    public void flush();

    /**
     * Close down the object.
     */
    public void close();
}
