package com.enjoyf.platform.serv.misc;

import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-5-13
 * Time: 下午8:21
 * To change this template use File | Settings | File Templates.
 */
public class WeixinCache {
    private static final long TIME_OUT_SEC = 60l * 100l;

    private static final long ADVERTISE_TIME_OUT_SEC = 24L * 60L * 60L;

    private static final String GET_ACCESSTOKEN = "get_access_token";

    private static final String GET_TICKET = "get_ticket";

    private static final String GET_ADVERTISE = "advertise_hztx";

    private MemCachedConfig config;

    private MemCachedManager manager;

    WeixinCache(MemCachedConfig config) {
        this.config = config;
        manager = new MemCachedManager(config);
    }

    //////////////////////////////////////////////////////////
    public void putAccessToken(String appid, String accessToken) {
        manager.put(GET_ACCESSTOKEN + appid, accessToken, TIME_OUT_SEC);
    }

    public String getAccessToken(String appid) {
        Object token = manager.get(GET_ACCESSTOKEN + appid);
        if (token == null) {
            return null;
        }
        return (String) token;
    }

    public boolean deleteAccessToken(String appid) {
        return manager.remove(GET_ACCESSTOKEN + appid);
    }


    public void putAdvertiseInfo(String key, String value) {
        manager.put(GET_ADVERTISE + key, value, ADVERTISE_TIME_OUT_SEC);
    }

    public String getAdvertiseInfo(String key) {
        Object token = manager.get(GET_ADVERTISE + key);
        if (token == null) {
            return null;
        }
        return (String) token;
    }

    public boolean deleteAdvertiseInfo(String key) {
        return manager.remove(GET_ADVERTISE + key);
    }


    public void putTicket(String access_token, String ticket) {
        manager.put(GET_TICKET + access_token, ticket, TIME_OUT_SEC);
    }

    public String getTicket(String access_token) {
        Object ticket = manager.get(GET_TICKET + access_token);
        if (ticket == null) {
            return null;
        }
        return (String) ticket;
    }

    public boolean deleteTicket(String access_token) {
        return manager.remove(GET_TICKET + access_token);
    }
}
