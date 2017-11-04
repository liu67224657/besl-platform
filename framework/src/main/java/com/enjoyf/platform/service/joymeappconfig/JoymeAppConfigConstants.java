/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.joymeappconfig;

import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @Auther: <a mailto:ericliu@fivewh.com>Eric Liu</a>
 */
public class JoymeAppConfigConstants {
    public static final String SERVICE_SECTION = "joymeappconfigservice";
    public static final String SERVICE_PREFIX = "joymeappconfigserver";
    public static final String SERVICE_TYPE = "joymeappconfigserver";

    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();


    static {
    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
