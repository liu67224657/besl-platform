/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.notice;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class NoticeConstants {
    public static final String SERVICE_SECTION = "noticeservice";
    public static final String SERVICE_PREFIX = "noticeserver";
    public static final String SERVICE_TYPE = "noticeserver";

    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    static {

    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
