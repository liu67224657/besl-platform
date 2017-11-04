package com.enjoyf.platform.service.rate;

import com.enjoyf.platform.service.rate.key.RateKey;
import com.enjoyf.platform.service.service.ServiceException;

/**
 * @author Yin Pengyi
 */
public interface RateService {
    public Rate getRate(RateKey key) throws ServiceException;

    public Rate getRate(RateLimit limit, RateKey key) throws ServiceException;

    public Rate recordCount(RateKey key) throws ServiceException;

    public Rate recordCount(RateLimit limit, RateKey key) throws ServiceException;
}
