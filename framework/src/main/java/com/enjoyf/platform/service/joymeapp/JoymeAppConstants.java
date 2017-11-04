/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @Auther: <a mailto:ericliu@fivewh.com>Eric Liu</a>
 */
public class JoymeAppConstants {
    public static final String SERVICE_SECTION = "joymeappservice";
    public static final String SERVICE_PREFIX = "joymeappserver";
    public static final String SERVICE_TYPE = "joymeappserver";

    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();


    static {
    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
