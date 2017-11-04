package com.enjoyf.platform.serv.rate;

import java.util.Collection;
import java.util.Hashtable;

import com.enjoyf.platform.service.rate.key.RateKey;

/**
 * @author Yin Pengyi
 */
class PeriodTable {
	
	private Hashtable<RateKey, PeriodCount> table = new Hashtable<RateKey, PeriodCount>();
	
    /**
     * Constructs a PeriodTable
     */
    public PeriodTable() {
    }

    public boolean contains(PeriodCount value) {
        return table.contains(value);
    }

    public boolean containsValue(PeriodCount value) {
        return table.containsValue(value);
    }

    public boolean containsKey(RateKey key) {
        return table.containsKey(key);
    }

    public PeriodCount get(RateKey key) {
        return table.get(key);
    }

    public PeriodCount put(RateKey key, PeriodCount value) {
        return table.put(key, value);
    }

    public PeriodCount remove(RateKey key) {
        return table.remove(key);
    }

	public boolean isEmpty() {
		return table.isEmpty();
	}

	public Collection<PeriodCount> values() {
		return table.values();
	}
}
