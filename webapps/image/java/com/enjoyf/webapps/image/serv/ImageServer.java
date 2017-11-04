/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.webapps.image.serv;

import com.enjoyf.platform.serv.thrserver.MainConfig;
import com.enjoyf.platform.serv.thrserver.MainInit;
import com.enjoyf.platform.serv.thrserver.ServerWrap;
import com.enjoyf.platform.serv.thrserver.ServiceInitializer;
import com.enjoyf.platform.service.service.ServiceInit;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-1-18 下午3:37
 * Description:
 */
public class ImageServer {
    //
    private static final Logger logger = LoggerFactory.getLogger(ServerWrap.class);

    //
    private static ImageServer instance;
    private boolean started = false;

    //
    private MainConfig mainConfig;

    private ImageServer() {
    }

    public static synchronized ImageServer get() {
        if (instance == null) {
            instance = new ImageServer();
        }

        return instance;
    }

    public void start() {
        logger.info("ImageServer start to start.");

        if (started) {
            logger.info("ImageServer is started already");
        } else {
            //init the server onfigure props.
            FiveProps servProps = Props.instance().getServProps();

            //
            ServiceInit.instance().init();
            ServiceInitializer initializer = new ImageServerInitializer();

            //
            mainConfig = new MainConfig(servProps, initializer.init(servProps));

            mainConfig.setInitServices(false);
            MainInit.instance().start(mainConfig);

            started = true;
        }

        logger.info("ImageServer started.");
    }
}
