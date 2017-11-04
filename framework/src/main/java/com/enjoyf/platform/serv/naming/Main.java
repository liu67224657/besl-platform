/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.naming;

import com.enjoyf.platform.serv.thrserver.MainConfig;
import com.enjoyf.platform.serv.thrserver.MainInit;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;

/**
 * This is the naming server.
 */
public class Main {

    public static void main(String[] args) {
        FiveProps servProps = Props.instance().getServProps();
        Initializer initializer = new Initializer();

        MainConfig config = new MainConfig(servProps, initializer.init(servProps));

        MainInit.instance().init(config);
        MainInit.instance().start();
    }
}
