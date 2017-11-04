/**
 * (C) 2010 Fivewh platform enjoyf.com
 */
package com.enjoyf.platform.service.viewline;

import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;

/**
 * @Auther: <a mailto:taijunli@staff.joyme.com>Li TaiJun</a>
 */
public class ViewLineServiceSngl {
    private static ViewLineService instance;

    public static synchronized void set(ViewLineService service) {
        instance = service;
    }

    public static synchronized ViewLineService get() {
        if (instance == null) {
            create();
        }

        return instance;
    }

    private static void create() {
        ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(ViewLineConstants.SERVICE_SECTION, ViewLineConstants.SERVICE_TYPE);

        instance = new ViewLineServiceBeslImpl(cfg);
    }

}
