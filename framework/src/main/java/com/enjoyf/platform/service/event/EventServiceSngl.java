/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.service.event;

import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public class EventServiceSngl {
    private static EventService instance;

    public static synchronized void set(EventService service) {
        instance = service;
    }

    public static synchronized EventService get() {
        if (instance == null) {
            create();
        }

        return instance;
    }

    private static void create() {
        ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(EventConstants.SERVICE_SECTION, EventConstants.SERVICE_TYPE);

        instance = new EventServiceBeslImpl(cfg);
    }

}
