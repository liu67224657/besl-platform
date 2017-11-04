package com.enjoyf.platform.util.memcached;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-5
 * Time: 下午2:29
 * To change this template use File | Settings | File Templates.
 */
public class MemCachedManager {

    private MemCachedManager instance = null;

    public MemCachedClient mcc;

    public MemCachedManager(MemCachedConfig config) {

        List<MemCachedServer> serverList = config.getServerList();

        String[] servers = new String[serverList.size()];
        Integer[] weights = new Integer[serverList.size()];

        int i = 0;
        for (MemCachedServer server : serverList) {
            servers[i] = server.getServerName();
            weights[i++] = server.getWeight();
        }
        SockIOPool pool = SockIOPool.getInstance();
        // 设置服务器信息
        pool.setServers(servers);
        pool.setWeights(weights);

        // 设置初始连接数、最小和最大连接数以及最大处理时间
        pool.setInitConn(config.getInitConnection());
        pool.setMinConn(config.getMinConnection());
        pool.setMaxConn(config.getMaxConnection());
        pool.setMaxIdle(config.getMaxIdel());

        // 设置主线程的睡眠时间
        pool.setMaintSleep(config.getMaintSleep());

        // 设置TCP的参数，连接超时等
        pool.setNagle(config.isNagle());
        pool.setAliveCheck(config.isAliveCheck());
        pool.setFailover(config.isFailOver());

        pool.setSocketTO(config.getSokectTo());
        pool.setSocketConnectTO(config.getSocketConnectTO());
        pool.setHashingAlg(SockIOPool.NEW_COMPAT_HASH);

        // 初始化连接池
        pool.initialize();
        mcc = new MemCachedClient();
    }

    public MemCachedClient getMcc() {
        return mcc;
    }

    /**
     * @param key
     * @param object
     * @param timeOutSec 小于0永久有效 大于0代表key的生命周期的秒数
     */
    public void put(String key, Object object, long timeOutSec) {
        if (timeOutSec < 0l) {
            mcc.set(key, object);
        } else {
            Date date = new Date(timeOutSec * 1000);
            mcc.set(key, object, date);
        }

    }

    public long addOrIncr(String key, long value, int timeOutSec) {
        if (timeOutSec < 0l) {
            return mcc.addOrIncr(key, value);
        } else {
            return mcc.addOrIncr(key, value, timeOutSec);
        }
    }


    public Object get(String key) {
        return mcc.get(key);
    }

    public boolean remove(String key) {
        return mcc.delete(key);
    }
}
