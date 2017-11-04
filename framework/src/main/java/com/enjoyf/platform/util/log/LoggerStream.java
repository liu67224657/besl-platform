/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.log;

import java.io.DataOutputStream;

/**
 * A class that logs to an output stream.
 */
public class LoggerStream extends LoggerDefault {
    public LoggerStream(DataOutputStream os) {
        super(new LoggerImplStream(os));
    }
}
