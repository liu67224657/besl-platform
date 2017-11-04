/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.db;

/**
 * A class to help us over a rough spot until we port all db code
 * to using DbRequest. The DbConnPool object was throwing a
 * RuntimeException; since we need better control, we'll define our
 * own unchecked exception.
 */
public class NoConnsException extends RuntimeException {
    public NoConnsException() {
        super("No conns available currently");
    }

    public NoConnsException(String msg) {
        super(msg);
    }
}
