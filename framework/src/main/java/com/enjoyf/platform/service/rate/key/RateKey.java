package com.enjoyf.platform.service.rate.key;

import java.io.Serializable;

import com.enjoyf.platform.service.rate.RateKeyDomain;

/**
 * @author Yin Pengyi
 */
public interface RateKey extends Serializable {

    /**
     * Gets the domain
     *
     * @return RateKeyDomain - domain of this key
     */
    public RateKeyDomain getDomain();

    /**
     * Sets the domain
     *
     * @param domain - domain of this key
     */
    public void setDomain(RateKeyDomain domain);

    /**
     * partition seed to divide up the partitions with
     *
     * @return int - partition seed
     */
    public int getPartitionSeed();
}
