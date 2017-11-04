/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.misc;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class MiscConstants {
    public static final String SERVICE_SECTION = "miscservice";
    public static final String SERVICE_PREFIX = "miscserver";
    public static final String SERVICE_TYPE = "miscserver";

    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    public static final byte USER_PROPS_GET = 1;

    static {
        transProfileContainer.put(new TransProfile(USER_PROPS_GET, "USER_PROPS_GET"));

    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
