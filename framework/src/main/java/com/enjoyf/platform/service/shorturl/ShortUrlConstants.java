/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.shorturl;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class ShortUrlConstants {
    public static final String SERVICE_SECTION = "shorturlservice";
    public static final String SERVICE_PREFIX = "shorturlserver";
    public static final String SERVICE_TYPE = "shorturlserver";

    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    public static final byte SHORTURL_GENERATE_ONE = 1;
    public static final byte SHORTURL_GENERATE_BATCH = 2;

    public static final byte SHORTURL_GET = 11;
    public static final byte SHORTURL_QUERY = 12;

    public static final byte SHORTURL_UPDATE = 21;

    public static final byte RECIEVE_EVENT = 127;


    static {
        transProfileContainer.put(new TransProfile(SHORTURL_GENERATE_ONE, "SHORTURL_GENERATE_ONE"));
        transProfileContainer.put(new TransProfile(SHORTURL_GENERATE_BATCH, "SHORTURL_GENERATE_BATCH"));

        transProfileContainer.put(new TransProfile(SHORTURL_GET, "SHORTURL_GET"));
        transProfileContainer.put(new TransProfile(SHORTURL_QUERY, "SHORTURL_QUERY"));

        transProfileContainer.put(new TransProfile(SHORTURL_UPDATE, "SHORTURL_UPDATE"));

        transProfileContainer.put(new TransProfile(RECIEVE_EVENT, "RECIEVE_EVENT"));
    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
