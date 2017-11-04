package com.enjoyf.platform.util.log;

/**
 * This class represents some notion of an alert msg.
 */
@SuppressWarnings("serial")
public class Alert implements java.io.Serializable {
    
	private String message;
    private AlertType alertType;
    /**
     * Set with the stack trace if we have a Throwable.
     */
    private String trace;
    /**
     * Set if we have a Throwable to the exception message.
     */
    private String exceptionMessage;

    /**
     * We don't pass the Throwable across the wire. Instead, we pull out
     * what we need from it and store that.
     */
    private transient Throwable throwable;

    public Alert(AlertType type, String msg) {
        this(type, msg, null);
    }

    public Alert(AlertType type, String msg, Throwable t) {
        alertType = type;
        message = msg;
        throwable = t;
        if (t != null) {
            setThrowable(t);
        }
    }

    public String getMsg() {
        return message;
    }

    public AlertType getType() {
        return alertType;
    }

    /**
     * Returns the stack trace for a Throwable if any, otherwise returns
     * null. Note that this is *just* the stack trace.
     */
    public String getTrace() {
        return trace;
    }

    /**
     * Returns the msg associated with a Throwable if any, otherwise
     * returns null.
     */
    public String getExceptionMessage() {
        return exceptionMessage;
    }

    /**
     * Returns true if we have a Throwable in here.
     */
    public boolean hasThrowable() {
        return exceptionMessage != null;
    }

    public void setThrowable(Throwable t) {
        throwable = t;
        trace = null;
        if (t != null) {
            exceptionMessage = t.toString();
            trace = (new StackTraceInfo(t)).getStackTrace(true);
        }
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setMsg(String msg) {
        message = msg;
    }

    public String toString() {
        return alertType + ":" + message;
    }
}
