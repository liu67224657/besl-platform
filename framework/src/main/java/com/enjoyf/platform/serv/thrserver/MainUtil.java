/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import com.enjoyf.platform.service.service.ServiceInit;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;

public class MainUtil {
    /**
     * Provides a utility method to perform the few lines of
     * initialization code. It's not necessarily useful for
     * everybody, but is fine for most cases.
     *
     * @param initializer The Initializer object that will be invoked
     *                    to perform initialization.
     */
    public static void doMain(ServiceInitializer initializer) {
        //init the server onfigure props.
        FiveProps servProps = Props.instance().getServProps();

        //
        ServiceInit.instance().init();

        //
        MainConfig mainConfig = new MainConfig(servProps, initializer.init(servProps));

        mainConfig.setInitServices(false);
        MainInit.instance().start(mainConfig);
    }
}
