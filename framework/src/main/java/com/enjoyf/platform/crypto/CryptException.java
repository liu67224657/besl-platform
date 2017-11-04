package com.enjoyf.platform.crypto;

/**
 * Generic exception class thrown by the crypto classes. Just print it
 * out as a string to get the message. If you get this exception, it
 * probably means there is a programmer error.
 */
public class CryptException extends Exception {
    CryptException(String msg) {
        super(msg);
    }

    CryptException(Exception e) {
        super(e.toString());
    }

    CryptException(String msg, Exception e) {
        super(msg + ": " + e.toString());
    }
}
