/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;

public class ConfigurePropsSngl {
    private static ConfigureProps configProps = null;

    public static synchronized ConfigureProps get() {
        if (configProps == null) {
            create();
        }

        return configProps;
    }

    private static void create() {
        FiveProps envProps = Props.instance().getEnvProps();

        configProps = new ConfigureProps(envProps);
    }
}
