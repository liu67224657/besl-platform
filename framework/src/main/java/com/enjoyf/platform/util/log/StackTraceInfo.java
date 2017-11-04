/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.log;

/**
 * StackTraceInfo is a wrapper around a throwable providing convenience
 * functions. NOTE: there is a StackTrace class in this directory with
 * similar functionality, but it is used by applet code, so it can't
 * really be changed. This implementation takes advantage of 1.4
 * functionality.
 */

public class StackTraceInfo {
    private Throwable m_throwable;

    /**
     * @param throwable The throwable we want to operate on.
     * @throws IllegalArgumentException Thrown if the passed in arg is
     *                                  null.
     */
    public StackTraceInfo(Throwable throwable) {
        if (throwable == null) {
            throw new IllegalArgumentException("throwable is null!");
        }

        m_throwable = throwable;
    }

    /**
     * @return Returns the throwable string. This is essentially the name
     *         of the Throwable concatenated with any detail message.
     */
    public String getThrowableString() {
        return m_throwable.toString();
    }

    /**
     * @return Returns the stack trace only as a String. Note that this is
     *         *just* the stack trace, not the actual throwable string.
     */
    public String getStackTrace() {
        return getStackTrace(false);
    }

    /**
     * @param insureTrace If true, then we do our best to get a stack trace
     *                    even if the Throwable doesn't have one within it. If it does, we use
     *                    that.
     * @return Returns the stack trace only as a String. Note that this is
     *         *just* the stack trace, not the actual throwable string.
     */
    public String getStackTrace(boolean insureTrace) {
        StackTraceElement[] elements = m_throwable.getStackTrace();
        if (elements.length == 0 && insureTrace) {
            //--
            // If we don't find a stack trace, the fill it in.
            //--
            m_throwable.fillInStackTrace();
            elements = m_throwable.getStackTrace();
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < elements.length; i++) {
            sb.append("\t at ");
            sb.append(elements[i].toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * @return Returns the full description of the throwable, eg:
     *         <p/>
     *         java.lang.NullPointerException
     *         at ...
     *         at ...
     *         <p/>
     *         There is a newline after the throwable name + after each trace.
     */
    public String getFullDescription() {
        return getFullDescription(false);
    }

    /**
     * @param insureTrace If true, tries to do it's best when computing
     *                    the stack trace.
     * @return This returns the full description of the stack trace and
     *         insures that we have a stack trace. The fact is that sometimes
     *         we do not get a stack trace from the input Throwable. So we do our
     *         best to try and get one.
     */
    public String getFullDescription(boolean insureTrace) {
        StringBuffer sb = new StringBuffer();
        sb.append(getThrowableString());
        sb.append("\n");
        sb.append(getStackTrace(insureTrace));
        return sb.toString();
    }
}
