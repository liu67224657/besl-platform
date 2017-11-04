/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

/**
 * This class encapsulates performance data for a server.
 */
public class PerformanceData implements java.io.Serializable {
    private int trans1;
    private int trans5;
    private int trans10;

    private int resp1;
    private int resp5;
    private int resp10;

    private int minTransTime;
    private int maxTransTime;
    private long total;

    public PerformanceData(TransDataCollector c) {
        trans1 = c.getThroughputAvg(1);
        trans5 = c.getThroughputAvg(5);
        trans10 = c.getThroughputAvg(10);

        resp1 = c.getRespTimeAvg(1);
        resp5 = c.getRespTimeAvg(5);
        resp10 = c.getRespTimeAvg(10);

        minTransTime = c.getMinTransTime();
        maxTransTime = c.getMaxTransTime();
        total = c.getTotal();
    }

    public int getThroughput1() {
        return trans1;
    }

    public int getThroughput5() {
        return trans5;
    }

    public int getThroughput10() {
        return trans10;
    }

    public int getRespTime1() {
        return resp1;
    }

    public int getRespTime5() {
        return resp5;
    }

    public int getRespTime10() {
        return resp10;
    }

    public boolean isEmpty() {
        return trans1 == 0 && trans5 == 0 && trans10 == 0;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(" Avg transTime at 1/5/10 min = ");
        sb.append(resp1 + "/" + resp5 + "/" + resp10);
        sb.append(": # of trans/min at 1/5/10 min = ");
        sb.append(trans1 + "/" + trans5 + "/" + trans10);
        sb.append(": min/max transTime (msecs) = ");
        sb.append(minTransTime + "/" + maxTransTime);
        sb.append(":totalTransactions = " + total);
        
        return new String(sb);
    }
}
