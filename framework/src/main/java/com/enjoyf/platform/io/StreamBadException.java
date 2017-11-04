/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

/**
 * A class to encapsulate a bad stream exception so that we don't
 * bug alert them in other parts of the code.
 */
@SuppressWarnings("serial")
public class StreamBadException extends RuntimeException {
	
    public StreamBadException() {
        super();
    }

    public StreamBadException(String msg) {
        super(msg);
    }
    
    public StreamBadException(String msg, Throwable cause) {
    	super(msg, cause);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("StreamBadException:");
        sb.append(super.toString());
        return sb.toString();
    }
}
