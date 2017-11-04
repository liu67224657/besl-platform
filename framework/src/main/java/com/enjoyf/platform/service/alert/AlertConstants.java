/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.alert;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

public class AlertConstants {
    public static final String SERVICE_PREFIX = "alertserver";
    public static final String SERVICE_SECTION = "alertservice";
    public static final String SERVICE_TYPE = "alertserver";

    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    public static final byte REPORT = 1;

    static {
        transProfileContainer.put(new TransProfile(REPORT, "REPORT"));
    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
