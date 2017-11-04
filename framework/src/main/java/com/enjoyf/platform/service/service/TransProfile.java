/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import java.io.Serializable;

/**
 * This class represents the profile for a transactions. What this
 * means for now is at what level severity should we log transaction
 * info.
 */
@SuppressWarnings("serial")
public class TransProfile implements TransId, Serializable {
	
    private int transId;
    private String transName;

    ///////////////////////////////////////////
    public TransProfile(int id, String name) {
        this.transId = id;
        this.transName = name;
    }

    public int getId() {
        return transId;
    }

    public String getName() {
        return transName;
    }

    public String getMetricsName() {
        return transName == null ? Integer.toString(transId) : transName;
    }

    public int hashCode() {
        return transId;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        TransProfile tp = (TransProfile) obj;
        return tp.transId == transId;
    }

    public String toString() {
        return transName + ":" + transId;
    }
}

