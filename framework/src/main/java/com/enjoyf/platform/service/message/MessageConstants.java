/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.message;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class MessageConstants {
    public static final String SERVICE_SECTION = "messageservice";
    public static final String SERVICE_PREFIX = "messageserver";
    public static final String SERVICE_TYPE = "messageserver";

    public static final String WANBA_KEY_MESSAGE_NOTICETIME = "wanba_redis_message_noticetime_";

    public static final String REDIS_KEY_WANBA_REDMESSAGE = MessageConstants.SERVICE_SECTION + "_redmessgae_";

    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    public static final byte USER_PROPS_GET = 1;

    static {
        transProfileContainer.put(new TransProfile(USER_PROPS_GET, "USER_PROPS_GET"));

    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
