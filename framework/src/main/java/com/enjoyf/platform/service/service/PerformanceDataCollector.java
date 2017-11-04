/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Collects transaction data for java servers. This class is used by
 * the server side framework to grab transaction stats. But it's also
 * publicly available to be used by non-framework code.
 */
public class PerformanceDataCollector {
    /**
     * Maps a TransId object to a TransDataCollector object.
     */
    private Map transCollectorsMap = Collections.synchronizedMap(new HashMap());
    private TransDataCollector nullDataCollector = new TransDataCollector();
    private TransDataCollector allDataCollector = new TransDataCollector();

    public PerformanceDataCollector() {
    }

    /**
     * Call this every time a transaction executes.
     *
     * @param executionTime Execution time of the transaction in msecs.
     */
    public synchronized void incTrans(TransId transId, int executionTime) {
        allDataCollector.incTrans(executionTime);

        TransDataCollector collector = (TransDataCollector) transCollectorsMap.get(transId);
        if (collector == null) {
            collector = new TransDataCollector();
            transCollectorsMap.put(transId, collector);
        }

        collector.incTrans(executionTime);
    }

    /**
     * Retrieves the performance info for the sum of all transactions
     * tracked by this server. Note that a copy of the data is returned.
     */
    public PerformanceData getAllPerfData() {
        return new PerformanceData(allDataCollector);
    }

    /**
     * Retrieves info for a specific transaction tracked by this
     * object.
     */
    public TransDataCollector getTransDataCollector(TransId transId) {
        return p_getCollector(transId);
    }

    /**
     * Returns a Map of TransId->PerformanceData objects. Note that a copy
     * of the data is returned.
     */
    public synchronized HashMap getPerfDataMap() {
        Iterator itr = transCollectorsMap.entrySet().iterator();
        HashMap outMap = new HashMap();

        while (itr.hasNext()) {
            Map.Entry entry = (Map.Entry) itr.next();
            TransId transId = (TransId) entry.getKey();
            PerformanceData data = new PerformanceData(
                    (TransDataCollector) entry.getValue());

            outMap.put(transId, data);
        }
        return outMap;
    }

    public synchronized String[] getDisplay() {
        return getDisplay("");
    }

    public synchronized String[] getDisplay(String prefix) {
        ArrayList list = new ArrayList();
        StringBuffer sb = new StringBuffer();
        Iterator itr = transCollectorsMap.entrySet().iterator();

        while (itr.hasNext()) {
            Map.Entry entry = (Map.Entry) itr.next();
            TransId transId = (TransId) entry.getKey();
            PerformanceData data = new PerformanceData((TransDataCollector) entry.getValue());

            if (!data.isEmpty()) {
                sb.delete(0, sb.length());

                if (prefix != null && prefix.length() > 0) {
                    sb.append(prefix);
                    sb.append(":");
                }
                sb.append(transId.getMetricsName());
                sb.append(":");
                sb.append(data.toString());

                // Add to the array...
                list.add(sb.toString());
            }
        }

        return (String[]) list.toArray(new String[list.size()]);
    }

    private synchronized TransDataCollector p_getCollector(TransId transId) {
        TransDataCollector collector = (TransDataCollector) transCollectorsMap.get(transId);
        if (collector == null) {
            collector = nullDataCollector;
        }

        return collector;
    }
}
