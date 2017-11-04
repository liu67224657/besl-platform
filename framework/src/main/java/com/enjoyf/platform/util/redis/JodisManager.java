package com.enjoyf.platform.util.redis;

import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.FiveProps;
import io.codis.jodis.JedisResourcePool;
import io.codis.jodis.RoundRobinJedisPool;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/18
 * Description:
 */
public class JodisManager {
    private FiveProps props;

    private static final String KEY_REDIS_HOST = "redis.host";
    private static final String KEY_REDIS_MAXACTIVE = "redis.maxactvie";
    private static final String KEY_REDIS_MAXWAIT = "redis.maxwait";
    private static final String KEY_REDIS_MAXIDEL = "redis.idel";


    public static final String RANGE_ORDERBY_DESC = "desc";

    public static final String RANGE_ORDERBY_ASC = "asc";

    protected JedisResourcePool pool;


    public JodisManager(FiveProps p) {
        this.props = p;
        init();
    }


    private void init() {
        pool = RoundRobinJedisPool.create().curatorClient("web001.dev:2181,web002.dev:2181", 30000).zkProxyDir("/codis3/codis-demo/jodis").build();
    }

    public JedisResourcePool getPool() {
        return pool;
    }


    public void set() throws IOException {
        Jedis jedis = pool.getResource();
        jedis.set("foo", "bar");
        String value = jedis.get("foo");
        System.out.println(value);
        jedis.close();
    }

    public static void main(String[] args) {
        try {
            JodisManager jodisManager = new JodisManager(null);
            jodisManager.set();
            jodisManager.set();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
