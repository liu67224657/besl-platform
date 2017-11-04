/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * StackTrace provides utilities to convert an Exception
 * stack trace into a String for futher processing.
 * <p/>
 * NOTE!! This class is used by applet code, so it must not use
 * any jdk features past 1.1.
 */

public class StackTrace {
    /**
     * Get stack trace out of an exception into a String. It's too
     * bad Java doesn't have (AFAIK) a more convenient way to do
     * this.
     */
    public static String getTrace(Throwable t) {
        if (t == null) {
            return "throwable is null";
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);

        t.printStackTrace(ps);
        ps.flush();
        String stackTrace = new String(out.toByteArray(), 0);
        return stackTrace;
    }

    /**
     * Return the stack trace of where you are now.
     */
    public static String here() {
        RuntimeException rt = new RuntimeException();
        rt.fillInStackTrace();
        return getTrace(rt);
    }

}
