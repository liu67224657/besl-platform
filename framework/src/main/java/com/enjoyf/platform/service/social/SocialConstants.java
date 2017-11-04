/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.social;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class SocialConstants {
    public static final String SERVICE_SECTION = "socialservice";
    public static final String SERVICE_PREFIX = "socialserver";
    public static final String SERVICE_TYPE = "socialserver";

    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    //
    public static final byte RELATION_QUERY = 1;
    public static final byte RELATION_GET = 2;
    public static final byte RELATION_BUILD = 3;
    public static final byte RELATION_BREAK = 4;

    static {
        transProfileContainer.put(new TransProfile(RELATION_QUERY, "RELATION_QUERY"));
        transProfileContainer.put(new TransProfile(RELATION_GET, "RELATION_GET"));
        transProfileContainer.put(new TransProfile(RELATION_BUILD, "RELATION_BUILD"));
        transProfileContainer.put(new TransProfile(RELATION_BREAK, "RELATION_BREAK"));

    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
