/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.image;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class ImageConstants {
    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    //
    public static final byte RESOURCE_FILE_REMOVE = 1;

    //
    static {
        transProfileContainer.put(new TransProfile(RESOURCE_FILE_REMOVE, "RESOURCE_FILE_REMOVE"));
    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
