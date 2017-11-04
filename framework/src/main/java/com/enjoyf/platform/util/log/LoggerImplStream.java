/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.util.log;

import java.io.DataOutputStream;
import java.io.IOException;

// Note:  The extra exception handlers at then end of every try block are
// there to render harmless bugs in Netscape's java console (particularly
// on the Mac).

/**
 * A LoggerImpl that logs to an output stream.
 */
public class LoggerImplStream implements LoggerImpl {
    protected DataOutputStream doStream;

    public LoggerImplStream(DataOutputStream os) {
        doStream = os;
    }

    public void print(String s) {
        synchronized (doStream) {
            try {
                doStream.writeBytes(s);
            } catch (IOException e) {
            } catch (Exception e) {
            }
        }
    }

    public void println(String s) {
        synchronized (doStream) {
            try {
                doStream.writeBytes(s);
                doStream.writeBytes("\n");
            } catch (IOException e) {
            } catch (Exception e) {
            }
            flush();
        }
    }

    /**
     * Flush the stream. Note that println() calls are auto-flushed,
     * but print() calls are not.
     */
    public void flush() {
        synchronized (doStream) {
            try {
                doStream.flush();
            } catch (IOException e) {
            } catch (Exception e) {
            }
        }
    }

    public void close() {
        if (doStream != null) {
            try {
                doStream.close();
            } catch (IOException e) {
            }
        }
    }
}
