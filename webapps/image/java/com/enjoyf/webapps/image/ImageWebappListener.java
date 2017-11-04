/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.webapps.image;

import com.enjoyf.webapps.image.serv.ImageServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-12-7 下午2:28
 * Description:
 */
public class ImageWebappListener implements ServletContextListener {
    //
    private static final Logger logger = LoggerFactory.getLogger(ImageWebappListener.class);

    //
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //
        logger.info("ImageWebappListener starts to contextInitialize.");

        //
        ImageServer.get().start();

        //
        logger.info("ImageWebappListener, contextInitialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //
        logger.info("ImageWebappListener, contextDestroyed.");
    }
}
