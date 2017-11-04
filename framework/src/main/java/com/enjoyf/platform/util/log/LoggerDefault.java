/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.log;

import java.util.Date;

import com.enjoyf.platform.util.collection.CircularQueue;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThread;

/**
 * This is essentially an abstract class that implements most
 * of the functionality that the Logger interface requires (as well
 * as some utility functions).
 * <p/>
 * The one missing object is a LoggerImpl which is the final
 * destination of a message.
 * <p/>
 * Subclasses should provide the missing LoggerImpl object.
 */
public abstract class LoggerDefault {
    private SeverityLevel severityLevel = SeverityLevel.HIGH;

    private boolean useTimestampFlag = true;
    private boolean printThreadInfoFlag = true;
    protected LoggerImpl loggerImpl = null;
    private String timeStampFormat = null;

    //this is used to add buffer when write log to files
    private QueueThread queueThread = null;

    /**
     * This class is essentially abstract. Derived classes will
     * just ctor the right kind of LoggerImpl object and pass it
     * in here.
     */
    protected LoggerDefault(LoggerImpl impl) {
        loggerImpl = impl;

        queueThread = new QueueThread(
                new QueueListener() {
                    public void process(Object obj) {
                        loggerImpl.println((String) obj);
                    }
                },
                new CircularQueue(100)
        );
    }

    /**
     * Set the severity for this logger object. Only messages
     * with <= the set severity will be printed.
     *
     * @param l The severity.
     */
    public void setSeverity(SeverityLevel l) {
        severityLevel = l;
    }

    /**
     * Set the severity from a String: "low", "medium", "high", etc.
     */
    public void setSeverity(String sev) {
        setSeverity(SeverityLevel.getByName(sev));
    }

    /**
     * Return the severity that this logger object is currently set to.
     */
    public SeverityLevel getSeverity() {
        return severityLevel;
    }

    /**
     * Enable/disable time stamping of messages. By default,
     * it is disabled.
     */
    public void setUseTimestamp(boolean val) {
        useTimestampFlag = val;
    }

    /**
     * Set the length of a prepended timestamp.
     * Possible values a "SHORT", "MEDIUM", "LONG", "FULL", "MILLIS"
     * Setting to "MILLIS" produces a short date & time stamp
     * containing milliseconds, all other values result in a stamp
     * as formatted by a corresponding DateFormat.
     * If an invalid value or none specified, Date.toString() should be used.
     */
    public void setTimeStampLength(String len) {
        timeStampFormat = len;
    }

    /**
     * Retrieve the length of a prepended timestamp.
     */
    public String getTimeStampLength() {
        return timeStampFormat;
    }

    public void setPrintThreadInfo(boolean val) {
        printThreadInfoFlag = val;
    }

    /**
     * Return true if timestamping is enabled.
     */
    public boolean isUseTimestamp() {
        return useTimestampFlag;
    }

    /**
     * Use this method in derived classes as a utility method to
     * put together a basic output string. Basically the string
     * returned will include timestamp as well as thread info
     * prepended to the input param.
     *
     * @param s This is the string we want to print out.
     */
    protected String p_getPrintString(String s) {
        //--
        // Profiling suggests that significant amount of time is spent
        // in StringBuffer.expandCapacity() within this function. Let's try
        // to allocate the right internal buffer size right off the bat.
        //--
        String threadName = Thread.currentThread().getName();
        int length = s == null ? 5 : s.length();
        if (isUseTimestamp()) {
            length += 25;
        }
        if (printThreadInfoFlag) {
            length += threadName.length() + 4;
        }

        StringBuffer sb = new StringBuffer(length);
        if (isUseTimestamp()) {
            sb.append(p_getTimestamp());
            sb.append(": ");
        }
        if (printThreadInfoFlag) {
            sb.append("[");
            sb.append(threadName);
            sb.append("]: ");
        }
        sb.append(s);
        return new String(sb);
    }

    /**
     * Return a timestamp.
     */
    protected String p_getTimestamp() {
    	return new Date().toString();
    }

    /**
     * The method used to print something.
     *
     * @param severity The severity of this message.
     * @param s        The string to print.
     */
    public void print(SeverityLevel severity, String s) {
        if (!checkSeverity(severity)) {
            return;
        }

        String ps = p_getPrintString(s);
        if (loggerImpl != null) {
            loggerImpl.print(ps);
        }
    }

    /**
     * The following will end the print command with a newline.
     */
    public void println(SeverityLevel severity, String s) {
        if (!checkSeverity(severity)) {
            return;
        }

        String ps = p_getPrintString(s);

        //add buffer here, so there is NOT waiting for return;
        if (loggerImpl != null) {
            loggerImpl.println(ps);
        }
    }

    public void flush() {
        if (loggerImpl != null) {
            loggerImpl.flush();
        }
    }

    /**
     * The following is used to check the severity of a message
     * to the currently set severity.
     *
     * @return Returns 'true' if the message should be printed.
     */
    protected boolean checkSeverity(SeverityLevel s) {
        return s.getLevel() <= severityLevel.getLevel();
    }

    /**
     * Prints the stack trace of a Throwable. We need a helper routine
     * because Loggers are not PrintStreams.
     */
    public void printStackTrace(SeverityLevel severity, Throwable t) {
        println(severity, StackTrace.getTrace(t));
    }

    public void close() {
        if (loggerImpl != null) {
            loggerImpl.close();
        }
    }
}
