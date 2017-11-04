package com.enjoyf.platform.service.rate;

import com.enjoyf.platform.service.rate.key.RateKey;
import com.enjoyf.platform.service.service.ServiceException;

/**
 * @author Yin Pengyi
 */
@SuppressWarnings("serial")
public class RateServiceException extends ServiceException {
	
    private RateKey rateKey;

    public static final int LIMIT_NOT_SET = ServiceException.BASE_RATE + 1;

    public static final int RATE_EXCEEDED = ServiceException.BASE_RATE + 2;

    public static final int FAST_FAIL = ServiceException.BASE_RATE + 3;

    public RateServiceException() {
    }

    public RateServiceException(int value) {
        super(value);
    }
    
    /**
     * Construct a RATE_EXCEEDED exception with the passed in RateKey
     */
    public RateServiceException(RateKey key) {
        super(RATE_EXCEEDED);
        rateKey = key;
    }

    public RateServiceException(int val, String name) {
        super(val, name);
    }

    /**
     * Return the RateKey that exceeded the rate. This method only applies
     * if this is a RATE_EXCEEDED exception.
     */
    public RateKey getKey() {
        return rateKey;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(super.toString());
        if (rateKey != null) {
            sb.append(":rateKey=" + rateKey);
        }

        return new String(sb);
    }
}
