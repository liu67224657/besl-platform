/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.event;

import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public class ActivityServiceSngl {
    private static ActivityService instance;

    public static synchronized void set(ActivityService service) {
        instance = service;
    }

    public static synchronized ActivityService get() {
        if (instance == null) {
            create();
        }

        return instance;
    }

    private static void create() {
        ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(EventConstants.SERVICE_SECTION, EventConstants.SERVICE_TYPE);

        instance = new ActivityServiceBeslImpl(cfg);
    }

}
