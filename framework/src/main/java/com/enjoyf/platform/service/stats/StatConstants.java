/**
 * (C) 2009 Fivewh platform platform.com
 */
package com.enjoyf.platform.service.stats;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public class StatConstants {
    public static final String SERVICE_SECTION = "statsservice";
    public static final String SERVICE_PREFIX = "statsserver";
    public static final String SERVICE_TYPE = "statsserver";

    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    public static final byte REPORT_STAT = 1;
    public static final byte REPORT_STAT_BATCH = 2;
    public static final byte INCREASE_STAT = 3;

    public static final byte QUERY_STAT_BY_DOMAIN_SECTION = 11;
    public static final byte QUERY_STAT_BY_DOMAIN = 12;
    public static final byte QUERY_STAT_BY_DOMAIN_PERIOD = 13;

    public static final byte RECEIVE_PLAYER_EVENT = 127;

    static {
        transProfileContainer.put(new TransProfile(REPORT_STAT, "REPORT_STAT"));
        transProfileContainer.put(new TransProfile(REPORT_STAT_BATCH, "REPORT_BATCH_STAT"));
        transProfileContainer.put(new TransProfile(INCREASE_STAT, "INCREASE_STAT"));

        transProfileContainer.put(new TransProfile(QUERY_STAT_BY_DOMAIN_SECTION, "QUERY_STAT_BY_DOMAIN_SECTION"));
        transProfileContainer.put(new TransProfile(QUERY_STAT_BY_DOMAIN, "QUERY_STAT_BY_DOMAIN"));
        transProfileContainer.put(new TransProfile(QUERY_STAT_BY_DOMAIN_PERIOD, "QUERY_STAT_BY_DOMAIN_PERIOD"));

        transProfileContainer.put(new TransProfile(RECEIVE_PLAYER_EVENT, "RECEIVE_PLAYER_EVENT"));
    }

    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
