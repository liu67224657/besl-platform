/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.util.Date;

import com.enjoyf.platform.util.collection.CircularQueue;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * @author Yin Pengyi
 */
class StatsCollector {
    private Data current;
    private CircularQueue oldData;
    private int period;
    private String statsFileName = DEF_STATS_FILE_NAME;

    private static final String DEF_STATS_FILE_NAME = "stats.dat";

    /**
     * @param period     This is how often to write to the stats data
     *                   file, and how long to collect stats over one period. In msecs.
     * @param numPeriods This is how many periods to keep at any one
     *                   time.
     */
    public StatsCollector(int period, int numPeriods) {
        this.period = period;
        oldData = new CircularQueue(numPeriods);
        current = new Data();
        p_readInData();
    }

    public void setFileName(String name) {
        statsFileName = name;
    }

    public synchronized void add(int totTime, int queueSize) {
        if (current.getCurrentDuration() > period) {
            current.endIt();
            if (current.m_count != 0) {
                oldData.add(current);
                p_flushData();
            }
            current = new Data();
        }
        current.add(totTime, queueSize);
    }

    /**
     * Retrieves a string containing a report.
     */
    public synchronized String getReport() {
        current.endIt();
        oldData.add(current);
        p_flushData();
        current = new Data();

        StringBuffer sb = new StringBuffer();
        Object[] arr = oldData.getObjects();
        for (int i = 0; i < arr.length; i++) {
            Data d = (Data) arr[i];
            sb.append(d.getReport());
            sb.append("*********************\n\n");
        }
        return new String(sb);
    }

    /**
     * Clears out all data.
     */
    public synchronized void clear() {
        oldData.clear();
        current = new Data();
        try {
            File f = new File(statsFileName);
            if (f.exists()) {
                f.delete();
            }
        }
        catch (Exception e) {
            GAlerter.lab("StatsCollector.clear: Could not remove stats file", e);
        }
    }

    private void p_readInData() {
        try {
            File f = new File(statsFileName);
            if (!f.exists()) {
                return;
            }

            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream is = new ObjectInputStream(fis);
            Object[] arr = (Object[]) is.readObject();
            if (arr != null) {
                for (int i = 0; i < arr.length; i++) {
                    oldData.add(arr[i]);
                }
            }
            is.close();
            fis.close();
        }
        catch (Exception e) {
            GAlerter.lan("MailServer.StatsCollector: "
                    + "Could not read in data file: " + e
                    + " (this error is not necessarily bad)");
        }
    }

    private void p_flushData() {
        try {
            FileOutputStream fos = new FileOutputStream(statsFileName);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            Object[] arr = oldData.getObjects();
            os.writeObject(arr);
            os.close();
            fos.close();
        }
        catch (Exception e) {
            GAlerter.lab("MailServer.StatsCollector: "
                    + " Could not write out mail server stats!" + e);
        }
    }

    @SuppressWarnings("serial")
	static class Data implements java.io.Serializable {
    	
        static NumberFormat numberFormat = NumberFormat.getInstance();

        static {
            numberFormat.setMaximumFractionDigits(2);
        }

        static int HOUR = 3600 * 1000;
        static int MIN30 = 1800 * 1000;
        static int MIN10 = 10 * 60 * 1000;
        static int MIN1 = 1 * 60 * 1000;
        static int SEC30 = 30 * 1000;
        static int SEC10 = 10 * 1000;
        static int SEC1 = 1000;

        int m_60min;
        int m_30min;
        int m_10min;
        int m_1min;
        int m_30sec;
        int m_10sec;
        int m_1sec;
        int m_0sec;

        long m_min = Integer.MAX_VALUE;
        long m_max = 0;

        long m_start;
        long m_end;

        long m_count;
        double m_total;
        long m_totalQueueSize = 0;
        int m_minQueueSize = Integer.MAX_VALUE;
        int m_maxQueueSize = 0;

        Data() {
            m_start = System.currentTimeMillis();
        }

        /**
         * Add an entry.
         */
        void add(int totTime, int queueSize) {
            if (queueSize < m_minQueueSize) {
                m_minQueueSize = queueSize;
            }
            if (queueSize > m_maxQueueSize) {
                m_maxQueueSize = queueSize;
            }

            m_totalQueueSize += queueSize;

            m_total += totTime;
            m_count++;
            if (totTime > HOUR) {
                m_60min++;
            } else if (totTime > MIN30) {
                m_30min++;
            } else if (totTime > MIN10) {
                m_10min++;
            } else if (totTime > MIN1) {
                m_1min++;
            } else if (totTime > SEC30) {
                m_30sec++;
            } else if (totTime > SEC10) {
                m_10sec++;
            } else if (totTime > SEC1) {
                m_1sec++;
            } else {
                m_0sec++;
            }

            if (totTime < m_min) {
                m_min = totTime;
            }

            if (totTime > m_max) {
                m_max = totTime;
            }
        }

        long getCurrentDuration() {
            return System.currentTimeMillis() - m_start;
        }

        long getDuration() {
            return m_end - m_start;
        }

        void endIt() {
            m_end = System.currentTimeMillis();
        }

        String getReport() {
            StringBuffer sb = new StringBuffer();
            sb.append("Start:\t" + new Date(m_start));
            sb.append("\n");
            sb.append("End:\t" + new Date(m_end));
            sb.append("\n");
            sb.append("Duration:\t"
                    + numberFormat.format((float) (m_end - m_start) / 1000.0 / 3600.0)
                    + " hours. ");
            sb.append("\n");
            sb.append("min/max =\t" + m_min + "/" + m_max + " in msecs.");
            sb.append("\n");
            sb.append("Total Msgs:\t" + m_count);
            sb.append("\n");
            sb.append("% T > 1 hour:\t" + p_perc(m_60min, m_count));
            sb.append("\n");
            sb.append("% 30min < T < 1hr:\t" + p_perc(m_30min, m_count));
            sb.append("\n");
            sb.append("% 10min < T < 30min:\t" + p_perc(m_10min, m_count));
            sb.append("\n");
            sb.append("% 1min < T < 10min:\t" + p_perc(m_1min, m_count));
            sb.append("\n");
            sb.append("% 30sec < T < 1min:\t" + p_perc(m_30sec, m_count));
            sb.append("\n");
            sb.append("% 10sec < T < 30sec:\t" + p_perc(m_10sec, m_count));
            sb.append("\n");
            sb.append("% 1sec < T < 10sec:\t" + p_perc(m_1sec, m_count));
            sb.append("\n");
            sb.append("% 0sec < T < 1sec:\t" + p_perc(m_0sec, m_count));
            sb.append("\n");
            sb.append("Avg time:\t"
                    + (m_count == 0 ? 0 : (int) (m_total / (double) m_count)) + " msecs");

            sb.append("\n");
            sb.append("Throughput:\t"
                    + numberFormat.format((double) m_count
                    / (double) (m_end - m_start) * 1000.0 * 60.0) + "/min");
            sb.append("\n");
            sb.append("min/max/avg queue:\t"
                    + m_minQueueSize + "/" + m_maxQueueSize + "/"
                    + numberFormat.format((double) m_totalQueueSize / (double) m_count));
            sb.append("\n");

            return new String(sb);
        }

        private String p_perc(long val, long count) {
            if (count == 0) {
                return "0.0";
            }

            return numberFormat.format((float) val / (float) count * 100.0);
        }
    }
}
