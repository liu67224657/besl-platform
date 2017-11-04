/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.mail;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @author Yin Pengyi
 */
public class MailConstants {
    public static final byte SEND = 1;
    public static final byte SET_DELAY = 2;

    public static final String SERVICE_SECTION = "mailservice";
    public static final String SERVICE_PREFIX = "mailserver";
    public static final String SERVICE_TYPE = "mailserver";

    /**
     * Return the mappings hashtable.
     */
    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }

    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    static {
        transProfileContainer.put(new TransProfile(SEND, "SEND"));
        transProfileContainer.put(new TransProfile(SET_DELAY, "SET_DELAY"));
    }
}
