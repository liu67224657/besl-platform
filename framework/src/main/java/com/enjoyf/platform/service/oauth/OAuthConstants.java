/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.oauth;

import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;

/**
 * @Auther: <a mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */
public class OAuthConstants {
    //
    public static final String SERVICE_SECTION = "oauthservice";
    public static final String SERVICE_PREFIX = "oauthserver";
    public static final String SERVICE_TYPE = "oauthserver";

    //
    private static TransProfileContainer transProfileContainer = new TransProfileContainer();

    public static final byte APP_GET = 1;
    public static final byte APP_APPLY = 2;
    public static final byte APP_MODIFY = 3;
    public static final byte APP_QUERY = 4;
    public static final byte APP_QUERY_PAGE = 5;

    public static final byte TOKEN_GET = 11;
    public static final byte TOKEN_APPLY = 12;
    public static final byte TOKEN_REMOVE = 13;
    public static final byte GENERATOR_STICKET = 15;
    public static final byte GET_STICKET = 16;

    public static final byte TOKEN_CREATE = 17;
    public static final byte TOKEN_GET_ACCESSTOKEN = 18;
    public static final byte TOKEN_GET_REFESHTOKEN = 19;
    public static final byte GENERATER_OAUTHINFO = 20;

    public static final byte SAVE_UNO_TIMESTAMP = 21;
    public static final byte GET_UNO_TIMESTAMP = 22;

    public static final byte GET_SOCIALAPI = 23;
    public static final byte REMOVE_SOCIALAPI = 24;

    public static final byte APP_GET_GAMEKEY = 25;

    public static final byte CHANNELINFO_CREATE = 26;
    public static final byte CHANNELINFO_GET = 27;
    public static final byte CHANNELINFO_QUERY = 28;
    public static final byte CHANNELINFO_UPDATE = 29;
    public static final byte CHANNELINFO_DELETE = 30;

    public static final byte CHANNELCONFIG_CREATE = 31;
    public static final byte CHANNELCONFIG_GET = 32;
    public static final byte CHANNELCONFIG_QUERY = 33;
    public static final byte CHANNELCONFIG_UPDATE = 34;
    public static final byte CHANNELCONFIG_DELETE = 35;

    static {
        //
        transProfileContainer.put(new TransProfile(APP_GET, "APP_GET"));
        transProfileContainer.put(new TransProfile(APP_APPLY, "APP_APPLY"));
        transProfileContainer.put(new TransProfile(APP_MODIFY, "APP_MODIFY"));
        transProfileContainer.put(new TransProfile(APP_QUERY, "APP_QUERY"));
        transProfileContainer.put(new TransProfile(APP_QUERY_PAGE, "APP_QUERY_PAGE"));

        //
        transProfileContainer.put(new TransProfile(TOKEN_GET, "TOKEN_GET"));
        transProfileContainer.put(new TransProfile(TOKEN_APPLY, "TOKEN_APPLY"));
        transProfileContainer.put(new TransProfile(TOKEN_REMOVE, "TOKEN_REMOVE"));

        transProfileContainer.put(new TransProfile(GENERATOR_STICKET, "GENERATOR_STICKET"));
        transProfileContainer.put(new TransProfile(GET_STICKET, "GET_STICKET"));


        transProfileContainer.put(new TransProfile(TOKEN_CREATE, "TOKEN_CREATE"));
        transProfileContainer.put(new TransProfile(TOKEN_GET_ACCESSTOKEN, "TOKEN_GET_ACCESSTOKEN"));
        transProfileContainer.put(new TransProfile(TOKEN_GET_REFESHTOKEN, "TOKEN_GET_REFESHTOKEN"));
        transProfileContainer.put(new TransProfile(GENERATER_OAUTHINFO, "GENERATER_OAUTHINFO"));

        transProfileContainer.put(new TransProfile(SAVE_UNO_TIMESTAMP, "SAVE_UNO_TIMESTAMP"));
        transProfileContainer.put(new TransProfile(GET_UNO_TIMESTAMP, "GET_UNO_TIMESTAMP"));


        transProfileContainer.put(new TransProfile(GET_SOCIALAPI, "GET_SOCIALAPI"));
        transProfileContainer.put(new TransProfile(REMOVE_SOCIALAPI, "REMOVE_SOCIALAPI"));

        transProfileContainer.put(new TransProfile(APP_GET_GAMEKEY, "APP_GET_GAMEKEY"));

        transProfileContainer.put(new TransProfile(CHANNELINFO_CREATE, "CHANNELINFO_CREATE"));
        transProfileContainer.put(new TransProfile(CHANNELINFO_GET, "CHANNELINFO_GET"));
        transProfileContainer.put(new TransProfile(CHANNELINFO_QUERY, "CHANNELINFO_QUERY"));
        transProfileContainer.put(new TransProfile(CHANNELINFO_UPDATE, "CHANNELINFO_UPDATE"));
        transProfileContainer.put(new TransProfile(CHANNELINFO_DELETE, "CHANNELINFO_DELETE"));

        transProfileContainer.put(new TransProfile(CHANNELCONFIG_CREATE, "CHANNELCONFIG_CREATE"));
        transProfileContainer.put(new TransProfile(CHANNELCONFIG_GET, "CHANNELCONFIG_GET"));
        transProfileContainer.put(new TransProfile(CHANNELCONFIG_QUERY, "CHANNELCONFIG_QUERY"));
        transProfileContainer.put(new TransProfile(CHANNELCONFIG_UPDATE, "CHANNELCONFIG_UPDATE"));
        transProfileContainer.put(new TransProfile(CHANNELCONFIG_DELETE, "CHANNELCONFIG_DELETE"));
    }


    public static TransProfileContainer getTransContainer() {
        return transProfileContainer;
    }
}
