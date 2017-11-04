/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.advertise;

import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-6-25 下午5:51
 * Description:
 */
public class AdvertiseConstants {
    //
    public static final String SERVICE_SECTION = "advertiseservice";
    public static final String SERVICE_PREFIX = "advertiseserver";
    public static final String SERVICE_TYPE = "advertiseserver";

    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    //
    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
