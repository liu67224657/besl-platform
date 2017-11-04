/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.log;

import com.enjoyf.platform.util.thread.ThreadNotifier;

/**
 * A Logger that logs to a rotating file.
 */

public class LoggerFileRotating extends LoggerDefault {

    public LoggerFileRotating(String nameBase, long maxLength, ThreadNotifier logRotator) {
        super(new LoggerImplFileRotating(nameBase, maxLength, logRotator));
    }
}
