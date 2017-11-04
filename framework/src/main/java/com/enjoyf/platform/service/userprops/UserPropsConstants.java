/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.userprops;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class UserPropsConstants {
    public static final String SERVICE_SECTION = "userpropsservice";
    public static final String SERVICE_PREFIX = "userpropsserver";
    public static final String SERVICE_TYPE = "userpropsserver";

    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    public static final byte USER_PROPS_GET = 1;
    public static final byte USER_PROPS_QUERY = 2;

    public static final byte USER_PROPS_SET = 11;
    public static final byte USER_PROPS_INCREASE = 12;

    public static final byte USER_PROPS_UPDATE = 13;

    public static final byte USER_PROPS_DELETE = 21;

    static {
        transProfileContainer.put(new TransProfile(USER_PROPS_GET, "USER_PROPS_GET"));
        transProfileContainer.put(new TransProfile(USER_PROPS_QUERY, "USER_PROPS_QUERY"));

        transProfileContainer.put(new TransProfile(USER_PROPS_SET, "USER_PROPS_SET"));
        transProfileContainer.put(new TransProfile(USER_PROPS_INCREASE, "USER_PROPS_INCREASE"));
        transProfileContainer.put(new TransProfile(USER_PROPS_UPDATE, "USER_PROPS_UPDATE"));



        transProfileContainer.put(new TransProfile(USER_PROPS_DELETE, "USER_PROPS_DELETE"));
    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
