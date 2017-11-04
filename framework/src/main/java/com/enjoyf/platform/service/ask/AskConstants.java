/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.ask;

import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * ask constants
 * @author <a mailto:ericliu@fivewh.com>Eric Liu</a>
 */
public class AskConstants {
    public static final String SERVICE_SECTION = "askservice";
    public static final String SERVICE_PREFIX = "askserver";
    public static final String SERVICE_TYPE = "askserver";

    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();


    static {
    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
