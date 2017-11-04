/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

/**
 * Implement this interface if you would like to receive the input from
 * a console object.
 */
public interface ConsoleListener {
    /**
     * This method will be invoked when a line has been read from
     * the console, a line being defined as something terminated by
     * a newline.
     */
    public void notify(String line);
}
