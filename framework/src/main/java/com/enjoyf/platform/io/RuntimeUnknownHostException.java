/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

/**
 * RuntimeUnknownHostException is a convenience exception that
 * indicates an UnknownHostException has occured but derives from
 * RuntimeException such that manual throws clauses are not required.
 */
public class RuntimeUnknownHostException extends RuntimeException {
    private String host;

    public RuntimeUnknownHostException(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public String toString() {
        return super.toString() + ":host=" + host;
    }
}
