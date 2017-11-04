package com.enjoyf.platform.util.memcached;

import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-5
 * Time: 下午2:29
 * To change this template use File | Settings | File Templates.
 */
public class MemCachedServer {
    private String serverName;
    private int weight = 1;


    public MemCachedServer(String serverName, int weight) {
        this.serverName = serverName;
        this.weight = weight;
    }

    public String getServerName() {
        return serverName;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
