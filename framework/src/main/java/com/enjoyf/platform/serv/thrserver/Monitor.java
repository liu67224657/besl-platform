package com.enjoyf.platform.serv.thrserver;

import java.net.SocketException;
import java.text.DateFormat;

import com.enjoyf.platform.util.log.GAlerter;

public class Monitor {
	
    private final DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM);

    private SocketWrapper client = null;

    private long lastClient = Long.MIN_VALUE;
    private long lastReported = Long.MIN_VALUE;
    private long staySilent = Long.MIN_VALUE;

    public Monitor() {
        // report once an hour
        this(60 * 60 * 1000);
    }

    public Monitor(long staySilent) {
        this.staySilent = staySilent;
    }

    public void onClientConnected(SocketWrapper client) {
        this.client = client;
        lastClient = System.currentTimeMillis();
    }

    public void onExceptionCaught(SocketException exception) {
        reportException(exception);
    }

    public void reportException(SocketException exception) {
        long now = System.currentTimeMillis();

        // make sure we haven't reported anything recently
        if (lastReported > Long.MIN_VALUE && (now - lastReported) < staySilent) {
            return;
        }

        StringBuffer report = new StringBuffer();

        report.append("Exception \"");
        report.append(exception.getMessage());
        report.append("\" caught accepting connections.");

        if (lastClient > Long.MIN_VALUE) {
            report.append(" Last client connected at ");
            report.append(formatTime(lastClient));
            report.append(" from ");
            report.append(client.getLocalAddress().getHostAddress());
            report.append(".");
        }

        try {
            GAlerter.lan(report.toString());
        }
        catch (ThreadDeath td) {
            throw td;
        }
        catch (Throwable t) {
            GAlerter.lab(report.toString());
        }

        lastReported = now;
    }

    public synchronized String formatTime(long time) {
        return timeFormat.format(new java.util.Date(time));
    }

}
