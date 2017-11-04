/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.image.test;

import com.enjoyf.platform.service.image.ImageServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-24 下午4:30
 * Description:
 */
public class Main {
    public static void main(String[] args) {
        try {
            ImageServiceSngl.get().removeResourceFile("/r002/image/2012/01/43/xxxxxxx.jpg");
            ImageServiceSngl.get().removeResourceFile("/r001/image/2012/01/34/yyyyyyyyyyy.jpg");
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
