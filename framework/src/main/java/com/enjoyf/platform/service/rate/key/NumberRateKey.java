package com.enjoyf.platform.service.rate.key;

import com.enjoyf.platform.service.rate.RateKeyDomain;

/**
 * @author Yin Pengyi
 */
@SuppressWarnings("serial")
public abstract class NumberRateKey implements RateKey {
	
    private RateKeyDomain rateDomain;
    private long rateId;

    public NumberRateKey(RateKeyDomain domain, long id) {
        rateDomain = domain;
        rateId = id;
    }

    public NumberRateKey(RateKeyDomain domain, int id) {
        rateDomain = domain;
        rateId = id;
    }

    public long getId() {
        return rateId;
    }

    public void setId(long id) {
        rateId = id;
    }

    public RateKeyDomain getDomain() {
        return rateDomain;
    }

    public void setDomain(RateKeyDomain domain) {
        rateDomain = domain;
    }

    public int getPartitionSeed() {
        return hashCode();
    }

    /**
     * equals override
     *
     * @param o - object to compare this to
     */
    public boolean equals(Object o) {
        // quick tests to get us out of here
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        // it's a RateKey, but it's not this one, so check it for equality
        NumberRateKey key = (NumberRateKey) o;
        return rateDomain.equals(key.getDomain()) && rateId == key.getId();
    }

    /**
     * hashCode override
     */
    public int hashCode() {
        return rateDomain.hashCode() + new Long(rateId).intValue();
    }
}
