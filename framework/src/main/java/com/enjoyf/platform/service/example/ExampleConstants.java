/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.example;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class ExampleConstants {
    //the service type, prefix, section.
    public static final String SERVICE_SECTION = "exampleservice";
    public static final String SERVICE_PREFIX = "exampleserver";
    public static final String SERVICE_TYPE = "exampleserver";

    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    //the apis method type. each method needs a byte here.
    public static final byte CREATE = 1;

    public static final byte GET = 11;

    public static final byte QUERY_BY_PAGE = 12;
    public static final byte QUERY_BY_RANGE = 13;

    public static final byte MODIFY = 21;

    static {
        //the following is set to show the trans log.
        transProfileContainer.put(new TransProfile(CREATE, "CREATE"));

        transProfileContainer.put(new TransProfile(GET, "GET"));

        transProfileContainer.put(new TransProfile(QUERY_BY_PAGE, "QUERY_BY_PAGE"));
        transProfileContainer.put(new TransProfile(QUERY_BY_RANGE, "QUERY_BY_RANGE"));

        transProfileContainer.put(new TransProfile(MODIFY, "MODIFY"));

    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
