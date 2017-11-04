package com.enjoyf.platform.service.gameres;

import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;

/**
 * Author: zhaoxin
 * Date: 11-8-25
 * Time: 下午12:51
 * Desc:
 */
public class GameResourceServiceSngl {

    private static GameResourceService instance = null;

    public static synchronized void set(GameResourceService gst) {
        GameResourceServiceSngl.instance = gst;
    }

    public static synchronized GameResourceService get() {
        if (instance == null) {
            ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(
                    GameResourceConstants.SERVICE_SECTION, GameResourceConstants.SERVICE_TYPE);

            instance = new GameResourceServiceImpl(cfg);
        }

        return instance;
    }

}
