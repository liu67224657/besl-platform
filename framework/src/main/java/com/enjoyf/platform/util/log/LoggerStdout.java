/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.log;

import java.io.DataOutputStream;

/**
 * A logger that logs to stdout.
 */
public class LoggerStdout extends LoggerStream {
    public LoggerStdout() {
        super(new DataOutputStream(System.out));
    }
}

