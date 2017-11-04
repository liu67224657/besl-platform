package com.enjoyf.platform.io;

import java.io.IOException;

/**
 * This is the exception object thrown by the Transactor methods.
 *
 * @see Transactor
 */
@SuppressWarnings("serial")
public class TtlException extends Exception {

    private int type;

    //the types.
    public static final int UNKNOWN = 0;
    public static final int CONNECT = 1;
    public static final int TIMEOUT = 4;

    /**
     * Constructs an object of the given type.
     */
    public TtlException(int type) {
        this(type, "", null);
    }

    /**
     * Constructs an object of the given type, and includes a
     * message as well.
     *
     * @param type The type.
     * @param s    The msg.
     */
    public TtlException(int type, String s) {
        this(type, s, null);
    }

    /**
     * Constructs an object of the given type, and includes a message
     * as well as an underlying IO exception object.
     *
     * @param type The type.
     * @param s    The msg.
     * @param e    The io exception encountered.
     */
    public TtlException(int type, String s, IOException e) {
        super(s, e);
        this.type = type;
    }

    /**
     * Returns true if it's a timeout exception.
     */
    public boolean isTimeout() {
        return type == TIMEOUT;
    }

    /**
     * Returns true if it's a connection exception.
     */
    public boolean isConnect() {
        return type == CONNECT;
    }

    /**
     * Returns the type of the exception.
     */
    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        //the type.
        switch (type) {
            case CONNECT:
                sb.append("CONNECT:");
                break;
            case TIMEOUT:
                sb.append("TIMEOUT:");
                break;
            case UNKNOWN:
            default:
                sb.append("UNKNOWN:");
        }

        //the message
        sb.append(super.getMessage());

        //the io exception
        if (getCause() != null) {
            sb.append("\nIOException: ");
            sb.append(getCause().getMessage());
        }

        return sb.toString();
    }
}
