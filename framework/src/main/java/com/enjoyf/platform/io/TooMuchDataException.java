/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.IOException;

/**
 * This exception is thrown when trying to put data into a buffer,
 * but the max size of the buffer is exceeded.
 */
public class TooMuchDataException extends IOException {

    public TooMuchDataException() {
        super();
    }

    public TooMuchDataException(String s) {
        super(s);
    }
}
