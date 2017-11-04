/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A container for TransProfile objects.
 */
public class TransProfileContainer {
    private Map<Integer, TransProfile> transProfilesMap = Collections.synchronizedMap(new HashMap<Integer, TransProfile>());

    public TransProfileContainer() {
    }

    public synchronized TransProfile get(int transId) {
        return transProfilesMap.get(new Integer(transId));
    }

    /**
     * Will create a default one if we don't have one and add it to our
     * list.
     */
    public synchronized TransProfile getNotNull(int transId) {
        TransProfile tp = transProfilesMap.get(transId);
        if (tp == null) {
            tp = new TransProfile(transId, "UNKNOWN");
            transProfilesMap.put(transId, tp);
        }
        
        return tp;
    }

    public void put(TransProfile transProfile) {
        transProfilesMap.put(transProfile.getId(), transProfile);
    }
}
