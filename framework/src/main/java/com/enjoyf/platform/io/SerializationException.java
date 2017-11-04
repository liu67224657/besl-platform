package com.enjoyf.platform.io;

import java.io.InvalidClassException;

/**
 * An unchecked Exception wrapper around a java.io.InvalidClassException.
 */
@SuppressWarnings("serial")
public class SerializationException extends RuntimeException {
    

    public SerializationException(InvalidClassException e) {
        super("Cannot serialize object.", e);
    }

    public String toString() {
        return super.toString() + ":exception=" + getCause();
    }
}
