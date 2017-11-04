/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.rate;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @author Yin Pengyi
 */
public class RateConstants {
    public static final String SERVICE_SECTION = "rateservice";
    public static final String SERVICE_PREFIX = "rateserver";
    public static final String SERVICE_TYPE = "rateserver";

    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    public static final byte GET_RATE = 1;
    public static final byte RECORD_COUNT = 2;

    static {
        transProfileContainer.put(new TransProfile(GET_RATE, "GET_RATE"));
        transProfileContainer.put(new TransProfile(RECORD_COUNT, "RECORD_COUNT"));
    }

    /**
     * Return the TransProfile container.
     */
    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
