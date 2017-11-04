package com.enjoyf.platform.util.memcached;

import com.enjoyf.platform.util.FiveProps;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-6-5
 * Time: 下午2:29
 * To change this template use File | Settings | File Templates.
 */
public class MemCachedConfig {

    private static final String LIST_MEMCACHED_SERVERLIST = "memcached.server.list";
    private static final String KEY_MEMCACHED_WEIGHT = ".weight";
    //
    private static final String KEY_MEMCACHED_INIT_CONNECTION = "memcached.init.conection";
    private static final String KEY_MEMCACHED_MIN_CONNECTION = "memcached.min.conection";
    private static final String KEY_MEMCACHED_MAX_CONNECTION = "memcached.max.conection";
    private static final String KEY_MEMCACHED_MAX_IDEL = "memcached.max.idel";
    private static final String KEY_MEMCACHED_MAINT_SLEEP = "memcached.maint.sleep";

    private static final String KEY_MEMCACHED_NAGLE = "memcached.nagle";
    private static final String KEY_MEMCACHED_ALIVECHECK = "memcached.alivecheck";
    private static final String KEY_MEMCACHED_FAILOVER = "memcached.failover";

    private static final String KEY_MEMCACHED_SOKECTTO = "memcached.sokectto";
    private static final String KEY_MEMCACHED_SOCKETCONNECTTO = "memcached.socketconnectto";


    private ArrayList<MemCachedServer> serverList = new ArrayList<MemCachedServer>();

    private int initConnection = 5;
    private int minConnection = 5;
    private int maxConnection = 4000;
    private long maxIdel = 1000L * 60L * 60L * 6L;

    // 设置主线程的睡眠时间
    private long maintSleep = 30L;

    private boolean nagle = false;
    private boolean aliveCheck = false;
    private boolean failOver = false;

    private int sokectTo = 3000;
    private int socketConnectTO = 0;

    private FiveProps props;


    public MemCachedConfig(FiveProps p) {
        this.props = p;
        init();
    }


    private void init() {
        List<String> serverNameList = props.getList(LIST_MEMCACHED_SERVERLIST);
        for (String serverName : serverNameList) {
            int serverWeigth = props.getInt(serverName+KEY_MEMCACHED_WEIGHT, 1);
            MemCachedServer memCachedServer = new MemCachedServer(serverName, serverWeigth);
            serverList.add(memCachedServer);
        }

        initConnection = props.getInt(KEY_MEMCACHED_INIT_CONNECTION, initConnection);
        minConnection = props.getInt(KEY_MEMCACHED_MIN_CONNECTION, minConnection);
        maxConnection = props.getInt(KEY_MEMCACHED_MAX_CONNECTION, maxConnection);
        maxIdel = props.getLong(KEY_MEMCACHED_MAX_IDEL, maxIdel);
        maintSleep = props.getLong(KEY_MEMCACHED_MAINT_SLEEP, maintSleep);
        nagle = props.getBoolean(KEY_MEMCACHED_NAGLE, nagle);
        aliveCheck = props.getBoolean(KEY_MEMCACHED_ALIVECHECK, aliveCheck);
        failOver = props.getBoolean(KEY_MEMCACHED_FAILOVER, failOver);
        sokectTo = props.getInt(KEY_MEMCACHED_SOKECTTO, sokectTo);
        socketConnectTO = props.getInt(KEY_MEMCACHED_SOCKETCONNECTTO, socketConnectTO);
    }

    public ArrayList<MemCachedServer> getServerList() {
        return serverList;
    }

    public int getInitConnection() {
        return initConnection;
    }

    public int getMinConnection() {
        return minConnection;
    }

    public int getMaxConnection() {
        return maxConnection;
    }

    public long getMaxIdel() {
        return maxIdel;
    }

    public long getMaintSleep() {
        return maintSleep;
    }

    public boolean isNagle() {
        return nagle;
    }

    public boolean isAliveCheck() {
        return aliveCheck;
    }

    public boolean isFailOver() {
        return failOver;
    }

    public int getSokectTo() {
        return sokectTo;
    }

    public int getSocketConnectTO() {
        return socketConnectTO;
    }

    public FiveProps getProps() {
        return props;
    }
}
