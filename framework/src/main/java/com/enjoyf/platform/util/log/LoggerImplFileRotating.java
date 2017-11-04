package com.enjoyf.platform.util.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import com.enjoyf.platform.util.thread.ThreadListener;
import com.enjoyf.platform.util.thread.ThreadNotifier;

/**
 * this is the stupidest and slowest logger class I have ever seen all my life
 */
public class LoggerImplFileRotating implements LoggerImpl, ThreadListener {
	
    long maxLength = Long.MIN_VALUE;
    PrintWriter logWriter = null;
    File liveFile = null;
    File deadFile = null;
    private LoggerStdout loggerStdout = new LoggerStdout();

    LoggerImplFileRotating(String nameBase, long maxLen, ThreadNotifier logRotator) {
        maxLength = maxLen;

        liveFile = new File(nameBase);
        deadFile = new File(nameBase + "-");

        logRotator.attachListener(this);
    }

    public synchronized void println(String message) {
        PrintWriter writer = p_getWriter();
        if (writer == null) {
            return;
        }

        writer.println(message);
    }

    public synchronized void print(String message) {
        PrintWriter writer = p_getWriter();
        if (writer == null) {
            return;
        }

        writer.print(message);
    }

    public synchronized void flush() {
        PrintWriter writer = p_getWriter();
        if (writer == null) {
            return;
        }

        writer.flush();
    }

    public synchronized void close() {
        PrintWriter writer = p_getWriter();
        if (writer == null) {
            return;
        }

        writer.close();
    }

    public synchronized void notify(ThreadNotifier logRotator) {
        p_rotateLog();
    }

    private PrintWriter p_getWriter() {
        //--
        // WARNING! This routine can't use GAlerterLogger to log messages since
        // it's possible that this logger is the GAlerterLogger logger, in which
        // case we end up in an infinite loop.
        //--
        if (logWriter == null) {
            try {
                if (liveFile.createNewFile()) {
                    loggerStdout.println(SeverityLevel.HIGH,
                            "LoggerFileRotating: created log file " +
                                    liveFile.getAbsolutePath());
                } else {
                    loggerStdout.println(SeverityLevel.HIGH,
                            "LoggerFileRotating: reusing log file " +
                                    liveFile.getAbsolutePath());
                }

                FileOutputStream fileOut = new FileOutputStream(liveFile.getAbsolutePath(), true);
                OutputStreamWriter writer = new OutputStreamWriter(fileOut, "UTF8");
                logWriter = new PrintWriter(writer, true);
            } catch (IOException x) {
                loggerStdout.println(SeverityLevel.HIGH,
                        "LoggerFileRotating: ERROR! - " +
                                "exception caught opening file " +
                                liveFile.getAbsolutePath() + ": " + x);
            }
        }

        return logWriter;
    }

    private void p_rotateLog() {
        // make sure we have something to rotate
        if (!liveFile.exists()) {
            return;
        }

        // make sure the log file is big enough
        if (liveFile.length() < maxLength) {
            return;
        }

        // make sure we don't replace the temp file
        if (deadFile.exists()) {
            loggerStdout.println(SeverityLevel.HIGH,
                    "LoggerFileRotating: ERROR! - "
                            + "dead log file " + deadFile.getAbsolutePath()
                            + " has not been picked up");
            return;
        }

        // close the stream if open
        if (logWriter != null) {
            logWriter.flush();
            logWriter.close();

            logWriter = null;
        }

        // rename the log file
        if (liveFile.renameTo(deadFile)) {
            loggerStdout.println(SeverityLevel.HIGH,
                    "LoggerFileRotating: rotated log file "
                            + liveFile.getAbsolutePath());
        } else {
            loggerStdout.println(SeverityLevel.HIGH,
                    "LoggerFileRotating: ERROR! - "
                            + "failed to rotate log file"
                            + liveFile.getAbsolutePath());
        }
    }
}
