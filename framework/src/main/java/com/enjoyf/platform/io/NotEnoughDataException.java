/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.io;

import java.io.IOException;

/**
 * This exception is thrown when trying to read data from a non-blocking input,
 * but the requested amount of data is not yet available.
 */
public class NotEnoughDataException extends IOException {
    
    public NotEnoughDataException() {
        super();
    }

    public NotEnoughDataException(String s) {
        super(s);
    }
}
