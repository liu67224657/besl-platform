/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.image;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-21 下午6:42
 * Description:
 */
public class ImageServiceSngl {
    //
    private static ImageService instance;

    public synchronized static ImageService get() {
        if (instance == null) {
            instance = new ImageServiceMulti();
        }

        return instance;
    }
}
