/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.userprops;

import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class UserPropsServiceSngl {
    private static UserPropsService instance;

    public static synchronized void set(UserPropsService service) {
        instance = service;
    }

    public static synchronized UserPropsService get() {
        if (instance == null) {
            create();
        }

        return instance;
    }

    private static void create() {
        ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(
                UserPropsConstants.SERVICE_SECTION, UserPropsConstants.SERVICE_TYPE);

        instance = new UserPropsServiceBeslImpl(cfg);
    }
}
