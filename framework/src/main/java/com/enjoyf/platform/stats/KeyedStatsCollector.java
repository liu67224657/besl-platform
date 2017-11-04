/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.stats;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Keeps track of averages over the last N minutes for a set of keys.
 */
public class KeyedStatsCollector {
    private HashMap m_map = new HashMap();
    private int m_windowSize;

    /**
     * Create the object with the default of a 10 minute window.
     */
    public KeyedStatsCollector() {
        this(10);
    }

    /**
     * Create the object with an N minute window.
     *
     * @param windowSize The window size in minutes.
     */
    public KeyedStatsCollector(int windowSize) {
        m_windowSize = windowSize;
    }

    /**
     * Call this every time "something" happens. The stat will be stored
     * for the passed in key. Note that the key better have well defined
     * hashCode() and equals() methods.
     *
     * @param key The key of the stat.
     */
    public synchronized void add(Object key) {
        StatsCollector c = (StatsCollector) m_map.get(key);
        if (c == null) {
            c = new StatsCollector(m_windowSize);
            m_map.put(key, c);
        }
        c.add();
    }

    /**
     * A convenience method to specify an array of objects.
     */
    public void add(Object[] keys) {
        for (int i = 0; i < keys.length; i++) {
            add(keys[i]);
        }
    }

    /**
     * A convenience method to specify a Collection of objects.
     * The caller is responsible for thread safety while this method
     * iterates over the collection.
     */
    public void add(Collection c) {
        Iterator itr = c.iterator();
        while (itr.hasNext()) {
            Object key = itr.next();
            add(key);
        }
    }

    /**
     * Retrieve the average over the specified window.
     *
     * @param windowSize The window size in minutes. Any integer
     *                   up to the specified window size specified in the ctor can
     *                   be specified. Eg, if ctor'ed with 10, you can retrieve the
     *                   average over a 1 minute window, up to a 10 minute window.
     */
    public synchronized int getAverage(Object key, int windowSize) {
        StatsCollector c = (StatsCollector) m_map.get(key);
        //--
        // We'll assume that if no key is found it doesn't mean an error,
        // rather we just don't have data for that key yet.
        //--
        if (c == null) {
            return 0;
        }

        return c.getAverage(windowSize);
    }

    /**
     * Return an iterator over the keys. Thread-safe iteration over the
     * returned object is up to the caller.
     */
    public synchronized Iterator getKeys() {
        return m_map.keySet().iterator();
    }

    static class Data {
        Object m_key;
        int[] m_averages;

        public Data(Object key, int count) {
            m_key = key;
            m_averages = new int[count];
        }

        /**
         * Get the key for the object.
         */
        public Object getKey() {
            return m_key;
        }

        /**
         * Get the average over the windows specified.
         */
        public int[] getAverages() {
            return m_averages;
        }
    }

    /**
     * Return a Collection of KeyedStats.Data objects.
     *
     * @param windows An array of windows, eg., could be 1,5,10 for
     *                1, 5, 10 minute windows.
     */
    public synchronized Collection getAverages(int[] windows) {
        LinkedList l = new LinkedList();
        Iterator itr = m_map.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry entry = (Map.Entry) itr.next();
            Object key = entry.getKey();
            StatsCollector c = (StatsCollector) entry.getValue();
            Data d = new Data(key, windows.length);
            for (int j = 0; j < windows.length; j++) {
                d.m_averages[j] = c.getAverage(windows[j]);
            }

            l.add(d);
        }
        return l;
    }

    /**
     * Return a String suitable for display containing the data.
     *
     * @param windows          The windows at which to get data.
     * @param newLineSeparated If true, each entry is separated by
     *                         a new line, if false, a colon will separate each entry. Don't
     *                         pass in a false if you have lots of keys and you want to print
     *                         the returned String.
     */
    public String getDisplayString(int[] windows, boolean newLineSeparated) {
        StringBuffer sb = new StringBuffer();
        Iterator itr = getAverages(windows).iterator();
        while (itr.hasNext()) {
            Data data = (Data) itr.next();
            sb.append(data.getKey());
            sb.append(" ");
            for (int i = 0; i < windows.length; i++) {
                sb.append(windows[i]);
                if (i != windows.length - 1) {
                    sb.append("/");
                }
            }
            sb.append("=");
            for (int i = 0; i < windows.length; i++) {
                sb.append(data.getAverages()[i]);
                if (i != windows.length - 1) {
                    sb.append("/");
                }
            }
            sb.append(newLineSeparated ? "\n" : ":");
        }
        return new String(sb);
    }
}
