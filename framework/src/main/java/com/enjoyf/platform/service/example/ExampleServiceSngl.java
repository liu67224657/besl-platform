/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.service.example;

import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;

/**
 * @Auther: <a mailto:yinpengyi@enjoyf.com>Yin Pengyi</a>
 */
public class ExampleServiceSngl {
    private static ExampleService instance;

    public static synchronized void set(ExampleService service) {
        instance = service;
    }

    public static synchronized ExampleService get() {
        if (instance == null) {
            create();
        }

        return instance;
    }

    private static void create() {
        ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(ExampleConstants.SERVICE_SECTION, ExampleConstants.SERVICE_TYPE);

        instance = new ExampleServiceBeslImpl(cfg);
    }

}
