/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

/**
 * Data class containing the stats for choosing a connection.
 */
public class ChooseStats {
    /**
     * How many times we found a conn to be "slow".
     */
    private long slowCount;

    /**
     * How many times we found a conn to be ok.
     */
    private long goodCount;

    /**
     * How many times we found a conn to be stuck, that is stuck
     * on a socketWrite() to the server side.
     */
    private long stuckCount;

    /**
     * How many times we found the conn to have timed out.
     */
    private long timeoutCount;

    ChooseStats(long good, long slow, long stuck, long timeout) {
        goodCount = good;
        slowCount = slow;
        stuckCount = stuck;
        timeoutCount = timeout;
    }

    /**
     * The percentage that the connection was found to be good when an
     * attempt to use it was made.
     */
    public int goodPercentage() {
        long sum = goodCount + slowCount + stuckCount + timeoutCount;
        if (sum == 0) {
            return 100;
        }

        return (int) ((double) (goodCount) / (double) sum * 100.0);
    }

    /**
     * Return true if the stats indicate that the conns are
     * functioning w/o any problems.
     */
    boolean isClean() {
        return goodPercentage() >= 99;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("good=" + goodCount);
        sb.append(":slow=" + slowCount);
        sb.append(":timeout=" + timeoutCount);
        sb.append(":stuck=" + stuckCount);
        return sb.toString();
    }
}
